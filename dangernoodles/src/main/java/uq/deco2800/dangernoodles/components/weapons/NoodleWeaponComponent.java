package uq.deco2800.dangernoodles.components.weapons;

import uq.deco2800.dangernoodles.ecs.Component;
import uq.deco2800.dangernoodles.ecs.Entity;

/**
 * All noodles (player and AI) should add a NoodleWeaponComponent to themselves
 * This provides the noodle with a link to the entity representing their weapon
 * 
 * @author danyon
 *
 */
public class NoodleWeaponComponent extends Component {
    private Entity weapon;

    /**
     * Constructor for the NoodleWeaponComponent
     * 
     * @param weapon
     *            The weapon that this Noodle is holding
     */
    public NoodleWeaponComponent(Entity weapon) {
        this.weapon = weapon;
    }

    /**
     * Gets the weapon entity related with this Noodle
     * 
     * @return The weapon entity that's related to this Noodle
     */
    public Entity getWeapon() {
        return weapon;
    }
}
