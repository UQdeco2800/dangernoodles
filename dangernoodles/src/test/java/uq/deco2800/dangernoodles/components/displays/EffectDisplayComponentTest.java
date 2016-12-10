package uq.deco2800.dangernoodles.components.displays;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by minhnguyen on 18/09/2016.
 */
public class EffectDisplayComponentTest {

    EffectDisplayComponent edc = new EffectDisplayComponent();

    /**
     * Test initial state of isShowEffects
     *
     */
    @Test
    public void isShowEffects(){
        assertTrue(edc.isShowEffects());

    }

    /**
     * Test changing showEffects from true to false
     */
    @Test
    public void setShowEffects() {
        edc.setShowEffects(false);
        assertFalse(edc.isShowEffects());

    }

    /**
     * Test initial state of isShowTooltip
     */
    @Test
    public void isShowTooltip() {
        assertFalse(edc.isShowTooltip());
    }

    /**
     * Test changing showTooltip from false to true
     */
    @Test
    public void setShowTooltip() {
        edc.setShowTooltip(true);
        assertTrue(edc.isShowEffects());

    }

    /**
     * Test toogling showToolTip
     */
    @Test
    public void toogleShowTooltip() throws Exception {
        assertFalse(edc.isShowTooltip());
        edc.toogleShowTooltip();
        assertTrue(edc.isShowTooltip());
        edc.toogleShowTooltip();
        assertFalse(edc.isShowTooltip());

    }

}