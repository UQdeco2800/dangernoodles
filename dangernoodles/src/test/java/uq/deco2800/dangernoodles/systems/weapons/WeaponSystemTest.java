package uq.deco2800.dangernoodles.systems.weapons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import uq.deco2800.dangernoodles.FrameHandler;
import uq.deco2800.dangernoodles.components.CursorComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.components.weapons.ProjectileComponent;
import uq.deco2800.dangernoodles.components.weapons.WeaponComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;
import uq.deco2800.dangernoodles.prefabs.PlayerEntities;
import uq.deco2800.dangernoodles.prefabs.WeaponEntities;
import uq.deco2800.dangernoodles.systems.weapons.WeaponSystem;
import uq.deco2800.dangernoodles.weapons.WeaponDefinition;
import uq.deco2800.dangernoodles.weapons.WeaponDefinitionList;

public class WeaponSystemTest {
    

    @Test
    public void TestInitialiseWithNoWeapons() {
        World world = new World(1000, 1000);
        
        WeaponSystem weaponSystem = new WeaponSystem();
        world.addSystem(weaponSystem, 1);

        // If an error occurs test has failed
        for (double t = 0; t < 10; t += 0.1) {
            world.process(t, 0.1);
        }
    }

    @Test
    public void TestSystemHumanPlayerRotationAndFlipping() {
        World world = new World(1000, 1000);
        WeaponSystem weaponSystem = new WeaponSystem();
        world.addSystem(weaponSystem, 1);

        // Mocked Objects
        CursorComponent cursorComp = new CursorComponent();
        cursorComp.setClicked(false);
        PositionComponent cursorPos = new PositionComponent(0, 0);
        Entity cursor = world.createEntity();

        world.addComponent(cursor, cursorComp);
        world.addComponent(cursor, cursorPos);

        // Add player and weapon
        Entity player = PlayerEntities.createPlayer(world,
                NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_ALPHA, 0, 0);
        WeaponDefinition weaponDef = new WeaponDefinition(0, "", "", "", 0, 0);
        Entity weapon = WeaponEntities.createWeapon(world, weaponDef, player);

        // Get player components
        PositionComponent position = world
                .getComponent(player, PositionComponent.class).get();
        SpriteComponent noodleSprite = world
                .getComponent(player, SpriteComponent.class).get();

        // Put the noodle so weapon should be (50, 50)
        position.setX(50 - noodleSprite.getWidth() / 2);
        position.setY(50 - noodleSprite.getHeight() / 2);

        // Get weapon Components
        PositionComponent pos = world
                .getComponent(weapon, PositionComponent.class).get();
        SpriteComponent sprite = world
                .getComponent(weapon, SpriteComponent.class).get();
        WeaponComponent wc = world.getComponent(weapon, WeaponComponent.class)
                .get();
        TurnComponent turn = world
                .getComponent(wc.getParent(), TurnComponent.class).get();

        turn.setTurn(0);

        // Expected values
        List<Double> rots = Arrays.asList(135.0, 90.0, 45.0, 0.0, -45.0, -90.0,
                -135.0, -180.0);

        for (int i = 0; i < 8; i++) {
            // set to silly values to ensure that it's setting correctly
            pos.setX(-100);
            pos.setY(-100);
            sprite.setFlipped(false);
            sprite.setRotation(100000);
            wc.setDirection(100000);
            double rot = rots.get(i);
            // set the cursor values
            setXYVals(rot, cursorPos);
            // process the system
            world.process(i, 1);
            // Get weapon Components
            pos = world.getComponent(weapon, PositionComponent.class).get();
            sprite = world.getComponent(weapon, SpriteComponent.class).get();
            wc = world.getComponent(weapon, WeaponComponent.class).get();
            // check it's correct
            assertTrue("Incorrect x position", Math.abs(pos.getX() - 50) < 0.1);
            assertTrue("Incorrect y position", Math.abs(pos.getY() - 50) < 0.1);
            assertTrue("Incorrect weapon rotation",
                    Math.abs(wc.getDirection() - rot) < 0.1);
            assertTrue("Incorrect sprite rotation",
                    Math.abs(sprite.getRotation() - accountForFlip(rot)) < 0.1);
            assertEquals("Inccorect Flip value", isFlipped(rot),
                    sprite.isFlipped());
        }
    }

