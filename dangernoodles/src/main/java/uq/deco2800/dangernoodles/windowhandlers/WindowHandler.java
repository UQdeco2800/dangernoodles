package uq.deco2800.dangernoodles.windowhandlers;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.concurrent.*;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.dangernoodles.*;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.inputhandlers.KeyboardHandler;
import uq.deco2800.dangernoodles.inputhandlers.MouseHandler;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;
import uq.deco2800.singularity.clients.realtime.messaging.MessagingClient;
import uq.deco2800.singularity.clients.restful.SingularityRestClient;
import uq.deco2800.singularity.common.representations.User;

import javax.ws.rs.WebApplicationException;

public class WindowHandler implements Initializable {
    @FXML
    private StackPane stackPane;

    private FrameHandler handler;
    private StaticFrameHandler staticHandler;

    private TextField toMessage;
    private TextField messageToSend;
    private Label messaging;
    private String messageChannelId;
    private ArrayList<Map<TeamEnum, ArrayList<NoodleEnum>>> teams;
    private Canvas canvas;
    private Canvas hudCanvas;
    private Game game;

    private final Logger logger = LoggerFactory.getLogger(WindowHandler.class);

    public WindowHandler() {
        teams = new ArrayList<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        double width = 1050;
        double height = 700;
        canvas = new Canvas(width, height);
        //Give canvas a copy of this window handler so that game over maybe called from other classes
        canvas.setUserData(this);

        stackPane.getStylesheets().add("./Background.css");
        // stackPane style class is added in WeatherSystem
        stackPane.getChildren().add(canvas);

        hudCanvas = new Canvas(width, height);
        hudCanvas.setStyle("-fx-background-color: rgb(0, 0, 0, 0);");
        stackPane.getChildren().add(hudCanvas);

        KeyboardHandler keyHandler = new KeyboardHandler();
        MouseHandler mouseHandler = new MouseHandler(canvas);

        stackPane.setOnKeyPressed(keyHandler);
        stackPane.setOnKeyReleased(keyHandler);

        stackPane.setOnMouseMoved(mouseHandler);
        stackPane.setOnMouseDragged(mouseHandler);
        stackPane.setOnMousePressed(mouseHandler);
        stackPane.setOnMouseReleased(mouseHandler);
        stackPane.setOnScroll(mouseHandler.getMouseWheelHandler());

        Image image = new Image("cursor/cursor_normal_32x32.png");
        stackPane.setCursor(new ImageCursor(image));
        stackPane.requestFocus();
        this.game = new Game(keyHandler, mouseHandler, canvas, stackPane);
    }

    /**
     * Gets the instance of the game.
     *
     * @return Game
     * the game object
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * Accessible method for starting game ticks
     */
    public void startGame(boolean isMultiplayer) {
        startGameTicks(game, canvas, hudCanvas, isMultiplayer);
    }

    //chat system
    public void sendMessage() {
        logger.info("Sending message");
        MessagingClient client = ClientManager.getClientManager().getMessagingClient();
        String message = messageToSend.getText();

        logger.info(
                "Currently have: [{}] and client:[{}] and messageChannelId = [{}]",
                message, client, messageChannelId
        );

        if (client == null || messageChannelId == null || message.isEmpty()) {
            return;
        }
        client.sendMessage(messageChannelId, message);
        messageToSend.clear();
    }

    //chat system
    public void changeUserToMessage() {
        String newDestinationUserName = toMessage.getText();
        if (newDestinationUserName.isEmpty()) {
            return;
        }

        SingularityRestClient restClient = ClientManager.getClientManager().getRestClient();
        try {
            User user = restClient.getUserInformationByUserName(newDestinationUserName);
            messaging.setText(user.getUsername());
            messageChannelId = restClient.createMessageChannel(user.getUserId());
            toMessage.clear();
        } catch (WebApplicationException exception) {
            logger.error("RestClient error {}",
                    exception.getResponse().readEntity(String.class));
        } catch (IOException exception) {
            logger.error("WindowHandler IO Exception", exception);
        }

    }

    /**
     * This is the function that starts the game actually moving forwards.
     *
     * @param game
     * @param canvas
     */
    private void startGameTicks(Game game, Canvas canvas, Canvas hudCanvas,
                                boolean isMultiplayer) {
        // Start the game ticks
        game.addSimulationSystems(isMultiplayer);
        GameTimer timer = new GameTimer(game);

        GraphicsContext context = canvas.getGraphicsContext2D();
        GraphicsContext hudContext = hudCanvas.getGraphicsContext2D();


        handler = new FrameHandler(context, game);
        staticHandler = new StaticFrameHandler(hudContext);


        game.addRenderSystems(handler, staticHandler);
        game.addClientInputSystems(canvas);
        new Thread(timer).start();
        handler.start();

        staticHandler.start();
    }

    /**
     * Ends a game by displaying the End Game screen with a given winner.
     * @param winningTeam the winning team.
     */
    public void endGame(TeamEnum winningTeam) {
        //Stop frame handlers
        handler.stop();
        staticHandler.stop();
        stackPane.setVisible(false);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Stage primaryStage = (Stage) stackPane.getScene().getWindow();
                Parent end = null;
                FXMLLoader loader;
                EndGameHandler handler;
                try {
                    loader = new FXMLLoader(getClass().getResource("/EndGame.fxml"));
                    end = loader.load();
                    handler = loader.getController();

                    //Set Game Over message
                    handler.setMessageText("Team " + winningTeam.getName() + " wins!", winningTeam.getColor());

                } catch (IOException e) {
                    logger.error("Error in WindowHandler", e);
                    System.exit(-1);
                }

                AudioManager.playMusic("resources/music/gameover.wav", false, false);

                primaryStage.getScene().setRoot(end);
                return null;
            }
        };
        Thread taskThread = new Thread(task);
        taskThread.start();
    }
}

