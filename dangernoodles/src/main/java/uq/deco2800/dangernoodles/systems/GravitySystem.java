package uq.deco2800.dangernoodles.systems;

import uq.deco2800.dangernoodles.components.GravityComponent;
import uq.deco2800.dangernoodles.components.MassComponent;
import uq.deco2800.dangernoodles.components.MovementComponent;
import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.World;

/**
 * This system assigns a vertical acceleration to every object which should be experiencing gravity. The magnitude of
 * gravity is determined by the object's mass.
 */
public class GravitySystem implements uq.deco2800.dangernoodles.ecs.System {
    @Override
    public void run(World world, double t, double dt) {
        for (ComponentMap cm : world.getIterator(MovementComponent.class, GravityComponent.class,
                MassComponent.class)) {
            MovementComponent m = cm.get(MovementComponent.class);
            GravityComponent g = cm.get(GravityComponent.class);

            // If you'd like to change the gravity to a non-constant value, please inform the AI team, as changing it
            // has a direct implication on shooting accuracy of AI. Thank you.
            if (g.getGravity()) {
                m.setAcceleration(m.getAX(), 500);
            }
        }
    }
}
