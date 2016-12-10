package uq.deco2800.dangernoodles.components;

import uq.deco2800.dangernoodles.ecs.Component;

public class MovementComponent extends Component {
    private double vx;
    private double vy;
    private double ax;
    private double ay;
    private boolean isMovingX;
    private boolean isMovingY;
    private boolean lastMovingLeft;
    private boolean movable;

    public MovementComponent(double vx, double vy, double ax, double ay) {
        this.vx = vx;
        this.vy = vy;
        this.ax = ax;
        this.ay = ay;
        this.isMovingX = Math.abs(this.vx) > 0.5;
        this.isMovingY = Math.abs(this.vy) > 0.5;
        this.lastMovingLeft = this.vx <= 0.0;
        this.movable = true;
    }
    
    public double getVX() {
    	return this.vx;
    }
    
    public double getVY() {
    	return this.vy;
    }
    
    public double getAX() {
    	return this.ax;
    }
    
    public double getAY() {
    	return this.ay;
    }
    
    /**
     * @return true if the entity has movement on the horizontal (x) axis.
     */
    public boolean isMovingX() {
        return this.isMovingX;
    }
    
    /**
     * @return true if the entity has movement on the vertical (y) axis
     * @author Paul Haley
     */
    public boolean isMovingY() {
    	return this.isMovingY;
    }
    
    /**
     * @return true if the noodle was last moving left .
     */
    public boolean getLastMovingLeft() {
        return this.lastMovingLeft;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public void setAx(double ax) {
        this.ax = ax;
    }

    public void setAy(double ay) {
        this.ay = ay;
    }

    public void setVelocity(double vx, double vy) {
    	this.vx = vx;
    	this.vy = vy;
    }
    
    public void setAcceleration(double ax, double ay) {
    	this.ax = ax;
    	this.ay = ay;
    }
    
    /**
     * Changes the isMovingX variable
     * @param moving is for if the entity is moving on the horizontal (x) axis
     */
    public void setIsMovingX(boolean moving) {
        this.isMovingX = moving;
    }
    
    /**
     * Changes the isMovingY variable
     * @param moving is for if the entity is moving on the vertical (y) axis
     * @author Paul Haley
     */
    public void setIsMovingY(boolean moving) {
    	this.isMovingY = moving;
    }
    
    /**
     * Change the lastMovingLeft variable for the entities last direction
     * @param left variable to denote the last movement of the entity
     */
    public void setLastMovingLeft(boolean left) {
        this.lastMovingLeft = left;
    }

    /**
     * This will be false if noodle runs out of mana.
     */
    public boolean isMovable() {
        return movable;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

}
