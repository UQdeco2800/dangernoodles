package uq.deco2800.dangernoodles.components.stats;

import org.junit.Test;
import uq.deco2800.dangernoodles.components.stats.InvulnerableComponent;
import uq.deco2800.dangernoodles.components.stats.ManaComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

import static org.junit.Assert.*;

/**
 * Created by khoi_truong on 2016/08/30.
 *
 * This class is used to test out functionality of InvulnerableComponent.
 */
public class InvulnerableComponentTest {

    // Private field to store information
    private InvulnerableComponent component;


    // Normal test with a working instance
    @Test
    public void Test01() {
        component = new InvulnerableComponent();
        assertFalse(component.isInvulnerable());
    }

    // Set invulnerable status
    @Test
    public void Test02() {
        component = new InvulnerableComponent();
        component.setInvulnerable(true);
        assertTrue(component.isInvulnerable());
    }

    // Test resetDefault method
    @Test
    public void Test03() {
        component = new InvulnerableComponent();
        component.setInvulnerable(true);
        component.resetDefault();
        assertFalse(component.isInvulnerable());
    }

    // Test toString method
    @Test
    public void Test04() {
        component = new InvulnerableComponent();
        // Expected string to compare against
        String string = "Current entity is vulnerable.";
        // Then test value
        assertEquals(string, component.toString());
        // Set status to true and test the string output again
        component.setInvulnerable(true);
        string = "Current entity is invulnerable.";
        assertEquals(string, component.toString());
    }

    // Test equals method - working case
    @Test
    public void Test05() {
        component = new InvulnerableComponent();
        InvulnerableComponent another = new InvulnerableComponent();
        // Then test the method
        assertTrue(component.equals(another));
    }

    // Test equals method - wrong type of object
    @Test
    public void Test06() {
        component = new InvulnerableComponent();
        ManaComponent another = new ManaComponent(1000);
        // Then test the method
        assertFalse(component.equals(another));
    }

    // Test equal method - not equal case
    @Test
    public void Test07() {
        component = new InvulnerableComponent();
        InvulnerableComponent another = new InvulnerableComponent();
        another.setInvulnerable(true);
        // Then test the method
        assertFalse(component.equals(another));
    }

    // Test hash code method
    @Test
    public void Test08() {
        component = new InvulnerableComponent();
        // Then test the method
        assertEquals(1449956, component.hashCode());
    }
}
