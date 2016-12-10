package uq.deco2800.dangernoodles.components;

import org.junit.Test;
import uq.deco2800.dangernoodles.shop.WeaponProduct;
import uq.deco2800.dangernoodles.weapons.WeaponDefinition;
import uq.deco2800.dangernoodles.weapons.WeaponDefinitionList;

import static org.junit.Assert.*;

/**
 * Created by minhnguyen on 18/09/2016.
 */
public class ShopComponentTest {
    ShopComponent shopComp = new ShopComponent();

    /**
     * Test initial size of product list - Implement after hard coded product is removed
     * and products are taken from WeaponDefinitionList
     */
    @Test
    public void getWeaponProductList() {
    }

    /**
     * Get the list of slots the shop has - Implement after the traversing functionality is complete
     * and 8 slot is being used and not two
     */
    @Test
    public void getProductSlot() {
    }

    /**
     * Test initial value of key
     */
    @Test
    public void getKey() {
        int initialKeyValue = 0;
        int shopKeyValue = shopComp.getKey();
        assertEquals(shopKeyValue, initialKeyValue);
    }

    /**
     * Test incrementing key value
     */
    @Test
    public void incrementKey() {
        for(int i = 0; i < shopComp.getWeaponProductList().size(); i++) {
            shopComp.incrementKey();
        }
        int finalKeyValue = shopComp.getWeaponProductList().size() - shopComp.getMaxSlots();
        int shopKeyValue = shopComp.getKey();
        assertTrue(shopKeyValue <= finalKeyValue);
    }

    /**
     * Test decrementing key value
     */
    @Test
    public void decrementKey() {
        // Decrement key
        int finalKeyValue = 0;
        shopComp.incrementKey();
        shopComp.decrementKey();
        int shopKeyValue = shopComp.getKey();
        assertEquals(shopKeyValue, finalKeyValue);

        // Decrement no effect
        shopComp.decrementKey();
        assertEquals(shopKeyValue, finalKeyValue);
    }

    /**
     * Test reseting key value
     */
    @Test
    public void resetKey() {
        int finalKeyValue = 0;
        shopComp.incrementKey();
        shopComp.incrementKey();
        shopComp.resetKey();
        int shopKeyValue = shopComp.getKey();
        assertEquals(shopKeyValue, finalKeyValue);
    }


    /**
     * Test getting the max slot number
     */
    @Test
    public void getMaxSlots() {
        int maxSlot = 4;
        int shopMaxSlot = shopComp.getMaxSlots();
        assertEquals(maxSlot, shopMaxSlot);

    }

    /**
     * Test for initial state of selected product and setting selected product to null.
     */
    @Test
    public void setSelectedProductNull() {
        // Testing initial state of selected product
       assertEquals(null, shopComp.getSelectedProduct());

        // fabricate weapon product
        Integer weaponID = shopComp.getWeaponProductList().get(0).getWeaponDefinition().getID();
        WeaponProduct prefabProduct = shopComp.getWeaponProductList().get(0);

        // Selected a product
        shopComp.setSelectedProduct(0);
        assertEquals(prefabProduct, shopComp.getSelectedProduct());
        
        // reset selected product
        shopComp.setSelectedProductNull();
        assertEquals(null, shopComp.getSelectedProduct());

    }

    /**
     * Testing that all products are taken from WeaponDefinitionList
     */
    @Test
    public void weaponDefinintionListIntegratoin() {
        int shopListSize = shopComp.getWeaponProductList().size();
        WeaponDefinitionList weapDefList = new WeaponDefinitionList();
        int weapDefListSize = weapDefList.size();
        assertEquals(shopListSize, weapDefListSize);
    }


}