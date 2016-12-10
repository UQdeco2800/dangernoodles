package uq.deco2800.dangernoodles.ecs;

import java.util.*;

import uq.deco2800.dangernoodles.weapons.WeaponDefinitionList;

/**
 * The world class holds all the entities and all the systems, thus it is the
 * starting point for all simulation.
 */
public class World {
    ArrayList<Integer> generations = new ArrayList<>();
    private Deque<Integer> dead = new ArrayDeque<>();
    private double width;
    private double height;
    
    // Weapon Definition List
    private WeaponDefinitionList weaponDefinitions;

    /**
     * Creates a new world with the given width and height
     * @param width Width of the world
     * @param height Height of the world
     */
    public World(double width, double height) {
        this.width = width;
        this.height = height;
        weaponDefinitions = new WeaponDefinitionList();
    }

    private HashMap<Class<? extends Component>, ArrayList<Component>> components = new HashMap<>();

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    /**
     * A private class that contains a system and its priority
     */
    private class SystemInfo {
        System system;
        int priority;

        SystemInfo(System system, int priority) {
            this.system = system;
            this.priority = priority;
        }

        int getPriority() {
            return this.priority;
        }

        System getSystem() {
            return this.system;
        }
    }

    private ArrayList<SystemInfo> systems = new ArrayList<>();

    /**
     * Creates a new entity empty.
     * @return Entity
     */
    public Entity createEntity() {
        if (!this.dead.isEmpty()) {
            int id = this.dead.pop();
            int gen = generations.get(id) + 1;

            generations.set(id, gen);

            return new Entity(id, gen, this);
        } else {
            int id = generations.size();
            generations.add(0);

            return new Entity(id, 0, this);
        }
    }

    /**
     * An iterator over all the entities that have the given components.
     * @param components Variable arguments of all the components needed
     * @return A new entity iterator on a set of components
     */
    @SafeVarargs
    public final EntityIterator getIterator(Class<? extends Component>... components) {
        return new EntityIterator(this, components);
    }

    /**
     * Destroys an existing entity
     * @param entity The entity to be destroyed
     */
    public void destroyEntity(Entity entity) {
        for (ArrayList<Component> comp : components.values()) {
            Iterator<Component> iter = comp.iterator();
            while (iter.hasNext()) {
                if (iter.next().getEntity().equals(entity)) {
                    iter.remove();
                }
            }
        }

        this.dead.push(entity.getId());
    }

    /**
     * Associates a component with an entity
     * @param entity The entity the component has to be added to
     * @param component The component to be added
     * @param <T> The type of the component
     */
    public <T extends Component> void addComponent(Entity entity, T component) {
        Class<? extends Component> type = component.getClass();
        if (!this.components.containsKey(type)) {
            this.components.put(type, new ArrayList<>());
        }

        ArrayList<Component> comps = this.components.get(type);
        component.setEntity(entity);
        comps.add(component);
    }

    /**
     * Checks if the given entity has the given component
     * @param entity Entity to be checked
     * @param type Component to be tested if included in entity
     * @return true if the entity has the component requested, false otherwise
     */
    public <T extends Component> Boolean hasComponent(Entity entity, Class<T> type) {
        if (this.components.containsKey(type)) {
            for (T c : this.getComponents(type)) {
                if (c.getEntity().equals(entity)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Removes the given component from the given entity
     * @param entity Entity to have component removed from
     * @param type Component to be removed from entity
     */
    public <T extends Component> void removeComponent(Entity entity, Class<T> type) {
        if (this.components.containsKey(type)) {
            Iterator<T> iter = this.getComponents(type).iterator();
            while (iter.hasNext()) {
                T c = iter.next();
                if (c.getEntity().equals(entity)) {
                    iter.remove();
                }
            }
        }
    }

    /**
     * Gets the component of a specified type for an entity
     * @param entity The entity to get the component for
     * @param type The type of the component, e.g. <pre>PositionComponent.class</pre>
     * @param <T> The component's type
     * @return Returns an optional that either contains the component or is empty
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> Optional<T> getComponent(Entity entity, Class<T> type) {
        if (!this.components.containsKey(type)) {
            return Optional.empty();
        }

        for (Component c : this.components.get(type)) {
            if (c.getEntity().equals(entity)) {
                return Optional.of((T) c);
            }
        }
        return Optional.empty();
    }

    /**
     * Get a list of all the components of a given type
     * @param type The type of the component to be gotten, e.g. <pre>PositionComponent.class</pre>
     * @param <T> The component's type
     * @return Returns a list with all the components of that type
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> List<T> getComponents(Class<T> type) {
        return (List<T>) this.components.get(type);
    }

    /**
     * Adds a new system with a given priority to the world
     * @param system The system to be added
     * @param priority The priority, where lower runs first
     */
    public void addSystem(System system, int priority) {
        this.systems.add(new SystemInfo(system, priority));
        this.systems.sort(Comparator.comparing(SystemInfo::getPriority));
    }

    /**
     * Is called every tick and runs each of the systems according to priority with the t and dt parameters
     * @param t Time since the beginning of the game
     * @param dt The delta time, i.e. the time since last frame
     */
    public void process(double t, double dt) {
        for (SystemInfo s : this.systems) {
            s.system.run(this, t, dt);
        }
    }
    
    /**
     * Gets a copy of the WeaponDefinitionList used by the world
     * 
     * @return A copy of the WeaponDefinitionList used by the world
     */
    public WeaponDefinitionList getWeaponDefinitions() {
        return new WeaponDefinitionList(this.weaponDefinitions.getInventory());
    }
}
