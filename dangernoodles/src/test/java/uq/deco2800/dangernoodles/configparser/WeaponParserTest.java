package uq.deco2800.dangernoodles.configparser;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import uq.deco2800.dangernoodles.weapons.ProjectileDefinition;
import uq.deco2800.dangernoodles.weapons.ProjectileDefinitionList;
import uq.deco2800.dangernoodles.weapons.WeaponDefinition;

/**
 * @author torusse
 *
 */
public class WeaponParserTest {
	
    @Test
    public void testBasicWeapon() {
        WeaponParser parserTest = new WeaponParser(
                "resources/weapons/weapons_test1.xml", 
                new ProjectileDefinitionList(testProjectiles()));
        assertEquals("Correct weapons created", 
                parserTest.getWeaponList(), createTestList1());
    }
    
    @Test
    public void testWeaponWithProjectile() {
        WeaponParser parserTest = new WeaponParser(
                "resources/weapons/weapons_test2.xml",
                new ProjectileDefinitionList(testProjectiles()));
        assertEquals("Correct weapons created", 
                parserTest.getWeaponList(), createTestList2());
    }
    
    @Test
    public void testMultipleWeapons() {
        WeaponParser parserTest = new WeaponParser(
                "resources/weapons/weapons_test3.xml", 
                new ProjectileDefinitionList(testProjectiles()));
        assertEquals("Correct weapons created", 
                parserTest.getWeaponList(), createTestList3());
    }
    
    private ArrayList<WeaponDefinition> createTestList1() {
        ArrayList<WeaponDefinition> weapons = new ArrayList<>();
        weapons.add(new WeaponDefinition(1, "Knife", "Melee", 
        		"resources/weapons/weapons_test1.png", 34, 64, false));
        return weapons;
    }
    
    private ArrayList<WeaponDefinition> createTestList2() {
        ArrayList<WeaponDefinition> weapons = new ArrayList<>();
        weapons.add(new WeaponDefinition(1, "Grenade", "Explosives", "", 20, 
        		10, true, true, 1, testProjectiles().get(2)));
        return weapons;
    }
    
    private ArrayList<WeaponDefinition> createTestList3() {
        ArrayList<WeaponDefinition> weapons = new ArrayList<>();
        weapons.add(new WeaponDefinition(1, "Handgun", "Guns", "", 34, 64,
                false, true, 1, testProjectiles().get(0)));
        weapons.add(new WeaponDefinition(2, "Rocket Launcher", "Guns", "", 10,
        		10, false, true, 1, testProjectiles().get(1)));
        weapons.add(new WeaponDefinition(3, "Grenade", "Explosives", "", 20, 
        		10, true, true, 1, testProjectiles().get(2)));
        return weapons;
    }
    
    private ArrayList<ProjectileDefinition> testProjectiles() {
        ArrayList<ProjectileDefinition> projectiles = new ArrayList<>();
        projectiles.add(new ProjectileDefinition(1, "Test Bullet", "test.png", 
                16, 16, 0.05, 5, false, 0));

        projectiles.add(new ProjectileDefinition(2, "Test Rocket", "test.png", 
                16, 16, 1.50, 25, true, 5000, true, 10));

        projectiles.add(new ProjectileDefinition(3, "Test Grenade", "test.png", 
                16, 16, 0.50, 20, true, 3000, true, 5, true, 4, 
                projectiles.get(0)));
        return projectiles;
    }

}
