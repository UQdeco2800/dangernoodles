package uq.deco2800.dangernoodles.components.damages;

import uq.deco2800.dangernoodles.ecs.Component;

import java.math.BigDecimal;

/**
 * Created by khoi_truong on 2016/08/31.
 * <p>
 * This class is used to handle damage that entity can cause.
 */
public class InstantDamageComponent extends Component {
    // Private field to store information.
    private double amount;
    private double defaultAmount;

    /**
     * Default constructor for this class.
     * <p>
     * Upon instantiation, default value for damage will also be set. This allows caller to fall back when needed.
     *
     * @param amount
     *         a double representing amount of this entity
     *
     * @throws IllegalArgumentException
     *         if amount is less 0
     * @require amount >= 0
     * @ensure new instance of this component with given amount of damage
     */
    public InstantDamageComponent(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be less than 0.");
        }
        this.amount = defaultAmount = amount;
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
     * Reset the amount of damage this entity deals to default value.
     *
     * @ensure the amount of damage this entity deals will be reset to default value
     */
    public void resetDefault() {
        amount = defaultAmount;
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
        return "Current entity: " + getEntity().toString() + " deals an amount of " + amount + ".";
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
        } else if (!(obj instanceof InstantDamageComponent)) {
            return false;
        } else {
            InstantDamageComponent converted = (InstantDamageComponent) obj;
            BigDecimal preciseAmount = BigDecimal.valueOf(amount);
            BigDecimal preciseDefaultAmount = BigDecimal.valueOf(defaultAmount);
            BigDecimal preciseConvertedAmount = BigDecimal.valueOf(converted.amount);
            BigDecimal preciseConvertedDefaultAmount = BigDecimal.valueOf(converted.defaultAmount);
            return preciseAmount.equals(preciseConvertedAmount) &&
                    preciseDefaultAmount.equals(preciseConvertedDefaultAmount);
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
        return prime;
    }
}
