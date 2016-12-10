package uq.deco2800.dangernoodles.weapons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Run of the mill set of test that test the getters of WeaponDefinition objects
 * using constructors (mostly testing the default values of some getters)
 * 
 * @author danyon
 *
 */
public class WeaponDefinitionTest {

    @Test
    public void testFirstConstructor() {
        WeaponDefinition weapon = new WeaponDefinition(1, "Machine gun", "Guns",
                "", 30, 100);
        assertEquals("ID getter is wrong", weapon.getID(), 1);
        assertEquals("Name getter is wrong", weapon.getName(), "Machine gun");
        assertEquals("Category getter is wrong", weapon.getCategory(), "Guns");
        assertEquals("The WIDTH getter is wrong", weapon.getWidth(), 30);
        assertEquals("The HEIGHT getter is wrong", weapon.getHeight(), 100);
        assertEquals("The sprite map getter is wrong", weapon.getImages(),
                "");
        assertFalse("The powered check is wrong", weapon.isPowered());
        assertFalse("Weapon should not make projectiles",
                weapon.makesProjectiles());
        assertNull("Weapon should not have a projectile definition",
                weapon.getProjectileType());
        assertEquals("Weapon should produce no projectiles",
                weapon.getNumOfProjectiles(), 0);
    }

    @Test
    public void testSecondConstructor() {
        WeaponDefinition weapon = new WeaponDefinition(1, "Machine gun", "Guns",
                "", 30, 100, false);
        assertEquals("ID getter is wrong", weapon.getID(), 1);
        assertEquals("Name getter is wrong", weapon.getName(), "Machine gun");
        assertEquals("Category getter is wrong", weapon.getCategory(), "Guns");
        assertEquals("The WIDTH getter is wrong", weapon.getWidth(), 30);
        assertEquals("The HEIGHT getter is wrong", weapon.getHeight(), 100);
        assertEquals("The sprite map getter is wrong", weapon.getImages(),
                "");
        assertFalse("The powered check is wrong", weapon.isPowered());
        assertFalse("Weapon should not make projectiles",
                weapon.makesProjectiles());
        assertNull("Weapon should not have a projectile definition",
                weapon.getProjectileType());
        assertEquals("Weapon should produce no projectiles",
                weapon.getNumOfProjectiles(), 0);

        weapon = new WeaponDefinition(2, "Uzi", "Guns",
                "", 70, 25, true);
        assertEquals("ID getter is wrong", weapon.getID(), 2);
        assertEquals("Name getter is wrong", weapon.getName(), "Uzi");
        assertEquals("Category getter is wrong", weapon.getCategory(), "Guns");
        assertEquals("The WIDTH getter is wrong", weapon.getWidth(), 70);
        assertEquals("The HEIGHT getter is wrong", weapon.getHeight(), 25);
        assertEquals("The sprite map getter is wrong", weapon.getImages(),
                "");
        assertTrue("The powered check is wrong", weapon.isPowered());
        assertFalse("Weapon should not make projectiles",
                weapon.makesProjectiles());
        assertNull("Weapon should not have a projectile definition",
                weapon.getProjectileType());
        assertEquals("Weapon should produce no projectiles",
                weapon.getNumOfProjectiles(), 0);
    }

    @Test
    public void testThirdConstructor() {
        ProjectileDefinition projectile = new ProjectileDefinition(1, "", "", 0,
                0, 0.0, 0);
        WeaponDefinition weapon = new WeaponDefinition(1, "Machine gun", "Guns",
                "", 30, 100, false, false, 0, null);
        assertEquals("ID getter is wrong", weapon.getID(), 1);
        assertEquals("Name getter is wrong", weapon.getName(), "Machine gun");
        assertEquals("Category getter is wrong", weapon.getCategory(), "Guns");
        assertEquals("The WIDTH getter is wrong", weapon.getWidth(), 30);
        assertEquals("The HEIGHT getter is wrong", weapon.getHeight(), 100);
        assertEquals("The sprite map getter is wrong", weapon.getImages(),
                "");
        assertFalse("The powered check is wrong", weapon.isPowered());
        assertFalse("Weapon should not make projectiles",
                weapon.makesProjectiles());
        assertEquals("Weapon should produce no projectiles",
                weapon.getNumOfProjectiles(), 0);
        assertNull("Weapon should not have a projectile definition",
                weapon.getProjectileType());

        weapon = new WeaponDefinition(1, "Banana", "Guns",
                "", 30, 100, true, false, 0, null);
        assertEquals("ID getter is wrong", weapon.getID(), 1);
        assertEquals("Name getter is wrong", weapon.getName(), "Banana");
        assertEquals("Category getter is wrong", weapon.getCategory(), "Guns");
        assertEquals("The WIDTH getter is wrong", weapon.getWidth(), 30);
        assertEquals("The HEIGHT getter is wrong", weapon.getHeight(), 100);
        assertEquals("The sprite map getter is wrong", weapon.getImages(),
                "");
        assertTrue("The powered check is wrong", weapon.isPowered());
        assertFalse("Weapon should not make projectiles",
                weapon.makesProjectiles());
        assertEquals("Weapon should produce no projectiles",
                weapon.getNumOfProjectiles(), 0);
        assertNull("Weapon should not have a projectile definition",
                weapon.getProjectileType());

        weapon = new WeaponDefinition(1, "Sheep", "Ranged",
                "", 30, 100, true, true, 1,
                projectile);
        assertEquals("ID getter is wrong", weapon.getID(), 1);
        assertEquals("Name getter is wrong", weapon.getName(), "Sheep");
        assertEquals("Category getter is wrong", weapon.getCategory(),
                "Ranged");
        assertEquals("The WIDTH getter is wrong", weapon.getWidth(), 30);
        assertEquals("The HEIGHT getter is wrong", weapon.getHeight(), 100);
        assertEquals("The sprite map getter is wrong", weapon.getImages(),
                "");
        assertTrue("The powered check is wrong", weapon.isPowered());
        assertTrue("Weapon should not make projectiles",
                weapon.makesProjectiles());
        assertEquals("Weapon should produce no projectiles",
                weapon.getNumOfProjectiles(), 1);
        assertEquals("Weapon should not have a projectile definition",
                weapon.getProjectileType(), projectile);
    }
}
