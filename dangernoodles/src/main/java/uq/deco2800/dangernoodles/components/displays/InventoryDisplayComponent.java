package uq.deco2800.dangernoodles.components.displays;

import uq.deco2800.dangernoodles.ecs.Component;
/**
 * Created by Park on 10/6/2016.
 */
public class InventoryDisplayComponent extends Component  {
        private boolean isDisplayingInventory = false;

        /**
         * Check if the Inventory is displayed or not.
         *
         * @return a boolean to check if the inventory is displayed.
         *
         */
        public boolean isDisplayingInventory() {
            return isDisplayingInventory;
        }

        /**
         * Set the variable that is used to tell the system if the inventory is displayed or not.
         *
         * @param displaying
         *         a boolean representing if the inventory is displayed or not.
         */
        public void setDisplaying(boolean displaying) {
            isDisplayingInventory = displaying;
        }


}

