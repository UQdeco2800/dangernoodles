package uq.deco2800.dangernoodles.AISystemTest;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uq.deco2800.dangernoodles.systems.AISystem;

public class AIAdjustAngleTests {

    /**
     * Test 4th quadrant: defaulting to -45 degrees, negative diffY
     */
    @Test
    public void testSetAngleForNonPowered1() {
        AISystem aiSystem = new AISystem();

        double angDeg = -30.89783123;
        double distance = 37;

        double diffX = distance * Math.cos(Math.toRadians(angDeg));
        double diffY = distance * Math.sin(Math.toRadians(angDeg));

        double expectedAngle = Math.toRadians(-45);

        boolean powered = false;

        double actualAngle = aiSystem.getAngle(diffX, diffY, powered);

        assertTrue(actualAngle == expectedAngle);
    }

    /**
     * Test 4th quadrant: defaulting to -45 degrees, positive diffY
     */
    @Test
    public void testSetAngleForNonPowered2() {
        AISystem aiSystem = new AISystem();

        double angDeg = 20.123123;
        double distance = 37;

        double diffX = distance * Math.cos(Math.toRadians(angDeg));
        double diffY = distance * Math.sin(Math.toRadians(angDeg));

        double expectedAngle = Math.toRadians(-45);

        boolean powered = false;

        double actualAngle = aiSystem.getAngle(diffX, diffY, powered);

        assertTrue(actualAngle == expectedAngle);
    }

    /**
     * Test 4th quadrant with bonus angle for non-powered weapons
     */
    @Test
    public void testSetAngleForNonPowered3() {
        AISystem aiSystem = new AISystem();

        double bonusAngleDeg = 5;
        double angDeg = -45 - 20.123123;
        double distance = 1237;

        double diffX = distance * Math.cos(Math.toRadians(angDeg));
        double diffY = distance * Math.sin(Math.toRadians(angDeg));

        double expectedAngle = Math.toRadians(angDeg - bonusAngleDeg);

        boolean powered = false;

        double actualAngle = aiSystem.getAngle(diffX, diffY, powered);

        assertTrue(Math.abs(actualAngle - expectedAngle) < 0.0001);
    }

    /**
     * Test 4th quadrant without bonus angle for non-powered weapons
     */
    @Test
    public void testSetAngleForNonPowered4() {
        AISystem aiSystem = new AISystem();

        double angDeg = -88;
        double distance = 1237;

        double diffX = distance * Math.cos(Math.toRadians(angDeg));
        double diffY = distance * Math.sin(Math.toRadians(angDeg));

        double expectedAngle = Math.toRadians(angDeg);

        boolean powered = false;

        double actualAngle = aiSystem.getAngle(diffX, diffY, powered);

        assertTrue(Math.abs(actualAngle - expectedAngle) < 0.0001);
    }

    /**
     * Test 3rd quadrant: defaulting to -135 degrees, negative diffY
     */
    @Test
    public void testSetAngleForNonPowered5() {
        AISystem aiSystem = new AISystem();

        double angDeg = -157.89783123;
        double distance = 8273;

        double diffX = distance * Math.cos(Math.toRadians(angDeg));
        double diffY = distance * Math.sin(Math.toRadians(angDeg));

        double expectedAngle = Math.toRadians(-135);

        boolean powered = false;

        double actualAngle = aiSystem.getAngle(diffX, diffY, powered);

        assertTrue(actualAngle == expectedAngle);
    }

    /**
     * Test 3rd quadrant: defaulting to -135 degrees, positive diffY
     */
    @Test
    public void testSetAngleForNonPowered6() {
        AISystem aiSystem = new AISystem();

        double angDeg = 127;
        double distance = 97;

        double diffX = distance * Math.cos(Math.toRadians(angDeg));
        double diffY = distance * Math.sin(Math.toRadians(angDeg));

        double expectedAngle = Math.toRadians(-135);

        boolean powered = false;

        double actualAngle = aiSystem.getAngle(diffX, diffY, powered);

        assertTrue(actualAngle == expectedAngle);
    }

