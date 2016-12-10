package uq.deco2800.dangernoodles.prefabs;

import static org.junit.Assert.*;

import org.junit.Test;

import uq.deco2800.dangernoodles.components.InputComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.components.weapons.NoodleWeaponComponent;
import uq.deco2800.dangernoodles.components.weapons.WeaponComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.weapons.WeaponDefinition;
import uq.deco2800.dangernoodles.weapons.WeaponDefinitionList;

public class WeaponEntitiesTest {
    
    @Test
    public void testBasicWeapon() {
        World world = new World(1000, 1000);
        Entity player = PlayerEntities.createPlayer(world,
                NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_ALPHA, 0, 0);
        WeaponDefinition wdef = new WeaponDefinition(1, "", "","", 0, 0);
        Entity weapon = WeaponEntities.createWeapon(world, wdef, player);

        assertTrue("Expected a weapon to have component",
                world.getComponent(weapon, SpriteComponent.class).isPresent());
        assertTrue("Expected a weapon to have component", world
                .getComponent(weapon, PositionComponent.class).isPresent());
        assertTrue("Expected a weapon to have component",
                world.getComponent(weapon, WeaponComponent.class).isPresent());

        WeaponComponent wc = world.getComponent(weapon, WeaponComponent.class)
                .get();
        assertEquals("Expected different owner", weapon, wc.getEntity());
        assertEquals("Expected different parent", player, wc.getParent());
        assertEquals("Expected different definition", wdef, wc.getDefinition());
        assertFalse("Expected different isFired value", wc.isFired());
        assertTrue("Expected different power value",
                Math.abs(0.0 - wc.getPower()) < 0.001);
        assertTrue("Expected different direction value",
                Math.abs(0.0 - wc.getDirection()) < 0.001);

        PositionComponent wpc = world
                .getComponent(weapon, PositionComponent.class).get();
        PositionComponent npc = world
                .getComponent(player, PositionComponent.class).get();
        assertTrue("Expected different x position",
                Math.abs(npc.getX() - wpc.getX()) < 0.001);
        assertTrue("Expected different y position",
                Math.abs(npc.getY() - wpc.getY()) < 0.001);

        wc.setDirection(10.1);
        wc.setFired(true);
        wc.setPower(100.1);
        assertTrue("Expected different isFired value", wc.isFired());
        assertTrue("Expected different power value",
                Math.abs(100.1 - wc.getPower()) < 0.001);
        assertTrue("Expected different direction value",
                Math.abs(10.1 - wc.getDirection()) < 0.001);
        assertTrue("Expected a weapon to have component",
                world.getComponent(weapon, InputComponent.class).isPresent());

        assertTrue("Expected a noodle to have component", world
                .getComponent(player, NoodleWeaponComponent.class).isPresent());
        NoodleWeaponComponent nwc = world
                .getComponent(player, NoodleWeaponComponent.class).get();
        assertEquals("Expected a different weapon for noodle", weapon,
                nwc.getWeapon());
        
        // remove the weapon
        WeaponEntities.removeWeapon(world, weapon);
        assertFalse("Player shouldn't have a weapon", 
                world.hasComponent(player, NoodleWeaponComponent.class));
        world.removeComponent(player, NoodleWeaponComponent.class);
        
        
        // give them back the weapon
        WeaponEntities.createWeapon(world, wdef, player);
        WeaponEntities.removePlayersWeapon(world, player);
        assertFalse("Shouldn't have had a weapon", 
                world.hasComponent(player, NoodleWeaponComponent.class));
    }
}