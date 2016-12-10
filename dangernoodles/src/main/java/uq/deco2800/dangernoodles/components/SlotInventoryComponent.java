package uq.deco2800.dangernoodles.components;

import uq.deco2800.dangernoodles.ecs.Component;

/**
 * Created by Park on 10/20/2016.
 *
 * This class is used to store slot location for the inventory.
 */
public class SlotInventoryComponent extends Component {
	
	private static final int[][] slotPositions = {
			{ 430, 30 },
			{ 485, 30 },
			{ 550, 30 },
			{ 610, 30 },
			{ 670, 30 },
			{ 430, 85 },
			{ 485, 85 },
			{ 550, 85 },
			{ 610, 85 },
			{ 670, 85 },
	};

    /**
     * This method is used to store X position of the inventory.
     *
     * @return 375
     */
    public Integer inventoryX() { return 375;}
    /**
     * This method is used to store Y position of the inventory.
     *
     * @return 10
     */
    public Integer inventoryY() { return 10;}
    /**
     * This method is used to store width of the inventory.
     *
     * @return 375
     */
    public Integer inventoryW() { return 375;}
    /**
     * This method is used to store height of the inventory in a integer format.
     *
     * @return 125
     */
    public Integer inventoryH() { return 125;}
    /**
     * This method is used to store a image location for the inventory.
     *
     * @return image location in a string format.
     */
    public String inventoryStr() { return "/Inventory/new inventory3.png";}
    /**
     * This method is used to store a image location for a default weapon.
     *
     * @return image location in a string format.
     */
    public String defaultWeaponStr() { return "/weapons/grenade_left.png";}
    /**
     * This method is used to store a image width for all the weapons.
     *
     * @return 30
     */
    public Integer imageWidth() { return 30;}
    /**
     * This method is used to store image height for all the weapons.
     *
     * @return 30
     */
    public Integer imageHeight() { return 30;}
    /**
     * This method returns X coordinate for slot1 which is used to display a default weapon.
     *
     * @return 430
     */
    public Integer getDefaultWeaponX(){
        return 430;
    }
    /**
     * This method returns X coordinate for slot1 which is used to display a default weapon.
     *
     * @return 30
     */
    public Integer getDefaultWeaponY(){
        return 30;
    }
    /**
     * Gives you the position of the inventory slot
     * 
     * @param slotNumber
     *            the slot of which you want the position
     * @return the PositionComponent 
     */
    public PositionComponent getSlotPosition(int slotNumber) {
    	int slot = slotNumber - 1; // Slot count starts at 1 (not 0)
    	return new PositionComponent(slotPositions[slot][0], slotPositions[slot][1]);
    }
    
    /**
     * The number of slots in the inventory
     * 
     * @return number of slots
     */
    public int getNumberOfSlots() {
    	return slotPositions.length;
    }
    
    /**
     * Gets the array containing the positions of the slots
     * 
     * @return the slot position array (array of array of ints)
     */
    public int[][] getSlotPositionArray() {
    	return slotPositions;
    }

}
