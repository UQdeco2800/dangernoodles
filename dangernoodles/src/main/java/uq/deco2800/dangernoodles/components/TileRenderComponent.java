package uq.deco2800.dangernoodles.components;

import uq.deco2800.dangernoodles.ecs.Component;

/**
 * Used to determine a tile's sprite to render.
 * Component used by the TileRenderSystem to determine the relative position of
 * a tile in the game, calculate the correct sprite to render and if collision
 * should be able to occur.
 */
public class TileRenderComponent extends Component{

    int spriteIndex; // assumes we are only using one type of terrain tile.
    double x; //represents the location of the tile (x position).
    double y; //represents the location of the tile (y position).
    boolean exists;

    /**
     * A constructor, x,y are not really necessary, should be taken from
     * a static position component.
     *
     * @param x the absolute x position of the tile
     * @param y the absolute y position of the tile
     *
     * */
    public TileRenderComponent(int x, int y){
        this.x=x;
        this.y=y;
        spriteIndex = 0b00000000;
        this.exists = true;
    }

    public TileRenderComponent(double x, double y,boolean exists){
        //ugly casts
        this.x= x;
        this.y= y;
        spriteIndex = 0b00000000;
        this.exists = exists;
    }

    /**
     * Function to get the relevant sprite for display by this tile.
     * @return A byte representing the index at which this tile's appropriate sprite is to be found.
     * */
    public int getIndex(){
        return spriteIndex;
    }

    /**
     * Function to determine if a tile exists for rendering purposes.
     * @return A boolean representing if this tile 'exists' (can be collided/should be rendered, etc)
     * */
    public boolean exists(){
        return this.exists;
    }
    /**
     * Used to 'remove' a tile from the game... this seems like it should be integrated with a 'tileComponent'.
     * */
    public void ceaseToExist(){
        this.exists = false;
    }


    /**
     * Sets the index of the tile to render appropriate sprite
     * @param index the value the index is to be set to, used as the output of a calculation of the tile's index.
     * */
    public void setIndex(int index){this.spriteIndex = index;}

    /**
     * Returns an int representing this tile's x position
     * @return this component's x position in the tilemap*/
    public int getX(){
        return (int) x;
    }
    /**
     * Returns an int representing this tile's y position
     * @return this component's y position in the tilemap*/
    public int getY(){
        return (int) y;
    }
}
