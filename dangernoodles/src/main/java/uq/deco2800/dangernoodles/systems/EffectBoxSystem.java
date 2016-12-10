package uq.deco2800.dangernoodles.systems;

import uq.deco2800.dangernoodles.AudioManager;
import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.components.effects.*;
import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.EffectBox;
import uq.deco2800.dangernoodles.prefabs.Parachute;

import java.util.Optional;

import static java.lang.Math.random;


/**
 * Created by Jaysun on 2016/09/15, re-implemented by khoi_truong.
 * <p>
 * This class is used to control collision with an effectbox
 */
public class EffectBoxSystem implements System {

    private double counter = 0;

    /**
     * This method checks if a box collided with a player - if so it makes a new
     * box and destroys the old one
     *
     * @param world
     *         is the game world
     * @param t
     *         Time since the beginning of the game
     * @param dt
     *         The delta time, i.e. the time since last frame
     */
    @Override
    public void run(World world, double t, double dt) {
        // Increase the counter by the time delta.
        counter += dt;
        // Once the counter reaches the limit second, start dropping effect box.
        if (counter >= 10) {
            double boxX = random() * 700;
            EffectBox.createBox(world, boxX, 250);
            Parachute.createParachute(world, boxX - 5, 210);
            counter = 0;
        }
        // Get all the noodles in the world
        for (ComponentMap cm : world.getIterator(
                PlayerComponent.class,
                CollisionComponent.class)) {
            // Retrieve the collision component
            CollisionComponent collisionComponent = cm.get(CollisionComponent.class);
            // For each of the entities that this noodle is colliding, see if
            // one of them is an effect box.
            for (int i = 0; i < collisionComponent.getCollisions().size(); i++) {
                Entity e = collisionComponent.getCollisions().get(i);
                Optional<NameComponent> name;
                name = world.getComponent(e, NameComponent.class);
                // If yes, than start applying effect to the noodle.
                if (name.isPresent() && "Mystery Box".equals(name.get().getName())) {
                	// Playing sound of create being collected
                	AudioManager.playSound("resources/sounds/crate1.wav", false);
                	
                    appliedEffect(world, e, cm.getEntity());
                    world.destroyEntity(e);
                    break;
                }
            }
        }
    }

