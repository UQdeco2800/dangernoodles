package uq.deco2800.dangernoodles.systems;

import javafx.scene.input.KeyCode;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.inputhandlers.KeyboardHandler;
import uq.deco2800.dangernoodles.windowhandlers.CameraEnum;

/**
 * Created by khoi_truong and jaysun on 2016/09/15.
 * <p>
 * This class is used to control the camera inside the game.
 */
public class CameraSystem implements System {

    private KeyboardHandler keyboardHandler;

    private CameraEnum value;

    public CameraSystem(KeyboardHandler keyboardHandler) {
        this.keyboardHandler = keyboardHandler;
    }

    /**
     * Is called every tick and runs with the t and dt parameters
     *
     * @param world
     * @param t
     *         Time since the beginning of the game
     * @param dt
     *         The delta time, i.e. the time since last frame
     */
    @Override
    public void run(World world, double t, double dt) {

        if (keyboardHandler.isPressed(KeyCode.UP)) {
            if (keyboardHandler.isPressed(KeyCode.LEFT)) {
                value = CameraEnum.TOP_LEFT;
            } else if (keyboardHandler.isPressed(KeyCode.RIGHT)) {
                value = CameraEnum.TOP_RIGHT;
            } else {
                value = CameraEnum.UP;
            }
        } else if (keyboardHandler.isPressed(KeyCode.DOWN)) {
            if (keyboardHandler.isPressed(KeyCode.LEFT)) {
                value = CameraEnum.BOTTOM_LEFT;
            } else if (keyboardHandler.isPressed(KeyCode.RIGHT)) {
                value = CameraEnum.BOTTOM_RIGHT;
            } else {
                value = CameraEnum.DOWN;
            }
        } else if (keyboardHandler.isPressed(KeyCode.LEFT)) {
            value = CameraEnum.LEFT;
        } else if (keyboardHandler.isPressed(KeyCode.RIGHT)) {
            value = CameraEnum.RIGHT;
        } else if (keyboardHandler.isPressed(KeyCode.EQUALS)) {
            value = CameraEnum.ZOOM_IN;
        } else if (keyboardHandler.isPressed(KeyCode.MINUS)) {
            value = CameraEnum.ZOOM_OUT;
        } else {
            value = CameraEnum.DONT_MOVE;
        }

        keyboardHandler.setValue(value);
    }

    /**
     * This function returns the value that the keyboard gets set too
     * This value is passed to the keyboard and used by the camera system
     * To determine which direction the camera should pan
     *
     * @return CameraEnum value - The direction the camera should move in
     */
    public CameraEnum getValue() {
        return value;
    }

}
