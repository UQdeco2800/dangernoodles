package uq.deco2800.dangernoodles.systems;

import javafx.scene.text.Font;
import uq.deco2800.dangernoodles.FrameHandler;
import uq.deco2800.dangernoodles.components.NameComponent;
import uq.deco2800.dangernoodles.components.PlayerComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;

public class PlayerNameSystem implements System {
    private FrameHandler handler;

    public PlayerNameSystem(FrameHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run(World world, double t, double dt) {
        for (ComponentMap cm : world.getIterator(
                PlayerComponent.class,
                PositionComponent.class,
                NameComponent.class,
                SpriteComponent.class
        )) {
            PositionComponent p = cm.get(PositionComponent.class);
            PlayerComponent player = cm.get(PlayerComponent.class);
            SpriteComponent s = cm.get(SpriteComponent.class);
            NameComponent n = cm.get(NameComponent.class);

            // Display name above the noodle
            handler.addRenderAction((context, frameHandler) -> {
                context.setFill(player.getTeamColor());
                context.setFont(Font.font ("Verdana", 14));
                context.fillText(
                        n.getName(),
                        p.getX() - n.getName().length() * 3
                                + s.getWidth() / 2,
                        p.getY() - 15
                );
            });
        }
    }
}
