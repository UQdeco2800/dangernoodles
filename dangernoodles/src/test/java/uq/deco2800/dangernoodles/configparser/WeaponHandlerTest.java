package uq.deco2800.dangernoodles.configparser;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import uq.deco2800.dangernoodles.weapons.ProjectileDefinition;
import uq.deco2800.dangernoodles.weapons.ProjectileDefinitionList;
import uq.deco2800.dangernoodles.weapons.WeaponDefinition;

public class WeaponHandlerTest {

    @Test
    public void testInitialiseEmpty() {
    	new WeaponHandler(null);
    }
    
    @Test
    public void testBasicWeapon() {
    	assertEquals("Incorrect weapon created", weapons().get(0), 
    			basicTestEnvironment().getWeaponList().get(0));
    }
    
    @Test
    public void testProjectileWeapon() {
    	assertEquals("Incorrect weapon created", weapons().get(1), 
    			projectileTestEnvironment().getWeaponList().get(0));
    }
    
    private WeaponHandler basicTestEnvironment() {
		WeaponHandler defaultHandler = new WeaponHandler(
				new ProjectileDefinitionList(testProjectiles()));
		
		String[] tags = { "name", "category", "spritesheet",
				"HEIGHT", "WIDTH", "powered", "makesProjectiles", 
				"numOfProjectiles", "projectileType" };
		
		String[] entry1 = { "Knife", "Melee", 
				"resources/weapons/weapons_test1.png", "34", "64", "false" };

		AttributesImpl attributes1 = new AttributesImpl();
		attributes1.addAttribute("", "", "id", "", "1");
		Attributes attribute1 = (Attributes) attributes1;
		
		try {
			defaultHandler.startElement("", "", "weapon", attribute1);
		} catch (Exception e) { }
		for (int i = 0; i < 6; i++) {
			defaultHandler.tagProcessor(tags[i], entry1[i]);
		}
		try {
			defaultHandler.endElement("", "", "weapon");
		} catch (Exception e) { }
		
		return defaultHandler;
	}
    
	private WeaponHandler projectileTestEnvironment() {
		WeaponHandler defaultHandler = new WeaponHandler(
				new ProjectileDefinitionList(testProjectiles()));
		
		String[] tags = { "name", "category", "spritesheet",
				"HEIGHT", "WIDTH", "powered", "makesProjectiles", 
				"numOfProjectiles", "projectileType" };
		
		String[] entry1 = { "Handgun", "Guns", "weapons_test1.png", "10", "20",
				"false", "true", "1", "1" };

		AttributesImpl attributes1 = new AttributesImpl();
		attributes1.addAttribute("", "", "id", "", "2");
		Attributes attribute1 = (Attributes) attributes1;
		
		try {
			defaultHandler.startElement("", "", "weapon", attribute1);
		} catch (Exception e) { }
		for (int i = 0; i < 9; i++) {
			defaultHandler.tagProcessor(tags[i], entry1[i]);
		}
		try {
			defaultHandler.endElement("", "", "weapon");
		} catch (Exception e) { }
		
		return defaultHandler;
	}
	
	private ArrayList<WeaponDefinition> weapons() {
		ArrayList<WeaponDefinition> weapons = new ArrayList<>();
		weapons.add(new WeaponDefinition(1, "Knife", "Melee",
				"resources/weapons/weapons_test1.png", 34, 64, false));
        weapons.add(new WeaponDefinition(2, "Handgun", "Guns", 
        		"weapons_test1.png", 10, 20, false, true, 1, 
        		testProjectiles().get(0)));
        return weapons;
	}
	
    private ArrayList<ProjectileDefinition> testProjectiles() {
        ArrayList<ProjectileDefinition> projectiles = new ArrayList<>();
        projectiles.add(new ProjectileDefinition(1, "Test Bullet", "test.png", 
                16, 16, 0.05, 5, false, 0));
        return projectiles;
    }

}
