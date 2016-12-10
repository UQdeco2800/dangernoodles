package uq.deco2800.dangernoodles.systems;

import uq.deco2800.dangernoodles.components.MovementComponent;
import uq.deco2800.dangernoodles.components.noodles.NoodleComponent;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;
import uq.deco2800.dangernoodles.prefabs.PlayerEntities;
import uq.deco2800.dangernoodles.systems.NoodleSystem;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * Test class for the NoodleSystem
 * @author Paul Haley
 */
public class NoodleSystemTest{
	String[] teams = {"_Bteam", "_Rteam", "", "_Yteam"};
	String[] characters = {"Civilian", "Juggernaught", "Scout", "Warrior"};
	String moving = "";
	String idle = "_idle";
	String jumping = "_jump";
	String left = "";
	String right = "I-";
	NoodleEnum noodleClasses[] = NoodleEnum.values();
	TeamEnum noodleTeams[] = TeamEnum.values();
	
	/**
	 * @require 0 <= character <= 3 && 0 <= team <= 4
	 * 
	 * Used to quickly make a string for the expect filepath.
	 * @param left Boolean if moving left or not
	 * @param character character class number, 0 for Plain, 1 for 
	 * Juggernaught, 2 for Agility and 3 for Warrior
	 * @param moving Boolean for if the character is moving or idle
	 * @param team number for which team the noodle is on, 1 for blue, 2 for 
	 * red, 3 for green and 4 for yellow.
	 * @return The expected filepath for the given parameters
	 */
	private String makePath(boolean left, int character, int moving, int team) {
		String action;
		switch(moving) {
		case 0:
			action = this.idle;
			break;
		case 1:
			action = this.moving;
			break;
		case 2:
			action = this.jumping;
			break;
		default:
			action = this.idle;
			break;
		}
		
		return "/characters/" + (left ? this.left : this.right) 
				+ this.characters[character] 
				+ action
				+ teams[team]
				+ ".gif";
	}
	
