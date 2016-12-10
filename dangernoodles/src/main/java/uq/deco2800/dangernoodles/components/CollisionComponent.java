package uq.deco2800.dangernoodles.components;

import uq.deco2800.dangernoodles.ecs.Component;
import uq.deco2800.dangernoodles.ecs.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * A component which allows an entity to collide with other entities. Must be
 * used in conjunction with a positionComponent and shapeComponent to work.
 * Non-Floor solid entities such as noodles should have a massComponent and
 * movementComponent too.
 */
public class CollisionComponent extends Component {
	private boolean collidable;
	private boolean solid;
	private shape type;

    private boolean frictionVertical;
    private boolean frictionHorizontal;
    private double verticalFrictionValue;
    private double horizontalFrictionValue;
    private boolean stepUp;
    private int reboundUp;
    private int reboundDown;
    private int reboundLeft;
    private int reboundRight;
    private boolean reboundCorner;
    private boolean reboundX;
    private boolean reboundY;

    private ArrayList<Entity> collisions;

    // This list is used to ignore collision between the owner of this
    // component and the entities within this list.
	private ArrayList<Entity> ignoredEntities;
	// list of all collisions that would have happened if they weren't ignored
	private ArrayList<Entity> ignoredCollisions;

    /**
	 * A boolean variable representing whether an entity is able to collide
	 * with other entities in the world or not.
	 */
	public CollisionComponent(boolean solid, shape type) {
		this.collidable = true;
		this.solid = solid;
		this.type = type;

        this.collisions = new ArrayList<>();
        this.ignoredCollisions = new ArrayList<>();
        this.ignoredEntities = new ArrayList<>();
	}

	public shape getType() {
		return type;
	}

	public void setType(shape type) {
		this.type = type;
	}

	public boolean isSolid() {
		return solid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

    public boolean isFrictionVertical() {
        return frictionVertical;
    }

    public void setFrictionVertical(boolean frictionVertical) {
        this.frictionVertical = frictionVertical;
    }

    public boolean isFrictionHorizontal() {
        return frictionHorizontal;
    }

    public void setFrictionHorizontal(boolean frictionHorizontal) {
        this.frictionHorizontal = frictionHorizontal;
    }

    public int getReboundUp() {
        return reboundUp;
    }

    public void setReboundUp(int reboundUp) {
        this.reboundUp = reboundUp;
    }

    public int getReboundDown() {
        return reboundDown;
    }

    public void setReboundDown(int reboundDown) {
        this.reboundDown = reboundDown;
    }

    public int getReboundLeft() {
        return reboundLeft;
    }

    public void setReboundLeft(int reboundLeft) {
        this.reboundLeft = reboundLeft;
    }

    public int getReboundRight() {
        return reboundRight;
    }

    public void setReboundRight(int reboundRight) {
        this.reboundRight = reboundRight;
    }

    public double getVerticalFrictionValue() {
        return verticalFrictionValue;
    }

    public void setVerticalFrictionValue(double verticalFrictionValue) {
        this.verticalFrictionValue = verticalFrictionValue;
    }

    public double getHorizontalFrictionValue() {
        return horizontalFrictionValue;
    }

    public void setHorizontalFrictionValue(double horizontalFrictionValue) {
        this.horizontalFrictionValue = horizontalFrictionValue;
    }

    public boolean isStepUp() {
        return stepUp;
    }

    public void setStepUp(boolean stepUp) {
        this.stepUp = stepUp;
    }

    public boolean isReboundCorner() {
        return reboundCorner;
    }

    public void setReboundCorner(boolean reboundCorner) {
        this.reboundCorner = reboundCorner;
    }

    public boolean isReboundX() {
        return reboundX;
    }

    public void setReboundX(boolean reboundX) {
        this.reboundX = reboundX;
    }

    public boolean isReboundY() {
        return reboundY;
    }

    public void setReboundY(boolean reboundY) {
        this.reboundY = reboundY;
    }

    public enum shape {
		CIRCLE, RECTANGLE, POLYGON
	}

	/**
	 * Switch Collision On
	 */
	public void collisionOn() {
		this.collidable = true;
	}
	
	/**
	 * Switch Collision Off
	 */
	public void collisionOff() {
		this.collidable = false;
	}
	
	/**
	 * Return the collidable status of this entity.
	 * @return a boolean, true representing that an entity is collidable
	 * within the game world.
	 */
	public  boolean isCollidable() {
		return this.collidable;
	}

	public void solidOn() {
		this.solid = true;
	}

	public void solidOff() {
		this.solid = false;
	}

    /**
     * Returns true if colliding with something not ignored
     * @return
     */
    public boolean isCollided() {

        boolean returnValue = false;

        for (Entity e : this.getCollisions()) {
            if (!this.getIgnoredEntities().contains(e)) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    public void addCollision(Entity e) {
        collisions.add(e);
    }

    public List<Entity> getCollisions() {
        return collisions;
    }

    public void resetCollisions() {

        collisions.clear();

        frictionHorizontal = false;
        frictionVertical = false;
        horizontalFrictionValue = 0;
        verticalFrictionValue = 0;
        stepUp = false;
        reboundUp = 0;
        reboundDown = 0;
        reboundLeft = 0;
        reboundRight = 0;
        reboundCorner = false;
        reboundX = false;
        reboundY = false;
    }
    
    public void addIgnoredCollision(Entity e) {
        ignoredCollisions.add(e);
    }

    public List<Entity> getIgnoredCollisions() {
        return ignoredCollisions;
    }

    public List<Entity> getIgnoredEntities() {
        return ignoredEntities;
    }

    public void addIgnored(Entity entity) {
        if (!ignoredEntities.contains(entity)) {
            ignoredEntities.add(entity);
        }
    }

    public void removeIgnored(Entity entity) {
        if (ignoredEntities.contains(entity)) {
            ignoredEntities.remove(entity);
        }
    }
}
