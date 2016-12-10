package uq.deco2800.dangernoodles.windowhandlers;

import javafx.scene.control.Alert;
import uq.deco2800.dangernoodles.AudioManager;

public class HandlerUtil {

    /**
     * This method exits the game, called by the exit button
     */
    public static void exitGame() {
        clickedSound();
        System.exit(0);
    }

    /**
     * Plays the clicked sound.
     */
    public static void clickedSound() {
        AudioManager.playSound("resources/sounds/click2.wav", false);
    }

    /**
     * Plays the sound for when a button is hovered over.
     */
    public static void typingSound() {
        AudioManager.playSound("resources/sounds/click1.wav", false);
    }

    /**
     * this method is used to display an error alert on log in failure.
     *
     * @param error
     *         is the error message you wish to provide.
     */
    public static void showErrorDialog(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Failed Login");
        alert.setContentText(error);
        alert.showAndWait();

        // Plays click sound for when the box is closed.
        clickedSound();
    }
}
