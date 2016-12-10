package uq.deco2800.dangernoodles.systems;

import uq.deco2800.dangernoodles.FrameHandler;
import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.components.displays.EffectDisplayComponent;

import uq.deco2800.dangernoodles.components.displays.ShopDisplayComponent;
import uq.deco2800.dangernoodles.components.NameComponent;


import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;

import uq.deco2800.dangernoodles.inputhandlers.MouseHandler;
import uq.deco2800.dangernoodles.windowhandlers.Camera;


import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class InputSystem implements System {
    // Variable to store the last clicked entity
    private Entity entity;

    private MouseHandler mouseHandler;
    private FrameHandler handler;

    private Logger logger = Logger.getLogger(InputSystem.class.getName());

    public InputSystem(MouseHandler mouseHandler, FrameHandler handler) {
        this.mouseHandler = mouseHandler;
        this.handler = handler;
    }

    private void removeCam(World world) {
        List<StatusBarComponent> bars = world.getComponents(StatusBarComponent.class);
        if (bars.size() > 1) {
            double camX = handler.getCamera().getX();
            double camY = handler.getCamera().getY();
            StatusBarComponent healthbar = bars.get(1);
            healthbar.setOffsetX(-510 - camX);
            healthbar.setOffsetY(-387 - camY);
            healthbar.setWidth(0);
            healthbar.setHeight(0);
        }
    }

    @Override
    public void run(World world, double t, double dt) {
        List<PositionComponent> positions = world.getComponents(PositionComponent.class);

        // This variable is used to determine if this click has landed on an entity
        boolean hitEntity = false;
        String entityString = "entity: ";

        for (PositionComponent p : positions) {
            Optional<RectangleComponent> rectangleComponent = world.getComponent(p.getEntity(), RectangleComponent.class);
            Optional<EffectDisplayComponent> effectDisplayComponent = world.getComponent(p.getEntity(), EffectDisplayComponent.class);
            Optional<ShopDisplayComponent> shopDisplayComponent = world.getComponent(p.getEntity(), ShopDisplayComponent.class);




            if (rectangleComponent.isPresent() && effectDisplayComponent.isPresent()) {
                RectangleComponent r = rectangleComponent.get();
                EffectDisplayComponent d = effectDisplayComponent.get();
                Camera cam = handler.getCamera();

                if (mouseHandler.isPressed()) {
                    logger.info(entityString + p.getEntity());
                    logger.info("Mouse X:" + (int)mouseHandler.getMouseX());
                    logger.info("Mouse Y:" + (int)mouseHandler.getMouseY());
                    logger.info("Cam X:" + (int)cam.getX());
                    logger.info("Cam Y:" + (int)cam.getY());
                    logger.info("Pos X:" + (int)p.getX());
                    logger.info("Pos Y:" + (int)p.getY());


                }

                if (mouseHandler.isPressed() &&
                        mouseHandler.getMouseX() - cam.getX() >= p.getX() &&
                        mouseHandler.getMouseX() - cam.getX() <= p.getX() + r.getWidth() &&
                        mouseHandler.getMouseY() - cam.getY() >= p.getY() &&
                        mouseHandler.getMouseY() - cam.getY() <= p.getY() + r.getHeight()) {

                    hitEntity = true;

                    // This line checks if the noodle clicked is actually the same one from the
                    // last click
                    if (entity == null || !entity.equals(p.getEntity())) {
                        logger.info(entityString + p.getEntity().toString() + " is clicked for the first time.");
                        entity = p.getEntity();

                        for(PositionComponent pos : positions) {
                            Optional<EffectDisplayComponent> otherDisplay =  world.getComponent(pos.getEntity(), EffectDisplayComponent.class);
                            if(otherDisplay.isPresent()) {
                                EffectDisplayComponent noodleDisplay = otherDisplay.get();
                                Entity ownerOfDisplay = noodleDisplay.getEntity();
                                if(!(ownerOfDisplay.equals(entity)) && noodleDisplay.isShowTooltip()) {
                                    noodleDisplay.setShowTooltip(false);
                                }
                            }
                        }

                        d.setShowTooltip(true);



                    } else {
                        logger.info(entityString + p.getEntity().toString() + " is clicked already.");
                        d.setShowTooltip(true);

                    }
                    break;
                }
            }

            if (rectangleComponent.isPresent() && shopDisplayComponent.isPresent()) {
                RectangleComponent r = rectangleComponent.get();
                ShopDisplayComponent sdc = shopDisplayComponent.get();

                if (mouseHandler.isPressed() &&
                        mouseHandler.getMouseX() >= p.getX() && mouseHandler.getMouseX() <= p.getX() + r.getWidth() &&
                        mouseHandler.getMouseY() >= p.getY() && mouseHandler.getMouseY() <= p.getY() + r.getHeight()) {

                    hitEntity = true;

                    // This line checks if the noodle clicked is actually the same one from the
                    // last click.
                    if (entity == null || !entity.equals(p.getEntity())) {
                        logger.info(entityString + p.getEntity().toString() + " is clicked for the first time.");
                        Optional<NameComponent> shopName = world.getComponent(p.getEntity(), NameComponent.class);
                        if (shopName.isPresent()) {  // checking correct entity - MINH
                            NameComponent name = shopName.get();
                            logger.info("Entered shop: " + name.getName());
                        }
                        entity = p.getEntity();
                        sdc.setShowDisplay(true);

                    } else {
                        logger.info(entityString + p.getEntity().toString() + " is clicked already.");
                        sdc.setShowDisplay(true);
                    }
                    break;
                }
            }
        }

        // Check if the player just clicked in empty space. Or dragged and released the mouse
        if (!hitEntity && mouseHandler.isPressed()) {
            logger.info("Click elsewhere.");
            if (entity != null) {

                Optional<EffectDisplayComponent> effectDisplayCompmonent = world.getComponent(entity, EffectDisplayComponent.class);
                if (effectDisplayCompmonent.isPresent()) {
                    EffectDisplayComponent d = effectDisplayCompmonent.get();
                    d.setShowTooltip(false);
                    removeCam(world);
                }
                Optional<ShopDisplayComponent> shopDisplayCompmonent = world.getComponent(entity, ShopDisplayComponent.class);
                if (shopDisplayCompmonent.isPresent()) {
                    ShopDisplayComponent s = shopDisplayCompmonent.get();
                    s.setShowDisplay(false);

                }


                entity = null;
            }
        }

    }

}


