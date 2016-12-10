package uq.deco2800.dangernoodles.AISystemTest;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uq.deco2800.dangernoodles.components.CollisionComponent;
import uq.deco2800.dangernoodles.components.FrictionComponent;
import uq.deco2800.dangernoodles.components.GravityComponent;
import uq.deco2800.dangernoodles.components.InputComponent;
import uq.deco2800.dangernoodles.components.MovementComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.RectangleComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.components.TileRenderComponent;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;
import uq.deco2800.dangernoodles.prefabs.PlayerEntities;
import uq.deco2800.dangernoodles.systems.AISystem;
import uq.deco2800.dangernoodles.systems.CollisionSystem;

public class AIMovementTest1 {

    public int[][] tileMap = new int[][] {
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

    private void createTerrainRender(World world, double yOffset) {
        int i = tileMap.length;
        int j = tileMap[0].length; // assumes all the same length
        int spriteSize = 40;

        for (int y = 0; y < i; y++) {
            for (int x = 0; x < j; x++) {
                if (tileMap[y][x] == 0) {
                    // build a tile that doesn't collide or display
                } else {
                    // build a tile that does collide and displays
                    world.createEntity().addComponent(new PositionComponent(spriteSize * x, yOffset + spriteSize * y))
                            .addComponent(new RectangleComponent(spriteSize, spriteSize))
                            .addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
                            .addComponent(new FrictionComponent(0.1, true)).addComponent(new GravityComponent(false))
                            .addComponent(new TileRenderComponent(x, y, true));
                }
            }
        }
    }

    /**
     * test turning right
     */
    // *
    @Test
    public void testMoveAI1() {

        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();
        createTerrainRender(testWorld, 500);

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        boolean isAI = true;

        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[0], isAI, availableTeams[0], 1, 0);

        PositionComponent aiPos = testWorld.getComponent(aiPlayer, PositionComponent.class).get();
        InputComponent aiInput = testWorld.getComponent(aiPlayer, InputComponent.class).get();

        aiInput.setX(0); // reset aiInput to something wrong to make it more difficult for the test to pass
        aiPos.setX(320); // set AI's x position
        aiPos.setY(450);

        testAISystem.moveAI(aiPlayer, 1100, "stay", testWorld);
        assertTrue(Double.compare(aiInput.getX(), 1) == 0);

        aiInput.setX(0); // reset aiInput to something wrong to make it more difficult for the test to pass

        testAISystem.moveAI(aiPlayer, 539, "stay", testWorld);
        assertTrue(Double.compare(aiInput.getX(), 1) == 0);

        aiInput.setX(0); // reset aiInput to something wrong to make it more difficult for the test to pass

        testAISystem.moveAI(aiPlayer, 1100, "turn", testWorld);
        assertTrue(Double.compare(aiInput.getX(), 1) == 0);

        aiInput.setX(0); // reset aiInput to something wrong to make it more difficult for the test to pass

        testAISystem.moveAI(aiPlayer, 539, "turn", testWorld);
        assertTrue(Double.compare(aiInput.getX(), 1) == 0);
    }

    /**
     * test turning left
     */
    @Test
    public void testMoveAI2() {

        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();
        createTerrainRender(testWorld, 500);

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        boolean isAI = true;

        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[0], isAI, availableTeams[0], 1, 0);

        PositionComponent aiPos = testWorld.getComponent(aiPlayer, PositionComponent.class).get();
        InputComponent aiInput = testWorld.getComponent(aiPlayer, InputComponent.class).get();

        aiInput.setX(0); // reset aiInput to something wrong to make it more difficult for the test to pass
        aiPos.setX(320); // set AI's x position
        aiPos.setY(450); // set AI's y position

        testAISystem.moveAI(aiPlayer, 0, "stay", testWorld);
        assertTrue(Double.compare(aiInput.getX(), -1) == 0);

        aiInput.setX(0); // reset aiInput to something wrong to make it more difficult for the test to pass

        testAISystem.moveAI(aiPlayer, 238, "stay", testWorld);
        assertTrue(Double.compare(aiInput.getX(), -1) == 0);

        aiInput.setX(0); // reset aiInput to something wrong to make it more difficult for the test to pass

        testAISystem.moveAI(aiPlayer, 0, "turn", testWorld);
        assertTrue(Double.compare(aiInput.getX(), -1) == 0);

        aiInput.setX(0); // reset aiInput to something wrong to make it more difficult for the test to pass

