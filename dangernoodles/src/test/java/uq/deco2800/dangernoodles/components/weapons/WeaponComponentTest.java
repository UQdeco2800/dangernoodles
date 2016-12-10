package uq.deco2800.dangernoodles.components.weapons;

import static org.junit.Assert.*;

import org.junit.Test;

import uq.deco2800.dangernoodles.weapons.ProjectileDefinition;

public class WeaponComponentTest {
    
    @Test
    public void TestFiring() {
        WeaponComponent wc = new WeaponComponent(null, null);
        assertFalse("Shouldn't be firing", wc.isFiring());
        assertFalse("Shouldn't be fired", wc.isFired());
        assertTrue("Power should be 0", Math.abs(wc.getPower()) < 0.1);
        wc.setPower(100);
        wc.setFiring(true);
        assertTrue("Should be firing", wc.isFiring());
        assertFalse("Shouldn't be fired", wc.isFired());
        assertTrue("Power should be 0", Math.abs(wc.getPower()) < 0.1);
        wc.setFiring(false);
        assertFalse("Shouldn't be firing", wc.isFiring());
        assertTrue("Should be fired", wc.isFired());

    }
    
    @Test
    public void TestIncrement() {
        WeaponComponent wc = new WeaponComponent(null, null);
        assertTrue("Power should be 0", Math.abs(wc.getPower()) < 0.1);
        assertFalse("Shouldn't have overlapped", wc.incrementPower(10));
        assertTrue("Power should be 0", Math.abs(wc.getPower()) < 0.1);
        wc.setFiring(true);
        assertFalse("Shouldn't have overlapped", wc.incrementPower(35));
        assertTrue("Power should be 35", Math.abs(wc.getPower() - 35) < 0.1);
        assertFalse("Shouldn't have overlapped", wc.incrementPower(35));
        assertTrue("Power should be 70", Math.abs(wc.getPower() - 70) < 0.1);
        assertTrue("Should have overlapped", wc.incrementPower(35));
        assertTrue("Power should be 100", Math.abs(wc.getPower() - 100) < 0.1);
        // Negative
        assertFalse("Shouldn't have overlapped", wc.incrementPower(35));
        assertTrue("Power should be 65", Math.abs(wc.getPower() - 65) < 0.1);
        assertFalse("Shouldn't have overlapped", wc.incrementPower(35));
        assertTrue("Power should be 30", Math.abs(wc.getPower() - 30) < 0.1);
        assertTrue("Should have overlapped", wc.incrementPower(35));
        assertTrue("Power should be 0", Math.abs(wc.getPower()) < 0.1);
    }
    
    //Extra projectile component test to get coverage up
    @Test
    public void TestProjectileComponent() {
        ProjectileDefinition pdef = new ProjectileDefinition(1, "", "", 1, 1, 1, 1, false, 1000);
        ProjectileComponent pc = new ProjectileComponent(pdef);
        assertFalse("Shouldn't time out", pc.timesOut());
        assertTrue("Shouldn't have any life ticks", Math.abs(pc.ticksLeft()) < 0.1);
        assertFalse("Shouldn't be timed out", pc.timedOut());
    }
}
