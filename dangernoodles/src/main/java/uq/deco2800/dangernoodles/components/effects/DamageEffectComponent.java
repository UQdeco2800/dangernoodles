package uq.deco2800.dangernoodles.components.effects;

import java.math.BigDecimal;

/**
 * Created by khoi_truong on 2016/09/16.
 * <p>
 * This class is used to represent a damage effect.
 */
public class DamageEffectComponent extends EffectComponent {
    // Private field to store the value of this effect
    private double damageValue;

    /**
     * Default constructor of this class.
     *
     * @param name
     *         an EffectEnum representing the name of this effect
     * @param turnDuration
     *         an integer representing the duration of this effect
     * @param value
     *         a double representing the value of this effect
     *
     * @throws NullPointerException
     *         if name is null
     * @throws IllegalArgumentException
     *         if turnDuration is less than 0 or value is less than 0 if this is
     *         a buff or value is more than or equal to 0 if this is a debuff
     * @require name != null && turnDuration >= 0 && (value >= 0 if
     * name.isBuff() || value < 0 if !name.isBuff)
     * @ensure new instance of this class
     */
    public DamageEffectComponent(EffectEnum name,
                                 int turnDuration,
                                 double value) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null.");
        } else if (name.isBuff() && value < 0) {
            throw new IllegalArgumentException("Value cannot be less than " +
                    "zero for a buff");
        } else if (!name.isBuff() && value >= 0) {
            throw new IllegalArgumentException("Value cannot be more than or " +
                    "equal to zero for a debuff.");
        } else if (turnDuration < 0) {
            throw new IllegalArgumentException("Duration cannot be less than " +
                    "zero.");
        }
        this.name = name;
        this.turnDuration = turnDuration;
        this.damageValue = value;
        this.isBuff = name.isBuff();
    }

    /**
     * Return the damage addition of this effect.
     *
     * @return a double representing the damage addition of this effect
     *
     * @ensure a double representing the damage addition of this effect
     */
    public double getDamage() {
        return damageValue;
    }

    /**
     * Set the damage addition of this effect.
     *
     * @param value
     *         a double representing the damage addition of this effect
     *
     * @throws IllegalArgumentException
     *         if value is less than 0 if this is a buff or value is more than
     *         or equal to 0 if this is a debuff
     * @require value >= 0 if name.isBuff() || value < 0 if !name.isBuff
     * @ensure getDamage() == value
     */
    public void setDamage(double value) {
        if (name.isBuff() && value < 0) {
            throw new IllegalArgumentException("Value cannot be less than " +
                    "zero for a buff");
        } else if (!name.isBuff() && value >= 0) {
            throw new IllegalArgumentException("Value cannot be more than or " +
                    "equal to zero for a debuff.");
        }
        this.damageValue = value;
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
        return "Current effect gives damage of " + damageValue + ".";
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
        } else if (!(obj instanceof DamageEffectComponent)) {
            return false;
        } else {
            DamageEffectComponent converted = (DamageEffectComponent) obj;
            BigDecimal preciseEffect = BigDecimal.valueOf(damageValue);
            BigDecimal preciseConverted = BigDecimal.valueOf(converted.damageValue);
            return preciseEffect.equals(preciseConverted) &&
                    this.name == converted.name &&
                    this.turnDuration == converted.turnDuration &&
                    this.isBuff == converted.isBuff();
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
        prime *= damageValue * 37;
        return Math.abs(prime);
    }
}