        testAISystem.moveAI(aiPlayer, 238, "turn", testWorld);
        assertTrue(Double.compare(aiInput.getX(), -1) == 0);
    }

    /**
     * test stopping
     */
    @Test
    public void testMoveAI3() {

        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();
        createTerrainRender(testWorld, 500);

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        boolean isAI = true;

        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[0], isAI, availableTeams[0], 1, 0);

        PositionComponent aiPos = testWorld.getComponent(aiPlayer, PositionComponent.class).get();
        InputComponent aiInput = testWorld.getComponent(aiPlayer, InputComponent.class).get();

        aiInput.setX(1); // reset aiInput to something wrong to make it more difficult for the test to pass
        aiPos.setX(750); // set AI's x position
        aiPos.setY(450); // set AI's y position

        testAISystem.moveAI(aiPlayer, 770, "stay", testWorld);
        assertTrue(Double.compare(aiInput.getX(), 0) == 0);

        aiInput.setX(1); // reset aiInput to something wrong to make it more difficult for the test to pass

        testAISystem.moveAI(aiPlayer, 770, "turn", testWorld);
        assertTrue(Double.compare(aiInput.getX(), 0) == 0);
    }

    /**
     * test turning right (edge case)
     */
    @Test
    public void testMoveAI4() {

        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();
        createTerrainRender(testWorld, 500);

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        boolean isAI = true;

        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[0], isAI, availableTeams[0], 1, 0);

        PositionComponent aiPos = testWorld.getComponent(aiPlayer, PositionComponent.class).get();
        MovementComponent aiMovement = testWorld.getComponent(aiPlayer, MovementComponent.class).get();
        InputComponent aiInput = testWorld.getComponent(aiPlayer, InputComponent.class).get();

        aiInput.setX(0); // reset aiInput to something wrong to make it more difficult for the test to pass
        aiPos.setX(367); // set AI's x position
        aiPos.setY(450); // set AI's y position
        aiMovement.setVx(-23.235); // EDGE CASE branch

        testAISystem.moveAI(aiPlayer, 523, "stay", testWorld);
        assertTrue(Double.compare(aiInput.getX(), 1) == 0);

        aiInput.setX(0); // reset aiInput to something wrong to make it more difficult for the test to pass

        testAISystem.moveAI(aiPlayer, 523, "turn", testWorld);
        assertTrue(Double.compare(aiInput.getX(), 1) == 0);
    }

    /**
     * test turning left (edge case)
     */
    @Test
    public void testMoveAI5() {

        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();
        createTerrainRender(testWorld, 500);

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        boolean isAI = true;

        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[0], isAI, availableTeams[0], 1, 0);

        PositionComponent aiPos = testWorld.getComponent(aiPlayer, PositionComponent.class).get();
        MovementComponent aiMovement = testWorld.getComponent(aiPlayer, MovementComponent.class).get();
        InputComponent aiInput = testWorld.getComponent(aiPlayer, InputComponent.class).get();

        aiInput.setX(0); // reset aiInput to something wrong to make it more difficult for the test to pass
        aiPos.setX(367); // set AI's x position
        aiPos.setY(550); // set AI's y position
        aiMovement.setVx(5338.235); // EDGE CASE branch

        testAISystem.moveAI(aiPlayer, 123, "stay", testWorld);
        assertTrue(Double.compare(aiInput.getX(), -1) == 0);

        aiInput.setX(0); // reset aiInput to something wrong to make it more difficult for the test to pass

        testAISystem.moveAI(aiPlayer, 123, "turn", testWorld);
        assertTrue(Double.compare(aiInput.getX(), -1) == 0);
    }

    /**
     * test turning left after collision on the right
     */
    @Test
    public void testMoveAI6() {

        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();
        createTerrainRender(testWorld, 100);
        CollisionSystem testCollisionSystem = new CollisionSystem();

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        boolean isAI = true;

        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[0], isAI, availableTeams[0], 1, 0);
        Entity player = PlayerEntities.createPlayer(testWorld, availableNoodles[0], !isAI, availableTeams[0], 2, 0);

        PositionComponent aiPos = testWorld.getComponent(aiPlayer, PositionComponent.class).get();
        PositionComponent playerPos = testWorld.getComponent(player, PositionComponent.class).get();
        InputComponent aiInput = testWorld.getComponent(aiPlayer, InputComponent.class).get();

        aiInput.setX(1); // reset aiInput to something wrong to make it more difficult for the test to pass
        aiPos.setX(367); // set AI's x position
        aiPos.setY(50);
        playerPos.setX(389);
        playerPos.setY(50);

        testCollisionSystem.run(testWorld, 5, 0.016);

        testAISystem.moveAI(aiPlayer, 800, "stay", testWorld);
        assertTrue(Double.compare(aiInput.getX(), 0) == 0);

        aiInput.setX(1); // reset aiInput to something wrong to make it more difficult for the test to pass

        testAISystem.moveAI(aiPlayer, 800, "turn", testWorld);
        assertTrue(Double.compare(aiInput.getX(), -1) == 0);
    }

    /**
     * test turning right after collision on the left
     */
    @Test
    public void testMoveAI7() {

        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();
        createTerrainRender(testWorld, 782);
        CollisionSystem testCollisionSystem = new CollisionSystem();

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        boolean isAI = true;

        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[0], isAI, availableTeams[0], 1, 0);
        Entity player = PlayerEntities.createPlayer(testWorld, availableNoodles[0], !isAI, availableTeams[0], 2, 0);

        PositionComponent aiPos = testWorld.getComponent(aiPlayer, PositionComponent.class).get();
        PositionComponent playerPos = testWorld.getComponent(player, PositionComponent.class).get();
        InputComponent aiInput = testWorld.getComponent(aiPlayer, InputComponent.class).get();

        aiInput.setX(-1); // reset aiInput to something wrong to make it more difficult for the test to pass
        aiPos.setX(582); // set AI's x position
        aiPos.setY(732);
        playerPos.setX(532);
        playerPos.setY(732);

        testCollisionSystem.run(testWorld, 5, 0.016);

        testAISystem.moveAI(aiPlayer, 232.2, "stay", testWorld);
        assertTrue(Double.compare(aiInput.getX(), 0) == 0);

        aiInput.setX(1); // reset aiInput to something wrong to make it more difficult for the test to pass

        testAISystem.moveAI(aiPlayer, 232.2, "turn", testWorld);
        assertTrue(Double.compare(aiInput.getX(), 1) == 0);
    }

    /**
     * test turning left after reaching the right most edge of a terrain
     */
    @Test
    public void testMoveAI8() {

        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();
        CollisionSystem testCollisionSystem = new CollisionSystem();

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        boolean isAI = true;

        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[0], isAI, availableTeams[0], 1, 0);
        testWorld.createEntity().addComponent(new PositionComponent(321, 792))
                .addComponent(new RectangleComponent(500, 100))
                .addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
                .addComponent(new SpriteComponent(500, 100, "resources/dirt.png"))
                .addComponent(new TileRenderComponent(321, 792));

        PositionComponent aiPos = testWorld.getComponent(aiPlayer, PositionComponent.class).get();
        InputComponent aiInput = testWorld.getComponent(aiPlayer, InputComponent.class).get();

        aiPos.setX(321 + 495);
        aiPos.setY(780);
        aiPos.setNextX(321 + 496); // set AI's x position
        aiPos.setNextY(791);

        testCollisionSystem.run(testWorld, 5, 0.016);

        aiInput.setX(1); // reset aiInput to something wrong to make it more difficult for the test to pass
        testAISystem.moveAI(aiPlayer, 1000, "stay", testWorld);
        assertTrue(Double.compare(aiInput.getX(), 0) == 0);

        aiInput.setX(1); // reset aiInput to something wrong to make it more difficult for the test to pass
        testAISystem.moveAI(aiPlayer, 1000, "turn", testWorld);
        assertTrue(Double.compare(aiInput.getX(), -1) == 0);
    }

    /**
     * test turning right after reaching the left most edge of a terrain
     */
    @Test
    public void testMoveAI9() {

        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();
        CollisionSystem testCollisionSystem = new CollisionSystem();

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        boolean isAI = true;

        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[0], isAI, availableTeams[0], 1, 0);
        testWorld.createEntity().addComponent(new PositionComponent(321, 792))
                .addComponent(new RectangleComponent(500, 100))
                .addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
                .addComponent(new SpriteComponent(500, 100, "resources/dirt.png"))
                .addComponent(new TileRenderComponent(321, 792));

        PositionComponent aiPos = testWorld.getComponent(aiPlayer, PositionComponent.class).get();
        InputComponent aiInput = testWorld.getComponent(aiPlayer, InputComponent.class).get();

        aiPos.setX(321 - 45);
        aiPos.setY(792);
        aiPos.setNextX(321 - 46); // set AI's x position
        aiPos.setNextY(792);

        testCollisionSystem.run(testWorld, 5, 0.016);

        aiInput.setX(-1); // reset aiInput to something wrong to make it more difficult for the test to pass
        testAISystem.moveAI(aiPlayer, 125, "stay", testWorld);
        assertTrue(Double.compare(aiInput.getX(), 0) == 0);

        aiInput.setX(1); // reset aiInput to something wrong to make it more difficult for the test to pass
        testAISystem.moveAI(aiPlayer, 125, "turn", testWorld);
        assertTrue(Double.compare(aiInput.getX(), 1) == 0);
    }

}
