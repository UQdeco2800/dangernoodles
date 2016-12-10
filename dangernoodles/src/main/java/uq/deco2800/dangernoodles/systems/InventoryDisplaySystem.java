package uq.deco2800.dangernoodles.systems;

import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.components.weapons.NoodleWeaponComponent;
import uq.deco2800.dangernoodles.components.weapons.WeaponComponent;
import uq.deco2800.dangernoodles.inputhandlers.MouseHandler;
import uq.deco2800.dangernoodles.prefabs.WeaponEntities;
import uq.deco2800.dangernoodles.weapons.WeaponDefinition;

import java.util.List;

import javafx.scene.paint.Color;

/**
 * Created by Park on 10/6/2016.
 *
 * This class is used to display inventory and weapons.
 */
public class InventoryDisplaySystem implements System {
	private static final int MAX_SIZE = 30;

    private static StaticFrameHandler staticFrame;
    private static MouseHandler mouseHandler;
    SlotInventoryComponent slotInven = new SlotInventoryComponent();


    /**
     * Creates a system that retrieves the noodle entities inventory and renders it on an inventory panel.
     *
     * @param staticFrame
     *          Frame handler to render static objects onto the canvas.
     * @param mouseHandler
     *                     Mouse handler to handle mouse-related events
     * @throws NullPointerException
     *         if staticHandler or mouseHandler is null
     * @require staticHandler != null && mouseHandler != null
     * @ensure an instance of this class is constructed.
     */
    public InventoryDisplaySystem(StaticFrameHandler staticFrame, MouseHandler mouseHandler) {
        if (staticFrame == null || mouseHandler == null) {
            throw new NullPointerException("Static frame handler and " +
                    "mouseHandler cannot be null");
        }
        InventoryDisplaySystem.staticFrame = staticFrame;
        InventoryDisplaySystem.mouseHandler = mouseHandler;

    }


    /**
     * Renders weapon sprites from the noodles inventory to the inventory panel
     *
     * @param world World object
     * @param t
     *         Time since the beginning of the game
     * @param dt
     *         Delta time - changing time
     */
    @Override
    public void run(World world, double t, double dt) {
        double mX = mouseHandler.getMouseX();
        double mY = mouseHandler.getMouseY();

        staticFrame.addStaticRenderAction((context, handler) -> {
                context.drawImage(staticFrame.loadImage(slotInven.inventoryStr()), slotInven.inventoryX(),
                        slotInven.inventoryY(), slotInven.inventoryW(), slotInven.inventoryH());

            });

        List<WeaponDefinition> weapons = world.getWeaponDefinitions().getInventory();
        int i = 1;
        for (WeaponDefinition weapon : weapons) {
        	PositionComponent slotPos = slotInven.getSlotPosition(i);
        	int[] imgSize = calculateImageSize(weapon);
        	staticFrame.addStaticRenderAction((context, handler) -> {
                context.drawImage(staticFrame.loadImage(weapon.getImages()), slotPos.getX(), slotPos.getY(), 
                		imgSize[0], imgSize[1]);
            });
        	i++;
        }
        
        if (mouseHandler.isClicked()) {
        	processClick(mX, mY, world, weapons);
        }
        int slotNum = checkIfMouseOverSlot(mX, mY);
        drawSelectBox(slotNum, Color.LIGHTBLUE);

    }
    
    /**
     * Resize a weapon image, while holding true to its aspect ratio, constrained by a square.
     * 
     * @param weapon
     *            a WeaponDefinition
     * @return an int array of the new width an height
     */
    private int[] calculateImageSize(WeaponDefinition weapon) {
    	int width = 0;
    	int height = 0;
    	double ratio = 0;
    	double wWidth = weapon.getWidth();
    	double wHeight = weapon.getHeight();
    	if ((int)wWidth == (int)wHeight) {
    		width = MAX_SIZE;
    		height = MAX_SIZE;
    	} else if (wWidth > wHeight) {
    		width = MAX_SIZE;
    		ratio = wHeight / wWidth;
    		height = (int) (ratio * MAX_SIZE);
    	} else {
    		height = MAX_SIZE;
    		ratio = wWidth / wHeight;
    		width = (int) (ratio * MAX_SIZE);
    	}
    	
    	return new int[] { width, height };
    }
    
