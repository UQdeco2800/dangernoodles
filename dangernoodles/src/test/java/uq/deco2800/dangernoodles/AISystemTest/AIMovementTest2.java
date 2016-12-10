package uq.deco2800.dangernoodles.AISystemTest;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uq.deco2800.dangernoodles.components.AIComponent;
import uq.deco2800.dangernoodles.components.CollisionComponent;
import uq.deco2800.dangernoodles.components.FrictionComponent;
import uq.deco2800.dangernoodles.components.GravityComponent;
import uq.deco2800.dangernoodles.components.InputComponent;
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

public class AIMovementTest2 {

    public int[][] tileMap = new int[][] {
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

    private void createTerrainRender(World world, double yOffset) {
        int i = tileMap.length;
        int j = tileMap[0].length; // assumes all the same length
        int spriteSize = 25;

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
                            .addComponent(new TileRenderComponent(x, y, true))
                            .addComponent(new SpriteComponent(spriteSize, spriteSize, "terrain/dirt.png"));
                }
            }
        }
    }

    @Test
    public void testMoveBackAndForth1() {
        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();
        createTerrainRender(testWorld, 500);

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        boolean isAI = true;

        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[0], isAI, availableTeams[0], 1, 0);
        PositionComponent aiPos = testWorld.getComponent(aiPlayer, PositionComponent.class).get();
        InputComponent aiInput = testWorld.getComponent(aiPlayer, InputComponent.class).get();

        aiPos.setX(300);
        aiPos.setY(450);

        // top if branch and second if branch
        aiInput.setX(0);
        testAISystem.moveBackAndForth(aiPlayer, 100, testWorld);
        assertTrue(Double.compare(aiInput.getX(), -1) == 0);

    }

    @Test
    public void testMoveBackAndForth2() {
        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();
        createTerrainRender(testWorld, 500);

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        boolean isAI = true;

        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[0], isAI, availableTeams[0], 1, 0);
        PositionComponent aiPos = testWorld.getComponent(aiPlayer, PositionComponent.class).get();
        InputComponent aiInput = testWorld.getComponent(aiPlayer, InputComponent.class).get();

        aiPos.setX(300);
        aiPos.setY(450);

        // top if branch second condition and second if branch
        aiInput.setX(1);
        testAISystem.moveBackAndForth(aiPlayer, 100, testWorld);
        assertTrue(Double.compare(aiInput.getX(), -1) == 0);
    }

    @Test
    public void testMoveBackAndForth3() {
        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();
        createTerrainRender(testWorld, 500);

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        boolean isAI = true;

        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[0], isAI, availableTeams[0], 1, 0);
        PositionComponent aiPosition = testWorld.getComponent(aiPlayer, PositionComponent.class).get();
        InputComponent aiInput = testWorld.getComponent(aiPlayer, InputComponent.class).get();

        aiPosition.setX(237);
        aiPosition.setY(450);

        // destination == null and input == -1
        aiInput.setX(-1);
        testAISystem.moveBackAndForth(aiPlayer, 100, testWorld);
        assertTrue(Double.compare(aiInput.getX(), -1) == 0);
    }

    @Test
    public void testMoveBackAndForth4() {
        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();
        createTerrainRender(testWorld, 500);

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        boolean isAI = true;

        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[0], isAI, availableTeams[0], 1, 0);

        InputComponent aiInput = testWorld.getComponent(aiPlayer, InputComponent.class).get();
        PositionComponent aiPosition = testWorld.getComponent(aiPlayer, PositionComponent.class).get();
        AIComponent aiComponent = testWorld.getComponent(aiPlayer, AIComponent.class).get();
        PositionComponent aiDestination = new PositionComponent(0, 0);

        // destination != null and AI has reached destination and input is 0
        aiPosition.setX(127);
        aiPosition.setY(450);
        aiDestination.setX(127);
        aiComponent.setDestination(aiDestination);
        aiInput.setX(0);

        testAISystem.moveBackAndForth(aiPlayer, 100, testWorld);
        assertTrue(Double.compare(aiInput.getX(), -1) == 0);
    }

    @Test
    public void testMoveBackAndForth5() {
        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();
        createTerrainRender(testWorld, 500);

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        boolean isAI = true;

        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[0], isAI, availableTeams[0], 1, 0);

        InputComponent aiInput = testWorld.getComponent(aiPlayer, InputComponent.class).get();
        PositionComponent aiPosition = testWorld.getComponent(aiPlayer, PositionComponent.class).get();
        AIComponent aiComponent = testWorld.getComponent(aiPlayer, AIComponent.class).get();
        PositionComponent aiDestination = new PositionComponent(0, 0);

        // top if branch and second else branch, second condition, turn right
        aiPosition.setX(530);
        aiPosition.setY(450);
        aiDestination.setX(250);
        aiComponent.setDestination(aiDestination);

        aiInput.setX(1);
        testAISystem.moveBackAndForth(aiPlayer, 700, testWorld);
        assertTrue(Double.compare(aiInput.getX(), 1) == 0);
    }

    @Test
    public void testMoveBackAndForth6() {
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
        AIComponent aiComponent = testWorld.getComponent(aiPlayer, AIComponent.class).get();
        PositionComponent aiDestination = new PositionComponent(0, 0);

        aiComponent.setDestination(aiDestination);

        // top if branch and second else branch, first condition, turn right
        aiPos.setX(389);
        aiPos.setY(50);
        playerPos.setX(367); // previously not moving; now turn to the left and bump
        playerPos.setY(50);

        aiInput.setX(0);

        testCollisionSystem.run(testWorld, 5, 0.016);

        testAISystem.moveBackAndForth(aiPlayer, 100, testWorld);
        assertTrue(Double.compare(aiInput.getX(), 1) == 0);
    }

    @Test
    public void testMoveBackAndForth7() {
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
        AIComponent aiComponent = testWorld.getComponent(aiPlayer, AIComponent.class).get();
        PositionComponent aiDestination = new PositionComponent(0, 0);

        aiComponent.setDestination(aiDestination);

        // top else branch and second else branch, first condition, turn left
        aiPos.setX(367);
        aiPos.setY(50);
        playerPos.setX(389); // bumped into him on the right
        playerPos.setY(50);

        aiInput.setX(-1); // previously moving towards left; now turn to the right and bump

        testCollisionSystem.run(testWorld, 5, 0.016);

        testAISystem.moveBackAndForth(aiPlayer, 100, testWorld);
        assertTrue(Double.compare(aiInput.getX(), -1) == 0);
    }

    @Test
    public void testMoveBackAndForth8() {
        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();
        CollisionSystem testCollisionSystem = new CollisionSystem();

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        boolean isAI = true;

        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[0], isAI, availableTeams[0], 1, 0);
        testWorld.createEntity().addComponent(new PositionComponent(783, 792))
                .addComponent(new TileRenderComponent(783, 792, true)).addComponent(new RectangleComponent(500, 100))
                .addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
                .addComponent(new FrictionComponent(0.1, true));

        PositionComponent aiPos = testWorld.getComponent(aiPlayer, PositionComponent.class).get();
        InputComponent aiInput = testWorld.getComponent(aiPlayer, InputComponent.class).get();
        AIComponent aiComponent = testWorld.getComponent(aiPlayer, AIComponent.class).get();
        PositionComponent aiDestination = new PositionComponent(0, 0);

        aiComponent.setDestination(aiDestination);

        // top if branch and second else branch, third condition, turn left
        aiPos.setX(783 + 500 - 10); // right most edge
        aiPos.setY(792);

        aiInput.setX(-1); // previously moving to the left; now turn right and face edge

        testCollisionSystem.run(testWorld, 5, 0.016);

        testAISystem.moveBackAndForth(aiPlayer, 100, testWorld);
        assertTrue(Double.compare(aiInput.getX(), -1) == 0);
    }

    @Test
    public void testMoveBackAndForth9() {
        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();
        CollisionSystem testCollisionSystem = new CollisionSystem();

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        boolean isAI = true;

        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[0], isAI, availableTeams[0], 1, 0);
        testWorld.createEntity().addComponent(new PositionComponent(783, 792))
                .addComponent(new TileRenderComponent(783, 792, true)).addComponent(new RectangleComponent(500, 100))
                .addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
                .addComponent(new FrictionComponent(0.1, true));

        PositionComponent aiPos = testWorld.getComponent(aiPlayer, PositionComponent.class).get();
        InputComponent aiInput = testWorld.getComponent(aiPlayer, InputComponent.class).get();
        AIComponent aiComponent = testWorld.getComponent(aiPlayer, AIComponent.class).get();
        PositionComponent aiDestination = new PositionComponent(0, 0);

        aiComponent.setDestination(aiDestination);

        // top if branch and second else branch, third condition, turn right
        aiPos.setX(783 - 10);
        aiPos.setY(792);

        aiInput.setX(1); // previously moving to the right; now turn left and face edge

        testCollisionSystem.run(testWorld, 5, 0.016);

        testAISystem.moveBackAndForth(aiPlayer, 100, testWorld);
        assertTrue(Double.compare(aiInput.getX(), 1) == 0);
    }
}
