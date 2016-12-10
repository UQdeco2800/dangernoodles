package uq.deco2800.dangernoodles.systems.weapons;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uq.deco2800.dangernoodles.AudioManager;
import uq.deco2800.dangernoodles.components.AIComponent;
import uq.deco2800.dangernoodles.components.CollisionComponent;
import uq.deco2800.dangernoodles.components.CursorComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.components.damages.InstantDamageComponent;
import uq.deco2800.dangernoodles.components.noodles.NoodleComponent;
import uq.deco2800.dangernoodles.components.stats.ManaComponent;
import uq.deco2800.dangernoodles.components.weapons.WeaponComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.ProjectileEntities;
import uq.deco2800.dangernoodles.weapons.ProjectileDefinition;
import uq.deco2800.dangernoodles.weapons.WeaponDefinition;

/**
 * Will handle the logic around different weapons including: - The firing of
 * weapons (of all different types) - The creation/deletion of projectiles - The
 * choosing of which sprite to draw for each weapon (i.e. aiming, firing, fired
 * & left or right)
 * 
 * @author danyon
 *
 */
public class WeaponSystem implements System {

    private static String thisClass = WeaponSystem.class.getName();
    private static final double MANA_CONSTANT = 50;
    private final Logger logger = LoggerFactory.getLogger(thisClass);

    public static final boolean isTesting = false; // If true, doesn't play sound


    @Override
    public void run(World world, double t, double dt) {
        List<WeaponComponent> weapons = world
                .getComponents(WeaponComponent.class);

        // Make sure there are some weapons
        if (weapons == null || weapons.isEmpty())
            return;

        for (WeaponComponent weapon : weapons) {
            Optional<PositionComponent> parentPositionOpt = world
                    .getComponent(weapon.getParent(), PositionComponent.class);
            Optional<SpriteComponent> spriteOpt = world
                    .getComponent(weapon.getEntity(), SpriteComponent.class);
            Optional<SpriteComponent> parentSpriteOpt = world
                    .getComponent(weapon.getParent(), SpriteComponent.class);
            Optional<PositionComponent> positionOpt = world
                    .getComponent(weapon.getEntity(), PositionComponent.class);
            Optional<TurnComponent> turnOpt = world
                    .getComponent(weapon.getParent(), TurnComponent.class);

            if (!parentPositionOpt.isPresent() || !spriteOpt.isPresent()
                    || !positionOpt.isPresent() || !turnOpt.isPresent()) {
                logger.warn("Doesn't have all components");
                continue;
            }

            PositionComponent parentPos = parentPositionOpt.get();
            PositionComponent position = positionOpt.get();
            SpriteComponent parentSprite = parentSpriteOpt.get();
            SpriteComponent sprite = spriteOpt.get();
            CursorComponent cursor = world.getComponents(CursorComponent.class)
                    .get(0);
            PositionComponent cursorPosition = world
                    .getComponent(cursor.getEntity(), PositionComponent.class)
                    .get();
            AIComponent ai = world
                    .getComponent(weapon.getParent(), AIComponent.class)
                    .orElse(null);
            TurnComponent turn = turnOpt.get();

            if (!turn.getTurn()) {
                sprite.setRender(false);
                continue;
            }

            // make sure we're drawing the sprite
            sprite.setRender(true);

            handleSpritePosition(position, parentPos, sprite, parentSprite);

            boolean dontFire = setRotationAndFlipping(position, cursorPosition,
                    weapon, ai, sprite);

            if (!dontFire) {
                handleFiring(ai, weapon, cursor, world);
            }
        }
    }

