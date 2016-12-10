package uq.deco2800.dangernoodles.components.damages;

import org.junit.Test;
import uq.deco2800.dangernoodles.components.stats.ManaComponent;
import uq.deco2800.dangernoodles.components.damages.OvertimeDamageComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by khoi_truong on 2016/08/31.
 * <p>
 * This class is used to test out functionality of OvertimeDamageComponent.
 */
public class OvertimeDamageComponentTest {
    // A mini world to test out this component
    private World miniWorld = new World(1100, 800);
    // Object used to test out
    private OvertimeDamageComponent component;

    // Normal test with a working instance
    @Test
    public void Test01() {
        component = new OvertimeDamageComponent(10, 10);
        assertEquals(10, component.getAmount(), 0);
        assertEquals(10, component.getDuration(), 0);
    }

    // Exception with invalid argument when instantiate - negative amount
    @Test(expected = IllegalArgumentException.class)
    public void Test02() {
        component = new OvertimeDamageComponent(-1, 10);
    }

    // Exception with invalid argument when instantiate - negative duration
    @Test(expected = IllegalArgumentException.class)
    public void Test03() {
        component = new OvertimeDamageComponent(10, -1);
    }

    // Working instance and then set value to new value
    @Test
    public void Test04() {
        component = new OvertimeDamageComponent(10, 10);
        // Set new valid value
        component.setAmount(15);
        component.setDuration(15);
        assertEquals(15, component.getAmount(), 0);
        assertEquals(15, component.getDuration(), 0);
    }

    // Exception with invalid argument when set new damage
    @Test(expected = IllegalArgumentException.class)
    public void Test05() {
        component = new OvertimeDamageComponent(10, 10);
        // Set new invalid value
        component.setAmount(-1);
    }

    // Exception with invalid argument when set new duration
    @Test(expected = IllegalArgumentException.class)
    public void Test06() {
        component = new OvertimeDamageComponent(10, 10);
        // Set new invalid value
        component.setDuration(-1);
    }

    // Test resetDefault method
    @Test
    public void Test07() {
        component = new OvertimeDamageComponent(10, 10);
        // Set new valid value
        component.setAmount(15);
        component.setDuration(15);
        // Then resetDefault
        component.resetDefault();
        assertEquals(10, component.getAmount(), 0);
        assertEquals(10, component.getDuration(), 0);
    }

    // Test toString method
    @Test
    public void Test08() {
        component = new OvertimeDamageComponent(10, 10);
        // Create an entity to add component
        Entity entity = miniWorld.createEntity();
        entity.addComponent(component);
        // Expected string to compare against
        String string = "Current entity: 0 deals an amount of 10.0 over a duration of 10.0.";
        // Then test value
      //  assertEquals(string, component.toString());
    }

    // Test equals method - working case
    @Test
    public void Test09() {
        component = new OvertimeDamageComponent(10, 10);
        OvertimeDamageComponent another = new OvertimeDamageComponent(10, 10);
        // Create an entity to add components
        Entity entity = miniWorld.createEntity();
        entity.addComponent(component);
        entity.addComponent(another);
        // Then test the method
        assertTrue(component.equals(another));
    }

    // Test equals method - null argument case
    @Test(expected = NullPointerException.class)
    public void Test10() {
        component = new OvertimeDamageComponent(10, 10);
        OvertimeDamageComponent another = null;
        // Then test the method
        assertTrue(component.equals(another));
    }

    // Test equals method - wrong type of object
    @Test
    public void Test11() {
        component = new OvertimeDamageComponent(10, 10);
        ManaComponent another = new ManaComponent(1000);
        // Then test the method
        assertFalse(component.equals(another));
    }

    // Test equal method - not equal case
    @Test
    public void Test12() {
        component = new OvertimeDamageComponent(10, 10);
        OvertimeDamageComponent another = new OvertimeDamageComponent(15, 15);
        // Create an entity to add components
        Entity entity = miniWorld.createEntity();
        entity.addComponent(component);
        entity.addComponent(another);
        // Then test the method
        assertFalse(component.equals(another));
    }

    // Test hash code method
    @Test
    public void Test13() {
        component = new OvertimeDamageComponent(10, 10);
        // Create an entity to add components
        Entity entity = miniWorld.createEntity();
        entity.addComponent(component);
        // Then test the method
       // assertEquals(2147483647, component.hashCode());
    }

    // Test hash code method with null entity
    @Test(expected = NullPointerException.class)
    public void Test14() {
        component = new OvertimeDamageComponent(10, 10);
        int hashCode = component.hashCode();
    }
}
