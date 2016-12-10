package uq.deco2800.dangernoodles.prefabs;

import java.util.List;
import java.util.Random;

import javafx.scene.paint.Color;
import uq.deco2800.dangernoodles.AudioManager;
import uq.deco2800.dangernoodles.components.AIComponent;
import uq.deco2800.dangernoodles.components.CollisionComponent;
import uq.deco2800.dangernoodles.components.CursorComponent;
import uq.deco2800.dangernoodles.components.GravityComponent;
import uq.deco2800.dangernoodles.components.InputComponent;
import uq.deco2800.dangernoodles.components.MassComponent;
import uq.deco2800.dangernoodles.components.MovementComponent;
import uq.deco2800.dangernoodles.components.NameComponent;
import uq.deco2800.dangernoodles.components.PlayerComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.RectangleComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.components.StatusBarComponent;
import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.components.displays.EffectDisplayComponent;
import uq.deco2800.dangernoodles.components.noodles.NoodleComponent;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.components.stats.DamageComponent;
import uq.deco2800.dangernoodles.components.stats.HealthComponent;
import uq.deco2800.dangernoodles.components.stats.InvulnerableComponent;
import uq.deco2800.dangernoodles.components.stats.ManaComponent;
import uq.deco2800.dangernoodles.components.stats.ShieldComponent;
import uq.deco2800.dangernoodles.components.stats.SpeedComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

/**
 * Utility class that provides prefabricated players (noodles) to be added to
 * the world.
 *
 * @author Paul Haley
 * @author Anant Tuli
 */
public class PlayerEntities {

    private static int count = 0;

    /**
     * Private constructor to hide the implicit public one.
     */
    private PlayerEntities() {
    }

    /**
     * This static method is used to create entities when the a multiplayer
     * game starts.
     */
    public static Entity createEntity(World world, String name,
                                      double positionX, double positionY,
                                      NoodleEnum noodleEnum, int playerID,
                                      TeamEnum team) {
        final int playerMaxHealth = 100;
        final int size = 50;
        final int healthBarWidth = 100;
        int playerHealth = playerMaxHealth + noodleEnum.getHealthOffset();

        PositionComponent healthBarPosition = new PositionComponent(size / 2 - healthBarWidth / 2, -55);

        Entity player = world.createEntity()
                .addComponent(new NameComponent(name))
                .addComponent(new NoodleComponent(noodleEnum))
                .addComponent(new PositionComponent(positionX, positionY))
                // Default: ux->0.0, uy->0.0, ax->1.0, ay->0.0
                .addComponent(new MovementComponent(0.0, 0.0, 1.0 + noodleEnum.getAxOffset(), 0.0))
                // Defaults to snakesprite.png, will update as soon as the NoodleSystem is called.
                .addComponent(new SpriteComponent(size, size, noodleEnum.getImage()))
                .addComponent(new InputComponent()).addComponent(new TurnComponent())
                .addComponent(new PlayerComponent(playerID, team))
                // Default: 10.0
                .addComponent(new MassComponent(10.0 + noodleEnum.getMassOffset()))
                // Default: true
                .addComponent(new GravityComponent(true))
                .addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
                // Add statistics
                // Default: 1000
                .addComponent(new SpeedComponent(1000 + noodleEnum.getMaxSpeedOffset()))
                // Default: 0
                .addComponent(new ShieldComponent(0 + noodleEnum.getShieldOffset()))
                .addComponent(new InvulnerableComponent())
                .addComponent(new DamageComponent(15))
                // Default: 500
                .addComponent(new ManaComponent(500 + noodleEnum.getManaOffset()))
                .addComponent(new RectangleComponent(size, size))
                .addComponent(new EffectDisplayComponent())
                // Default: noodle health = 100
                .addComponent(new HealthComponent(playerHealth))
                .addComponent(new StatusBarComponent("Compact", healthBarWidth, 10, playerHealth, Color.LIMEGREEN,
                        Color.RED, false, healthBarPosition))
                .addComponent(new StatusBarComponent("compactManaBar", healthBarWidth, 10, 500 + noodleEnum.getManaOffset(),
                        Color.CORNFLOWERBLUE, Color.DARKBLUE,
                        false, new PositionComponent(size / 2 - healthBarWidth / 2, -43)))
                //This the mana bar
                .addComponent(new StatusBarComponent("ManaBar", 125, 10, 500 + noodleEnum.getManaOffset(),
                        Color.CORNFLOWERBLUE, Color.DARKBLUE,
                        true, new PositionComponent(90, 45)))
                // This is the effect display health Bar
                .addComponent(new StatusBarComponent("StatusPanel", 215, 10, playerHealth, Color.LIMEGREEN, Color.RED,
                        true, new PositionComponent(90, 30)));

        return player;
    }

