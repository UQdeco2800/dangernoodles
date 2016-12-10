package uq.deco2800.dangernoodles.systems;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.ecs.World;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * Created by Jason on 19/10/16.
 */

public class ClockSystemTest {

    private static final String CLASS = ClockSystemTest.class.getName();
    private static final Logger LOGGER = LoggerFactory.getLogger(CLASS);

    private World testworld = new World(0, 0);
    double width = 1050;
    double height = 700;

    @Test
    public void run() {
        Canvas canvas = Mockito.mock(Canvas.class);
        GraphicsContext context = canvas.getGraphicsContext2D();
        StaticFrameHandler staticFrameHandler = new StaticFrameHandler(context);
        ClockSystem clockSystem = new ClockSystem(staticFrameHandler);
        testworld.addSystem(clockSystem, 0);
        clockSystem.run(testworld, 30, 0);
    }

    @Test
    public void secStr() {
        Canvas canvas = Mockito.mock(Canvas.class);
        GraphicsContext context = canvas.getGraphicsContext2D();
        StaticFrameHandler staticFrameHandler = new StaticFrameHandler(context);
        ClockSystem clockSystem = new ClockSystem(staticFrameHandler);
        String value = clockSystem.secStr(0);
        assertEquals(value, "0");
        value = clockSystem.secStr(25);
        assertEquals(value, "25");
    }

    @Test
    public void minStr() {
        Canvas canvas = Mockito.mock(Canvas.class);
        GraphicsContext context = canvas.getGraphicsContext2D();
        StaticFrameHandler staticFrameHandler = new StaticFrameHandler(context);
        ClockSystem clockSystem = new ClockSystem(staticFrameHandler);
        String value = clockSystem.minStr(0);
        assertEquals(value, "0");
        value = clockSystem.minStr(25);
        assertEquals(value, "25");
    }
}