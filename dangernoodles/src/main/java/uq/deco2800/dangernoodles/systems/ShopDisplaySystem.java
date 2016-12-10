package uq.deco2800.dangernoodles.systems;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uq.deco2800.dangernoodles.AudioManager;
import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.components.stats.ManaComponent;
import uq.deco2800.dangernoodles.inputhandlers.MouseHandler;

import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.ecs.Entity;

import uq.deco2800.dangernoodles.components.displays.ShopDisplayComponent;
import uq.deco2800.dangernoodles.components.weapons.NoodleWeaponComponent;
import uq.deco2800.dangernoodles.shop.WeaponProduct;


import uq.deco2800.dangernoodles.prefabs.WeaponEntities;
import uq.deco2800.dangernoodles.weapons.WeaponDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;


/**
 * Created by minhnguyen on 4/09/2016.
 * <p>
 * This class is used to display and hide the shop panel when necessary and manipulate player and shop data based
 * on the user input.
 */
public class ShopDisplaySystem implements System {
    private StaticFrameHandler staticHandler;
    private MouseHandler mHandler;
    private boolean pressedOnce = false;
    private String fontVerdana = "Verdana";
    private static String thisClass = ShopDisplaySystem.class.getName();
    private final Logger logger = LoggerFactory.getLogger(thisClass);

    /**
     * Creates a system that controls the interaction between players and the shop entity.
     *
     * @param staticHandler
     *          Frame handler to render static objects onto the canvas.
     * @param mouseHandler
     *          Mouse handler to handle mouse events in the game.
     */
    public ShopDisplaySystem(StaticFrameHandler staticHandler, MouseHandler mouseHandler) {
        this.mHandler = mouseHandler;
        this.staticHandler = staticHandler;

    }

