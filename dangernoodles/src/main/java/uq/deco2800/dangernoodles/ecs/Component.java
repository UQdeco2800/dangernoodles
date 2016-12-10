package uq.deco2800.dangernoodles.ecs;

/**
 * The base abstract class every component should implement.
 */
public abstract class Component {
    private Entity entity = null;

    /**
     * Gets the entity that the component belongs to
     * @return The Entity
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Sets the entity the component belongs to
     * @param entity The new entity
     */
    void setEntity(Entity entity) {
        this.entity = entity;
    }
}
