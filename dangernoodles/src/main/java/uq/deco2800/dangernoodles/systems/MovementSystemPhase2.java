package uq.deco2800.dangernoodles.systems;

import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;

import java.util.List;

/**
 * This system allocates the nextX and nextY positions to be the currentX and
 * currentY for each position, because collision checks have been done and
 * therefore we know that it's safe to move them to their new positions.
 * Created by Luke on 7/09/2016.
 */
public class MovementSystemPhase2 implements System{
    @Override
    public void run(World world, double t, double dt) {
        List<PositionComponent> positions =
                world.getComponents(PositionComponent.class);

        for (PositionComponent p : positions) {
            p.setX(p.getNextX());
            p.setY(p.getNextY());
        }

    }
}