    /**
     * Apply the given effect stored inside the effect box to an entity.
     *
     * @param world
     *         the parent world
     * @param effectBox
     *         an Entity object representing effect box
     * @param noodle
     *         an Entity object representing the noodle
     *
     * @throws NullPointerException
     *         if either world, effectBox or noodle is null
     * @require world != null && effectBox != null && noodle != null
     * @ensure noodle is applied with effect stored in effectBox
     */
    @SuppressWarnings("unchecked")
    private void appliedEffect(World world, Entity effectBox, Entity noodle) {
        if (world == null || effectBox == null || noodle == null) {
            throw new NullPointerException("Either world, effect box or " +
                    "noodle cannot be null.");
        }

        // Get the effect that this box is carrying.
        Optional<DamageEffectComponent> damage;
        damage = world.getComponent(effectBox, DamageEffectComponent.class);
        Optional<SpeedEffectComponent> speed;
        speed = world.getComponent(effectBox, SpeedEffectComponent.class);
        Optional<ShieldEffectComponent> shield;
        shield = world.getComponent(effectBox, ShieldEffectComponent.class);
        Optional<ManaEffectComponent> mana;
        mana = world.getComponent(effectBox, ManaEffectComponent.class);
        Optional<InvulnerableEffectComponent> invulnerable;
        invulnerable = world.getComponent(effectBox, InvulnerableEffectComponent.class);

        // Then go through each one to see which effect is current stored in
        // the box.
        if (damage.isPresent()) {
            // Check if the noodle already has a damage effect. If yes, the
            // effect's duration and value will be increased. Otherwise, just
            // add it.
            if (world.hasComponent(noodle, DamageEffectComponent.class)) {
                // Get the current effect that the noodle is holding
                Optional<DamageEffectComponent> currentOptional;
                currentOptional = world.getComponent(noodle, DamageEffectComponent.class);
                DamageEffectComponent currentEffect = currentOptional.get();
                // Retrieve the duration and value
                int currentDuration = currentEffect.getTurnDuration();
                double currentDamage = currentEffect.getDamage();
                // Get the new effect
                DamageEffectComponent newEffect = damage.get();
                // Retrieve the new duration and value
                int newDuration = newEffect.getTurnDuration();
                double newDamage = newEffect.getDamage();
                // Adjust them and add them to the current effect.
                newDuration = currentDuration + newDuration / 2;
                newDamage = currentDamage + newDamage / 2;

                currentEffect.setTurnDuration(newDuration);
                currentEffect.setDamage(newDamage);
            } else {
                noodle.addComponent(damage.get());
            }
        } else if (speed.isPresent()) {
            // Check if the noodle already has a speed effect. If yes, the
            // effect's duration and value will be increased. Otherwise, just
            // add it.
            if (world.hasComponent(noodle, SpeedEffectComponent.class)) {
                // Get the current effect that the noodle is holding
                Optional<SpeedEffectComponent> currentOptional;
                currentOptional = world.getComponent(noodle, SpeedEffectComponent.class);
                SpeedEffectComponent currentEffect = currentOptional.get();
                // Retrieve the duration and value
                int currentDuration = currentEffect.getTurnDuration();
                double currentSpeed = currentEffect.getSpeed();
                // Get the new effect
                SpeedEffectComponent newEffect = speed.get();
                // Retrieve the new duration and value
                int newDuration = newEffect.getTurnDuration();
                double newSpeed = newEffect.getSpeed();
                // Adjust them and add them to the current effect.
                newDuration = currentDuration + newDuration / 2;
                newSpeed = currentSpeed + newSpeed / 2;

                currentEffect.setTurnDuration(newDuration);
                currentEffect.setSpeed(newSpeed);
            } else {
                noodle.addComponent(speed.get());
            }
        } else if (shield.isPresent()) {
            // Check if the noodle already has a shield effect. If yes, the
            // effect's duration and value will be increased. Otherwise, just
            // add it.
            if (world.hasComponent(noodle, ShieldEffectComponent.class)) {
                // Get the current effect that the noodle is holding
                Optional<ShieldEffectComponent> currentOptional;
                currentOptional = world.getComponent(noodle, ShieldEffectComponent.class);
                ShieldEffectComponent currentEffect = currentOptional.get();
                // Retrieve the duration and value
                int currentDuration = currentEffect.getTurnDuration();
                double currentShield = currentEffect.getShield();
                // Get the new effect
                ShieldEffectComponent newEffect = shield.get();
                // Retrieve the new duration and value
                int newDuration = newEffect.getTurnDuration();
                double newShield = newEffect.getShield();
                // Adjust them and add them to the current effect.
                newDuration = currentDuration + newDuration / 2;
                newShield = currentShield + newShield / 2;

                currentEffect.setTurnDuration(newDuration);
                currentEffect.setShield(newShield);
            } else {
                noodle.addComponent(shield.get());
            }
        } else if (invulnerable.isPresent()) {
            // Check if the noodle already has a invulnerable effect. If yes,
            // the effect's duration and value will be increased. Otherwise,
            // just  add it.
            if (world.hasComponent(noodle, InvulnerableEffectComponent.class)) {
                // Get the current effect that the noodle is holding
                Optional<InvulnerableEffectComponent> currentOptional;
                currentOptional = world.getComponent(noodle, InvulnerableEffectComponent.class);
                InvulnerableEffectComponent currentEffect = currentOptional.get();
                // Retrieve the duration and value
                int currentDuration = currentEffect.getTurnDuration();
                // Get the new effect
                InvulnerableEffectComponent newEffect = invulnerable.get();
                // Retrieve the new duration and value
                int newDuration = newEffect.getTurnDuration();
                // Adjust them and add them to the current effect.
                newDuration = currentDuration + newDuration / 2;

                currentEffect.setTurnDuration(newDuration);
            } else {
                noodle.addComponent(invulnerable.get());
            }
        } else if (mana.isPresent()) {
            // Check if the noodle already has a mana effect. If yes, the
            // effect's duration and value will be increased. Otherwise, just
            // add it.
            if (world.hasComponent(noodle, ManaEffectComponent.class)) {
                // Get the current effect that the noodle is holding
                Optional<ManaEffectComponent> currentOptional;
                currentOptional = world.getComponent(noodle, ManaEffectComponent.class);
                ManaEffectComponent currentEffect = currentOptional.get();
                // Retrieve the duration and value
                int currentDuration = currentEffect.getTurnDuration();
                int currentMana = currentEffect.getMana();
                // Get the new effect
                ManaEffectComponent newEffect = mana.get();
                // Retrieve the new duration and value
                int newDuration = newEffect.getTurnDuration();
                int newMana = newEffect.getMana();
                // Adjust them and add them to the current effect.
                newDuration = currentDuration + newDuration / 2;
                newMana = currentMana + newMana / 2;

                currentEffect.setTurnDuration(newDuration);
                currentEffect.setMana(newMana);
            } else {
                noodle.addComponent(mana.get());
            }
        }
    }
}

