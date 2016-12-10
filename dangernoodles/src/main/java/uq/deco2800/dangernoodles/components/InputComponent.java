package uq.deco2800.dangernoodles.components;

import uq.deco2800.dangernoodles.ecs.Component;

public class InputComponent extends Component {
    private double x = 0.0;
    private double lastX = 0.0;
    private double y = 0.0;

    // Private fields to store information about mouse click
    private double mouseX = 0.0;
    private double mouseY = 0.0;
    private boolean clicked = false;

    public double getX() {
        return x;
    }

    /**
     * Sets x to the passed in value. If the value given is non-zero, it will also update the lastX variable to the 
     * one given.
     * @param x Sets x to the passed value.
     */
    public void setX(double x) {
    	this.lastX = (Math.abs(x) < 0.001) ? this.lastX : x;
        this.x = x;
    }
    
    /**
     * @return The last non-negative x value
     */
    public double getLastX() {
    	return this.lastX;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getMouseX() {
        return mouseX;
    }

    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
}
