package uq.deco2800.dangernoodles.systems;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uq.deco2800.dangernoodles.FrameHandler;
import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;

/**
 * Created by khoi_truong on 2016/09/16.
 */
public class FrameSystem implements System {

    private FrameHandler frameHandler;
    private StaticFrameHandler staticHandler;

    public FrameSystem(FrameHandler frameHandler, StaticFrameHandler staticHandler) {
        this.frameHandler = frameHandler;
        this.staticHandler = staticHandler;
    }

    /**
     * Is called every tick and runs with the t and dt parameters
     *
     * @param world
     * @param t
     *         Time since the beginning of the game
     * @param dt
     *         The delta time, i.e. the time since last frame
     */
    @Override
    public void run(World world, double t, double dt) {
        float fps = frameHandler.calculateFPS();

        staticHandler.addStaticRenderAction((context, frameHandler1) -> {
            context.clearRect(0, 0, 1100, 800);
            if (fps > 40) {
                context.setFill(Color.LIGHTGREEN);
            } else if (fps < 40 && fps > 20) {
                context.setFill(Color.ORANGE);
            } else {
                context.setFill(Color.CRIMSON);
            }
            context.setFont(Font.font("Verdana", 22));

            context.fillText(" " + ((int) fps > 60 ? 60 : (int) fps), 750, 30);
        });
    }
}
