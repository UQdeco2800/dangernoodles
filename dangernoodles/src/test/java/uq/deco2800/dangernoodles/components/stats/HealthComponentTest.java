package uq.deco2800.dangernoodles.components.stats;

import org.junit.Test;
import uq.deco2800.dangernoodles.components.stats.HealthComponent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the component HealthComponent.
 */
public class HealthComponentTest {
    private HealthComponent component;

    /**
     * Tests base case (instance creation).
     */
    @Test
    public void BaseCaseTest() {
        int maxHealth = 100;
        component = new HealthComponent(maxHealth);
        assertEquals(maxHealth, component.getHealth());
    }

    /**
     * Test bad initialisation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void BadInitTest() {
        component = new HealthComponent(-1);
    }

    /**
     * Test set health with positive value.
     */
    @Test
    public void SetPositiveHealthTest() {
        int setHealthValue = 65;
        int maxHealth = 100;
        component = new HealthComponent(maxHealth);
        component.setHealth(setHealthValue);

        assertEquals(setHealthValue, component.getHealth());
        assertEquals(maxHealth, component.getMaxHealth());
    }

    /**
     * Test set health with negative value.
     */
    @Test
    public void TestInvalidSetHealth() {
        int setHealthValue = -1;
        component = new HealthComponent(100);
        component.setHealth(setHealthValue);

        // health value should be capped at 0.
        assertEquals(0, component.getHealth());
        assertEquals(100, component.getMaxHealth());
    }

    /**
     * Test reset health.
     */
    @Test
    public void ResetHealthTest() {
        int maxHealth = 100;
        component = new HealthComponent(maxHealth);
        component.setHealth(15);
        component.resetHealth();

        assertEquals(maxHealth, component.getHealth());
    }
}
