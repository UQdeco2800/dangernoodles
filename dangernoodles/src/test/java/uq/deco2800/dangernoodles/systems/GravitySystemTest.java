package uq.deco2800.dangernoodles.systems;

import org.junit.Test;
import uq.deco2800.dangernoodles.components.CollisionComponent;
import uq.deco2800.dangernoodles.components.GravityComponent;
import uq.deco2800.dangernoodles.components.MovementComponent;
import uq.deco2800.dangernoodles.components.NameComponent;
import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.World;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Park on 10/24/2016.
 */

public class GravitySystemTest
{
    World testworld = new World(0, 0);
    GravitySystem gravitySystem = new GravitySystem();
    @Test
    public void testRun() {
        for (ComponentMap cm : testworld.getIterator(
                NameComponent.class,
                CollisionComponent.class)) {

            MovementComponent m = cm.get(MovementComponent.class);
            GravityComponent g = cm.get(GravityComponent.class);
            if (g.getGravity()) {
                m.setAcceleration(m.getAX(), 500);
            }


        }
    }

}