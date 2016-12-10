package uq.deco2800.dangernoodles.prefabs;

import uq.deco2800.dangernoodles.prefabs.AngelEntity;
import uq.deco2800.dangernoodles.components.CollisionComponent;
import uq.deco2800.dangernoodles.components.MovementComponent;
import uq.deco2800.dangernoodles.components.noodles.AngelComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.RectangleComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.components.stats.*;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * Test class for the Angel Entity Prefab.
 * @author Paul Haley
 */
public class AngelEntitiesTest {

    /**
     * Tests base case (instance creation) and that all components expected are present.
     */
    @Test
    public void baseCreationTest() {

        World miniWorld = new World(0, 0);
        Entity player = AngelEntity.createAngel(miniWorld, 0, 0);
        
        assertTrue(miniWorld.hasComponent(player, AngelComponent.class));
        assertTrue(miniWorld.hasComponent(player, PositionComponent.class));
        assertTrue(miniWorld.hasComponent(player, MovementComponent.class));
        assertTrue(miniWorld.hasComponent(player, SpriteComponent.class));
        assertTrue(miniWorld.hasComponent(player, SpeedComponent.class));
        assertTrue(miniWorld.hasComponent(player, InvulnerableComponent.class));
        assertTrue(miniWorld.hasComponent(player, RectangleComponent.class));
    }
}
