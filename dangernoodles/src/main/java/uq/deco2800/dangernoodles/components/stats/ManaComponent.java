package uq.deco2800.dangernoodles.components.stats;

import uq.deco2800.dangernoodles.ecs.Component;

import static java.lang.Math.abs;

/**
 * Created by khoi_truong on 2016/08/30.
 * <p>
 * This class is used to represent the mana of an entity.
 */
public class ManaComponent extends Component {
    // Private field to store information.
    private int capacity;
    private int defaultCapacity;
    private int mana;
    private boolean manaUpdateableStatus = true;

    /**
     * Default constructor for this component.
     * <p>
     * Upon instantiation, argument will be assigned to capacity to represent
     * the maximum amount of mana that this entity can have. Current mana will
     * also be set with this value. Also a default capacity will be assigned to
     * allow caller to resetDefault if needed.
     *
     * @param capacity
     *         an integer representing the maximum amount of mana this entity
     *         can have
     *
     * @throws IllegalArgumentException
     *         if capacity is less than or equal to 0
     * @require capacity > 0
     * @ensure new instance of this component with given capacity
     */
    public ManaComponent(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity cannot be less than or equal to 0.");
        }
        this.capacity = defaultCapacity = mana = capacity;
    }

    /**
     * Return the maximum capacity of mana of this entity
     *
     * @return an integer representing the maximum capacity of mana of this
     * entity
     *
     * @ensure an integer representing the maximum capacity of mana of this
     * entity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Return the current mana of this entity
     *
     * @return an integer representing the current mana of this entity
     *
     * @ensure an integer representing the current mana of this entity
     */
    public int getMana() {
        return mana;
    }

    /**
     * Set maximum capacity of mana of this entity.
     * <p>
     * If the new maximum capacity of mana is less than current mana, then
     * current mana will be capped at the new maximum capacity.
     *
     * @param newCapacity
     *         an integer representing the maximum capacity of mana of this
     *         entity
     *
     * @throws IllegalArgumentException
     *         if newCapacity is less than or equal to 0
     * @require newCapacity > 0
     * @ensure getCapacity() == newCapacity
     */
    public void setCapacity(int newCapacity) {
        if (newCapacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be less than 0.");
        }
        if (newCapacity < mana) {
            mana = capacity = newCapacity;
        } else {
            capacity = newCapacity;
        }
    }

    /**
     * Set current mana of this entity.
     * <p>
     * If newMana is higher than capacity, current mana will be capped at
     * capacity.
     *
     * @param newMana
     *         an integer representing current mana of this entity
     *
     * @throws IllegalArgumentException
     *         if newMana is less than 0
     * @require newMana >= 0
     * @ensure getMana() = newMana
     */
    public void setMana(int newMana) {
        if (manaUpdateableStatus) {
            if (newMana < 0) {
                throw new IllegalArgumentException("Mana cannot be less than 0.");
            }
            if (newMana > capacity) {
                mana = capacity;
            } else mana = newMana;
        } else {
            return;
        }
    }

    /**
     * Reset current mana to maximum capacity. This will be used every turn to
     * resetDefault each entity's mana back to normal.
     *
     * @ensure mana will be resetDefault back to normal before every turn
     */
    public void reset() {
        mana = capacity;
    }

    /**
     * Reset capacity to default value.
     *
     * @ensure capacity is reset to default value
     */
    public void resetDefault() {
        capacity = defaultCapacity;
    }

    /**
     * Return whether mana can be updated for the associated noodle/s.
     *
     * @return a boolean representing whether mana can be updated
     */
    public boolean getManaUpdateableStatus(){
        return manaUpdateableStatus;
    }

    /**
     * Set whether mana can be updated for the associated noodle/s.
     *
     * @param status
     *         a boolean representing whether mana can be updated
     */
    public void setManaUpdateableStatus(boolean status){
        manaUpdateableStatus = status;
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
        return "Current entity with current mana of " + mana + " of a maximum capacity " +
                "of " + capacity + ".";
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
        } else if (!(obj instanceof ManaComponent)) {
            return false;
        } else {
            ManaComponent converted = (ManaComponent) obj;
            return converted.capacity == this.capacity &&
                    converted.defaultCapacity == this.defaultCapacity &&
                    converted.mana == this.mana;
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
        prime *= capacity * 101 + defaultCapacity;
        prime *= mana * 97;
        return abs(prime);
    }
}
