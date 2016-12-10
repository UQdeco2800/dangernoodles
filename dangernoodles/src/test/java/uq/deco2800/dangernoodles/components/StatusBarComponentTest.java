package uq.deco2800.dangernoodles.components;

import javafx.scene.paint.Color;
import org.junit.Test;
import uq.deco2800.dangernoodles.components.stats.HealthComponent;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the component StatusBarComponent.
 */
public class StatusBarComponentTest {

    /**
     * Tests base case (instance creation).
     */
    @Test
    public void BaseCaseTest() {
        StatusBarComponent component = new StatusBarComponent("", 100, 10, 100,
                Color.LIMEGREEN, Color.RED, false, new PositionComponent(-25, -55));

        assertEquals(100, (int)component.getWidth());
        assertEquals(10, (int)component.getHeight());
        assertEquals(100, component.getMaxValue());
        assertEquals(Color.LIMEGREEN, component.getStartColor());
        assertEquals(Color.RED, component.getEndColor());
        assertEquals(-25, (int)component.getOffsetX());
        assertEquals(-55, (int)component.getOffsetY());
    }
}
