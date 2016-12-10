package uq.deco2800.dangernoodles.prefabs;

import uq.deco2800.dangernoodles.components.AIComponent;
import uq.deco2800.dangernoodles.components.CollisionComponent;
import uq.deco2800.dangernoodles.components.GravityComponent;
import uq.deco2800.dangernoodles.components.InputComponent;
import uq.deco2800.dangernoodles.components.MassComponent;
import uq.deco2800.dangernoodles.components.MovementComponent;
import uq.deco2800.dangernoodles.components.NameComponent;
import uq.deco2800.dangernoodles.components.noodles.AngelComponent;
import uq.deco2800.dangernoodles.components.noodles.NoodleComponent;
import uq.deco2800.dangernoodles.components.PlayerComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.RectangleComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.components.StatusBarComponent;
import uq.deco2800.dangernoodles.components.displays.EffectDisplayComponent;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.components.stats.*;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;

import java.util.Optional;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * Test class for the Player Entity Prefab.
 * @author Paul Haley
 */
public class PlayerEntitiesTest {
	private final String[] colourExtensions = {"_Bteam", "_Rteam", "", "_Yteam"};

    /**
     * Tests base case (instance creation) and that all components expected are present.
     */
    @Test
    public void baseCreationTest() {
        World miniWorld = new World(0, 0);
        Entity player = PlayerEntities.createPlayer(miniWorld,
                NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_ALPHA, 0, 0);
       
        // Common components of both AI and non-AI players.
        assertTrue(miniWorld.hasComponent(player, NameComponent.class));
        assertTrue(miniWorld.hasComponent(player, PositionComponent.class));
        assertTrue(miniWorld.hasComponent(player, NoodleComponent.class));
        assertTrue(miniWorld.hasComponent(player, MovementComponent.class));
        assertTrue(miniWorld.hasComponent(player, SpriteComponent.class));
        assertTrue(miniWorld.hasComponent(player, InputComponent.class));
        assertTrue(miniWorld.hasComponent(player, PlayerComponent.class));
        assertTrue(miniWorld.hasComponent(player, MassComponent.class));
        assertTrue(miniWorld.hasComponent(player, GravityComponent.class));
        assertTrue(miniWorld.hasComponent(player, CollisionComponent.class));
        assertTrue(miniWorld.hasComponent(player, SpeedComponent.class));
        assertTrue(miniWorld.hasComponent(player, ShieldComponent.class));
        assertTrue(miniWorld.hasComponent(player, InvulnerableComponent.class));
        assertTrue(miniWorld.hasComponent(player, DamageComponent.class));
        assertTrue(miniWorld.hasComponent(player, ManaComponent.class));
        assertTrue(miniWorld.hasComponent(player, RectangleComponent.class));
        assertTrue(miniWorld.hasComponent(player, EffectDisplayComponent.class));
        assertTrue(miniWorld.hasComponent(player, HealthComponent.class));
        assertTrue(miniWorld.hasComponent(player, StatusBarComponent.class));
        
        // Checking that the human controlled noodle has an InputComponent and not AIComponent
        assertTrue(miniWorld.hasComponent(player, InputComponent.class));
        assertFalse(miniWorld.hasComponent(player, AIComponent.class));
    }
    
    /**
     * Tests base case (instance creation) and that all components expected are present.
     */
    @Test
    public void entityCreationTest() {
        World miniWorld = new World(0, 0);
        Entity player = PlayerEntities.createEntity(miniWorld, "", 0, 0, NoodleEnum.NOODLE_PLAIN, 0,
        		TeamEnum.TEAM_ALPHA);
       
        // Common components of both AI and non-AI players.
        assertTrue(miniWorld.hasComponent(player, NameComponent.class));
        assertTrue(miniWorld.hasComponent(player, PositionComponent.class));
        assertTrue(miniWorld.hasComponent(player, NoodleComponent.class));
        assertTrue(miniWorld.hasComponent(player, MovementComponent.class));
        assertTrue(miniWorld.hasComponent(player, SpriteComponent.class));
        assertTrue(miniWorld.hasComponent(player, InputComponent.class));
        assertTrue(miniWorld.hasComponent(player, PlayerComponent.class));
        assertTrue(miniWorld.hasComponent(player, MassComponent.class));
        assertTrue(miniWorld.hasComponent(player, GravityComponent.class));
        assertTrue(miniWorld.hasComponent(player, CollisionComponent.class));
        assertTrue(miniWorld.hasComponent(player, SpeedComponent.class));
        assertTrue(miniWorld.hasComponent(player, ShieldComponent.class));
        assertTrue(miniWorld.hasComponent(player, InvulnerableComponent.class));
        assertTrue(miniWorld.hasComponent(player, DamageComponent.class));
        assertTrue(miniWorld.hasComponent(player, ManaComponent.class));
        assertTrue(miniWorld.hasComponent(player, RectangleComponent.class));
        assertTrue(miniWorld.hasComponent(player, EffectDisplayComponent.class));
        assertTrue(miniWorld.hasComponent(player, HealthComponent.class));
        assertTrue(miniWorld.hasComponent(player, StatusBarComponent.class));
        
        // Checking that the human controlled noodle has an InputComponent and not AIComponent
        assertTrue(miniWorld.hasComponent(player, InputComponent.class));
        assertFalse(miniWorld.hasComponent(player, AIComponent.class));
    }
    