    /**
     * Sets the rotation and flipping for the weapon's Sprite and Weapon
     * Components
     * 
     * @param position
     *            The PositionComponent of the weapon
     * @param cursorPosition
     *            The PositionComponent of the cursor
     * @param weapon
     *            The weapon's WeaponComponent
     * @param ai
     *            The AIComponent associated with the player (null if
     *            user-player)
     * @param sprite
     *            The SpriteComponent associated with the weapon
     * @return Whether or not the weapon is allowed to fire based on the
     *         cursor's position
     */
    private boolean setRotationAndFlipping(PositionComponent position,
            PositionComponent cursorPosition, WeaponComponent weapon,
            AIComponent ai, SpriteComponent sprite) {
        double rotation;
        boolean dontFire = false;

        if (ai == null) {
            // it's a human player, so rotate based on mouse pos
            double dx;
            double dy;
            dx = cursorPosition.getX() - position.getX();
            dy = cursorPosition.getY() - position.getY();

            rotation = Math.atan2(dy, dx);
            // rotation is in rads, needs to be degs
            rotation = rotation * 180 / Math.PI; // convert to deg

            // Only allow players to fire if there're not right on top of
            // the player
            dontFire = Math.sqrt(dx * dx + dy * dy) <= 45;
        } else {
            // it's an AI player, so rotate based on weaponComponent vals
            rotation = weapon.getDirection();
        }

        weapon.setDirection(rotation); // set the actual direction of firing

        if (Math.abs(rotation) > 90) {
            // Needs to be flipped
            sprite.setFlipped(true);
            // Multiplier is +/- 1
            double multiplier = rotation / Math.abs(rotation);
            // Get the reflection of angle in quadrant 1
            rotation = 180 - Math.abs(rotation);
            // reflect to quadrant 4 if required
            rotation *= multiplier;
        } else {
            sprite.setFlipped(false);
        }

        sprite.setRotation(rotation);

        return dontFire;
    }

    /**
     * Sets the position and pivots of the weapon's sprite based on the position
     * and size of its parent noodle
     * 
     * @param position
     *            The PositionComponent associated with the weapon
     * @param parentPos
     *            The PositionComponent associated with the weapon's
     *            noodle-owner
     * @param sprite
     *            The SpriteComponent associated with the weapon
     * @param parentSprite
     *            The SpriteComponent associated with the weapon's noodle-owner
     */
    private void handleSpritePosition(PositionComponent position,
            PositionComponent parentPos, SpriteComponent sprite,
            SpriteComponent parentSprite) {
        // move the weapon to the correct position (based on the
        // position of its parent)
        position.setX(parentPos.getX() + parentSprite.getWidth() / 2);
        position.setY(parentPos.getY() + parentSprite.getHeight() / 2);

        // Make sure the pivot is halfway down the left side of the weapon
        sprite.setPivotX(0);
        sprite.setPivotY(sprite.getHeight() / 2);
    }

    /**
     * Handles the logic involved with the firing of the weapon
     * 
     * @param ai
     *            The AIComponent associated with the player firing (can be
     *            null)
     * @param weapon
     *            The WeaponComponent associated with the player firing
     * @param cursor
     *            The CursorComponent associated with the player firing
     * @param world
     *            The World in which the weapon exists
     */
    private void handleFiring(AIComponent ai, WeaponComponent weapon,
            CursorComponent cursor, World world) {
        // only keep going if you've got enough mana to fire
        if (!enoughManaToFire(weapon, world)) {
            return;
        }
        WeaponDefinition wdef = weapon.getDefinition();
        // Get whether the player is firing
        if (ai == null) {
            weapon.setFiring(cursor.isClicked());
        }

        if (weapon.isFiring()) {
            weapon.incrementPower(2);
        }

        // Check if the weapon has been fired
        if (weapon.isFired()) {
            // if it's not a powered weapon, just shoot with set power
            if (!wdef.isPowered()) {
                weapon.setPower(80);
            }
            weaponSoundPlayer(weapon);
            weapon.setFired(false);
            fireWeapon(weapon, world);
            // update the mana
            updateManaFromFire(weapon, world);
        }
    }

