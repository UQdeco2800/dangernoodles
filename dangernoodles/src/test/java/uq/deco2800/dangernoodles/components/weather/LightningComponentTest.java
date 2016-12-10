package uq.deco2800.dangernoodles.components.weather;

import org.junit.Test;

import uq.deco2800.dangernoodles.components.weather.*;

import static org.junit.Assert.*;

/**
 * 
 * @author Created by Robert Cochran
 *
 */

public class LightningComponentTest {

	LightningComponent lightning = new LightningComponent(5);
	
    /**
     * Test instantiation
     */
    @Test
    public void testInstantiation() {
    	assertTrue(lightning != null);
    }
    
    /**
     * Test the damage returned when LightningComponent is instantiated 
     */
    @Test
    public void testReturnedDamage() {
    	int damage = 5;
    	assertTrue(lightning.getDamage() == damage);
    }

    /**
     * Test the damage set using setDamage is true for greater than 0.05
     */
    @Test
    public void testHighSetDamage() {
    	double damage = 8;
    	lightning.setDamage(damage);
    	assertTrue(lightning.getDamage() == damage && lightning.getIfDamagable() == true);
    }
    
    /**
     * Test the damage set using setDamage is true for less than 0.05
     */
    @Test
    public void testLowSetDamage() {
    	double damage = 0.01;
    	lightning.setDamage(damage);
    	assertTrue(lightning.getDamage() == 0 && lightning.getIfDamagable() == false);
    }
	
    /**
     * Test the setDuration function
     */
    @Test
    public void testSetDuration() {
    	assertTrue(lightning.getDuration() == 0);
    }
    
    /**
     * Test the Duration increment function
     */
    @Test
    public void testIncDuration() {
    	lightning.IncrementDuration();
    	lightning.IncrementDuration();
    	assertTrue(lightning.getDuration() == 2);
    }
}
