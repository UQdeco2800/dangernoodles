package uq.deco2800.dangernoodles.components.weather;

import org.junit.Test;

import uq.deco2800.dangernoodles.components.weather.RainingDuckComponent;

import static org.junit.Assert.*;

public class RainingDuckComponentTest {
	
	RainingDuckComponent duck = new RainingDuckComponent();

    /**
     * Test instantiation
     */
    @Test
    public void testInstantiation() {
    	assertTrue(duck != null);
    }
}
