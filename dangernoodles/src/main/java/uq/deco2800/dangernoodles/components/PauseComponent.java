package uq.deco2800.dangernoodles.components;

import uq.deco2800.dangernoodles.ecs.Component;

/**
 * Created by khoi_truong on 2016/10/07.
 * <p>
 * This component is used to control the pausing of the game.
 */
public class PauseComponent extends Component {
    private boolean isPaused = false;
    private boolean instructions = true;

    /**
     * Checks if the the game is currently paused.
     *
     * @return boolean denoting if the game should be paused.
     */
    public synchronized boolean isPaused() {
        return isPaused;
    }

    /**
     * Changes the current pause state of the game to the boolean of the
     * argument.
     *
     * @param paused
     *         boolean denoting the new pause state of the game.
     */
    public synchronized void setPaused(boolean paused) {
        isPaused = paused;
    }

    /**
     * Instructions is used to indicate whether pause should show instructions
     * or the settings this method recieves the value to check either or
     * <p>
     * Instructions is used to indicate whether pause should show instructions
     * or the settings
     *
     * @param value
     *         is the value to set instructions too - true means show
     *         instructions false means don't and show setting instead on pause
     */
    public synchronized void setInstructions(boolean value) {
        this.instructions = value;
    }

    /**
     * Instructions is used to indicate whether pause should show instructions
     * or the settings this method recieves the value to check either or
     */
    public synchronized boolean getInstructions() {
        return instructions;
    }
}
