package uq.deco2800.dangernoodles.systems;

import javafx.scene.paint.Color;
import uq.deco2800.dangernoodles.FrameHandler;
import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.StatusBarComponent;
import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;

import java.util.*;

public class StatusBarSystem implements System {
    // Hash map to store all of the static bars' position. This is to prevent shifting when moving the camera.
    private HashMap<StatusBarComponent, ArrayList<Double>> staticBars = new HashMap<>();


    private FrameHandler handler;
    private StaticFrameHandler staticHandler;


    public StatusBarSystem(FrameHandler handler, StaticFrameHandler staticHandler) {
        this.handler = handler;
        this.staticHandler = staticHandler;
    }

    @Override
    public void run(World world, double t, double dt) {
        List<StatusBarComponent> statusBars = world.getComponents(StatusBarComponent.class);
        for (StatusBarComponent statusBar : statusBars) {

            // Add all static bars in the world to the hashmap with their
            // static position
            if (statusBar.getStatic() && (staticBars.isEmpty() || !staticBars.containsKey(statusBar))) {
                ArrayList<Double> positions = new ArrayList<>();
                positions.add(statusBar.getOffsetX());
                positions.add(statusBar.getOffsetY());
                staticBars.put(statusBar, positions);
            }

            float oldPercentage = statusBar.getPercentage();
            statusBar.setPercentage((float) statusBar.getValue() / (float) statusBar.getMaxValue());

            if (statusBar.getPercentage() > oldPercentage * 1.01 ||
                    statusBar.getPercentage() < oldPercentage * 0.99) {
                updateColour(statusBar);
            }

            if (!statusBar.getStatic()) {
                Optional<PositionComponent> position = world.getComponent(statusBar.getEntity(), PositionComponent.class);
                if (position.isPresent()) {
                    this.drawBar(statusBar, adjustedPosition(statusBar, position.get()));
                }
            }
        }

        // Run this method to draw all those static bars
        drawStaticBar(world);
    }

    /**
     * Determines a value between two values based on healthPercentage
     *
     * @param from Starting value between 0 and 1
     * @param to   Ending value between 0 and 1
     * @return value between from and to based on healthPercentage
     * @ensure return value between 0 and 1
     */
    private double getGradient(StatusBarComponent bar, double from, double to) {
        double c = (from * bar.getPercentage()) +
                (to * (1 - bar.getPercentage()));
        if (c > 1) {
            return 1.0f;
        }
        if (c < 0) {
            return 0.0f;
        }
        return c;
    }

    /**
     * Updates health bar colour according to remaining healthPercentage
     * using currently set gradient colours
     */
    private void updateColour(StatusBarComponent bar) {
        bar.setCurrentColor(Color.rgb(
                (int) (255 * getGradient(bar, bar.getStartColor().getRed(),
                        bar.getEndColor().getRed())),
                (int) (255 * getGradient(bar, bar.getStartColor().getGreen(),
                        bar.getEndColor().getGreen())),
                (int) (255 * getGradient(bar, bar.getStartColor().getBlue(),
                        bar.getEndColor().getBlue()))
                )
        );
    }

    /**
     * Draws status bar for given position and status bar components.
     *
     * @param statusBar
     * @param position
     */
    private void drawBar(StatusBarComponent statusBar,
                         PositionComponent position) {
        double width = statusBar.getWidth();
        double height = statusBar.getHeight();
        double fillPercentage = statusBar.getPercentage();
        Color colour = statusBar.getCurrentColor();
        double x = position.getX();
        double y = position.getY();
        handler.addRenderAction((context, handler1) -> {
            context.setFill(Color.BLACK);
            context.fillRect(x, y, width, height);
            context.setFill(colour);
            context.fillRect(x + 1, y + 1, (int) ((width * fillPercentage) - 2),
                    height - 2);
        });
    }

    /**
     * Draw static status bars inside a world.
     * <p>
     * This method only works for bars that are static AND only displayed
     * during turn.
     *
     * @param world the game world
     */
    private void drawStaticBar(World world) {
        Set<StatusBarComponent> keys = staticBars.keySet();

        for (StatusBarComponent statusBar : keys) {

            Entity e = statusBar.getEntity();
            Optional<TurnComponent> turnOpt;
            turnOpt = world.getComponent(e, TurnComponent.class);

            if (turnOpt.isPresent() && turnOpt.get().getTurn()) {
                ArrayList<Double> positions = staticBars.get(statusBar);
                double x = positions.get(0);
                double y = positions.get(1);
                double width = statusBar.getWidth();
                double height = statusBar.getHeight();
                double fillPercentage = statusBar.getPercentage();
                Color colour = statusBar.getCurrentColor();
                staticHandler.addStaticRenderAction((context, handler2) -> {
                    context.setFill(Color.BLACK);
                    context.fillRect(x, y, width, height);
                    context.setFill(colour);
                    context.fillRect(x + 1, y + 1, (int) (width * fillPercentage) - 2,
                            height - 2);
                });
            }

        }
    }

    private PositionComponent adjustedPosition(StatusBarComponent statusBar,
                                              PositionComponent position) {

        return new PositionComponent(position.getX() + statusBar.getOffsetX(),
                position.getY() + statusBar.getOffsetY());
    }

}
