package uq.deco2800.dangernoodles.windowhandlers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import uq.deco2800.dangernoodles.AudioManager;
import uq.deco2800.dangernoodles.ClientManager;
import uq.deco2800.dangernoodles.Game;
import uq.deco2800.singularity.clients.dangernoodle.DangernoodleEventListener;
import uq.deco2800.singularity.common.representations.realtime.RealTimeSessionConfiguration;

/**
 * Created by khoi_truong on 2016/10/19.
 * <p>
 * This class is used to handle the game lobby screen. Once connected this
 * screen will display lobbies that the server is current holding so that the
 * player can choose to join one.
 */
public class MainHallScreenHandler implements Initializable {
    private static final String CLASS = MainHallScreenHandler.class.getName();
    private static final Logger LOGGER = LoggerFactory.getLogger(CLASS);

    private List<RealTimeSessionConfiguration> lobbies;

    @FXML
    private Button joinLobbyButton;

    @FXML
    private Button createLobbyButton;

    @FXML
    private Button refreshLobbyButton;

    @FXML
    private Label lobbyOne;

    @FXML
    private Label lobbyTwo;

    @FXML
    private Label lobbyThree;

    @FXML
    private Label lobbyFour;

    @FXML
    private Label lobbyFive;

    @FXML
    private HBox lobbyOneBox;

    @FXML
    private HBox lobbyTwoBox;

    @FXML
    private HBox lobbyThreeBox;

    @FXML
    private HBox lobbyFourBox;

    @FXML
    private HBox lobbyFiveBox;

    @FXML
    private Label playerOne;

    @FXML
    private Label playerTwo;

    @FXML
    private Label playerThree;

    @FXML
    private Label playerFour;

    @FXML
    private Label playerFive;

    @FXML
    private Label playerSix;

    @FXML
    private Label playerSeven;

    @FXML
    private Label playerEight;

    @FXML
    private Label playerNine;

    @FXML
    private Label playerTen;

    @FXML
    private Button leaveLobbyButton;

    @FXML
    private BorderPane mainHallBorderPane;

    @FXML
    private BorderPane lobbyMainBorderPane;

