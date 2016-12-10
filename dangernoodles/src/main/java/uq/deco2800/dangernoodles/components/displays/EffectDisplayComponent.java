package uq.deco2800.dangernoodles.components.displays;

import uq.deco2800.dangernoodles.ecs.Component;

/**
 * Created by s4334636 on 31/08/2016.
 * <p>
 * This class is used to allow entity to be displayed with tooltip.
 */
public class EffectDisplayComponent extends Component {
    // One variable guards the tooltip box, another guards the effects being displayed on top of the health bar
    private boolean showTooltip = false;
    private boolean showEffects = true;

    /**
     * Check if the effects need to be displayed for this noodle.
     *
     * @return a boolean to check if the effects need to be displayed for this noodle
     *
     * @ensure a boolean to check if the effects need to be displayed for this noodle
     */
    public boolean isShowEffects() {
        return showEffects;
    }

    /**
     * Set the display of the effects for this noodle.
     *
     * @param showEffects
     *         a boolean representing if the effects need to be displayed for this noodle
     *
     * @ensure isShowEffects() == showEffects
     */
    public void setShowEffects(boolean showEffects) {
        this.showEffects = showEffects;
    }

    /**
     * Check if the tooltip needs to be displayed for this noodle.
     *
     * @return a boolean to check if the tooltip needs to be displayed for this noodle
     *
     * @ensure a boolean to check if the tooltip needs to be displayed for this noodle
     */
    public boolean isShowTooltip() {
        return showTooltip;
    }

    /**
     * Check if the tooltip needs to be displayed for this noodle.
     *
     * @param showTooltip
     *         a boolean representing if the tooltip needs to be displayed for this noodle
     *
     * @ensure isShowTooltip() == showTooltip
     */
    public void setShowTooltip(boolean showTooltip) {
        this.showTooltip = showTooltip;
    }

    /**
     * Toggle the display of the tooltip
     *
     * @ensure isShowTooltip() = !isShowTooltip()
     */
    public void toogleShowTooltip() {
        showTooltip = !showTooltip;
    }
}
