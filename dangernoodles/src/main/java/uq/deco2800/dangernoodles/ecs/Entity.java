package uq.deco2800.dangernoodles.ecs;

/**
 * The entity class that represents every object in the world
 */
public class Entity {
    private final int id;
    private final int generation;
    private final World world;

    /**
     * Creates a new entity. Note an id + generation combo is unique during a run of the game
     * @param id The id associated with the entity. Note this is reused.
     * @param generation The entity's generation, increments each time the id is reused.
     * @param world The world the entity belongs to
     */
    public Entity(int id, int generation, World world) {
        this.id = id;
        this.generation = generation;
        this.world = world;
    }
    
    /**
     * @return The World the entity is in.
     */
    public World getWorld() {
    	return this.world;
    }

    /**
     * Adds a component to the entity, equivalent to <pre>world.addComponent(entity, component);</pre>
     * @param component The component to be added to the entity
     * @param <T> The type of the component
     * @return Returns the current Entity to allow for method chaining
     */
    public <T extends Component> Entity addComponent(T component) {
        this.world.addComponent(this, component);
        return this;
    }

    /**
     * Compares id, generation, and world to see if the entity is equal to another
     * @param o The object to compare to
     * @return Whether the entities are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Entity entity = (Entity) o;

        if (id != entity.id || generation != entity.generation) {
            return false;
        }

        return world != null ? world.equals(entity.world) : entity.world == null;

    }

    /**
     * Hashes the entity
     * @return A hashcode build from the id, generation, and world.
     */
    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + generation;
        result = 31 * result + (world != null ? world.hashCode() : 0);
        return result;
    }

    /**
     * Gets the entity's ID, should only be used by the ecs package.
     * @return The id
     */
    int getId() {
        return id;
    }

    /**
     * Returns a string representation of the Entity.
     * @return A string
     */
    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", generation=" + generation +
                '}';
    }
}
