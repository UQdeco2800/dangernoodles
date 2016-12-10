package uq.deco2800.dangernoodles.components.stats;

import org.junit.Test;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by khoi_truong on 2016/09/18.
 *
 * This class is used to test out functionality of DamageComponent.
 */
public class DamageComponentTest {
    // Object used to test out
    private DamageComponent component;

    // Normal test with a working instance
    @Test
    public void Test01() {
        component = new DamageComponent(10);
        // Test damage value
        assertEquals(10, component.getDamage(), 0);
    }

    // Exception with invalid argument when instantiate
    @Test(expected = IllegalArgumentException.class)
    public void Test02() {
        component = new DamageComponent(-1);
    }

    // Working instance and then set value to new value
    @Test
    public void Test03() {
        component = new DamageComponent(10);
        // Set new valid value
        component.setDamage(15);
        // Test damage value
        assertEquals(15, component.getDamage(), 0);
    }

    // Exception with invalid argument when set new damage
    @Test(expected = IllegalArgumentException.class)
    public void Test04() {
        component = new DamageComponent(10);
        // Set new invalid value
        component.setDamage(-1);
    }

    // Test resetDefault method
    @Test
    public void Test05() {
        component = new DamageComponent(10);
        // Set new valid value
        component.setDamage(15);
        // Then resetDefault
        component.resetDefault();
        // And test damage value
        assertEquals(10, component.getDamage(), 0);
    }

    // Test toString method
    @Test
    public void Test06() {
        component = new DamageComponent(10);
        // Expected string to compare against
        String string = "Current entity with damage of 10.0.";
        // Then test value
        assertEquals(string, component.toString());
    }

    // Test equals method - working case
    @Test
    public void Test07() {
        component = new DamageComponent(10);
        DamageComponent another = new DamageComponent(10);
        // Then test the method
        assertTrue(component.equals(another));
    }

    // Test equals method - null argument case
    @Test(expected = NullPointerException.class)
    public void Test08() {
        component = new DamageComponent(10);
        SpeedComponent another = null;
        // Then test the method
        assertTrue(component.equals(another));
    }

    // Test equals method - wrong type of object
    @Test
    public void Test09() {
        component = new DamageComponent(10);
        ManaComponent another = new ManaComponent(1000);
        // Then test the method
        assertFalse(component.equals(another));
    }

    // Test equal method - not equal case
    @Test
    public void Test10() {
        // First case: both damage and defaultDamage values are different
        component = new DamageComponent(10);
        DamageComponent another = new DamageComponent(15);
        // Then test the method
        assertFalse(component.equals(another));

        // Second case: damage values are the same, but defaultDamage values are different
        component = new DamageComponent(15);
        another = new DamageComponent(10);
        another.setDamage(15);
        // Test the method
        assertFalse(component.equals(another));

        // Third case: damage values are different, but defaultDamage values are the same
        component = new DamageComponent(10);
        another = new DamageComponent(10);
        another.setDamage(15);
        // Test the method
        assertFalse(component.equals(another));
    }

    // Test hash code method
    @Test
    public void Test11() {
        component = new DamageComponent(10);
        // Then test the method
        assertEquals(14060, component.hashCode());
    }
}
