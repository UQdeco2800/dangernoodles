package uq.deco2800.dangernoodles.components;

import org.junit.Test;
import uq.deco2800.dangernoodles.shop.WeaponProduct;
import uq.deco2800.dangernoodles.weapons.WeaponDefinition;
import uq.deco2800.dangernoodles.weapons.WeaponDefinitionList;

import static org.junit.Assert.*;


/**
 * Created by Jason on 10/22/2016.
 */
public class InventoryComponentTest {

    private InventoryComponent inventoryComponent = new InventoryComponent();
    private int weaponSize = new WeaponDefinitionList().getInventory().size();

    @Test
    public void getWeaponProductList() throws Exception {

        assertTrue(inventoryComponent.getWeaponProductList().size() == weaponSize);

    }

    @Test
    public void getKey() throws Exception {

        assertTrue(inventoryComponent.getKey() == 0);

    }

    @Test
    public void buyWeapon() throws Exception {

        WeaponDefinitionList weapon = new WeaponDefinitionList();
        WeaponDefinition wp1 = weapon.getInventory().get(0);
        WeaponProduct prod1 = new WeaponProduct(wp1, wp1.getImages(), 3, 3);

        inventoryComponent.buyWeapon(prod1);
        assertTrue(inventoryComponent.getWeaponProductList().size() == (weaponSize + 1));

    }

    @Test
    public void sellWeapon() throws Exception {

        WeaponDefinitionList weapon = new WeaponDefinitionList();
        WeaponDefinition wp1 = weapon.getInventory().get(0);
        WeaponProduct prod1 = new WeaponProduct(wp1, wp1.getImages(), 3, 3);

        inventoryComponent.sellWeapon();
        assertTrue(inventoryComponent.getWeaponProductList().size() == (weaponSize - 1));

    }

    @Test
    public void resetWeapon() throws Exception {
        inventoryComponent.resetWeapon();
        assertTrue(inventoryComponent.getWeaponProductList().size() == weaponSize);

    }


}