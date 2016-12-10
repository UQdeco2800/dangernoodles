/**
 * 
 */
package uq.deco2800.dangernoodles.weapons;

import java.util.*;

import uq.deco2800.dangernoodles.configparser.WeaponParser;

/**
 * @author nkululekojonas
 * 
 */
public class WeaponDefinitionList implements Iterable<WeaponDefinition> {

    // A list that contains all the weapons by definition
    private ArrayList<WeaponDefinition> weapons;

    /*
     * WeaponInventory constructor to create a new WeaponInverntory this
     * function just initialises the list of all the weapons in the game.
     */
    public WeaponDefinitionList() {
    	this.weapons = new ArrayList<WeaponDefinition>();
    	
        this.weapons.addAll(new WeaponParser(
    			"resources/configs/basic_weapons.xml", 
    			new ProjectileDefinitionList()).getWeaponList());
    }

    /**
     * Allows the creation of WeaponDefinitionList with a set of provided
     * WeaponDefinitions
     * 
     * NOTE: Intended only for testing. If you are using this for something
     * other than testing make sure that you be careful...
     * 
     * @param list
     *            the list of WeaponDefinitions to be put into the
     *            WeaponDefinitionList
     */
    public WeaponDefinitionList(List<WeaponDefinition> list) {
        this.weapons = new ArrayList<>(list);
    }

    /**
     * Returns all weapon definitions that fit the category requested
     * 
     * @param category
     *            The category of weapon to search for
     * @return A list of all the WeaponDefinitions, d, in inventory such that
     *         d.category == category
     */
    public List<WeaponDefinition> getByCategory(String category) {
        List<WeaponDefinition> returned = new ArrayList<WeaponDefinition>();
        
        if (category == null) {
            return returned;
        }
        
        for (WeaponDefinition weapon : this.weapons) {
            if (weapon.getCategory().toLowerCase().equals(
                    category.toLowerCase())) {
                returned.add(weapon);
            }
        }
        return returned;
    }

    /**
     * Returns a list of all categories for the currently loaded weapon
     * definitions
     * 
     * @return A List<String>, list, such that for all categories, c, in list
     *         there exists a weapon in inventory such that weapon.category == c
     */
    public List<String> getAllCategories() {

        List<String> returned = new ArrayList<String>();
        int size = this.weapons.size();

        for (int i = 0; i < size; i++) {
            String category = this.weapons.get(i).getCategory();

            if (returned.contains(category)) {
                i++;
            } else {
                returned.add(category);
                i++;
            }
        }

        return returned;
    }

    /**
     * Checks if category exists in weapon inventory
     * 
     * @param category
     *            The category to search for
     * @return True if there exists a weapon in inventory such that
     *         weapon.category == category, else returns false
     */
    public boolean containsCategory(String category) {
        if (category == null) {
            return false;
        }
        
        for (WeaponDefinition weapon : this.weapons) {
            if (weapon.getCategory().toLowerCase()
                    .equals(category.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the entire inventory
     * 
     * @return A list of all weapons in the inventory
     */
    public List<WeaponDefinition> getInventory() {
        // clone list so as not to make it mutable
        return new ArrayList<WeaponDefinition>(this.weapons);

    }

    /**
     * Gets a single weapon by id, if it exists
     * 
     * @param id
     *            The id of the weapon to retrieve
     * @return Returns the WeaponDefinition with the matching id, if a match is
     *         found. If a match is not found, returns null
     */
    public WeaponDefinition getWeaponByID(int id) {

        for (WeaponDefinition weapon : this.weapons) {
            if (weapon.getID() == id) {
                return weapon;
            }
        }

        throw new ArrayIndexOutOfBoundsException(
                "Id is not recognised within current inventory");

    }
    
    /**
     * Gets the "next" WeaponDefintion by ID (i.e. the non-inclusive successor
     * of lastID) if one exists, or the "first" (i.e. lowest id) if a successor
     * doesn't exist
     * 
     * @param lastID
     *            The ID of the last weapon used
     * @return The next WeaponDefinition in the list
     */
    public WeaponDefinition next(int lastID) {
        int minID = this.weapons.get(0).getID();
        int successor = lastID;
        for (WeaponDefinition wdef : this.weapons) {
            int thisID = wdef.getID();
            // if this id is smaller than the min so far, update it
            if (minID > thisID) {
                minID = thisID;
            }
            // if this id is bigger than the last id, and it's either smaller 
            // than the current successor or the current successor hasn't been
            // updated, update the successor
            if (thisID > lastID && 
                    (successor == lastID || thisID < successor)) {
                successor = thisID;
            }
        }
        if (successor == lastID) {
            // successor was never updated - use the min
            return getWeaponByID(minID);
        }
        return getWeaponByID(successor);
    }
    
    /**
     * Gets the "previous" WeaponDefintion by ID (i.e. the non-inclusive
     * predecessor of lastID) if one exists, or the "last" (i.e. lowest id) if a
     * predecessor doesn't exist
     * 
     * @param lastID
     *            The ID of the last weapon used
     * @return The previous WeaponDefinition in the list
     */
    public WeaponDefinition previous(int lastID) {
        int maxID = this.weapons.get(0).getID();
        int predecessor = lastID;
        for (WeaponDefinition wdef : this.weapons) {
            int thisID = wdef.getID();
            // if this id is bigger than the max so far, update it
            if (maxID < thisID) {
                maxID = thisID;
            }
            // if this id is smaller than the last id, and it's either bigger 
            // than the current predecessor or the current predecessor hasn't
            // been updated, update the predecessor
            if (thisID < lastID && 
                    (predecessor == lastID || thisID > predecessor)) {
                predecessor = thisID;
            }
        }
        if (predecessor == lastID) {
            // successor was never updated - use the max
            return getWeaponByID(maxID);
        }
        return getWeaponByID(predecessor);
    }

    /**
     * Allows inventory to be iterated over i.e. for (WeaponDefinition w :
     * inventory) { doStuff(w); }
     */
    @Override
    public Iterator<WeaponDefinition> iterator() {
        return this.getInventory().iterator();
    }

    /**
     * Get the size of the inventory
     * 
     * @return the number of weapons in the inventory
     */
    public int size() {
        return this.weapons.size();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof WeaponDefinitionList) {
            WeaponDefinitionList other = (WeaponDefinitionList) o;
            if (this.size() == other.size()) {
                for (WeaponDefinition weapon : this) {
                    boolean foundMatch = false;
                    for (WeaponDefinition otherWeapon : other) {
                        if (weapon.equals(otherWeapon)) {
                            foundMatch = true;
                            break;
                        }
                    }
                    if (!foundMatch) {
                        // there's a weapon in this that's not in other
                        return false;
                    }
                }
                // found matches for all weapons in this
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int sum = 0;
        for (WeaponDefinition w : this) {
            sum += 31 * w.getID();
        }
        return sum;
    }

    @Override
    public String toString() {
        return this.weapons.toString();
    }
}
