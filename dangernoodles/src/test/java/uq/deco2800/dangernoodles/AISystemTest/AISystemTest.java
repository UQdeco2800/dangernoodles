package uq.deco2800.dangernoodles.AISystemTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import uq.deco2800.dangernoodles.components.AIComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.components.weapons.WeaponComponent;
import uq.deco2800.dangernoodles.prefabs.WeaponEntities;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.*;
import uq.deco2800.dangernoodles.systems.AISystem;
import uq.deco2800.dangernoodles.weapons.WeaponDefinition;

public class AISystemTest {

    // Test world to be used to test the component
    private static World testWorld = new World(1100, 800);

    static Entity EEAI = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_PLAIN, true, TeamEnum.TEAM_ALPHA, 1, 600);
    static Entity EEEAI = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_PLAIN, true, TeamEnum.TEAM_ALPHA, 1, 640);
    static Entity AII = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_PLAIN, true, TeamEnum.TEAM_BRAVO, 3, 40);
    static Entity AAII = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_PLAIN, true, TeamEnum.TEAM_BRAVO, 4, 800);
    static Entity EAI = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_PLAIN, true, TeamEnum.TEAM_ALPHA, 2, 300);
    static PositionComponent AIPosition = testWorld.getComponent(AII, PositionComponent.class).get();
    static PositionComponent otherAIPosition = testWorld.getComponent(AAII, PositionComponent.class).get();
    static PositionComponent closeEnemy = testWorld.getComponent(EAI, PositionComponent.class).get();
    static PositionComponent farEnemy = testWorld.getComponent(EEAI, PositionComponent.class).get();
    static PositionComponent fartherEnemy = testWorld.getComponent(EEEAI, PositionComponent.class).get();
    WeaponDefinition weaponDef = new WeaponDefinition(0, "Grenade", "Thrown", "weapons/grenade_left.png",
            15, 15);
    

    @BeforeClass
    public static void setupTest1Class() {
        // Sets up positions for testing case
        AIPosition.setY(40);
        closeEnemy.setY(50);
        otherAIPosition.setY(50);
        farEnemy.setY(300);
        fartherEnemy.setY(320);
    }

    @Test
    public void TestAIActivation() {
        AISystem testAISystem = new AISystem();
        // ensure no AI is active
        testWorld.getComponent(AII, TurnComponent.class).get().clearTurn(10);
        // System.out.println(testWorld.getComponent(AII, TurnComponent.class).get().getTurn());
        assertEquals(testAISystem.activeAI(testWorld), null);
        // manually set an AIComponent to reflect an active AI and test against the function activeAI()
        testWorld.getComponent(AII, TurnComponent.class).get().setTurn(0);
        AIComponent aiComponent = testWorld.getComponent(AII, AIComponent.class).get();
        assertEquals(testAISystem.activeAI(testWorld), aiComponent.getEntity());
        //Sets the AI's target using the SetTargetEntity function and tests it
        testAISystem.setTargetEntity(AII, testAISystem.getPotentialTargets(testWorld, AII), testWorld);
        assertEquals(testWorld.getComponent(AII, AIComponent.class).get().getTargetPosition(), closeEnemy);
    }

    
    @Test
    public void TestSetWeaponNormal() {
    	AISystem testAISystem = new AISystem();
    	//set target and find all possible targets
    	List <PositionComponent> targets = testAISystem.getPotentialTargets(testWorld, AII);
    	testWorld.getComponent(AII, AIComponent.class).get().setDifficulty("normal");
    	
    	testWorld.getComponent(AII, AIComponent.class).get().setTarget(closeEnemy);
    	//determine appropriate weapon for the situation and set it
    	testAISystem.setAIWeapon(testWorld, AII, targets);
    	//Find weapon associated with Noodle entity and ensure the chosen weapon fits the scenario
    	List<WeaponComponent> weapons = testWorld.getComponents(WeaponComponent.class);
    	WeaponComponent weapon = null;
    	for (WeaponComponent w : weapons) {
            if (w.getParent() == AII) {
                weapon = w;
            }
        }
    	assertEquals(weapon.getDefinition().toString(), "{ID: 1 - Frag Grenade}");
    	
    }
    
    @Test
    public void TestSetWeaponTop() {
    	AISystem testAISystem = new AISystem();
    	//set target and find all possible targets
    	List <PositionComponent> targets = testAISystem.getPotentialTargets(testWorld, AII);
    	testWorld.getComponent(AII, AIComponent.class).get().setDifficulty("top");
    	testWorld.getComponent(AII, AIComponent.class).get().setTarget(closeEnemy);
    	//determine appropriate weapon for the situation and set it
    	testAISystem.setAIWeapon(testWorld, AII, targets);
    	//Find weapon associated with Noodle entity and ensure the chosen weapon fits the scenario
    	List<WeaponComponent> weapons = testWorld.getComponents(WeaponComponent.class);
    	WeaponComponent weapon = null;
    	for (WeaponComponent w : weapons) {
            if (w.getParent() == AII) {
                weapon = w;
            }
        }
    	assertEquals(weapon.getDefinition().toString(), "{ID: 6 - Rail Gun}");
    	
    }
    
    
    @Test
    public void TestSetWeapon2Normal(){
    	AISystem testAISystem = new AISystem();
    	//set target and find all possible targets
    	List <PositionComponent> targets = testAISystem.getPotentialTargets(testWorld, AAII);
    	testWorld.getComponent(AAII, AIComponent.class).get().setDifficulty("normal");
    	testWorld.getComponent(AAII, AIComponent.class).get().setTarget(fartherEnemy);
    	//determine appropriate weapon for the situation and set it
    	testAISystem.setAIWeapon(testWorld, AAII, targets);
    	//Find weapon associated with Noodle entity and ensure the chosen weapon fits the scenario
    	List<WeaponComponent> weapons = testWorld.getComponents(WeaponComponent.class);
    	WeaponComponent weapon = null;
    	for (WeaponComponent w : weapons) {
            if (w.getParent() == AAII) {
                weapon = w;
            }
        }
    	//assertEquals(weapon.getDefinition().toString(), "{ID: 3 - Rocket Launcher}");

    	assertEquals(weapon.getDefinition().toString(), "{ID: 4 - Rocket Launcher}");
    }
    
    @Test
    public void TestSetWeapon2Top(){
    	AISystem testAISystem = new AISystem();
    	//set target and find all possible targets
    	List <PositionComponent> targets = testAISystem.getPotentialTargets(testWorld, AAII);
    	testWorld.getComponent(AAII, AIComponent.class).get().setDifficulty("top");
    	testWorld.getComponent(AAII, AIComponent.class).get().setTarget(fartherEnemy);
    	//determine appropriate weapon for the situation and set it
    	testAISystem.setAIWeapon(testWorld, AAII, targets);
    	//Find weapon associated with Noodle entity and ensure the chosen weapon fits the scenario
    	List<WeaponComponent> weapons = testWorld.getComponents(WeaponComponent.class);
    	WeaponComponent weapon = null;
    	for (WeaponComponent w : weapons) {
            if (w.getParent() == AAII) {
                weapon = w;
            }
        }
    	assertEquals(weapon.getDefinition().toString(), "{ID: 3 - Crazy Grenade}");
    }
    
    @Test
    public void TestSetWeapon3Normal(){
    	AISystem testAISystem = new AISystem();
    	//Manually set default AI weapon in this controlled environment
    	WeaponEntities.createWeapon(testWorld,testWorld.getWeaponDefinitions().getWeaponByID(2) , EEEAI);
    	//set target and find all possible targets
    	List <PositionComponent> targets = testAISystem.getPotentialTargets(testWorld, EEEAI);
    	testWorld.getComponent(EEEAI, AIComponent.class).get().setTarget(otherAIPosition);
    	testWorld.getComponent(EEEAI, AIComponent.class).get().setDifficulty("normal");
    	//determine appropriate weapon for the situation and set it
    	testAISystem.setAIWeapon(testWorld, EEEAI, targets);
    	//Find weapon associated with Noodle entity and ensure the chosen weapon fits the scenario
    	List<WeaponComponent> weapons = testWorld.getComponents(WeaponComponent.class);
    	WeaponComponent weapon = null;
    	for (WeaponComponent w : weapons) {
            if (w.getParent() == EEEAI) {
                weapon = w;
            }
        }
    	assertEquals(weapon.getDefinition().toString(), "{ID: 2 - Handgun}");
    	
    }
    @Test
    public void TestSetWeapon3top(){
    	AISystem testAISystem = new AISystem();
    	//Manually set default AI weapon in this controlled environment
    	WeaponEntities.createWeapon(testWorld,testWorld.getWeaponDefinitions().getWeaponByID(2) , EEEAI);
    	//set target and find all possible targets
    	List <PositionComponent> targets = testAISystem.getPotentialTargets(testWorld, EEEAI);
    	testWorld.getComponent(EEEAI, AIComponent.class).get().setTarget(otherAIPosition);
    	testWorld.getComponent(EEEAI, AIComponent.class).get().setDifficulty("top");
    	//determine appropriate weapon for the situation and set it
    	testAISystem.setAIWeapon(testWorld, EEEAI, targets);
    	//Find weapon associated with Noodle entity and ensure the chosen weapon fits the scenario
    	List<WeaponComponent> weapons = testWorld.getComponents(WeaponComponent.class);
    	WeaponComponent weapon = null;
    	for (WeaponComponent w : weapons) {
            if (w.getParent() == EEEAI) {
                weapon = w;
            }
        }
    	assertEquals(weapon.getDefinition().toString(), "{ID: 4 - Rocket Launcher}");
    	
    }
    
    @Test
    public void AISystemIdleTest(){
    	AISystem testAISystem = new AISystem();
    	testAISystem.run(testWorld, 0, 0);
    }

}
