package uq.deco2800.dangernoodles.systems;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import org.mockito.Mockito;
import uq.deco2800.dangernoodles.FrameHandler;
import uq.deco2800.dangernoodles.Game;
import uq.deco2800.dangernoodles.components.NameComponent;
import uq.deco2800.dangernoodles.components.console.ConsoleDisplayComponent;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.components.stats.DamageComponent;
import uq.deco2800.dangernoodles.components.stats.ManaComponent;
import uq.deco2800.dangernoodles.components.weather.WeatherComponent;
import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.inputhandlers.KeyboardHandler;
import uq.deco2800.dangernoodles.inputhandlers.MouseHandler;
import uq.deco2800.dangernoodles.components.stats.HealthComponent;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;
import uq.deco2800.dangernoodles.prefabs.PlayerEntities;

/**
 * Created by Jason on 19/10/16.
 */
public class ConsoleDisplaySystemTest {

    private Canvas canvas = Mockito.mock(Canvas.class);
    private StackPane stackPane = new StackPane();

    private KeyboardHandler keyboardHandler = new KeyboardHandler();
    private MouseHandler mouseHandler = new MouseHandler(canvas);

    //private Game game = new Game(keyboardHandler, mouseHandler, canvas, stackPane);
    private Game game = Mockito.mock(Game.class);

    private GraphicsContext context = canvas.getGraphicsContext2D();
    private FrameHandler framehandler = new FrameHandler(context, game);
    private ConsoleDisplaySystem consoleDisplaySystem = new ConsoleDisplaySystem(framehandler);

    @Test
    public void runTest() {
        World testworld = new World(0, 0);
        testworld.addSystem(consoleDisplaySystem, 1);
        testworld.createEntity()
                .addComponent(new NameComponent("NAME"))
                .addComponent(new DamageComponent(10));
        testworld.createEntity().addComponent(new ConsoleDisplayComponent());
        consoleDisplaySystem.run(testworld, 0, 0);
        assertEquals(framehandler.getCommand(), null);
    }



    @Test
    public void checkCommandTest() {
        World testworld = new World(0, 0);
        testworld.addSystem(consoleDisplaySystem, 1);
        testworld.createEntity()
                .addComponent(new NameComponent("dolan"))
                .addComponent(new DamageComponent(10));
        testworld.createEntity().addComponent(new ConsoleDisplayComponent());
        framehandler.setCommand("THIS IS THE COMMAND");
        assertEquals(framehandler.getCommand(), "THIS IS THE COMMAND");
        consoleDisplaySystem.run(testworld, 0, 0);
        assertEquals(framehandler.getCommand(), null);

    }

    @Test
    public void applyDamageTest() {
        World testworld = new World(0, 0);
        testworld.addSystem(consoleDisplaySystem, 1);

        testworld.createEntity()
                .addComponent(new NameComponent("dolan"))
                .addComponent(new DamageComponent(10));
        testworld.createEntity().addComponent(new ConsoleDisplayComponent());
        //Test damage is updated.
        framehandler.setCommand("damage dolan 10");
        assertEquals(framehandler.getCommand(), "damage dolan 10");
        consoleDisplaySystem.run(testworld, 0, 0);
        assertEquals(framehandler.getCommand(), null);

        framehandler.setCommand("damage dolan 20");
        consoleDisplaySystem.run(testworld, 0, 0);
        assertEquals(framehandler.getCommand(), null);

        framehandler.setCommand("damage badName 10");
        consoleDisplaySystem.run(testworld, 0, 0);
        assertEquals(framehandler.getCommand(), null);

        testworld.createEntity().addComponent(new ConsoleDisplayComponent());
        testworld.getComponents(ConsoleDisplayComponent.class).get(0).setDisplaying(false);
        framehandler.setCommand("damage badName 10");
        consoleDisplaySystem.run(testworld, 0, 0);
        assertEquals(framehandler.getCommand(), null);
    }

