package uq.deco2800.dangernoodles.prefabs;

import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

/**
 * Created by Jason on 3/09/2016.
 * <p>
 * This class is used to create a parachute entity that are attached to the effect box.
 */
public class Parachute {

    /**
     * Private constructor to hide the implicit public one.
     */
    private Parachute() {}

    /**
     * This method will create a parachute entity inside the world
     *
     * @param world
     *         Parent world object.
     *
     * @return The created world entity.
     */
    public static Entity createParachute(World world, double x, double y) {
        return  world.createEntity()
                .addComponent(new NameComponent("Parachute"))
                .addComponent(new PositionComponent(x, y))
                .addComponent(new SpriteComponent(40, 50, "/BuffCrate/para.png"))
                .addComponent(new MassComponent(5.0))
                .addComponent(new GravityComponent(true))
                .addComponent(new RectangleComponent(30, 30))
                .addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
                .addComponent(new MovementComponent(0.0, 0.0, 1.0, 0.0));

    }
}
