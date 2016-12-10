package uq.deco2800.dangernoodles;

import java.util.List;
import java.util.Random;

import javafx.scene.canvas.Canvas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.components.weather.WeatherComponent;
import uq.deco2800.dangernoodles.components.weather.WindComponent;
import uq.deco2800.dangernoodles.configparser.TerrainParser;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.inputhandlers.KeyboardHandler;
import uq.deco2800.dangernoodles.inputhandlers.MouseHandler;
import uq.deco2800.dangernoodles.prefabs.*;
import uq.deco2800.dangernoodles.systems.*;
import uq.deco2800.dangernoodles.systems.weapons.ExplosionSystem;
import uq.deco2800.dangernoodles.systems.weapons.ProjectileSystem;
import uq.deco2800.dangernoodles.systems.weapons.WeaponPowerBarSystem;
import uq.deco2800.dangernoodles.systems.weapons.WeaponSystem;
import uq.deco2800.dangernoodles.weapons.WeaponDefinition;
import uq.deco2800.dangernoodles.weapons.WeaponDefinitionList;
import uq.deco2800.dangernoodles.windowhandlers.GameLobbyScreenHandler;
import uq.deco2800.dangernoodles.windowhandlers.WindowHandler;
import uq.deco2800.dangernoodles.systems.DeathSystem;
import javafx.scene.layout.StackPane;

/**
 * 30/7/16 Created by Timothy Ryan Hadwen for deco2800-2016-dangernoodles
 * <p>
 * Game handles the entities and also the turn based nature of the game
 *
 * @author Timothy Hadwen
 */
public class Game {
    private static final String CLASS = Game.class.getName();
    private static final Logger LOGGER = LoggerFactory.getLogger(CLASS);

    private World world;
    private FrameHandler frameHandler;
    private StaticFrameHandler staticHandler;
    private KeyboardHandler keyHandler;
    private MouseHandler mouseHandler;
    private Canvas canvas;
    private StackPane stackPane;

    // Weapon Definition List
    private WeaponDefinitionList weaponDefinitions;
    private TeamEnum winner;


    private static final int INPUT_PRIORITY = 0;
    private static final int COLLISION_PRIORITY = 5;
    private static final int WEAPONS_PRIORITY = 50;
    private static final int RENDERING_PRIORITY = 100;

    private int[] positionXs= {300, 500, 400, 700, 200};

    private static final int SPRITE_SIZE = 25;

    private double gameTime = 0;

    private static boolean isMultiplayer;

    public Game(KeyboardHandler keyHandler, MouseHandler mouseHandler, Canvas canvas, StackPane stackPane) {
        this.keyHandler = keyHandler;
        this.mouseHandler = mouseHandler;
        this.canvas = canvas;
        this.stackPane = stackPane;
        this.world = new World(canvas.getWidth(), canvas.getHeight());
        this.weaponDefinitions = new WeaponDefinitionList();

        //Set current winner to be a special "null value" team
        this.winner = TeamEnum.TEAM_NULL;
        tileMap = new TileMap().returnTileMap();
        addTestEntities();
        createTerrainRender();
    }

    /**
     * Sets the type of game to multiplayer or single player.
     *
     * @param isMultiplayer
     *         boolean representing the type of game
     */
    public static void setMultiplayer(boolean isMultiplayer) {
        Game.isMultiplayer = isMultiplayer;
    }

    public static boolean isMultiplayer() {
        return isMultiplayer;
    }

    public void addClientInputSystems(Canvas canvas) {
        this.world.addSystem(new KeyboardSystem(keyHandler), INPUT_PRIORITY);
        this.world.addSystem(new MouseSystem(mouseHandler, frameHandler, canvas), INPUT_PRIORITY);
    }

    /**
     * Creates systems for managing weapons and adds them to the world.
     */
    private void addWeaponsSystems() {
        // Should happen in this order, near the end of the priority list
        this.world.addSystem(new ExplosionSystem(), WEAPONS_PRIORITY - 2);
        this.world.addSystem(new ProjectileSystem(), WEAPONS_PRIORITY - 1);
        this.world.addSystem(new WeaponSystem(), WEAPONS_PRIORITY);
    }