    /**
     * This method creates and returns a new noodle entity (player). The noodle
     * made can be AI controlled or player controlled as per the given input.
     * The world the noodle exists in must also be given and the respective
     * starting horizontal position. It must be noted that the xPos given will
     * not be tested if it is on the map or somewhere safe for the noodle to
     * spawn.
     *
     * @param world
     *         Parent world object.
     * @param noodle
     *         Noodle character of this noodle entity from the NoodleEnum.
     * @param isAi
     *         True if player is AI, false otherwise.
     * @param team
     *         Int of the team number the noodle entity is a part of.
     * @param playerId
     *         Int of the player's ID
     * @param xPos
     *         Starting x position of the noodle entity as spawn (NOT TESTING IF
     *         THIS IS ON THE MAP)
     *
     * @return The created world noodle entity as per the given parameters
     */
    public static Entity createPlayer(World world, NoodleEnum noodle, Boolean isAi, TeamEnum team, int playerId,
                                      double xPos) {
        final int playerMaxHealth = 200;
        double yPos = 400;
        final int size = 50;
        final int healthBarWidth = 100;
        int playerHealth = playerMaxHealth + noodle.getHealthOffset();
        String[] playerNames = {"snoopDawg", "doge", "dolan", "leggy", "xXx_N00DL3-L1F3_xXx", "Snek", "Awesome",
                "Buffalo", "I don't " + "SHODAN", "110100100", "long doggo"};

        PositionComponent healthBarPosition = new PositionComponent(size / 2 - healthBarWidth / 2, -55);

        if (count >= playerNames.length) {
            count = 0;
        }

        Entity player = world.createEntity()
                .addComponent(new NameComponent(playerNames[count++]))
                .addComponent(new NoodleComponent(noodle))
                .addComponent(new PositionComponent(xPos, yPos))
                // Default: ux->0.0, uy->0.0, ax->1.0, ay->0.0
                .addComponent(new MovementComponent(0.0, 0.0, 0.0, 0.0))
                // Defaults to snakesprite.png, will update as soon as the NoodleSystem is called.
                .addComponent(new SpriteComponent(size, size, noodle.getImage()))
                .addComponent(new InputComponent()).addComponent(new TurnComponent())
                .addComponent(new PlayerComponent(playerId, team))
                // Default: 10.0
                .addComponent(new MassComponent(10.0 + noodle.getMassOffset()))
                // Default: true
                .addComponent(new GravityComponent(true))
                // Add statistics
                // Default: 1000
                .addComponent(new SpeedComponent(1000 + noodle.getMaxSpeedOffset()))
                // Default: 0
                .addComponent(new ShieldComponent(0 + noodle.getShieldOffset()))
                .addComponent(new InvulnerableComponent())
                .addComponent(new DamageComponent(0))
                // Default: 500
                .addComponent(new ManaComponent(500 + noodle.getManaOffset()))
                .addComponent(new RectangleComponent(size, size))
                .addComponent(new EffectDisplayComponent())
                // Default: noodle health = 100
                .addComponent(new HealthComponent(playerHealth))
                .addComponent(new StatusBarComponent("Compact", healthBarWidth, 10, playerHealth, Color.LIMEGREEN,
                        Color.RED, false, healthBarPosition))
                .addComponent(new StatusBarComponent("compactManaBar", healthBarWidth, 10, 500 + noodle.getManaOffset(),
                        Color.CORNFLOWERBLUE, Color.BLUE,
                        false, new PositionComponent(size / 2 - healthBarWidth / 2, -43)))
                // This the mana bar
                .addComponent(new StatusBarComponent("ManaBar", 125, 10, 500 + noodle.getManaOffset(),
                        Color.CORNFLOWERBLUE, Color.BLUE,
                        true, new PositionComponent(90, 45)))
                // This is the effect display health Bar
                .addComponent(new StatusBarComponent("StatusPanel", 215, 10, playerHealth, Color.LIMEGREEN, Color.RED,
                        true, new PositionComponent(90, 30)));

        // Make a collision component - make it ignore the cursor
        CollisionComponent collision = new CollisionComponent(true, CollisionComponent.shape.RECTANGLE);
        List<CursorComponent> cursors = world.getComponents(CursorComponent.class);
        // if there were cursor components returned
        if (cursors != null && !cursors.isEmpty()) {
            collision.addIgnored(cursors.get(0).getEntity());
        }
        // add the collision component to the player
        world.addComponent(player, collision);

        // From the AI team: you may want to delete this as this is meant to show you the different levels of
        // difficulties
        String[] difficulties = {"easy", "normal", "top"};

        // If player is a human player
        if (!isAi) {
            player.addComponent(new InputComponent());
        } else {// Otherwise a AI bot
            player.addComponent(new AIComponent(null, difficulties[new Random().nextInt(3)]));
        }

        return player;
    }

