package uq.deco2800.dangernoodles.components.effects;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by Christopher Thorpe, but redone by khoi_truong.
 * <p>
 * This class is used to test out functionality of DamageEffectComponent.
 */
public class DamageEffectComponentTest {

    private DamageEffectComponent component;

    //Typical use
    @Test
    public void Test01() {
        component = new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 3, 20);
        assertEquals(3, component.getTurnDuration());
        assertEquals(EffectEnum.DAMAGE_BUFF, component.getName());
        assertEquals(20, component.getDamage(), 0);
        assertTrue(component.isBuff());
    }

    // Exception with invalid name when instantiate - null given
    @Test(expected = NullPointerException.class)
    public void Test02() {
        component = new DamageEffectComponent(null, 3, 20);
    }

    // Exception with invalid value when instantiate - negative amount
    @Test(expected = IllegalArgumentException.class)
    public void Test03_1() {
        component = new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 3, -20);
    }
    
 // Exception with invalid value when instantiate - positive amount on debuff
    @Test(expected = IllegalArgumentException.class)
    public void Test03_2() {
        component = new DamageEffectComponent(EffectEnum.DAMAGE_DEBUFF, 3, 20);
    }

    // Exception with invalid value when instantiate - negative duration
    @Test(expected = IllegalArgumentException.class)
    public void Test04() {
        component = new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, -3, 20);
    }

    // Testing setDamage method - working case
    @Test
    public void Test05() {
        component = new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 3, 40);
        component.setDamage(10);
        double currentDamage = component.getDamage();
        assertTrue(currentDamage == 10);
    }

    // Testing setDamage method - negative amount case
    @Test(expected = IllegalArgumentException.class)
    public void Test06_1() {
        component = new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 3, 40);
        component.setDamage(-3);
    }
    
    // Testing setDamage method - positive amount case on debuff
    @Test(expected = IllegalArgumentException.class)
    public void Test06_2() {
        component = new DamageEffectComponent(EffectEnum.DAMAGE_DEBUFF, 3, -40);
        component.setDamage(3);
    }

    // Testing setDuration method - working case
    @Test
    public void Test07() {
        component = new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 3, 40);
        component.setTurnDuration(5);
        int currentDuration = component.getTurnDuration();
        assertTrue(currentDuration == 5);
    }

    // Testing setTurnDuration method - negative amount case
    @Test(expected = IllegalArgumentException.class)
    public void Test08() {
        component = new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 3, 40);
        component.setTurnDuration(-3);
    }

    // Test toString method
    @Test
    public void Test09() {
        component = new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 3, 40);
        // Expected string to compare against
        String string = "Current effect gives damage of 40.0.";
        // Then test value
        assertEquals(string, component.toString());
    }

    // Test equals method - working case
    @Test
    public void Test10() {
        component = new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 3, 40);
        DamageEffectComponent another = new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 3, 40);
        // Then test the method
        assertTrue(component.equals(another));
    }

    // Test equals method - null argument case
    @Test(expected = NullPointerException.class)
    public void Test11() {
        component = new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 3, 40);
        DamageEffectComponent another = null;
        // Then test the method
        assertTrue(component.equals(another));
    }

    // Test equals method - wrong type of object
    @Test
    public void Test12() {
        component = new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 3, 40);
        ManaEffectComponent another = new ManaEffectComponent(EffectEnum.MANA_BUFF, 3, 40);
        // Then test the method
        assertFalse(component.equals(another));
    }

    // Test equal method - not equal case 1
    @Test
    public void Test13_1() {
        component = new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 3, 40);
        DamageEffectComponent another = new DamageEffectComponent(EffectEnum.DAMAGE_DEBUFF, 3, -40);
        // Then test the method
        assertFalse(component.equals(another));
    }
    
    // Test equal method - not equal case 2
    @Test
    public void Test13_2() {
        component = new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 3, 40);
        DamageEffectComponent another = new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 4, 40);
        // Then test the method
        assertFalse(component.equals(another));
    }

    // Test equal method - not equal case 3
    @Test
    public void Test13_3() {
        component = new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 3, 40);
        DamageEffectComponent another = new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 3, 50);
        // Then test the method
        assertFalse(component.equals(another));
    }

    // Test hash code method
    @Test
    public void Test14() {
        component = new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 3, 40);
        // Then test the method
        assertEquals(54760, component.hashCode());
    }
}
