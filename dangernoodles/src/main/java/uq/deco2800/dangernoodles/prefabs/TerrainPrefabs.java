package uq.deco2800.dangernoodles.prefabs;

import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.components.stats.HealthComponent;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.ecs.Entity;

public class TerrainPrefabs {
    /**
     * Is a collection of prefabs to create terrain
     * 
     */
    private TerrainPrefabs(){

    }
    /**
     * Creates a Dirt block
     * 
     * @param world
     * 			Parent world object.
     * @param x
     * 			X position of where it will be created
     * @param y
     * 			Y position of where it will be created
     * @param SpriteSize
     * 			Size of the terrain block to be created
     * @return
     * 			The created world entity
     */
    public static Entity createDirt(World world, int x, int y, int spriteSize) {
        
    	return world.createEntity()
    			.addComponent(new PositionComponent(spriteSize * x, 500 + spriteSize * y))
    			.addComponent(new RectangleComponent(spriteSize, spriteSize))
    			.addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
    			.addComponent(new FrictionComponent(0.1, true))
    			.addComponent(new SpriteComponent(spriteSize,
    					spriteSize, "terrain/dirt.png"))
    			.addComponent(new HealthComponent(5))
    			.addComponent(new TileRenderComponent(x, y, true));
    }
    /**
     * Creates a Grass block
     * 
     * @param world
     * 			Parent world object.
     * @param x
     * 			X position of where it will be created
     * @param y
     * 			Y position of where it will be created
     * @param SpriteSize
     * 			Size of the terrain block to be created
     * @return
     * 			The created world entity
     */
	public static Entity createGrass(World world, int x, int y, int spriteSize) {
	        
	    	return world.createEntity()
	    			.addComponent(new PositionComponent(spriteSize * x, 500 + spriteSize * y))
	    			.addComponent(new RectangleComponent(spriteSize, spriteSize))
	    			.addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
	    			.addComponent(new FrictionComponent(0.1, true))
	    			.addComponent(new SpriteComponent(spriteSize,
	    					spriteSize, "terrain/grass.png"))
	    			.addComponent(new HealthComponent(5))
	    			.addComponent(new TileRenderComponent(x, y, true));
	    }
    /**
     * Creates a Rock block
     * 
     * @param world
     * 			Parent world object.
     * @param x
     * 			X position of where it will be created
     * @param y
     * 			Y position of where it will be created
     * @param SpriteSize
     * 			Size of the terrain block to be created
     * @return
     * 			The created world entity
     */
	public static Entity createRock(World world, int x, int y, int spriteSize) {
	        
	    	return world.createEntity()
	    			.addComponent(new PositionComponent(spriteSize * x, 500 + spriteSize * y))
	    			.addComponent(new RectangleComponent(spriteSize, spriteSize))
	    			.addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
	    			.addComponent(new FrictionComponent(0.1, true))
	    			.addComponent(new SpriteComponent(spriteSize,
	    					spriteSize, "terrain/rock.png"))
	    			.addComponent(new HealthComponent(7))
	    			.addComponent(new TileRenderComponent(x, y, true));
	    }
    /**
     * Creates a Underwater Rock block
     * 
     * @param world
     * 			Parent world object.
     * @param x
     * 			X position of where it will be created
     * @param y
     * 			Y position of where it will be created
     * @param SpriteSize
     * 			Size of the terrain block to be created
     * @return
     * 			The created world entity
     */
	public static Entity createUnderWaterRock(World world, int x, int y, int spriteSize) {
        
    	return world.createEntity()
    			.addComponent(new PositionComponent(spriteSize * x, 500 + spriteSize * y))
    			.addComponent(new RectangleComponent(spriteSize, spriteSize))
    			.addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
    			.addComponent(new FrictionComponent(0.1, true))
    			.addComponent(new SpriteComponent(spriteSize,
    					spriteSize, "terrain/waterrock.png"))
    			.addComponent(new HealthComponent(9))
    			.addComponent(new TileRenderComponent(x, y, true));
    }
    /**
     * Creates a SpaceRock block
     * 
     * @param world
     * 			Parent world object.
     * @param x
     * 			X position of where it will be created
     * @param y
     * 			Y position of where it will be created
     * @param SpriteSize
     * 			Size of the terrain block to be created
     * @return
     * 			The created world entity
     */
	public static Entity createSpaceRock(World world, int x, int y, int spriteSize) {
        
    	return world.createEntity()
    			.addComponent(new PositionComponent(spriteSize * x, 500 + spriteSize * y))
    			.addComponent(new RectangleComponent(spriteSize, spriteSize))
    			.addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
    			.addComponent(new FrictionComponent(0.1, true))
    			.addComponent(new SpriteComponent(spriteSize,
    					spriteSize, "terrain/spacerock.png"))
    			.addComponent(new HealthComponent(9))
    			.addComponent(new TileRenderComponent(x, y, true));
    }
    /**
     * Creates a Base Rock block
     * 
     * @param world
     * 			Parent world object.
     * @param x
     * 			X position of where it will be created
     * @param y
     * 			Y position of where it will be created
     * @param SpriteSize
     * 			Size of the terrain block to be created
     * @return
     * 			The created world entity
     */
	public static Entity createBaseRock(World world, int x, int y, int spriteSize) {
        
    	return world.createEntity()
    			.addComponent(new PositionComponent(spriteSize * x, 500 + spriteSize * y))
    			.addComponent(new RectangleComponent(spriteSize, spriteSize))
    			.addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
    			.addComponent(new FrictionComponent(0.1, true))
    			.addComponent(new SpriteComponent(spriteSize,
    					spriteSize, "terrain/base.png"))
    			.addComponent(new HealthComponent(1000)) //Almost Unbreakable
    			.addComponent(new TileRenderComponent(x, y, true));
    }
    /**
     * Creates a Sand block
     * 
     * @param world
     * 			Parent world object.
     * @param x
     * 			X position of where it will be created
     * @param y
     * 			Y position of where it will be created
     * @param SpriteSize
     * 			Size of the terrain block to be created
     * @return
     * 			The created world entity
     */
	public static Entity createSand(World world, int x, int y, int spriteSize) {
        
    	return world.createEntity()
    			.addComponent(new PositionComponent(spriteSize * x, 500 + spriteSize * y))
    			.addComponent(new RectangleComponent(spriteSize, spriteSize))
    			.addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
    			.addComponent(new FrictionComponent(0.1, true))
    			.addComponent(new SpriteComponent(spriteSize,
    					spriteSize, "terrain/sand.png"))
    			.addComponent(new HealthComponent(5))
    			.addComponent(new TileRenderComponent(x, y, true));
    }
    /**
     * Creates a MoonRock block
     * 
     * @param world
     * 			Parent world object.
     * @param x
     * 			X position of where it will be created
     * @param y
     * 			Y position of where it will be created
     * @param SpriteSize
     * 			Size of the terrain block to be created
     * @return
     * 			The created world entity
     */
	public static Entity createMoonRock(World world, int x, int y, int spriteSize) {
        
    	return world.createEntity()
    			.addComponent(new PositionComponent(spriteSize * x, 500 + spriteSize * y))
    			.addComponent(new RectangleComponent(spriteSize, spriteSize))
    			.addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
    			.addComponent(new FrictionComponent(0.1, true))
    			.addComponent(new SpriteComponent(spriteSize,
    					spriteSize, "terrain/moonrock.png"))
    			.addComponent(new HealthComponent(9))
    			.addComponent(new TileRenderComponent(x, y, true));
    }
    /**
     * Creates a HardRock block
     * 
     * @param world
     * 			Parent world object.
     * @param x
     * 			X position of where it will be created
     * @param y
     * 			Y position of where it will be created
     * @param SpriteSize
     * 			Size of the terrain block to be created
     * @return
     * 			The created world entity
     */
	public static Entity createHardRock(World world, int x, int y, int spriteSize) {
        
    	return world.createEntity()
    			.addComponent(new PositionComponent(spriteSize * x, 500 + spriteSize * y))
    			.addComponent(new RectangleComponent(spriteSize, spriteSize))
    			.addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
    			.addComponent(new FrictionComponent(0.1, true))
    			.addComponent(new SpriteComponent(spriteSize,
    					spriteSize, "terrain/hardrock.png"))
    			.addComponent(new HealthComponent(7))
    			.addComponent(new TileRenderComponent(x, y, true));
    }
    /**
     * Creates a DeepDirt block
     * 
     * @param world
     * 			Parent world object.
     * @param x
     * 			X position of where it will be created
     * @param y
     * 			Y position of where it will be created
     * @param SpriteSize
     * 			Size of the terrain block to be created
     * @return
     * 			The created world entity
     */
	public static Entity createDeepDirt(World world, int x, int y, int spriteSize) {
        
    	return world.createEntity()
    			.addComponent(new PositionComponent(spriteSize * x, 500 + spriteSize * y))
    			.addComponent(new RectangleComponent(spriteSize, spriteSize))
    			.addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
    			.addComponent(new FrictionComponent(0.1, true))
    			.addComponent(new SpriteComponent(spriteSize,
    					spriteSize, "terrain/deepdirt.png"))
    			.addComponent(new HealthComponent(5))
    			.addComponent(new TileRenderComponent(x, y, true));
    }
    /**
     * Creates a DeepGrass block
     * 
     * @param world
     * 			Parent world object.
     * @param x
     * 			X position of where it will be created
     * @param y
     * 			Y position of where it will be created
     * @param SpriteSize
     * 			Size of the terrain block to be created
     * @return
     * 			The created world entity
     */
	public static Entity createDeepGrass(World world, int x, int y, int spriteSize) {
        
    	return world.createEntity()
    			.addComponent(new PositionComponent(spriteSize * x, 500 + spriteSize * y))
    			.addComponent(new RectangleComponent(spriteSize, spriteSize))
    			.addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
    			.addComponent(new FrictionComponent(0.1, true))
    			.addComponent(new SpriteComponent(spriteSize,
    					spriteSize, "terrain/deepgrass.png"))
    			.addComponent(new HealthComponent(5))
    			.addComponent(new TileRenderComponent(x, y, true));
    }
    /**
     * Creates a Water block
     * 
     * @param world
     * 			Parent world object.
     * @param x
     * 			X position of where it will be created
     * @param y
     * 			Y position of where it will be created
     * @param SpriteSize
     * 			Size of the terrain block to be created
     * @return
     * 			The created world entity
     */
	public static Entity createWater(World world, int x, int y, int spriteSize) {
        
    	return world.createEntity()
    			.addComponent(new PositionComponent(spriteSize * x, 500 + spriteSize * y))
    			.addComponent(new RectangleComponent(spriteSize, spriteSize))
    			.addComponent(new FrictionComponent(0.5, true))
    			.addComponent(new HealthComponent(1))
    			.addComponent(new SpriteComponent(spriteSize,
    					spriteSize, "terrain/water.png"))
    			.addComponent(new TileRenderComponent(x, y, true));
    }
}