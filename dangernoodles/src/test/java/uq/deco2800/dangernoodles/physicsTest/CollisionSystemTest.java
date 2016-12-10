package uq.deco2800.dangernoodles.physicsTest;

import org.hibernate.validator.cfg.defs.AssertTrueDef;
import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.physics.QuadTree;
import uq.deco2800.dangernoodles.systems.CollisionSystem;

import javax.validation.constraints.AssertTrue;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Created by Luke on 7/09/2016.
 */
public class CollisionSystemTest {
    World world = new World(1000,1000);

    //EntityOne
    //A noodle entity properly created for collisions
    PositionComponent p1 = new PositionComponent(0,0);
    CollisionComponent c1 = new CollisionComponent(true,
            CollisionComponent.shape.RECTANGLE);
    RectangleComponent r1 = new RectangleComponent(100, 100);

    //EntityTwo
    //A noodle Entity properly created for collisions
    PositionComponent p2 = new PositionComponent(50,50);
    CollisionComponent c2 = new CollisionComponent(true,
            CollisionComponent.shape.RECTANGLE);
    RectangleComponent r2 = new RectangleComponent(100, 100);

    @Test
    public void testOne() {
        Entity e1 = this.world.createEntity();

        e1.addComponent(p1)
                .addComponent(c1)
                .addComponent(r1);

        Entity e2 = this.world.createEntity();

        e2.addComponent(p2)
                .addComponent(c2)
                .addComponent(r2);

        world.addSystem(new CollisionSystem(), 1);
        world.process(2, 0.2);

        for (CollisionComponent c : world.getComponents(CollisionComponent.class)) {
            assertTrue(c.getCollisions().size() == 0);
        }
    }
}
