package uq.deco2800.dangernoodles.components.displays;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by Jason on 20/10/16.
 */
public class InventoryDisplayComponentTest {
    private InventoryDisplayComponent invComp = new InventoryDisplayComponent();


    @Test
    public void isDisplayingInventory() {
        assertEquals(invComp.isDisplayingInventory(), false);

    }

    @Test
    public void setDisplaying() {
        invComp.setDisplaying(true);
        assertEquals(invComp.isDisplayingInventory(), true);
        invComp.setDisplaying(false);
        assertEquals(invComp.isDisplayingInventory(), false);

    }

}