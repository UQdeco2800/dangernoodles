package uq.deco2800.dangernoodles.systems;

import static org.junit.Assert.*;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.mockito.Mock.*;
import uq.deco2800.dangernoodles.FrameHandler;
import uq.deco2800.dangernoodles.Game;
import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.ecs.World;


/**
 * Created by Jason on 23/10/16.
 */
public class MinimapBoxSystemTest {
    Canvas canvas = mock(Canvas.class);
    private World testworld = new World(0, 0);
    private Game game = mock(Game.class);

    GraphicsContext context = (canvas.getGraphicsContext2D());
    StaticFrameHandler staticFrameHandler = new StaticFrameHandler(context);
    FrameHandler frameHandler = new FrameHandler(context, game);

    MinimapBoxSystem boxSystem = new MinimapBoxSystem(frameHandler, staticFrameHandler);

    @Test
    public void drawSquare() {

        testworld.addSystem(boxSystem, 0);

        boxSystem.run(testworld, 0, 0);


    }

}