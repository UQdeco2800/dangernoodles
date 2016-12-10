package uq.deco2800.dangernoodles.components;

import uq.deco2800.dangernoodles.ecs.Component;

/**
 * Created by Luke on 2/09/2016.
 */
public class CircleComponent extends Component {
    private double radius;

    /**
     * Creates a circle component with the specified radius.
     * @param radius
     */
    public CircleComponent(double radius) {
        this.radius = radius;
    }

    /**
     * Retrieves the radius of the component
     * @return
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Set a new radius for the circle component
     * @param radius
     *          double representing the new radius of the circle component
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }
}
