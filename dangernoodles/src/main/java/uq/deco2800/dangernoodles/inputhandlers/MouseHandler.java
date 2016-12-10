package uq.deco2800.dangernoodles.inputhandlers;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by khoi_truong on 2016/08/31.
 * <p>
 * This class is used to handle mouse events in the game.
 */
public class MouseHandler implements EventHandler<MouseEvent> {
    // Variables to store mouse coordinates
    private double mouseX;
    private double mouseY;
    // Variables to store various states of the mouse
    private boolean pressed = false;
    private boolean clicked = false;
    private boolean moved = false;
    private boolean dragged = false;
    private boolean released = false;
    private boolean scrollUp = false;
    private boolean scrolling = false;
    // Variables to store the main canvas as well as a mouse scroll handler.
    private Canvas canvas;
    private MouseWheelHandler mouseWheelHandler;
    private static String thisClass = MouseHandler.class.getName();
    private final Logger logger = LoggerFactory.getLogger(thisClass);
    /**
     * Constructor of this class.
     *
     * @param canvas
     *         the main canvas of the game
     *
     * @throws NullPointerException
     *         if canvas is null
     * @require canvas != null
     * @ensure new instance of this class with given canvas
     */
    public MouseHandler(Canvas canvas) {
        this.canvas = canvas;
        mouseWheelHandler = new MouseWheelHandler();
    }

    /**
     * Invoked when a specific event of the type for which this handler is
     * registered happens.
     *
     * @param event
     *         the event which occurred
     */
    @Override
    public void handle(MouseEvent event) {
        canvas.requestFocus();

        EventType type = event.getEventType();

        // Check if the mouse is pressed while standing still
        if (type == MouseEvent.MOUSE_PRESSED) {
            logger.info("Pressed");
            pressed = true;
            clicked = false;
        } else if (type == MouseEvent.MOUSE_RELEASED) {
            logger.info("Released");
            pressed = false;
            clicked = true;
        }

        // Check if the mouse has moved
        if (type == MouseEvent.MOUSE_MOVED) {
            mouseX = event.getX();
            mouseY = event.getY();
            moved = true;
        }
        // Check if the mouse is pressed and dragged in the canvas
        if (event.isPrimaryButtonDown() && type == MouseEvent.MOUSE_MOVED) {
            dragged = true;
        }
        // Check if the mouse button is released right after being dragged
        if (!event.isPrimaryButtonDown() && dragged) {
            dragged = false;
        }
    }

    /**
     * Check if the mouse is being clicked
     *
     * @return boolean representing if there is a click
     */
    public boolean isClicked() {
        return clicked;
    }

    /**
     * Changes the state of clicked to the argument passed
     *
     * @param clicked boolean representing the new state of clicked
     */
    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }


    /**
     * Check if the mouse is scrolling up.
     *
     * @return a boolean representing if the mouse is scrolling up
     *
     * @ensure a boolean representing if the mouse is scrolling up
     */
    public boolean isScrollUp() {
        return scrollUp;
    }

    /**
     * Check if the mouse is scrolling.
     *
     * @return a boolean representing if the mouse is scrolling
     *
     * @ensure a boolean representing if the mouse is scrolling
     */
    public boolean isScrolling() {
        return scrolling;
    }

    /**
     * Set scrolling state of the mouse.
     *
     * @param scrolling
     *         a boolean representing if the mouse is scrolling
     *
     * @ensure isScrolling() == scrolling
     */
    public void setScrolling(boolean scrolling) {
        this.scrolling = scrolling;
    }

    /**
     * Get the mouse scroll handler.
     *
     * @return the mouse scroll handler
     *
     * @ensure the mouse scroll handler
     */
    public MouseWheelHandler getMouseWheelHandler() {
        return mouseWheelHandler;
    }

    /**
     * Get the x coordinate of the mouse
     *
     * @return a double representing the x coordinate of the mouse
     *
     * @ensure a double representing the x coordinate of the mouse
     */
    public double getMouseX() {
        return mouseX;
    }

    /**
     * Get the y coordinate of the mouse
     *
     * @return a double representing the y coordinate of the mouse
     *
     * @ensure a double representing the y coordinate of the mouse
     */

    public double getMouseY() {
        return mouseY;
    }

    /**
     * Set the x coordinate of the mouse
     *
     * @param x a integer representing the new x coordinate of the mouse
     *
     * @return a double representing the x coordinate of the mouse
     *
     * @ensure a double representing the x coordinate of the mouse
     */
    public double setMouseX(int x) {
        mouseX = x;
        return mouseX;
    }

    /**
     * Set the y coordinate of the mouse
     *
     * @param y a integer representing the new y coordinate of the mouse

     *
     * @return a double representing the y coordinate of the mouse
     *
     * @ensure a double representing the y coordinate of the mouse
     */
    public double setMouseY(int y) {
        mouseY = y;
        return mouseY;
    }


    /**
     * Check if the mouse is pressed.
     *
     * @return a boolean representing if the mouse is pressed
     *
     * @ensure a boolean representing if the mouse is pressed
     */
    public boolean isPressed() {
        return pressed;
    }

    /**
     * Check if the mouse is moved.
     *
     * @return a boolean representing if the mouse is moved
     *
     * @ensure a boolean representing if the mouse is moved
     */
    public boolean isMoved() {
        return moved;
    }

    /**
     * Set moving state of the mouse.
     *
     * @param moved
     *         a boolean representing if the mouse is moving
     *
     * @ensure isMoved() == moved
     */
    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    /**
     * Check if the mouse is dragged.
     *
     * @return a boolean representing if the mouse is dragged
     *
     * @ensure a boolean representing if the mouse is dragged
     */
    public boolean isDragged() {
        return dragged;
    }

    /**
     * Set dragging state of the mouse.
     *
     * @param dragged
     *         a boolean representing if the mouse is dragged
     *
     * @ensure isDragged() == dragged
     */
    public void setDragged(boolean dragged) {
        this.dragged = dragged;
    }

    /**
     * Check if the mouse button is released.
     *
     * @return a boolean representing if the mouse button is released
     *
     * @ensure a boolean representing if the mouse button is released
     */
    public boolean isReleased() {
        return released;
    }

    /**
     * Set released state of the mouse.
     *
     * @param released
     *         a boolean representing if the mouse button is released
     *
     * @ensure isReleased() == released
     */
    public void setReleased(boolean released) {
        this.released = released;
    }

    /**
     * This class is used to handle the scrolling of the mouse.
     */
    private class MouseWheelHandler implements EventHandler<ScrollEvent> {
        /**
         * Invoked when a specific event of the type for which this handler is
         * registered happens.
         *
         * @param event
         *         the event which occurred
         */
        @Override
        public void handle(ScrollEvent event) {
            // Check if the mouse is scrolling
            if (event.getEventType() == ScrollEvent.SCROLL) {
                scrolling = true;
                scrollUp = event.getDeltaY() < 0;
            }

        }
    }
}

