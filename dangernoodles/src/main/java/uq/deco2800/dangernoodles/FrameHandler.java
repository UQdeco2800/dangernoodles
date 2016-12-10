package uq.deco2800.dangernoodles;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.animation.AnimationTimer;

import javafx.event.EventHandler;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.dangernoodles.components.console.ConsoleDisplayComponent;
import uq.deco2800.dangernoodles.inputhandlers.KeyboardHandler;
import uq.deco2800.dangernoodles.windowhandlers.Camera;
import uq.deco2800.dangernoodles.windowhandlers.CameraEnum;


/**
 * 30/7/16 Created by Timothy Ryan Hadwen for deco2800-2016-dangernoodles
 * <p>
 * This class is used to render non-static objects into the canvas.
 * @author Timothy Hadwen
 */
public class FrameHandler extends AnimationTimer {
    private GraphicsContext graphicsContext;
    private Camera cam;
    private float lastTime = 0;
    private double scaleFactor = 1;
    private double totalZoom = 1;
    private CameraEnum direction;
    private List<List<RenderAction>> renderActionLists;
    private List<List<StaticRenderAction>> staticRenderActionList;
    private KeyboardHandler camHandler;

    /*
     * currentRenderList refers to the list of render actions that new actions will be added to
     * Example: if currentRenderList == 0, new actions added to renderActionLists[0]
     * and handle draws all the actions in renderActionLists[1]
     */
    private int currentRenderList = 0;


    private HashMap<String, Image> imagePool = new HashMap<>();
    private static String thisClass = FrameHandler.class.getName();
    private final Logger logger = LoggerFactory.getLogger(thisClass);

    public FrameHandler(GraphicsContext graphicscontext, Game game) {
        this.graphicsContext = graphicscontext;
        this.cam = new Camera(0, 0);

        this.renderActionLists = new ArrayList<>();
        this.renderActionLists.add(new ArrayList<>());
        this.renderActionLists.add(new ArrayList<>());

        this.staticRenderActionList = new ArrayList<>();
        this.staticRenderActionList.add(new ArrayList<>());
        this.staticRenderActionList.add(new ArrayList<>());

        this.camHandler = game.getCamHandler();
    }

    /**
     * Return the current camera.
     *
     * @return an instance of Camera class that represents the current camera
     */
    public Camera getCamera() {
        return cam;
    }

    /**
     * Set the camera position based on key pressed.
     */
    private void getPosition() {
        direction = camHandler.getValue();
    }

    /**
     * Returns the scale current scale factor
     *
     * @return the current scale factor
     */

    public double getTotalZoom() {
        return this.totalZoom;
    }

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    /**
     * Scale came based on which key is pressed.
     *
     * This function is used for zooming the game, it acts as a method
     * of scaling the entire canvas focused at the centre.
     *
     */
    private void scaleCam() {
        double notches = cam.getNotch();
        scaleFactor = notches;

        if (scaleFactor < 1) {
            totalZoom = totalZoom - (1 - scaleFactor);
        } else {
            totalZoom = totalZoom + (scaleFactor - 1);
        }

        cam.setNotch(1);
        if (totalZoom <= 1.5 && totalZoom >= 1) {
            graphicsContext.scale(scaleFactor, scaleFactor);
            graphicsContext.getCanvas().setWidth(graphicsContext.getCanvas().getWidth() * scaleFactor);
            graphicsContext.getCanvas().setHeight(graphicsContext.getCanvas().getHeight() * scaleFactor);
        } else if (totalZoom > 1.5) {
            totalZoom = 1.5;
        } else if (totalZoom < 1) {
            totalZoom = 1;
        }
    }

    /**
     * Called when a rendering tick is due for rendering
     */
    @Override
    public synchronized void handle(long now) {
        createConsoleWindow();
        getPosition();

        if (direction != null) {
            cam.onTick(direction);
        }
        if (direction == CameraEnum.ZOOM_IN || direction == CameraEnum.ZOOM_OUT) {
            scaleCam();
        }

        graphicsContext.clearRect(0, 0, 1100, 800);

        graphicsContext.translate(cam.getX(), cam.getY()); //Start Camera

        //Do all the rendering actions in this list
        for (RenderAction action : getRenderList()) {
            action.doRenderAction(this.graphicsContext, this);
        }

        graphicsContext.translate(-cam.getX(), -cam.getY()); // End Camera
        //Do all the rendering actions in this list
    }

    private List<RenderAction> getBuildList() {
        // XOR 1 of 1 is 0 and XOR 1 of 0 is 1
        return this.renderActionLists.get(this.currentRenderList ^ 1);
    }

    private List<RenderAction> getRenderList() {
        return this.renderActionLists.get(this.currentRenderList);
    }

    public synchronized void addRenderAction(RenderAction action) {
        this.getBuildList().add(action);
    }

    /**
     * Toggles the renderActionList that new actions are added to
     */
    synchronized void swapCurrentRenderLists() {
        this.getRenderList().clear();
        this.currentRenderList = this.currentRenderList ^ 1;
    }

    /**
     * Calculates the current FPS based on time of rendering ticks
     *
     * @return float of the number of frames per second
     */
    public float calculateFPS() {
        long thisTime = System.nanoTime();
        float x = 1 / ((thisTime - lastTime) / (float) (1000000000.0));
        this.lastTime = thisTime;
        return x;
    }

