package uq.deco2800.dangernoodles.components.weapons;

import uq.deco2800.dangernoodles.ecs.Component;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.weapons.ProjectileDefinition;

public class ProjectileComponent extends Component {
    private ProjectileDefinition definition; // all the static information
    private double lifeTicksLeft; // amount of time it has before it explodes
    private Entity parent; // the object that fired this projectile
    private boolean clearedParent; // has it cleared its parent?
    private boolean destroyNextRun; // should it be destroyed on the next run

    /**
     * Override for ProjectileComponent constructor that has no parent entity
     * creating it
     * 
     * @param definition
     *            the definition of the projectile
     */
    public ProjectileComponent(ProjectileDefinition definition) {
        this(definition, null);
    }
    
    /**
     * Constructor for ProjectileComponent
     * 
     * @param definition
     *            the definition of the projectile
     * @param parent
     *            the entity that
     */
    public ProjectileComponent(ProjectileDefinition definition, Entity parent) {
        this.definition = definition;
        this.lifeTicksLeft = 0.0;
        if (this.definition.timesOut()) {
            this.lifeTicksLeft = (double) this.definition.getLifeTicks();
        }
        this.parent = parent;
        this.clearedParent = false; // by default it hasn't cleared its parent
    }

    public ProjectileDefinition getDefinition() {
        return definition;
    }

    public boolean timesOut() {
        return definition.timesOut();
    }

    public boolean timedOut() {
        if (this.timesOut()) {
            return lifeTicksLeft <= 0;
        }
        return false;
    }

    public double ticksLeft() {
        return lifeTicksLeft < 0 ? 0 : lifeTicksLeft;
    }

    public void decrementTicks(double dt) {
        if (this.timesOut()) {
            lifeTicksLeft -= (int)(1000 * dt);
        }
    }
    
    /**
     * Gets the Entity of the object that "fired" the projectile
     * 
     * @return The ECS Entity of the object that fired the projectile
     */
    public Entity getParent() {
        return this.parent;
    }

    /**
     * Checks if the projectile has been found to have finished colliding with
     * its parent
     * 
     * @return Whether or not the projectile has cleared its parent
     */
    public boolean hasClearedParent() {
        return this.clearedParent;
    }

    /**
     * Sets the clearedParent flag signifying that projectile has finished
     * colliding with its parent
     */
    public void clearedParent() {
        this.clearedParent = true;
    }
    
    /**
     * Raise the flag to destroy this on the next run
     */
    public void destroyNextRun() {
        this.destroyNextRun = true;
    }

    /**
     * Check if this is flagged to be destroyed
     * 
     * @return whether this projectile should be destroyed
     */
    public boolean shouldDestroy() {
        return this.destroyNextRun;
    }
}
