package uq.deco2800.dangernoodles.prefabs;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Optional;

import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.components.weather.RainComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

/**
 * Created by Melissa Nguyen 18/09/16.
 */
public class RainEntityTest {
	
	World miniWorld = new World(0,0);
	
	/** 
	 * Testing RainEntity instance creation and whether it has the right 
	 * components attached
	 */
	@Test
	public void testRainEntityCreation() {
		Entity rainEntity = WeatherPrefabs.createRain(miniWorld);
		
		Optional<PositionComponent> pos = miniWorld.getComponent(rainEntity, PositionComponent.class);
		Optional<MovementComponent> movement = miniWorld.getComponent(rainEntity, MovementComponent.class);
		Optional<RectangleComponent> rect = miniWorld.getComponent(rainEntity, RectangleComponent.class);
		Optional<GravityComponent> gravity = miniWorld.getComponent(rainEntity, GravityComponent.class);
		Optional<MassComponent> mass = miniWorld.getComponent(rainEntity, MassComponent.class);
		Optional<SpriteComponent> sprite = miniWorld.getComponent(rainEntity, SpriteComponent.class);
		Optional<RainComponent> leaf = miniWorld.getComponent(rainEntity, RainComponent.class);
	
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