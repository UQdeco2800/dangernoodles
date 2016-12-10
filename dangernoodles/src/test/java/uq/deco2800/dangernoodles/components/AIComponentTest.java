package uq.deco2800.dangernoodles.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AIComponentTest {

    private AIComponent testAIComponent = new AIComponent(null, null);

    /**
     * This tests the setTarget() method of AIComponent
     */
    @Test
    public void testSetTarget() {
        PositionComponent testTargetPosition = new PositionComponent(300, 500);
        testAIComponent.setTarget(testTargetPosition);
        assertEquals(testAIComponent.getTargetPosition(), testTargetPosition);
    }

    /**
     * This tests the clearTarget() method of AIComponent
     */
    @Test
    public void testClearTarget() {
        PositionComponent testTargetPosition = new PositionComponent(150, 300);
        testAIComponent.setTarget(testTargetPosition);
        testAIComponent.clearTarget();
        assertTrue(testAIComponent.getTargetPosition() == null);
    }
    @Test
    public void testSetWeaponPosition(){
    	PositionComponent newWeaponPosition = new PositionComponent(20,50);
    	assertNull(testAIComponent.getWeaponPosition());
    	testAIComponent.setWeaponPosition(newWeaponPosition);
    	assertEquals(testAIComponent.getWeaponPosition(), newWeaponPosition);
    }
	@Test
    public void testAdditionalGetters(){
    	testAIComponent.setDifficulty("top");
    	assertTrue(testAIComponent.getRandomness()== 0.15);
    	assertFalse(testAIComponent.hasFired());
    	assertEquals(testAIComponent.getDifficulty(), "top");
    	testAIComponent.setFired(true);
    	assertTrue(testAIComponent.hasFired());
    }
}
