package uq.deco2800.dangernoodles.components.stats;

import org.junit.Test;
import uq.deco2800.dangernoodles.components.stats.ManaComponent;
import uq.deco2800.dangernoodles.components.stats.SpeedComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

import static org.junit.Assert.*;

/**
 * Created by khoi_truong on 2016/08/30.
 * <p>
 * This class is used to test out functionality of SpeedComponent
 */
public class SpeedComponentTest {
    // Object used to test out
    private SpeedComponent component;

    // Normal test with a working instance
    @Test
    public void Test01() {
        component = new SpeedComponent(10);
        // Test speed value
        assertEquals(10, component.getSpeed(), 0);
    }

    // Exception with invalid argument when instantiate
    @Test(expected = IllegalArgumentException.class)
    public void Test02() {
        component = new SpeedComponent(-1);
    }

    // Working instance and then set value to new value
    @Test
    public void Test03() {
        component = new SpeedComponent(10);
        // Set new valid value
        component.setSpeed(15);
        // Test speed value
        assertEquals(15, component.getSpeed(), 0);
    }

    // Exception with invalid argument when set new speed
    @Test(expected = IllegalArgumentException.class)
    public void Test04() {
        component = new SpeedComponent(10);
        // Set new invalid value
        component.setSpeed(-1);
    }

    // Test resetDefault method
    @Test
    public void Test05() {
        component = new SpeedComponent(10);
        // Set new valid value
        component.setSpeed(15);
        // Then resetDefault
        component.resetDefault();
        // And test speed value
        assertEquals(10, component.getSpeed(), 0);
    }

    // Test toString method
    @Test
    public void Test06() {
        component = new SpeedComponent(10);
        // Expected string to compare against
        String string = "Current entity with speed of 10.0.";
        // Then test value
        assertEquals(string, component.toString());
    }

    // Test equals method - working case
    @Test
    public void Test07() {
        component = new SpeedComponent(10);
        SpeedComponent another = new SpeedComponent(10);
        // Then test the method
        assertTrue(component.equals(another));
    }

    // Test equals method - null argument case
    @Test(expected = NullPointerException.class)
    public void Test08() {
        component = new SpeedComponent(10);
        SpeedComponent another = null;
        // Then test the method
        assertTrue(component.equals(another));
    }

    // Test equals method - wrong type of object
    @Test
    public void Test09() {
        component = new SpeedComponent(10);
        ManaComponent another = new ManaComponent(1000);
        // Then test the method
        assertFalse(component.equals(another));
    }

    // Test equal method - not equal case
    @Test
    public void Test10() {
        // First case: both speed and defaultSpeed values are different
        component = new SpeedComponent(10);
        SpeedComponent another = new SpeedComponent(15);
        // Then test the method
        assertFalse(component.equals(another));

        // Second case: speed values are the same, but defaultSpeed values are different
        component = new SpeedComponent(15);
        another = new SpeedComponent(10);
        another.setSpeed(15);
        // Test the method
        assertFalse(component.equals(another));

        // Third case: speed values are different, but defaultSpeed values are the same
        component = new SpeedComponent(10);
        another = new SpeedComponent(10);
        another.setSpeed(15);
        // Test the method
        assertFalse(component.equals(another));
    }

    // Test hash code method
    @Test
    public void Test11() {
        component = new SpeedComponent(10);
        // Then test the method
        assertEquals(14060, component.hashCode());
    }
}
