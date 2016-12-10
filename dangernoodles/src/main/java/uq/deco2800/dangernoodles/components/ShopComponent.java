package uq.deco2800.dangernoodles.components;

import uq.deco2800.dangernoodles.ecs.Component;
import uq.deco2800.dangernoodles.shop.WeaponProduct;
import uq.deco2800.dangernoodles.weapons.WeaponDefinition;
import uq.deco2800.dangernoodles.weapons.WeaponDefinitionList;


import java.util.List;
import java.util.ArrayList;


/**
 * Created by minhnguyen on 4/09/2016.
 * <p>
 *
 * This class is used as a container for all the necessary data required for the shop entity to operate. The data
 * includes: a list of products, a list of weapon ID, an integer key, the maximum number of displayed items, and
 * the selected weapon.
 */
public class ShopComponent extends Component {

    // Private field to store information of available purchases, their stock and their cost
    private List<WeaponProduct> weaponProductList;
    // Private field that contains a list of integer ID of weapons that are displayed
    private List<Integer> productSlot;
    // Private field used to determine what product from the product list should be displayed in the panel
    private int key;
    // Private field used to determine how many products can be displayed at one time
    private static final int MAX_SLOTS = 4;
    // private field used to determine what product is selected for purchasing
    private WeaponProduct selectedWeapon;

    /**
     * Constructor for ShopComponent. The constructor initialises an empty list of WeaponProduct and populates
     * it with a single new instance of WeaponProducts for each instance of weaponDefinition from the
     * WeaponDefinitionList. Each product is given a stock and cost of 3.
     */
    public ShopComponent(){
        this.weaponProductList = new ArrayList<WeaponProduct>();
        this.productSlot = new ArrayList<Integer>(MAX_SLOTS);
        this.selectedWeapon = null;
        this.key = 0;

        // creates an individual product for every weapon type in weapon definition list.
        WeaponDefinitionList weapDefList = new WeaponDefinitionList();
        for(int i = 0; i < weapDefList.size(); i++) {
            WeaponDefinition weapon = weapDefList.getInventory().get(i);
            WeaponProduct product = new WeaponProduct(weapon, weapon.getImages(), 3, 3);
            weaponProductList.add(i, product);
        }
    }


    /**
     *
     * Returns a list of WeaponProducts. Each element in this list represents a Weapon Product that is created from
     * a distinct Weapon Definition from the WeaponDefinitionList, a stock and cost value. The WeaponProducts list
     * should always have the same size as the WeaponDefinitionList.
     *
     * @return a list of WeaponProduct representing weapon that the shop has available for players to buy.
     */
    public List<WeaponProduct> getWeaponProductList() { return weaponProductList; }

    /**
     * Returns a list of integers. The list represents a set of weapon ID that is used to identify weapons from
     * the weapon definition list. The image location of these identified weapons are to be displayed when the shop
     * panel is displaying. The weapon ID in the list are selected from the WeaponProductList based on the value of
     * private field 'key'. The productSlot list should always have a size of MAX_SLOT.
     *
     * @return a list of strings representing the image locations of the currently displayed products.
     */
    public List<Integer> getProductSlot() { return productSlot; }

    /**
     * Returns an integer value that is used to determine what WeaponProduct from the WeaponProductList should be
     * displayed in the shop panel at any given time. The key value should remain between
     * 0 and WeaponProductList.size() - MAX_SLOTS at all times.
     *
     * @return an integer representing the key value used to display a set of products.
     */
    public Integer getKey() { return this.key; }

    /**
     * Increments the key value by 1 if the key value is less than the value of MAX_SLOTS. The key should
     * only be incremented when a user clicks the 'down' arrow in the shop panel.
     */
    public void incrementKey() {
        int maxKey = getWeaponProductList().size() - MAX_SLOTS;
        if (getKey() < maxKey) {
            this.key += 1;
        }
    }

    /**
     * Decrements the key value by 1 if the key value is less than than 0. The key should
     * only be decremented when a user clicks the 'down' arrow in the shop panel.
     */
    public void decrementKey() {
        if(getKey() > 0) {
            this.key -= 1;
        }
    }

    /**
     * Resets the key value to 0 for new players. This method should be called on every new players turn or when
     * the shop panel is closed.
     */
    public void resetKey() {
        this.key = 0;
    }

    /**
     * Return an integer representing the number of products a shop can display at one time. This value should NOT be
     * changed without adjusting the ShopDisplaySystem to account for the increase/decrease in displayed products. This
     * value is pre-determined by the designer to limit screen space consumption and dimensions used for the panel.
     *
     * @return maximum number of images the shop panel can display at one time.
     */
    public int getMaxSlots() { return MAX_SLOTS; }


    /**
     * Returns the WeaponProduct that has been selected. The object should be an instance of a WeaponProduct from the
     * WeaponProductList or null (if no WeaponProduct was selected).
     *
     * @return a weapon product that has been selected.
     */
    public WeaponProduct getSelectedProduct() { return selectedWeapon; }

    /**
     * Sets the selected weapon to null for new players. This method should be called on every new players turn or when
     * the shop panel is closed.
     */
    public void setSelectedProductNull() { selectedWeapon = null; }

    /**
     * Takes an integer that represents a weapon ID. Looks through the shops inventory for a weapon product that
     * a weapon definition with the same id value. Sets the selectedWeapon variable to the product with this ID.
     *
     * @param slotWeaponID
     *          image location of selected WeaponProduct
     */
    public void setSelectedProduct(Integer slotWeaponID) {
        for(int i = 0; i < weaponProductList.size(); i++) {
            Integer weaponID = weaponProductList.get(i).getWeaponDefinition().getID();
            if(weaponID.equals(slotWeaponID)) {
                selectedWeapon = weaponProductList.get(i);
                break;
            }
        }
    }

}