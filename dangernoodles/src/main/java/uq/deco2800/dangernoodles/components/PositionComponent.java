package uq.deco2800.dangernoodles.components;

import uq.deco2800.dangernoodles.ecs.Component;

public class PositionComponent extends Component {

    private double x;
    private double y;
    private double nextX;
    private double nextY;

    /**
     * Create a position component for the entity. A position has x and
     * y coordinates.
     * @param x the x coordinate of an entity
     * @param y the y coordinate of an entity
     */
    public PositionComponent(double x, double y) {
        this.x = x;
        this.y = y;
        this.nextY = y;
        this.nextX = x;
    }
    
    /**
     * Get the current x coordinate of an entity
     * @return a double, x, representing the current position of the 
     * entity along the X axis.
     */
    public double getX() {
    	return this.x;
    }
    
    /**
     * Get the current y coordinate of an entity
     * @return a double, y, representing the current position of the entity
     * along the Y axis.
     */
    public double getY() {
    	return this.y;
    }
    
    /**
     * Set the X coordinates of the entity.
     * @param x The new position of the entity along the X axis.
     */
    public void setX(double x) {
    	this.x = x;
    }
    
    /**
     * Set the Y coordinates of the entity
     * @param y The new position of the entity along the Y axis.
     */
    public void setY(double y) {
    	this.y = y;
    }

    public double getNextX() {
        return nextX;
    }

    public void setNextX(double nextX) {
        this.nextX = nextX;
    }

    public double getNextY() {
        return nextY;
    }

    public void setNextY(double nextY) {
        this.nextY = nextY;
    }

    public PositionComponent nextP() {
        return new PositionComponent(this.nextX, this.nextY);
    }

    public String toString() {
        return "Current X " + this.getX() + " Current Y " + this.getY() +
                " Next X " + this.getNextX() + " Next Y " + this.getNextY();
    }
}
