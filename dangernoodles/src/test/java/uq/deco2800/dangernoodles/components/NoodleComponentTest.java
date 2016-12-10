package uq.deco2800.dangernoodles.components;

import uq.deco2800.dangernoodles.components.noodles.NoodleComponent;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * Test suite for the NoodleComponent
 * @author Paul Haley
 *
 */
public class NoodleComponentTest {

	/**
	 * Test creation of entity with single NoodleComponent added with valid parameters.
	 */
	@Test
	public void entitySingleComponentValid() {
		World miniWorld = new World(0, 0);
		
		NoodleEnum[] characters = NoodleEnum.values();
		
		for (int i = 0; i < characters.length; ++i) {
			Entity entityNoodle = miniWorld.createEntity().addComponent(new NoodleComponent(characters[i]));
			// Test that the noodle has the NoodleComponent added to it.
			assertTrue(miniWorld.hasComponent(entityNoodle, NoodleComponent.class));
			
			// Checks that the NoodleComponent is correct by testing name.
			NoodleComponent noodleComponent = miniWorld.getComponent(entityNoodle, NoodleComponent.class).get();
	        assertTrue(noodleComponent.getNoodleClassName().equals(characters[i].getName()));
	        
	        // Checks that the component can be successfully removed
	        miniWorld.removeComponent(entityNoodle, NoodleComponent.class);
	        assertFalse(miniWorld.hasComponent(entityNoodle, NoodleComponent.class));
		}
	}
	
	
}
