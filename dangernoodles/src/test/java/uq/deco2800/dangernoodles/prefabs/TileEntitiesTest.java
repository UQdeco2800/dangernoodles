package uq.deco2800.dangernoodles.prefabs;

import org.junit.Test;
import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

import static org.junit.Assert.*;

/**
 * Created by Siyu Liu on 18/09/2016.
 */
public class TileEntitiesTest {

    /*
    * Test for rectangle tile component
    * */
    @Test
    public void testNewRectangleTileEntity() throws Exception {
        World world = new World(1000, 1000);
        Entity rectangleTileEntity = TileEntities.createRectangleTile(world, 20, 10);
        assertTrue("Expected there to be a tile component",
                world.getComponent(rectangleTileEntity, TileComponent.class)
                        .isPresent());
        assertTrue("Expected there to be a tile render component",
                world.getComponent(rectangleTileEntity, TileRenderComponent.class)
                        .isPresent());
        assertTrue("Expected there to be a position component",
                world.getComponent(rectangleTileEntity, PositionComponent.class)
                        .isPresent());
        assertTrue("Expected there to be a collision component",
                world.getComponent(rectangleTileEntity, CollisionComponent.class)
                        .isPresent());
        /*assertTrue("Expected there to be a sprite component",
                world.getComponent(rectangleTileEntity, SpriteComponent.class)
                        .isPresent());*/
    }

    /*
    * Test for polygon tile component
    * */
    @Test
    public void testPolygonTileEntity() throws Exception {
        World world = new World(1000, 1000);
        Entity rectangleTileEntity = TileEntities.createPolygonTile(world, 30, 40);
        assertTrue("Expected there to be a tile component",
                world.getComponent(rectangleTileEntity, TileComponent.class)
                        .isPresent());
        assertTrue("Expected there to be a tile render component",
                world.getComponent(rectangleTileEntity, TileRenderComponent.class)
                        .isPresent());
        assertTrue("Expected there to be a position component",
                world.getComponent(rectangleTileEntity, PositionComponent.class)
                        .isPresent());
        assertTrue("Expected there to be a collision component",
                world.getComponent(rectangleTileEntity, CollisionComponent.class)
                        .isPresent());
        /*assertTrue("Expected there to be a sprite component",
                world.getComponent(rectangleTileEntity, SpriteComponent.class)
                        .isPresent());*/
    }
}