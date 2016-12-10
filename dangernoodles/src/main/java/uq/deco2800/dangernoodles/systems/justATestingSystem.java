package uq.deco2800.dangernoodles.systems;

import javafx.scene.paint.Color;
import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;

/**
 * Created by khoi_truong on 2016/10/06.
 */
public class justATestingSystem implements System {
    private StaticFrameHandler handler;

    public justATestingSystem(StaticFrameHandler handler) {
        this.handler = handler;
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
        handler.addStaticRenderAction((aContext, aHandler) -> {
            aContext.setFill(Color.LAVENDER);
            aContext.fillRect(50, 50, 50, 50);
        });
    }
}
