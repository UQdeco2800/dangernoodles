package uq.deco2800.dangernoodles.systems;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;
import static org.mockito.Mockito.*;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.components.PlayerComponent;
import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.components.weather.WeatherComponent;
import uq.deco2800.dangernoodles.components.weather.WindComponent;
import uq.deco2800.dangernoodles.components.weather.AsteroidComponent;
import uq.deco2800.dangernoodles.components.weather.LeafComponent;
import uq.deco2800.dangernoodles.components.weather.RainComponent;
import uq.deco2800.dangernoodles.components.weather.SnowflakeComponent;
import uq.deco2800.dangernoodles.components.weather.RainingDuckComponent;
import uq.deco2800.dangernoodles.components.weather.LightningComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.EntityIterator;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;
import uq.deco2800.dangernoodles.prefabs.PlayerEntities;


/**
 * 
 * J-Unit Test WeatherSystem 
 * -Creates an instance of weather system, weatherComponent and windComponent
 * -Tests if components have been created
 * -Checks different weather states and if the relevant backgrounds, buffs, prefabs 
 * 	are created and applied
 * @author Robert Cochran & Melissa Nguyen
 *
 */

public class WeatherSystemTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {}
	
	// had some trouble with jUnit running tests parallel - some variables from one test
	// would be present in another. Recognise this is a terrible way to run unit tests
	// but they really are more like integration tests.
	// This was the only solution we could think of :(
	// also had some trouble with setUp methods not parsing all needed variables so 
	// there is a lot of repeated code
	@Test
	public void runWeatherSystemsTests() {
		checkConstructorAndInitialisedVariables();
		checkWindAndWeatherNotNull();
		checkDuckMode();
		checkSpaceMode();
		checkSunWindMode();
		checkSunNoWindMode();
		checkRainMode();
		checkSnowMode();
		checkFrozenBuff();
	}
	
	// TEST: check that when weather systems is instantiated, that all the variables are correct
	public void checkConstructorAndInitialisedVariables() {
		// variables needed to construct
		StackPane stackPane = new StackPane();
        Canvas canvas = mock(Canvas.class);
        GraphicsContext context = canvas.getGraphicsContext2D();
        StaticFrameHandler staticHandler = new StaticFrameHandler(context);
        
        // construct weather system
        WeatherSystem weatherSystem = new WeatherSystem(stackPane, staticHandler, true);
		
        // initialised variables to check against
		int[] initialisedWindDirections = {-1, 0, 1};
		int[] initialisedWeatherStates = {0, 1, 2};
		
		// check all initialised variables
		assertTrue(weatherSystem.getWindLengthTick() == 0);
		assertTrue(Arrays.equals(weatherSystem.getWindDirections(), initialisedWindDirections));
		assertTrue(Arrays.equals(weatherSystem.getWeatherStates(), initialisedWeatherStates));
		assertTrue(weatherSystem.getWeatherLengthTick() == 0);
		assertTrue(WeatherSystem.getDucksModeActive() == false);
		assertTrue(WeatherSystem.getSpaceModeActive() == false);
		assertTrue(weatherSystem.getWindBreakNext() == false);
		assertTrue(weatherSystem.getIsTest() == true);
		assertTrue(weatherSystem.getBuffApplied() == "none");
		assertTrue(weatherSystem.getStackPane() == stackPane);
		assertTrue(weatherSystem.getStaticHandler() == staticHandler);
		assertTrue(weatherSystem.getLastActiveNoodleTurn() == null);	
	}
	
	// TEST: check that wind and weather are not null when they are created in the world
	public void checkWindAndWeatherNotNull() {
        World world = new World(1000, 1000);
		
		world.createEntity().addComponent(new WindComponent());
        world.createEntity().addComponent(new WeatherComponent());
        WeatherComponent weather = world.getComponents(WeatherComponent.class).get(0);
        WindComponent wind = world.getComponents(WindComponent.class).get(0);
        
        assertTrue(weather != null);
        assertTrue(wind != null);
	}
	
	// TEST: check all things are displaying and functioning well for duck mode
	public void checkDuckMode() {
		StackPane stackPane = new StackPane();
        World world = new World(1000, 1000);
        Canvas canvas = mock(Canvas.class);
        GraphicsContext context = canvas.getGraphicsContext2D();
        StaticFrameHandler staticHandler = new StaticFrameHandler(context);
        WeatherSystem weatherSystem = new WeatherSystem(stackPane, staticHandler, true);
        world.addSystem(weatherSystem, 1);
        
        // set duck mode on
		WeatherSystem.setRainingDucks(true);
		
		// add wind and weather
		world.createEntity().addComponent(new WindComponent());
        world.createEntity().addComponent(new WeatherComponent());
        WeatherComponent weather = world.getComponents(WeatherComponent.class).get(0);
       
        // add at least one player
        createPlayerAddToWorld(world);
        EntityIterator noodleIterator = world.getIterator(PlayerComponent.class);
        Entity noodleEntity = noodleIterator.next().getEntity();
		TurnComponent noodleTurnComponent = world.getComponent(noodleEntity, TurnComponent.class).get(); 
		
		// set player to be active
		noodleTurnComponent.setTurn(0);
		// run the world
		weatherSystem.run(world, 0, 0);
		
		// check that the correct prefabs are being created
		assertTrue(!world.getComponents(RainingDuckComponent.class).isEmpty());
		assertTrue(world.getComponents(RainComponent.class) == null);
		assertTrue(world.getComponents(LeafComponent.class) == null);
		assertTrue(world.getComponents(SnowflakeComponent.class) == null);
		assertTrue(world.getComponents(LightningComponent.class) == null);
		assertTrue(world.getComponents(AsteroidComponent.class) == null);
		
		// check that its sunny when there are ducks
		assertTrue(weather.getWeatherState() == 0);
		// check no buffs are being applied due to ducks
		assertTrue(weatherSystem.getBuffApplied() == "none");
		
		// check correct background applied
		assertTrue(weatherSystem.getStackPane().getStyleClass().contains("sunny"));
		String[] backgroundsNotApplied = {"space", "rainy", "snowy"};
		for (int i = 0; i < backgroundsNotApplied.length; i++) {
			assertFalse(weatherSystem.getStackPane().getStyleClass().contains(backgroundsNotApplied.length));
		}
	}
	
	// TEST: check all things are displaying and functioning as expected during space mode
	public void checkSpaceMode() {
		StackPane stackPane = new StackPane();
        World world = new World(1000, 1000);
        Canvas canvas = mock(Canvas.class);
        GraphicsContext context = canvas.getGraphicsContext2D();
        StaticFrameHandler staticHandler = new StaticFrameHandler(context);
        WeatherSystem weatherSystem = new WeatherSystem(stackPane, staticHandler, true);
        world.addSystem(weatherSystem, 1);
        
        // make sure space weather is one and ducks are off
        WeatherSystem.setRainingDucks(false);
		WeatherSystem.setSpaceWeather(true);
		
		// populate world with wind and weather
		world.createEntity().addComponent(new WindComponent());
        world.createEntity().addComponent(new WeatherComponent());
        WeatherComponent weather = world.getComponents(WeatherComponent.class).get(0);
       
        // create at least one player
        createPlayerAddToWorld(world);
        EntityIterator noodleIterator = world.getIterator(PlayerComponent.class);
        Entity noodleEntity = noodleIterator.next().getEntity();
		TurnComponent noodleTurnComponent = world.getComponent(noodleEntity, TurnComponent.class).get(); 
		
		// set during players turn 
		noodleTurnComponent.setTurn(0);
		// run world
		weatherSystem.run(world, 0, 0);
		
		// check that the correct prefabs are created 
		assertTrue(!world.getComponents(AsteroidComponent.class).isEmpty());
		assertTrue(world.getComponents(RainComponent.class) == null);
		assertTrue(world.getComponents(LeafComponent.class) == null);
		assertTrue(world.getComponents(SnowflakeComponent.class) == null);
		assertTrue(world.getComponents(LightningComponent.class) == null);
		assertTrue(world.getComponents(RainingDuckComponent.class) == null);
		
		// weather should be sunny during space mode
		assertTrue(weather.getWeatherState() == 0);
		// no buffs applied due to space mode
		assertTrue(weatherSystem.getBuffApplied() == "none");
		
		// correct background is applied
		assertTrue(weatherSystem.getStackPane().getStyleClass().contains("space"));
		String[] backgroundsNotApplied = {"sunny", "rainy", "snowy"};
		for (int i = 0; i < backgroundsNotApplied.length; i++) {
			assertFalse(weatherSystem.getStackPane().getStyleClass().contains(backgroundsNotApplied.length));
		}
	}
	
	// TEST: check all things are displaying and functioning as expected during sun mode with wind
	public void checkSunWindMode() {
		StackPane stackPane = new StackPane();
        World world = new World(1000, 1000);
        Canvas canvas = mock(Canvas.class);
        GraphicsContext context = canvas.getGraphicsContext2D();
        StaticFrameHandler staticHandler = new StaticFrameHandler(context);
        WeatherSystem weatherSystem = new WeatherSystem(stackPane, staticHandler, true);
        world.addSystem(weatherSystem, 1);
        
        // make sure these are set correctly
        WeatherSystem.setRainingDucks(false);
		WeatherSystem.setSpaceWeather(false);
		
		// populate world with wind and weather
		world.createEntity().addComponent(new WindComponent());
        world.createEntity().addComponent(new WeatherComponent());
        WeatherComponent weather = world.getComponents(WeatherComponent.class).get(0);
        WindComponent wind = world.getComponents(WindComponent.class).get(0);
        
        // make sure there is wind and it is sunny
        wind.setDirection(1);
        wind.setStrength((float)0.5);
        weather.setWeatherState(0);
       
        // create at least one player
        createPlayerAddToWorld(world);
        EntityIterator noodleIterator = world.getIterator(PlayerComponent.class);
        Entity noodleEntity = noodleIterator.next().getEntity();
		TurnComponent noodleTurnComponent = world.getComponent(noodleEntity, TurnComponent.class).get(); 
		
		// set player active
		noodleTurnComponent.setTurn(0);
		weatherSystem.run(world, 0, 0);
		
		// check that the right prefabs are created
		assertTrue(!world.getComponents(LeafComponent.class).isEmpty());
		assertTrue(world.getComponents(RainComponent.class) == null);
		assertTrue(world.getComponents(AsteroidComponent.class) == null);
		assertTrue(world.getComponents(SnowflakeComponent.class) == null);
		assertTrue(world.getComponents(LightningComponent.class) == null);
		assertTrue(world.getComponents(RainingDuckComponent.class) == null);
		
		// make sure the waether is sunny
		assertTrue(weather.getWeatherState() == 0);
		// no buffs applied due to sun and wind
		assertTrue(weatherSystem.getBuffApplied() == "none");
		
		// make sure correct background is applied
		assertTrue(weatherSystem.getStackPane().getStyleClass().contains("sunny"));
		String[] backgroundsNotApplied = {"space", "rainy", "snowy"};
		for (int i = 0; i < backgroundsNotApplied.length; i++) {
			assertFalse(weatherSystem.getStackPane().getStyleClass().contains(backgroundsNotApplied.length));
		}
	}
	
	// TEST: check all things are displaying and functioning as expected during sun mode with no wind
	public void checkSunNoWindMode() {
		StackPane stackPane = new StackPane();
        World world = new World(1000, 1000);
        Canvas canvas = mock(Canvas.class);
        GraphicsContext context = canvas.getGraphicsContext2D();
        StaticFrameHandler staticHandler = new StaticFrameHandler(context);
        WeatherSystem weatherSystem = new WeatherSystem(stackPane, staticHandler, true);
        world.addSystem(weatherSystem, 1);
        
        // make sure these are set correctly 
        WeatherSystem.setRainingDucks(false);
		WeatherSystem.setSpaceWeather(false);
		
		// populate world with wind and weather 
		world.createEntity().addComponent(new WindComponent());
        world.createEntity().addComponent(new WeatherComponent());
        WeatherComponent weather = world.getComponents(WeatherComponent.class).get(0);
        WindComponent wind = world.getComponents(WindComponent.class).get(0);
        
        // make sure there is no wind and its sunny
        wind.setDirection(0);
        wind.setStrength(0);
        weather.setWeatherState(0);
       
        // create at least on player
        createPlayerAddToWorld(world);
        EntityIterator noodleIterator = world.getIterator(PlayerComponent.class);
        Entity noodleEntity = noodleIterator.next().getEntity();
		TurnComponent noodleTurnComponent = world.getComponent(noodleEntity, TurnComponent.class).get(); 
		
		// player active 
		noodleTurnComponent.setTurn(0);
		weatherSystem.run(world, 0, 0);
		
		// check that no prefabs are generated 
		assertTrue(world.getComponents(LeafComponent.class) == null);
		assertTrue(world.getComponents(RainComponent.class) == null);
		assertTrue(world.getComponents(AsteroidComponent.class) == null);
		assertTrue(world.getComponents(SnowflakeComponent.class) == null);
		assertTrue(world.getComponents(LightningComponent.class) == null);
		assertTrue(world.getComponents(RainingDuckComponent.class) == null);
		
		// make sure weather state is sunny
		assertTrue(weather.getWeatherState() == 0);
		// no buffs applied in sun 
		assertTrue(weatherSystem.getBuffApplied() == "none");
		// make sure correct weather background is applied
		assertTrue(weatherSystem.getStackPane().getStyleClass().contains("sunny"));
		String[] backgroundsNotApplied = {"space", "rainy", "snowy"};
		for (int i = 0; i < backgroundsNotApplied.length; i++) {
			assertFalse(weatherSystem.getStackPane().getStyleClass().contains(backgroundsNotApplied.length));
		}
	}
	
	// TEST: check all things are displaying and functioning as expected during rain
	public void checkRainMode() {
		StackPane stackPane = new StackPane();
        World world = new World(1000, 1000);
        Canvas canvas = mock(Canvas.class);
        GraphicsContext context = canvas.getGraphicsContext2D();
        StaticFrameHandler staticHandler = new StaticFrameHandler(context);
        WeatherSystem weatherSystem = new WeatherSystem(stackPane, staticHandler, true);
        world.addSystem(weatherSystem, 1);
        
        // make sure these are set correctly 
        WeatherSystem.setRainingDucks(false);
		WeatherSystem.setSpaceWeather(false);
		
		// populate world with wind and weather
		world.createEntity().addComponent(new WindComponent());
        world.createEntity().addComponent(new WeatherComponent());
        WeatherComponent weather = world.getComponents(WeatherComponent.class).get(0);
        WindComponent wind = world.getComponents(WindComponent.class).get(0);
        
        // make sure there its rainy, doesnt matter what wind is
        wind.setDirection(0);
        weather.setWeatherState(1);
       
        // create at least one player
        createPlayerAddToWorld(world);
        EntityIterator noodleIterator = world.getIterator(PlayerComponent.class);
        Entity noodleEntity = noodleIterator.next().getEntity();
		TurnComponent noodleTurnComponent = world.getComponent(noodleEntity, TurnComponent.class).get(); 
		
		// player is active
		noodleTurnComponent.setTurn(0);
		weatherSystem.run(world, 0, 0);
		
		// make sure rain and lightning prefabs created
		assertTrue(world.getComponents(LeafComponent.class) == null);
		assertTrue(!world.getComponents(RainComponent.class).isEmpty());
		assertTrue(world.getComponents(AsteroidComponent.class) == null);
		assertTrue(world.getComponents(SnowflakeComponent.class) == null);
		assertTrue(!world.getComponents(LightningComponent.class).isEmpty());
		assertTrue(world.getComponents(RainingDuckComponent.class) == null);
		
		// make sure its raining
		assertTrue(weather.getWeatherState() == 1);
		// rain buff should be applied
		assertTrue(weatherSystem.getBuffApplied() == "rain");
		// make sure correct background is displayed
		assertTrue(weatherSystem.getStackPane().getStyleClass().contains("rainy"));
		String[] backgroundsNotApplied = {"space", "sunny", "snowy"};
		for (int i = 0; i < backgroundsNotApplied.length; i++) {
			assertFalse(weatherSystem.getStackPane().getStyleClass().contains(backgroundsNotApplied.length));
		}
	}
	
	// TEST: check all things are displaying and functioning as expected during snow
	public void checkSnowMode() {
		StackPane stackPane = new StackPane();
        World world = new World(1000, 1000);
        Canvas canvas = mock(Canvas.class);
        GraphicsContext context = canvas.getGraphicsContext2D();
        StaticFrameHandler staticHandler = new StaticFrameHandler(context);
        WeatherSystem weatherSystem = new WeatherSystem(stackPane, staticHandler, true);
        world.addSystem(weatherSystem, 1);
        
     // make sure these are set correctly 
        WeatherSystem.setRainingDucks(false);
		WeatherSystem.setSpaceWeather(false);
		
		// populate world with weather and wind
		world.createEntity().addComponent(new WindComponent());
        world.createEntity().addComponent(new WeatherComponent());
        WeatherComponent weather = world.getComponents(WeatherComponent.class).get(0);
        WindComponent wind = world.getComponents(WindComponent.class).get(0);
        
        // wind doesnt matter, weather set to snow
        wind.setDirection(0);
        weather.setWeatherState(2);
       
        // make at least one player
        createPlayerAddToWorld(world);
        EntityIterator noodleIterator = world.getIterator(PlayerComponent.class);
        Entity noodleEntity = noodleIterator.next().getEntity();
		TurnComponent noodleTurnComponent = world.getComponent(noodleEntity, TurnComponent.class).get(); 
		
		// player is active (beginning of turn)
		noodleTurnComponent.setTurn(0);
		weatherSystem.run(world, 0, 0);
		
		// make sure snowflakes are being generated
		assertTrue(world.getComponents(LeafComponent.class) == null);
		assertTrue(world.getComponents(RainComponent.class) == null);
		assertTrue(world.getComponents(AsteroidComponent.class) == null);
		assertTrue(!world.getComponents(SnowflakeComponent.class).isEmpty());
		assertTrue(world.getComponents(LightningComponent.class) == null);
		assertTrue(world.getComponents(RainingDuckComponent.class) == null);
	
		// make sure its actually snowing
		assertTrue(weather.getWeatherState() == 2);
		// make sure snow buff is applied when its at the begning of the users turn
		assertTrue(weatherSystem.getBuffApplied() == "snow");
		// make sure correct background applied
		assertTrue(weatherSystem.getStackPane().getStyleClass().contains("snowy"));
		String[] backgroundsNotApplied = {"space", "sunny", "rainy"};
		for (int i = 0; i < backgroundsNotApplied.length; i++) {
			assertFalse(weatherSystem.getStackPane().getStyleClass().contains(backgroundsNotApplied.length));
		}
	}
	
	// TEST: make sure frozen buff is being applied when the player is > 10 ticks into their turn
	public void checkFrozenBuff() {
		StackPane stackPane = new StackPane();
        World world = new World(1000, 1000);
        Canvas canvas = mock(Canvas.class);
        GraphicsContext context = canvas.getGraphicsContext2D();
        StaticFrameHandler staticHandler = new StaticFrameHandler(context);
        WeatherSystem weatherSystem = new WeatherSystem(stackPane, staticHandler, true);
        world.addSystem(weatherSystem, 1);
        
        // make sure these are set correctly
        WeatherSystem.setRainingDucks(false);
		WeatherSystem.setSpaceWeather(false);
		
		// populate world with wind and weather
		world.createEntity().addComponent(new WindComponent());
        world.createEntity().addComponent(new WeatherComponent());
        WeatherComponent weather = world.getComponents(WeatherComponent.class).get(0);
        WindComponent wind = world.getComponents(WindComponent.class).get(0);
        
        // wind doesnt matter, make sure its snowing
        wind.setDirection(0);
        weather.setWeatherState(2);
       
        // make sure there is one player
        createPlayerAddToWorld(world);
        EntityIterator noodleIterator = world.getIterator(PlayerComponent.class);
        Entity noodleEntity = noodleIterator.next().getEntity();
		TurnComponent noodleTurnComponent = world.getComponent(noodleEntity, TurnComponent.class).get(); 
		
		// player is active, run world making sure that its been 11 (>10) ticks since the players turn
		noodleTurnComponent.setTurn(0);
		weatherSystem.run(world, 11, 0);

		// make sure frozen buff is applied
		assertTrue(weatherSystem.getBuffApplied() == "frozen");
	}
	
	// create a player in the world;
	public void createPlayerAddToWorld(World world) {
		Entity player = PlayerEntities.createPlayer(world, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_ALPHA, 0);
		player.addComponent(new TurnComponent());
	}
}
