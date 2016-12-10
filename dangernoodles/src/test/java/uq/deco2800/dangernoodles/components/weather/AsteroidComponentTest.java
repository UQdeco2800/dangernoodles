package uq.deco2800.dangernoodles.components.weather;

import org.junit.Test;

import uq.deco2800.dangernoodles.components.weather.AsteroidComponent;

import static org.junit.Assert.*;

/**
 * Created by Melissa Nguyen 
 */
public class AsteroidComponentTest {
    AsteroidComponent asteroid = new AsteroidComponent();

    /**
     * Test instantiation
     */
    @Test
    public void testInstantiation() {
    	assertTrue(asteroid != null);
    }
}