package uq.deco2800.dangernoodles.systems;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.Optional;
import java.util.Random;

import uq.deco2800.dangernoodles.AudioManager;
import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.EntityIterator;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.components.PlayerComponent;
import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.components.effects.EffectEnum;
import uq.deco2800.dangernoodles.components.effects.WeatherSpeedEffectComponent;
import uq.deco2800.dangernoodles.components.weather.WeatherComponent;
import uq.deco2800.dangernoodles.components.weather.WindComponent;
import uq.deco2800.dangernoodles.prefabs.WeatherPrefabs;


/**
 * Will handle the logic around generating weather states for the game
 * 	 - randomly generating which weather component will be used
 * 	 - generating weather strength/direction 
 *   - displaying weather state to screen
 * @author team-sitcom
 *
 */
public class WeatherSystem implements System {
	// measures ticks since the last wind changed
	private double windLengthTick = 0;
	// -1 is left, 1 is right, 0 is none
	private int[] windDirections = {-1, 0, 1};
	// 0 for sunny, 1 for rain, 2 for snow
	private int[] weatherStates = {0, 1, 2};
	// measures ticks since last weather change
	private double weatherLengthTick = 0;
	//Is a boolean if it should rain ducks or not
	private static boolean ducksModeActive = false;
	//Is a boolean to determine to see if its space weather
	private static boolean spaceModeActive = false;
	// boolean to determine if the next wind will be the break (no wind period) 
	private boolean windBreakNext = false;
	// stackPane for rendering wind info on screen
	private StackPane stackPane;
	// static handler for rendering wind info on screen
	private StaticFrameHandler staticHandler;
	// which buff is being applied, can be "none", "snow", "rain" or "frozen"
	private String buffApplied = "none";
	// the turn component of the noodle that was last active
	private TurnComponent lastActiveNoodleTurn;
	// injection for testing, prefabs will be created differently if its a test instance of weather system
	private boolean isTest;
	
	/**
     * Constructor for weather system
     * 
     * @param stackPane for the games javaFX rendering
     * 
     * @param staticHandler for the games javaFX rendering
     * 
     * @param isTest true if this the system is instantiated for a test
     */
	public WeatherSystem(StackPane stackPane, StaticFrameHandler staticHandler, boolean isTest) {
		this.stackPane = stackPane;
		this.staticHandler = staticHandler;
		this.isTest = isTest;
	}
	
