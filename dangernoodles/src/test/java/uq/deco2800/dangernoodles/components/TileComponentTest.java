package uq.deco2800.dangernoodles.components;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Siyu Liu on 18/09/2016.
 */
public class TileComponentTest {
    TileComponent tileComponent = new TileComponent(10, 20);

    /**
     * Testing initial x position of tile
     */
    @Test
    public void getXPosition(){
        int x = 10;
        int tileX = tileComponent.getXPosition();
        assertTrue(x == tileX);
    }

    /**
     * Testing initial y position of tile
     */
    @Test
    public void getYPosition(){
        int y = 20;
        int tileY = tileComponent.getYPosition();
        assertTrue(y == tileY);
    }

    /**
     * Test changing the x position of tile
     */
    @Test
    public void setXPosition(){
        int x = 15;
        tileComponent.setXPosition(15);
        int tileX = tileComponent.getXPosition();
        assertTrue(x == tileX);
    }

    /**
     * Test changing the y position of tile
     */
    @Test
    public void setYPosition(){
        int y = 25;
        tileComponent.setYPosition(25);
        int tileY = tileComponent.getYPosition();
        assertTrue(y == tileY);
    }

    /**
     * Testing initial existence value of tile
     */
    @Test
    public void isExist()  {
        boolean exist = true;
        boolean tileExist = tileComponent.isExist();
        assertTrue(exist == tileExist);
    }

    /**
     * Test changing the existence value of tile
     */
    @Test
    public void setExist()  {
        boolean exist = false;
        tileComponent.setExist(false);
        boolean tileExist = tileComponent.isExist();
        assertTrue(exist == tileExist);
    }

}