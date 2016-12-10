package uq.deco2800.dangernoodles.systems;


import static org.junit.Assert.*;

import org.junit.Test;

import uq.deco2800.dangernoodles.components.CollisionComponent;
import uq.deco2800.dangernoodles.components.NameComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;
import uq.deco2800.dangernoodles.prefabs.Parachute;
import uq.deco2800.dangernoodles.prefabs.PlayerEntities;

import java.util.List;

/**
 * Created by Jason on 10/22/2016.
 */
public class ParachuteSystemTest {

    World testworld = new World(0, 0);

    ParachuteSystem parachuteSystem = new ParachuteSystem();


    @Test
    public void testRun() {

        //RUNS WITHOUT ANY BOXES
        testworld.addSystem(parachuteSystem, 0);
        PlayerEntities.createPlayer(testworld, NoodleEnum.NOODLE_TANK, false, TeamEnum.TEAM_ALPHA, 0, 0);
        parachuteSystem.run(testworld, 0, 0);

        int boxes = 0;
        //Create a box
        assertTrue(boxes == 0);
        Entity parachute = Parachute.createParachute(testworld, 0, 390);

        List<NameComponent> nameComponentList = testworld.getComponents(NameComponent.class);

        for (int i = 0; i < nameComponentList.size(); i++) {
            if (("Parachute").equals(nameComponentList.get(i).getName())) {
                boxes++;
            }
        }

        assertTrue(boxes == 1);

        //Ensure there was no collisions

        for (ComponentMap cm : testworld.getIterator(
                NameComponent.class,
                CollisionComponent.class)) {

            CollisionComponent collision = cm.get(CollisionComponent.class);

            assertTrue(collision.getCollisions().isEmpty());

        }

        boxes = 0;

        //Create run the system and check the box is destroyed

        for (ComponentMap cm : testworld.getIterator(
                PositionComponent.class,
                CollisionComponent.class)) {

            CollisionComponent collisions = cm.get(CollisionComponent.class);
            collisions.addCollision(parachute);


        }

        parachuteSystem.run(testworld, 0, 0);

        for (int i = 0; i < nameComponentList.size(); i++) {
            if (("Parachute").equals(nameComponentList.get(i).getName())) {
                boxes++;
            }
        }

        assertTrue(boxes == 0);


        for (ComponentMap cm : testworld.getIterator(
                NameComponent.class,
                CollisionComponent.class)) {

            CollisionComponent collision = cm.get(CollisionComponent.class);

            assertTrue(collision.getCollisions().size() == 1);

        }

    }


}