    /**
     * Does the actual firing of the weapon (i.e. creates the projectiles)
     * 
     * @param weapon
     *            the weapon to be fired
     * @param world
     *            the world in which to fire the weapon
     */
    private void fireWeapon(WeaponComponent weapon, World world) {
        // fire the weapon
        WeaponDefinition def = weapon.getDefinition();
        switch (def.getCategory()) {
        case "Projectile":
        case "Guns":
        case "Thrown":
            Entity proj = ProjectileEntities.createProjectile(world, weapon);
            // Ignore the parent
            CollisionComponent projCollision = world
                    .getComponent(proj, CollisionComponent.class).get();
            Entity parent = weapon.getParent();
            projCollision.addIgnored(parent);
            ProjectileEntities.push(proj, world, weapon.getPower(),
                    weapon.getDirection());
            break;
        case "Melee":
            // get max/min x/y co-oridinates to search
            double weaponRotation = weapon.getDirection();
            double weaponHeight = world
                    .getComponent(weapon.getEntity(), SpriteComponent.class)
                    .get().getHeight();
            double weaponWidth = world
                    .getComponent(weapon.getEntity(), SpriteComponent.class)
                    .get().getWidth();

            if (weaponRotation == 360) {
                weaponRotation = 0;
            }

            double maxXTranslation = Math.cos(Math.toRadians(weaponRotation))
                    * weaponWidth;
            double maxYTranslation = Math.sin(Math.toRadians(weaponRotation))
                    * weaponHeight;

            PositionComponent weaponPosition = world
                    .getComponent(weapon.getEntity(), PositionComponent.class)
                    .get();

            double maxXRangeToCheck = weaponPosition.getX() + maxXTranslation;
            double maxYRangeToCheck = weaponPosition.getY() + maxYTranslation;

            // get current weapon position
            double currentXPosition = world
                    .getComponent(weapon.getEntity(), PositionComponent.class)
                    .get().getX();
            double currentYPosition = world
                    .getComponent(weapon.getEntity(), PositionComponent.class)
                    .get().getY();
            
            double minXPosition = currentXPosition;
            double minYPosition = currentYPosition;
            
            // check for requirements for checkAreaForNoodle()
            if (currentXPosition > maxXRangeToCheck){
                minXPosition = maxXRangeToCheck;
                maxXRangeToCheck = currentXPosition;
            }
            if (currentXPosition > maxYRangeToCheck){
                minYPosition = maxYRangeToCheck;
                maxYRangeToCheck = currentYPosition;
            }
            

            // list of entities to apply damage
            List<Entity> affectedNoodles = checkAreaForNoodle(world,
                    currentXPosition, maxXRangeToCheck, currentYPosition,
                    maxYRangeToCheck);

            // apply damage
            for (int i = 0; i < affectedNoodles.size(); i++) {
                Entity affectedEntity = affectedNoodles.get(i);
                affectedEntity.addComponent(new InstantDamageComponent(weapon
                        .getDefinition().getProjectileType().getDamage()));
            }
            break;
        case "Placed":
            throw new NotImplementedException("We haven't finished this yet");
        default:
            throw new NotImplementedException(
                    "What type of weapon is " + def.getCategory() + "?");
        }

    }

    /**
     * Checks if the player has enough mana to shoot the given weapon
     * 
     * @param weapon
     *            The Weapon the player wants to shoot
     * @param world
     *            The world in which the player and weapon exists
     * @return Whether or not the player
     */
    private boolean enoughManaToFire(WeaponComponent weapon,
            World world) {
        Optional<ManaComponent> manaOpt = world
                .getComponent(weapon.getParent(), ManaComponent.class);
        if (!manaOpt.isPresent()) {
            // if it doesn't have one log it and say no
            logger.warn("Player doesn't have a mana component");
            return false;
        }
        ManaComponent mana = manaOpt.get();
        // Get the damage done by the weapon (or its first projectile)
        double damage = getWeaponDamage(weapon);
        // check if there's enough mana
        int manaCost = calculateManaCost(damage);
        return mana.getMana() >= manaCost;
    }

    /**
     * Calculates the cost of firing a weapon with the given damage
     * 
     * @param damage
     *            The amount of damage the weapon does
     * @return The amount of mana shooting weapon will cost
     */
    private int calculateManaCost(double damage) {
        return (int) Math.floor(Math.sqrt(damage) * MANA_CONSTANT);
    }
    
    
    /**
     * A private method for finding entities with noodle components within a given squares positional co-oridinates
     * @param world - current world for the game
     * @param startingXPosition - double type for the bottom left corner of the square
     * @param endingXPosition - double type for the bottom right corner of the square
     * @param startingYPosition - double type for the top left corner of the square
     * @param endingYPosition - double type for the top right corner of the square
     * @return List<Entity> representing all entities with noodle components that should be rendered within the square
     */
    private List<Entity> checkAreaForNoodle(World world,
            double startingXPosition, double endingXPosition,
            double startingYPosition, double endingYPosition) {

        List<NoodleComponent> allNoodles = new ArrayList<NoodleComponent>();
        allNoodles.addAll(0, world.getComponents(NoodleComponent.class));

        List<Entity> returnedNoodles = new ArrayList<Entity>();

        for (int i = 0; i < allNoodles.size(); i++) {
            NoodleComponent currentNoodle = allNoodles.get(i);
            Entity parentEntity = currentNoodle.getEntity();
            PositionComponent pos = world
                    .getComponent(parentEntity, PositionComponent.class).get();

            int height = world.getComponent(parentEntity, SpriteComponent.class).get().getHeight();
            int width = world.getComponent(parentEntity, SpriteComponent.class).get().getWidth();
            
            double minPositionX = pos.getX();
            double maxPositionX = pos.getX() + width;
            
            double minPositionY = pos.getY() - height;
            double maxPositionY = pos.getY();
            
            int nonRepeatedNoodleFlag = 0;
            
            for (double count = minPositionX; count <= maxPositionX; count++){
                if (count >= startingXPosition && count <= endingXPosition){
                    nonRepeatedNoodleFlag = 1;
                }
            }
            
            for (double count = minPositionY; count <= maxPositionY; count++){
                if (count >= startingYPosition && count <= endingYPosition && nonRepeatedNoodleFlag == 1){
                    returnedNoodles.add(parentEntity);
                }
            }
        }

        return returnedNoodles;

    }

