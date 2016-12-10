package uq.deco2800.dangernoodles.prefabs;

import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.components.displays.ShopDisplayComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

/**
 * Created by minhnguyen on 4/09/2016.
 * <p>
 * This class is used to create an shop in the world.
 */
public class GameShop {
    /**
     * Private constructor to hide the implicit public one.
     */
    private GameShop() {}
    /**
     * Create a new game shop in the world.
     *
     * @param world
     *          Parent world object.
     * @return
     *      The created world entity.
     */
    public static Entity createShop(World world) {

    	return world.createEntity()
                .addComponent(new NameComponent("Noodle Shop"))
                .addComponent(new SpriteComponent(50, 50, "Store.png"))
                .addComponent(new ShopComponent())
                .addComponent(new ShopDisplayComponent());
    }

}
