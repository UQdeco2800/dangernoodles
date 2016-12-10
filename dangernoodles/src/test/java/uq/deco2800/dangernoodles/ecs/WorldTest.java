package uq.deco2800.dangernoodles.ecs;

import org.junit.Test;
import uq.deco2800.dangernoodles.components.MovementComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.systems.MovementSystem;

import java.util.Optional;

public class WorldTest {
    @Test
    public void testWorld() {
        World world = new World(0, 0);

        Entity e = world.createEntity();
        assert(world.generations.size() == 1);

        PositionComponent positionComponent = new PositionComponent(0, 0);
        e.addComponent(positionComponent);
        assert(world.hasComponent(e, PositionComponent.class));
        assert(world.getComponents(PositionComponent.class).size() == 1);

        Optional<PositionComponent> pos = world.getComponent(e, PositionComponent.class);
        assert(pos.isPresent());
        assert(pos.get() == positionComponent);

        for (ComponentMap cm : world.getIterator(PositionComponent.class)) {
            PositionComponent p = cm.get(PositionComponent.class);

            assert(p == positionComponent);
            assert(cm.getEntity().equals(e));
        }

        e.addComponent(new MovementComponent(1.0, 1.0, 0.0, 0.0));
        world.addSystem(new MovementSystem(), 1);
        world.process(0, 1);
        assert(positionComponent.getNextX() == 1 && positionComponent.getNextY() ==
                1);

        world.removeComponent(e, MovementComponent.class);
        assert(world.getComponents(MovementComponent.class).size() == 0);
    }
}