    private static double accountForFlip(double rotation) {
        if (Math.abs(rotation) > 90) {
            return 180 * rotation / Math.abs(rotation) - rotation;
        }
        return rotation;
    }

    private static boolean isFlipped(double rotation) {
        return Math.abs(rotation) > 90;
    }

    private static void setXYVals(double rotation, PositionComponent position) {
        position.setX(50 + 50 * Math.cos(Math.toRadians(rotation)));
        position.setY(50 + 50 * Math.sin(Math.toRadians(rotation)));
    }

    @Test
    public void TestFiring() {
        World world = new World(1000, 1000);
        WeaponSystem weaponSystem = new WeaponSystem();
        world.addSystem(weaponSystem, 1);

        // Setup the components we need
        CursorComponent cursorComp = new CursorComponent();
        cursorComp.setClicked(false);
        PositionComponent cursorPos = new PositionComponent(0, 0);
        Entity cursor = world.createEntity();

        world.addComponent(cursor, cursorComp);
        world.addComponent(cursor, cursorPos);

        // Add player and weapon
        Entity player = PlayerEntities.createPlayer(world,
                NoodleEnum.NOODLE_PLAIN, true, TeamEnum.TEAM_ALPHA, 0, 0);
        WeaponDefinition weaponDef = new WeaponDefinitionList()
                .getByCategory("guns")
                .get(0);
        Entity weapon = WeaponEntities.createWeapon(world, weaponDef, player);

        // Get player components
        PositionComponent position = world
                .getComponent(player, PositionComponent.class).get();

        // Put the noodle away from (0,0) (where the cursor is)
        position.setX(0);
        position.setY(0);

        WeaponComponent wc = world.getComponent(weapon, WeaponComponent.class)
                .get();
        TurnComponent turn = world
                .getComponent(wc.getParent(), TurnComponent.class).get();

        turn.setTurn(0);
        wc.setFired(true);
        wc.setDirection(0);
        wc.setPower(100);

        world.process(1, 1);

        List<ProjectileComponent> projectiles = world
                .getComponents(ProjectileComponent.class);

        assertNotNull("There should be a projectile", projectiles);
        assertFalse("There should be projectiles in the list",
                projectiles.isEmpty());
    }

    @Test
    public void TestFiringOverTime() {
        World world = new World(1000, 1000);
        WeaponSystem weaponSystem = new WeaponSystem();
        world.addSystem(weaponSystem, 1);

        // Mocked Objects
        CursorComponent cursorComp = new CursorComponent();
        cursorComp.setClicked(false);
        PositionComponent cursorPos = new PositionComponent(0, 0);
        Entity cursor = world.createEntity();

        world.addComponent(cursor, cursorComp);
        world.addComponent(cursor, cursorPos);

        // Add player and weapon
        Entity player = PlayerEntities.createPlayer(world,
                NoodleEnum.NOODLE_PLAIN, true, TeamEnum.TEAM_ALPHA, 0, 0);
        WeaponDefinition weaponDef = new WeaponDefinitionList()
                .getWeaponByID(1);
        Entity weapon = WeaponEntities.createWeapon(world, weaponDef, player);

        // Get player components
        PositionComponent position = world
                .getComponent(player, PositionComponent.class).get();

        // Put the noodle away from (0,0) (where the cursor is)
        position.setX(1000);
        position.setY(1000);

        WeaponComponent wc = world.getComponent(weapon, WeaponComponent.class)
                .get();
        TurnComponent turn = world
                .getComponent(wc.getParent(), TurnComponent.class).get();

        turn.setTurn(0);
        wc.setFired(false);
        wc.setFiring(true);
        wc.setDirection(0);

        for (int i = 0; i < 10; i++) {
            world.process(i, 1);
        }
        wc.setFiring(false);
        world.process(11, 1);

        List<ProjectileComponent> projectiles = world
                .getComponents(ProjectileComponent.class);

        assertNotNull("There should be a projectile", projectiles);
        assertFalse("There should be projectiles in the list",
                projectiles.isEmpty());
        assertTrue("Unexpected value for power",
                Math.abs(wc.getPower() - 20) < 0.1);
    }

