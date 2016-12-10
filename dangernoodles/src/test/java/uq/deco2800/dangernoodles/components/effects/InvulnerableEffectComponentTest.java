package uq.deco2800.dangernoodles.components.effects;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Christopher Thorpe, redone by khoi_truong.
 * <p>
 * This class is used to test out functionality of DamageEffectComponent.
 */
public class InvulnerableEffectComponentTest {

    private InvulnerableEffectComponent component;

    //Typical use
    @Test
    public void Test01() {
        component = new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, 3, true);
        assertEquals(3, component.turnDuration);
        assertEquals(EffectEnum.INVULNERABLE_BUFF, component.getName());
        assertTrue(component.isInvulnerable());
        assertTrue(component.isBuff());
    }

    // Exception with invalid name when instantiate - null given
    @Test(expected = NullPointerException.class)
    public void Test02() {
        component = new InvulnerableEffectComponent(null, 3, true);
    }

    // Exception with invalid name when instantiate - negative duration
    @Test(expected = IllegalArgumentException.class)
    public void Test03() {
        component = new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, -3, true);
    }

    // Testing setInvulnerable method - working case
    @Test
    public void Test04() {
        component = new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, 3, true);
        component.setInvulnerable(false);
        assertFalse(component.isInvulnerable());
    }

    // Test toString method of INVULNERABLE_BUFF
    @Test
    public void Test05_1() {
        component = new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, 3, true);
        // Expected string to compare against
        String string = "Current effect gives invulnerable.";
        // Then test value
        assertEquals(string, component.toString());
    }
    
    // Test toString method of INVULNERABLE_DEBUFF
    @Test
    public void Test05_2() {
        component = new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_DEBUFF, 3, false);
        // Expected string to compare against
        String string = "Current effect gives vulnerable.";
        // Then test value
        assertEquals(string, component.toString());
    }
    

    // Test equals method - working case
    @Test
    public void Test06() {
        component = new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, 3, true);
        InvulnerableEffectComponent another = new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, 3, true);
        // Then test the method
        assertTrue(component.equals(another));
    }

    // Test equals method - null argument case
    @Test(expected = NullPointerException.class)
    public void Test07() {
        component = new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, 3, true);
        InvulnerableEffectComponent another = null;
        // Then test the method
        assertTrue(component.equals(another));
    }

    // Test equals method - wrong type of object
    @Test
    public void Test08() {
        component = new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, 3, true);
        ManaEffectComponent another = new ManaEffectComponent(EffectEnum.MANA_BUFF, 3, 40);
        // Then test the method
        assertFalse(component.equals(another));
    }

    // Test equal method - not equal case
    @Test
    public void Test09_1() {
        component = new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, 3, true);
        InvulnerableEffectComponent another = new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_DEBUFF, 3, true);
        // Then test the method
        assertFalse(component.equals(another));
    }
    
    // Test equal method - not equal case
    @Test
    public void Test09_2() {
        component = new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, 4, true);
        InvulnerableEffectComponent another = new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, 3, true);
        // Then test the method
        assertFalse(component.equals(another));
    }

    // Test equal method - not equal case
    @Test
    public void Test09_3() {
        component = new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, 4, true);
        InvulnerableEffectComponent another = new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, 4, false);
        // Then test the method
        assertFalse(component.equals(another));
    }

    // Test hash code method
    @Test
    public void Test10_1() {
        component = new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, 3, true);
        // Then test the method
        assertEquals(47915, component.hashCode());
    }
    
}
