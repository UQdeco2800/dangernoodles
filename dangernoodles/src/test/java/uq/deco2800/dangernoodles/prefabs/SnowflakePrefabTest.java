package uq.deco2800.dangernoodles.prefabs;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Optional;

import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.components.weather.SnowflakeComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

/**
 * Created by Melissa Nguyen 18/09/16.
 */
public class SnowflakePrefabTest {
	
	World miniWorld = new World(0,0);
	
	/** 
	 * Testing SnowflakePrefab instance creation and whether it has the right 
	 * components attached
	 */
	@Test
	public void testSnowflakePrefabCreation() {
		Entity snowflake = WeatherPrefabs.createSnowflake(miniWorld);
		
		Optional<PositionComponent> pos = miniWorld.getComponent(snowflake, PositionComponent.class);
		Optional<MovementComponent> movement = miniWorld.getComponent(snowflake, MovementComponent.class);
		Optional<RectangleComponent> rect = miniWorld.getComponent(snowflake, RectangleComponent.class);
		Optional<GravityComponent> gravity = miniWorld.getComponent(snowflake, GravityComponent.class);
		Optional<MassComponent> mass = miniWorld.getComponent(snowflake, MassComponent.class);
		Optional<SpriteComponent> sprite = miniWorld.getComponent(snowflake, SpriteComponent.class);
		Optional<SnowflakeComponent> leaf = miniWorld.getComponent(snowflake, SnowflakeComponent.class);
	
		assertTrue(pos.isPresent());
        assertTrue(movement.isPresent());
        assertTrue(rect.isPresent());
        assertTrue(gravity.isPresent());
        assertTrue(mass.isPresent());
        assertTrue(sprite.isPresent());
        assertTrue(leaf.isPresent());
	}
	
	// Could add further tests to test that each component has the correct values set
}