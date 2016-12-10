package uq.deco2800.dangernoodles.components.damages;

import uq.deco2800.dangernoodles.ecs.Component;

import java.math.BigDecimal;

/**
 * Created by khoi_truong on 2016/08/31.
 * <p>
 * This class is used to handle the amount of over-time damage that an entity deals.
 */
public class OvertimeDamageComponent extends Component {
    // Private field to store information.
    private double amount;
    private double defaultAmount;
    private double duration;
    private double defaultDuration;

    /**
     * Default constructor for this class.
     * <p>
     * Upon instantiation, default value for damage will also be set. This allows caller to fall back when needed.
     *
     * @param amount
     *         a double representing amount of this entity
     *
     * @throws IllegalArgumentException
     *         if amount or duration is less 0
     * @require amount >= 0 && duration >= 0
     * @ensure new instance of this component with given amount of damage for a duration of time
     */
    public OvertimeDamageComponent(double amount, double duration) {
        if (amount < 0 || duration < 0) {
            throw new IllegalArgumentException("Amount or duration cannot be less than 0.");
        }
        this.amount = defaultAmount = amount;
        this.duration = defaultDuration = duration;
    }

    /**
     * Return the amount of damage this entity deals.
     *
     * @return a double representing the amount of damage this entity deals
     *
     * @ensure a double representing the amount of damage this entity deals
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Set the amount of damage this entity deals.
     *
     * @param newAmount
     *         a double representing the amount of damage this entity deals
     *
     * @throws IllegalArgumentException
     *         if newSpeed is less than 0
     * @require amount >= 0
     * @ensure getAmount() == newAmount
     */
    public void setAmount(double newAmount) {
        if (newAmount < 0) {
            throw new IllegalArgumentException("Amount cannot be less than 0.");
        }
        this.amount = newAmount;
    }

    /**
     * Return the duration of this component
     *
     * @return a double representing the duration of this component
     *
     * @ensure a double representing the duration of this component
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Set the duration of this component
     *
     * @param newDuration
     *         a double representing the duration of this component
     *
     * @throws IllegalArgumentException
     *         if newDuration is less than 0
     * @require newDuration >= 0
     * @ensure getDuration() == newDuration
     */
    public void setDuration(double newDuration) {
        if (newDuration < 0) {
            throw new IllegalArgumentException("Duration cannot be less than 0.");
        }
        this.duration = newDuration;
    }

    /**
     * Reset the amount of damage this entity deals as well as the duration to default values.
     *
     * @ensure the amount of damage this entity deals as well as the duration will be reset to default values
     */
    public void resetDefault() {
        amount = defaultAmount;
        duration = defaultDuration;
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
        return "Current entity: " + getEntity().toString() + " deals an amount of " + amount + " over a duration of " +
                duration + ".";
    }

    /**
     * Check if this component is equal to given object.
     *
     * @param obj
     *         an object to be checked against
     *
     * @return a boolean representing whether this component is equal to given object
     *
     * @throws NullPointerException
     *         if object is null
     * @require obj != null
     * @ensure a boolean representing whether this component is equal to given object
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            throw new NullPointerException("Object cannot be null.");
        } else if (!(obj instanceof OvertimeDamageComponent)) {
            return false;
        } else {
            OvertimeDamageComponent converted = (OvertimeDamageComponent) obj;
            BigDecimal preciseAmount = BigDecimal.valueOf(amount);
            BigDecimal preciseDefaultAmount = BigDecimal.valueOf(defaultAmount);
            BigDecimal preciseDuration = BigDecimal.valueOf(duration);
            BigDecimal preciseDefaultDuration = BigDecimal.valueOf(defaultDuration);
            BigDecimal preciseConvertedAmount = BigDecimal.valueOf(converted.amount);
            BigDecimal preciseConvertedDefaultAmount = BigDecimal.valueOf(converted.defaultAmount);
            BigDecimal preciseConvertedDuration = BigDecimal.valueOf(converted.duration);
            BigDecimal preciseConvertedDefaultDuration = BigDecimal.valueOf(converted.defaultDuration);
            return preciseAmount.equals(preciseConvertedAmount) &&
                    preciseDefaultAmount.equals(preciseConvertedDefaultAmount) &&
                    preciseDuration.equals(preciseConvertedDuration) &&
                    preciseDefaultDuration.equals(preciseConvertedDefaultDuration);
        }
    }

    /**
     * Return the hash code of this component
     *
     * @return an integer representing the hashcode of this component
     *
     * @throws NullPointerException
     *         if entity is not set
     * @ensure an integer representing the hashcode of this component
     */
    @Override
    public int hashCode() {
        if (getEntity() == null) {
            throw new NullPointerException("Entity is not set for this component.");
        }
        int prime = 37;
        prime *= 101;
        prime *= amount * 97 + defaultAmount;
        prime *= duration * 307 + defaultDuration;
        return prime;
    }
}
