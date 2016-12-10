package uq.deco2800.dangernoodles.physics;

import uq.deco2800.dangernoodles.ecs.Entity;

/**
 * Represents an immutable rectangle attached to an entity
 */
public class Rectangle {
    private final Entity entity;

    private final double x;
    private final double y;
    private final double width;
    private final double height;

    /**
     * Create a new rectangle
     * @param entity The entity
     * @param x Position
     * @param y Position
     * @param width Size
     * @param height Size
     */
    public Rectangle(Entity entity, double x, double y, double width, double height) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Entity getEntity() {
        return entity;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Rectangle rectangle = (Rectangle) o;

        if (Double.compare(rectangle.x, x) != 0
                || Double.compare(rectangle.y, y) != 0
                || Double.compare(rectangle.width, width) != 0) {
            return false;
        }

        return Double.compare(rectangle.height, height) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(width);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(height);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Rectangle - X = " + this.getX() + " Y = " + this.getY() + " " +
                "Width = " + this.getWidth() + " Height = " + this.getHeight();
    }
}