	/**
	 * Used to get the appropriate movement status int for the makePath method to work with.
	 * @param movementComponent The movement component of the entity
	 * @return the appropriate value int for the noodle moving. 0 for idle, 1 for moving (horizontally only) or 2 for 
	 * 		for moving vertically.
	 */
	private int actionStatus(MovementComponent movementComponent) {
		if (movementComponent.isMovingY()) {
			return 2;
		} else if (movementComponent.isMovingX()) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * Tests the defaults of a noodle creation and assuring the system has not ran yet.
	 */
	@Test
	public void noodleWorldNotRan() {
		// Tells the world to update all systems
        //world.process(0, 1);
		World world = new World(100, 100);
        Entity player = PlayerEntities.createPlayer(world, 
        		NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_ALPHA, 0, 0);
        
        // World has not ran, so default image should be present
        SpriteComponent spriteComponent = world.getComponent(player, SpriteComponent.class).get();
        assertTrue(spriteComponent.getImage().equals("/snakesprite.png"));        
	}
	
	/**
	 * Tests the NoodleSystem of a noodle after the world has began to run.
	 */
	@Test
	public void noodleWorldRan() {
		World world = new World(1000, 1000);
		
		// Tests each permutation of noodle class and team possible
		for (int i = 0; i < noodleClasses.length; ++i) {
			for (int j = 0; j < noodleTeams.length - 1; ++j) {
				int index = j + i * (noodleTeams.length - 1);
				// Creates a noodle
				Entity player = PlayerEntities.createPlayer(world, noodleClasses[i], false, noodleTeams[j], index, 0);
				
				// Accessing the components that directly influence/read results of the NoodleSystem.
		        NoodleComponent noodleComponent = world.getComponent(player, NoodleComponent.class).get();
		        MovementComponent movementComponent = world.getComponent(player, MovementComponent.class).get();
		        
		        NoodleSystem noodleSystem = new NoodleSystem();
		        MovementSystem movementSystem = new MovementSystem();
		        
		        // Makes the character move right (direction of noodle on spawn is indeterministic).
		        movementComponent.setVx(5);
		        
		        /* Updates the required systems. The order is vital to update the 
		         * movement which informs the noodle system that dictates the sprite 
		         * the spriteComponent will report.
		         */
		        movementSystem.run(world, 0, 0);
		        noodleSystem.run(world, 0, 0);
		        
		        int action = actionStatus(movementComponent);
		        String expectedPath = makePath(movementComponent.getLastMovingLeft(), 
		        		i, action, j);
		        String sprite = noodleComponent.getCurrentSprite();
		        // Tests if the sprite has the same file path as the one constructed in the test
		        assertTrue("Expected: " + expectedPath + "\nGiven: " + sprite, 
		        		noodleComponent.getCurrentSprite().equals(expectedPath));
		        	        
		        // Removing the noodle from the world
		        world.destroyEntity(player);
			}
		}
	}

	/**
	 * Tests the NoodleSystem of a noodle that has changed direction or stopped
	 */
	@Test
	public void noodleMoving() {
		World world = new World(1000, 1000);		
		// Tests each permutation of noodle class and team possible
		for (int i = 0; i < noodleClasses.length; ++i) {
			for (int j = 0; j < noodleTeams.length - 1; ++j) {
				int index = j + i * (noodleTeams.length - 1);
				// Creates a noodle
				Entity player = PlayerEntities.createPlayer(world, noodleClasses[i], false, noodleTeams[j], index, 0);
				
				// Accessing the components that directly influence/read results of the NoodleSystem.
		        NoodleComponent noodleComponent = world.getComponent(player, NoodleComponent.class).get();
		        MovementComponent movementComponent = world.getComponent(player, MovementComponent.class).get();
		        
		        NoodleSystem noodleSystem = new NoodleSystem();
		        MovementSystem movementSystem = new MovementSystem();
		        
		        // Makes the character move right, no vertical
		        movementComponent.setVx(5);
		        movementComponent.setVy(0);
		        
		        /* Updates the required systems. The order is vital to update the 
		         * movement which informs the noodle system that dictates the sprite 
		         * the spriteComponent will report.
		         */
		        movementSystem.run(world, 0, 0);
		        noodleSystem.run(world, 0, 0);
		        
		        int action = actionStatus(movementComponent);
		        String expectedPath = makePath(movementComponent.getLastMovingLeft(), 
		        		i, action, j);
		        String sprite = noodleComponent.getCurrentSprite();
		        // Tests if the sprite has the same file path as the one constructed in the test
		        assertTrue("Expected: " + expectedPath + "\nGiven: " + sprite, 
		        		noodleComponent.getCurrentSprite().equals(expectedPath));
		        
		        // Makes the character move right while having a vertical velocity
		        movementComponent.setVx(5);
		        movementComponent.setVy(50);
		        
		        /* Updates the required systems. The order is vital to update the 
		         * movement which informs the noodle system that dictates the sprite 
		         * the spriteComponent will report.
		         */
		        movementSystem.run(world, 0, 0);
		        noodleSystem.run(world, 0, 0);
		        
		        action = actionStatus(movementComponent);
		        expectedPath = makePath(movementComponent.getLastMovingLeft(), 
		        		i, action, j);
		        sprite = noodleComponent.getCurrentSprite();
		        // Tests if the sprite has the same file path as the one constructed in the test
		        assertTrue("Expected: " + expectedPath + "\nGiven: " + sprite, 
		        		noodleComponent.getCurrentSprite().equals(expectedPath));
		        
		        // Makes the character stop moving right and vertically
		        movementComponent.setVx(0);
		        movementComponent.setVy(0);
		        
		        movementSystem.run(world, 0, 0);
		        noodleSystem.run(world, 0, 0);
		        
		        action = actionStatus(movementComponent);
		        expectedPath = makePath(movementComponent.getLastMovingLeft(), 
		        		i, action, j);
		        sprite = noodleComponent.getCurrentSprite();
		        // Tests if the sprite has the same file path as the one constructed in the test
		        assertTrue("Expected: " + expectedPath + "\nGiven: " + sprite, 
		        		noodleComponent.getCurrentSprite().equals(expectedPath));
		        
		        // Makes the character start moving left, no vertical component
		        movementComponent.setVx(-5);
		        
		        movementSystem.run(world, 0, 0);
		        noodleSystem.run(world, 0, 0);
		        
		        action = actionStatus(movementComponent);
		        expectedPath = makePath(movementComponent.getLastMovingLeft(), 
		        		i, action, j);
		        sprite = noodleComponent.getCurrentSprite();
		        // Tests if the sprite has the same file path as the one constructed in the test
		        assertTrue("Expected: " + expectedPath + "\nGiven: " + sprite, 
		        		noodleComponent.getCurrentSprite().equals(expectedPath));
		        
		        // Makes the character start moving left while with vertical velocity
		        movementComponent.setVx(-5);
		        movementComponent.setVy(50);
		        
		        movementSystem.run(world, 0, 0);
		        noodleSystem.run(world, 0, 0);
		        
		        action = actionStatus(movementComponent);
		        expectedPath = makePath(movementComponent.getLastMovingLeft(), 
		        		i, action, j);
		        sprite = noodleComponent.getCurrentSprite();
		        // Tests if the sprite has the same file path as the one constructed in the test
		        assertTrue("Expected: " + expectedPath + "\nGiven: " + sprite, 
		        		noodleComponent.getCurrentSprite().equals(expectedPath));
		        
		        // Removing the noodle from the world
		        world.destroyEntity(player);
			}
		}
	}
}