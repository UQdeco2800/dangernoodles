package uq.deco2800.dangernoodles;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by khoi_truong on 2016/10/06.
 * <p>
 * This class is used to render static objects into the canvas.
 */
public class StaticFrameHandler extends AnimationTimer {
    private GraphicsContext graphicsContext;
    private HashMap<String, Image> imagePool = new HashMap<>();
    private List<List<StaticRenderAction>> staticRenderActionList;
    private int currentStaticRenderList = 0;

    public StaticFrameHandler(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
        this.staticRenderActionList = new ArrayList<>();
        this.staticRenderActionList.add(new ArrayList<>());
        this.staticRenderActionList.add(new ArrayList<>());
    }

    /**
     * This method needs to be overridden by extending classes. It is going to
     * be called in every frame while the {@code AnimationTimer} is active.
     *
     * @param now
     *         The timestamp of the current frame given in nanoseconds. This
     *         value will be the same for all {@code AnimationTimers} called
     *         during one frame.
     */
    @Override
    public synchronized void handle(long now) {
        for (StaticRenderAction action : getStaticRenderList()) {
            action.doStaticRenderAction(this.graphicsContext, this);
        }
    }

    /**
     * Toggles the renderActionList that new actions are added to
     */
    synchronized void swapCurrentRenderLists() {
        this.getStaticRenderList().clear();
        this.currentStaticRenderList = this.currentStaticRenderList ^ 1;
    }

    /**
     * Return the static build list.
     *
     * @return a list of static render actions
     *
     * @ensure a list of static render actions
     */
    private List<StaticRenderAction> getStaticBuildList() {
        // XOR 1 of 1 is 0 and XOR 1 of 0 is 1
        return this.staticRenderActionList.get(this.currentStaticRenderList ^ 1);
    }

    /**
     * Return the static render list.
     *
     * @return a list of static render actions
     *
     * @ensure a list of static render actions
     */
    private List<StaticRenderAction> getStaticRenderList() {
        return this.staticRenderActionList.get(this.currentStaticRenderList);
    }

    /**
     * Add static render action to list.
     *
     * @param action
     *         static render action to be added to the list
     */
    public synchronized void addStaticRenderAction(StaticRenderAction action) {
        this.getStaticBuildList().add(action);
    }

    /**
     * Checks if the given image path exists in the hash map.
     * If so, retrieve the image from the hash map and return it.
     * Otherwise, add it to the hash map and return the image.
     *
     * @param image
     *          image location that is being checked
     * @return image to be loaded
     *
     */
    public Image loadImage(String image) {
        try {
            if (!imagePool.containsKey(image)) {
                imagePool.put(image, new Image(image));
            }
        } catch (IllegalArgumentException e) {
        }
        return imagePool.get(image);
    }
}
