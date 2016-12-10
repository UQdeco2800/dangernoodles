package uq.deco2800.dangernoodles.components.weather;

import org.junit.Test;

import uq.deco2800.dangernoodles.components.weather.SnowflakeComponent;

import static org.junit.Assert.*;

/**
 * Created by Melissa Nguyen on 18/09/2016.
 */
public class SnowflakeComponentTest {
    SnowflakeComponent snowflake = new SnowflakeComponent();

    /**
     * Test instantiation
     */
    @Test
    public void testInstantiation() {
    	assertTrue(snowflake != null);
    }
}