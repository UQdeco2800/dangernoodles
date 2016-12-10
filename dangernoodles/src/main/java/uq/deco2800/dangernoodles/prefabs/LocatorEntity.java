package uq.deco2800.dangernoodles.prefabs;

import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

/**
 * This entity is meant to be used as a marker to mark the position of interest in the game. This is used for
 * development purposes such as displaying the target position that AI aims to shoot.
 * 
 * @author Irsan
 *
 */
public class LocatorEntity {
    /**
     * Private constructor to hide the implicit public one.
     */
    private LocatorEntity() {}

    public static Entity createLocator(World world, double positionX, double positionY) {
        return world.createEntity().addComponent(new PositionComponent(positionX, positionY))
                .addComponent(new SpriteComponent(16, 16, "/projectiles/test.png"));
    }
}
