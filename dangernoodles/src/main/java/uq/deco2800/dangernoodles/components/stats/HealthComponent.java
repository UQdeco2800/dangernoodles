package uq.deco2800.dangernoodles.components.stats;

import uq.deco2800.dangernoodles.ecs.Component;

/**
 * Created by Team Mighty Ducks on 8/29/2016.
 *
 * Health Component
 * Allows an entity to have health
 */
public class HealthComponent extends Component {
	// Max health cap for entities associated health.
	private final int maxHealth;
	// Current health of entities associated health.
	private int health;

	public HealthComponent(int maxHealth) {
		if (maxHealth <= 0)
			throw new IllegalArgumentException("Max health must be " +
					"greater than 0");
		this.maxHealth = maxHealth;
		this.health = maxHealth;
	}

	/**
	 * Sets Entity's health to a specified value. Health is capped at 0.
	 * @param health
	 * 	    Value to set Entity's health.
	 * @ensure
	 * 		this.health >= 0
	 */
	public void setHealth(int health) {
		this.health = health < 0 ? 0 : health;
	}

	/**
	 * Resets Entity's health to its specified maximum value.
	 */
	public void resetHealth() {
		this.health = this.maxHealth;
	}

	/**
	 * Gets Entity's health.
	 * @return
	 *      returns Entity's total health.
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Gets Entity's specified max health.
	 * @return
	 *      returns Entity's specified maximum health.
	 */
	public int getMaxHealth(){
		return maxHealth;
	}
}