    /**
     * Creates systems for simulating events and adds them to the world. Adds
     * the AI system only if the game is not multiplayer.
     *
     * @param isMultiplayer
     *         boolean representing if the game is multiplayer
     */
    public void addSimulationSystems(boolean isMultiplayer) {
        this.world.addSystem(new MovementSystem(), COLLISION_PRIORITY - 1);
        this.world.addSystem(new GravitySystem(), COLLISION_PRIORITY - 1);
        this.world.addSystem(new CollisionSystem(), COLLISION_PRIORITY);
        this.world.addSystem(new MovementSystemPhase2(), COLLISION_PRIORITY + 1);
        this.world.addSystem(new CursorSystem(), COLLISION_PRIORITY + 2);
        this.world.addSystem(new EffectBoxSystem(), COLLISION_PRIORITY + 3);

        this.world.addSystem(new EffectSystem(), 5);
        this.world.addSystem(new HealthSystem(), 8);
        this.world.addSystem(new ManaSystem(), 8);
        this.world.addSystem(new DamageSystem(), 8);

        this.world.addSystem(new WeatherMovementSystem(), 16);
        if (!isMultiplayer) {
            this.world.addSystem(new AISystem(), 11);
        } else {
            this.world.addSystem(new NetworkSystem(), 30);
        }
        this.world.addSystem(new NoodleSystem(), 14);
        this.world.addSystem(new CameraSystem(keyHandler), 15);
        this.world.addSystem(new DeathSystem(), 16);
        //this.world.addSystem(new TerrainSystem(tileMap), 12); Deprecated

        addWeaponsSystems();
    }

    /**
     * Creates all systems that render on the canvas and adds them to the world.
     * Rendering systems are given a priority after simulation systems. All
     * rendering system should have a similar rendering priority. Some systems
     * priority have been adjusted to process Mouse Events appropriately.
     *
     * @param handler
     *         Frame handler to render non-static objects onto the canvas.
     * @param staticHandler
     *         Frame handler to render static objects onto the canvas.
     * @param handler
     *         Frame handler to render non-static objects onto the canvas.
     * @param staticHandler
     *         Frame handler to render static objects onto the canvas.
     */
    public void addRenderSystems(FrameHandler handler, StaticFrameHandler staticHandler) {
        this.frameHandler = handler;
        this.staticHandler = staticHandler;

        this.world.addSystem(new FrameSystem(handler, staticHandler), WEAPONS_PRIORITY - 7);
        this.world.addSystem(new MinimapSystem(staticHandler), RENDERING_PRIORITY);
        this.world.addSystem(new SpriteSystem(handler, staticHandler), RENDERING_PRIORITY);
        this.world.addSystem(new PlayerNameSystem(handler), RENDERING_PRIORITY);
        this.world.addSystem(new EffectDisplaySystem(staticHandler), RENDERING_PRIORITY);
        this.world.addSystem(new StatusBarSystem(handler, staticHandler), RENDERING_PRIORITY);
        this.world.addSystem(new ParachuteSystem(), RENDERING_PRIORITY);
        this.world.addSystem(new WeaponPowerBarSystem(frameHandler), RENDERING_PRIORITY);
        this.world.addSystem(new ShopDisplaySystem(staticHandler, mouseHandler), WEAPONS_PRIORITY - 5);
        this.world.addSystem(new SettingDisplaySystem(staticHandler, mouseHandler, keyHandler), WEAPONS_PRIORITY - 6);
        this.world.addSystem(new InventoryDisplaySystem(staticHandler, mouseHandler), RENDERING_PRIORITY);
        this.world.addSystem(new ClockSystem(staticHandler), RENDERING_PRIORITY);
        this.world.addSystem(new TurnSystem(world, handler, staticHandler, winner), RENDERING_PRIORITY);
        this.world.addSystem(new WeatherSystem(stackPane, staticHandler, false), RENDERING_PRIORITY);
        this.world.addSystem(new MinimapBoxSystem(handler, staticHandler), RENDERING_PRIORITY);
        this.world.addSystem(new ConsoleDisplaySystem(frameHandler), RENDERING_PRIORITY);
        this.world.addSystem(new InstructionsSystem(this.staticHandler, mouseHandler, keyHandler), RENDERING_PRIORITY);
    }

