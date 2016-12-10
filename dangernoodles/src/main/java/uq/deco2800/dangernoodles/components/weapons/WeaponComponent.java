package uq.deco2800.dangernoodles.components.weapons;

import uq.deco2800.dangernoodles.ecs.Component;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.weapons.WeaponDefinition;

public class WeaponComponent extends Component {

    private WeaponDefinition definition;
    private Entity parent;
    private boolean fired;
    private boolean firing;
    private double power;
    private double direction;
    
    // Whether incremented power should be added
    private boolean incrementPowerAdd;

    /**
     * Constructs a WeaponComponent which stores necessary information about the
     * weapon. Such as: what type of weapon it is, the player using it, whether
     * or not it has been fired, the amount of power the player used to fire the
     * weapon, and the last direction the weapon faced.
     * 
     * @param definition
     *            the type of weapon used by the WeaponComponent
     * @param parent
     *            the player entity using the WeaponComponent
     */
    public WeaponComponent(WeaponDefinition definition, Entity parent) {
        this.definition = definition;
        this.parent = parent;
        this.incrementPowerAdd = true;
        fired = false;
        power = 0.0;
        direction = 0.0;
    }

    /**
     * Gets the weapon definition
     * 
     * @return the definition of the weapon
     */
    public WeaponDefinition getDefinition() {
        return definition;
    }

    /**
     * Gets the parent noodle (i.e. the player who is holding the weapon)
     * 
     * @return Noodle that is holding the weapon
     */
    public Entity getParent() {
        return parent;
    }

    /**
     * Gets whether or not the weapon has been fired
     * 
     * @return a fired boolean
     */
    public boolean isFired() {
        return fired;
    }

    /**
     * Sets whether or not the weapon has been fired by a player
     * 
     * @param fired
     *            a boolean (indicating if the weapon is fired or not)
     */
    public void setFired(boolean fired) {
        this.fired = fired;
    }

    /**
     * Set's whether or not the weapon is in the process of firing
     * 
     * @return whether the weapon is currently firing
     */
    public boolean isFiring() {
        return firing;
    }

    /**
     * Sets whether or not the weapon has been fired by a player NOTE: If you're
     * trying to tell a Weapon to fire, this will not cause it to fire until you
     * set it to false again. It is used to give the appearance of firing over
     * time
     * 
     * @param firing
     *            whether or not the weapon should be firing
     */
    public void setFiring(boolean firing) {
        // Check if this is the first time this has been set
        if (firing && !this.firing) {
            this.power = 0;
        }
        // Check if this is the last time (they released the mouse)
        if (this.firing && !firing) {
            this.fired = true;
        }
        this.firing = firing;
    }

    /**
     * Gets amount of power the weapon has been fired with
     * 
     * @return a double of the power value
     */
    public double getPower() {
        return power;
    }

    /**
     * Sets the power level of the weapon, as chosen by the player
     * 
     * @param power
     *            a double of the power to be used by the weapon
     */
    public void setPower(double power) {
        this.power = power;
    }
    
    /**
     * Increments the power to shoot with by a given amount. Ensures that power
     * stays within the range of 0-100, and return true if you would have
     * exceeded that range. If goes over, starts incrementing down (and vice 
     * versa). Returns if a bounce happened this turn
     * 
     * @param inc
     *            The amount to increment the power by
     * @return Whether power's bounds (0-100) would have been exceeded
     */
    public boolean incrementPower(double inc) {
        if (!firing)
            return false;

        this.power += incrementPowerAdd ? inc : -inc;

        // check to see if you have to bounce
        if (power > 100) {
            power = 100;
            incrementPowerAdd = false;
            return true;
        }
        if (power < 0) {
            power = 0;
            incrementPowerAdd = true;
            return true;
        }

        if (power < 0) {
            power = 0;
            return true;
        }

        return false;
    }

    /**
     * The direction in which the weapon is currently facing
     * 
     * @return a double of the direction (between 0 and 360)
     */
    public double getDirection() {
        return direction;
    }

    /**
     * Sets the direction of the weapon, calculated by mouse position in
     * relation to the weapon
     * 
     * @ensure direction >= 0 && direction < 360
     * 
     * @param direction
     *            a double of the direction of the weapon
     */
    public void setDirection(double direction) {
        this.direction = direction;
    }
}
