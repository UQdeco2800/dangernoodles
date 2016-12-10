package uq.deco2800.dangernoodles.prefabs;

import static org.junit.Assert.*;
import org.junit.Test;
import uq.deco2800.dangernoodles.components.displays.InventoryDisplayComponent;

import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;


/**
 * Created by Jason on 23/10/16.
 */
public class SettingInventoryTest {


    @Test
    public void createInventory() {
        SettingInventory settingInventory = new SettingInventory();
        World testworld = new World(0, 0);

        Entity Inventory = settingInventory.createInventory(testworld, 0, 0, 150, 150);

        assertEquals(testworld.getComponents(InventoryDisplayComponent.class).size(), 1);

        testworld.destroyEntity(Inventory);

        assertEquals(testworld.getComponents(InventoryDisplayComponent.class).size(), 0);

    }

}