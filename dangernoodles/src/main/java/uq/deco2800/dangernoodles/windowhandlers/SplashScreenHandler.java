package uq.deco2800.dangernoodles.windowhandlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uq.deco2800.dangernoodles.AudioPlayer;
import uq.deco2800.dangernoodles.AudioManager;
import uq.deco2800.dangernoodles.ClientManager;
import uq.deco2800.dangernoodles.Game;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;
import uq.deco2800.singularity.clients.restful.SingularityRestClient;
import uq.deco2800.singularity.common.representations.User;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.ws.rs.ProcessingException;

/**
 * 13/7/16 Created by Timothy Ryan Hadwen for deco2800-2016-dangernoodles
 * <p>
 * SplashScreenHandler
 *
 * @author Timothy Hadwen
 */
public class SplashScreenHandler implements Initializable {
    // Declaring the JavaFX labels that will be modified.
    @FXML
    Label userLabel;
    @FXML
    Label passwordLabel;
    @FXML
    Label confirmPassword;

    // Declaring the JavaFX text and password fields that will be utilised.
    @FXML
    TextField userField;
    @FXML
    PasswordField passwordField;
    @FXML
    PasswordField confirmTextField = new PasswordField();

    // Declaring the JavaFX buttons to be used
    @FXML
    Button loginButton;
    @FXML
    Button registerButton;
    @FXML
    Button registerButton2;
    @FXML
    Button backButton;
    @FXML
    Button gkpr;
    @FXML
    Button playLocalButton;
    @FXML
    Button playLocalButtonFinal;
    @FXML
    Button playOnlineButton;
    @FXML
    Button optionsButton;
    @FXML
    Button exitButton;

    // Display Panes
    @FXML
    GridPane paneLogin;
    @FXML
    GridPane paneMainMenu;
    @FXML
    GridPane paneSetup;

    // Game Setup
    @FXML
    ChoiceBox gameWeather;

    @FXML
    TextField team1Name;
    @FXML
    ChoiceBox team1PlayerCount;
    @FXML
    CheckBox team1Ai;

    @FXML
    TextField team2Name;
    @FXML
    ChoiceBox team2PlayerCount;
    @FXML
    CheckBox team2Ai;

    @FXML
    TextField team3Name;
    @FXML
    ChoiceBox team3PlayerCount;
    @FXML
    CheckBox team3Ai;

    @FXML
    TextField team4Name;
    @FXML
    ChoiceBox team4PlayerCount;
    @FXML
    CheckBox team4Ai;

    private boolean loaded = false;
    private static final String EMPTY = "";

    // Logger
    private final Logger logger = LoggerFactory.getLogger(SplashScreenHandler.class);

    // Audio player for menuMusic, will not run until told.
    private AudioPlayer menuMusic;

