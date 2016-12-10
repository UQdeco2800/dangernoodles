package uq.deco2800.dangernoodles.systems;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.junit.Test;
import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.inputhandlers.MouseHandler;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

/**
 * Created by Park on 10/21/2016.
 */
public class InventoryDisplaySystemTest {

    World testWorld = new World(0, 0);
    Canvas canvas = mock(Canvas.class);
    GraphicsContext context = canvas.getGraphicsContext2D();
    StaticFrameHandler staticFrameHandler = new StaticFrameHandler(context);
    MouseHandler mouseHandler = new MouseHandler(canvas);
    InventoryDisplaySystem inventoryDisplaySystem = new InventoryDisplaySystem(staticFrameHandler, mouseHandler);

   @Test
    public void run() throws Exception {
        testWorld.addSystem(inventoryDisplaySystem, 0);
        inventoryDisplaySystem.run(testWorld, 0, 0);

    }


}