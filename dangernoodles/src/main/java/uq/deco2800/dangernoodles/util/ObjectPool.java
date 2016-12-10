package uq.deco2800.dangernoodles.util;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Stores a number of objects of the same type in memory to not have to pay the initialisation cost
 * for objects that are rapidly created and destroyed.
 * @param <T>
 */
public class ObjectPool<T> {
    private final int maxCapacity;
    private ArrayList<T> objects;
    private BitSet alive;
    private Class<?> type;

    /**
     * Creates a new object pool.
     * @param maxCapacity The number of objects that should be stored
     * @param type The class of the object
     */
    public ObjectPool(int maxCapacity, Class<?> type) {
        this.maxCapacity = maxCapacity;
        this.objects = new ArrayList<>();
        this.alive = new BitSet(maxCapacity);
        this.type = type;

        try {
            type.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                "The class "
                + type.getSimpleName()
                + " passed in to ObjectPool must have a public empty constructor"
            );
        }
    }

    /**
     * Creates a new object of the type
     * @return A new object of type T
     */
    private T constructObject() {
        try {
            return (T) type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    /**
     * Returns an object from the pool if the capacity isn't exceeded. If it is exceeded it just creates
     * a new object normally.
     * @return A object from the pool.
     */
    public T alloc() {
        int nextDead = alive.nextClearBit(0);

        if (nextDead >= maxCapacity) {
            return constructObject();
        }

        alive.set(nextDead);
        if (objects.size() <= nextDead) {
            for (int i = objects.size(); i <= nextDead; ++i) {
                objects.add(constructObject());
            }
        }
        return objects.get(nextDead);
    }

    /**
     * Free an object and make it available in the pool again
     * @param object The object to free
     */
    public void free(T object) {
        for (int i = 0; i < objects.size(); ++i) {
            // NOTE: Checking the pointer not the values
            if (objects.get(i) == object) {
                alive.clear(i);
                return;
            }
        }
    }
}
