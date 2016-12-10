package uq.deco2800.dangernoodles.components.weather;

import uq.deco2800.dangernoodles.ecs.Component;

/**
 * Created by Todd Billington on 4/09/2016.
 */
public class WeatherComponent extends Component {
	
	// minimum weather length in ticks
	private double minimumWeatherLength = 15;
	//0 for sun, 1 for rain, 2 for snow, 3 for ducks
	private int weatherState; 
	// length of ticks weather will go for
	private double lengthOfWeather;

	/**
     * Default constructor of this class.
     *
     * @ensure new instance of this component with weatherState and lengthOfWeather set to 0
     */
    public WeatherComponent() {
    	this.weatherState = 0;
    	this.lengthOfWeather = 0;
    }
    
    /**
     * Get the current State of Weather
     *
     * @return a double, weatherState, representing the current weather state
     *
     * @ensure a double, lengthOfWeather, representing the current weather state
     */
    public int getWeatherState() {
    	return weatherState;
    }
    
    /**
     * Get the current Length of Weather
     *
     * @return a double, lengthOfWeather, representing the length of time this weather state lasts for
     *
     * @ensure a double, lengthOfWeather, representing the length of time this weather state lasts for
     */
    public double getLengthOfWeather() {
    	return lengthOfWeather;
    }
    
    /**
     * Get the current Minimum Weather Length
     *
     * @return a double, minimumWeatherLength, representing the current minimum amount of time weather state will exist for
     *
     * @ensure a double, minimumWeatherLength, representing the current minimum amount of time weather state will exist for
     */
    public double getMinWeatherLength() {
		return minimumWeatherLength;
	}
    
    /**
     * Setter method for state the weather is currently in (Sunny, Raining, Snowing)
     *
     * @param state
     *         an int representing the weather state (0, Sunny; 1, Raining; 2, Snowing)
     *
     * @ensure Component has weather state of given input
     */
    public void setWeatherState(int state) {
    	if (state != 0 && state != 1 && state !=2) {
    		throw new IllegalArgumentException("Weather must be 0, 1 or 2!");
    	} else {
    		this.weatherState = state;
    	}
    }
    
    /**
     * Setter method for Length of time Weather lasts for
     *
     * @param length
     *         a double representing the length of time weather lasts for this component, must be > 0
     *
     * @ensure Component has weather length of given input
     */
    public void setLengthOfWeather(double length) {
    	if (length < 0) {
    		throw new IllegalArgumentException("Weather length cannot be less than 0");
    	} else {
    		this.lengthOfWeather = length;
    	}
    }

}