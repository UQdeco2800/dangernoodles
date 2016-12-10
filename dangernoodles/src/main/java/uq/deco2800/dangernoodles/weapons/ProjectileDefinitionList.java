package uq.deco2800.dangernoodles.weapons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uq.deco2800.dangernoodles.configparser.ProjectileParser;

/**
 * @author torusse
 * 
 */
public class ProjectileDefinitionList implements Iterable<ProjectileDefinition> {

    // A list that contains all the projectiles by definition
    private ArrayList<ProjectileDefinition> projectiles;

    /*
     * ProjectileInventory constructor to create a new ProjectileInverntory this
     * function just initialises the list of all the projectiles in the game.
     */
    public ProjectileDefinitionList() {
        this.projectiles = new ArrayList<ProjectileDefinition>(
        		new ProjectileParser("resources/configs/basic_projectiles.xml"
        				).getProjectileList());
    }

    /**
     * Allows the creation of ProjectileDefinitionList with a set of provided
     * ProjectileDefinitions
     * 
     * NOTE: Intended only for testing. If you are using this for something
     * other than testing make sure that you be careful...
     * 
     * @param list
     *            the list of ProjectileDefinitions to be put into the
     *            ProjectileDefinitionList
     */
    public ProjectileDefinitionList(List<ProjectileDefinition> list) {
        this.projectiles = new ArrayList<>(list);
    }


    /**
     * Gets the entire inventory
     * 
     * @return A list of all projectiles in the inventory
     */
    public List<ProjectileDefinition> getInventory() {
        // clone list so as not to make it mutable
        return new ArrayList<ProjectileDefinition>(this.projectiles);

    }

    /**
     * Gets a single projectile by id, if it exists
     * 
     * @param id
     *            The id of the projectile to retrieve
     * @return Returns the ProjectileDefinition with the matching id, if a match is
     *         found. If a match is not found, returns null
     */
    public ProjectileDefinition getProjectileByID(int id) {

        for (ProjectileDefinition projectile : this.projectiles) {
            if (projectile.getId() == id) {
                return projectile;
            }
        }

        throw new ArrayIndexOutOfBoundsException(
                "Id is not recognised within current inventory");

    }

    /**
     * Allows inventory to be iterated over i.e. for (ProjectileDefinition w :
     * inventory) { doStuff(w); }
     */
    @Override
    public Iterator<ProjectileDefinition> iterator() {
        return this.getInventory().iterator();
    }

    /**
     * Get the size of the inventory
     * 
     * @return the number of projectiles in the inventory
     */
    public int size() {
        return this.projectiles.size();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ProjectileDefinitionList) {
            ProjectileDefinitionList other = (ProjectileDefinitionList) o;
            if (this.size() == other.size()) {
                for (ProjectileDefinition projectile : this) {
                    boolean foundMatch = false;
                    for (ProjectileDefinition otherProjectile : other) {
                        if (projectile.equals(otherProjectile)) {
                            foundMatch = true;
                            break;
                        }
                    }
                    if (!foundMatch) {
                        // there's a projectile in this that's not in other
                        return false;
                    }
                }
                // found matches for all projectiles in this
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int sum = 0;
        for (ProjectileDefinition w : this) {
            sum += 29 * w.getId();
        }
        return sum;
    }

    @Override
    public String toString() {
        return this.projectiles.toString();
    }
}
