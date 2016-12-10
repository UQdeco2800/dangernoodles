package uq.deco2800.dangernoodles.components;

import uq.deco2800.dangernoodles.ecs.Component;

/**
 * Created by khoi_truong on 2016/09/01.
 * <p>
 * This class is used to give an entity a name.
 */
public class NameComponent extends Component {
    // Private field to store information
    private String name;

    /**
     * Default constructor for this component
     *
     * @param name
     *         a string representing the name of this component
     *
     * @throws NullPointerException
     *         if name is null
     * @require name != null
     * @ensure new instance of this component with given name
     */
    public NameComponent(String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null.");
        }
        this.name = name;
    }

    /**
     * Return the name of this component.
     *
     * @return a string representing the name of this component
     *
     * @ensure a string representing the name of this component
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this component.
     *
     * @param name
     *         a string representing the name of this component
     *
     * @throws IllegalArgumentException
     *         if name is null
     * @require name != null
     * @ensure getName() == name
     */
    public void setName(String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null.");
        }
        this.name = name;
    }
}
