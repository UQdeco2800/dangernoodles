package uq.deco2800.dangernoodles.components;

import javafx.scene.paint.Color;
import uq.deco2800.dangernoodles.ecs.Component;

public class StatusBarComponent extends Component {

    private String id;

    private float valuePercentage = 1.0f;
    private int maxValue;
    private int currentValue;
    private Color startColour;
    private Color endColour;
    private Color currentColour;
    private double width;
    private double height;
    private double xOffset;
    private double yOffset;
    private boolean isStatic;

    public StatusBarComponent(String id, double width, double height,
                              int maxValue,
                              Color from, Color to, boolean isStatic,
                              PositionComponent position) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.maxValue = maxValue;
        this.currentValue = this.maxValue;
        this.startColour = from;
        this.currentColour = from;
        this.endColour = to;
        this.xOffset = position.getX();
        this.yOffset = position.getY();
        this.isStatic = isStatic;
    }

    // Sizing

    /**
     *  Sets the height of the StatusBar.
     *
     * @param height of the StatusBar
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Sets the width of the StatusBar.
     *
     * @param width of the StatusBar
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Returns the current width of the StatusBar.
     *
     * @return StatusBar width
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Returns the current height of the StatusBar.
     *
     * @return StatusBar height
     */
    public double getHeight() {
        return this.height;
    }

    // Colors

    /**
     * Returns the stating color of the StatusBar when completely full.
     *
     * @return StatusBar startColour
     */
    public Color getStartColor() {
        return this.startColour;
    }

    /**
     * Sets the starting color of the StatusBar when completely full.
     *
     * @param startColour of the StatusBar
     */
    public void setStartColor(Color startColour) {
        this.startColour = startColour;
    }

    /**
     * Returns the final colour that the StatusBar will change towards when reducing.
     *
     * @return StatusBar endColour
     */
    public Color getEndColor() {
        return this.endColour;
    }

    /**
     * Sets the final colour that the StatusBar will change towards when reducing.
     *
     * @param endColour of the StatusBar
     */
    public void setEndColor(Color endColour) {
        this.endColour = endColour;
    }

    /**
     * Returns the mixture of the startColour and endColour.
     *
     * @return StatusBar currentColour
     */
    public Color getCurrentColor() {
        return this.currentColour;
    }

    /**
     * Sets the mixture of the startColour and endColour.
     *
     * @param colour mixture between startColour and endColour
     */
    public void setCurrentColor(Color colour) {
        this.currentColour = colour;
    }

    // Offsets

    /**
     * Returns the positional offset from parent entity's position, in the x axis.
     *
     * @return xOffset in relation to parent entity.
     */
    public double getOffsetX() {
        return this.xOffset;
    }

    /**
     * Sets the positional offset from parent entity's position, in the x axis.
     *
     * @param xOffset Offset in relation to x axis
     */
    public void setOffsetX(double xOffset) {
        this.xOffset = xOffset;
    }

    /**
     * Returns the positional offset from parent entity's position, in the y axis.
     *
     * @return yOffset in relation to parent entity.
     */
    public double getOffsetY() {
        return this.yOffset;
    }

    /**
     * Sets the positional offset from parent entity's position, in the y axis.
     *
     * @param yOffset Offset in relation to y axis
     */
    public void setOffsetY(double yOffset) {
        this.yOffset = yOffset;
    }

    // Values

    /**
     * Returns the unique identifier for the StatusBar, for using multiple StatusBar's.
     *
     * @return StatusBar Id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the StatusBar, for using multiple StatusBar's.
     *
     * @param id the identifier for the StatusBar
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the amount left in the status bar as a percentage.
     *
     * @return valuePercentage
     */
    public float getPercentage() {
        return this.valuePercentage;
    }

    /**
     * Sets the amount left in the status bar as an integer.
     *
     * @param percentage the amount left in the StatusBar
     */
    public void setPercentage(float percentage) {
        this.valuePercentage = percentage;
    }

    /**
     * Returns the amount left in the status bar as an integer.
     *
     * @return currentValue
     */
    public int getValue() {
        return this.currentValue;
    }

    /**
     * Returns the amount left in the status bar as an integer.
     *
     * @param value an integer value
     */
    public void setValue(int value) {
        this.currentValue = value;
    }

    /**
     * Returns the maximum integer value of the StatusBar.
     *
     * @return maxValue
     */
    public int getMaxValue() {
        return this.maxValue;
    }

    /**
     * Sets the maximum integer value of the StatusBar.
     *
     * @param maxValue the maximum integer value of the StatusBar
     */
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Returns if the StatusBar will have a static position or not.
     *
     * @return isStatic boolean
     */
    public boolean getStatic() {
        return this.isStatic;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            throw new NullPointerException("Object cannot be null.");
        } else if (!(obj instanceof StatusBarComponent)) {
            return false;
        } else {
            StatusBarComponent converted = (StatusBarComponent) obj;
            return this.id.equals(converted.id) &&
                    this.getEntity().equals(converted.getEntity());
        }
    }

    @Override
    public int hashCode() {
        int prime = 37;
        prime *= id.hashCode() * 37 + this.getEntity().hashCode() * 79;
        return Math.abs(prime);
    }
}
