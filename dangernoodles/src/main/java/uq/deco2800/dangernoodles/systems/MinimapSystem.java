package uq.deco2800.dangernoodles.systems;


import javafx.scene.paint.Color;

import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;
/**
 * Created by Jason on 25/09/2016.
 */
public class MinimapSystem implements System{

    private StaticFrameHandler handler;

    /**
     * Creates a system that renders the base panel for the minimap
     *
     * @param handler
     *          Frame handler to render static objects onto the canvas.
     */
    public MinimapSystem(StaticFrameHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run(World world, double t, double dt) {
        handler.addStaticRenderAction((context, frameHandler) -> {
            context.setFill(new Color(0.2, 0.2, 0.2, 0.6));
            context.setStroke(Color.BLACK);
            context.fillRoundRect(790, 10, 250, 183, 5, 5);
            context.strokeRoundRect(790, 10, 250, 183, 5, 5);
        });

    }


}
