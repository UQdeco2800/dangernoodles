package uq.deco2800.dangernoodles.configparser;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import uq.deco2800.dangernoodles.weapons.ProjectileDefinition;

/**
 * @author torusse
 *
 */
public class ProjectileParserTest {
    

    @Test
    public void testBasic() {
        ProjectileParser parserTest1 = new ProjectileParser(
                "resources/projectiles/projectiles_test1.xml");
        assertEquals("Incorrect projectiles created", 
        		testList1(), parserTest1.getProjectileList());
    }
    
    @Test
    public void testMaxLife() {
	    ProjectileParser parserTest2 = new ProjectileParser(
	            "resources/projectiles/projectiles_test2.xml");
	    assertEquals("Incorrect projectiles created", 
	    		testList2(), parserTest2.getProjectileList());
    }
    
    @Test
    public void testExplodes() {
	    ProjectileParser parserTest3 = new ProjectileParser(
	            "resources/projectiles/projectiles_test3.xml");
	    assertEquals("Incorrect projectiles created", 
	    		testList3(), parserTest3.getProjectileList());
	    }
    
    @Test
    public void testClusters() {
	    ProjectileParser parserTest4 = new ProjectileParser(
	            "resources/projectiles/projectiles_test4.xml");
	    assertEquals("Incorrect projectiles created", 
	    		testList4(), parserTest4.getProjectileList());
    }
    
    @Test
    public void testAll() {
	    ProjectileParser parserTest5 = new ProjectileParser(
	            "resources/projectiles/projectiles_test5.xml");
	    assertEquals("Incorrect projectiles created", 
	    		testList5(), parserTest5.getProjectileList());
    }
    
    private ArrayList<ProjectileDefinition> testList1() {
        ArrayList<ProjectileDefinition> projectiles = new ArrayList<>();
        projectiles.add(new ProjectileDefinition(1, "Test Bullet", "test.png", 
                16, 16, 0.05, 5));
        return projectiles;
    }
    
    private ArrayList<ProjectileDefinition> testList2() {
        ArrayList<ProjectileDefinition> projectiles = new ArrayList<>();
        projectiles.add(new ProjectileDefinition(1, "Test Bullet", "test.png", 
                16, 16, 0.05, 5, true, 10000));
        return projectiles;
    }
    
    private ArrayList<ProjectileDefinition> testList3() {
        ArrayList<ProjectileDefinition> projectiles = new ArrayList<>();
        projectiles.add(new ProjectileDefinition(1, "Test Rocket", "test.png", 
                16, 16, 1.50, 25, true, 5000, true, 10));
        return projectiles;
    }
    
    private ArrayList<ProjectileDefinition> testList4() {
        ArrayList<ProjectileDefinition> projectiles = new ArrayList<>();
        projectiles.add(new ProjectileDefinition(1, "Test Bullet", "test.png", 
                16, 16, 0.05, 5, false, 0));
        
        projectiles.add(new ProjectileDefinition(2, "Test Grenade", "test.png", 
                16, 16, 0.50, 20, true, 3000, true, 5, true, 4, 
                projectiles.get(0)));
        return projectiles;
    }
    
    private ArrayList<ProjectileDefinition> testList5() {
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
