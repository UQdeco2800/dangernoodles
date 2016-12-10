package uq.deco2800.dangernoodles.systems;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.After;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.components.MovementComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.weather.LightningComponent;
import uq.deco2800.dangernoodles.components.weather.RainComponent;
import uq.deco2800.dangernoodles.components.weather.WeatherComponent;
import uq.deco2800.dangernoodles.components.weather.WindComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.EntityIterator;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.WeatherPrefabs;

/**
 * 
 * Test for Weather Movement System
 * 
 * Will test weatherMovementSystems for
 * 	 - movement of components 
 * 	 - removal when components are out of bounds
 * @author robert-cochran & melissa-n
 *
 */


public class WeatherMovementSystemTest {

	StackPane stackPane;
	World world;
	Canvas canvas;
	GraphicsContext context;
	StaticFrameHandler staticFrameHandler;
	WeatherSystem weatherSystem;
	WeatherMovementSystem weatherMovementSystem;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {}
	
	@Before
	public void setUp(){
		stackPane = new StackPane();
        world = new World(1000, 1000);
        canvas = mock(Canvas.class);
        context = canvas.getGraphicsContext2D();
        staticFrameHandler = new StaticFrameHandler(context);
        weatherSystem = new WeatherSystem(stackPane, staticFrameHandler, false);
        weatherMovementSystem = new WeatherMovementSystem();
        world.addSystem(weatherSystem, 1);
        world.addSystem(weatherMovementSystem, 2);
	}
	
	
	/**
	 * tests if rain moves in the x axis when wind is present blowing right
	 */

	@Test
	public void testRainMovementRight() {
		// create the world
		World world = new World(1000, 1000);
		// add weather movement system to world
		WeatherMovementSystem weatherMovementSystem = new WeatherMovementSystem();
		world.addSystem(weatherMovementSystem, 1);
		// create wind component
		world.createEntity().addComponent(new WindComponent());
		WindComponent windComponent = world.getComponents(WindComponent.class).get(0);
		// create rain entity
		Entity rainEntity = WeatherPrefabs.createRain(world);
		//make wind blow right and strongly for testing
		windComponent.setStrength((float)1);
		windComponent.setDirection(1);
		// check initial X position of rain entity
		double initialPosX = world.getComponent(rainEntity, MovementComponent.class).get().getVX();
		// run the movement system
		weatherMovementSystem.run(world, 0, 0);
		// check the post tick X position of the rain entity
		double posAfterTickX = world.getComponent(rainEntity, MovementComponent.class).get().getVX();
		// since wind is blowing right, the final X position should be bigger than initial
		assertTrue(posAfterTickX > initialPosX);
	}
	
	/**
	 * tests if rain moves in the x axis when wind is present blowing left
	 */
	@Test
	public void testRainMovementLeft() {
		// create the world
		World world = new World(1000, 1000);
		// add weather movement system to world
		WeatherMovementSystem weatherMovementSystem = new WeatherMovementSystem();
		world.addSystem(weatherMovementSystem, 1);
		// create wind component
		world.createEntity().addComponent(new WindComponent());
		WindComponent windComponent = world.getComponents(WindComponent.class).get(0);
		// create rain entity
		Entity rainEntity = WeatherPrefabs.createRain(world);
		//make wind blow right and strongly for testing
		windComponent.setStrength((float)1);
		windComponent.setDirection(-1);
		// check initial X position of rain entity
		double initialPosX = world.getComponent(rainEntity, MovementComponent.class).get().getVX();
		// run the movement system
		weatherMovementSystem.run(world, 0, 0);
		// check the post tick X position of the rain entity
		double posAfterTickX = world.getComponent(rainEntity, MovementComponent.class).get().getVX();
		
		// since wind is blowing right, the final X position should be bigger than initial
		assertTrue(posAfterTickX < initialPosX);
	}
	
	
	/**
	 * tests if entity is created and deleted after it leaves the screen
	 */
	@Test
	public void testRemoval() {
		// create the world
		World world = new World(1000, 1000);
		// add weather movement system to world
		WeatherMovementSystem weatherMovementSystem = new WeatherMovementSystem();
		world.addSystem(weatherMovementSystem, 1);
		// create wind component
		world.createEntity().addComponent(new WindComponent());
		WindComponent windComponent = world.getComponents(WindComponent.class).get(0);
		// create rain entity
		Entity rainEntity = WeatherPrefabs.createRain(world);
		// run the movement system
		weatherMovementSystem.run(world, 0, 0);
		// set the Y value to be past the 1500 removal boundary
		world.getComponent(rainEntity, PositionComponent.class).get().setY(1600);
		// run movement system again
		weatherMovementSystem.run(world, 0, 0);
		// since Y was > 1500, rainEntity should no longer have a Y component
		assertTrue(world.getComponents(RainComponent.class).isEmpty());
	}
	
}

