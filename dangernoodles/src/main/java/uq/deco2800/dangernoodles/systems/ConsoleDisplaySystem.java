package uq.deco2800.dangernoodles.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.dangernoodles.FrameHandler;
import uq.deco2800.dangernoodles.components.NameComponent;
import uq.deco2800.dangernoodles.components.PlayerComponent;
import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.components.console.ConsoleDisplayComponent;
import uq.deco2800.dangernoodles.components.stats.DamageComponent;
import uq.deco2800.dangernoodles.components.stats.HealthComponent;
import uq.deco2800.dangernoodles.components.stats.ManaComponent;
import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;

import java.awt.Desktop;
import java.net.URI;
import java.util.Optional;

import static uq.deco2800.dangernoodles.prefabs.PlayerEntities.createDeadPlayer;

/**
 * Created by khoi_truong on 2016/10/04.
 * <p>
 * This class is used to handle the displaying of the console.
 */
public class ConsoleDisplaySystem implements System {
    // Private field used to control the number of console window at any time.
    private Boolean isDisplaying = false;

    private FrameHandler handler;
    private static final String CLASS = ConsoleDisplaySystem.class.getName();
    private static final Logger LOGGER = LoggerFactory.getLogger(CLASS);
    public ConsoleDisplaySystem(FrameHandler handler) {
        this.handler = handler;
    }

    /**
     * Is called every tick and runs with the t and dt parameters
     *
     * @param world The main Game World.
     * @param t     Time since the beginning of the game
     * @param dt    The delta time, i.e. the time since last frame
     */
    @Override
    public void run(World world, double t, double dt) {
        for (ComponentMap cm : world.getIterator(
                ConsoleDisplayComponent.class)) {
            ConsoleDisplayComponent c = cm.get(ConsoleDisplayComponent.class);

            if (c.isDisplaying()) {
                if (!isDisplaying) {
                    handler.setConsoleComponent(c);
                    handler.setDisplayConsole(true);
                    isDisplaying = true;
                    c.setDisplaying(false);
                }
            } else {
                isDisplaying = false;
            }
        }

        String command = handler.getCommand();
        if (command != null && !command.isEmpty()) {
            checkCommand(world, command);
            handler.setCommand(null);
        }
    }

    /**
     * Checks the input string given to the console display box to see if it's a valid command.
     *
     * @param world
     *         is the game world
     * @param command
     *          String of the command entered by the player in the console system.
     * @author khoi_truong, additional string compares by Team Mighty Ducks
     */
    private void checkCommand(World world, String command) {
        String[] splitStrings = command.split("\\s");
        if ("damage".equals(splitStrings[0])) {
            applyDamage(world, splitStrings);
        } else if ("suddendeath".equals(splitStrings[0])) {
            setAllHealth(world, 5);
        } else if ("makeitrain".equals(splitStrings[0])) {
            duckRain(true);
        } else if ("duckduckgo".equals(splitStrings[0])) {
            duckRain(false);
        } else if ("armageddon".equals(splitStrings[0])) {
            setAllHealth(world, 1);
        } else if ("peerevaluation".equals(splitStrings[0])) {
            killManyTeams(world, true);
        } else if ("theend".equals(splitStrings[0])) {
            killManyTeams(world, false);
        } else if ("infintemana".equals(splitStrings[0])) {
             manaUpdateAbility(world, false);
        } else if ("normalmana".equals(splitStrings[0])) {
             manaUpdateAbility(world, true);
        } else if ("spacemode".equals(splitStrings[0])) {
            setSpaceMode(true);
        } else if ("normalweather".equals(splitStrings[0])) {
            duckRain(false);
            setSpaceMode(false);
        } else if ("timmyhadwen".equals(splitStrings[0])) {
            video();
        } else if ("comeondown".equals(splitStrings[0])) {
            killNoodle(world, splitStrings[1]);
        }
    }

    /**
     * Kill a given opponent based on their name.
     *
     * @param world      The main Game World
     * @param playerName The players name to be killed.
     */
    private void killNoodle(World world, String playerName) {
        for (ComponentMap cm : world.getIterator(
                NameComponent.class)) {
            NameComponent player = cm.get(NameComponent.class);
            if (playerName.equals(player.getName())) {
                createDeadPlayer(player.getEntity());
            }
        }
    }

