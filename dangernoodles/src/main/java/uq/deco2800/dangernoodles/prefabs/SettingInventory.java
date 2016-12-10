package uq.deco2800.dangernoodles.prefabs;

import uq.deco2800.dangernoodles.components.displays.InventoryDisplayComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.components.*;


/**
 * Created by Park on 10/6/2016.
 *
 *
 * This class is used to draw Inventory
 */
public class SettingInventory {

    public SettingInventory(){
        //This method is empty as it returns a new inventory entity in createInventory

    }

    /**
     *
     *
     * @param world
     *          World object.
     * @param x
     *          X coordinate of this entity
     * @param y
     *          Y coordinate of this entity
     *
     * @return
     *      This returns world entity.
     */

    public static Entity createInventory(World world, double x, double y, double width, double height) {
        return world.createEntity()
                .addComponent(new NameComponent("Inventory"))
                .addComponent(new RectangleComponent(width, height))
                .addComponent(new CollisionComponent(false, CollisionComponent.shape.RECTANGLE))
                .addComponent(new PositionComponent(x, y))
                .addComponent(new InventoryDisplayComponent());
    }

}