    /**
     * Returns the game world
     *
     * @return world representing the game
     */
    public World getWorld() {
        return world;
    }

    /**
     * Game tick handler
     *
     * @param dt
     *         - delta time - change in time
     */
    void onTick(double dt) {
        List<PauseComponent> ps = world.getComponents(PauseComponent.class);
        if (ps == null || !ps.get(0).isPaused()) {
            gameTime += dt;
            world.process(gameTime, dt);
            this.frameHandler.swapCurrentRenderLists();
        } else {
            // If the game is paused, SettingDisplaySystem's static run
            // method will run.
            if (ps.get(0).getInstructions()) {
                try {
                    InstructionsSystem.staticRun(world);
                } catch (InterruptedException e) {
                    LOGGER.info("Interrupted Exception is found");
                    Thread.currentThread().interrupt();
                }
            } else {
                SettingDisplaySystem.staticRun(world);
            }
        }

        if (winner.getTeamId() != -1) {
            WindowHandler handler = (WindowHandler) canvas.getUserData();
            handler.endGame(winner);
        }

        this.staticHandler.swapCurrentRenderLists();
    }

    /**
     * Adds a team of Noodle entities to the game.
     *
     * @param teamId
     *         Team's unique integer identifier (starting from 0)
     * @param teamName
     *         TeamEnum of team
     * @param playerTypes
     *         List of each player in team
     * @param isAi
     *         Ai flag
     */
    public void addTeam(int teamId, TeamEnum teamName, List<NoodleEnum> playerTypes, boolean isAi) {
        WeaponDefinition weaponDef = world.getWeaponDefinitions().getWeaponByID(2);
        int noodleWidth = 100;
        int mapLength = tileMap[0].length * SPRITE_SIZE;
        int startingPosition; // Noodle's spawn coords
        for (int i = 0; i < playerTypes.size(); i++) {
            startingPosition = 5 + teamId * 400 + i * noodleWidth; // group by team offset by 5px
            // spread teams as far apart as map will allow
            int fact = teamId < 2 ? 0 : 1;
            startingPosition +=
                    ((teamId % 2) * (mapLength - noodleWidth * (playerTypes.size() - i) - startingPosition));
            startingPosition -= fact * 4 * noodleWidth; // adjust for overlap
            Entity player = PlayerEntities.createPlayer(this.world, playerTypes.get(i), isAi,
                    teamName, teamId, startingPosition);
            WeaponEntities.createWeapon(world, weaponDef, player);
        }

    }

    /**
     * Adds various test entities to the world, including console, settings
     * menu, shop, and weather components.
     */
    private void addTestEntities() {
        Console.createConsoleEntity(world);
        SettingInventory.createInventory(world, 375, 20, 375, 150);
        SettingsMenu.createPause(world);
        GameShop.createShop(this.world);

        this.world.createEntity().addComponent(new NameComponent("Cursor")).addComponent(new PositionComponent(0, 0))
                .addComponent(new RectangleComponent(30, 30))
                .addComponent(new CollisionComponent(false, CollisionComponent.shape.RECTANGLE))
                .addComponent(new CursorComponent());

        if (isMultiplayer) {
            List<String> playerNames = ClientManager.getClientManager()
                    .getRestClient()
                    .getCurrentLobby()
                    .getPlayerIDs();
            TeamEnum[] availableTeams = TeamEnum.values();
            NoodleEnum[] availableNoodles = NoodleEnum.values();
            int positionY = 300;
            WeaponDefinition weaponDef = this.weaponDefinitions.getWeaponByID(1);
            for (int i = 0; i < playerNames.size(); i++) {
                Entity player = PlayerEntities.createEntity(world, playerNames.get(i),
                        positionXs[i], positionY, availableNoodles[i], i,
                        availableTeams[i]);
                WeaponEntities.createWeapon(world, weaponDef, player);
            }
        }

        // Add wind and weather to the world
        this.world.createEntity().addComponent(new WindComponent());
        this.world.createEntity().addComponent(new WeatherComponent());
    }

