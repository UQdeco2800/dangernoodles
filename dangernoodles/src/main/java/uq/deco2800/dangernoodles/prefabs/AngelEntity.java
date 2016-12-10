package uq.deco2800.dangernoodles.prefabs;


import uq.deco2800.dangernoodles.components.MovementComponent;
import uq.deco2800.dangernoodles.components.noodles.AngelComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.RectangleComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.components.stats.*;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

/**
 * Utility class that provides prefabricated noodle angels to be added to
 * the world.
 *  
 *  @author Paul Haley
 */
public class AngelEntity {
	/**
	 * Private constructor to hide the implicit public one.
	 */
	private AngelEntity() {}

	/**
	 * This method creates and returns a new noodle angle entity (player). The world the noodle exists in must also 
	 * be given and the respective starting horizontal and vertical position. It must be noted that the xPos given 
	 * will not be tested if it is on the map. 
	 * 
	 * @param world Parent world object.
	 * @param xPos Starting x position of the noodle angel entity to spawn (NOT TESTING IF THIS IS ON THE MAP)
	 * @param yPos Starting y position of the noodle angel entity to spawn (NOT TESTING IF THIS IS ON THE MAP)
	 * @return The created world angle entity as per the given parameters
	 */
	public static Entity createAngel(World world, double xPos, double yPos) {
		final int size = 50;

		return  world.createEntity()
				.addComponent(new AngelComponent())
				.addComponent(new PositionComponent(xPos, yPos))
				.addComponent(new MovementComponent(0.0, -5.0, 0.0, -5.0))
				.addComponent(new SpriteComponent(size, size, "/characters/dead_ghost.gif"))
				.addComponent(new SpeedComponent(1000)) // Not sure if this one is needed
				.addComponent(new InvulnerableComponent())
				.addComponent(new RectangleComponent(size, size));
		

	}
}