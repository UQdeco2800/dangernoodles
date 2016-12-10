package uq.deco2800.dangernoodles.prefabs;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Optional;

import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.components.weather.LeafComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

/**
 * Created by Melissa Nguyen 18/09/16.
 */
public class WindVisualTest {
	
	World miniWorld = new World(0,0);
	
	/** 
	 * Testing WindVisual instance creation and whether it has the right 
	 * components attached
	 */
	@Test
	public void testWindVisualsCreation() {
		Entity windVisual = WeatherPrefabs.createWindVisual(miniWorld);
		
		Optional<PositionComponent> pos = miniWorld.getComponent(windVisual, PositionComponent.class);
		Optional<MovementComponent> movement = miniWorld.getComponent(windVisual, MovementComponent.class);
		Optional<RectangleComponent> rect = miniWorld.getComponent(windVisual, RectangleComponent.class);
		Optional<GravityComponent> gravity = miniWorld.getComponent(windVisual, GravityComponent.class);
		Optional<MassComponent> mass = miniWorld.getComponent(windVisual, MassComponent.class);
		Optional<SpriteComponent> sprite = miniWorld.getComponent(windVisual, SpriteComponent.class);
		Optional<LeafComponent> leaf = miniWorld.getComponent(windVisual, LeafComponent.class);
	
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