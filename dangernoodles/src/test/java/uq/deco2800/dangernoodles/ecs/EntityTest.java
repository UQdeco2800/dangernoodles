package uq.deco2800.dangernoodles.ecs;

import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.components.noodles.NoodleComponent;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * Test class for generic entities (Entity.java)
 * @author Paul Haley
 *
 */
public class EntityTest {
	/**
	 * Tests that an entity can be created and return expected parameters
	 */
	@Test
	public void creation() {
		World miniWorld = new World(0, 0);
		int id = 0;
		int generation = 0;
		Entity entity = new Entity(id, generation, miniWorld);
		
		assertEquals(entity.getId(), id);
		assertTrue(entity.equals(entity));
		assertTrue(entity.toString().equals("Entity{id=" + id + ", generation=" + generation + "}"));
	}
	
	/**
	 * Tests the hashCode and Equals method for same parameter entities
	 */
	@Test
	public void equalSame() {
		World miniWorld = new World(0, 0);
		int id = 0;
		int generation = 1;
		Entity entity1 = new Entity(id, generation, miniWorld);
		Entity entity2 = new Entity(id, generation, miniWorld);
		
		assertEquals(entity1.hashCode(), entity2.hashCode());
		assertTrue(entity1.equals(entity2));
		assertTrue(entity1.toString().equals(entity2.toString()));
		assertEquals(entity1.getId(), entity2.getId());
	}
	
	/**
	 * Tests the hashCode and Equals method for different parameter entities
	 */
	@Test
	public void equalDifferent() {
		World miniWorld = new World(0, 0);
		int id1 = 0;
		int id2= 5;
		int generation1 = 1;
		int generation2 = 2;
		Entity entity1 = new Entity(id1, generation1, miniWorld);
		Entity entity2 = new Entity(id2, generation2, miniWorld);
		
		assertNotEquals(entity1.hashCode(), entity2.hashCode());
		assertFalse(entity1.equals(entity2));
		assertFalse(entity1.toString().equals(entity2.toString()));
		assertNotEquals(entity1.getId(), entity2.getId());
	}

	/**
	 * Tests the hashCode and Equals method for same parameter entities but with a component added to only one of the
	 * entities.
	 */
	@Test
	public void equalSameComponent() {
		World miniWorld = new World(0, 0);
		int id = 0;
		int generation = 1;
		Entity entity1 = new Entity(id, generation, miniWorld);
		Entity entity2 = new Entity(id, generation, miniWorld);
		// Adding a component to entity 1
		entity1.addComponent(new NoodleComponent(NoodleEnum.NOODLE_AGILITY));
		
		assertEquals(entity1.hashCode(), entity2.hashCode());
		assertTrue(entity1.equals(entity2));
		assertTrue(entity1.toString().equals(entity2.toString()));
		assertEquals(entity1.getId(), entity2.getId());
	}
	
	/**
	 * Tests the hashCode and Equals method for same parameter entities but with a component added to only one of the
	 * entities.
	 */
	@Test
	public void equalDifferentComponent() {
		World miniWorld = new World(0, 0);
		int id1 = 0;
		int id2= 5;
		int generation1 = 1;
		int generation2 = 2;
		Entity entity1 = new Entity(id1, generation1, miniWorld);
		Entity entity2 = new Entity(id2, generation2, miniWorld);
		// Adding a component to entity 1
		entity1.addComponent(new NoodleComponent(NoodleEnum.NOODLE_AGILITY));
		
		assertNotEquals(entity1.hashCode(), entity2.hashCode());
		assertFalse(entity1.equals(entity2));
		assertFalse(entity1.toString().equals(entity2.toString()));
		assertNotEquals(entity1.getId(), entity2.getId());
	}
	
	/**
	 * Tests the equals method on for nulls
	 */
	@Test
	public void nullEntityTest() {
		World miniWorld = new World(0, 0);
		int id = 0;
		int generation = 1;
		Entity entity1 = new Entity(id, generation, miniWorld);
		Entity entity2 = null;
		
		assertFalse(entity1.equals(null));
		assertFalse(entity1.equals(entity2));
	}
}
