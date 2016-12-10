package uq.deco2800.dangernoodles.components;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by minhnguyen on 20/10/2016.
 */
public class CircleComponentTest {

    CircleComponent circComp = new CircleComponent(10.10);
    double initRadius = 10.10;
    double newRadius = 30.10;

    /**
     * Test for retrieving circle radius
     */
    @Test
    public void getRadius() {
         assertEquals(initRadius, circComp.getRadius(), 0.1);
    }

    /**
     * Test for setting new circle radius
     */
    @Test
    public void setRadius(){
        circComp.setRadius(30.10);
        assertEquals(newRadius,circComp.getRadius(), 0.1);

    }

}