    /**
     * Testing the case that a plain noodle was made but was an AI. Checks that it has the AIComponent.
     */
    @Test
    public void baseCreationTestAI() {
    	
    	World miniWorld = new World(0, 0);
    	Entity bot = PlayerEntities.createPlayer(miniWorld,
                NoodleEnum.NOODLE_PLAIN, true, TeamEnum.TEAM_ALPHA, 0, 0);
    	
    	// Checking that the AI controlled noodle has an AIComponent
        assertTrue(miniWorld.hasComponent(bot, AIComponent.class));
    }

    /**
     * Makes a tank noodle (Jugganaut) and tests its default values.
     * 
     * This test has assumed that the baseCaseTest is true and that the required components are present for testing. 
     * This decision was made for testing as to improve readability and maintainability of the code.
     */
    @Test
    public void baseDefaultsTank() {
    	final int xPosition = 20;
    	final int playerID = 1;
    	final NoodleEnum noodleT = NoodleEnum.NOODLE_TANK;
    	final TeamEnum team = TeamEnum.TEAM_BRAVO;
    	
    	// Making world and noodle tank to go in it
    	World miniWorld = new World(0, 0);
        Entity player = PlayerEntities.createPlayer(miniWorld,
                noodleT, false, team, playerID, xPosition);
        
        // Checks the Noodle has non-null name
        NameComponent nameComponent = miniWorld.getComponent(player, NameComponent.class).get();
        assertFalse(nameComponent.getName() == null);
        
        // Checks the Noodle has expected noodle component class name
        NoodleComponent noodleComponent = miniWorld.getComponent(player, NoodleComponent.class).get();
        assertTrue(noodleComponent.getNoodleClassName().equals(noodleT.getName()));
        
        // Checks the Noodle has the expected x position as given
        PositionComponent positionComponent = miniWorld.getComponent(player, PositionComponent.class).get();
        assertTrue(positionComponent.getX() < xPosition + 0.05 && 
        		positionComponent.getX() > xPosition - 0.05);
        
        // Check the noodle has the expected movement component values given
        MovementComponent movementComponent = miniWorld.getComponent(player, MovementComponent.class).get();
        assertTrue(movementComponent.getVX() < 0.0 + 0.5 && movementComponent.getVX() > 0.0 - 0.5);
        assertTrue(movementComponent.getVY() < 0.0 + 0.5 && movementComponent.getVY() > 0.0 - 0.5);
        assertTrue(movementComponent.getAX() < 1.0 + noodleT.getAxOffset() + 0.5 && 
        		movementComponent.getAX() > 1.0 + noodleT.getAxOffset() - 0.5);
        assertTrue(movementComponent.getAY() < 0.0 + 0.5 && movementComponent.getAY() > 0.0 - 0.5);
        
        // Checks the noodle's sprite filepath and file exists
        SpriteComponent spriteComponent = miniWorld.getComponent(player, SpriteComponent.class).get();
        String image = spriteComponent.getImage();
        /* Checks if the image is in the characters folder with the word Juggernaught (internal for TANK) and file 
         * extension .gif. Alternatively it checks the noodle has the default image given by the NoodleEnum.
         */
        assertTrue((image.contains("/characters/") && image.contains("Juggernaught") && image.contains(".gif")) 
        		|| image.equals("/snakesprite.png"));
        assertTrue(spriteComponent.getHeight() > 0);
        assertTrue(spriteComponent.getWidth() > 0);
        
        // Check the noodles player and team IDs match those passed via PlayerComponent
        PlayerComponent playerComponent = miniWorld.getComponent(player, PlayerComponent.class).get();
        assertEquals(playerComponent.getPlayerId(), playerID);
        assertEquals(playerComponent.getTeamId(), team.getTeamId());
        
        // Check the mass of the noodle is correct from the MassComponent
        MassComponent massComponent = miniWorld.getComponent(player, MassComponent.class).get();
        assertTrue(massComponent.getMass() < 10.0 + noodleT.getMassOffset() + 0.05 
        		&& massComponent.getMass() > 10.0 + noodleT.getMassOffset() - 0.05);
        
        // Check the noodle has gravity via the GravityComponent
        GravityComponent gravityComponent = miniWorld.getComponent(player, GravityComponent.class).get();
        assertTrue(gravityComponent.getGravity());
        
        // Checks collisions are correct as per the CollisionComponent
        CollisionComponent collisionComponent = miniWorld.getComponent(player, CollisionComponent.class).get();
        // Not hitting anything (there is nothing else in the world though)
        assertTrue(collisionComponent.getCollisions().isEmpty());
        assertTrue(collisionComponent.getType().equals(CollisionComponent.shape.RECTANGLE));
        assertTrue(collisionComponent.isCollidable());
        assertTrue(collisionComponent.isSolid());
        assertFalse(collisionComponent.isCollided());
        
        // Checks SpeedComponent is set correctly
        SpeedComponent speedComponent = miniWorld.getComponent(player, SpeedComponent.class).get();
        assertTrue(speedComponent.getSpeed() < 1000.0 + noodleT.getMaxSpeedOffset() + 0.5
        		&& speedComponent.getSpeed() > 1000.0 + noodleT.getMaxSpeedOffset() - 0.5);
        
        // Check the shield is correct default for noodle
        ShieldComponent shieldComponent = miniWorld.getComponent(player, ShieldComponent.class).get();
        assertTrue(shieldComponent.getShield() < 0.0 + noodleT.getShieldOffset() + 0.05 
        		&& shieldComponent.getShield() > 0.0 + noodleT.getShieldOffset() - 0.05);
        
        // Checks the invulnerability status of the noodle
        InvulnerableComponent invulnerableComponent = miniWorld.getComponent(player, InvulnerableComponent.class)
        		.get();
        assertFalse(invulnerableComponent.isInvulnerable());

        // Checks DamageComponent defaults. NOODLES DO NOT CHANGE THIS
        DamageComponent damageComponent = miniWorld.getComponent(player, DamageComponent.class).get();
        assertTrue(damageComponent.getDamage() < 100 + 0.05 
        		&& damageComponent.getDamage() < 100 + 0.05);
        
        // Check the ManaComponent defaults with respect to the noodle
        ManaComponent manaComponent = miniWorld.getComponent(player, ManaComponent.class).get();
        assertEquals(manaComponent.getCapacity(), 500 + noodleT.getManaOffset());
        // Mana may have be consumed shortly after spawning
        assertTrue(manaComponent.getMana() <= 500 + noodleT.getManaOffset());
    }
    
