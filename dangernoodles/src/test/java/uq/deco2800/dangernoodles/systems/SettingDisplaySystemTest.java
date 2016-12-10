package uq.deco2800.dangernoodles.systems;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.inputhandlers.KeyboardHandler;
import uq.deco2800.dangernoodles.inputhandlers.MouseHandler;
import org.junit.Test;
import uq.deco2800.dangernoodles.prefabs.SettingsMenu;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mock.*;


/**
 * Created by Jason on 10/22/2016.
 */
public class SettingDisplaySystemTest {

    private World testworld = new World(0, 0);
    Canvas canvas = mock(Canvas.class);
    GraphicsContext context = (canvas.getGraphicsContext2D());
    private KeyboardHandler keyhandler = mock(KeyboardHandler.class);
    private MouseHandler mousehandler = mock(MouseHandler.class);
    private StaticFrameHandler staticFrameHandler = new StaticFrameHandler(context);

    private SettingDisplaySystem sds = new SettingDisplaySystem(staticFrameHandler, mousehandler, keyhandler);


    @Test
    public void runTest() {
        SettingsMenu.createPause(testworld);
        testworld.addSystem(sds, 0);

        assertEquals(sds.isLastHoveredCog(), false);

        sds.run(testworld, 0, 0);
        //Hovering over cog
        mousehandler.setMouseX(327);
        mousehandler.setMouseY(16);
        sds.run(testworld, 0, 0);
        assertEquals(sds.isLastHoveredCog(), false);
        mousehandler.setMouseX(0);
        mousehandler.setMouseY(0);
        sds.run(testworld, 0, 0);
        assertEquals(sds.isLastHoveredCog(), false);

        //click cog - not paused
        mousehandler.setMouseX(327);
        mousehandler.setMouseY(16);
        mousehandler.setClicked(true);
        sds.run(testworld, 0, 0);

        //Hovering over exit - paused
        mousehandler.setMouseX(678);
        mousehandler.setMouseY(545);
        sds.staticRun(testworld);

        //Hovering over exit and click- paused
        mousehandler.setClicked(true);
        sds.staticRun(testworld);

        //Hovering over exit and click outside
        mousehandler.setMouseX(0);
        mousehandler.setMouseY(0);
        mousehandler.setClicked(false);
        sds.staticRun(testworld);


    }

}



