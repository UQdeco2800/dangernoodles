package uq.deco2800.dangernoodles.weapons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class WeaponDefinitionListTest {
    private ArrayList<WeaponDefinition> createTestList() {
        ArrayList<WeaponDefinition> weapons = new ArrayList<>();
        int i = 0;
        weapons.add(new WeaponDefinition(i++, "uzi", "ProjectileWeapon", "",
                100, 10));
        weapons.add(
                new WeaponDefinition(i++, "stick", "MeleeWeapon", "", 100, 10));
        weapons.add(new WeaponDefinition(i++, "Baseball Bat", "MeleeWeapon", "",
                100, 10));
        weapons.add(
                new WeaponDefinition(i++, "Knife", "MeleeWeapon", "", 100, 10));
        weapons.add(new WeaponDefinition(i++, "Machine Gun", "ProjectileWeapon",
                "", 100, 10));
        weapons.add(new WeaponDefinition(i++, "Bazooka", "ProjectileWeapon", "",
                100, 10));
        weapons.add(new WeaponDefinition(i++, "Broadsword", "MeleeWeapon", "",
                100, 10));
        weapons.add(new WeaponDefinition(i++, "Grenade", "ProjectileWeapon", "",
                100, 10));
        weapons.add(new WeaponDefinition(i++, "Grenade Launcher",
                "ProjectileWeapon", "", 100, 10));
        weapons.add(new WeaponDefinition(i++, "Handgun", "ProjectileWeapon", "",
                100, 10));
        return weapons;
    }
    
    // Make sure that you don't throw errors in the constructor
    @Test
    public void testInit() {
    	new WeaponDefinitionList();
    }

    @Test
    public void testGetByCategory() {
        WeaponDefinitionList weapons = new WeaponDefinitionList(
                createTestList());
        List<WeaponDefinition> guns = weapons
                .getByCategory("ProjectileWeapon");
        List<WeaponDefinition> melee = weapons
                .getByCategory("MeleeWeapon");
        assertEquals("Correct number of guns", guns.size(), 6);
        assertEquals("Correct number of melee weapons", melee.size(), 4);
        assertEquals("Correct number of weapons", weapons.size(), 10);
    }

    @Test
    public void testGetCategories() {
        WeaponDefinitionList weapons = new WeaponDefinitionList(
                createTestList());
        List<String> categories = weapons.getAllCategories();
        ArrayList<String> actual = new ArrayList<>();
        actual.add("ProjectileWeapon");
        actual.add("MeleeWeapon");
        assertEquals("Not the correct categories", categories, actual);
    }

    @Test
    public void testGetById() {
        ArrayList<WeaponDefinition> actual = createTestList();
        WeaponDefinitionList weapons = new WeaponDefinitionList(actual);
        assertEquals("WeaponDefinitionList has unexpected size", actual.size(),
                weapons.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals("Getting by ID didn't work for id: " + i,
                    actual.get(i), weapons.getWeaponByID(i));
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetBadId() {
        WeaponDefinitionList weapons = new WeaponDefinitionList(
                createTestList());
        weapons.getWeaponByID(-1);
    }

    @Test
    public void testContainsCategory() {
        WeaponDefinitionList weapons = new WeaponDefinitionList(
                createTestList());
        assertTrue("Doesn't contain expected category",
                weapons.containsCategory("MeleeWeapon"));
        assertTrue("Doesn't contain expected category",
                weapons.containsCategory("ProjectileWeapon"));
        assertFalse("Contains unexpected category",
                weapons.containsCategory(""));
        assertFalse("Contains unexpected category",
                weapons.containsCategory("MeleeWeapons"));
        assertFalse("Contains unexpected category",
                weapons.containsCategory("ProjectileWeapons"));
        assertFalse("Contains unexpected category",
                weapons.containsCategory("bob"));
        assertFalse("Contains unexpected category",
                weapons.containsCategory(null));
    }

    @Test
    public void testGetInventory() {
        ArrayList<WeaponDefinition> actual = createTestList();
        WeaponDefinitionList weapons = new WeaponDefinitionList(actual);
        List<WeaponDefinition> inv = weapons.getInventory();
        for (WeaponDefinition weapon : actual) {
            assertTrue("Returned inventory does not contain expected",
                    inv.contains(weapon));
        }
        for (WeaponDefinition weapon : inv) {
            assertTrue("Returned inventory contains unexpected weapon",
                    actual.contains(weapon));
        }
    }

    @Test
    public void testEquals() {
        ArrayList<WeaponDefinition> actual = createTestList();
        WeaponDefinitionList weapons = new WeaponDefinitionList(actual);
        actual.sort((t, o) -> t.getName().compareTo(o.getName()));
        // sort the array by name (different order)
        WeaponDefinitionList otherWeapons = new WeaponDefinitionList(actual);
        assertFalse("Should not be equal", weapons.equals(actual));
        assertFalse("Should not be equal", weapons.equals(null));
        assertFalse("Should not be equal", weapons.equals("hello"));
        assertTrue("Should be equal", weapons.equals(otherWeapons));
        assertTrue("Should be equal", otherWeapons.equals(weapons));
    }

    @Test
    public void testHashCode() {
        ArrayList<WeaponDefinition> actual = createTestList();
        WeaponDefinitionList weapons = new WeaponDefinitionList(actual);
        WeaponDefinitionList otherWeapons = new WeaponDefinitionList(actual);
        assertEquals("Should be equal", weapons.hashCode(),
                otherWeapons.hashCode());
    }
    
    @Test
    public void testNextFunction() {
        List<WeaponDefinition> listOfWeapons = createTestList();
        WeaponDefinitionList wdl = new WeaponDefinitionList(listOfWeapons);
        
        int last = listOfWeapons.get(0).getID(); // start with 1st element
        
        for (int i = 0; i < wdl.size(); i++) {
            WeaponDefinition wdef = wdl.next(last);
            assertEquals("IDs not equal", wdef.getID(), 
                    listOfWeapons.get((i + 1) % listOfWeapons.size()).getID()
            );
            last = wdef.getID();
        }
    }
    
    @Test
    public void testPreviousFunction() {
        List<WeaponDefinition> listOfWeapons = createTestList();
        WeaponDefinitionList wdl = new WeaponDefinitionList(listOfWeapons);
        // start with last element
        int last = listOfWeapons.get(listOfWeapons.size() - 1).getID(); 
        
        for (int i = wdl.size() - 1; i >= 0; i--) {
            WeaponDefinition wdef = wdl.previous(last);
            int index = i > 0 ? i - 1 : wdl.size() - 1;
            assertEquals("IDs not equal", wdef.getID(), 
                    listOfWeapons.get(index).getID()
            );
            last = wdef.getID();
        }
    }

}
