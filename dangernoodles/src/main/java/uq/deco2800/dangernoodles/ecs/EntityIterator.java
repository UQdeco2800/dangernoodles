package uq.deco2800.dangernoodles.ecs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents an iterator of all the entities that implement a certain set of components
 */
public class EntityIterator implements Iterator<ComponentMap>, Iterable<ComponentMap> {
    private ArrayList<ComponentMap> entities = new ArrayList<>();
    private int location = 0;

    /**
     * Creates an instance of EntityIterator. The EntityIterator created 
     * contains ComponentMaps of all the entities that have the specified 
     * components.
     * 
     * @param world Current game world
     * @param components Components requested
     */
    EntityIterator(World world, Class<? extends Component>[] components) {
        // If the entity has no entities, throws exception
        if (components.length < 1) {
            throw new IllegalArgumentException("Entity Iterator must be initialised with at least 1 component");
        }

        for (int i = 0; i < world.generations.size(); ++i) {
            Entity e = new Entity(i, world.generations.get(i), world);
            if (this.shouldAddEntity(world, e, components)) {
                ComponentMap cm = new ComponentMap(e);
                for (Class<? extends Component> c : components) {
                    cm.add(c, world.getComponent(e, c).get());
                }
                entities.add(cm);
            }
        }
    }

    /**
     * Checks if all an entity's components are part of the entity.
     * 
     * @param world current game world
     * @param e entity
     * @param components component of entity requested
     * @return true of the entity has all the components, false otherwise
     */
    private boolean shouldAddEntity(World world, Entity e, Class<? extends Component>[] components) {
        for (Class<? extends Component> c : components) {
            if (!world.hasComponent(e, c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns itself as an iterator
     * @return This
     */
    @Override
    public Iterator<ComponentMap> iterator() {
        return this;
    }

    /**
     * Check if there is a next entity
     * @return If there is an entity next
     */
    @Override
    public boolean hasNext() {
        return location < entities.size();
    }

    /**
     * Returns the next Entity
     * @return Next entity
     */
    @Override
    public ComponentMap next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        return this.entities.get(location++);
    }
}
