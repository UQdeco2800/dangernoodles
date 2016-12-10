package uq.deco2800.dangernoodles.components.stats;

import uq.deco2800.dangernoodles.ecs.Component;

import java.math.BigDecimal;

/**
 * Created by khoi_truong on 2016/09/16.
 * <p>
 * This class is used to represent the base damage that each noodle has.
 */
public class DamageComponent extends Component {
    // Private fields to store information
    private double damage;
    private double defaultDamage;

    /**
     * Default constructor for this class.
     * <p>
     * Upon instantiation, default value for damage will also be set. This
     * allows caller to fall back when needed.
     *
     * @param damage
     *         a double representing damage of this entity
     *
     * @throws IllegalArgumentException
     *         if damage is less 0
     * @require damage >= 0
     * @ensure new instance of this component with given damage
     */
    public DamageComponent(double damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be less than 0.");
        }
        this.damage = defaultDamage = damage;
    }

    /**
     * Return damage of this entity.
     *
     * @return a double representing damage of this entity
     *
     * @ensure a double representing damage of this entity
     */
    public double getDamage() {
        return damage;
    }

    /**
     * Set damage for this entity.
     *
     * @param newSpeed
     *         a double representing damage of this entity
     *
     * @throws IllegalArgumentException
     *         if newSpeed is less than 0
     * @require damage >= 0
     * @ensure getDamage() == newSpeed
     */
    public void setDamage(double newSpeed) {
        if (newSpeed < 0) {
            throw new IllegalArgumentException("Damage cannot be less than 0.");
        }
        this.damage = newSpeed;
    }

    /**
     * Reset the damage to default value.
     *
     * @ensure damage will be reset to default value
     */
    public void resetDefault() {
        damage = defaultDamage;
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
        return "Current entity with damage of " + damage + ".";
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
        } else if (!(obj instanceof DamageComponent)) {
            return false;
        } else {
            DamageComponent converted = (DamageComponent) obj;
            BigDecimal preciseSpeed = BigDecimal.valueOf(damage);
            BigDecimal preciseDefaultSpeed = BigDecimal.valueOf(defaultDamage);
            BigDecimal preciseConvertedSpeed = BigDecimal.valueOf(converted.damage);
            BigDecimal preciseConvertedDefaultSpeed = BigDecimal.valueOf(converted.defaultDamage);
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
        prime *= damage * 37 + defaultDamage;
        return Math.abs(prime);
    }
}
