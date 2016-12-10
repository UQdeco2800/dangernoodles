package uq.deco2800.dangernoodles;

import javafx.scene.canvas.GraphicsContext;

/**
 * Created by khoi_truong on 2016/09/16.
 * <p>
 * This interface is used to render static objects that are not affected by the
 * camera.
 */
@FunctionalInterface
public interface StaticRenderAction {
    void doStaticRenderAction(GraphicsContext context,
                              StaticFrameHandler handler);
}