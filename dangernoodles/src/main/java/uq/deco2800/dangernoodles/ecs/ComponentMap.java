package uq.deco2800.dangernoodles.ecs;

import java.util.HashMap;

/**
 * A group of components all belonging to a single entity.
 */
public class ComponentMap {
    private Entity entity;
    private HashMap<Class<? extends Component>, Component> map = new HashMap<>();

    /**
     * Creates a new ComponentMap attached to the given Entity
     * @param entity The entity
     */
    ComponentMap(Entity entity) {
        this.entity = entity;
    }

    /**
     * Add a new component to the component map
     * @param type The type of the component
     * @param c The component
     */
    protected void add(Class<? extends Component> type, Component c) {
        this.map.put(type, c);
    }

    /**
     * Get a component from the map
     * @param type The type of the component
     * @param <T> The generic type of the component
     * @return The component of that type
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<? extends Component> type) {
        return (T) this.map.get(type);
    }

    /**
     * Gets the entity attached to the map
     */
    public Entity getEntity() {
        return entity;
    }
}
