package uq.deco2800.dangernoodles.components.console;

import uq.deco2800.dangernoodles.ecs.Component;

/**
 * Created by khoi_truong on 2016/10/04.
 * <p>
 * This class is used to control the display of the console. When the
 */
public class ConsoleDisplayComponent extends Component {
    // Private field to store information about display status of the console
    // window.
    private boolean isDisplaying = false;

    /**
     * Check if the console is displaying.
     *
     * @return a boolean representing whether the console is displaying
     *
     * @ensure a boolean representing whether the console is displaying
     */
    public boolean isDisplaying() {
        return isDisplaying;
    }

    /**
     * Set the display status of the console
     *
     * @param displaying
     *         a boolean used to set the display status of the console
     *
     * @ensure isDisplaying() == displaying
     */
    public void setDisplaying(boolean displaying) {
        isDisplaying = displaying;
    }
}
