package uq.deco2800.dangernoodles.systems;

import java.util.List;
import java.util.Optional;

import javafx.scene.input.KeyCode;
import uq.deco2800.dangernoodles.AudioManager;
import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.components.console.ConsoleDisplayComponent;
import uq.deco2800.dangernoodles.components.displays.ShopDisplayComponent;
import uq.deco2800.dangernoodles.components.weapons.NoodleWeaponComponent;
import uq.deco2800.dangernoodles.components.weapons.WeaponComponent;
import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.inputhandlers.KeyboardHandler;
import uq.deco2800.dangernoodles.prefabs.WeaponEntities;
import uq.deco2800.dangernoodles.weapons.WeaponDefinition;

public class KeyboardSystem implements System {
	private static final float volumeINCREMENT = 0.1f;
	
    private KeyboardHandler keys;

    private double x = 0.0;
    private double y = 0.0;
    private boolean shop = false;
    private boolean pressOnce = false;
    private int pushed = 0;
    
    private boolean qPressed = false;
    private boolean ePressed = false;
    
    private boolean nPressed = false;

    /**
     * Creates a systme for managing keyboard keys being pressed. Keys are linked to changing
     * the state of variables in different features which in extension determine how that feature performs.
     *
     * @param keyboardHandler
     */
    public KeyboardSystem(KeyboardHandler keyboardHandler) {
        this.keys = keyboardHandler;
    }

