package uq.deco2800.dangernoodles.components.weather;

import org.junit.Test;

import uq.deco2800.dangernoodles.components.weather.WeatherComponent;

import static org.junit.Assert.*;

/**
 * Created by Melissa Nguyen on 18/09/2016.
 */
public class WeatherComponentTest {
    WeatherComponent weather = new WeatherComponent();
    
    /**
     * Test instantiation
     */
    @Test
    public void testInstantiation() {
    	assertTrue(weather != null);
    }
    
    /**
     * Test minimum length (ticks) of weather
     */
    @Test
    public void testMinimumLength() {
    	double minimumLength = 15;
    	assertTrue(minimumLength == weather.getMinWeatherLength());
    }

    /**
     * Test initial weather state
     */
    @Test
    public void testInitialWeatherState() {
    	float initialState = 0;
    	assertTrue(initialState == weather.getWeatherState());
    }
    
    /**
     * Test initial weather length
     */
    @Test
    public void testInitialLength() {
    	float initialLength = 0;
    	assertTrue(initialLength == weather.getLengthOfWeather());
    }

    /**
     * Test setting weather state valid
     */
    @Test
    public void testSetStateValid() {
    	int state = 2;
    	weather.setWeatherState(2);
    	assertTrue(state == weather.getWeatherState());
    }
    
    /**
     * Test setting weather state invalid
     */
    @Test(expected=IllegalArgumentException.class)
    public void testSetStateInvalid() {
    	int state = 5;
    	weather.setWeatherState(state);
    }
    
    /**
     * Test setting weather length valid
     */
    @Test
    public void testSetLengthValid() {
    	double length = 40;
    	weather.setLengthOfWeather(length);
    	assertTrue(length == weather.getLengthOfWeather());
    	assertTrue(weather.getLengthOfWeather() >= weather.getMinWeatherLength());
    }   
    
    /**
     * Test setting weather length invalid
     */
    @Test(expected=IllegalArgumentException.class)
    public void testSetLengthInvalid() {
    	double length = -3;
    	weather.setLengthOfWeather(length);
    }   
}