package uq.deco2800.dangernoodles.components.effects;

import org.junit.Test;

import uq.deco2800.dangernoodles.components.effects.SpeedEffectComponent;
import uq.deco2800.dangernoodles.components.effects.EffectEnum;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Christopher Thorpe, redone by khoi_truong.
 * <p>
 * This class is used to test out functionality of SpeedEffectComponent.
 */
public class SpeedEffectComponentTest {
	private SpeedEffectComponent component;

	//Typical use
	@Test
	public void Test01() {
		component = new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 3, 20);
		assertEquals(3, component.getTurnDuration());
		assertEquals(EffectEnum.SPEED_BUFF, component.getName());
		assertEquals(20, component.getSpeed(), 0);
		assertTrue(component.isBuff());
	}

	// Exception with invalid name when instantiate - null given
	@Test(expected = NullPointerException.class)
	public void Test02() {
		component = new SpeedEffectComponent(null, 3, 20);
	}

	// Exception with invalid value when instantiate - negative amount
	@Test(expected = IllegalArgumentException.class)
	public void Test03() {
		component = new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 3, -20);
	}

	// Exception with invalid value when instantiate - negative duration
	@Test(expected = IllegalArgumentException.class)
	public void Test04() {
		component = new SpeedEffectComponent(EffectEnum.SPEED_BUFF, -3, 20);
	}

	// Testing setSpeed method - working case
	@Test
	public void Test05() {
		component = new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 3, 40);
		component.setSpeed(10);
		double currentSpeed = component.getSpeed();
		assertTrue(currentSpeed == 10);
	}

	// Testing setSpeed method - negative amount case
	@Test(expected = IllegalArgumentException.class)
	public void Test06() {
		component = new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 3, 40);
		component.setSpeed(-3);
	}

	// Testing setDuration method - working case
	@Test
	public void Test07() {
		component = new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 3, 40);
		component.setTurnDuration(5);
		int currentDuration = component.getTurnDuration();
		assertTrue(currentDuration == 5);
	}

	// Testing setTurnDuration method - negative amount case
	@Test(expected = IllegalArgumentException.class)
	public void Test08() {
		component = new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 3, 40);
		component.setTurnDuration(-3);
	}

	// Test toString method
	@Test
	public void Test09() {
		component = new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 3, 40);
		// Expected string to compare against
		String string = "Current effect gives speed of 40.0.";
		// Then test value
		assertEquals(string, component.toString());
	}

	// Test equals method - working case
	@Test
	public void Test10() {
		component = new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 3, 40);
		SpeedEffectComponent another = new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 3, 40);
		// Then test the method
		assertTrue(component.equals(another));
	}

	// Test equals method - null argument case
	@Test(expected = NullPointerException.class)
	public void Test11() {
		component = new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 3, 40);
		SpeedEffectComponent another = null;
		// Then test the method
		assertTrue(component.equals(another));
	}

	// Test equals method - wrong type of object
	@Test
	public void Test12() {
		component = new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 3, 40);
		ManaEffectComponent another = new ManaEffectComponent(EffectEnum.MANA_BUFF, 3, 40);
		// Then test the method
		assertFalse(component.equals(another));
	}

	// Test equal method - not equal case
	@Test
	public void Test13() {
		component = new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 3, 40);
		SpeedEffectComponent another = new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 4, 50);
		// Then test the method
		assertFalse(component.equals(another));
	}

	// Test hash code method
	@Test
	public void Test14() {
		component = new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 3, 40);
		// Then test the method
		assertEquals(54760, component.hashCode());
	}
}