    /**
     * Created by Team Mighty Ducks
     */
    @Test
    public void duckRainTest(){
        World testWorld = new World(0, 0);
        testWorld.addSystem(consoleDisplaySystem, 1);

        testWorld.createEntity()
                .addComponent(new WeatherComponent());

        testWorld.createEntity().addComponent(new ConsoleDisplayComponent());

        // Test rain is set to ducks.
        framehandler.setCommand("makeitrain");
        assertEquals(framehandler.getCommand(), "makeitrain");
        consoleDisplaySystem.run(testWorld, 0, 0);
        assertTrue(WeatherSystem.getDucksModeActive());

        // Test raining ducks is removed
        framehandler.setCommand("duckduckgo");
        assertEquals(framehandler.getCommand(), "duckduckgo");
        consoleDisplaySystem.run(testWorld, 0, 0);
        assertFalse(WeatherSystem.getDucksModeActive());
    }

    @Test
    public void spaceModeTest(){
        World testWorld = new World(0, 0);
        testWorld.addSystem(consoleDisplaySystem, 1);

        testWorld.createEntity()
                .addComponent(new WeatherComponent());

        testWorld.createEntity().addComponent(new ConsoleDisplayComponent());

        // Test rain is set to ducks.
        framehandler.setCommand("spacemode");
        assertEquals(framehandler.getCommand(), "spacemode");
        consoleDisplaySystem.run(testWorld, 0, 0);
        assertTrue(WeatherSystem.getSpaceModeActive());

        // Test raining ducks is removed
        framehandler.setCommand("normalweather");
        assertEquals(framehandler.getCommand(), "normalweather");
        consoleDisplaySystem.run(testWorld, 0, 0);
        assertFalse(WeatherSystem.getDucksModeActive());
        assertFalse(WeatherSystem.getSpaceModeActive());
    }

    @Test
    public void suddenDeathTest(){
        World testWorld = new World(0, 0);
        testWorld.addSystem(consoleDisplaySystem, 1);
        testWorld.createEntity().addComponent(new HealthComponent(100));
        testWorld.createEntity().addComponent(new HealthComponent(100));
        framehandler.setCommand("suddendeath");
        assertEquals(framehandler.getCommand(), "suddendeath");
        consoleDisplaySystem.run(testWorld, 0, 0);
        for (ComponentMap cm : testWorld.getIterator(HealthComponent.class)) {
            HealthComponent health = cm.get(HealthComponent.class);
            assertEquals(health.getHealth(), 5);
        }
    }

    @Test
    public void armageddonTest() {
        World testWorld = new World(0, 0);
        testWorld.addSystem(consoleDisplaySystem, 1);
        testWorld.createEntity().addComponent(new HealthComponent(100));
        testWorld.createEntity().addComponent(new HealthComponent(100));
        framehandler.setCommand("armageddon");
        assertEquals(framehandler.getCommand(), "armageddon");
        consoleDisplaySystem.run(testWorld, 0, 0);
        for (ComponentMap cm : testWorld.getIterator(HealthComponent.class)) {
            HealthComponent health = cm.get(HealthComponent.class);
            assertEquals(health.getHealth(), 1);
        }
    }

    //@Test
    public void manaCheatTest(){
        World testworld = new World(0, 0);
        testworld.addSystem(consoleDisplaySystem, 1);
        testworld.createEntity().addComponent(new ConsoleDisplayComponent());
        Entity player = PlayerEntities.createPlayer(testworld, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);
        // Test mana is not able to be reduced.
        framehandler.setCommand("infinitemana");
        assertEquals(framehandler.getCommand(), "infinitemana");
        consoleDisplaySystem.run(testworld, 0, 0);

        ManaComponent mana = testworld.getComponent(player, ManaComponent.class).get();
        ManaComponent reducedMana = testworld.getComponent(player, ManaComponent.class).get();
        reducedMana.setMana(reducedMana.getMana()- 10);

        assertNotEquals(testworld.getComponent(player, ManaComponent.class).get().getMana(), reducedMana.getMana());

        // Reduce Mana
        mana.setMana(mana.getMana() - 10);
        assertNotEquals(testworld.getComponent(player, ManaComponent.class).get().getMana(), reducedMana.getMana());

        // Test mana is able to be reduced.
        framehandler.setCommand("normalmana");
        assertEquals(framehandler.getCommand(), "normalmana");
        consoleDisplaySystem.run(testworld, 0, 0);
        assertNotEquals(testworld.getComponent(player, ManaComponent.class).get().getMana(), reducedMana.getMana());

        // Reduce Mana
        mana.setMana(mana.getMana() - 10);
        assertEquals(testworld.getComponent(player, ManaComponent.class).get().getMana(), reducedMana.getMana());
    }
}