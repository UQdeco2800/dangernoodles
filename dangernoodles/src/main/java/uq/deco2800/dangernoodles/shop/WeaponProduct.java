package uq.deco2800.dangernoodles.shop;

import uq.deco2800.dangernoodles.weapons.WeaponDefinition;

/**
 * Created by minhnguyen on 15/09/2016.
 * </p>
 *
 * This class will be used in ShopComponent to represent the products that players can purchase in the shop. Every
 * instance of WeaponProduct should be created from a distinct WeaponDefinition from WeaponDefinitionList.
 */
public class WeaponProduct {
    private WeaponDefinition weapon;
    private String imageLocation;
    private int stock;
    private int cost;

    /**
     * Constructor for WeaponProduct. Creates a single WeaponProduct that will be stored in a 'list' in ShopComponent.
     * Each WeaponProduct represents a weapon from WeaponDefinitionList that noodles may purchase and utilize.
     *
     * @param weapon
     *            a weapon definition representing the weapon that the product is
     * @param imageLocation
     *            a string representing the image path of the displayed product
     * @param stock
     *            an integer representing the remaining stock of the product
     * @param cost
     *            an integer representing the mana cost of purchasing the product
     * @return a weapon product
     */
    public WeaponProduct(WeaponDefinition weapon, String imageLocation, int stock, int cost) {
        this.weapon = weapon;
        this.imageLocation = imageLocation;
        this.stock = stock;
        this.cost = cost;
    }

    /**
     * Return the weaponDefinition that was used to create the instance of Weapon Product
     *
     * @return the weapon definition of the product.
     */
    public WeaponDefinition getWeaponDefinition() {
        return this.weapon;
    }

    /**
     * Returns the image path of the Weapon Product.
     *
     * @return a string representing the image path of the weapon product.
     */
    public String getImageLocation() {
        return this.imageLocation;
    }

    /**
     * Returns a integer value that represents how many stock of this Weapon Product Remains.
     *
     * @return amount of stock for this Weapon Product.
     */
    public int getStock() {
        return this.stock;
    }

    /**
     * Sets the new value of stock available. This method is used to decrement the value of stock when a user
     * purchases their selected weapon from the shop during their turn.
     */
    public void setStock(int newStock) {
        this.stock = newStock;
    }

    /**
     * Return the cost for this product. Cost represents the value of mana that will be deducted if the player
     * chooses to purchase the Weapon Product.
     *
     * @return an integer representing the mana cost of purchasing the product
     */
    public int getCost() {
        return this.cost;
    }




}
