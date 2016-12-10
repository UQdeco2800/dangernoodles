package uq.deco2800.dangernoodles.AISystemTest;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import uq.deco2800.dangernoodles.components.AIComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;
import uq.deco2800.dangernoodles.prefabs.PlayerEntities;
import uq.deco2800.dangernoodles.systems.AISystem;

public class AITargetTests {

    /**
     * This test tests the method for finding enemy players when there are none.
     */
    @Test
    public void testFindTargets1() {

        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();

        boolean isAI = true;

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[1], isAI, availableTeams[2], 1, 320);

        List<PositionComponent> potentialTargets = testAISystem.getPotentialTargets(testWorld, aiPlayer);

        assertTrue(potentialTargets.size() == 0);

    }

    /**
     * This test tests the method for finding enemy players
     */
    @Test
    public void testFindTargets2() {

        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();

        boolean isAI = true;

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        Entity player1 = PlayerEntities.createPlayer(testWorld, availableNoodles[0], !isAI, availableTeams[0], 1, 50);
        Entity player2 = PlayerEntities.createPlayer(testWorld, availableNoodles[1], !isAI, availableTeams[0], 2, 60);
        Entity player3 = PlayerEntities.createPlayer(testWorld, availableNoodles[2], !isAI, availableTeams[0], 3, 70);
        Entity player4 = PlayerEntities.createPlayer(testWorld, availableNoodles[3], !isAI, availableTeams[0], 4, 80);
        Entity player5 = PlayerEntities.createPlayer(testWorld, availableNoodles[2], !isAI, availableTeams[1], 5, 90);
        Entity player6 = PlayerEntities.createPlayer(testWorld, availableNoodles[1], !isAI, availableTeams[1], 6, 190);
        Entity player7 = PlayerEntities.createPlayer(testWorld, availableNoodles[0], !isAI, availableTeams[1], 7, 290);

        List<Entity> opponents = new ArrayList<>();

        opponents.add(player1);
        opponents.add(player2);
        opponents.add(player3);
        opponents.add(player4);
        opponents.add(player5);
        opponents.add(player6);
        opponents.add(player7);

        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[1], isAI, availableTeams[2], 8, 320);

        List<PositionComponent> potentialTargets = testAISystem.getPotentialTargets(testWorld, aiPlayer);

        assertTrue(potentialTargets.size() == 7);

        for (int i = 0; i < 7; ++i) {
            assertTrue(opponents.contains(potentialTargets.get(i).getEntity()));
        }

        // check target is correct
        testAISystem.setTargetEntity(aiPlayer, potentialTargets, testWorld);
        PositionComponent targetPosition = testWorld.getComponent(aiPlayer, AIComponent.class).get()
                .getTargetPosition();
        PositionComponent correctTargetPos = testWorld.getComponent(player7, PositionComponent.class).get();

        assertTrue(Double.compare(targetPosition.getX(), correctTargetPos.getX()) == 0);
        assertTrue(Double.compare(targetPosition.getY(), correctTargetPos.getY()) == 0);
    }

    /**
     * This test tests the method for finding enemy players some of which are AIs
     */
    @Test
    public void testFindTargets3() {

        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();

        boolean isAI = true;

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        Entity player1 = PlayerEntities.createPlayer(testWorld, availableNoodles[0], !isAI, availableTeams[0], 1, 50);
        Entity player2 = PlayerEntities.createPlayer(testWorld, availableNoodles[1], !isAI, availableTeams[0], 2, 60);
        Entity player3 = PlayerEntities.createPlayer(testWorld, availableNoodles[2], !isAI, availableTeams[0], 3, 70);
        Entity player4 = PlayerEntities.createPlayer(testWorld, availableNoodles[3], isAI, availableTeams[0], 4, 80);
        Entity player5 = PlayerEntities.createPlayer(testWorld, availableNoodles[2], isAI, availableTeams[1], 5, 90);
        Entity player6 = PlayerEntities.createPlayer(testWorld, availableNoodles[1], !isAI, availableTeams[1], 6, 190);
        Entity player7 = PlayerEntities.createPlayer(testWorld, availableNoodles[0], !isAI, availableTeams[1], 7, 290);

        List<Entity> opponents = new ArrayList<>();

        opponents.add(player1);
        opponents.add(player2);
        opponents.add(player3);
        opponents.add(player4);
        opponents.add(player5);
        opponents.add(player6);
        opponents.add(player7);

        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[1], isAI, availableTeams[2], 8, 320);

        List<PositionComponent> potentialTargets = testAISystem.getPotentialTargets(testWorld, aiPlayer);

        // check number of potential targets is correct
        assertTrue(potentialTargets.size() == 7);

        // check all the potential targets have been identified
        for (int i = 0; i < 7; ++i) {
            assertTrue(opponents.contains(potentialTargets.get(i).getEntity()));
        }

        // check target is correct
        testAISystem.setTargetEntity(aiPlayer, potentialTargets, testWorld);
        PositionComponent targetPosition = testWorld.getComponent(aiPlayer, AIComponent.class).get()
                .getTargetPosition();
        PositionComponent correctTargetPos = testWorld.getComponent(player7, PositionComponent.class).get();

        assertTrue(Double.compare(targetPosition.getX(), correctTargetPos.getX()) == 0);
        assertTrue(Double.compare(targetPosition.getY(), correctTargetPos.getY()) == 0);
    }

    /**
     * This test tests the method for finding enemy players who are a mixture or AI and non AIs, when the AI being
     * tested is placed within an opposing team of such a mixture itself.
     */
    @Test
    public void testFindTargets4() {

        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();

        boolean isAI = true;

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        Entity player1 = PlayerEntities.createPlayer(testWorld, availableNoodles[0], !isAI, availableTeams[0], 1, 1000);
        Entity player2 = PlayerEntities.createPlayer(testWorld, availableNoodles[1], !isAI, availableTeams[0], 2, 60);
        Entity player3 = PlayerEntities.createPlayer(testWorld, availableNoodles[2], !isAI, availableTeams[0], 3, 70);
        Entity player4 = PlayerEntities.createPlayer(testWorld, availableNoodles[3], isAI, availableTeams[0], 4, 80);
        Entity player5 = PlayerEntities.createPlayer(testWorld, availableNoodles[2], isAI, availableTeams[1], 5, 90);
        Entity player6 = PlayerEntities.createPlayer(testWorld, availableNoodles[1], !isAI, availableTeams[1], 6, 333);
        Entity player7 = PlayerEntities.createPlayer(testWorld, availableNoodles[0], !isAI, availableTeams[1], 7, 290);

        List<Entity> opponents = new ArrayList<>();

        opponents.add(player1);
        opponents.add(player2);
        opponents.add(player3);
        opponents.add(player4);
        opponents.add(player5);
        opponents.add(player6);
        opponents.add(player7);

        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[1], isAI, availableTeams[2], 8, 320);
        Entity aiPlayer1 = PlayerEntities.createPlayer(testWorld, availableNoodles[2], isAI, availableTeams[2], 9, 323);
        Entity aiPlayer2 = PlayerEntities.createPlayer(testWorld, availableNoodles[0], isAI, availableTeams[2], 10,
                330);
        PlayerEntities.createPlayer(testWorld, availableNoodles[1], !isAI, availableTeams[2], 11, 323);
        PlayerEntities.createPlayer(testWorld, availableNoodles[1], !isAI, availableTeams[2], 12, 423);
        PlayerEntities.createPlayer(testWorld, availableNoodles[0], !isAI, availableTeams[2], 13, 823);
        PlayerEntities.createPlayer(testWorld, availableNoodles[0], !isAI, availableTeams[2], 14, 803);
        PlayerEntities.createPlayer(testWorld, availableNoodles[0], !isAI, availableTeams[2], 14, 823);

        List<PositionComponent> potentialTargets = testAISystem.getPotentialTargets(testWorld, aiPlayer);
        List<PositionComponent> potentialTargets1 = testAISystem.getPotentialTargets(testWorld, aiPlayer1);
        List<PositionComponent> potentialTargets2 = testAISystem.getPotentialTargets(testWorld, aiPlayer2);

        assertTrue(potentialTargets.size() == 7);
        assertTrue(potentialTargets1.size() == 7);
        assertTrue(potentialTargets2.size() == 7);

        for (int i = 0; i < 7; ++i) {
            assertTrue(opponents.contains(potentialTargets.get(i).getEntity()));
            assertTrue(opponents.contains(potentialTargets1.get(i).getEntity()));
            assertTrue(opponents.contains(potentialTargets2.get(i).getEntity()));
        }

        // check target is correct
        testAISystem.setTargetEntity(aiPlayer, potentialTargets, testWorld);
        PositionComponent targetPosition = testWorld.getComponent(aiPlayer, AIComponent.class).get()
                .getTargetPosition();
        PositionComponent correctTargetPos = testWorld.getComponent(player6, PositionComponent.class).get();

        assertTrue(Double.compare(targetPosition.getX(), correctTargetPos.getX()) == 0);
        assertTrue(Double.compare(targetPosition.getY(), correctTargetPos.getY()) == 0);

        // check target is correct
        testAISystem.setTargetEntity(aiPlayer1, potentialTargets1, testWorld);
        PositionComponent targetPosition1 = testWorld.getComponent(aiPlayer1, AIComponent.class).get()
                .getTargetPosition();
        PositionComponent correctTargetPos1 = testWorld.getComponent(player6, PositionComponent.class).get();

        assertTrue(Double.compare(targetPosition1.getX(), correctTargetPos1.getX()) == 0);
        assertTrue(Double.compare(targetPosition1.getY(), correctTargetPos1.getY()) == 0);

        // check target is correct
        testAISystem.setTargetEntity(aiPlayer2, potentialTargets2, testWorld);
        PositionComponent targetPosition2 = testWorld.getComponent(aiPlayer2, AIComponent.class).get()
                .getTargetPosition();
        PositionComponent correctTargetPos2 = testWorld.getComponent(player6, PositionComponent.class).get();

        assertTrue(Double.compare(targetPosition2.getX(), correctTargetPos2.getX()) == 0);
        assertTrue(Double.compare(targetPosition2.getY(), correctTargetPos2.getY()) == 0);
    }

}
