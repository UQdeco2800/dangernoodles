package uq.deco2800.dangernoodles.components;


import uq.deco2800.dangernoodles.ecs.Component;
import uq.deco2800.dangernoodles.shop.WeaponProduct;
import uq.deco2800.dangernoodles.weapons.WeaponDefinition;
import uq.deco2800.dangernoodles.weapons.WeaponDefinitionList;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by Park on 10/13/2016.
 *
 * This class is used to store information from weapon definition list.
 */
public class InventoryComponent  extends Component {

    private ArrayList<WeaponProduct> weaponProductList;

    private int key;
    private static final int MAX_SLOTS = 3;


    public InventoryComponent() {

        this.weaponProductList = new ArrayList<WeaponProduct>();
        this.key = 0;

        WeaponDefinitionList weapons = new WeaponDefinitionList();
        
        int i = 0;
        for (WeaponDefinition weapon : weapons.getInventory()) {
        	WeaponProduct prod = new WeaponProduct(weapon, weapon.getImages(), 3, 3);
        	weaponProductList.add(i, prod);
        	i++;
        }

    }

    /**
     *
     * This method returns a list of weapon sprites.
     *
     * @return weaponProductList
     */
    public List<WeaponProduct> getWeaponProductList() { return weaponProductList; }

    /**
     *
     * This method gets a key.
     *
     * @return this.key
     */
    public Integer getKey() {
        return this.key;
    }

    /**
     *
     * This method add a weapon sprie to a list.
     */
    public void buyWeapon(WeaponProduct wp) {
        weaponProductList.add(wp);
    }


    /**
     *
     * This method removes a weapon sprite from a list.
     */
    public void sellWeapon() {
        weaponProductList.remove(weaponProductList.size()-1);
    }


    /**
     *
     * This method rests all the weapons and gets a default weapon which is a grenade.
     */
    public void resetWeapon(){
        weaponProductList.clear();
        WeaponDefinitionList weapons = new WeaponDefinitionList();
        int i = 0;
        for (WeaponDefinition weapon : weapons.getInventory()) {
        	WeaponProduct prod = new WeaponProduct(weapon, weapon.getImages(), 3, 3);
        	weaponProductList.add(i, prod);
        	i++;
        }
    }

}


