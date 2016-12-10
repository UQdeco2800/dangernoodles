package uq.deco2800.dangernoodles.AISystemTest;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uq.deco2800.dangernoodles.components.InputComponent;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;
import uq.deco2800.dangernoodles.prefabs.PlayerEntities;
import uq.deco2800.dangernoodles.systems.AISystem;
import uq.deco2800.dangernoodles.systems.AISystem.Direction;

/**
 * @author Irsan Winarto
 * 
 *         This class tests the correctness of setting the horizontal and vertical velocities of AI players'
 *         MovementComponents.
 */
public class AIMovementTest {

    @Test
    public void testSetVelocitiesRIGHT() {

        // set up environment for testing
        Direction horizontal = Direction.RIGHT;
        Direction vertical = Direction.STAY;
        World testWorld = new World(1100, 800);
        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_AGILITY, true, TeamEnum.TEAM_BRAVO,
                1, 200);
        AISystem testAISystem = new AISystem();
        testAISystem.setAIMovementDirection(horizontal, vertical, aiPlayer, testWorld);

        // check ai input component is correct
        InputComponent aiInputComp = testWorld.getComponent(aiPlayer, InputComponent.class).get();
        assertTrue(aiInputComp.getX() == horizontal.getDirection());
        assertTrue(aiInputComp.getY() == vertical.getDirection());
    }

    @Test
    public void testSetVelocitiesLEFT() {

        // set up environment for testing
        Direction horizontal = Direction.LEFT;
        Direction vertical = Direction.STAY;
        World testWorld = new World(1100, 800);
        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_AGILITY, true, TeamEnum.TEAM_BRAVO,
                1, 200);
        AISystem testAISystem = new AISystem();
        testAISystem.setAIMovementDirection(horizontal, vertical, aiPlayer, testWorld);

        // check ai input component is correct
        InputComponent aiInputComp = testWorld.getComponent(aiPlayer, InputComponent.class).get();
        assertTrue(aiInputComp.getX() == horizontal.getDirection());
        assertTrue(aiInputComp.getY() == vertical.getDirection());
    }

    @Test
    public void testSetVelocitiesUP() {

        // set up environment for testing
        Direction horizontal = Direction.STAY;
        Direction vertical = Direction.UP;
        World testWorld = new World(1100, 800);
        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_AGILITY, true, TeamEnum.TEAM_BRAVO,
                1, 200);
        AISystem testAISystem = new AISystem();
        testAISystem.setAIMovementDirection(horizontal, vertical, aiPlayer, testWorld);

        // check ai input component is correct
        InputComponent aiInputComp = testWorld.getComponent(aiPlayer, InputComponent.class).get();
        assertTrue(aiInputComp.getX() == horizontal.getDirection());
        assertTrue(aiInputComp.getY() == vertical.getDirection());
    }

    @Test
    public void testSetVelocitiesDOWN() {

        // set up environment for testing
        Direction horizontal = Direction.STAY;
        Direction vertical = Direction.DOWN;
        World testWorld = new World(1100, 800);
        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_AGILITY, true, TeamEnum.TEAM_BRAVO,
                1, 200);
        AISystem testAISystem = new AISystem();
        testAISystem.setAIMovementDirection(horizontal, vertical, aiPlayer, testWorld);

        // check ai input component is correct
        InputComponent aiInputComp = testWorld.getComponent(aiPlayer, InputComponent.class).get();
        assertTrue(aiInputComp.getX() == horizontal.getDirection());
        assertTrue(aiInputComp.getY() == vertical.getDirection());
    }

    @Test
    public void testSetVelocitiesLEFTUP() {

        // set up environment for testing
        Direction horizontal = Direction.LEFT;
        Direction vertical = Direction.UP;
        World testWorld = new World(1100, 800);
        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_AGILITY, true, TeamEnum.TEAM_BRAVO,
                1, 200);
        AISystem testAISystem = new AISystem();
        testAISystem.setAIMovementDirection(horizontal, vertical, aiPlayer, testWorld);

        // check ai input component is correct
        InputComponent aiInputComp = testWorld.getComponent(aiPlayer, InputComponent.class).get();
        assertTrue(aiInputComp.getX() == horizontal.getDirection());
        assertTrue(aiInputComp.getY() == vertical.getDirection());
    }

    @Test
    public void testSetVelocitiesLEFTDOWN() {

        // set up environment for testing
        Direction horizontal = Direction.LEFT;
        Direction vertical = Direction.DOWN;
        World testWorld = new World(1100, 800);
        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_AGILITY, true, TeamEnum.TEAM_BRAVO,
                1, 200);
        AISystem testAISystem = new AISystem();
        testAISystem.setAIMovementDirection(horizontal, vertical, aiPlayer, testWorld);

        // check ai input component is correct
        InputComponent aiInputComp = testWorld.getComponent(aiPlayer, InputComponent.class).get();
        assertTrue(aiInputComp.getX() == horizontal.getDirection());
        assertTrue(aiInputComp.getY() == vertical.getDirection());
    }

    @Test
    public void testSetVelocitiesRIGHTUP() {

        // set up environment for testing
        Direction horizontal = Direction.RIGHT;
        Direction vertical = Direction.UP;
        World testWorld = new World(1100, 800);
        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_AGILITY, true, TeamEnum.TEAM_BRAVO,
                1, 200);
        AISystem testAISystem = new AISystem();
        testAISystem.setAIMovementDirection(horizontal, vertical, aiPlayer, testWorld);

        // check ai input component is correct
        InputComponent aiInputComp = testWorld.getComponent(aiPlayer, InputComponent.class).get();
        assertTrue(aiInputComp.getX() == horizontal.getDirection());
        assertTrue(aiInputComp.getY() == vertical.getDirection());
    }

    @Test
    public void testSetVelocitiesRIGHTDOWN() {

        // set up environment for testing
        Direction horizontal = Direction.RIGHT;
        Direction vertical = Direction.DOWN;
        World testWorld = new World(1100, 800);
        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_AGILITY, true, TeamEnum.TEAM_BRAVO,
                1, 200);
        AISystem testAISystem = new AISystem();
        testAISystem.setAIMovementDirection(horizontal, vertical, aiPlayer, testWorld);

        // check ai input component is correct
        InputComponent aiInputComp = testWorld.getComponent(aiPlayer, InputComponent.class).get();
        assertTrue(aiInputComp.getX() == horizontal.getDirection());
        assertTrue(aiInputComp.getY() == vertical.getDirection());
    }

    @Test
    public void testSetVelocitiesSTAY() {

        // set up environment for testing
        Direction horizontal = Direction.STAY;
        Direction vertical = Direction.STAY;
        World testWorld = new World(1100, 800);
        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_AGILITY, true, TeamEnum.TEAM_BRAVO,
                1, 200);
        AISystem testAISystem = new AISystem();
        testAISystem.setAIMovementDirection(horizontal, vertical, aiPlayer, testWorld);

        // check ai input component is correct
        InputComponent aiInputComp = testWorld.getComponent(aiPlayer, InputComponent.class).get();
        assertTrue(aiInputComp.getX() == horizontal.getDirection());
        assertTrue(aiInputComp.getY() == vertical.getDirection());
    }

}
