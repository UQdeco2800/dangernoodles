package uq.deco2800.dangernoodles.components.weather;

import org.junit.Test;

import uq.deco2800.dangernoodles.components.weather.WindComponent;

import static org.junit.Assert.*;

/**
 * Created by Melissa Nguyen on 18/09/2016.
 */
public class WindComponentTest {
    WindComponent wind = new WindComponent();
    
    /**
     * Test instantiation
     */
    @Test
    public void testInstantiation() {
    	assertTrue(wind != null);
    }

    /**
     * Test initial strength of wind
     */
    @Test
    public void testInitialStrength() {
    	float initialStrength = 0;
    	assertTrue(initialStrength == wind.getStrength());
    }
    
    /**
     * Test initial direction of wind
     */
    @Test
    public void testInitialDirection() {
    	int initialDirection = 0;
    	assertTrue(initialDirection == wind.getDirection());
    }
    
    /**
     * Test initial length of wind
     */
    @Test
    public void testInitialLength() {
    	double initialLength = 0;
    	assertTrue(initialLength == wind.getLength());
    }
    
    /**
     * Test minimum length (ticks) of wind
     */
    @Test
    public void testMinimumLength() {
    	double minimumLength = 10;
    	assertTrue(minimumLength == wind.getMinWindLength());
    }

    /**
     * Test setting wind strength valid
     */
    @Test
    public void testSetStrengthValid() {
    	float strength = (float)0.39;
    	wind.setStrength(strength);
    	assertTrue(strength == wind.getStrength());
    }

    /**
     * Test setting wind strength invalid
     */
    @Test(expected=IllegalArgumentException.class)
    public void testSetStrengthInvalid() {
    	float strength = (float)-3;
    	wind.setStrength(strength);
    }
    
    /**
     * Test setting wind direction valid
     */
    @Test
    public void testSetDirectionValid() {
    	int direction = -1;
    	wind.setDirection(direction);
    	assertTrue(direction == wind.getDirection());
    }
    
    /**
     * Test setting wind direction invalid
     */
    @Test(expected=IllegalArgumentException.class)
    public void testSetDirectionInvalid() {
    	int direction = -5;
    	wind.setDirection(direction);
    }
    
    /**
     * Test setting wind length valid
     */
    @Test
    public void testSetLengthValid() {
    	double length = 15;
    	wind.setLength(length);
    	assertTrue(length == wind.getLength());
    	assertTrue(wind.getLength() >= wind.getMinWindLength());
    }
    
    /**
     * Test setting wind length invalid
     */
    @Test(expected=IllegalArgumentException.class)
    public void testSetLengthInvalid() {
    	double length = -8;
    	wind.setLength(length);
    }
}