package uq.deco2800.dangernoodles.systems;

import static org.junit.Assert.*;

import org.junit.*;

import uq.deco2800.dangernoodles.components.MovementComponent;
import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.components.stats.ManaComponent;
import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;
import uq.deco2800.dangernoodles.prefabs.PlayerEntities;

public class ManaSystemTest {
	
	private static World testWorld = new World(1100, 800);
	
	
	static Entity noodleA = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_PLAIN, true, TeamEnum.TEAM_ALPHA, 1, 600);
    static Entity noodleB = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_PLAIN, true, TeamEnum.TEAM_ALPHA, 1, 640);
    static ManaComponent firstMana = testWorld.getComponent(noodleA, ManaComponent.class).get();
    static ManaComponent secondMana = testWorld.getComponent(noodleB, ManaComponent.class).get();
    static MovementComponent firstMovement = testWorld.getComponent(noodleA, MovementComponent.class).get();
    static MovementComponent secondMovement = testWorld.getComponent(noodleB, MovementComponent.class).get();
    static TurnComponent firstTurn = testWorld.getComponent(noodleA, TurnComponent.class).get();
    static TurnComponent secondTurn = testWorld.getComponent(noodleB, TurnComponent.class).get();
    
    @Test
    public void testManaSystem(){
    	ManaSystem manaSystem = new ManaSystem();
    	// setup an unmovable and empty noodle, and a movable noodle with 1 mana
    	firstMana.setMana(0);
    	firstMovement.setMovable(false);
    	secondTurn.setTurn(10);
    	secondMovement.setIsMovingX(true);
    	secondMana.setManaUpdateableStatus(true);
    	secondMana.setMana(1);
    	int oldMana = secondMana.getMana();
        manaSystem.run(testWorld, 0, 0);
        //ensure system has run correctly
        assertEquals(oldMana-1, secondMana.getMana());
        assertTrue(secondMovement.isMovable());
        assertEquals(firstMana.getMana(), 500);
        assertTrue(firstMovement.isMovable());
        //ensure system disables movement of those with no mana
        manaSystem.run(testWorld, 0, 0);
        assertFalse(secondMovement.isMovable());
        
    }
}
