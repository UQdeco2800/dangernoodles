package uq.deco2800.dangernoodles.AISystemTest;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import uq.deco2800.dangernoodles.components.AIComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.components.weapons.WeaponComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;
import uq.deco2800.dangernoodles.prefabs.PlayerEntities;
import uq.deco2800.dangernoodles.prefabs.WeaponEntities;
import uq.deco2800.dangernoodles.systems.AISystem;
import uq.deco2800.dangernoodles.weapons.ProjectileDefinition;
import uq.deco2800.dangernoodles.weapons.WeaponDefinition;

public class AIMoveOrShootTests {

	/*
    @Test
    public void testFindTargets3() {

        World testWorld = new World(1100, 800);
        AISystem testAISystem = new AISystem();

        boolean isAI = true;

        TeamEnum[] availableTeams = TeamEnum.values();
        NoodleEnum[] availableNoodles = NoodleEnum.values();

        Entity player1 = PlayerEntities.createPlayer(testWorld, availableNoodles[0], !isAI, availableTeams[0], 1, 523);
        Entity player2 = PlayerEntities.createPlayer(testWorld, availableNoodles[1], !isAI, availableTeams[0], 2, 6);
        Entity player6 = PlayerEntities.createPlayer(testWorld, availableNoodles[1], !isAI, availableTeams[1], 6, 1090);
        Entity player7 = PlayerEntities.createPlayer(testWorld, availableNoodles[0], !isAI, availableTeams[1], 7, 299);

        List<Entity> opponents = new ArrayList<>();

        opponents.add(player1);
        opponents.add(player2);
        opponents.add(player6);
        opponents.add(player7);

        Entity aiPlayer = PlayerEntities.createPlayer(testWorld, availableNoodles[1], isAI, availableTeams[2], 8, 790);

        ProjectileDefinition grenade = new ProjectileDefinition(0, "Grenade", "weapons/grenade_left.png", 15, 15, 1.0,
                50.0);
        WeaponDefinition weaponDef = new WeaponDefinition(0, "Grenade", "Thrown", "weapons/grenade_left.png", 15, 15,
                true, true, 1, grenade);
        WeaponEntities.createWeapon(testWorld, weaponDef, aiPlayer);

        List<PositionComponent> potentialTargets = testAISystem.getPotentialTargets(testWorld, aiPlayer);

        // check number of potential targets is correct
        assertTrue(potentialTargets.size() == 4);

        // check all the potential targets have been identified
        for (int i = 0; i < 4; ++i) {
            assertTrue(opponents.contains(potentialTargets.get(i).getEntity()));
        }

        // check target is correct
        testAISystem.setTargetEntity(aiPlayer, potentialTargets, testWorld);
        PositionComponent targetPosition = testWorld.getComponent(aiPlayer, AIComponent.class).get()
                .getTargetPosition();
        PositionComponent correctTargetPos = testWorld.getComponent(player1, PositionComponent.class).get();

        assertTrue(Double.compare(targetPosition.getX(), correctTargetPos.getX()) == 0);
        assertTrue(Double.compare(targetPosition.getY(), correctTargetPos.getY()) == 0);

        // check AI shoots the target
        testAISystem.moveAsRequiredOrShootTarget(aiPlayer, testWorld);

        AIComponent aiComponent = testWorld.getComponent(aiPlayer, AIComponent.class).get();

        List<WeaponComponent> weapons = testWorld.getComponents(WeaponComponent.class);
        WeaponComponent weapon = null;

        for (WeaponComponent w : weapons) {
            if (w.getParent() == aiPlayer) {
                weapon = w;
                aiComponent.setWeaponPosition(testWorld.getComponent(w.getParent(), PositionComponent.class).get());
            }
        }

        assertTrue(aiComponent.hasFired() == true);
        assertTrue(weapon.isFired() == true);

    }
	*/
}
