package uq.deco2800.dangernoodles.components;

import uq.deco2800.dangernoodles.ecs.Component;

/**
 * A component which allows an object to be effected by gravity. Can be
 * toggled on and off. Must be used in conjunction with a movementComponent
 * and a massComponent.
 */
public class GravityComponent extends Component {
	private boolean gravity = false;
	
	/**
	 * A component which allows the world to inflict gravity on an entity.
	 * @param gravitySetting, a boolean, true if gravity should be switched
	 * on.
	 */
	public GravityComponent(boolean gravitySetting) {
		this.gravity = gravitySetting;
	}
	
	/**
	 * Switch the current gravity setting of an entity.
	 * @param gravitySetting, true for gravity on.
	 */
	public void setGravity(boolean gravitySetting) {
		this.gravity = gravitySetting;
	}
	
	/**
	 * return the current configuration of an entity's gravity.
	 * @return Component's gravity value
	 */
	public boolean getGravity() {
		return this.gravity;
	}
}
