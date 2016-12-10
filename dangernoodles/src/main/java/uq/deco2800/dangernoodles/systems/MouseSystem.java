package uq.deco2800.dangernoodles.systems;

import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import uq.deco2800.dangernoodles.FrameHandler;
import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.inputhandlers.MouseHandler;

import java.util.List;
import java.util.Optional;

public class MouseSystem implements System {
    private MouseHandler mouseHandler;
    private FrameHandler handler;
    private Canvas canvas;

    private Image cursorImage = new Image("cursor/cursor_normal_32x32.png");
    private ImageCursor cursor = new ImageCursor(cursorImage);

    /**
     * Creates a system that manages the performance of the mouse.
     *
     * @param mouseHandler
     *          Mouse handler to handle mouse events in the game.
     * @param handler
     *          Frame handler to retrieve coordinates of the mouse.
     * @param canvas
     *          Canvas of where the mouse is.
     */
    public MouseSystem(MouseHandler mouseHandler, FrameHandler handler, Canvas canvas) {
        this.mouseHandler = mouseHandler;
        this.handler = handler;
        this.canvas = canvas;
    }

    /**
     * This method controls the mouses interaction on the canvas. It changes the state (clicked, not clicked, etc..)
     * and position (x and y coordinate) of the mouse according to its movement and actions.
     *
     * @param world
     * @param t Time since the beginning of the game
     * @param dt The delta time, i.e. the time since last frame
     */
    @Override
    public void run(World world, double t, double dt) {
        canvas.setCursor(cursor);

        double xOffset = handler.getCamera().getX();
        double yOffset = handler.getCamera().getY();

        List<NameComponent> names = world.getComponents(NameComponent.class);

        for (NameComponent name : names) {
            if ("Cursor".equals(name.getName())) {
                Entity e = name.getEntity();
                Optional<CursorComponent> cursor1 = world.getComponent(e, CursorComponent.class);
                Optional<PositionComponent> position = world.getComponent(e, PositionComponent.class);

                if (cursor1.isPresent() && position.isPresent()) {
                    CursorComponent c = cursor1.get();
                    PositionComponent p = position.get();

                    c.setClicked(mouseHandler.isPressed());
                    p.setNextX(mouseHandler.getMouseX() - xOffset);
                    p.setNextY(mouseHandler.getMouseY() - yOffset);
                }
                break;
            }
        }
    }
}
