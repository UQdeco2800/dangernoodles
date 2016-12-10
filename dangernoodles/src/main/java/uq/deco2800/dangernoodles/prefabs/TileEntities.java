package uq.deco2800.dangernoodles.prefabs;

import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

/**
 * Created by Siyu Liu on 18/09/2016.
 */
public class TileEntities {

    /**
     * Private constructor to hide the implicit public one.
     */
    private TileEntities() {}

    /**
     * Creates a Rectangle Tile Entity in a given World, for a given x, y position
     *
     * @param world
     *            World in which to create tile
     * @param x the x position of the tile entity
     * @param y the y position of the tile entity
     *
     * @return The created tile entity
     */
    public static Entity createRectangleTile(World world, int x, int y) {
        //String imageName; //will implement the images later
        Entity tile = world.createEntity();
        TileComponent tileComponent = new TileComponent(x, y);
        tile.addComponent(tileComponent)
                .addComponent(new TileRenderComponent(x, y))
                .addComponent(new PositionComponent(x, y))
                .addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE));
        return tile;
    }

    /**
     * Creates a Polygon Tile Entity in a given World, for a given x, y position
     *
     * @param world
     *            World in which to create tile
     * @param x the x position of the tile entity
     * @param y the y position of the tile entity
     *
     * @return The created tile entity
     */
    public static Entity createPolygonTile(World world, int x, int y) {
        Entity tile = world.createEntity();
        TileComponent tileComponent = new TileComponent(x, y);
        tile.addComponent(tileComponent)
                .addComponent(new TileRenderComponent(x, y))
                .addComponent(new PositionComponent(x, y))
                .addComponent(new CollisionComponent(true, CollisionComponent.shape.POLYGON));
        return tile;
    }



}