	/**
     * Overrides the parent method Run
     * 
     * @param world is the parent world object
     * 
     * @param t Time since the beginning of the game
     * 
     * @param dt The delta time, i.e. the time since last frame
     */
	@Override
	public void run(World world, double t, double dt) {
		// get wind and weather components
		WindComponent wind = world.getComponents(WindComponent.class).get(0);
		WeatherComponent weather = world.getComponents(WeatherComponent.class).get(0);

		// apply the appropriate buff (depending on weather) to the noodle whose turn it is
		EntityIterator noodleIterator = world.getIterator(PlayerComponent.class);
		while (noodleIterator.hasNext()) {
			
			Entity noodleEntity = noodleIterator.next().getEntity();
			TurnComponent noodleTurnComponent = world.getComponent(noodleEntity, TurnComponent.class).get();
			
			// if a special mode is happening or the weather is sunny, no buffs should be applied
			if (ducksModeActive || spaceModeActive || weather.getWeatherState() == 0) {
				world.removeComponent(noodleEntity, WeatherSpeedEffectComponent.class);
				buffApplied = "none";
			} else {
				// if its the noodles turn
				if (noodleTurnComponent.getTurn()) {
					// if this while loop is running when the turn has freshly changed, then reset 
					// buffs
					if (noodleTurnComponent.equals(lastActiveNoodleTurn)) {
						lastActiveNoodleTurn = noodleTurnComponent;
						buffApplied = "none";
					}
		            
					// make the noodle faster in rain
					if (weather.getWeatherState() == 1) {
						Optional<WeatherSpeedEffectComponent> weatherFx = 
								world.getComponent(noodleEntity, WeatherSpeedEffectComponent.class);
						if (!weatherFx.isPresent()) {
							WeatherSpeedEffectComponent fastBuff = 
									new WeatherSpeedEffectComponent(EffectEnum.FAST_RAIN_BUFF , 15, (double)600);
							noodleEntity.addComponent(fastBuff);
							buffApplied = "rain";
						}
					// make the noodle slower in snow
					} else if (weather.getWeatherState() == 2) {
						Optional<WeatherSpeedEffectComponent> weatherFx = 
								world.getComponent(noodleEntity, WeatherSpeedEffectComponent.class);
						if (!weatherFx.isPresent() && !(buffApplied == "frozen")) {
							WeatherSpeedEffectComponent slowBuff = 
									new WeatherSpeedEffectComponent(EffectEnum.SLOW_SNOW_DEBUFF , 15, (double)-600);
							noodleEntity.addComponent(slowBuff);
							buffApplied = "snow";
						} 
						// when its been 10 ticks since the turn started, freeze the noodle 
						double timeSinceTurnStarted = t - noodleTurnComponent.getTurnStartTime();
						if (timeSinceTurnStarted > 10) {
							if (!(buffApplied == "frozen")) {
								world.removeComponent(noodleEntity, WeatherSpeedEffectComponent.class);
								buffApplied = "none";
							} 
							WeatherSpeedEffectComponent frozenBuff = new
									WeatherSpeedEffectComponent(EffectEnum.FROZEN_NOODLE_DEBUFF, 5, (double)-10000);
							noodleEntity.addComponent(frozenBuff);
							buffApplied = "frozen";
						}
					}
				} 
			}
		}
		
		//Depending on the weather state either create snowflakes, leaves or rain drops.	
		if (ducksModeActive) { 
			WeatherPrefabs.createDuck(world);
			weather.setWeatherState(0);
		} else if (spaceModeActive) {
			if (determineIfPrefabCreated(isTest)) {
				WeatherPrefabs.createAsteroid(world);
				weather.setWeatherState(0);
			}
		} else {
			if ((weather.getWeatherState() == 0) && (wind.getDirection() != 0)) {
				if (determineIfPrefabCreated(isTest)) {
					WeatherPrefabs.createWindVisual(world);
				}
			} else if (weather.getWeatherState() == 1) {
				if (Math.random()>.95 || isTest) {
					WeatherPrefabs.createLightning(world,Math.random() * 2000,(Math.random()*500 + 500),0);
				}
				WeatherPrefabs.createRain(world);
			} else if (weather.getWeatherState() == 2 && determineIfPrefabCreated(isTest)) {
				WeatherPrefabs.createSnowflake(world);
			}
			// generate the weather if its not a special weather kind
			generateWeather(t, weather);
		}
		
		// always generate wind and display wind strength
		generateWind(t, wind);
		displayWindStrength(wind);
		changeBackground(weather);
	}	
	
	/**
     * change the background depending on the weather state
     * 
     * @param weather - the weather component of the game
     */
	private void changeBackground(WeatherComponent weather) {
		// 0 for sunny, 1 for rain, 2 for snow
		String[] backgrounds = {"space", "sunny", "rainy", "snowy"};
		
		if (spaceModeActive) {
			if (!stackPane.getStyleClass().contains("space")) {
				this.stackPane.getStyleClass().add("space");		
				removeOtherBackgrounds("space", backgrounds);
			}
		} else {
			if (weather.getWeatherState() == 0) {
				if (!stackPane.getStyleClass().contains("sunny")) {
					this.stackPane.getStyleClass().add("sunny");
					removeOtherBackgrounds("sunny", backgrounds);
				}
			} else if (weather.getWeatherState() == 1) {
				if (!stackPane.getStyleClass().contains("rainy")) {
					this.stackPane.getStyleClass().add("rainy");
					removeOtherBackgrounds("rainy", backgrounds);
				}
			} else if (weather.getWeatherState() == 2) {
				if (!stackPane.getStyleClass().contains("snowy")) {
					this.stackPane.getStyleClass().add("snowy");
					removeOtherBackgrounds("snowy", backgrounds);
				}
			}
		}
	}
		
	/**
     * Removes background images that arent in for the current weather state
     * 
     * @param backgroundOnScreen - the background that you want on the screen (dont remove)
     * 
     * @param backgrounds - possible game backgrounds
     */
	private void removeOtherBackgrounds(String backgroundOnScreen, String[] backgrounds) {
		for (int i = 0; i < backgrounds.length; i++) {
			if (backgrounds[i] != backgroundOnScreen) {
				stackPane.getStyleClass().remove(backgrounds[i]);
			}
		}
	}
		
