package uq.deco2800.dangernoodles.physicsTest;

import org.junit.Test;

import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.physics.QuadTree;
import uq.deco2800.dangernoodles.physics.Rectangle;
import uq.deco2800.dangernoodles.util.ObjectPool;

public class QuadTreeTest {

	@Test
	public void checkQuadranttest() {
        World world = new World(1000, 1000);
		QuadTree tree = new QuadTree(0, 0, 1000, 1000, new ObjectPool<>(20, QuadTree.class));

        // Insert enough to cause a split
        for (int i = 0; i < 20; ++i) {
            tree.insert(new Rectangle(new Entity(i, 0, world), i + 5, 0, 5, 5));
        }
	}
}
