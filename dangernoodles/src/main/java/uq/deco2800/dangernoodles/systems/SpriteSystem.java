package uq.deco2800.dangernoodles.systems;

import javafx.scene.image.Image;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import uq.deco2800.dangernoodles.FrameHandler;
import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;

public class SpriteSystem implements System {
    private FrameHandler handler;
    private StaticFrameHandler staticHandler;


    public SpriteSystem(FrameHandler handler, StaticFrameHandler staticHandler) {
        this.handler = handler;
        this.staticHandler = staticHandler;
    }


    @Override
    public void run(World world, double t, double dt) {
        for (ComponentMap cm : world.getIterator(
                PositionComponent.class,
                SpriteComponent.class
        )) {
            PositionComponent p = cm.get(PositionComponent.class);
            SpriteComponent s = cm.get(SpriteComponent.class);

            // skip sprite if it shouldn't be rendered
            if (!s.render())
                continue;

            if (Math.abs(s.getRotation()) < 0.01) {
                //no rotation needs to be performed
                handler.addRenderAction((c, h) -> {
                    c.save();
                    if (s.isFlipped()) {
                        Scale scale = new Scale(-1, 1, p.getX() + s.getPivotX(),
                                p.getY() + s.getPivotY());
                        c.setTransform(scale.getMxx(), scale.getMyx(),
                                scale.getMxy(), scale.getMyy(),
                                scale.getTx() + h.getCamera().getX(),
                                scale.getTy() + h.getCamera().getY());
                    }
                    c.drawImage(h.loadImage(s.getImage()), p.getX(), p.getY(),
                            s.getWidth(), s.getHeight());
                    c.restore();
                });

                staticHandler.addStaticRenderAction((context, handler1) -> {
                    //Draw mini images in the corner
                    context.drawImage(handler1.loadImage(s.getImage()),
                            p.getX() * 0.125 + 840,
                            p.getY() * 0.125 + 10,
                            s.getWidth() * 0.125,
                            s.getHeight() * 0.125);
                });

            } else {
                //Need to rotate
                /*
                 * Saves how the canvas is, rotates it, draws the image reloads
                 * how the canvas was
                 *
                 * From http://stackoverflow.com/a/18262938
                 */

                handler.addRenderAction((c, h) -> {
                            double pivX = p.getX() + s.getPivotX();
                            double pivY = p.getY() + s.getPivotY();
                            double camX = h.getCamera().getX();
                            double camY = h.getCamera().getY();

                            c.save();
                            Rotate rotation = new Rotate(s.getRotation(), pivX, pivY);
                            Image imIn = h.loadImage(s.getImage());
                            c.setTransform(new Affine()); // empty transform

                            if (s.isFlipped()) {
                                Scale scale = new Scale(-1, 1, pivX, pivY);

                                c.transform(scale.getMxx(), scale.getMyx(),
                                        scale.getMxy(), scale.getMyy(),
                                        scale.getTx() + camX, scale.getTy() + camY);
                                camX = 0; // Don't apply the camera translation
                                camY = 0;
                            }

                            c.transform(rotation.getMxx(), rotation.getMyx(),
                                    rotation.getMxy(), rotation.getMyy(),
                                    rotation.getTx() + camX, rotation.getTy() + camY);

                            c.drawImage(imIn, p.getX(), p.getY(), s.getWidth(),
                                    s.getHeight());

                            c.restore();
                        }
                );
            }
        }
    }
}
