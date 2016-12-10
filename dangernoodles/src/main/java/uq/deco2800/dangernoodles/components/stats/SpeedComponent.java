package uq.deco2800.dangernoodles.components.stats;

import uq.deco2800.dangernoodles.ecs.Component;

import java.math.BigDecimal;

/**
 * Created by khoi_truong on 2016/08/30.
 * <p>
 * This class is used to capture the movement speed of an entity.
 */
public class SpeedComponent extends Component {
    // Private fields to store information
    private double speed;
    private double defaultSpeed;

    /**
     * Default constructor for this class.
     * <p>
     * Upon instantiation, default value for speed will also be set. This allows
     * caller to fall back when needed.
     *
     * @param speed
     *         a double representing speed of this entity
     *
     * @throws IllegalArgumentException
     *         if speed is less 0
     * @require speed >= 0
     * @ensure new instance of this component with given speed
     */
    public SpeedComponent(double speed) {
        if (speed < 0) {
            throw new IllegalArgumentException("Speed cannot be less than 0.");
        }
        this.speed = defaultSpeed = speed;
    }

    /**
     * Return speed of this entity.
     *
     * @return a double representing speed of this entity
     *
     * @ensure a double representing speed of this entity
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Set speed for this entity.
     *
     * @param newSpeed
     *         a double representing speed of this entity
     *
     * @throws IllegalArgumentException
     *         if newSpeed is less than 0
     * @require speed >= 0
     * @ensure getDamage() == newSpeed
     */
    public void setSpeed(double newSpeed) {
        if (newSpeed < 0) {
            throw new IllegalArgumentException("Speed cannot be less than 0.");
        }
        this.speed = newSpeed;
    }

    /**
     * Reset the speed to default value.
     *
     * @ensure speed will be reset to default value
     */
    public void resetDefault() {
        speed = defaultSpeed;
    }

    /**
     * Return a string representation of this component.
     *
     * @return a string representation of this component
     *
     * @ensure a string representation of this component
     */
    @Override
    public String toString() {
        return "Current entity with speed of " + speed + ".";
    }

    /**
     * Check if this component is equal to given object.
     *
     * @param obj
     *         an object to be checked against
     *
     * @return a boolean representing whether this component is equal to given
     * object
     *
     * @throws NullPointerException
     *         if object is null
     * @require obj != null
     * @ensure a boolean representing whether this component is equal to given
     * object
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            throw new NullPointerException("Object cannot be null.");
        } else if (!(obj instanceof SpeedComponent)) {
            return false;
        } else {
            SpeedComponent converted = (SpeedComponent) obj;
            BigDecimal preciseSpeed = BigDecimal.valueOf(speed);
            BigDecimal preciseDefaultSpeed = BigDecimal.valueOf(defaultSpeed);
            BigDecimal preciseConvertedSpeed = BigDecimal.valueOf(converted.speed);
            BigDecimal preciseConvertedDefaultSpeed = BigDecimal.valueOf(converted.defaultSpeed);
            return preciseSpeed.equals(preciseConvertedSpeed) &&
                    preciseDefaultSpeed.equals(preciseConvertedDefaultSpeed);
        }
    }

    /**
     * Return the hash code of this component
     *
     * @return an integer representing the hashcode of this component
     *
     * @ensure an integer representing the hashcode of this component
     */
    @Override
    public int hashCode() {
        int prime = 37;
        prime *= speed * 37 + defaultSpeed;
        return Math.abs(prime);
    }
}
