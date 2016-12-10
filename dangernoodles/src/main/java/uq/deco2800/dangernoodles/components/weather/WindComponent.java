package uq.deco2800.dangernoodles.components.weather;

import uq.deco2800.dangernoodles.ecs.Component;

public class WindComponent extends Component {
	
	// minimum wind length (in ticks)
	private double minimumWindLength = 10;
	
	// break between winds changing (in ticks)
	private double breakBetweenWinds = 10;
	
	// float that represents wind strength (0 = no wind)
	private float strength;
	
	// int repesenting wind direction, currently either left or right (-1 is left, 0, 1 is right)
	private int direction;
	
	// double representing how long the current wind strength & direction will last for
	private double length;

	
	/**
     * Default constructor of the WindComponent class.
     *
     * @ensure new instance of this component created with strength, direction and length set to 0
     * 
     * @ensure isPresent is true if wind is present in the game
     */
	public WindComponent() {
		this.strength = 0;
		this.direction = 0;
		this.length = 0;
	}
	
	/**
     * Get the current Strength the Wind moves at
     *
     * @return a float, strength, representing the current strength of the wind
     *
     * @ensure a float, strength, representing the current strength of the wind
     */
	public float getStrength() {
		return this.strength;
	}
	
	/**
     * Get the current Direction the Wind moves at
     *
     * @return an int, direction, representing the current direction of the wind
     *
     * @ensure an int, direction, representing the current direction of the wind
     */
	public int getDirection() {
		return this.direction;
	}
	
	/**
     * Get the current Length of time the Wind lasts before changing
     *
     * @return a double, length, representing the current direction of the wind
     *
     * @ensure a double, length, representing the current direction of the wind
     */
	public double getLength() {
		return this.length;
	}
	
	/**
     * Get the current minimum Length the Wind lasts before changing
     *
     * @return a double, minimumWindLength, representing the minimum length of time before wind changes
     *
     * @ensure a double, minimumWindLength, representing the minimum length of time before wind changes
     */
	public double getMinWindLength() {
		return minimumWindLength;
	}
	
	/**
     * Get the amount of break time between wind changes
     *
     * @return a double, length, representing the length of break
     *
     * @ensure a double, length, representing the length of break
     */
	public double getBreakBetweenWinds() {
		return breakBetweenWinds;
	}
	
	/**
     * Set the new Strength of Wind 
     *
     * @param a float, strength, representing the new strength the wind will move at
     * 				must be between 0 and 1
     *
     * @ensure a float, strength, representing the new strength the wind will move at
     */
	public void setStrength(float strength) {
		if (strength < 0 || strength > 1) {
			throw new IllegalArgumentException("strength must be between 0 and 1");
		} else {
			this.strength = strength;
		}
	}
	
	/**
     * Set the new Direction of Wind 
     *
     * @param an int, direction, representing the new direction the wind will move at
     * 		must be -1 for left, 0 for none of 1 for right
     *
     * @ensure an int, direction, representing the new direction the wind will move at
     */
	public void setDirection(int direction) {
		if (direction != -1 && direction != 0 && direction != 1) {
			throw new IllegalArgumentException("direction must be -1, 0 or 1");
		} else {
			this.direction = direction;
		}
	}
	
	/**
     * Set the Length of time Wind lasts before changing
     *
     * @param a double, length, representing the new amount of time Wind lasts before changing
     * 			must be > 0
     *
     * @ensure a double, length, representing the new amount of time Wind lasts before changing
     */
	public void setLength(double length) {
		if (length < 0) {
			throw new IllegalArgumentException("length must be >= 0");
		} else {
			this.length = length;
		}
	}
	
}