	/**
     * Displays the wind strength on screen with an arrow showing the direction
     * 
     * @param wind - the wind component of the game
     */
	private void displayWindStrength(WindComponent wind) {
		staticHandler.addStaticRenderAction((context, handler1) -> {
            context.setFill(Color.LIGHTGREY);
            context.fillRoundRect(10, 145, 205, 30, 2, 2);
            context.strokeRoundRect(10, 145, 205, 30, 2, 2);
            context.setFill(Color.RED);
            context.setFont(Font.font("Verdana", 12));
            context.fillText("Wind Strength: " + Math.round(wind.getStrength()*50) + "km/h", 20, 165);
            if (wind.getDirection() == -1) {
            	context.drawImage(handler1.loadImage("/Weather/Left Arrow.png"), 174, 148, 35, 25);
            } else if (wind.getDirection() == 1) {
            	context.drawImage(handler1.loadImage("/Weather/Right Arrow.png"), 174, 148, 35, 25);
            } else {
            	context.drawImage(handler1.loadImage("/Weather/No Direction.png"), 174, 148, 35, 25);
            }
        });
	}
		
	
	/**
     * Limits the number of entities created by using math.random() 
     * to return a boolean that is true 30% of the time
     *
     * @return a boolean
     *
     * @ensure boolean is true only if .rand < 0.4
     */
	private boolean determineIfPrefabCreated(boolean isTest) {
		if (isTest) {
			return true;
		} else {
			double randomNumber = Math.random();
			return randomNumber < 0.4;
		}
	}
	
	/**
     * Generates new values for Wind State, Length and Direction if the time since 
     * the last successful generateWind is greater than the current length of time 
     * for Wind (length)
     *
     * @return nothing
     * 
     * @param t, the time since the beginning of game
     * 
     * @param wind, for generateWind to check against
     *
     * @ensure New values for Wind will only be generated when the time
     * since the last generateWind was done is greater than the random value
     * we produced for its length. nothing will happen if this does not occur
     */
	private void generateWind(double t, WindComponent wind) {
		if ((t - windLengthTick) > wind.getLength()) {
			if (windBreakNext) {
				wind.setStrength(0);
				wind.setDirection(0);
				wind.setLength(wind.getBreakBetweenWinds());
				windLengthTick = t;
				windBreakNext = false;
			} else {
				generateWindStrength(wind);
				generateWindDirection(wind);
				generateWindLength(wind);
				if (wind.getStrength() == 0) {
					wind.setDirection(0);
				} 
				if (wind.getDirection() == 0) {
					wind.setStrength(0);
				}
				windLengthTick = t;
				windBreakNext = true;
			}
		}
	}
	
	/**
     * Generates new values for Weather State and Length if the time since 
     * the last successful generateWeather is greater than the current length of 
     * time for Weather (length)
     *
     * @return nothing
     * 
     * @param t, the time since the beginning of game
     * 
     * @param weather, for generateWeather to check against
     *
     * @ensure New states for Weather will only be generated when the time
     * since the last generated Weather state is greater than the random value
     * we produced for its length. nothing will happen if this does not occur
     */
	private void generateWeather(double t, WeatherComponent weather) {
		if ((t - weatherLengthTick) > weather.getLengthOfWeather()) {
			generateWeatherState(weather);
			generateWeatherLength(weather);
			weatherLengthTick = t;
			
			// Plays the appropriate audio for the new weather.
			setWeatherAudio(weather);
		} 
	}
	
	/**
	 * Changes the playing overlaying audio for each weather pattern.
	 * @ensure Correct weather ambiance is played if it exists, otherwise stops the current overlay.
	 * @param weather The current weather component.
	 */
	private void setWeatherAudio(WeatherComponent weather) {
		// Stops the currently playing music overlay
		AudioManager.stopMusicOverlay();
		
		// Finds the correct weather overlay sound to play.
		switch (weather.getWeatherState()) {
			case 0: // Sunny, play bird ambiance
				AudioManager.playMusic("resources/music/sunny_loop1.wav", true, false);
				break;
			case 1: // Raining, play rain loop.
				AudioManager.playMusic("resources/music/rain_loop1.wav", true, false);
				break;
			case 2: // Snowing, play snow storm loop.
				AudioManager.playMusic("resources/music/snowstorm_loop1.wav", true, false);
				break;
			default: // Unknown weather, keep silent.
				break;
		}
		return;
	}

