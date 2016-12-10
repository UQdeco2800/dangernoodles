package uq.deco2800.dangernoodles.components;

import uq.deco2800.dangernoodles.ecs.Component;

import java.awt.*;

/**
 * Created by Siyu Liu on 18/09/2016.
 */
public class TileComponent extends Component {
    public static final int TILESIZE = 10;//pixels in a tile, might need to change later
    private int xPosition;//represents the x position of a tile
    private int yPosition;//represents the y position of a tile
    private boolean exist;//existence of the tile
    private static Image image;//image loaded in the tile



    /**
     * Constructor
     * @param x the x position of the tile.
     * @param y the y position of the tile.
     * */
    public TileComponent(int x, int y){
        this.xPosition = x;
        this.yPosition = y;
        this.exist = true;
    }

    /**
     * Get the tile's x position
     * @return this component's x position in the tilemap
     * */
    public int getXPosition() {
        return xPosition;
    }

    /**
     * Get the tile's y position
     * @return this component's y position in the tilemap
     * */
    public int getYPosition() {
        return yPosition;
    }

    /**
     * Set the tile's x position
     * @param xPosition this component's new x position in the tilemap
     * */
    public void setXPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    /**
     * Set the tile's y position
     * @param yPosition this component's new y position in the tilemap
     * */
    public void setYPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    /**
     * Function to determine if a tile exists.
     * @return A boolean representing if this tile 'exists' (can be collided/should be rendered, etc)
     * */
    public boolean isExist() {
        return exist;
    }

    /**
     * Function to change the existence state of the tile.
     * @param exist the new state of the tile.
     * */
    public void setExist(boolean exist) {
        this.exist = exist;
    }
    /**
     * Set the tile's image
     * @param image this component's image
     * */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Get the tile's image in order to know what type of image is stored in the tile
     * @return A string of this component's image name
     * */
    public String getImage() {
        return image.toString();//might need to change this later
    }

    /**
     * Get the tile's size for further calculations
     * @return The tile size
     * */
    public static int getTileSize() {
        return TILESIZE;
    }
}
