package uq.deco2800.dangernoodles.systems;

import static org.junit.Assert.*;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.mockito.Mock.*;
import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.ecs.World;

/**
 * Created by Jason on 23/10/16.
 */
public class MinimapSystemTest {
    private World testworld = new World(0, 0);
    Canvas canvas = mock(Canvas.class);
    GraphicsContext context = (canvas.getGraphicsContext2D());
    StaticFrameHandler staticFrameHandler = new StaticFrameHandler(context);

    MinimapSystem miniSystem = new MinimapSystem(staticFrameHandler);


    @Test
    public void runTest() {

        testworld.addSystem(miniSystem, 0);

        miniSystem.run(testworld, 0, 0);


    }

}