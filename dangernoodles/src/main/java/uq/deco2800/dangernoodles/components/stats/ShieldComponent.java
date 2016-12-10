package uq.deco2800.dangernoodles.components.stats;

import uq.deco2800.dangernoodles.ecs.Component;

import java.math.BigDecimal;

/**
 * Created by khoi_truong on 2016/08/30.
 * <p>
 * This class is used to represent the shield that this entity has.
 */
public class ShieldComponent extends Component {
    // Private fields to store information
    private double shield;
    private double defaultShield;

    /**
     * Default constructor for this class.
     * <p>
     * Upon instantiation, default value of shield will also be set for callers
     * to fall back to if needed.
     *
     * @param shield
     *         a double representing the shield value that this entity has
     *
     * @ensure new instance of this component with given shield value
     */
    public ShieldComponent(double shield) {
        this.shield = defaultShield = shield;
    }

    /**
     * Return the shield value of this entity
     *
     * @return a double representing the shield value that this entity has
     *
     * @ensure a double representing the shield value that this entity has
     */
    public double getShield() {
        return shield;
    }

    /**
     * Set the shield value of this entity
     *
     * @param newShield
     *         a double representing the shield value that this entity has
     *
     * @ensure getShield() = newShield
     */
    public void setShield(double newShield) {
        this.shield = newShield;
    }

    /**
     * Reset shield to default value
     *
     * @ensure shield is reset to default value
     */
    public void resetDefault() {
        shield = defaultShield;
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
        return "Current entity with shield of " + shield + ".";
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
        } else if (!(obj instanceof ShieldComponent)) {
            return false;
        } else {
            ShieldComponent converted = (ShieldComponent) obj;
            BigDecimal preciseShield = BigDecimal.valueOf(shield);
            BigDecimal preciseDefaultShield = BigDecimal.valueOf(defaultShield);
            BigDecimal preciseConvertedShield = BigDecimal.valueOf(converted.shield);
            BigDecimal preciseConvertedDefaultShield = BigDecimal.valueOf(converted.defaultShield);
            return preciseShield.equals(preciseConvertedShield) &&
                    preciseDefaultShield.equals(preciseConvertedDefaultShield);
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
        prime *= 97;
        prime *= shield * 97 + defaultShield;
        return prime;
    }
}
