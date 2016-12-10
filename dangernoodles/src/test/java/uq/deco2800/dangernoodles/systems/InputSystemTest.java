package uq.deco2800.dangernoodles.systems;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.junit.Test;
import uq.deco2800.dangernoodles.FrameHandler;
import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.RectangleComponent;
import uq.deco2800.dangernoodles.components.StatusBarComponent;
import uq.deco2800.dangernoodles.components.displays.EffectDisplayComponent;
import uq.deco2800.dangernoodles.components.displays.ShopDisplayComponent;
import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.inputhandlers.KeyboardHandler;
import uq.deco2800.dangernoodles.inputhandlers.MouseHandler;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by Park on 10/24/2016.
 */
public class InputSystemTest {
    private World testworld = new World(0, 0);
    Canvas canvas = mock(Canvas.class);
    private FrameHandler framehandler = mock(FrameHandler.class);
    private MouseHandler mousehandler = mock(MouseHandler.class);

    private InputSystem inputSystem = new InputSystem(mousehandler, framehandler);

    @Test
    public void runTest() {
        testworld.addSystem(inputSystem, 0);


        List<StatusBarComponent> bars = testworld.getComponents(StatusBarComponent.class);
            List<PositionComponent> positions = testworld.getComponents(PositionComponent.class);

        for (ComponentMap p : testworld.getIterator(
        RectangleComponent.class,
                EffectDisplayComponent.class,
                ShopDisplayComponent.class

        )) {
            Optional<RectangleComponent> rectangleComponent = testworld.getComponent(p.getEntity(), RectangleComponent.class);
            Optional<EffectDisplayComponent> effectDisplayComponent = testworld.getComponent(p.getEntity(), EffectDisplayComponent.class);
            Optional<ShopDisplayComponent> shopDisplayComponent = testworld.getComponent(p.getEntity(), ShopDisplayComponent.class);
        }

        }

}