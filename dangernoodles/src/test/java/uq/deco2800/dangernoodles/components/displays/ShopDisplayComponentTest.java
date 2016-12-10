package uq.deco2800.dangernoodles.components.displays;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by minhnguyen on 18/09/2016.
 */
public class ShopDisplayComponentTest {

    ShopDisplayComponent sdc = new ShopDisplayComponent();

    /**
     * Test initial state of shop display component
     */
    @Test
    public void testSetShowingDisplay() {
        assertFalse(sdc.isShowingDisplay());
    }

    /**
     * Test changing initial state from false to true
     */
    @Test
    public void testChangeShowingDisplay() {
        sdc.setShowDisplay(true);
        assertTrue(sdc.isShowingDisplay());
    }
}