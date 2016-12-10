package uq.deco2800.dangernoodles.components;

import uq.deco2800.dangernoodles.ecs.Component;

public class CursorComponent extends Component {

    private boolean clicked = false;

    /**
     * Checks if the the mouse has been clicked
     */
    public boolean isClicked() {
        return clicked;
    }

    /**
     * Sets the mouse to clicked
     * @param clicked
     *          boolean representing if the mouse has been clicked
     */
    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
    
    /** 
     * Sets the mouse being clicked to false. Is used for consuming clicks in Setting and Shop system to prevent
     * weapons from firing when operating in these systems.
     */
    public void consumeClick() {
        this.clicked = false;
    }
}
