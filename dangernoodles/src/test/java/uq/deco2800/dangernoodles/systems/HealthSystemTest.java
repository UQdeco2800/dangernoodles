package uq.deco2800.dangernoodles.systems;

import javafx.scene.paint.Color;
import org.junit.Test;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.StatusBarComponent;
import uq.deco2800.dangernoodles.components.stats.HealthComponent;
import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.World;

import static org.junit.Assert.assertEquals;

/**
 * This test suite ensures that the health system correctly updates health bars.
 * Created by Team Mighty Ducks on 24/10/16.
 */
public class HealthSystemTest {
    private HealthSystem healthSystem = new HealthSystem();

    @Test
    public void testHealth() {
        World testWorld = new World(0, 0);
        testWorld.addSystem(healthSystem, 1);
        PositionComponent healthBarPosition = new PositionComponent(50 / 2 - 100 / 2, -55);
        testWorld.createEntity()
                .addComponent(new HealthComponent(50))
                .addComponent(new StatusBarComponent("Compact", 100, 10, 100, Color.LIMEGREEN,
                        Color.RED, false, healthBarPosition));
        healthSystem.run(testWorld, 0, 0);
        for (ComponentMap cm : testWorld.getIterator(StatusBarComponent.class)) {
            StatusBarComponent statusBar = cm.get(StatusBarComponent.class);
            assertEquals(statusBar.getValue(), 50);
        }
    }
}