    /**
     * Checks if the given image path exists in the hash map.
     * If so, retrieve the image from the hash map and return it.
     * Otherwise, add it to the hash map and return the image.
     *
     * @param image
     *          image location that is being checked
     * @return image to be loaded
     */
    public Image loadImage(String image) {
        try {
            if (!imagePool.containsKey(image)) {
                imagePool.put(image, new Image(image));
               logger.info("Loading image {}", image);
            }
        } catch (IllegalArgumentException e) {
            logger.info("Exception loading image: {}", e);
        }
        return imagePool.get(image);
    }


    private boolean displayConsole = false;
    private String command;
    private ConsoleDisplayComponent consoleDisplayComponent;
    // Store the maximum height of texts inside scrollPane
    private double totalTextHeight = 0;
    private int counter = 0;
    private static final int MAXSIZE = 20;
    private ArrayList<String> commands = new ArrayList<>();

    /**
     * Returns the most recent message that the user has typed into the console. May return null if nothing
     * has been entered.
     *
     * @return String representign the most recent message the user has entered.
     */
    public synchronized String getCommand() {
        if (command != null && !command.isEmpty()) {
            return command;
        }
        return null;
    }

    /**
     * Updates the variable command to the given argument. The argument is taken from messages entered by the user
     * in the console display system.
     *
     * @param command
     *          String representation of the message entered by the player in the console system.
     */
    public synchronized void setCommand(String command) {
        this.command = command;
    }

    /**
     * Sets the display state of the display of the console
     *
     * @param displaying boolean representing if the console should  be displayed
     */
    public synchronized void setDisplayConsole(boolean displaying) {
        displayConsole = displaying;
    }

    /**
     * Sets the display component for the console
     *
     * @param c
     *          Component used to determine if the console window should be displayed
     */
    public synchronized void setConsoleComponent(ConsoleDisplayComponent c) {
        consoleDisplayComponent = c;
    }

    /**
     * Checks if the console should be displayed. If so, create a window through javaFX UI components.
     */
    public void createConsoleWindow() {
        if (displayConsole) {
            BorderPane console = new BorderPane();
            // Command history from here
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setMinWidth(500);
            scrollPane.setMaxWidth(500);
            scrollPane.setMinHeight(260);
            scrollPane.setMaxHeight(260);
            // Pane inside scrollPane
            Pane innerPane = new Pane();
            innerPane.setMinWidth(480);
            innerPane.setMaxWidth(480);
            // VBox inside innerPane
            VBox innerPaneBox = new VBox();
            innerPaneBox.setMinWidth(480);
            innerPaneBox.setMaxWidth(480);
            // Add innerPaneBox into innerPane
            innerPane.getChildren().add(innerPaneBox);
            // VBox inside scrollPane to contain innerPane
            VBox scrollBox = new VBox(innerPane);
            scrollBox.setMinWidth(500);
            scrollBox.setMaxWidth(500);
            // Add scrollBox to scrollPane
            scrollPane.setContent(scrollBox);
            // Add the scrollPane to center of the console pane
            console.setCenter(scrollPane);
            // Text field
            TextField textField = new TextField();
            textField.setFont(new Font("Courier New", 17));

            HBox bottom = new HBox(textField);
            textField.setMaxWidth(500);
            textField.setMinWidth(500);
            textField.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                /**
                 * Monitors input from the keyboard and adjusts the window accordingly.
                 * @param event
                 *          Event representing a key stroke
                 */
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.UP && !commands.isEmpty() && counter > 0) {
                            textField.setText(commands.get(--counter));
                            textField.positionCaret(1);
                    }

                    if (event.getCode() == KeyCode.DOWN
                            && !commands.isEmpty() && counter < commands.size() - 1) {
                            textField.setText(commands.get(++counter));
                            textField.positionCaret(1);
                    }

                    if (event.getCode() == KeyCode.ENTER) {
                        command = textField.getText();
                        if (command.isEmpty()) {
                            return;
                        }

                        // Add command to the last index of the list
                        commands.add(command);
                        counter = commands.size();
                        if (commands.size() >= MAXSIZE) {
                            commands.remove(0);
                        }

                        // Add new command into the window
                        Text newText = new Text(command);
                        newText.setFont(new Font("Courier New", 17));
                        if (-0.05 <= totalTextHeight && totalTextHeight <= 0.05) {
                            totalTextHeight = newText.getLayoutBounds().getHeight();
                        }
                        totalTextHeight += newText.getLayoutBounds().getHeight();
                        logger.info("SP: " + scrollPane.getHeight());
                        logger.info("TH: " + totalTextHeight);
                        innerPaneBox.getChildren().addAll(newText);
                        innerPaneBox.setMinHeight(totalTextHeight);
                        innerPaneBox.setMaxHeight(totalTextHeight);
                        innerPane.setMinHeight(totalTextHeight);
                        innerPane.setMaxHeight(totalTextHeight);
                        scrollPane.setVvalue(100);
                        textField.clear();
                    }
                }
            });
            textField.lengthProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.intValue() > oldValue.intValue() && textField.getText().length() >= 46) {
                        textField.setText(textField.getText().substring(0, 46));
                }
            });

            bottom.setMaxWidth(500);
            bottom.setMinWidth(500);
            console.setBottom(bottom);

            Scene consoleScene = new Scene(console, 500, 300);
            consoleScene.setCursor(new ImageCursor(new Image("/cursor/cursor_normal_32x32.png")));

            Stage consoleStage = new Stage();
            consoleStage.setTitle("Console");
            consoleStage.setScene(consoleScene);
            consoleStage.show();
            consoleStage.setResizable(false);
            displayConsole = false;

            consoleStage.setOnCloseRequest(e -> {
                consoleDisplayComponent.setDisplaying(false);
                totalTextHeight = 0;
            });
        }
    }
}
