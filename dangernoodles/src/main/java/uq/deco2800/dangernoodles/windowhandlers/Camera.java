package uq.deco2800.dangernoodles.windowhandlers;

/**
 * Created by Jason on 27/08/2016.
 * <p>
 * This class is used to set up a camera for the game.
 */
public class Camera {
    // Private fields to store information about the camera.
    private double x;
    private double y;
    private double saveX = 0;
    private double saveY = 0;
    private double notches = 1;

    /**
     * Default constructor for this class.
     *
     * @param x
     *         a double representing the x coordinate
     * @param y
     *         a double representing the y coordinate
     *
     * @ensure new instance of this class with given coordinates
     */
    public Camera(double x, double y) {
        this.x = x;
        this.y = y;
        this.saveX = x;
        this.saveY = y;
    }

    /**
     * This method is called on every tick of the game and used to control the
     * panning of the camera inside the game
     *
     * @param direction
     *         the direction the camera should go
     */
    public void onTick(CameraEnum direction) {
        double maxHeight = 200;
        double maxWidth = 450;
        double maxDown = 750;
        switch (direction) {
            case UP:
                if (saveY < maxHeight) {
                    this.y += 5;
                    saveY = this.y;
                }
                break;
            case LEFT:
                if (saveX < maxWidth) {
                    this.x += 5;
                    saveX = this.x;
                }
                break;
            case DOWN:
                if (saveY > -maxDown) {
                    this.y -= 5;
                    saveY = this.y;
                }
                break;
            case RIGHT:
                if (saveX > -maxWidth) {
                    this.x -= 5;
                    saveX = this.x;
                }
                break;
            case TOP_RIGHT:
                if (saveX > -maxWidth && saveY < maxHeight) {
                    this.y += 5;
                    this.x -= 5;
                    saveXY(this.x, this.y);
                }
                break;
            case TOP_LEFT:
                if (saveX < maxWidth && saveY < maxHeight) {
                    this.y += 5;
                    this.x += 5;
                    saveXY(this.x, this.y);
                }
                break;
            case BOTTOM_RIGHT:
                if (saveX > -maxWidth && saveY > -maxDown) {
                    this.y -= 5;
                    this.x -= 5;
                    saveXY(this.x, this.y);
                }
                break;
            case BOTTOM_LEFT:
                if (saveX < maxWidth && saveY > -maxDown) {
                    this.y -= 5;
                    this.x += 5;
                    saveXY(this.x, this.y);
                }
                break;
            case ZOOM_IN:
                notches = notches + 0.005;
                break;
            case ZOOM_OUT:
                notches = notches - 0.005;
                break;
            case DONT_MOVE:
                this.x = saveX;
                this.y = saveY;
                break;
            default:
                break;
        }
    }

    /**
     * Save the value of x and y to SaveX and saveY
     *
     * @param x
     *         is the x value to be saved.
     * @param y
     *         is the y value to be saved.
     *
     * @ensure x and y are valid doubles.
     */

    public void saveXY(double x, double y) {
        saveX = x;
        saveY = y;
    }

    /**
     * Return the saved x coordinate, this is used when the camera doesn't move
     * and for camera snapping
     *
     * @return a double representing the saved x coordinate
     *
     * @ensure a double representing the saved x coordinate
     */
    public double getSaveX() {
        return saveX;
    }

    /**
     * Return the saved y coordinate, this is used when the camera doesn't move
     * and for camera snapping
     *
     * @return a double representing the saved y coordinate
     *
     * @ensure a double representing the saved y coordinate
     */
    public double getSaveY() {
        return saveY;
    }

    /**
     * Return the x coordinate
     *
     * @return a double representing the x coordinate
     *
     * @ensure a double representing the x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Return the y coordinate
     *
     * @return a double representing the y coordinate
     *
     * @ensure a double representing the y coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Set the x coordinate.
     *
     * @param x
     *         a double representing the x coordinate
     *
     * @ensure getX() == x
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Set the y coordinate.
     *
     * @param y
     *         a double representing the y coordinate
     *
     * @ensure getY() == y
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * this method returns the current zoom notch
     *
     * @return current zoom notch
     */
    public double getNotch() {
        return this.notches;
    }

    /**
     * sets the notch to be an integer value
     *
     * @param notch an integer to set the notch too.
     */
    public void setNotch(double notch) {
       this.notches = notch;
    }


}


