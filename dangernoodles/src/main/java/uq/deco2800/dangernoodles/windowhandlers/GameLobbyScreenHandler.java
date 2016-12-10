package uq.deco2800.dangernoodles.windowhandlers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.dangernoodles.ClientManager;
import uq.deco2800.singularity.common.representations.realtime.RealTimeSessionConfiguration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by khoi_truong on 2016/10/19.
 * <p>
 * This class is used to handle the lobby screen where the players are
 * waiting to play game.
 */
public class GameLobbyScreenHandler implements Initializable {
    private static final String CLASS = GameLobbyScreenHandler.class.getName();
    private static final Logger LOGGER = LoggerFactory.getLogger(CLASS);

    private RealTimeSessionConfiguration currentLobby;

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

    private List<Label> playersInLobby = new ArrayList<>();

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
        // Add all players label in a lobby to the list.
        playersInLobby.add(playerOne);
        playersInLobby.add(playerTwo);
        playersInLobby.add(playerThree);
        playersInLobby.add(playerFour);
        playersInLobby.add(playerFive);
        playersInLobby.add(playerSix);
        playersInLobby.add(playerSeven);
        playersInLobby.add(playerEight);
        playersInLobby.add(playerNine);
        playersInLobby.add(playerTen);
        // If leave button is pressed, go back to the main hall
        leaveLobbyButton.setOnAction(e -> leaveLobby());
        displayPlayers();
    }

    private void displayPlayers() {
        ArrayList<String> playerList = ClientManager.getClientManager().
                getRestClient().getCurrentLobby().getPlayerIDs();
        for (int i = 0; i < playerList.size(); i++) {
            playersInLobby.get(i).setText(playerList.get(i));
        }
    }

    private void leaveLobby() {
        LOGGER.info("Leaving lobby");
        // Tell the server that we are leaving it.
        ClientManager.getClientManager().requestLeaveLobby();
        // Remove current lobby after leave the lobby
        currentLobby = null;

        Stage primaryStage = (Stage) leaveLobbyButton.getScene().getWindow();

        StackPane mainHallStackPane = null;
        try {
            mainHallStackPane = FXMLLoader.load(getClass().getResource
                    ("/gameLobby.fxml"));
        } catch (IOException e) {
            LOGGER.error("Lobby screen cannot be found.");
            System.exit(-1);
        }

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        Scene lobbyScene = new Scene(mainHallStackPane, bounds.getWidth(), bounds.getHeight());
        primaryStage.setScene(lobbyScene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
    }

    public void setCurrentLobby(RealTimeSessionConfiguration currentLobby) {
        if (currentLobby == null) {
            throw new NullPointerException("Current Lobby must not be null.");
        }
        this.currentLobby = currentLobby;
    }
}
