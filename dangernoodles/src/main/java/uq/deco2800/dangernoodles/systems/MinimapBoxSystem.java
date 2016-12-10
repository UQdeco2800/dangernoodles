package uq.deco2800.dangernoodles.systems;
import javafx.scene.paint.Color;
import uq.deco2800.dangernoodles.FrameHandler;
import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;


/**
 * Created by Jason on 25/09/2016.
 */
public class MinimapBoxSystem implements System{

    private FrameHandler handler;
    private StaticFrameHandler staticHandler;

    /**
     * Creates a system that controls the movement of the 'focus' square in the rendered minimap.
     *
     * @param handler
     *          Frame handler to render objects onto the canvas.
     * @param staticHandler
     *          Frame handler to render static objects onto the canvas.
     */
    public MinimapBoxSystem(FrameHandler handler, StaticFrameHandler staticHandler) {
        this.handler = handler;
        this.staticHandler = staticHandler;
    }

    /**
     * This method draws the square that moves around the minimap.
     *
     * @param world
     *         is the game world
     * @param t
     *         Time since the beginning of the game
     * @param dt
     *         The delta time, i.e. the time since last frame
     */

    @Override
    public void run(World world, double t, double dt) {
        drawSquare();
    }

    /**
     * This method draws the square that moves around the minimap
     */
    public void drawSquare(){
        double camX = handler.getCamera().getX();
        double camY = handler.getCamera().getY();

        staticHandler.addStaticRenderAction((context, frameHandler) -> {
            context.setFill(Color.DARKGREY);
            context.setFill(new Color(0.2, 0.2, 0.2, 0.3));
            context.setStroke(Color.BLACK);

            double canvasWidth = context.getCanvas().getWidth();
            context.fillRoundRect(canvasWidth - 204 - (camX*0.125), 35 - camY * 0.125, 137, 63, 2, 2);
            context.strokeRoundRect(canvasWidth - 204 - (camX*0.125), 35 - camY * 0.125, 137, 63, 2, 2);

        });
    }

}