    @Test
    public void TestBadComponents() {
        World world = new World(1000, 1000);
        WeaponSystem weaponSystem = new WeaponSystem();
        world.addSystem(weaponSystem, 1);

        // Mocked Objects
        CursorComponent cursorComp = new CursorComponent();
        cursorComp.setClicked(false);
        PositionComponent cursorPos = new PositionComponent(0, 0);
        Entity cursor = world.createEntity();

        world.addComponent(cursor, cursorComp);
        world.addComponent(cursor, cursorPos);

        // Add player and weapon
        Entity player = PlayerEntities.createPlayer(world,
                NoodleEnum.NOODLE_PLAIN, true, TeamEnum.TEAM_ALPHA, 0, 0);
        WeaponDefinition weaponDef = new WeaponDefinitionList()
                .getWeaponByID(1);
        Entity weapon = WeaponEntities.createWeapon(world, weaponDef, player);

        // Get player components
        PositionComponent position = world
                .getComponent(player, PositionComponent.class).get();

        // Put the noodle away from (0,0) (where the cursor is)
        position.setX(1000);
        position.setY(1000);

        WeaponComponent wc = world.getComponent(weapon, WeaponComponent.class)
                .get();
        PositionComponent pos = world
                .getComponent(weapon, PositionComponent.class).get();
        TurnComponent turn = world
                .getComponent(wc.getParent(), TurnComponent.class).get();

        turn.setTurn(0);

        position.setX(302);
        position.setY(304);
        pos.setX(-100);
        pos.setY(-100);

        world.removeComponent(weapon, SpriteComponent.class);

        world.process(1, 1);

        assertTrue("X position should not have changed",
                Math.abs(pos.getX() + 100) < 0.1);
        assertTrue("Y position should not have changed",
                Math.abs(pos.getY() + 100) < 0.1);

    }

    @Test
    public void TestNotTheirTurn() {
        World world = new World(1000, 1000);
        WeaponSystem weaponSystem = new WeaponSystem();
        world.addSystem(weaponSystem, 1);

        // Mocked Objects
        CursorComponent cursorComp = new CursorComponent();
        cursorComp.setClicked(false);
        PositionComponent cursorPos = new PositionComponent(0, 0);
        Entity cursor = world.createEntity();

        world.addComponent(cursor, cursorComp);
        world.addComponent(cursor, cursorPos);

        // Add player and weapon
        Entity player = PlayerEntities.createPlayer(world,
                NoodleEnum.NOODLE_PLAIN, true, TeamEnum.TEAM_ALPHA, 0, 0);
        WeaponDefinition weaponDef = new WeaponDefinitionList()
                .getWeaponByID(1);
        Entity weapon = WeaponEntities.createWeapon(world, weaponDef, player);

        // Get player components
        PositionComponent position = world
                .getComponent(player, PositionComponent.class).get();

        // Put the noodle away from (0,0) (where the cursor is)
        position.setX(1000);
        position.setY(1000);

        WeaponComponent wc = world.getComponent(weapon, WeaponComponent.class)
                .get();
        TurnComponent turn = world
                .getComponent(wc.getParent(), TurnComponent.class).get();
        PositionComponent pos = world
                .getComponent(weapon, PositionComponent.class).get();

        turn.clearTurn(10);

        position.setX(302);
        position.setY(304);
        pos.setX(-100);
        pos.setY(-100);

        world.process(1, 1);

        assertTrue("X position should not have changed",
                Math.abs(pos.getX() + 100) < 0.1);
        assertTrue("Y position should not have changed",
                Math.abs(pos.getY() + 100) < 0.1);
    }
}
