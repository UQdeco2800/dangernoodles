package uq.deco2800.dangernoodles.components.displays;

import uq.deco2800.dangernoodles.ecs.Component;

/**
 * Created by minhnguyen on 4/09/2016.
 *
 * This class is used to tell the ShopDisplaySystem if the shop panel should be displayed or hidden. This is determined
 * by the keyboard key 'B' being pressed, exit button and player turn.
 */

public class ShopDisplayComponent extends Component{
    private boolean showShopDisplay = false;

    /**
     * Returns a boolean that is used to check if the shop panel should be displayed. This boolean is used several
     * scenarios including: keyboard key 'B' being pressed, exit button of shop panel being pressed and end of
     * players turn.
     *
     * @return a boolean to check if the shop panel needs to be displayed.
     *
     */
    public boolean isShowingDisplay() { return showShopDisplay; }

    /**
     *
     * Set the variable that is used to tell the system if the shop panel should be displayed or not.
     *
     * @param showShopDisplay
     *         a boolean representing the new displaying state of hte shop panel.
     */
    public void setShowDisplay(boolean showShopDisplay) { this.showShopDisplay = showShopDisplay; }


}