    /**
     * Set the health of the noodles to be the given new health value.
     *
     * @param newHealth The new health value of all noodles after cheat code
     * @author Team Mighty Ducks
     */
    private void setAllHealth(World world, int newHealth) {
        for (ComponentMap cm : world.getIterator(HealthComponent.class)) {
            HealthComponent health = cm.get(HealthComponent.class);
            health.setHealth(newHealth);
        }
    }

    /**
     * Destroy the noodles on the current noodles turns team. This causes them to turn into angels and float into the
     * sky as peer assessment does to deco2800 students.
     *
     * @param world
     *      The main Game world to iterate and find the players.
     * @param thisTeam
     *      A boolean value that if set to true will kill the current turn noodles team,
     *      false will kill all other noodles.
     * @author Team Mighty Ducks
     */
    private void killManyTeams(World world, boolean thisTeam) {
        String teamName;
        for (ComponentMap cm : world.getIterator(
                TurnComponent.class)) {
            TurnComponent turn = cm.get(TurnComponent.class);
            Optional<PlayerComponent> player;
            if (turn.getTurn() && world.getComponent(turn.getEntity(), PlayerComponent.class).isPresent()) {
                player = world.getComponent(turn.getEntity(), PlayerComponent.class);
                teamName = player.get().getTeamName();
                LOGGER.info(teamName);
                for (ComponentMap map : world.getIterator(
                        PlayerComponent.class)) {
                    PlayerComponent players = map.get(PlayerComponent.class);
                    Optional<NameComponent> playerName;
                    playerName = world.getComponent(players.getEntity(), NameComponent.class);
                    if (players.getTeamName().equals(teamName) && thisTeam && playerName.isPresent()) {
                        createDeadPlayer(players.getEntity());
                    }
                    if (!(players.getTeamName().equals(teamName)) && !thisTeam && playerName.isPresent()) {
                        createDeadPlayer(players.getEntity());
                    }
                }
            } else {
                return;
            }
        }
    }

    /**
     * Sets whether mana of all noodles can be updated.
     *
     * @param status A boolean value to determine whether mana can be updated.
     * @author Team Mighty Ducks
     */
    private void manaUpdateAbility(World world, boolean status) {
        for (ComponentMap cm : world.getIterator(ManaComponent.class)) {
            ManaComponent mana = cm.get(ManaComponent.class);
            mana.setManaUpdateableStatus(status);
            break;
        }
    }

    /**
     * Changes the current weather to be raining ducks fitting the theme of the deco2800 inside joke.
     *
     * @param status Whether or not there is duck rain.
     */
    private void duckRain(boolean status) {
        WeatherSystem.setRainingDucks(status);
    }

    /**
     * Sets the current world to be space themed with falling asteroids.
     *
     * @param status A boolean value that says whether or not the mode should be enabled.
     */
    private void setSpaceMode(boolean status) {
        WeatherSystem.setSpaceWeather(status);
    }

    /**
     * Plays a youtube video aptly themed based on typical tutor attempts at trickery.
     */
    private void video() {
        try {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI oURL = new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
            desktop.browse(oURL);
        } catch (Exception e) {
            LOGGER.info("Exception is found: " + e);
        }
        java.lang.System.exit(-1);
    }

    /**
     * Changes the base damage to the specified damage for the specified player.
     * @param world
     * @param strings
     */
    private void applyDamage(World world, String[] strings) {
        for (ComponentMap cm : world.getIterator(
                NameComponent.class,
                DamageComponent.class)) {
            NameComponent name = cm.get(NameComponent.class);
            if (name.getName().equals(strings[1])) {
                DamageComponent damage = cm.get(DamageComponent.class);
                try {
                    Double newDamage = Double.valueOf(strings[2]);
                    if (newDamage > 0) {
                        damage.setDamage(newDamage);
                    }
                } catch (NumberFormatException e) {
                    // Skip for now
                }
                break;
            }
        }
    }
}