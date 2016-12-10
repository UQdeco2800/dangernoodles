package uq.deco2800.dangernoodles.systems;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mock.*;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import org.mockito.Mockito;
import uq.deco2800.dangernoodles.Game;
import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.components.PauseComponent;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.inputhandlers.KeyboardHandler;
import uq.deco2800.dangernoodles.inputhandlers.MouseHandler;
import uq.deco2800.dangernoodles.prefabs.SettingsMenu;

/**
 * Created by Jason on 23/10/16.
 */
public class InstructionsSystemTest {

    @Test
    public void Run() throws Exception {
        Canvas canvas = mock(Canvas.class);
        GraphicsContext context = (canvas.getGraphicsContext2D());
        KeyboardHandler keyhandler = mock(KeyboardHandler.class);
        MouseHandler mousehandler = mock(MouseHandler.class);
        StaticFrameHandler staticFrameHandler = new StaticFrameHandler(context);
        World testworld = new World(0, 0);
        InstructionsSystem instructionsSystem = new InstructionsSystem(staticFrameHandler, mousehandler, keyhandler);
        //Test run method
        SettingsMenu.createPause(testworld);

        Game.setMultiplayer(false);

        PauseComponent p = testworld.getComponents(PauseComponent.class).get(0);

        testworld.addSystem(instructionsSystem, 0);

        instructionsSystem.run(testworld, 0, 0);

    }

    @Test
    public void runsettings() throws Exception {
        Canvas canvas = mock(Canvas.class);
        GraphicsContext context = (canvas.getGraphicsContext2D());
        KeyboardHandler keyhandler = mock(KeyboardHandler.class);
        MouseHandler mousehandler = mock(MouseHandler.class);
        StaticFrameHandler staticFrameHandler = new StaticFrameHandler(context);
        World testworld = new World(0, 0);
        InstructionsSystem instructionsSystem = new InstructionsSystem(staticFrameHandler, mousehandler, keyhandler);
        testworld.addSystem(instructionsSystem, 0);
        SettingsMenu.createPause(testworld);
        InstructionsSystem.resetForTest();

        PauseComponent p = testworld.getComponents(PauseComponent.class).get(0);

        p.setInstructions(false);

        instructionsSystem.run(testworld, 0, 0);

        assertEquals(p.isPaused(), true);
        assertEquals(p.getInstructions(), false);

    }

    @Test
    public void testClose() {
        Canvas canvas = mock(Canvas.class);
        GraphicsContext context = (canvas.getGraphicsContext2D());
        KeyboardHandler keyhandler = mock(KeyboardHandler.class);
        MouseHandler mousehandler = mock(MouseHandler.class);
        StaticFrameHandler staticFrameHandler = new StaticFrameHandler(context);
        World testworld = new World(0, 0);
        InstructionsSystem instructionsSystem = new InstructionsSystem(staticFrameHandler, mousehandler, keyhandler);
        testworld.addSystem(instructionsSystem, 0);
        SettingsMenu.createPause(testworld);
        InstructionsSystem.resetForTest();

        PauseComponent p = testworld.getComponents(PauseComponent.class).get(0);

        p.setInstructions(true);

        //Test closing
        when(keyhandler.isPressed(KeyCode.I)).thenReturn(true);

        try {
            instructionsSystem.staticRun(testworld);
        } catch (InterruptedException e) {

        }
    }

    @Test
    public void testRight() {
        Canvas canvas = mock(Canvas.class);
        GraphicsContext context = (canvas.getGraphicsContext2D());
        KeyboardHandler keyhandler = mock(KeyboardHandler.class);
        MouseHandler mousehandler = mock(MouseHandler.class);
        StaticFrameHandler staticFrameHandler = new StaticFrameHandler(context);
        World testworld = new World(0, 0);
        InstructionsSystem instructionsSystem = new InstructionsSystem(staticFrameHandler, mousehandler, keyhandler);
        testworld.addSystem(instructionsSystem, 0);
        SettingsMenu.createPause(testworld);
        InstructionsSystem.resetForTest();

        PauseComponent p = testworld.getComponents(PauseComponent.class).get(0);

        p.setInstructions(true);
        Game.setMultiplayer(false);
        //Test closing
        when(keyhandler.isPressed(KeyCode.RIGHT)).thenReturn(true);
        InstructionsSystem.setPage(3);
        try {
            InstructionsSystem.staticRun(testworld);
        } catch (InterruptedException e) {

        }

        assertEquals(true, p.isPaused());
        assertEquals(true, p.getInstructions());

    }

