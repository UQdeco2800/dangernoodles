package uq.deco2800.dangernoodles.components;

import uq.deco2800.dangernoodles.ecs.Component;

public class SpriteComponent extends Component {
    private int width;
    private int height;
    private String image;
    private double rotation;
    private double pivotX;
    private double pivotY;
    private boolean flippped;
    private boolean render;

    public SpriteComponent(int width, int height, String image) {
        this.width = width;
        this.height = height;
        this.image = image;
        
        //default values for rotation and pivot position
        this.rotation = 0.0;
        this.pivotX = (double)this.width / 2;
        this.pivotY = (double)this.height / 2;
        this.flippped = false;
        this.render = true;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
    /**
     * Gets the rotation that should be applied to the sprite when rendered
     * 
     * @return the rotation in degrees
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * Sets the angle for the sprite to be rotated
     * 
     * @param rotation
     *            the rotation to be applied to the sprite
     */
    public void setRotation(double rotation) {
        //needs to be stored negated because of how the JavaFX rotations work
        this.rotation = rotation;
    }

    /**
     * Gets the x-offset of the pivot for the rotation/vertical flipping
     * 
     * @return the x-offset of the pivot
     */
    public double getPivotX() {
        return pivotX;
    }

    /**
     * Returns the x-offset of the pivot for the rotation/vertical flipping
     * 
     * @param pivotX
     *            x-offset of the pivot for the rotation/vertical flipping
     */
    public void setPivotX(double pivotX) {
        this.pivotX = pivotX;
    }

    /**
     * Gets the y-offset of the pivot for the rotation/horizontal flipping
     * 
     * @return y-offset of the pivot for the rotation/vertical flipping
     */
    public double getPivotY() {
        return pivotY;
    }

    /**
     * Returns the y-offset of the pivot for the rotation/horizontal flipping
     * 
     * @param pivotY
     *            y-offset of the pivot for the rotation/vertical flipping
     */
    public void setPivotY(double pivotY) {
        this.pivotY = pivotY;
    }
    
    /**
     * Lets you set whether or not the sprite should be vertically flipped when
     * being rendered
     * 
     * @param flipped
     */
    public void setFlipped(boolean flipped) {
        this.flippped = flipped;
    }

    /**
     * Returns whether or not the sprite should be flipped vertically
     * 
     * @return whether the sprite should get flipped
     */
    public boolean isFlipped() {
        return this.flippped;
    }
    
    /**
     * Allows you change whether or not the given sprite should be drawn on the
     * screen
     * 
     * @param render
     *            whether the sprite should be rendered
     */
    public void setRender(boolean render) {
        this.render = render;
    }

    /**
     * Gets whether the sprite should be drawn on the screen
     * 
     * @return whether or not the sprite should be rendered
     */
    public boolean render() {
        return this.render;
    }
}
