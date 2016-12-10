package uq.deco2800.dangernoodles.components;

import org.junit.*;

/**
 * Created by Park on 10/21/2016.
 */
public class SlotInventoryComponentTest {

    SlotInventoryComponent slotInven = new SlotInventoryComponent();
    String inventory = "/Inventory/new inventory3.png";
    String defaultWeapon = "/weapons/grenade_left.png";
    Integer defaultWeaponXTest = 430;
    Integer defaultWeaponYTest = 30;
    Integer invenX = 375;
    Integer invenY = 10;
    Integer invenW = 375;
    Integer invenH =125;
    Integer imageH =30;
    Integer imageW =30;


    @Test
    public void inventoryX(){
        Assert.assertEquals(invenX, slotInven.inventoryX());
    }
    @Test
    public void inventoryY(){
        Assert.assertEquals(invenY, slotInven.inventoryY());
    }
    @Test
    public void invenStrX(){
        Assert.assertEquals(inventory, slotInven.inventoryStr());
    }
    @Test
    public void defaultWeaponStr(){
        Assert.assertEquals(defaultWeapon, slotInven.defaultWeaponStr());
    }
    @Test
    public void imageH(){
        Assert.assertEquals(imageH, slotInven.imageHeight());
    }
    @Test
    public void defaultWeaponX(){Assert.assertEquals(defaultWeaponXTest, slotInven.getDefaultWeaponX());}
    @Test
    public void defaultWeaponY(){Assert.assertEquals(defaultWeaponYTest, slotInven.getDefaultWeaponY());}
    @Test
    public void imageW(){
        Assert.assertEquals(imageW, slotInven.imageWidth());
    }
    @Test
    public void inventoryW(){
        Assert.assertEquals(invenW, slotInven.inventoryW());
    }
    @Test
    public void inventoryH(){
        Assert.assertEquals(invenH, slotInven.inventoryH());
    }
    
    @Test
    public void testSlotLength() {
    	Assert.assertEquals(slotInven.getSlotPositionArray().length, 
    			slotInven.getNumberOfSlots());
    }
    
    @Test
    public void testGetSlotPosition() {
    	int[][] positions = slotInven.getSlotPositionArray();
    	int i = 1;
    	for (int[] position : positions) {
    		Assert.assertTrue(position[0] == slotInven.getSlotPosition(i).getX());
    		Assert.assertTrue(position[1] == slotInven.getSlotPosition(i).getY());
    		i++;
    	}
    }


}