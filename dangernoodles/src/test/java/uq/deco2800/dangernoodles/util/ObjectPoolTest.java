package uq.deco2800.dangernoodles.util;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class ObjectPoolTest {
    @Test
    public void testPool() throws Exception {
        ObjectPool<ArrayList> pool = new ObjectPool<>(10, ArrayList.class);

        ArrayList<ArrayList> objects = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            ArrayList o = pool.alloc();
            assertTrue(o != null);
            objects.add(o);
        }

        objects.forEach(pool::free);

        for (int i = 0; i < 10; ++i) {
            ArrayList o = pool.alloc();
            boolean found = false;
            for (ArrayList object : objects) {
                if (o == object) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }
}
