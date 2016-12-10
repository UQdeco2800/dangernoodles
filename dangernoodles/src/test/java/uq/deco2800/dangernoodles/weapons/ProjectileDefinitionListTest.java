package uq.deco2800.dangernoodles.weapons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ProjectileDefinitionListTest {
    private ArrayList<ProjectileDefinition> createTestList() {
        ArrayList<ProjectileDefinition> projectiles = new ArrayList<>();
        projectiles.add(new ProjectileDefinition(1, "Test Bullet", "test.png",
                16, 16, 0.05, 5, false, 0));

        projectiles.add(new ProjectileDefinition(2, "Test Rocket", "test.png",
                16, 16, 1.50, 25, true, 5000, true, 10));

        projectiles.add(new ProjectileDefinition(3, "Test Grenade", "test.png",
                16, 16, 0.50, 20, true, 3000, true, 5));

        return projectiles;
    }
    
    @Test
    public void testInit() {
    	new ProjectileDefinitionList();
    }

    @Test
    public void testGetById() {
        ArrayList<ProjectileDefinition> actual = createTestList();
        ProjectileDefinitionList projectiles = new ProjectileDefinitionList(
                actual);
        assertEquals("ProjectileDefinitionList has unexpected size", 
                actual.size(), projectiles.size());
        for (int i = 1; i <= actual.size(); i++) {
            assertEquals("Getting by ID didn't work for id: " + i, actual.get(
                    i - 1), projectiles.getProjectileByID(i));
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetBadId() {
        ProjectileDefinitionList projectiles = new ProjectileDefinitionList(
                createTestList());
        projectiles.getProjectileByID(-1);
    }

    @Test
    public void testGetInventory() {
        ArrayList<ProjectileDefinition> actual = createTestList();
        ProjectileDefinitionList projectiles = new ProjectileDefinitionList(
                actual);
        List<ProjectileDefinition> inv = projectiles.getInventory();
        for (ProjectileDefinition projectile : actual) {
            assertTrue("Returned inventory does not contain expected", inv
                    .contains(projectile));
        }
        for (ProjectileDefinition projectile : inv) {
            assertTrue("Returned inventory contains unexpected projectile",
                    actual.contains(projectile));
        }
    }

    @Test
    public void testEquals() {
        ArrayList<ProjectileDefinition> actual = createTestList();
        ProjectileDefinitionList projectiles = new ProjectileDefinitionList(
                actual);
        actual.sort((t, o) -> t.getName().compareTo(o.getName()));
        // sort the array by name (different order)
        ProjectileDefinitionList otherProjectiles = new ProjectileDefinitionList(
                actual);
        assertFalse("Should not be equal", projectiles.equals(actual));
        assertFalse("Should not be equal", projectiles.equals(null));
        assertFalse("Should not be equal", projectiles.equals("hello"));
        assertTrue("Should be equal", projectiles.equals(otherProjectiles));
        assertTrue("Should be equal", otherProjectiles.equals(projectiles));
    }

    @Test
    public void testHashCode() {
        ArrayList<ProjectileDefinition> actual = createTestList();
        ProjectileDefinitionList projectiles = new ProjectileDefinitionList(
                actual);
        ProjectileDefinitionList otherProjectiles = (
                new ProjectileDefinitionList(actual));
        assertEquals("Should be equal", projectiles.hashCode(), otherProjectiles
                .hashCode());
    }

}
