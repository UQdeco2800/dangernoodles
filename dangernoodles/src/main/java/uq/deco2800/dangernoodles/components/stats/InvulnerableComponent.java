package uq.deco2800.dangernoodles.components.stats;

import uq.deco2800.dangernoodles.ecs.Component;

/**
 * Created by khoi_truong on 2016/08/30.
 * <p>
 * This class is used to represent if this entity is invulnerable or not.
 */
public class InvulnerableComponent extends Component {
    // Private fields to store information.
    private boolean invulnerable;

    /**
     * Default constructor for this component.
     * <p>
     * By default, invulnerable variable will be set to false upon
     * instantiation.
     *
     * @ensure new instance of this component
     */
    public InvulnerableComponent() {
        invulnerable = false;
    }

    /**
     * Check if this entity is invulnerable.
     *
     * @return a boolean representing whether this entity is invulnerable
     *
     * @ensure a boolean representing whether this entity is invulnerable
     */
    public boolean isInvulnerable() {
        return invulnerable;
    }

    /**
     * Set current invulnerability status to given argument.
     *
     * @param status
     *         a boolean representing whether this entity is invulnerable
     *
     * @ensure isInvulnerable() == status
     */
    public void setInvulnerable(boolean status) {
        this.invulnerable = status;
    }

    /**
     * Reset the invulnerability status of this entity to false.
     *
     * @ensure the invulnerability status of this entity is reset to false
     */
    public void resetDefault() {
        invulnerable = false;
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
        return "Current entity is" + (invulnerable ? " invulnerable." : " vulnerable.");
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
        } else if (!(obj instanceof InvulnerableComponent)) {
            return false;
        } else {
            InvulnerableComponent converted = (InvulnerableComponent) obj;
            return converted.invulnerable == this.invulnerable;
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
        prime *= + 97;
        prime *= (invulnerable ? 101 : 97) + 307;
        return prime;
    }
}
