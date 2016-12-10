package uq.deco2800.dangernoodles.components.effects;

/**
 * Created by khoi_truong on 2016/09/16.
 * <p>
 * This class is used to represent an invulnerable effect.
 */
public class InvulnerableEffectComponent extends EffectComponent {
    // Private field to store the value of this effect
    private boolean invulnerableValue;

    /**
     * Default constructor of this class.
     *
     * @param name
     *         an EffectEnum representing the name of this effect
     * @param turnDuration
     *         an integer representing the duration of this effect
     * @param value
     *         a bool representing if this effect is active or not
     *
     * @throws NullPointerException
     *         if name is null
     * @require name != null && turnDuration >= 0
     * @ensure new instance of this class
     */
    public InvulnerableEffectComponent(EffectEnum name,
                                       int turnDuration,
                                       boolean value) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null.");
        } else if (turnDuration < 0) {
            throw new IllegalArgumentException("Duration cannot be less than " +
                    "zero.");
        }
        this.name = name;
        this.turnDuration = turnDuration;
        this.invulnerableValue = value;
        this.isBuff = name.isBuff();
    }

    /**
     * Return whether invulnerable is active or not
     *
     * @return a bool representing whether invulnerable is active or not
     *
     * @ensure a bool representing whether invulnerable is active or not
     */
    public boolean isInvulnerable() {
        return invulnerableValue;
    }

    /**
     * Set whether invulnerable is active or not
     *
     * @param value
     *         a bool representing whether invulnerable is active or not
     *
     * @ensure isInvulnerable() == value;
     */
    public void setInvulnerable(boolean value) {
        this.invulnerableValue = value;
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
        return "Current effect gives " + (invulnerableValue ? "invulnerable"
                : "vulnerable") + ".";
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
        } else if (!(obj instanceof InvulnerableEffectComponent)) {
            return false;
        } else {
            InvulnerableEffectComponent converted = (InvulnerableEffectComponent) obj;
            return this.invulnerableValue == converted.invulnerableValue &&
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
        prime *= (invulnerableValue ? 35 : 92) * 37;
        return Math.abs(prime);
    }
}