    // All lobby labels inside the main hall
    private List<Label> lobbyLabels = new ArrayList<>();
    private List<HBox> lobbyBoxes = new ArrayList<>();
    // All player labels inside the lobby page
    private List<Label> playerLabels = new ArrayList<>();

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location
     *         The location used to resolve relative paths for the root object,
     *         or <tt>null</tt> if the location is not known.
     * @param resources
     *         The resources used to localize the root object, or <tt>null</tt>
     *         if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Append all lobby labels to the list so that we can loop through
        // them and set them later.
        lobbyLabels.add(lobbyOne);
        lobbyLabels.add(lobbyTwo);
        lobbyLabels.add(lobbyThree);
        lobbyLabels.add(lobbyFour);
        lobbyLabels.add(lobbyFive);
        // Add all boxes
        lobbyBoxes.add(lobbyOneBox);
        lobbyBoxes.add(lobbyTwoBox);
        lobbyBoxes.add(lobbyThreeBox);
        lobbyBoxes.add(lobbyFourBox);
        lobbyBoxes.add(lobbyFiveBox);
        // Bind the button so that when pressed, it will create a new lobby.
        createLobbyButton.setOnAction(e -> createLobby());
        refreshLobbyButton.setOnAction(e -> displayLobbies());
        // Add all players label in a lobby to the list.
        playerLabels.add(playerOne);
        playerLabels.add(playerTwo);
        playerLabels.add(playerThree);
        playerLabels.add(playerFour);
        playerLabels.add(playerFive);
        playerLabels.add(playerSix);
        playerLabels.add(playerSeven);
        playerLabels.add(playerEight);
        playerLabels.add(playerNine);
        playerLabels.add(playerTen);
        // If leave button is pressed, go back to the main hall
        leaveLobbyButton.setOnAction(e -> leaveLobby());
        Game.setMultiplayer(true);
        // Display all lobbies in the game currently.
        displayLobbies();
    }

    /**
     * Set visibility of the main hall pane and all of its children.
     *
     * @param isDisplayed
     *         a boolean to set the visibility of the main hall pane
     *
     * @ensure main hall pane's visibility is set to isDisplayed
     */
    private void setVisibleMainHall(boolean isDisplayed) {
        for (int i = 0; i < mainHallBorderPane.getChildren().size(); i++) {
            mainHallBorderPane.getChildren().get(i).setVisible(isDisplayed);
        }
        mainHallBorderPane.setVisible(isDisplayed);
    }

    /**
     * Set visibility of the lobby pane and all of its children.
     *
     * @param isDisplayed
     *         a boolean to set the visibility of the lobby pane
     *
     * @ensure lobby pane's visibility is set to isDisplayed
     */
    private void setVisibleLobby(boolean isDisplayed) {
        for (int i = 0; i < lobbyMainBorderPane.getChildren().size(); i++) {
            lobbyMainBorderPane.getChildren().get(i).setVisible(isDisplayed);
        }
        lobbyMainBorderPane.setVisible(isDisplayed);
    }

    /**
     * This function is used to create a lobby. It will get the rest client to
     * request a new session (lobby) from the server.
     */
    private void createLobby() {
        // Plays click sound
        clickedSound();
        // Limit the lobbies that can be created. It checks for
        if (lobbies == null || lobbies.size() < 5) {
            ClientManager.getClientManager().getRestClient().requestNewLobby();
            LOGGER.info("Lobby created.");
        }
        displayIndividualLobby();
    }

    /**
     * This function is used to display all lobbies in the game. It will request
     * all available lobbies in the game and display them to appropriate
     * labels.
     */
    private void displayLobbies() {
        setVisibleLobby(false);
        setVisibleMainHall(true);
        // Plays click sound
        clickedSound();
        try {
            // Retrieve all lobbies there are in the game currently and display
            // them to the scene.
            lobbies = ClientManager.getClientManager().getRestClient().requestAvailableLobbies();
            LOGGER.info("Current lobbies in the game are: " + lobbies.size());
            // Reset all labels and boxes just in case.
            for (int i = 0; i < lobbyBoxes.size(); i++) {
                lobbyLabels.get(i).setText("");
                lobbyBoxes.get(i).setOnMouseClicked(null);
            }
            // Now simply go through all labels in the list and set them to
            // appropriate labels.
            for (int i = 0; i < lobbies.size() && i < 5; ++i) {
                lobbyLabels.get(i).setText("Lobby " + (i + 1));
                // Link each lobby to the function so that user can choose which
                // lobby they want to join.
                int finalI = i;
                lobbyLabels.get(i).setOnMouseClicked(e -> {
                    // Request to join the chosen lobby before displaying it
                    // visually.
                    ClientManager.getClientManager().getRestClient().requestJoinLobby(lobbies.get(finalI));
                    // Add listener to play game message.
                    ClientManager.getClientManager().getRestClient().getCurrentLobby()
                            .addPlayGameListener(new PlayGame());
                    // Add listener so that the lobby can update its player
                    // names.
                    ClientManager.getClientManager().getRestClient().getCurrentLobby()
                            .addPlayersNumberListener(new ChangeInPlayerIDs());
                    displayIndividualLobby();
                });
            }
        } catch (IOException e) {
            LOGGER.error("No lobbies were found.");
            java.lang.System.exit(-1);
        }
    }

    /**
     * This function is called when a lobby is clicked in the main hall.
     */
    private void displayIndividualLobby() {
        // Plays click sound
        clickedSound();
        // Simply switch to the lobby pane and display the players.
        setVisibleMainHall(false);
        setVisibleLobby(true);
        displayPlayers();
    }

    /**
     * Plays the clicked sound.
     */
    private void clickedSound() {
        AudioManager.playSound("resources/sounds/click2.wav", false);
    }

    /**
     * Display all player names inside the lobby pane.
     */
    private void displayPlayers() {
        // Retrieve the current player list inside client manager.
        ArrayList<String> playerList;
        while (true) {
            playerList = ClientManager.getClientManager().getRestClient().getCurrentLobby().getPlayerIDs();
            if (playerList != null) {
                break;
            }
        }
        LOGGER.debug("Current players in lobby: {}", playerList.size());
        // Clear all labels
        for (int i = 0; i < playerLabels.size(); i++) {
            playerLabels.get(i).setText("");
        }
        // Loop through all labels and set text to appropriate username.
        for (int i = 0; i < playerList.size(); i++) {
            playerLabels.get(i).setText(playerList.get(i));
        }
    }

    /**
     * Leave the current lobby that the user is in.
     */
    private void leaveLobby() {
        LOGGER.info("Leaving lobby");
        // Request to leave lobby
        ClientManager.getClientManager().getRestClient().requestLeaveLobby();
        // Switch back to main hall.
        setVisibleLobby(false);
        setVisibleMainHall(true);
        displayLobbies();
    }

    /**
     * Start the game when the lobby is full.
     */
    private void startGame() {
        Game.setMultiplayer(true);
        Stage primaryStage = (Stage) createLobbyButton.getScene().getWindow();
        Parent splash = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DangerNoodlesMain.fxml"));
        WindowHandler handler = null;
        try {
            splash = loader.load();
            handler = loader.getController();
        } catch (IOException e) {
            LOGGER.error("Error when loading DangerNoodlesMain.fxml");
            System.exit(-1);
        }
        // start game ticks
        handler.startGame(true);
        // Setting scene
        Scene scene = new Scene(splash);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        primaryStage.setTitle(ClientManager.getClientManager().getRestClient().getCurrentUser());
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });
    }

    /**
     * Internal private class which is used to signal that the player IDs has
     * been updated inside ClientManager.
     */
    private class ChangeInPlayerIDs extends DangernoodleEventListener {
        /**
         * When the server has sent back the players currently inside the lobby.
         * Simply refresh the page by re-display the player IDs.
         */
        @Override
        public void notifyListener() {
            Platform.runLater(() -> displayPlayers());
        }
    }

    /**
     * Internal private class which is used to signal that the game has now
     * started. All players will be brought to the real game screen.
     */
    private class PlayGame extends DangernoodleEventListener {
        @Override
        public void notifyListener() {
            // Start the game.
            Platform.runLater(() -> startGame());
        }
    }
}