    /**
     * Process a click event from the mouse at the given coordinates. If the click is over a slot
     * then change the current noodle's (if applicable) weapon to the weapon in the slot.
     * 
     * @param x
     *            the x-coordinate of the mouse
     * @param y
     *            the y-coordinate of the mouse
     * @param world
     * @param weapons
     *            the list of weapons from the WeaponDefinitionList
     */
    private void processClick(double x, double y, World world, List<WeaponDefinition> weapons) {
    	int weaponNum = 0;
    	PositionComponent slotS = slotInven.getSlotPosition(1);
    	PositionComponent slotE = slotInven.getSlotPosition(10);
    	if ((x >= slotS.getX() && x <= slotE.getX() + MAX_SIZE) &&
    			(y >= slotS.getY() && y <= slotE.getY() + MAX_SIZE)) {
    		mouseHandler.setClicked(false);
        	// consume the click for the cursor component
            CursorComponent cursor = world.getComponents(
                    CursorComponent.class).get(0);
            cursor.consumeClick();
            
    		weaponNum = checkIfMouseOverSlot(x, y);
    		if (weaponNum == 0 || weaponNum > weapons.size()) {
    			return;
    		}
    	} else {
    		return;
    	}
    	
        changeWeapon(weapons.get(weaponNum - 1), world);
    }
    
    /**
     * Find the slot that the mouse is currently over (if it is over a slot at all)
     * 
     * @param x
     *            the x-coordinate of the mouse
     * @param y
     *            the y-coordinate of the mouse
     * @return the slot number or zero (if not over a slot)
     */
    private int checkIfMouseOverSlot(double x, double y) {
    	for (int i = 1; i <= slotInven.getNumberOfSlots(); i++) {
			PositionComponent slot = slotInven.getSlotPosition(i);
			if ((x >= slot.getX() && x <= slot.getX() + MAX_SIZE) &&
	    			(y >= slot.getY() && y <= slot.getY() + MAX_SIZE)) {
				return i;
			}
		}
    	return 0;
    }
    
    /**
     * Change the weapon of the current player to the weapon specified.
     * NOTE: This is derived from the KeyBoardSystem changeWeapon method
     * 
     * @param newWeapon
     *            the WeaponDefinition to give to the noodle
     * @param world
     */
    private void changeWeapon(WeaponDefinition newWeapon, World world) {
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
                // Get the actual weapon component
                WeaponComponent weapon = world.getComponent(
                        noodleWeapon.getWeapon(), WeaponComponent.class)
                        .get();
                
                // If the weapon is not already selected
                if (!newWeapon.equals(weapon)) {
                    // remove the old weapon
                    WeaponEntities.removePlayersWeapon(world, turn.getEntity());
                    // add the new one
                    WeaponEntities.createWeapon(world, newWeapon,
                            turn.getEntity());
                }
                
                //TODO: Add weapon sounds
                break;
            }
        }
    }
    
    /**
     * Draws a box of a given colour around the specified slot
     * 
     * @param slotNumber
     *            the number of the slot
     * @param colour
     *            a valid javafx colour
     */
    private void drawSelectBox(int slotNumber, Color colour) {
    	if (slotNumber != 0 && slotNumber <= slotInven.getNumberOfSlots()) {
	        double slotX = slotInven.getSlotPosition(slotNumber).getX();
	        double slotY = slotInven.getSlotPosition(slotNumber).getY();
	        staticFrame.addStaticRenderAction((context, handler) -> {
	            //Default weapon glow effect
	            context.setFill(colour);
	            //left square
	            context.fillRoundRect(slotX - 2, slotY - 2,
	                    8, MAX_SIZE + 4, 5, 5);
	            //right square
	            context.fillRoundRect(slotX + 26, slotY - 2,
	                    8, MAX_SIZE + 4, 5, 5);
	            //upper square
	            context.fillRoundRect(slotX - 2, slotY - 2,
	                    30, 4, 5, 5);
	            //lower square
	            context.fillRoundRect(slotX - 2, slotY + 28,
	                    30, 4, 5, 5);
	        });
        }
    }

}




