package uq.deco2800.dangernoodles.components;

import uq.deco2800.dangernoodles.ecs.Component;

/**
 * A component which gives an entity mass. Mass is used to calculate the
 * effects of gravity and collisions on this entity.
 */
public class MassComponent extends Component{
	private double mass;
	
	/**
	 * A componenet representing the mass of the entity within the world.
	 * @param mass A double, mass.
	 */
	public MassComponent(double mass) {
		this.mass = mass;
	}
	
	/**
	 * Return the mass of the entity.
	 * @return a double, mass.
	 */
	public double getMass() {
		return this.mass;
	}
	
	/**
	 * Set a new mass for the entity.
	 * @param newMass the new mass for the entity.
	 */
	public void setMass(double newMass) {
		this.mass = newMass;
	}
}