    /**
     * Test 3rd quadrant with bonus angle for non-powered weapons
     */
    @Test
    public void testSetAngleForNonPowered7() {
        AISystem aiSystem = new AISystem();

        double bonusAngleDeg = 5;
        double angDeg = -123.786327;
        double distance = 277;

        double diffX = distance * Math.cos(Math.toRadians(angDeg));
        double diffY = distance * Math.sin(Math.toRadians(angDeg));

        double expectedAngle = Math.toRadians(angDeg + bonusAngleDeg);

        boolean powered = false;

        double actualAngle = aiSystem.getAngle(diffX, diffY, powered);

        assertTrue(Math.abs(actualAngle - expectedAngle) < 0.0001);
    }

    /**
     * Test 3rd quadrant without bonus angle for non-powered weapons
     */
    @Test
    public void testSetAngleForNonPowered8() {
        AISystem aiSystem = new AISystem();

        double angDeg = -93;
        double distance = 2237;

        double diffX = distance * Math.cos(Math.toRadians(angDeg));
        double diffY = distance * Math.sin(Math.toRadians(angDeg));

        double expectedAngle = Math.toRadians(angDeg);

        boolean powered = false;

        double actualAngle = aiSystem.getAngle(diffX, diffY, powered);

        assertTrue(Math.abs(actualAngle - expectedAngle) < 0.0001);
    }

    /**
     * Test 1st quadrant for non-powered weapons
     */
    @Test
    public void testSetAngleForPowered1() {
        AISystem aiSystem = new AISystem();

        double angDeg = 38.293321;
        double distance = 2237;

        double diffX = distance * Math.cos(Math.toRadians(angDeg));
        double diffY = distance * Math.sin(Math.toRadians(angDeg));

        double expectedAngle = Math.toRadians(angDeg);

        boolean powered = true;

        double actualAngle = aiSystem.getAngle(diffX, diffY, powered);

        assertTrue(Math.abs(actualAngle - expectedAngle) < 0.0001);
    }

    /**
     * Test 2nd quadrant for non-powered weapons
     */
    @Test
    public void testSetAngleForPowered2() {
        AISystem aiSystem = new AISystem();

        double angDeg = 138.293321;
        double distance = 2237;

        double diffX = distance * Math.cos(Math.toRadians(angDeg));
        double diffY = distance * Math.sin(Math.toRadians(angDeg));

        double expectedAngle = Math.toRadians(angDeg);

        boolean powered = true;

        double actualAngle = aiSystem.getAngle(diffX, diffY, powered);

        assertTrue(Math.abs(actualAngle - expectedAngle) < 0.0001);
    }

    /**
     * Test 3rd quadrant for non-powered weapons
     */
    @Test
    public void testSetAngleForPowered3() {
        AISystem aiSystem = new AISystem();

        double angDeg = -178.293321;
        double distance = 2237;

        double diffX = distance * Math.cos(Math.toRadians(angDeg));
        double diffY = distance * Math.sin(Math.toRadians(angDeg));

        double expectedAngle = Math.toRadians(angDeg);

        boolean powered = true;

        double actualAngle = aiSystem.getAngle(diffX, diffY, powered);

        assertTrue(Math.abs(actualAngle - expectedAngle) < 0.0001);
    }

    /**
     * Test 4th quadrant for non-powered weapons
     */
    @Test
    public void testSetAngleForPowered4() {
        AISystem aiSystem = new AISystem();

        double angDeg = -28.293321;
        double distance = 2237;

        double diffX = distance * Math.cos(Math.toRadians(angDeg));
        double diffY = distance * Math.sin(Math.toRadians(angDeg));

        double expectedAngle = Math.toRadians(angDeg);

        boolean powered = true;

        double actualAngle = aiSystem.getAngle(diffX, diffY, powered);

        assertTrue(Math.abs(actualAngle - expectedAngle) < 0.0001);
    }
}
