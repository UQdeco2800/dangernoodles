package uq.deco2800.dangernoodles.prefabs;

import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.components.stats.HealthComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * Test class for the Angel Entity Prefab.
 * @author Paul Haley
 */
public class TerrainBlocksTest {

    /**
     * Tests base case (instance creation) and that all components expected are present.
     */
    @Test
    public void baseCreationTest() {

        World miniWorld = new World(100,100);
        Entity dirt = TerrainPrefabs.createDirt(miniWorld, 0, 0, 25);
        Entity grass = TerrainPrefabs.createGrass(miniWorld, 0, 0, 25);
        Entity rock = TerrainPrefabs.createRock(miniWorld, 0, 0, 25);
        Entity underWaterRock = TerrainPrefabs.createUnderWaterRock(miniWorld, 0, 0, 25);
        Entity spaceRock = TerrainPrefabs.createSpaceRock(miniWorld, 0, 0, 25);
        Entity baseRock = TerrainPrefabs.createBaseRock(miniWorld, 0, 0, 25);
        Entity sand = TerrainPrefabs.createSand(miniWorld, 0, 0, 25);
        Entity moonRock = TerrainPrefabs.createMoonRock(miniWorld, 0, 0, 25);
        Entity hardRock = TerrainPrefabs.createHardRock(miniWorld, 0, 0, 25);
        Entity deepDirt = TerrainPrefabs.createDeepDirt(miniWorld, 0, 0, 25);
        Entity deepGrass = TerrainPrefabs.createDeepGrass(miniWorld, 0, 0, 25);
        Entity water = TerrainPrefabs.createWater(miniWorld, 0, 0, 25);

        
        assertTrue(miniWorld.hasComponent(dirt, PositionComponent.class));
        assertTrue(miniWorld.hasComponent(dirt, CollisionComponent.class));
        assertTrue(miniWorld.hasComponent(dirt, SpriteComponent.class));
        assertTrue(miniWorld.hasComponent(dirt, RectangleComponent.class));
        assertTrue(miniWorld.hasComponent(dirt, FrictionComponent.class));
        assertTrue(miniWorld.hasComponent(dirt, HealthComponent.class));
        assertTrue(miniWorld.hasComponent(dirt, TileRenderComponent.class));
        
        assertTrue(miniWorld.hasComponent(grass, PositionComponent.class));
        assertTrue(miniWorld.hasComponent(grass, CollisionComponent.class));
        assertTrue(miniWorld.hasComponent(grass, SpriteComponent.class));
        assertTrue(miniWorld.hasComponent(grass, RectangleComponent.class));
        assertTrue(miniWorld.hasComponent(grass, FrictionComponent.class));
        assertTrue(miniWorld.hasComponent(grass, HealthComponent.class));
        assertTrue(miniWorld.hasComponent(grass, TileRenderComponent.class));
        
        assertTrue(miniWorld.hasComponent(rock, PositionComponent.class));
        assertTrue(miniWorld.hasComponent(rock, CollisionComponent.class));
        assertTrue(miniWorld.hasComponent(rock, SpriteComponent.class));
        assertTrue(miniWorld.hasComponent(rock, RectangleComponent.class));
        assertTrue(miniWorld.hasComponent(rock, FrictionComponent.class));
        assertTrue(miniWorld.hasComponent(rock, HealthComponent.class));
        assertTrue(miniWorld.hasComponent(rock, TileRenderComponent.class));
        
        assertTrue(miniWorld.hasComponent(underWaterRock, PositionComponent.class));
        assertTrue(miniWorld.hasComponent(underWaterRock, CollisionComponent.class));
        assertTrue(miniWorld.hasComponent(underWaterRock, SpriteComponent.class));
        assertTrue(miniWorld.hasComponent(underWaterRock, RectangleComponent.class));
        assertTrue(miniWorld.hasComponent(underWaterRock, FrictionComponent.class));
        assertTrue(miniWorld.hasComponent(underWaterRock, HealthComponent.class));
        assertTrue(miniWorld.hasComponent(underWaterRock, TileRenderComponent.class));
        
        assertTrue(miniWorld.hasComponent(spaceRock, PositionComponent.class));
        assertTrue(miniWorld.hasComponent(spaceRock, CollisionComponent.class));
        assertTrue(miniWorld.hasComponent(spaceRock, SpriteComponent.class));
        assertTrue(miniWorld.hasComponent(spaceRock, RectangleComponent.class));
        assertTrue(miniWorld.hasComponent(spaceRock, FrictionComponent.class));
        assertTrue(miniWorld.hasComponent(spaceRock, HealthComponent.class));
        assertTrue(miniWorld.hasComponent(spaceRock, TileRenderComponent.class));
        
        assertTrue(miniWorld.hasComponent(baseRock, PositionComponent.class));
        assertTrue(miniWorld.hasComponent(baseRock, CollisionComponent.class));
        assertTrue(miniWorld.hasComponent(baseRock, SpriteComponent.class));
        assertTrue(miniWorld.hasComponent(baseRock, RectangleComponent.class));
        assertTrue(miniWorld.hasComponent(baseRock, FrictionComponent.class));
        assertTrue(miniWorld.hasComponent(baseRock, HealthComponent.class));
        assertTrue(miniWorld.hasComponent(baseRock, TileRenderComponent.class));
        
        assertTrue(miniWorld.hasComponent(sand, PositionComponent.class));
        assertTrue(miniWorld.hasComponent(sand, CollisionComponent.class));
        assertTrue(miniWorld.hasComponent(sand, SpriteComponent.class));
        assertTrue(miniWorld.hasComponent(sand, RectangleComponent.class));
        assertTrue(miniWorld.hasComponent(sand, FrictionComponent.class));
        assertTrue(miniWorld.hasComponent(sand, HealthComponent.class));
        assertTrue(miniWorld.hasComponent(sand, TileRenderComponent.class));
        
        assertTrue(miniWorld.hasComponent(moonRock, PositionComponent.class));
        assertTrue(miniWorld.hasComponent(moonRock, CollisionComponent.class));
        assertTrue(miniWorld.hasComponent(moonRock, SpriteComponent.class));
        assertTrue(miniWorld.hasComponent(moonRock, RectangleComponent.class));
        assertTrue(miniWorld.hasComponent(moonRock, FrictionComponent.class));
        assertTrue(miniWorld.hasComponent(moonRock, HealthComponent.class));
        assertTrue(miniWorld.hasComponent(moonRock, TileRenderComponent.class));
        
        assertTrue(miniWorld.hasComponent(hardRock, PositionComponent.class));
        assertTrue(miniWorld.hasComponent(hardRock, CollisionComponent.class));
        assertTrue(miniWorld.hasComponent(hardRock, SpriteComponent.class));
        assertTrue(miniWorld.hasComponent(hardRock, RectangleComponent.class));
        assertTrue(miniWorld.hasComponent(hardRock, FrictionComponent.class));
        assertTrue(miniWorld.hasComponent(hardRock, HealthComponent.class));
        assertTrue(miniWorld.hasComponent(hardRock, TileRenderComponent.class));
        
        assertTrue(miniWorld.hasComponent(deepDirt, PositionComponent.class));
        assertTrue(miniWorld.hasComponent(deepDirt, CollisionComponent.class));
        assertTrue(miniWorld.hasComponent(deepDirt, SpriteComponent.class));
        assertTrue(miniWorld.hasComponent(deepDirt, RectangleComponent.class));
        assertTrue(miniWorld.hasComponent(deepDirt, FrictionComponent.class));
        assertTrue(miniWorld.hasComponent(deepDirt, HealthComponent.class));
        assertTrue(miniWorld.hasComponent(deepDirt, TileRenderComponent.class));
        
        assertTrue(miniWorld.hasComponent(deepGrass, PositionComponent.class));
        assertTrue(miniWorld.hasComponent(deepGrass, CollisionComponent.class));
        assertTrue(miniWorld.hasComponent(deepGrass, SpriteComponent.class));
        assertTrue(miniWorld.hasComponent(deepGrass, RectangleComponent.class));
        assertTrue(miniWorld.hasComponent(deepGrass, FrictionComponent.class));
        assertTrue(miniWorld.hasComponent(deepGrass, HealthComponent.class));
        assertTrue(miniWorld.hasComponent(deepGrass, TileRenderComponent.class));
        
        assertTrue(miniWorld.hasComponent(water, PositionComponent.class));
        assertTrue(miniWorld.hasComponent(water, SpriteComponent.class));
        assertTrue(miniWorld.hasComponent(water, RectangleComponent.class));
        assertTrue(miniWorld.hasComponent(water, FrictionComponent.class));
        assertTrue(miniWorld.hasComponent(water, HealthComponent.class));
        assertTrue(miniWorld.hasComponent(water, TileRenderComponent.class));
    }
}
