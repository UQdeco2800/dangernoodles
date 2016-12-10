package uq.deco2800.dangernoodles.components.effects;

import org.junit.Test;
import uq.deco2800.dangernoodles.components.effects.ShieldEffectComponent;
import uq.deco2800.dangernoodles.components.effects.EffectEnum;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Christopher Thorpe
 * <p>
 * This class is used to test out functionality of ShieldEffectComponent.
 */
public class ShieldEffectComponentTest {
	private ShieldEffectComponent component;

	//Typical use
	@Test
	public void Test01() {
		component = new ShieldEffectComponent(EffectEnum.SHIELD_BUFF, 3, 20);
		assertEquals(3, component.getTurnDuration());
		assertEquals(EffectEnum.SHIELD_BUFF, component.getName());
		assertEquals(20, component.getShield(), 0);
		assertTrue(component.isBuff());
	}

	// Exception with invalid name when instantiate - null given
	@Test(expected = NullPointerException.class)
	public void Test02() {
		component = new ShieldEffectComponent(null, 3, 20);
	}

	// Exception with invalid value when instantiate - negative amount
	@Test(expected = IllegalArgumentException.class)
	public void Test03() {
		component = new ShieldEffectComponent(EffectEnum.SHIELD_BUFF, 3, -20);
	}

	// Exception with invalid value when instantiate - negative duration
	@Test(expected = IllegalArgumentException.class)
	public void Test04() {
		component = new ShieldEffectComponent(EffectEnum.SHIELD_BUFF, -3, 20);
	}

	// Testing setShield method - working case
	@Test
	public void Test05() {
		component = new ShieldEffectComponent(EffectEnum.SHIELD_BUFF, 3, 40);
		component.setShield(10);
		double currentShield = component.getShield();
		assertTrue(currentShield == 10);
	}

	// Testing setShield method - negative amount case
	@Test(expected = IllegalArgumentException.class)
	public void Test06() {
		component = new ShieldEffectComponent(EffectEnum.SHIELD_BUFF, 3, 40);
		component.setShield(-3);
	}

	// Testing setDuration method - working case
	@Test
	public void Test07() {
		component = new ShieldEffectComponent(EffectEnum.SHIELD_BUFF, 3, 40);
		component.setTurnDuration(5);
		int currentDuration = component.getTurnDuration();
		assertTrue(currentDuration == 5);
	}

	// Testing setTurnDuration method - negative amount case
	@Test(expected = IllegalArgumentException.class)
	public void Test08() {
		component = new ShieldEffectComponent(EffectEnum.SHIELD_BUFF, 3, 40);
		component.setTurnDuration(-3);
	}

	// Test toString method
	@Test
	public void Test09() {
		component = new ShieldEffectComponent(EffectEnum.SHIELD_BUFF, 3, 40);
		// Expected string to compare against
		String string = "Current effect gives shield of 40.0.";
		// Then test value
		assertEquals(string, component.toString());
	}

	// Test equals method - working case
	@Test
	public void Test10() {
		component = new ShieldEffectComponent(EffectEnum.SHIELD_BUFF, 3, 40);
		ShieldEffectComponent another = new ShieldEffectComponent(EffectEnum.SHIELD_BUFF, 3, 40);
		// Then test the method
		assertTrue(component.equals(another));
	}

	// Test equals method - null argument case
	@Test(expected = NullPointerException.class)
	public void Test11() {
		component = new ShieldEffectComponent(EffectEnum.SHIELD_BUFF, 3, 40);
		ShieldEffectComponent another = null;
		// Then test the method
		assertTrue(component.equals(another));
	}

	// Test equals method - wrong type of object
	@Test
	public void Test12() {
		component = new ShieldEffectComponent(EffectEnum.SHIELD_BUFF, 3, 40);
		ManaEffectComponent another = new ManaEffectComponent(EffectEnum.MANA_BUFF, 3, 40);
		// Then test the method
		assertFalse(component.equals(another));
	}

	// Test equal method - not equal case
	@Test
	public void Test13() {
		component = new ShieldEffectComponent(EffectEnum.SHIELD_BUFF, 3, 40);
		ShieldEffectComponent another = new ShieldEffectComponent(EffectEnum.SHIELD_BUFF, 4, 50);
		// Then test the method
		assertFalse(component.equals(another));
	}

	// Test hash code method
	@Test
	public void Test14() {
		component = new ShieldEffectComponent(EffectEnum.SHIELD_BUFF, 3, 40);
		// Then test the method
		assertEquals(54760, component.hashCode());
	}
}