	/**
     * Generates number between 0 and 1 for wind strength if there is wind, 
     * sets the wind strength to that value
     *
     * @return nothing
     * 
     * @param wind to update strength for
     *
     * @ensure Strength will always be a value between 0 and 1
     */
	// might need to scale up by multiplying
	private void generateWindStrength(WindComponent wind) {
		float randomStrength = (float)Math.random();
		wind.setStrength(randomStrength);
	}
	
	/**
     * Picks random direction for wind, sets wind direction
     *
     * @return nothing
     * 
     * @param wind to update direction for
     *
     */
	private void generateWindDirection(WindComponent wind) {
		int randomIndex = new Random().nextInt(windDirections.length);
		wind.setDirection(windDirections[randomIndex]);
	}

	/**
     * Generates number between 0 and 20 for length of current wind, 
     * sets the wind length to that result
     *
     * @return nothing
     * 
     * @param wind to update Length for
     *
     * @ensure Length will always be a value between 10 and 20
     */
	// might need to scale up by multiplying for ticks and frames
	private void generateWindLength(WindComponent wind) {
		double windLength = wind.getMinWindLength() + Math.random()*10;
		wind.setLength(windLength);
	} 
	
	/**
     * Generates random Weather state and sets param to that state
     *
     * @return nothing
     * 
     * @param weather to update weather state for
     *
     */
	private void generateWeatherState(WeatherComponent weather){
		int randomIndex = new Random().nextInt(weatherStates.length);
		weather.setWeatherState(weatherStates[randomIndex]);
	}
	
	/**
     * Generates random Weather length and sets param to that length
     *
     * @return nothing
     * 
     * @param weather to update weather length for
     * 
     * @ensure Weather length will always be between 15 and 30
     *
     */
	private void generateWeatherLength(WeatherComponent weather) {
		double lengthOfWeather = weather.getMinWeatherLength() + Math.random()*15;
		weather.setLengthOfWeather(lengthOfWeather);
	}
	/**
	 * Sets if it should rain ducks or just be normal weather
	 *
	 * @return nothing
	 * 
	 * @param bool
	 * 
	 * Boolean to set the rainingducks to be
	 */
	public static void setRainingDucks(boolean bool) {
		ducksModeActive = bool;
	}
	
	/**
	 * Sets if it should be space weather
	 *
	 * @return nothing
	 * 
	 * @param bool
	 * Boolean to set if the weather should be like space
	 */
	public static void setSpaceWeather(boolean bool) {
		spaceModeActive = bool;
	}
	
	/**
	 * @return windLengthTick - time since the 
	 */
	public double getWindLengthTick() {
		return this.windLengthTick;
	}
	
	/**
	 * @return windDirections
	 */
	public int[] getWindDirections() {
		return this.windDirections;
	}
	
	/**
	 * @return weatherStates
	 */
	public int[] getWeatherStates() {
		return this.weatherStates;
	}
	
	/**
	 * @return weatherLengthTick
	 */
	public double getWeatherLengthTick() {
		return this.weatherLengthTick;
	}
	
	/**
	 * @return ducksModeActive
	 */
	public static boolean getDucksModeActive() {
		return ducksModeActive;
	}
	
	/**
	 * @return spaceModeActive
	 */
	public static boolean getSpaceModeActive() {
		return spaceModeActive;
	}
	
	/**
	 * @return windBreakNext
	 */
	public boolean getWindBreakNext() {
		return this.windBreakNext;
	}
	
	/**
	 * @return stackPane
	 */
	public StackPane getStackPane() {
		return this.stackPane;
	} 
	
	/**
	 * @return statichandler
	 */
	public StaticFrameHandler getStaticHandler() {
		return this.staticHandler;
	}
	
	/**
	 * @return lastActiveNoodleTurn
	 */
	public TurnComponent getLastActiveNoodleTurn() {
		return this.lastActiveNoodleTurn;
	}
	
	/**
	 * @return buffApplied
	 */
	public String getBuffApplied() {
		return this.buffApplied;
	}
	
	/**
	 * @return isTest
	 */
	public boolean getIsTest() {
		return this.isTest;
	}
}