package uq.deco2800.dangernoodles.components.game.settings;

import uq.deco2800.dangernoodles.ecs.Component;

/**
 * Created by khoi_truong on 2016/10/04.
 * <p>
 * This class is used to control the game option menu when the user click on it.
 */
public class SettingDisplayComponent extends Component {
    // Private field to store information about whether the panel is being
    // displayed or not.
    private boolean isDisplaying = false;

    /**
     * Check whether the panel is being displayed or not.
     *
     * @return a boolean representing whether the panel is being displayed
     *
     * @ensure a boolean representing whether the panel is being displayed
     */
    public boolean isDisplaying() {
        return isDisplaying;
    }

    /**
     * Set the displaying status of the panel
     *
     * @param displaying
     *         a boolean a boolean representing whether the panel is being
     *         displayed or not
     *
     * @ensure isDisplaying() == displaying
     */
    public void setDisplaying(boolean displaying) {
        isDisplaying = displaying;
    }

}