    /**
     * Returns the Keyboard handler in the game world which is used to to
     * retrieve key strokes. Key strokes are used in FrameHandler to control the
     * camera.
     *
     * @return Keyboard handler that is used to manipulate camera directions
     */
    public KeyboardHandler getCamHandler() {
        return this.keyHandler;
    }

    private static final String[] maps = new String[]{"resources/configs/terrain_duck.xml",
            "resources/configs/terrain_map2.xml",
            "resources/configs/terrain_castle.xml",
            "resources/configs/default_terrain.xml",
            "resources/configs/terrain_map1.xml"};


    private static int randomIndex = new Random().nextInt(maps.length);

    // creates terrain tilemap for use in TerrainSystem and TileRenderSystem
    private static int[][] tileMap;

    /**
     * Generates terrain
     */
    private void createTerrainRender() {
        boolean spaceMode = false;
        boolean underwaterMode = false;
        int i = tileMap.length;
        int j = tileMap[0].length; // assumes all the same length
        for (int y = 0; y < i; y++) {
            for (int x = 0; x < j; x++) {
                if (!spaceMode && !underwaterMode) {
                    if (tileMap[y][x] == 0) {
                        // build a tile that doesn't collide or display
                    } else if (tileMap[y][x] == 1) {
                        // build a tile that does collide and displays
                        TerrainPrefabs.createDirt(world, x, y, SPRITE_SIZE);

                    } else if (tileMap[y][x] == 2) {
                        TerrainPrefabs.createGrass(world, x, y, SPRITE_SIZE);
                    } else if (tileMap[y][x] == 3) {
                        TerrainPrefabs.createRock(world, x, y, SPRITE_SIZE);
                    } else if (tileMap[y][x] == 4) {
                        TerrainPrefabs.createBaseRock(world, x, y, SPRITE_SIZE);
                    } else if (tileMap[y][x] == 5) {
                        TerrainPrefabs.createSand(world, x, y, SPRITE_SIZE);
                    } else if (tileMap[y][x] == 6) {
                        TerrainPrefabs.createHardRock(world, x, y, SPRITE_SIZE);
                    } else if (tileMap[y][x] == 7) {
                        TerrainPrefabs.createDeepDirt(world, x, y, SPRITE_SIZE);
                    } else if (tileMap[y][x] == 8) {
                        TerrainPrefabs.createDeepGrass(world, x, y, SPRITE_SIZE);
                    } else if (tileMap[y][x] == 9) {
                        TerrainPrefabs.createWater(world, x, y, SPRITE_SIZE);
                    } else {
                        TerrainPrefabs.createDirt(world, x, y, SPRITE_SIZE);
                    }
                } else if (spaceMode) {
                    if (tileMap[y][x] == 0) {
                        // build a tile that doesn't collide or display
                    } else if (tileMap[y][x] == 1) {
                        TerrainPrefabs.createSpaceRock(world, x, y, SPRITE_SIZE);
                    } else {
                        TerrainPrefabs.createMoonRock(world, x, y, SPRITE_SIZE);
                    }
                } else if (underwaterMode) {
                    TerrainPrefabs.createUnderWaterRock(world, x, y, SPRITE_SIZE);
                }
            }
        }
    }

    private static class TileMap {
        public int[][] returnTileMap() {
            LOGGER.error("CURRENT MULTIPLAYER MODE: {}", isMultiplayer);
            if (isMultiplayer) {
                return new TerrainParser("resources/configs/terrain_map2.xml").getTerrainArray();
            } else {
                return new TerrainParser(maps[randomIndex]).getTerrainArray();
            }
        }
    }

}