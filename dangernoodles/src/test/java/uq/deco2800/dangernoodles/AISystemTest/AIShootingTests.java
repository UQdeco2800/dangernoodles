package uq.deco2800.dangernoodles.AISystemTest;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import uq.deco2800.dangernoodles.components.AIComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.components.weapons.WeaponComponent;
import uq.deco2800.dangernoodles.components.weather.WindComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;
import uq.deco2800.dangernoodles.prefabs.PlayerEntities;
import uq.deco2800.dangernoodles.prefabs.WeaponEntities;
import uq.deco2800.dangernoodles.systems.AISystem;
import uq.deco2800.dangernoodles.weapons.WeaponDefinition;

public class AIShootingTests {

    /**
     * Test shooting using a non-powered weapon (no wind).
     */
    @Test
    public void aiShootingTest1() {
        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();

        testWorld.createEntity().addComponent(new WindComponent());

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        boolean isAI = true;
        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[0], isAI, availableTeams[0], 1, 0);
        Entity enemy = PlayerEntities.createPlayer(testWorld, availableNoodles[0], !isAI, availableTeams[1], 2, 0);

        // use rail gun (non-powered)
        WeaponDefinition weaponDef1 = testWorld.getWeaponDefinitions().getWeaponByID(6);
        WeaponEntities.createWeapon(testWorld, weaponDef1, aiPlayer);
        WeaponDefinition weaponDef2 = testWorld.getWeaponDefinitions().getWeaponByID(3);
        WeaponEntities.createWeapon(testWorld, weaponDef2, enemy);

        AIComponent aiComponent = testWorld.getComponent(aiPlayer, AIComponent.class).get();

        // set turn to be true
        TurnComponent aiTurn = testWorld.getComponent(aiPlayer, TurnComponent.class).get();
        aiTurn.setTurn(5);

        testAISystem.run(testWorld, 5, 0.016);

        List<WeaponComponent> weapons = testWorld.getComponents(WeaponComponent.class);
        WeaponComponent weapon = null;
        for (WeaponComponent w : weapons) {
            if (w.getParent() == aiPlayer) {
                weapon = w;
            }
        }

        PositionComponent weaponPos = testWorld.getComponent(weapon.getParent(), PositionComponent.class).get();

