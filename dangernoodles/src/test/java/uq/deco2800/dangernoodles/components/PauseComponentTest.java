package uq.deco2800.dangernoodles.components;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by minhnguyen on 18/10/2016.
 */
public class PauseComponentTest {
    PauseComponent pauseComp = new PauseComponent();

    /**
     * Test case for testing initial state of pause
     */
    @Test
    public void isPaused() {
        assertFalse(pauseComp.isPaused());
    }

    /**
     * Test case for changing initial state of pause
     */
    @Test
    public void setPaused() {
        pauseComp.setPaused(true);
        assertTrue(pauseComp.isPaused());
        pauseComp.setPaused(false);
        assertFalse(pauseComp.isPaused());
    }

    @Test
    public void setJustPaused() throws Exception {

    }

}