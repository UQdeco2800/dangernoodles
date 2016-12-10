package uq.deco2800.dangernoodles.components.weather;

import org.junit.Test;

import uq.deco2800.dangernoodles.components.weather.RainComponent;

import static org.junit.Assert.*;

/**
 * 
 * @author Created by Robert Cochran
 *
 */
public class RainComponentTest {
	
	RainComponent rain = new RainComponent();

    /**
     * Test instantiation
     */
    @Test
    public void testInstantiation() {
    	assertTrue(rain != null);
    }
}
