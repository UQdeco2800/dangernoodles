package uq.deco2800.dangernoodles.camera;
import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.components.displays.EffectDisplayComponent;
import uq.deco2800.dangernoodles.components.stats.ManaComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.EffectBox;
import uq.deco2800.dangernoodles.prefabs.Parachute;

import static org.junit.Assert.*;
/**
 * Created by Jason on 12/09/2016.
 */


public class effectBoxTest {
    @org.junit.Test
    public void BaseCaseTest() {
        World miniWorld = new World(0, 0);
        Entity box = EffectBox.createBox(miniWorld, 10, 2);
        miniWorld.hasComponent(box, PositionComponent.class);
        miniWorld.hasComponent(box, SpriteComponent.class);
        miniWorld.hasComponent(box, InputComponent.class);
        miniWorld.hasComponent(box, MassComponent.class);
        miniWorld.hasComponent(box, GravityComponent.class);
        miniWorld.hasComponent(box, RectangleComponent.class);
        miniWorld.hasComponent(box, EffectDisplayComponent.class);
        miniWorld.hasComponent(box, CollisionComponent.class);
        miniWorld.hasComponent(box, MovementComponent.class);
        miniWorld.hasComponent(box, ManaComponent.class);
        miniWorld.hasComponent(box, NameComponent.class);


        Entity parachute = Parachute.createParachute(miniWorld, 10 - 5, 2 - 40);

        miniWorld.hasComponent(parachute, PositionComponent.class);
        miniWorld.hasComponent(parachute, SpriteComponent.class);
        miniWorld.hasComponent(parachute, MassComponent.class);
        miniWorld.hasComponent(parachute, GravityComponent.class);
        miniWorld.hasComponent(parachute, MovementComponent.class);

    }
}