    /**
     * Test method that some other teams have made which omits the xPos. The
     * effects of having multiple entities in the exact same xPos is not likely
     * well defined.
     * <p>
     * DO NOT USE THIS.
     *
     * @deprecated
     */
    @Deprecated
    public static Entity createPlayer(World world, NoodleEnum noodle,
                                      Boolean isAi, TeamEnum team,
                                      int playerId) {
        return createPlayer(world, noodle, isAi, team, playerId, 0);
    }

    /**
     * This method take a noodle that has just died and uses it to make a dead
     * one matching some attributes. This method DOES NOT destroy the previous
     * noodle.
     *
     * @param deadNoodle
     *         The noodle that just died, see require for details.
     *
     * @require That the deadNoodle entity given was an noodle entity. The
     * requirements of this are only as strict as having Name, Noodle, Position
     * Components
     * @ensure An entity of the form of a dead noodle matching the last
     * attributes of the given noodle are returned
     */
    public static void createDeadPlayer(Entity deadNoodle) {
        World world = deadNoodle.getWorld();

        // Raise the Noodle off the ground before it dies
        PositionComponent position = world.getComponent(deadNoodle, PositionComponent.class).get();

        world.removeComponent(deadNoodle, MovementComponent.class);
        world.removeComponent(deadNoodle, GravityComponent.class);
        world.removeComponent(deadNoodle, MassComponent.class);
        world.removeComponent(deadNoodle, RectangleComponent.class);
        world.removeComponent(deadNoodle, CollisionComponent.class);

        // Add the prefix "The late" to the noodles old name.
        NameComponent name = world.getComponent(deadNoodle, NameComponent.class).get();
        name.setName("The late\n" + name.getName());

        // Removes unneeded components of a dead noodle
        world.removeComponent(deadNoodle, StatusBarComponent.class);
        world.removeComponent(deadNoodle, SpeedComponent.class);
        world.removeComponent(deadNoodle, DamageComponent.class);
        world.removeComponent(deadNoodle, ShieldComponent.class);
        world.removeComponent(deadNoodle, ManaComponent.class);
        world.removeComponent(deadNoodle, EffectDisplayComponent.class);
        world.removeComponent(deadNoodle, HealthComponent.class);
        world.removeComponent(deadNoodle, AIComponent.class);
        world.removeComponent(deadNoodle, InputComponent.class);

        // Makes the remains of the noodle invulnerable
        world.getComponent(deadNoodle, InvulnerableComponent.class).get().setInvulnerable(true);


        String deathImage;
        // Gives the noodle a death image, grave stone or color specific corpse
        if (Math.random() < 0.3) { // Gravestone
            deathImage = "/characters/grave.png";
            AngelEntity.createAngel(world, position.getX(), position.getY()); // Makes angel
        } else { // Dead noodle of player colour is made
            // check team and make string mutator to match file
            String teamID;
            switch (world.getComponent(deadNoodle, PlayerComponent.class).get().getTeamId()) {
                case 0:
                    teamID = "_Bteam";
                    break;
                case 1:
                    teamID = "_Rteam";
                    break;
                case 2:
                    teamID = "";
                    break;
                case 3:
                    teamID = "_Yteam";
                    break;
                default:// Default to green noodles
                    teamID = "";
            }
            deathImage = "/characters/dead" + teamID + ".png";
        }

        // Playing death chime, might add a more natural sound later for alternative deaths.
        Random random = new Random();
        AudioManager.playSound("resources/sounds/death_chime" + (random.nextInt(2) + 1) + ".wav", false);

        world.getComponent(deadNoodle, NoodleComponent.class).get().setCurrentSprite(deathImage);
        world.getComponent(deadNoodle, SpriteComponent.class).get().setImage(deathImage);
    }
}