        assertTrue(aiComponent.getWeaponPosition().equals(weaponPos));
        assertTrue(aiComponent.hasFired() == true);
    }

    /**
     * Test shooting using a powered weapon (no wind).
     */
    @Test
    public void aiShootingTest2() {
        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();

        testWorld.createEntity().addComponent(new WindComponent());

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        boolean isAI = true;
        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[0], isAI, availableTeams[0], 1, 0);
        Entity enemy = PlayerEntities.createPlayer(testWorld, availableNoodles[0], !isAI, availableTeams[1], 2, 0);

        // use machine gun (powered)
        WeaponDefinition weaponDef1 = testWorld.getWeaponDefinitions().getWeaponByID(5);
        WeaponEntities.createWeapon(testWorld, weaponDef1, aiPlayer);
        WeaponDefinition weaponDef2 = testWorld.getWeaponDefinitions().getWeaponByID(3);
        WeaponEntities.createWeapon(testWorld, weaponDef2, enemy);

        AIComponent aiComponent = testWorld.getComponent(aiPlayer, AIComponent.class).get();
        TurnComponent aiTurn = testWorld.getComponent(aiPlayer, TurnComponent.class).get();

        aiTurn.setTurn(5);

        testAISystem.run(testWorld, 5, 0.016);

        List<WeaponComponent> weapons = testWorld.getComponents(WeaponComponent.class);
        WeaponComponent weapon = null;
        for (WeaponComponent w : weapons) {
            if (w.getParent() == aiPlayer) {
                weapon = w;
            }
        }

        PositionComponent weaponPos = testWorld.getComponent(weapon.getParent(), PositionComponent.class).get();

        assertTrue(aiComponent.getWeaponPosition().equals(weaponPos));
        assertTrue(aiComponent.hasFired() == true);
    }

    /**
     * Test shooting using a non-powered weapon (with wind).
     */
    @Test
    public void aiShootingTest3() {
        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();

        // add wind to affect shot power calculation
        WindComponent wind = new WindComponent();
        wind.setStrength(1);
        wind.setDirection(1);
        wind.setLength(5);
        Entity windEntity = testWorld.createEntity().addComponent(wind);
        testWorld.addComponent(windEntity, wind);

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        boolean isAI = true;
        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[0], isAI, availableTeams[0], 1, 0);
        Entity enemy = PlayerEntities.createPlayer(testWorld, availableNoodles[0], !isAI, availableTeams[1], 2, 0);

        // use rail gun (non-powered)
        WeaponDefinition weaponDef1 = testWorld.getWeaponDefinitions().getWeaponByID(6);
        WeaponEntities.createWeapon(testWorld, weaponDef1, aiPlayer);
        WeaponDefinition weaponDef2 = testWorld.getWeaponDefinitions().getWeaponByID(3);
        WeaponEntities.createWeapon(testWorld, weaponDef2, enemy);

        AIComponent aiComponent = testWorld.getComponent(aiPlayer, AIComponent.class).get();

        // set turn to be true
        TurnComponent aiTurn = testWorld.getComponent(aiPlayer, TurnComponent.class).get();
        aiTurn.setTurn(5);

        testAISystem.run(testWorld, 5, 0.016);

        List<WeaponComponent> weapons = testWorld.getComponents(WeaponComponent.class);
        WeaponComponent weapon = null;
        for (WeaponComponent w : weapons) {
            if (w.getParent() == aiPlayer) {
                weapon = w;
            }
        }

        PositionComponent weaponPos = testWorld.getComponent(weapon.getParent(), PositionComponent.class).get();

        assertTrue(aiComponent.getWeaponPosition().equals(weaponPos));
        assertTrue(aiComponent.hasFired() == true);
    }

    /**
     * Test shooting using a powered weapon (with wind).
     */
    @Test
    public void aiShootingTest4() {
        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();

        // add wind to affect shot power calculation
        WindComponent wind = new WindComponent();
        wind.setStrength(1);
        wind.setDirection(1);
        wind.setLength(5);
        Entity windEntity = testWorld.createEntity().addComponent(wind);
        testWorld.addComponent(windEntity, wind);

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        boolean isAI = true;
        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[0], isAI, availableTeams[0], 1, 0);
        Entity enemy = PlayerEntities.createPlayer(testWorld, availableNoodles[0], !isAI, availableTeams[1], 2, 0);

        // use machine gun (powered)
        WeaponDefinition weaponDef1 = testWorld.getWeaponDefinitions().getWeaponByID(5);
        WeaponEntities.createWeapon(testWorld, weaponDef1, aiPlayer);
        WeaponDefinition weaponDef2 = testWorld.getWeaponDefinitions().getWeaponByID(3);
        WeaponEntities.createWeapon(testWorld, weaponDef2, enemy);

        AIComponent aiComponent = testWorld.getComponent(aiPlayer, AIComponent.class).get();
        TurnComponent aiTurn = testWorld.getComponent(aiPlayer, TurnComponent.class).get();

        aiTurn.setTurn(5);

        testAISystem.run(testWorld, 5, 0.016);

        List<WeaponComponent> weapons = testWorld.getComponents(WeaponComponent.class);
        WeaponComponent weapon = null;
        for (WeaponComponent w : weapons) {
            if (w.getParent() == aiPlayer) {
                weapon = w;
            }
        }

        PositionComponent weaponPos = testWorld.getComponent(weapon.getParent(), PositionComponent.class).get();

        assertTrue(aiComponent.getWeaponPosition().equals(weaponPos));
        assertTrue(aiComponent.hasFired() == true);
    }
}