    @Test
    public void testLeft() {
        Canvas canvas = mock(Canvas.class);
        GraphicsContext context = (canvas.getGraphicsContext2D());
        KeyboardHandler keyhandler = mock(KeyboardHandler.class);
        MouseHandler mousehandler = mock(MouseHandler.class);
        StaticFrameHandler staticFrameHandler = new StaticFrameHandler(context);
        World testworld = new World(0, 0);
        InstructionsSystem instructionsSystem = new InstructionsSystem(staticFrameHandler, mousehandler, keyhandler);
        testworld.addSystem(instructionsSystem, 0);
        SettingsMenu.createPause(testworld);
        InstructionsSystem.resetForTest();

        PauseComponent p = testworld.getComponents(PauseComponent.class).get(0);

        p.setInstructions(true);
        instructionsSystem.setPage(3);
        //Test closing
        when(keyhandler.isPressed(KeyCode.LEFT)).thenReturn(true);

        try {
            instructionsSystem.staticRun(testworld);
        } catch (InterruptedException e) {

        }
        assertEquals(p.isPaused(), true);
        assertEquals(p.getInstructions(), true);

    }

    @Test
    public void testMouseExit() {
        Canvas canvas = mock(Canvas.class);
        GraphicsContext context = (canvas.getGraphicsContext2D());
        KeyboardHandler keyhandler = mock(KeyboardHandler.class);
        MouseHandler mousehandler = mock(MouseHandler.class);
        StaticFrameHandler staticFrameHandler = new StaticFrameHandler(context);
        World testworld = new World(0, 0);
        InstructionsSystem instructionsSystem = new InstructionsSystem(staticFrameHandler, mousehandler, keyhandler);
        testworld.addSystem(instructionsSystem, 0);
        SettingsMenu.createPause(testworld);
        InstructionsSystem.resetForTest();

        PauseComponent p = testworld.getComponents(PauseComponent.class).get(0);

        p.setInstructions(true);

        //Test closing
        mousehandler.setMouseX(860);
        mousehandler.setMouseY(175);
        instructionsSystem.setPage(3);
        when(mousehandler.isClicked()).thenReturn(true);

        try {
            instructionsSystem.staticRun(testworld);
        } catch (InterruptedException e) {

        }
    }

    @Test
    public void testMouseLeft() {
        Canvas canvas = mock(Canvas.class);
        GraphicsContext context = (canvas.getGraphicsContext2D());
        KeyboardHandler keyhandler = mock(KeyboardHandler.class);
        MouseHandler mousehandler = mock(MouseHandler.class);
        StaticFrameHandler staticFrameHandler = new StaticFrameHandler(context);
        World testworld = new World(0, 0);
        InstructionsSystem instructionsSystem = new InstructionsSystem(staticFrameHandler, mousehandler, keyhandler);
        testworld.addSystem(instructionsSystem, 0);
        SettingsMenu.createPause(testworld);
        InstructionsSystem.resetForTest();

        PauseComponent p = testworld.getComponents(PauseComponent.class).get(0);

        p.setInstructions(true);
        Game.setMultiplayer(false);
        mousehandler.setMouseX(285);
        mousehandler.setMouseY(340);
        when(mousehandler.isClicked()).thenReturn(true);
        instructionsSystem.setPage(3);
        try {
            InstructionsSystem.staticRun(testworld);
        } catch (InterruptedException e) {

        }
        assertEquals(p.isPaused(), true);
        assertEquals(p.getInstructions(), true);

    }

    @Test
    public void testMouseRight() {
        Canvas canvas = mock(Canvas.class);
        GraphicsContext context = (canvas.getGraphicsContext2D());
        KeyboardHandler keyhandler = mock(KeyboardHandler.class);
        MouseHandler mousehandler = mock(MouseHandler.class);
        StaticFrameHandler staticFrameHandler = new StaticFrameHandler(context);
        World testworld = new World(0, 0);
        InstructionsSystem instructionsSystem = new InstructionsSystem(staticFrameHandler, mousehandler, keyhandler);
        testworld.addSystem(instructionsSystem, 0);
        SettingsMenu.createPause(testworld);
        PauseComponent p = testworld.getComponents(PauseComponent.class).get(0);
        InstructionsSystem.resetForTest();

        p.setInstructions(true);
        Game.setMultiplayer(false);

        //Test closing
        mousehandler.setMouseX(845);
        mousehandler.setMouseY(340);
        when(mousehandler.isClicked()).thenReturn(true);
        try {
            instructionsSystem.staticRun(testworld);
        } catch (InterruptedException e) {

        }
        assertEquals(p.isPaused(), true);
        assertEquals(p.getInstructions(), true);

    }

    @Test
    public void testInstructionsPage() {
        Canvas canvas = mock(Canvas.class);
        GraphicsContext context = (canvas.getGraphicsContext2D());
        KeyboardHandler keyhandler = mock(KeyboardHandler.class);
        MouseHandler mousehandler = mock(MouseHandler.class);
        StaticFrameHandler staticFrameHandler = new StaticFrameHandler(context);
        World testworld = new World(0, 0);
        InstructionsSystem instructionsSystem = new InstructionsSystem(staticFrameHandler, mousehandler, keyhandler);
        InstructionsSystem.resetForTest();
        instructionsSystem.setPage(3);
        assertTrue(instructionsSystem.getPage() == 3);
        instructionsSystem.setPage(5);
        assertTrue(instructionsSystem.getPage() == 5);

    }


}