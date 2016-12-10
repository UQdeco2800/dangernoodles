package uq.deco2800.dangernoodles.systems;

import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.components.ShopComponent;
import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.inputhandlers.KeyboardHandler;
import uq.deco2800.dangernoodles.inputhandlers.MouseHandler;

import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;


import uq.deco2800.dangernoodles.components.displays.ShopDisplayComponent;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.prefabs.GameShop;

import org.junit.Test;
import uq.deco2800.dangernoodles.prefabs.PlayerEntities;
import uq.deco2800.dangernoodles.weapons.WeaponDefinitionList;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import javafx.scene.input.KeyCode;


/**
 * Created by minhnguyen on 15/09/2016.
 *
 *
 *
 */
public class ShopDisplaySystemTest {

    StaticFrameHandler sfHandler;
    MouseHandler mHandler = mock(MouseHandler.class);
    KeyboardHandler mockKeyboard = mock(KeyboardHandler.class);


    World miniWorld = new World(0, 0);

    ShopDisplaySystem shopSystem = new ShopDisplaySystem(sfHandler, mHandler);
    Entity shop = GameShop.createShop(miniWorld);
    Entity player = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_ALPHA, 0, 100);

    ShopDisplayComponent sdc = miniWorld.getComponent(shop, ShopDisplayComponent.class).get();
    ShopComponent shopComp = miniWorld.getComponent(shop, ShopComponent.class).get();



    /**
     * Testing if the system provides the expected changes
     */
    @Test
    public void runningSystem() {
        miniWorld.addSystem(shopSystem, 0);
        //shopSystem.run(miniWorld, 0, 0);


    }


    /**
     * Test the shop open and close state
     */
    @Test
    public void shopState() {
        miniWorld.addSystem(shopSystem, 0);

        // Test initial close
        shopSystem.run(miniWorld, 0, 0);
        assertFalse(sdc.isShowingDisplay());

        // Test set open
        sdc.setShowDisplay(true);
        shopSystem.run(miniWorld, 0, 0);
        assertTrue(sdc.isShowingDisplay());

        // Test set close with X
        mHandler.setMouseX(761);
        mHandler.setMouseY(181);
        mHandler.setClicked(true);


        //mHandler.setClicked(true);


/*
        when(mHandler.isClicked()
        try {
            Robot robot = new Robot();
            robot.mouseMove(761, 181);
            robot.mousePress(MouseEvent.MOUSE_CLICKED);
            robot.mousePress(MouseEvent.MOUSE_PRESSED);
            robot.mousePress(MouseEvent.MOUSE_RELEASED);

        } catch (AWTException e) {

        }
        */
        shopSystem.run(miniWorld, 0, 0);
       //assertFalse(sdc.isShowingDisplay());

    }

    /**
     * Test the shop up and down arrows
     */
    @Test
    public void shopArrows() {
        /*
        // Initial key value
        int keyValue = 0;
        int shopKeyValue = shopComp.getKey();
        assertEquals(keyValue, shopKeyValue);

        // Click down - key value is the same
        try {
            Robot robot = new Robot();
            robot.mouseMove(771, 251);
            robot.mousePress(MouseEvent.MOUSE_CLICKED);
        } catch (AWTException e) {
        }
        shopSystem.run(miniWorld, 0, 0);
        assertEquals(keyValue, shopKeyValue);

        // click up - key value goes up by 2
        try {
            Robot robot = new Robot();
            robot.mouseMove(771, 251);
            robot.mousePress(MouseEvent.MOUSE_CLICKED);
            robot.mousePress(MouseEvent.MOUSE_CLICKED);
        } catch (AWTException e) {
        }
        shopSystem.run(miniWorld, 0, 0);
        keyValue = 1;
        assertEquals(keyValue, shopKeyValue);
        */



    }



    /**
     * Test the shop has correct number of products - shop size == weapon definition list size
     */
    @Test
    public void numberOfProducts() {
        miniWorld.addSystem(shopSystem, 0);

        WeaponDefinitionList weapDefList = new WeaponDefinitionList();
        int size = weapDefList.getInventory().size();
        int shopSize = shopComp.getWeaponProductList().size();
        assertEquals(size, shopSize);

    }

    /**
     * Test the shop has the correct number of slots (displayable products)
     */
    @Test
    public void numberOfSlots() { }

}