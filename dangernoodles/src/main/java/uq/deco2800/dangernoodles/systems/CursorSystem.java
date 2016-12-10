package uq.deco2800.dangernoodles.systems;

import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.components.displays.ShopDisplayComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;

public class CursorSystem implements System {

    private Entity oldEntity;

    @Override
    public void run(World world, double t, double dt) {
        CursorComponent c = world.getComponents(CursorComponent.class).get(0);
        CollisionComponent col = world.getComponent(c.getEntity(), CollisionComponent.class).get();
        ShopComponent shopComp = world.getComponents(ShopComponent.class).get(0);


        if (c.isClicked()) {
            Entity selected = null;
            for (Entity e : col.getCollisions()) {
                if (world.hasComponent(e, PlayerComponent.class)) {
                    PlayerComponent p = world.getComponent(e, PlayerComponent.class).get();
                    selected = e;
                    p.setSelected(true);
                    c.consumeClick();
                }
                if (world.hasComponent(e, ShopDisplayComponent.class)) {
                    ShopDisplayComponent s = world.getComponent(e, ShopDisplayComponent.class).get();
                    selected = e;
                    s.setShowDisplay(true);
                    c.consumeClick();
                }
            }

            if (selected != null) {
                for (PlayerComponent p : world.getComponents(PlayerComponent.class)) {
                    if (!p.getEntity().equals(selected)) {
                        p.setSelected(false);
                    }
                }
            } else {
                // Blank atm
            }
        }

        // Closes shop display when it isnt the selected entity
        for (ShopDisplayComponent s : world.getComponents(ShopDisplayComponent.class)) {
            for (TurnComponent tc : world.getComponents(TurnComponent.class)) {
                // first time
                if (oldEntity == null && tc.getTurn()) {
                    oldEntity = tc.getEntity();
                }
                // sequential times
                if (tc.getTurn() && !tc.getEntity().equals(oldEntity)) {
                    oldEntity = tc.getEntity();
                    s.setShowDisplay(false);
                    shopComp.setSelectedProductNull();
                    shopComp.resetKey();
                }
            }
        }
    }
}