    /**
     * A function which sets up the JavaFX object handlers for all buttons and
     * text fields.
     *
     * @param location
     *         The location used to resolve relative paths for the root object,
     *         or null if the location is not known.
     * @param resources
     *         The resources used to localize the root object, or null if the
     *         root object was not localized.
     *
     * @require location is valid && location != null && resources != null
     * @ensure The splash screen is made with handlers setup appropriately.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Style DangerNoodles title text with shadow effects
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);

        // Event handler for when the ENTER key is pressed to attempt logging in the user.
        EventHandler<KeyEvent> keyHandler = e -> {
            HandlerUtil.typingSound(); // Plays typing sound
            if (e.getCode() == KeyCode.ENTER) {
                this.attemptLogin(); // If ENTER was pressed, attempts a login with the current details given.
            }
        };

        // Setting up the text fields to work with the keyboard handler define above.
        userField.setOnKeyPressed(keyHandler);
        passwordField.setOnKeyPressed(keyHandler);
        confirmTextField.setOnKeyPressed(keyHandler);

        // Give team player count ChoiceBox values (1-4) in setup screen
        team1PlayerCount.setItems(FXCollections.observableArrayList(1, 2, 3, 4));
        team2PlayerCount.setItems(FXCollections.observableArrayList(1, 2, 3, 4));
        team3PlayerCount.setItems(FXCollections.observableArrayList(1, 2, 3, 4));
        team4PlayerCount.setItems(FXCollections.observableArrayList(1, 2, 3, 4));

        // Setting up the button handlers to call required methods when pressed.
        loginButton.setOnAction(e -> this.attemptLogin());
        registerButton.setOnAction(e -> this.createRegisterForm());
        registerButton2.setOnAction(e -> this.register());
        backButton.setOnAction(e -> this.back());
        exitButton.setOnAction(e -> HandlerUtil.exitGame());

        playOnlineButton.setOnAction(e -> this.viewLobbies());
        playLocalButton.setOnAction(e -> this.showPane(this.paneSetup));
        playLocalButtonFinal.setOnAction(e -> this.playLocalGame());

        menuMusic = AudioManager.playMusic("resources/music/Bumpy_Ride_Rag.wav", true, true);
        menuMusic.setAmplification(0.15f);

        // TODO: Remove this when merge to master
        menuMusic.pause();

        // Below is a shortcut mechanism to login, fills in the login and password in a dirty fashion.
        // obviously not production code: laziness wins out every time.
        gkpr.setOnAction((ActionEvent actionEvent) -> {
            HandlerUtil.clickedSound();
            userField.setText("leggy");
            passwordField.setText("duck");
            playGame();
        });
    }

    /**
     * Shows a given pane by hiding all other panes.
     *
     * @param pane
     *         The GridPane object to be shown
     */
    private void showPane(GridPane pane) {
        HandlerUtil.clickedSound();
        // hide all panes
        paneLogin.setVisible(false);
        paneMainMenu.setVisible(false);

        // show specified pane
        pane.setVisible(true);
    }