    /**
     * Makes a tank noodle (Juggernaught) and tests AI defaults that are exclusive to it.
     */
    @Test
    public void baseDefaultsTankAI() {
    	final int xPosition = 20;
    	final int playerID = 1;
    	final NoodleEnum noodleT = NoodleEnum.NOODLE_TANK;
    	final TeamEnum team = TeamEnum.TEAM_BRAVO;
    	
    	// Making world and noodle tank to go in it
    	World miniWorld = new World(0, 0);
        Entity bot = PlayerEntities.createPlayer(miniWorld,
                noodleT, true, team, playerID, xPosition);
        
        // Checks the AI intialised state
        AIComponent aiComponent = miniWorld.getComponent(bot, AIComponent.class).get();
        assertTrue(aiComponent.getTargetPosition() == null);
        assertTrue(aiComponent.getWeaponPosition() == null);
        
        // Not sure how to test this last one
        //assertTrue(aiComponent.getProjectileSpeed() >= 0.0);
    }
    
    /**
     * Tests Interaction between health and status bar components of the entity.
     */
    @Test
    public void prefabHealthBar() {
    	
        World miniWorld = new World(0, 0);
        Entity player = PlayerEntities.createPlayer(miniWorld,
                NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_ALPHA, 0, 0);

        Optional<StatusBarComponent> healthBar = miniWorld.getComponent(player,
                StatusBarComponent.class);
        Optional<HealthComponent> health = miniWorld.getComponent(player,
                HealthComponent.class);

        if (health.isPresent() && healthBar.isPresent()) {
            int value = 50;
            // Set BOTH HealthComponent and StatusBarComponent
            health.get().setHealth(value);
            healthBar.get().setValue(value);

            assertEquals(value, healthBar.get().getValue());
        } else {
            Assert.fail();
        }
    }
    
