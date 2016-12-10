package uq.deco2800.dangernoodles.components.damages;

import org.junit.Test;
import uq.deco2800.dangernoodles.components.damages.InstantDamageComponent;
import uq.deco2800.dangernoodles.components.stats.ManaComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by khoi_truong on 2016/08/31.
 * <p>
 * This class is used to test out functionality of InstantDamageComponent.
 */
public class InstantDamageComponentTest {
    // A mini world to test out this component
    private World miniWorld = new World(1100, 800);
    // Object used to test out
    private InstantDamageComponent component;

    // Normal test with a working instance
    @Test
    public void Test01() {
        component = new InstantDamageComponent(10);
        // Test damage amount value
        assertEquals(10, component.getAmount(), 0);
    }

    // Exception with invalid argument when instantiate
    @Test(expected = IllegalArgumentException.class)
    public void Test02() {
        component = new InstantDamageComponent(-1);
    }

    // Working instance and then set value to new value
    @Test
    public void Test03() {
        component = new InstantDamageComponent(10);
        // Set new valid value
        component.setAmount(15);
        // Test damage amount value
        assertEquals(15, component.getAmount(), 0);
    }

    // Exception with invalid argument when set new amount
    @Test(expected = IllegalArgumentException.class)
    public void Test04() {
        component = new InstantDamageComponent(10);
        // Set new invalid value
        component.setAmount(-1);
    }

    // Test resetDefault method
    @Test
    public void Test05() {
        component = new InstantDamageComponent(10);
        // Set new valid value
        component.setAmount(15);
        // Then resetDefault
        component.resetDefault();
        // And test damage amount value
        assertEquals(10, component.getAmount(), 0);
    }

    // Test toString method
    @Test
    public void Test06() {
        component = new InstantDamageComponent(10);
        // Create an entity to add component
        Entity entity = miniWorld.createEntity();
        entity.addComponent(component);
        // Expected string to compare against
        String string = "Current entity: 0 deals an amount of 10.0.";
        // Then test value
      //  assertEquals(string, component.toString());
    }

    // Test equals method - working case
    @Test
    public void Test07() {
        component = new InstantDamageComponent(10);
        InstantDamageComponent another = new InstantDamageComponent(10);
        // Create an entity to add components
        Entity entity = miniWorld.createEntity();
        entity.addComponent(component);
        entity.addComponent(another);
        // Then test the method
        assertTrue(component.equals(another));
    }

    // Test equals method - null argument case
    @Test(expected = NullPointerException.class)
    public void Test08() {
        component = new InstantDamageComponent(10);
        InstantDamageComponent another = null;
        // Then test the method
        assertTrue(component.equals(another));
    }

    // Test equals method - wrong type of object
    @Test
    public void Test09() {
        component = new InstantDamageComponent(10);
        ManaComponent another = new ManaComponent(1000);
        // Then test the method
        assertFalse(component.equals(another));
    }

    // Test equal method - not equal case
    @Test
    public void Test10() {
        component = new InstantDamageComponent(10);
        InstantDamageComponent another = new InstantDamageComponent(15);
        // Create an entity to add components
        Entity entity = miniWorld.createEntity();
        entity.addComponent(component);
        entity.addComponent(another);
        // Then test the method
        assertFalse(component.equals(another));
    }

    // Test hash code method
    @Test
    public void Test11() {
        component = new InstantDamageComponent(10);
        // Create an entity to add components
        Entity entity = miniWorld.createEntity();
        entity.addComponent(component);
        // Then test the method
     //   assertEquals(3662260, component.hashCode());
    }

    // Test hash code method with null entity
    @Test(expected = NullPointerException.class)
    public void Test12() {
        component = new InstantDamageComponent(10);
        int hashCode = component.hashCode();
    }
}
