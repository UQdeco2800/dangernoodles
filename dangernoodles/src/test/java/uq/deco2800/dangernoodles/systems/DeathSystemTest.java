package uq.deco2800.dangernoodles.systems;

import static org.junit.Assert.*;

import java.util.Deque;

import org.junit.*;

import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;
import uq.deco2800.dangernoodles.prefabs.PlayerEntities;
import uq.deco2800.dangernoodles.systems.DeathSystem;


public class DeathSystemTest {
	
	private static World testWorld = new World(1100, 800);
	static Entity in = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_PLAIN, true, TeamEnum.TEAM_ALPHA, 1, 10);
	static Entity out = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_PLAIN, true, TeamEnum.TEAM_BRAVO, 2, 100);
	static PositionComponent inPosition = testWorld.getComponent(in, PositionComponent.class).get();
    static PositionComponent outPosition = testWorld.getComponent(out, PositionComponent.class).get();
    
    @BeforeClass
    public static void setupTest1Class() {
        // Sets up positions for testing case
        inPosition.setY(100);
        outPosition.setY(5000);
        
    }

    @Test
    public void TestTagAndKill(){
    	DeathSystem deathSystem = new DeathSystem();
    	Deque<PositionComponent> killList = deathSystem.tagForDeath(testWorld);
    	assertEquals(killList.size(), 1);
    	assertEquals(killList.getFirst(), outPosition);
		while (!killList.isEmpty()) {
		    deathSystem.kill(testWorld, killList.pop());
		}
		assertEquals(killList.size(), 0);
    }
	
    
}
