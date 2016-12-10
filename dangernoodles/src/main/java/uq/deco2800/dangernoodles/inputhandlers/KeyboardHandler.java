package uq.deco2800.dangernoodles.inputhandlers;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import uq.deco2800.dangernoodles.windowhandlers.CameraEnum;

import java.util.EnumMap;

public class KeyboardHandler implements EventHandler<KeyEvent> {

    private EnumMap<KeyCode, Boolean> keys = new EnumMap<>(KeyCode.class);
    private CameraEnum value;

    @Override
    public void handle(KeyEvent event) {
        keys.put(event.getCode(), event.getEventType() == KeyEvent.KEY_PRESSED);
    }

    /**
     * Checks if the argument key code was pressed
     * @param keyCode
     *          KeyCode representing the key stroke that is pressed
     * @return boolean representing if the specified key was pressed
     */
    public boolean isPressed(KeyCode keyCode) {
        Boolean key = keys.get(keyCode);
        return key != null && key;
    }

    /**
     * Sets value to the most recently activated Camera direction
     * @param value
     *          CameraEnum that represents a direction that camera is moving
     */
    public void setValue(CameraEnum value) {
        this.value = value;
    }

    /**
     * Retrieves the most recently pressed camera direction
     * @return CameraEnum that represents the direction the camera is moving
     */
    public CameraEnum getValue() {
        return value;
    }

}