    /**
     * Tests the killing of a noodle entity has the correct components removed and remaining.
     */
    @Test
    public void killNoodleComponentsPresent() {
    	World miniWorld = new World(0, 0);
        Entity player = PlayerEntities.createPlayer(miniWorld,
                NoodleEnum.NOODLE_WARRIOR, false, TeamEnum.TEAM_DELTA, 0, 0);
        
        // Transition the noodle to dead phase
        PlayerEntities.createDeadPlayer(player);
        
        // Checks that the required components are still present
        assertTrue(miniWorld.hasComponent(player, PositionComponent.class));
        assertTrue(miniWorld.hasComponent(player, NameComponent.class));
        assertTrue(miniWorld.hasComponent(player, InvulnerableComponent.class));
        assertTrue(miniWorld.hasComponent(player, SpriteComponent.class));
        
        // Checks that unneeded components were removed
        assertFalse(miniWorld.hasComponent(player, StatusBarComponent.class));
        assertFalse(miniWorld.hasComponent(player, SpeedComponent.class));
        assertFalse(miniWorld.hasComponent(player, DamageComponent.class));
        assertFalse(miniWorld.hasComponent(player, ShieldComponent.class));
        assertFalse(miniWorld.hasComponent(player, ManaComponent.class));
        assertFalse(miniWorld.hasComponent(player, EffectDisplayComponent.class));
        assertFalse(miniWorld.hasComponent(player, HealthComponent.class));
        assertFalse(miniWorld.hasComponent(player, AIComponent.class));
        assertFalse(miniWorld.hasComponent(player, InputComponent.class));
        
        assertFalse(miniWorld.hasComponent(player, CollisionComponent.class));
        assertFalse(miniWorld.hasComponent(player, MovementComponent.class));
        assertFalse(miniWorld.hasComponent(player, GravityComponent.class));
        assertFalse(miniWorld.hasComponent(player, MassComponent.class));
        assertFalse(miniWorld.hasComponent(player, RectangleComponent.class));
    }
    
    /**
     * Tests all possible character death states
     */
    @Test
    public void killNoodleSpriteChange() {
    	World miniWorld = new World(0, 0);
    	int angleCount = 0;
    	for (int i = 0; i < colourExtensions.length; ++i) {


    		Entity player = PlayerEntities.createPlayer(miniWorld,
    				NoodleEnum.NOODLE_WARRIOR, false, TeamEnum.values()[i], 0, 0);

    		// Transition the noodle to dead phase
    		PlayerEntities.createDeadPlayer(player);

    		// Checks that the post death changes have been made.
    		// Checking name was updated
    		NameComponent name = miniWorld.getComponent(player, NameComponent.class).get();
    		assertTrue(name.getName().startsWith("The late\n"));
    		
    		// Tests the noodle sprite has been changed to the correct colour dead sprite or a gravestone.
    		// Due to randomness, both states are not tested in each test run.
    		SpriteComponent sprite = miniWorld.getComponent(player, SpriteComponent.class).get();
    		assertTrue(sprite.getImage().equals("/characters/dead" + colourExtensions[i] + ".png") || 
    				sprite.getImage().equals("/characters/grave.png"));
    		
    		// Increment count if the dead noodle made a grave with angel.
    		angleCount += sprite.getImage().contains("grave") ? 1 : 0;
    	}
    	
    	// Checking that there is the expected number of angels in the world
    	if (angleCount > 0) { // Needs to be tested due to limitation
    		assertTrue(miniWorld.getComponents(AngelComponent.class).size() == angleCount);
    	}
    }
}
