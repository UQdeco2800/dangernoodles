package uq.deco2800.dangernoodles.components;

import uq.deco2800.dangernoodles.ecs.Component;

/**
 * A component which gives friction to an entity. Must be used in conjunction
 * with a CollisionComponent and a PositionComponent to work
 */
public class FrictionComponent extends Component{
	private double frictionConstant;
	private boolean isFloor;
	
	/**
	 * A constant representing the amount of friction this component creates
	 * against another entity;
	 * @param frictionConstant
	 */
	public FrictionComponent(double frictionConstant, boolean isFloor) {
		this.frictionConstant = frictionConstant;
		this.isFloor = isFloor;
	}
	
	/**
	 * Get the current FrictionConstant.
	 */
	public double getFriction() {
		return this.frictionConstant;
	}
	
	/**
	 * Set the new friction constant for this entity.
	 * @param newFriction The new value for the entity's friction.
	 */
	public void setFriction(double newFriction) {
		this.frictionConstant = newFriction;
	}


	public boolean isFloor() {
		return isFloor;
	}

	public void setFloor(boolean floor) {
		isFloor = floor;
	}
}
