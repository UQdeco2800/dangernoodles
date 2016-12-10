package uq.deco2800.dangernoodles.components.weather;

import uq.deco2800.dangernoodles.ecs.Component;

public class LightningComponent extends Component{
	// amount of damage lightning does
	private double damage;
	
	// true if lightning will do damage
	private boolean damagable = false;
	
	// how long the lightning strike lasts for
	private double duration = 0;
	
	/**
     * Default constructor of LightningComponent
     */
	public LightningComponent (double damage) {
		this.setDamage(damage);
		return;
	}
	
	/**
     * @return damage
     */
	public double getDamage () {
		return this.damage;
	}

	/**
     * @return damagable
     */
	public boolean getIfDamagable () {
		return this.damagable;
	}
	
	/**
     * @param damage - amount of damage the strike will do
     *
     */
	public void setDamage(double damage) {
		if (damage <= 0.05) {
			this.damage = 0;
			this.damagable = false;
		} else {
			this.damage = damage;
			this.damagable = true;
		}
	}
	
	/**
     * @return duration of strike
     *
     */
	public double getDuration () {
		return this.duration;
	}
	
	/**
     * Increments duration
     *
     */
	public void IncrementDuration () {
		this.duration += 1;
	}
}