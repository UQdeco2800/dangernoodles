package uq.deco2800.dangernoodles.components;

import org.junit.Test;

import uq.deco2800.dangernoodles.components.weather.RainComponent;

import static org.junit.Assert.*;

/**
 * Created by Melissa Nguyen on 18/09/2016.
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