    /**
     * This method runs the game on call when the play button is pushed.
     */
    private void playGame() {
        menuMusic.stop(); // Stop the menu music playing, kills thread implicitly
        HandlerUtil.clickedSound();
        Stage primaryStage = (Stage) loginButton.getScene().getWindow();

        Parent splash = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DangerNoodlesMain.fxml"));

        WindowHandler handler = null;
        try {
            splash = loader.load();
            handler = loader.getController();
        } catch (IOException e) {
            logger.error("Error in SplashScreenHandler", e);
            System.exit(-1);
        }

        // test teams
        ArrayList<NoodleEnum> testTeams = new ArrayList<>();
        testTeams.add(NoodleEnum.NOODLE_AGILITY);
        testTeams.add(NoodleEnum.NOODLE_PLAIN);
        testTeams.add(NoodleEnum.NOODLE_TANK);
        testTeams.add(NoodleEnum.NOODLE_WARRIOR);

        handler.getGame().addTeam(0, TeamEnum.TEAM_ALPHA, testTeams, false); // human
        handler.getGame().addTeam(1, TeamEnum.TEAM_DELTA, testTeams, true); // ai

        menuMusic.stop();
        
        // start game ticks
        handler.startGame(false);

        // Setting scene
        Scene scene = new Scene(splash);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> {
            AudioManager.shutdown();
            System.exit(0);
        });
    }

    /**
     * Change the scene into the lobby screen.
     */
    private void viewLobbies() {
        // Plays click sound
        HandlerUtil.clickedSound();

        Stage primaryStage = (Stage) loginButton.getScene().getWindow();

        StackPane lobbiesMainStackPane = null;
        try {
            lobbiesMainStackPane = FXMLLoader.load(getClass().getResource
                    ("/gameLobby.fxml"));
        } catch (IOException e) {
            logger.error(e.getMessage());
            System.exit(-1);
        }

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        Scene lobbyScene = new Scene(lobbiesMainStackPane, bounds.getWidth(), bounds.getHeight());
        primaryStage.setScene(lobbyScene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> {
            AudioManager.shutdown();
            if (ClientManager.getClientManager().getRestClient().getCurrentLobby() != null) {
                ClientManager.getClientManager().getRestClient().requestLeaveLobby();
            }
            System.exit(0);
        });
    }

    /**
     * This method attempts to log into the game
     * <p>
     * The method checks username and password from the two JavaFX fields.
     * <p>
     * If valid, log into singularity.
     */
    private void attemptLogin() {
        HandlerUtil.clickedSound();
        // Tests if either field is empty, notifies the user if so
        if (EMPTY.equals(passwordField.getText()) || EMPTY.equals(userField.getText())) {
            logger.info("Invalid login");
            HandlerUtil.showErrorDialog("The username and/or password field is blank");
        } else {
            // Username and password (not proven valid) ready to be sent to server (Singularity Rest Client).
            SingularityRestClient restClient = ClientManager.getClientManager().getRestClient();
            // Attempts a login with the current credentials.
            try {
                restClient.setupCredentials(userField.getText(), passwordField.getText());
                logger.info("Current user: " + restClient.getUsername());
                // Set up client ID to know which client is which
                ClientManager.getClientManager().setClientId(userField.getText());
                Game.setMultiplayer(true);
            } catch (WebApplicationException exception) { // Username or password was not valid
                Response response = exception.getResponse();
                String data = response.readEntity(String.class);
                logger.error(response.getStatus() + " " + data);

                if (response.getStatus() == 403) {
                    HandlerUtil.showErrorDialog("Invalid login details");
                }

                return;
            }
            showPane(paneMainMenu);
        }
    }

    /**
     * this method creates the user registration form.
     * <p>
     * The back() method reverses this processes.
     */
    private void createRegisterForm() {
        HandlerUtil.clickedSound();

        // Hides the login and register button to make room for the confirm password field.
        loginButton.setVisible(false);
        gkpr.setVisible(false);
        registerButton.setVisible(false);

        // Tests if the confirm password field is loaded. If not, makes it visible.
        if (!loaded) {
            confirmPassword.setVisible(true);
            paneLogin.add(confirmTextField, 1, 2);
            loaded = true;
        } else {
            confirmPassword.setVisible(true);
            confirmTextField.setVisible(true);
        }
        registerButton2.setVisible(true);
        backButton.setVisible(true);
    }

    /**
     * This method is used to register a new user on the click of the register
     * button
     * <p>
     * It takes in the values of the username and two password fields
     * <p>
     * compares the passwords
     * <p>
     * is valid, creates a new account.
     */
    private void register() {
        HandlerUtil.clickedSound();

        logger.info("Attempting to Register User");

        // Checks if either the user, password or confirm password field are empty, tells user if so.
        if (EMPTY.equals(passwordField.getText()) || EMPTY.equals(userField.getText())
                || EMPTY.equals(confirmTextField.getText())) {
            logger.info("Invalid Registration");
            HandlerUtil.showErrorDialog("The username and/or password field is blank");

            // Testing if the password field contents matches that of the confirm password field, tells user if not.
        } else if (!(passwordField.getText().equals(confirmTextField.getText()))) {
            logger.info("Invalid Registration");
            HandlerUtil.showErrorDialog("Your password does not match the confirm field");
        } else {
            // Try and register if the passwords were confirmed
            SingularityRestClient restClient = ClientManager.getClientManager().getRestClient();
            try {
                // Makes a user profile based off the username and password given, then send it to Singularity.
                User user = new User(userField.getText(), EMPTY, EMPTY, EMPTY, passwordField.getText());
                restClient.createUser(user);
                logger.info("USER CREATED, Username: " + userField.getText() + " " + "Password: " +
                        passwordField.getText());
                this.back(); // Returns the user back to the non-register menu form

            } catch (ProcessingException exception) {
                //CATCH IF THE SERVER IS DOWN
                logger.info("Processing Exception");
            } catch (WebApplicationException exception) {  // Mostly looking for registration errors.
                Response response = exception.getResponse();
                String data = response.readEntity(String.class);
                logger.error(response.getStatus() + " " + data);
                //CATCH IF REGISTRATION FAILED

                if (response.getStatus() == 403) {
                    HandlerUtil.showErrorDialog("Registration Failed");
                }
            } catch (IOException exception) {
                // Return
                logger.info("IO Exception");
            }
        }
    }

    /**
     * Initialises a local game from player-specified parameters in the Setup
     * screen.
     */
    private void playLocalGame() {
        Game.setMultiplayer(false);
        menuMusic.stop(); // Stop the menu music playing, kills thread implicitly
        HandlerUtil.clickedSound();
        Stage primaryStage = (Stage) loginButton.getScene().getWindow();

        Parent splash = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DangerNoodlesMain.fxml"));

        WindowHandler handler = null;
        try {
            splash = loader.load();
            handler = loader.getController();
        } catch (IOException e) {
            System.exit(-1);
        }

        // available Noodle Types
        ArrayList<NoodleEnum> availableNoodles = new ArrayList<>();
        availableNoodles.add(NoodleEnum.NOODLE_AGILITY);
        availableNoodles.add(NoodleEnum.NOODLE_PLAIN);
        availableNoodles.add(NoodleEnum.NOODLE_TANK);
        availableNoodles.add(NoodleEnum.NOODLE_WARRIOR);

        // available Team Types
        ArrayList<TeamEnum> availableTeams = new ArrayList<>();
        availableTeams.add(TeamEnum.TEAM_ALPHA);
        availableTeams.add(TeamEnum.TEAM_BRAVO);
        availableTeams.add(TeamEnum.TEAM_CHARLIE);
        availableTeams.add(TeamEnum.TEAM_DELTA);

        // set team names based on user-set values
        availableTeams.get(0).setName(team1Name.getText());
        availableTeams.get(1).setName(team2Name.getText());
        availableTeams.get(2).setName(team3Name.getText());
        availableTeams.get(3).setName(team4Name.getText());

        // user-set values
        ArrayList<Object> selectTeams = new ArrayList<>();
        selectTeams.add(team1PlayerCount.getValue());
        selectTeams.add(team2PlayerCount.getValue());
        selectTeams.add(team3PlayerCount.getValue());
        selectTeams.add(team4PlayerCount.getValue());

        ArrayList<Boolean> selectTeamsAi = new ArrayList<>();
        selectTeamsAi.add(team1Ai.isSelected());
        selectTeamsAi.add(team1Ai.isSelected());
        selectTeamsAi.add(team1Ai.isSelected());
        selectTeamsAi.add(team1Ai.isSelected());

        int teamCount = 0;
        for (int i = 0; i < 4; i++) {
            // if team has a player count set, add it
            if (selectTeams.get(i) != null) {
                handler.getGame().addTeam(teamCount++, availableTeams.get(i),
                        availableNoodles.subList(0, (int) selectTeams.get(i)), selectTeamsAi.get(i));
            }
        }

        // start game ticks
        handler.startGame(false);

        // Setting scene
        Scene scene = new Scene(splash);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
    }

    /**
     * This back method is used to press back when registering a user
     * by pressing back you will be re prompted to log in
     */
    private void back() {
        HandlerUtil.clickedSound();

        // Hides registration menu items.
        gkpr.setVisible(true);
        confirmPassword.setVisible(false);
        registerButton2.setVisible(false);
        backButton.setVisible(false);
        confirmTextField.setVisible(false);

        // Makes original splash screen fields and buttons reappear.
        loginButton.setVisible(true);
        userLabel.setVisible(true);
        passwordLabel.setVisible(true);
        registerButton.setVisible(true);
    }
}