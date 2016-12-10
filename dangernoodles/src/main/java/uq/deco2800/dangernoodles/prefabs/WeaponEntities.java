package uq.deco2800.dangernoodles.prefabs;

import uq.deco2800.dangernoodles.components.InputComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.components.weapons.NoodleWeaponComponent;
import uq.deco2800.dangernoodles.components.weapons.WeaponComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.weapons.WeaponDefinition;

public final class WeaponEntities {

    /**
     * Private constructor to hide the implicit public one.
     */
    private WeaponEntities() {}

    /**
     * Creates a Weapon Entity in a given World, for a given noodle Entity based
     * on a given WeaponDefinition
     * 
     * @param world
     *            World in which to create weapon
     * @param weaponDef
     *            Definition of weapon to create
     * @param noodle
     *            Noodle Entity that owns the weapon
     * @return The created entity
     */
    public static Entity createWeapon(World world, WeaponDefinition weaponDef,
            Entity noodle) {
        Entity weapon = world.createEntity();
        PositionComponent playerPos = world
                .getComponent(noodle, PositionComponent.class).get();
        weapon.addComponent(new SpriteComponent(weaponDef.getWidth(),
                weaponDef.getHeight(), weaponDef.getImages()))
                .addComponent(new PositionComponent(playerPos.getX(),
                        playerPos.getY()))
                .addComponent(new WeaponComponent(weaponDef, noodle))
                .addComponent(new InputComponent());
        noodle.addComponent(new NoodleWeaponComponent(weapon));
        return weapon;
    }

    /**
     * Removes a Weapon Entity and all related components
     * 
     * @param world
     *            World to remove the weapon from
     * @param weapon
     *            weapon Entity to remove
     */
    public static void removeWeapon(World world, Entity weapon) {
        WeaponComponent weaponComp = world.getComponent(weapon, 
                WeaponComponent.class).orElse(null);
        if (weaponComp != null) {
            world.removeComponent(weaponComp.getParent(), 
                    NoodleWeaponComponent.class);
        }
        world.destroyEntity(weapon);
    }
    
    /**
     * Completely removes a weapon from the world, if the given player has one
     * 
     * @param world
     *            The world from which to remove the weapon
     * @param player
     *            The player that owns the weapon to be removed
     */
    public static void removePlayersWeapon(World world, Entity player) {
        NoodleWeaponComponent noodleWeapon = world
                .getComponent(player, NoodleWeaponComponent.class).orElse(null);
        if (noodleWeapon != null) {
            Entity weapon = noodleWeapon.getWeapon();
            removeWeapon(world, weapon);
        }
    }
}