    /**
     * Function for managing state of variables based on what keyboard key is pressed.
     *
     * @param world
     * @param t Time since the beginning of the game
     * @param dt The delta time, i.e. the time since last frame
     */
    @Override
    public void run(World world, double t, double dt) {
        for (ComponentMap cm : world.getIterator(ConsoleDisplayComponent.class)) {
            ConsoleDisplayComponent c = cm.get(ConsoleDisplayComponent.class);
            if (keys.isPressed(KeyCode.BACK_QUOTE)) {
                if (!pressOnce) {
                    c.setDisplaying(true);
                    pressOnce = true;
                }
            } else {
                c.setDisplaying(false);
                pressOnce = false;
            }
        }

        if (keys.isPressed(KeyCode.P)) {
            for (ComponentMap cm : world.getIterator(PauseComponent.class)) {
                PauseComponent p = cm.get(PauseComponent.class);
                if (!p.isPaused()) {
                    p.setPaused(true);
                }
            }
        }

        if (keys.isPressed(KeyCode.ESCAPE)) {
            for (ComponentMap cm : world.getIterator(PauseComponent.class)) {
                PauseComponent p = cm.get(PauseComponent.class);
                if (!p.isPaused()) {
                    p.setPaused(true);
                }
            }
        }
        
        // force next player's turn
        if (keys.isPressed(KeyCode.N)) {
            nPressed = true;
        } else {
            if (nPressed) {
                nPressed = false; // start again
                nextTurn(world, t);
            }
        }
        
        // Changing weapons
        // previous weapon
        if (keys.isPressed(KeyCode.Q)) {
            qPressed = true;
        } else {
            if (qPressed) {
                qPressed = false; // start again
                changeWeapon(false, world);
            }
        }
        
        // next weapon
        if (keys.isPressed(KeyCode.E)) {
            ePressed = true;
        } else {
            if (ePressed) {
                ePressed = false; // start again
                changeWeapon(true, world);
            }
        }

        if (keys.isPressed(KeyCode.I)) {
            for (ComponentMap cm : world.getIterator(PauseComponent.class)) {
                PauseComponent p = cm.get(PauseComponent.class);
                if (!p.isPaused()) {
                    p.setInstructions(true);
                    p.setPaused(true);
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                else {
                    p.setInstructions(false);
                    p.setPaused(false);
                }
            }
        }
        
        // Decreasing sound effects volume with "["
        if (keys.isPressed(KeyCode.OPEN_BRACKET)) {
        	AudioManager.volumeSound(AudioManager.getVolumeSound() - volumeINCREMENT);
        }
        
        // Increasing sound effects volume with "]"
        if (keys.isPressed(KeyCode.CLOSE_BRACKET)) {
        	AudioManager.volumeSound(AudioManager.getVolumeSound() + volumeINCREMENT);
        }
        
        // Decreasing music volume ";"
        if (keys.isPressed(KeyCode.SEMICOLON)) {
        	AudioManager.volumeMusic(AudioManager.getVolumeMusic() - volumeINCREMENT);
        }
        
        // Increasing music volume with "'"
        if (keys.isPressed(KeyCode.QUOTE)) {
        	AudioManager.volumeMusic(AudioManager.getVolumeMusic() + volumeINCREMENT);
        }
        
        // Toggle muting of sound effects with "."
        if (keys.isPressed(KeyCode.PERIOD)) {
        	AudioManager.toggleMuteSound();
        }
        
        // Toggle muting of music with "/"
        if (keys.isPressed(KeyCode.SLASH)) {
        	AudioManager.toggleMuteMusic();
        }

        updateShop(world);
        updateKeys();
        updatePlayer(world);


    }
    
    private void changeWeapon(boolean next, World world) {
        List<TurnComponent> turns = world.getComponents(TurnComponent.class);
        // check for the noodle who's turn it is
        for (TurnComponent turn : turns) {
            if (turn.getTurn()) {
                // don't do it for AI
                if (world.hasComponent(turn.getEntity(), AIComponent.class)) {
                    break;
                }
            	
                // Get the noodle weapon component
                NoodleWeaponComponent noodleWeapon = world.getComponent(
                        turn.getEntity(), NoodleWeaponComponent.class)
                        .orElse(null);
                // Just leave if it doesn't have a weapon....
                if (noodleWeapon == null) {
                    break;
                }
                // Get the actual weapon component, then the next weapon
                WeaponComponent weapon = world.getComponent(
                        noodleWeapon.getWeapon(), WeaponComponent.class)
                        .get();
                
                // get the next/previous weapon
                WeaponDefinition newWeapon;
                if (next) {
                    newWeapon = world.getWeaponDefinitions()
                            .next(weapon.getDefinition().getID());
                } else {
                    newWeapon = world.getWeaponDefinitions()
                            .previous(weapon.getDefinition().getID());
                }
                
                // remove the old weapon
                WeaponEntities.removePlayersWeapon(world, turn.getEntity());
                // add the new one
                WeaponEntities.createWeapon(world, newWeapon,
                        turn.getEntity());
                weaponChangeSound(newWeapon);
                break;
            }
        }
    }
    
    /**
     * NOTE: This is currently implemented for debugging
     * End the turn of the current player
     * 
     * @param world
     * @param t Time since the beginning of the game
     */
    private void nextTurn(World world, double t) {
    	List<TurnComponent> turns = world.getComponents(TurnComponent.class);
        // check for the noodle who's turn it is
        for (TurnComponent turn : turns) {
            if (turn.getTurn()) {
            	// next turn
                turn.clearTurn(t);
                
                break;
            }
        }
    }

    /**
     * Plays sound (if applicable) for the newly selected weapon.
     * 
     * @param newWeapon Weapon that the player just switched to/
     */
    private void weaponChangeSound(WeaponDefinition newWeapon) {
    	String weaponAudioPath = "";
    	
		switch (newWeapon.getName()) {
			case "Handgun":
				weaponAudioPath = "resources/sounds/pistol_cock1.wav";
				break;
			case "Rail Gun":
				weaponAudioPath = "resources/sounds/railgun_cock1.wav";
				break;
			default:
				break;
		}
		
		if (!weaponAudioPath.equals("")) {
			AudioManager.playSound(weaponAudioPath, false);
		}
	}

	/**
     * Updates the players in the world, if it's their turn update there input
     * component, else reset it
     *
     * @param world
     *         The world
     */
    private void updatePlayer(World world) {
        for (PlayerComponent p : world.getComponents(PlayerComponent.class)) {
            // update input component if and only if the player is not an AI
            if (!world.hasComponent(p.getEntity(), AIComponent.class)) {
                Optional<InputComponent> oi = world.getComponent(p.getEntity(), InputComponent.class);
                Optional<TurnComponent> turn = world.getComponent(p.getEntity(), TurnComponent.class);
                if (oi.isPresent() && turn.isPresent()) {
                    InputComponent i = oi.get();
                    TurnComponent t = turn.get();

                    if (t.getTurn()) {
                        i.setX(x);
                        i.setY(y);
                    } else {
                        i.setX(0);
                        i.setY(0);
                    }
                }
            }
        }
    }

    /**
     * Updates the x and y coordinates based on the keys pressed. These coordinates are used to determine
     * the positino of the player on the canvas.
     */
    private void updateKeys() {
        if (keys.isPressed(KeyCode.D)) {
            x = 1;
        } else if (keys.isPressed(KeyCode.A)) {
            x = -1;
        } else {
            x = 0;
        }

        if (keys.isPressed(KeyCode.S)) {
            y = 1;
        } else if (keys.isPressed(KeyCode.W)) {
            y = -1.75;
        } else {
            y = 0;
        }

        // If pressing in a diagonal direction, normalize the direction
        if (Double.compare(y, 0) != 0 && Double.compare(x, 0) != 0) {
            double magnitude = Math.sqrt((x * x) + (y * y));

            if (Double.compare(magnitude, 0) != 0) {
                x = x / magnitude;
                y = y / magnitude;
            }
        }

    }

    /**
     * Updates the displaying state of the shop based on the key 'B' being pressed.
     * Does not do anything if the key is pressed on a turn that isnt yours or if the turn belongs to an AI.
     */
    private void updateShop(World world) {
        if (keys.isPressed(KeyCode.B)) {
            // Grab current player and look for possible AI Component
            List<TurnComponent> turns = world.getComponents(TurnComponent.class);
            for (TurnComponent turn : turns) {
                if (turn.getTurn()) {
                    Entity player = turn.getEntity();
                    Optional<AIComponent> aiComp = world.getComponent(player, AIComponent.class);
                    // Process opening shop only if current player isnt AI
                    if (!aiComp.isPresent()) {
                        if (shop && pushed == 0) {
                            shop = false;
                        } else if (pushed == 0 && !shop) {
                            shop = true;
                        }
                        pushed++;
                        List<ShopDisplayComponent> shops = world.getComponents(ShopDisplayComponent.class);
                        for (ShopDisplayComponent shop1 : shops) {
                            if (shop) {
                                shop1.setShowDisplay(true);
                            } else {
                                shop1.setShowDisplay(false);
                            }
                        }
                    }
                }
            }
        } else {
            pushed = 0;
        }
    }
}
