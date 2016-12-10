package uq.deco2800.dangernoodles.camera;

import javafx.scene.input.KeyCode;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.inputhandlers.KeyboardHandler;
import uq.deco2800.dangernoodles.systems.CameraSystem;
import uq.deco2800.dangernoodles.windowhandlers.Camera;
import uq.deco2800.dangernoodles.windowhandlers.CameraEnum;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;


/**
 * Created by Jason on 19/10/2016.
 *
 * This class tests the camera system in every direction using
 * robots.
 */
public class CameraSystemTest {


    World testworld = new World(0, 0);


    @Test
    public void runTest() throws Exception {


        KeyboardHandler c = mock(KeyboardHandler.class);

        CameraSystem camSystem = new CameraSystem(c);
        testworld.addSystem(camSystem, 0);
        //Test up
        when(c.isPressed(KeyCode.UP)).thenReturn(true);
        camSystem.run(testworld, 0, 0);
        assertEquals(camSystem.getValue(), CameraEnum.UP);
        when(c.isPressed(KeyCode.UP)).thenReturn(false);

        //Test left
        when(c.isPressed(KeyCode.LEFT)).thenReturn(true);
        camSystem.run(testworld, 0, 0);
        assertEquals(camSystem.getValue(), CameraEnum.LEFT);
        when(c.isPressed(KeyCode.LEFT)).thenReturn(false);

        //Test down
        when(c.isPressed(KeyCode.DOWN)).thenReturn(true);
        camSystem.run(testworld, 0, 0);
        assertEquals(camSystem.getValue(), CameraEnum.DOWN);
        when(c.isPressed(KeyCode.DOWN)).thenReturn(false);

        //Test right
        when(c.isPressed(KeyCode.RIGHT)).thenReturn(true);
        camSystem.run(testworld, 0, 0);
        assertEquals(camSystem.getValue(), CameraEnum.RIGHT);
        when(c.isPressed(KeyCode.RIGHT)).thenReturn(false);

        //Test up right
        when(c.isPressed(KeyCode.UP)).thenReturn(true);
        when(c.isPressed(KeyCode.RIGHT)).thenReturn(true);
        camSystem.run(testworld, 0, 0);
        assertEquals(camSystem.getValue(), CameraEnum.TOP_RIGHT);
        when(c.isPressed(KeyCode.UP)).thenReturn(false);
        when(c.isPressed(KeyCode.RIGHT)).thenReturn(false);

        //Test up left
        when(c.isPressed(KeyCode.UP)).thenReturn(true);
        when(c.isPressed(KeyCode.LEFT)).thenReturn(true);
        camSystem.run(testworld, 0, 0);
        assertEquals(camSystem.getValue(), CameraEnum.TOP_LEFT);
        when(c.isPressed(KeyCode.UP)).thenReturn(false);
        when(c.isPressed(KeyCode.LEFT)).thenReturn(false);

        //Test down right
        when(c.isPressed(KeyCode.DOWN)).thenReturn(true);
        when(c.isPressed(KeyCode.RIGHT)).thenReturn(true);
        camSystem.run(testworld, 0, 0);
        assertEquals(camSystem.getValue(), CameraEnum.BOTTOM_RIGHT);
        when(c.isPressed(KeyCode.DOWN)).thenReturn(false);
        when(c.isPressed(KeyCode.RIGHT)).thenReturn(false);

        //Test down left
        when(c.isPressed(KeyCode.DOWN)).thenReturn(true);
        when(c.isPressed(KeyCode.LEFT)).thenReturn(true);
        camSystem.run(testworld, 0, 0);
        assertEquals(camSystem.getValue(), CameraEnum.BOTTOM_LEFT);
        when(c.isPressed(KeyCode.DOWN)).thenReturn(false);
        when(c.isPressed(KeyCode.LEFT)).thenReturn(false);

        //Test ZOOM IN
        when(c.isPressed(KeyCode.EQUALS)).thenReturn(true);
        camSystem.run(testworld, 0, 0);
        assertEquals(camSystem.getValue(), CameraEnum.ZOOM_IN);
        when(c.isPressed(KeyCode.EQUALS)).thenReturn(false);

        //Test ZOOM OUT
        when(c.isPressed(KeyCode.MINUS)).thenReturn(true);
        camSystem.run(testworld, 0, 0);
        assertEquals(camSystem.getValue(), CameraEnum.ZOOM_OUT);
        when(c.isPressed(KeyCode.MINUS)).thenReturn(false);

        //Test DONT MOVW
        camSystem.run(testworld, 0, 0);
        assertEquals(camSystem.getValue(), CameraEnum.DONT_MOVE);



    }
}