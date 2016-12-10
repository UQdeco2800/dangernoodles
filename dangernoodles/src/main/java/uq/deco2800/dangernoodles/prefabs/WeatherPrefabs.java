package uq.deco2800.dangernoodles.prefabs;

import uq.deco2800.dangernoodles.AudioManager;
import uq.deco2800.dangernoodles.components.CollisionComponent;
import uq.deco2800.dangernoodles.components.GravityComponent;
import uq.deco2800.dangernoodles.components.MassComponent;
import uq.deco2800.dangernoodles.components.MovementComponent;
import uq.deco2800.dangernoodles.components.NameComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.RectangleComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.components.weather.AsteroidComponent;
import uq.deco2800.dangernoodles.components.weather.LeafComponent;
import uq.deco2800.dangernoodles.components.weather.LightningComponent;
import uq.deco2800.dangernoodles.components.weather.RainComponent;
import uq.deco2800.dangernoodles.components.weather.RainingDuckComponent;
import uq.deco2800.dangernoodles.components.weather.SnowflakeComponent;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.ecs.Entity;

/**
 * Team sitcom
 * creates weather prefabs for different weather events 
 */
public class WeatherPrefabs {

    /**
     * Creates a leaves to signify wind in sunny weather
     *
     * @param world
     *         Parent world object.
     *
     * @return The created world entity.
     */
    public static Entity createWindVisual(World world) {
        double xPos = Math.random() * 3000 - 500;
        double yPos = -5;

        return world.createEntity().addComponent(new PositionComponent(xPos,yPos))
			.addComponent(new MovementComponent(0.0, 0.0, 0.0, 0.0))
			.addComponent(new RectangleComponent(25,25))
			.addComponent(new GravityComponent(true))
			.addComponent(new MassComponent(Math.random()/2 + .5))
			.addComponent(new SpriteComponent(25, 25, "Weather/leaf.png"))
			.addComponent(new LeafComponent());      
    }
    
    /**
     * @param world
     *          Parent world object.
     * 
     * @return
     *      The created world entity.
     *      
     * @ensure an entity with random mass will be created at a randomly generated x coordinate and constant y = -5
     */
    public static Entity createSnowflake(World world) {
        double xPos = Math.random() * 3000 - 500;
        double yPos = -5;

        return world.createEntity().addComponent(new PositionComponent(xPos,yPos))
			.addComponent(new MovementComponent(0.0, 0.0, 0.0, 0.0))
			.addComponent(new RectangleComponent(25,25))
			.addComponent(new GravityComponent(true))
			.addComponent(new MassComponent(Math.random()/2 + .5))
			.addComponent(new SpriteComponent(25, 25, "Weather/snow.png"))
			.addComponent(new SnowflakeComponent());
    }
    
    /**
     * @param world
     *          Parent world object.
     * 
     * @return
     *      The created world entity.
     *      
     * @ensure an entity with random mass will be created at a randomly generated x coordinate and constant y = -5
     */
    public static Entity createDuck(World world) {
        double xPos = Math.random() * 3000 - 500;
        double yPos = -5;

        return world.createEntity().addComponent(new PositionComponent(xPos,yPos))
        	.addComponent(new MovementComponent(0.0, 0.0, 0.0, 0.0))
			.addComponent(new RectangleComponent(25,25))
			.addComponent(new GravityComponent(true))
			.addComponent(new MassComponent(Math.random()/2 + .5))
			.addComponent(new SpriteComponent(25, 25, "Weather/ducks.PNG"))
			.addComponent(new RainingDuckComponent());
    }
    

    /**
     * Creates a single rain drop entity
     *
     * @param world
     *         Parent world object.
     *
     * @return The created world entity.
     * 
     * @ensure an entity with random mass will be created at a randomly generated x coordinate and constant y = -5
     */
    public static Entity createRain(World world) {
        double xPos = Math.random() * 3000 - 500;
        double yPos = -5;

        return world.createEntity().addComponent(new PositionComponent(xPos,yPos))
			.addComponent(new MovementComponent(0.0, 0.0, 0.0, 0.0))
			.addComponent(new RectangleComponent(25,25))
			.addComponent(new GravityComponent(true))
			.addComponent(new MassComponent(2))
			.addComponent(new SpriteComponent(2, 8, "Weather/raindrop.png"))
			.addComponent(new RainComponent());     
    }
    
    /**
     * @param world
     *          Parent world object.
     * 
     * @return
     *      The created world entity.
     *      
     * @ensure an entity with random mass will be created at a randomly generated x coordinate and constant y = -5
     */
    public static Entity createAsteroid(World world) {
        double xPos = Math.random() * 3000 - 500;
        double yPos = -5;

        Entity asteroid = world.createEntity().addComponent(new PositionComponent(xPos,yPos));
        asteroid.addComponent(new MovementComponent(0.0, 0.0, 0.0, 0.0));
		asteroid.addComponent(new RectangleComponent(25,25));
		asteroid.addComponent(new GravityComponent(true));
		asteroid.addComponent(new MassComponent(Math.random()*2 + .5));
		// switch between asteroids with no tails and asteroids with tails
		if (Math.random() > 0.5) {
			asteroid.addComponent(new SpriteComponent(25, 25, "Weather/Asteroid.png"));
		} else {
			asteroid.addComponent(new SpriteComponent(25, 50, "Weather/AsteroidWithTail.png"));
		}
		asteroid.addComponent(new AsteroidComponent());
		
		return asteroid;
    }
    
    /**
     * Creates a lightning entity
     * 
     * @param world
     * 			Parent world object.
     * @return
     * 			The created world entity
     */
    public static Entity createLightning(World world, double x, double y, double damage) {
        double yPos = -5;
        
        // Play thunder sound. Sorry about sound quality, really struggled on this one.
        AudioManager.playSound("resources/sounds/thunder1.wav", false);
        
        return world.createEntity().addComponent(new PositionComponent(x,yPos))
			.addComponent(new MovementComponent(0.0, 0.0, 0.0, 0.0))
			.addComponent(new RectangleComponent(20,y))
			.addComponent(new SpriteComponent(20, (int)y, "Weather/lightning.png"))
			.addComponent(new LightningComponent(damage))
			//.addComponent(new InstantDamageComponent(damage))
			.addComponent(new NameComponent("LightningBolt"))
			.addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE));
    }
}