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
 * This class is used to test out functionality of ManaComponent.
 */
public class ManaComponentTest {
    // Private field to store information
    private ManaComponent component;

    // Normal test with a working instance
    @Test
    public void Test01() {
        component = new ManaComponent(1000);
        assertEquals(1000, component.getCapacity());
        assertEquals(1000, component.getMana());
    }

    // Exception with invalid capacity when instantiate
    @Test(expected = IllegalArgumentException.class)
    public void Test02() {
        component = new ManaComponent(-1);
    }

    // Working instance then set valid capacity
    @Test
    public void Test03() {
        component = new ManaComponent(1000);
        component.setCapacity(1500);
        assertEquals(1500, component.getCapacity());
    }

    // Exception when set invalid capacity
    @Test(expected = IllegalArgumentException.class)
    public void Test04() {
        component = new ManaComponent(1000);
        component.setCapacity(-1);
    }

    // Working instance then set valid capacity that is less than current mana
    @Test
    public void Test05() {
        component = new ManaComponent(1000);
        component.setMana(700);
        component.setCapacity(600);
        assertEquals(600, component.getCapacity());
    }

    // Working instance then set valid mana
    @Test
    public void Test06() {
        component = new ManaComponent(1000);
        component.setMana(500);
        assertEquals(500, component.getMana());
    }

    // Exception when set invalid mana
    @Test(expected = IllegalArgumentException.class)
    public void Test07() {
        component = new ManaComponent(1000);
        component.setMana(-1);
    }

    // Working instance then set valid mana that is more than capacity
    @Test
    public void Test08() {
        component = new ManaComponent(1000);
        component.setCapacity(1100);
        assertEquals(1000, component.getMana());
    }

    // Test resetDefault method
    @Test
    public void Test09() {
        component = new ManaComponent(1000);
        component.setMana(500);
        component.reset();
        assertEquals(1000, component.getMana());
    }

    // Test resetDefault method - used to resetDefault capacity to its default value
    @Test
    public void Test10() {
        component = new ManaComponent(1000);
        component.setCapacity(500);
        component.resetDefault();
        assertEquals(1000, component.getCapacity());
    }

    // Test toString method
    @Test
    public void Test11() {
        component = new ManaComponent(1000);
        // Expected string to compare against
        String string = "Current entity with current mana of 1000 of a " +
                "maximum capacity of 1000.";
        // Then test value
        assertEquals(string, component.toString());
    }

    // Test equals method - working case
    @Test
    public void Test12() {
        component = new ManaComponent(1000);
        ManaComponent another = new ManaComponent(1000);
        // Then test the method
        assertTrue(component.equals(another));
    }

    // Test equals method - null argument case
    @Test(expected = NullPointerException.class)
    public void Test13() {
        component = new ManaComponent(1000);
        ManaComponent another = null;
        // Then test the method
        assertTrue(component.equals(another));
    }

    // Test equals method - wrong type of object
    @Test
    public void Test14() {
        component = new ManaComponent(1000);
        SpeedComponent another = new SpeedComponent(10);
        // Then test the method
        assertFalse(component.equals(another));
    }

    // Test equal method - not equal case
    @Test
    public void Test15() {
        component = new ManaComponent(1000);
        ManaComponent another = new ManaComponent(1500);
        // Then test the method
        assertFalse(component.equals(another));
    }

    // Test hash code method
    @Test
    public void test16() {
        component = new ManaComponent(1000);
        // Then test the method
        assertEquals(1223603328, component.hashCode());
    }
}