    /**
     * This method checks if the shop is displayed. If so, it responds to user input and manipulates data in the
     * shop component accordingly.
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
        List<TurnComponent> turns = world.getComponents(TurnComponent.class);
        SpriteComponent background = new SpriteComponent(400, 650, "/SHOP.png");
        // Getting components of shop entity
        Entity shop = world.getComponents(ShopComponent.class).get(0).getEntity();
        ShopComponent shopComp = world.getComponent(shop, ShopComponent.class).get();
        ShopDisplayComponent shopSDC = world.getComponent(shop, ShopDisplayComponent.class).get();

        // Set up shop when it is clicked
        if (shopSDC.isShowingDisplay()) {
            // Dimensions for panel images
            int boxWidth = 650;
            int boxHeight = 400;

            int slotX = 380;
            int slotOneY = 307;
            int slotTwoY = 365;
            int slotThreeY = 423;
            int slotFourY = 481;
            int slotImageW = 35;
            int slotW = 360;
            int slotH = 35;

            int exitX = 760;
            int exitY = 180;
            int exitLength = 60;
            int exitHeight = 45;

            int arrX = 770;
            int upArrY = 250;
            int downArrY = 480;
            int arrLength = 50;
            int arrHeight = 33;

            int buyX = 230;
            int buyY = 380;
            int buyW = 120;
            int buyH = 25;

            int numberProduct = shopComp.getWeaponProductList().size();

            // Adding weapons sprites to the slots based on key
            for (int i = 0; i < shopComp.getMaxSlots(); i++) {
                int indexOfProduct = (shopComp.getKey() % numberProduct) + i;
                Integer weaponID = shopComp.getWeaponProductList().get(indexOfProduct).getWeaponDefinition().getID();

                if (shopComp.getProductSlot().size() < shopComp.getMaxSlots()) {
                    shopComp.getProductSlot().add(i, weaponID);
                } else {
                    shopComp.getProductSlot().set(i, weaponID);
                }
            }

            // Process current players input
            for (TurnComponent ts : turns) {
                if (ts.getTurn()) {
                    Entity e = ts.getEntity();

                    // HANDLING POSITION BASED CLICKED
                    // DOWN arrow - increment key
                    if (mHandler.isClicked() && !pressedOnce &&
                            checkPositionClick(mHandler, arrX, arrX + arrLength, downArrY, downArrY + arrHeight)) {
                    	clickSound(false);
                        shopComp.incrementKey();
                        pressedOnce = true;
                    }

                    // UP arrow - decrement key
                    else if (mHandler.isClicked() && !pressedOnce &&
                            checkPositionClick(mHandler, arrX, arrX + arrLength, upArrY, upArrY + arrHeight)) {
                    	clickSound(true);
                        shopComp.decrementKey();
                        pressedOnce = true;
                    }

                    // exit button - close display
                    else if (mHandler.isClicked() &&
                            checkPositionClick(mHandler, exitX, exitX + exitLength, exitY, exitY + exitHeight)) {
                    	// Plays shop opening or closing sound
                    	AudioManager.playSound("resources/sounds/shop_open1.wav", false);
                        shopSDC.setShowDisplay(false);
                    }
                    // slot 0 - finding weapon with slot0 imageLocation and set selectedWeapo
                    else if (mHandler.isClicked() &&
                            checkPositionClick(mHandler, slotX, slotX + slotW, slotOneY, slotOneY + slotH)) {
                    	selectSound();
                        shopComp.setSelectedProduct(shopComp.getProductSlot().get(0));
                    }
                    // slot 1 - finding weapon with slot1 imageLocation and set selectedWeapo
                    else if (mHandler.isClicked() &&
                            checkPositionClick(mHandler, slotX, slotX + slotW, slotTwoY, slotTwoY + slotH)) {
                    	selectSound();
                        shopComp.setSelectedProduct(shopComp.getProductSlot().get(1));
                    }
                    // slot 2 - finding weapon with slot2 imageLocation and set selectedWeapo
                    else if (mHandler.isClicked() &&
                            checkPositionClick(mHandler, slotX, slotX + slotW, slotThreeY, slotThreeY + slotH)) {
                    	selectSound();
                        shopComp.setSelectedProduct(shopComp.getProductSlot().get(2));
                    }

                    // slot 3 - finding weapon with slot3 imageLocation and set selectedWeapo
                    else if (mHandler.isClicked() &&
                            checkPositionClick(mHandler, slotX, slotX + slotW, slotFourY, slotFourY + slotH)) {
                        selectSound();
                        shopComp.setSelectedProduct(shopComp.getProductSlot().get(3));
                    }

                    // buying - adjust stats - replaces weapon
                    else if (mHandler.isClicked() &&
                            checkPositionClick(mHandler, buyX, buyX + buyW, buyY, buyY + buyH)) {
                    	buyWeapon(world, e, shopComp);
                    }

                    // if it's showing (which it is) we want to gobble the click
                    mHandler.setClicked(false);
                    // consume the click for the cursor component as well
                    CursorComponent cursor = world.getComponents(CursorComponent.class).get(0);
                    cursor.consumeClick();

                    // render shop panel details
                    staticHandler.addStaticRenderAction((context, handler) -> {
                        context.drawImage(handler.loadImage(background.getImage()), 200, 150, boxWidth, boxHeight);
                        context.setFill(Color.WHITE);
                        context.setFont(Font.font(fontVerdana, 25));
                        context.fillText("BUY", 265, 400);
                        context.fillText("PROJECTILES", 385, 275, 100);
                        context.setFill(Color.DARKGREY);
                        context.fillText("...", 550, 265, 100);
                        context.fillText("...", 678, 265, 100);
                        context.setFill(Color.WHITE);
                    });

                    // render slot 1, 2, 3 and 4 respectively
                    renderSlot(shopComp, slotX, slotOneY, 15, slotImageW, slotH, 0);
                    renderSlot(shopComp, slotX, slotTwoY, 13, slotImageW, slotH, 1);
                    renderSlot(shopComp, slotX, slotThreeY, 11, slotImageW, slotH, 2);
                    renderSlot(shopComp, slotX, slotFourY, 9, slotImageW, slotH, 3);

                    // render player specific information
                    renderPlayerInfo(world, e);
                }
            }
        }
        pressedOnce = false;
    }

    /**
     * Check if the coordinates of the mouse has landed within the specified coordinates. The specified coordinates
     * represent an area on the shop panel that is actionable. Such as a slot, buy or arrows.
     *
     * @param mHandler
     *          Mouse handler to handle mouse events in the game.
     * @param startX
     *          inner x bound for the click
     * @param endX
     *          outter x bound for the click
     * @param startY
     *          inner y bound for the click
     * @param endY
     *          outter y bound for the click
     * @return boolean representing if the mouse has clicked in a position that represents an actionable feature
     */
    private boolean checkPositionClick(MouseHandler mHandler, int startX, int endX, int startY, int endY) {
        return mHandler.getMouseX() > startX && mHandler.getMouseX() < endX &&
                mHandler.getMouseY() > startY && mHandler.getMouseY() < endY;
    }

    /**
     * Compares the image location inside the index-th slot to the image location's in the shops WeaponProductList.
     * If there is a match, grab the necessary information (name, stock, cost) and renders them at the specified
     * location.
     *
     * @param shopComp
     *         component that contains data of the shop
     * @param slotX
     *          position on x axis where the weapon sprite is rendered
     * @param slotY
     *          position on y axis where the weapon sprite is rendered
     * @param incY
     *          offset on y axis from the top of the screen of where the weapon sprite is rendered
     * @param slotImageW
     *          width of sprite
     * @param slotH
     *          height of slot
     * @param index
     *          index of slot
     */
    private void renderSlot(ShopComponent shopComp, int slotX, int slotY, int incY, int slotImageW, int slotH, int index) {
        staticHandler.addStaticRenderAction((context, handler) -> {
            context.setFill(Color.WHITE);
            context.setFont(Font.font(fontVerdana, 18));

            // Get weaponID of the weapon in the Slot
            Integer weaponID = shopComp.getProductSlot().get(index);
            // Get image location of the weapon with that ID
            String slotImageLocation = shopComp.getWeaponProductList().get(weaponID).getImageLocation();
            context.drawImage(handler.loadImage(slotImageLocation), slotX, slotY, slotImageW, slotH);

            // Find weapDef in weapDefList that has ID of the one in the slot
            for(int i = 0; i < shopComp.getWeaponProductList().size(); i++) {
                Integer matchWeapID = shopComp.getWeaponProductList().get(i).getWeaponDefinition().getID();
                if(weaponID.equals(matchWeapID)) {
                    WeaponProduct matchWeapon = shopComp.getWeaponProductList().get(i);
                    if (checkSelected(index, shopComp)) {
                        context.setFill(Color.GOLDENROD);
                    }
                    String productName = matchWeapon.getWeaponDefinition().getName();
                    context.fillText(productName, slotX + 40, slotY + incY, 300);
                    String stock = Integer.toString(shopComp.getWeaponProductList().get(index).getStock());
                    String cost = Integer.toString(shopComp.getWeaponProductList().get(index).getCost());
                    context.setFont(Font.font(fontVerdana, 14));
                    String description = "Stock Remaining: " + stock + "  Product Value: " + cost;
                    context.fillText(description, slotX + 40, slotY + 32, 300);
                    context.setFill(Color.WHITE);
                    break;
                }
            }
        });
    }

