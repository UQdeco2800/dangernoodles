package uq.deco2800.dangernoodles.windowhandlers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class EndGameHandler implements Initializable {
    @FXML
    Label messageLabel;
    @FXML
    Button mainMenuButton;
    @FXML
    Button exitButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exitButton.setOnAction(e -> HandlerUtil.exitGame());
        mainMenuButton.setOnAction(e -> this.navigateMainMenu());
    }

    public void setMessageText(String message, Color color) {
        messageLabel.setTextFill(color);
        messageLabel.setText(message);
    }

    /**
     * Navigates user to main menu
     */
    private void navigateMainMenu() {
        HandlerUtil.clickedSound();
    }
}