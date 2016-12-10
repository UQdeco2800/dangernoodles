package uq.deco2800.dangernoodles.components.effects;

import org.junit.Test;
import uq.deco2800.dangernoodles.components.effects.ManaEffectComponent;
import uq.deco2800.dangernoodles.components.effects.EffectEnum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Christopher Thorpe, redone by khoi_truong.
 *         <p>
 *         This class is used to test out functionality of ManaEffectComponent.
 */
public class ManaEffectComponentTest {
    private ManaEffectComponent component;

    //Typical use
    @Test
    public void Test01() {
        component = new ManaEffectComponent(EffectEnum.MANA_BUFF, 3, 20);
        assertEquals(3, component.getTurnDuration());
        assertEquals(EffectEnum.MANA_BUFF, component.getName());
        assertEquals(20, component.getMana(), 0);
        assertTrue(component.isBuff());
    }

    // Exception with invalid name when instantiate - null given
    @Test(expected = NullPointerException.class)
    public void Test02() {
        component = new ManaEffectComponent(null, 3, 20);
    }

    // Exception with invalid value when instantiate - negative amount
    @Test(expected = IllegalArgumentException.class)
    public void Test03_1() {
        component = new ManaEffectComponent(EffectEnum.MANA_BUFF, 3, -20);
    }
    
    // Exception with invalid value when instantiate - negative amount
    @Test(expected = IllegalArgumentException.class)
    public void Test03_2() {
        component = new ManaEffectComponent(EffectEnum.MANA_DEBUFF, 3, 20);
    }

    // Exception with invalid value when instantiate - negative duration
    @Test(expected = IllegalArgumentException.class)
    public void Test04() {
        component = new ManaEffectComponent(EffectEnum.MANA_BUFF, -3, 20);
    }

    // Testing setMana method - working case
    @Test
    public void Test05() {
        component = new ManaEffectComponent(EffectEnum.MANA_BUFF, 3, 40);
        component.setMana(10);
        int currentMana = component.getMana();
        assertTrue(currentMana == 10);
    }

    // Testing setMana method - negative amount case
    @Test(expected = IllegalArgumentException.class)
    public void Test06_1() {
        component = new ManaEffectComponent(EffectEnum.MANA_BUFF, 3, 40);
        component.setMana(-3);
    }
    
    // Testing setMana method - negative amount case
    @Test(expected = IllegalArgumentException.class)
    public void Test06_2() {
        component = new ManaEffectComponent(EffectEnum.MANA_DEBUFF, 3, -40);
        component.setMana(3);
    }

    // Testing setDuration method - working case
    @Test
    public void Test07() {
        component = new ManaEffectComponent(EffectEnum.MANA_BUFF, 3, 40);
        component.setTurnDuration(5);
        int currentDuration = component.getTurnDuration();
        assertTrue(currentDuration == 5);
    }

    // Testing setTurnDuration method - negative amount case
    @Test(expected = IllegalArgumentException.class)
    public void Test08() {
        component = new ManaEffectComponent(EffectEnum.MANA_BUFF, 3, 40);
        component.setTurnDuration(-3);
    }

    // Test toString method
    @Test
    public void Test09() {
        component = new ManaEffectComponent(EffectEnum.MANA_BUFF, 3, 40);
        // Expected string to compare against
        String string = "Current effect gives mana of 40.";
        // Then test value
        assertEquals(string, component.toString());
    }

    // Test equals method - working case
    @Test
    public void Test10() {
        component = new ManaEffectComponent(EffectEnum.MANA_BUFF, 3, 40);
        ManaEffectComponent another = new ManaEffectComponent(EffectEnum.MANA_BUFF, 3, 40);
        // Then test the method
        assertTrue(component.equals(another));
    }

    // Test equals method - null argument case
    @Test(expected = NullPointerException.class)
    public void Test11() {
        component = new ManaEffectComponent(EffectEnum.MANA_BUFF, 3, 40);
        ManaEffectComponent another = null;
        // Then test the method
        assertTrue(component.equals(another));
    }

    // Test equals method - wrong type of object
    @Test
    public void Test12() {
        component = new ManaEffectComponent(EffectEnum.MANA_BUFF, 3, 40);
        SpeedEffectComponent another = new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 3, 40);
        // Then test the method
        assertFalse(component.equals(another));
    }

    // Test equal method - not equal case 1
    @Test
    public void Test13_1() {
        component = new ManaEffectComponent(EffectEnum.MANA_BUFF, 4, 40);
        SpeedEffectComponent another = new SpeedEffectComponent(EffectEnum.SPEED_DEBUFF, 4, -40);
        // Then test the method
        assertFalse(component.equals(another));
    }
    
    // Test equal method - not equal case 2
    @Test
    public void Test13_2() {
        component = new ManaEffectComponent(EffectEnum.MANA_BUFF, 3, 40);
        SpeedEffectComponent another = new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 4, 40);
        // Then test the method
        assertFalse(component.equals(another));
    }
    
    // Test equal method - not equal case 3
    @Test
    public void Test13_3() {
        component = new ManaEffectComponent(EffectEnum.MANA_BUFF, 4, 40);
        SpeedEffectComponent another = new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 4, 50);
        // Then test the method
        assertFalse(component.equals(another));
    }

    // Test hash code method
    @Test
    public void Test14() {
        component = new ManaEffectComponent(EffectEnum.MANA_BUFF, 3, 40);
        // Then test the method
        assertEquals(137640, component.hashCode());
    }
}