    /**
     * Gets the amount of damage a weapon is capable of doing. NOTE: for
     * non-projectile weapons it "guesses" at 30. For clustering weapons, only
     * counts the first projectile(s)
     * 
     * @param weapon
     *            The WeaponComponent associated with the weapon to check
     * @return The amount of damage a given weapon does
     */
    private double getWeaponDamage(WeaponComponent weapon) {
        double damage;
        WeaponDefinition wdef = weapon.getDefinition();
        if (wdef.makesProjectiles()) {
            // damage is the number of projectiles it makes times the damage
            // that they do
            ProjectileDefinition pdef = wdef.getProjectileType();
            damage = pdef.getDamage()
                    * weapon.getDefinition().getNumOfProjectiles();
        } else {
            damage = 30; // idk just a placeholder
        }
        return damage;
    }

    /**
     * Updates the amount of mana the player has after firing the given weapon
     * 
     * @param weapon
     *            The WeaponComponent associated with the weapon being fired
     * @param world
     *            The world in which the player/weapon live
     */
    private void updateManaFromFire(WeaponComponent weapon,
            World world) {
        ManaComponent mana = world
                .getComponent(weapon.getParent(), ManaComponent.class).get();
        // Get the damage done by the weapon (or its first projectile)
        double damage = getWeaponDamage(weapon);
        // check if there's enough mana
        int manaCost = calculateManaCost(damage);
        int newMana = mana.getMana() - manaCost;
        // make sure you didn't go too far
        if (newMana < 0) {
            newMana = 0;
            // you probably shouldn't have done this, so log a message
            logger.warn("Mana went below zero... Maybe you're missing a check");
        }
        mana.setMana(newMana);
    }

    /**
     * 
     * @param weapon
     *            - The WeaponComponent that is fired
     * @require weapon != null
     * 
     *          This method gets the name of the weapon and maps a sound file to
     *          it. Then it proceeds to play the sound using AudioPlayer.java
     *          and AudioManager.java Doesn't play sounds if the testing flag is
     *          set
     */
    private void weaponSoundPlayer(WeaponComponent weapon) {
        String weaponAudioPath;
        boolean isLooping = false; // Default the weapon sound will not loop.

        // Determine weapon sound to play based off weapon.
        Random random = new Random();
        switch (weapon.getDefinition().getName()) {
            case "Grenade":
            case "Frag Grenade":
                weaponAudioPath = "resources/sounds/pin" + (random.nextInt(3) + 1) + ".wav";
                // Fall through
                break;
            case "Handgun":
                weaponAudioPath = "resources/sounds/pistol_fire" + (random.nextInt(3) + 1) + ".wav";
                break;
            case "Rocket Launcher":
                weaponAudioPath = "resources/sounds/rocket1.wav";
                break;
            case "Machine Gun": // Looks and fires like a grenade launcher.
                weaponAudioPath = "resources/sounds/grenade_launcher1.wav";
                break;
            case "Rail Gun":
                weaponAudioPath = "resources/sounds/railgun" + (random.nextInt(2) + 1) + ".wav";
                break;
            default:
                return;
        }

        // Plays weapon firing sound if there is one present.
        if (!"".equals(weaponAudioPath)) {
            AudioManager.playSound(weaponAudioPath, isLooping);
        }
    }

}