    /**
     * Checks if a weapon has been selected. If so, checks if the selected weapon is a valid purchase (suffient mana,
     * sufficient stock). Otherwise, do nothing. If it is a valid purchase, adjust mana and stock appropriately and
     * swap the noodle's current weapon for the purchased weapon.
     *
     * @param world
     *         is the game world
     *
     * @param e
     *          noodle entity of the current turn
     * @param shopComp
     *          component that contains the shop data
     */
    private void buyWeapon(World world, Entity e, ShopComponent shopComp) {
        logger.info("Clicked purchase");

        // Do nothing if a weapon isnt selected
        if (shopComp.getSelectedProduct() != null) {
            Optional<NoodleWeaponComponent> nwc = world.getComponent(e, NoodleWeaponComponent.class);
            WeaponProduct weapon = shopComp.getSelectedProduct();
            ManaComponent mana = world.getComponent(e, ManaComponent.class).get();
            // if noodle is holding weapon, sufficient stock, sufficient mana
            if (nwc.isPresent() && !pressedOnce && weapon.getStock() > 0 && mana.getMana() > weapon.getCost()) {
                // Reduce mana
                mana.setMana(mana.getMana() - weapon.getCost());
                // Reduce stock
                weapon.setStock(weapon.getStock() - 1);
                // Remove and replace current holding weapon
                Entity weaponEntity = nwc.get().getWeapon();
                WeaponEntities.removeWeapon(world, weaponEntity);
                WeaponDefinition weapDef = weapon.getWeaponDefinition();
                WeaponEntities.createWeapon(world, weapDef, e);
                // Play sound for buying weapon.
                AudioManager.playSound("resources/sounds/coins1.wav", false);
            } else {
            	// Play select sound instead since item could not be bought.
            	selectSound();
            }
            pressedOnce = true;
        }
    }


    /**
     * Retrieves the name and sprite of the noodle entity whose turn it is and whose opened the shop.
     * @param world
     *         is the game world
     *
     * @param e
     *          noodle entity of the current turn
     */
    private void renderPlayerInfo(World world, Entity e) {
        SpriteComponent pSprite = world.getComponent(e, SpriteComponent.class).get();
        NameComponent pName = world.getComponent(e, NameComponent.class).get();
        staticHandler.addStaticRenderAction((context, handler) -> {
            context.drawImage(handler.loadImage(pSprite.getImage()), 220, 210, 150, 150);
            context.setFill(Color.WHITE);
            context.setFont(Font.font(fontVerdana, 40));
            context.fillText(pName.getName(), 230, 215, 550);
            context.setFont(Font.font(fontVerdana, 14));
        });
    }

    /**
     * Compares the image location of the selected weapon product to the image location of the slot 'id'.
     * Returns true if there is a match. Otherwise, returns false.
     *
     * @return
     *          boolean representing if product in slot 'id' is selected
     *
     * @param slotIndex
     *         the index of the slot being checked.
     *
     * @param shopComp
     *          component that contains all the shop data
     */
    private boolean checkSelected(int slotIndex, ShopComponent shopComp) {
        Integer weaponIDOfSlot = shopComp.getProductSlot().get(slotIndex);
        if(shopComp.getSelectedProduct() != null) {
            Integer selectedProductWeaponID = shopComp.getSelectedProduct().getWeaponDefinition().getID();
            return selectedProductWeaponID.equals(weaponIDOfSlot);
        } else {
            return false;
        }
    }
    
    /**
     * Plays the select sound for the shop.
     */
    private void selectSound() {
    	AudioManager.playSound("resources/sounds/shop_select1.wav", false);
    }
    
    /**
     * Plays click sound for up and down scrolling.
     * @param isUp Set true if the click was for upwards scrolling.
     */
    private void clickSound(boolean isUp) {
    	int direction = isUp ? 1 : 2;
    	AudioManager.playSound("resources/sounds/click" + direction + ".wav", false);
    }
}
