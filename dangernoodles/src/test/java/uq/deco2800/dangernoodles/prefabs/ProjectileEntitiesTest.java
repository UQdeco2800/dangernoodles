package uq.deco2800.dangernoodles.prefabs;

import uq.deco2800.dangernoodles.components.CollisionComponent;
import uq.deco2800.dangernoodles.components.CursorComponent;
import uq.deco2800.dangernoodles.components.GravityComponent;
import uq.deco2800.dangernoodles.components.MassComponent;
import uq.deco2800.dangernoodles.components.MovementComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.components.weapons.ProjectileComponent;
import uq.deco2800.dangernoodles.components.weapons.WeaponComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.weapons.ProjectileDefinition;
import uq.deco2800.dangernoodles.weapons.WeaponDefinition;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProjectileEntitiesTest {
    
    // Adds a cursor to the world
    public void createCursor(World world) {
        world.createEntity().addComponent(new CursorComponent());
    }

    @Test
    public void testNewAbsoluteProjectileNoTimeOut() {
        World world = new World(1000, 1000);
        
        createCursor(world);
        
        ProjectileDefinition pdef = new ProjectileDefinition(0, "", "", 0, 0, 0,
                0, true, 0, true, 0);
        Entity projectile = ProjectileEntities.createProjectile(world, pdef, 10,
                10, null);

        assertTrue("Expected there to be a movement component", world
                .getComponent(projectile, MovementComponent.class).isPresent());
        assertTrue("Expected there to be a position component", world
                .getComponent(projectile, PositionComponent.class).isPresent());
        assertTrue("Expected there to be a sprite", world
                .getComponent(projectile, SpriteComponent.class).isPresent());
        assertTrue("Expected there to be a mass component", world
                .getComponent(projectile, MassComponent.class).isPresent());
        assertTrue("Expected there to be a gravity component", world
                .getComponent(projectile, GravityComponent.class).isPresent());
        assertTrue("Expected there to be a collision component",
                world.getComponent(projectile, CollisionComponent.class)
                        .isPresent());
        assertTrue("Expected there to be a projectile",
                world.getComponent(projectile, ProjectileComponent.class)
                        .isPresent());

        PositionComponent posc = world
                .getComponent(projectile, PositionComponent.class).get();
        assertTrue("Expected different x position",
                Math.abs(posc.getX() - 10) < 0.0001);
        assertTrue("Expected different y position",
                Math.abs(posc.getY() - 10) < 0.0001);

        ProjectileComponent pc = world
                .getComponent(projectile, ProjectileComponent.class).get();
        assertEquals("Expected different owner", projectile, pc.getEntity());
        assertEquals("Expected different definition", pdef, pc.getDefinition());
        assertTrue("Expected different owner",
                Math.abs(pc.ticksLeft()) < 0.0001);
        assertTrue("Expected not to time out", pc.timesOut());
        assertTrue("Expected to not be timed out", pc.timedOut());
    }

    @Test
    public void testNewAbsoluteProjectileTimesOut() {
        World world = new World(1000, 1000);
        createCursor(world);
        ProjectileDefinition pdef = new ProjectileDefinition(0, "", "", 0, 0, 0,
                0, true, 5); // TODO:
        // change
        // lifeticks
        // in
        // projectileDefinition
        // to
        // double
        // (from
        // int)
        Entity projectile = ProjectileEntities.createProjectile(world, pdef, 10,
                10, null);

        assertTrue("Expected there to be a movement component", world
                .getComponent(projectile, MovementComponent.class).isPresent());
        assertTrue("Expected there to be a position component", world
                .getComponent(projectile, PositionComponent.class).isPresent());
        assertTrue("Expected there to be a sprite", world
                .getComponent(projectile, SpriteComponent.class).isPresent());
        assertTrue("Expected there to be a mass component", world
                .getComponent(projectile, MassComponent.class).isPresent());
        assertTrue("Expected there to be a gravity component", world
                .getComponent(projectile, GravityComponent.class).isPresent());
        assertTrue("Expected there to be a collision component",
                world.getComponent(projectile, CollisionComponent.class)
                        .isPresent());
        assertTrue("Expected there to be a projectile",
                world.getComponent(projectile, ProjectileComponent.class)
                        .isPresent());

        PositionComponent posc = world
                .getComponent(projectile, PositionComponent.class).get();
        assertTrue("Expected different x position",
                Math.abs(posc.getX() - 10) < 0.0001);
        assertTrue("Expected different y position",
                Math.abs(posc.getY() - 10) < 0.0001);

        ProjectileComponent pc = world
                .getComponent(projectile, ProjectileComponent.class).get();
        assertEquals("Expected different owner", projectile, pc.getEntity());
        assertEquals("Expected different owner", pdef, pc.getDefinition());
        assertTrue("Expected different owner",
                Math.abs(5.0 - pc.ticksLeft()) < 0.001);
        assertTrue("Expected to time out soon", pc.timesOut());
        assertFalse("Expected to not be timed out", pc.timedOut());

        pc.decrementTicks(10.0);
        assertTrue("Expected different owner",
                Math.abs(0.0 - pc.ticksLeft()) < 0.001);
        assertTrue("Expected to time out soon", pc.timesOut());
        assertTrue("Expected to be timed out", pc.timedOut());
    }

    @Test
    public void testNewProjectileFromWeapon() {
        World world = new World(1000, 1000);
        createCursor(world);
        ProjectileDefinition pdef = new ProjectileDefinition(0, "", "", 0, 0, 0,
                0, true, 0, true, 0);
        WeaponDefinition wdef = new WeaponDefinition(1, "gun",
                "ProjectileWeapon", "", 0, 0, false,
                true, 1, pdef);
        Entity noodle = PlayerEntities.createPlayer(world,
                NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_ALPHA, 0);
        Entity weapon = WeaponEntities.createWeapon(world, wdef, noodle);

        Entity projectile = ProjectileEntities.createProjectile(world,
                world.getComponent(weapon, WeaponComponent.class).get());

        assertTrue("Expected there to be a movement component", world
                .getComponent(projectile, MovementComponent.class).isPresent());
        assertTrue("Expected there to be a position component", world
                .getComponent(projectile, PositionComponent.class).isPresent());
        assertTrue("Expected there to be a sprite", world
                .getComponent(projectile, SpriteComponent.class).isPresent());
        assertTrue("Expected there to be a mass component", world
                .getComponent(projectile, MassComponent.class).isPresent());
        assertTrue("Expected there to be a gravity component", world
                .getComponent(projectile, GravityComponent.class).isPresent());
        assertTrue("Expected there to be a collision component",
                world.getComponent(projectile, CollisionComponent.class)
                        .isPresent());
        assertTrue("Expected there to be a projectile",
                world.getComponent(projectile, ProjectileComponent.class)
                        .isPresent());

        PositionComponent posc = world
                .getComponent(projectile, PositionComponent.class).get();
        PositionComponent noodlePos = world
                .getComponent(noodle, PositionComponent.class).get();
        assertTrue("Expected different x position",
                Math.abs(posc.getX() - noodlePos.getX()) < 0.0001);
        assertTrue("Expected different y position",
                Math.abs(posc.getY() - noodlePos.getY()) < 0.0001);

        ProjectileComponent pc = world
                .getComponent(projectile, ProjectileComponent.class).get();
        assertEquals("Expected different owner", projectile, pc.getEntity());
        assertEquals("Expected different definition", pdef, pc.getDefinition());
        assertTrue("Expected different owner",
                Math.abs(pc.ticksLeft()) < 0.0001);
        assertTrue("Expected not to time out", pc.timesOut());
        assertTrue("Expected to not be timed out", pc.timedOut());
    }

    @Test
    public void testNewClusteringProjectile() {
        World world = new World(1000, 1000);
        createCursor(world);
        ProjectileDefinition pdef = new ProjectileDefinition(0, "", "", 0, 0, 0,
                0, true, 0, true, 0);
        ProjectileDefinition parentDef = new ProjectileDefinition(1,
                "Cluster Grenade", "ProjectileWeapon", 0, 0, 0, 0, true, 0,
                false, 0, true, 1, pdef);
        Entity parent = ProjectileEntities.createProjectile(world, parentDef,
                45, 45, null);
        Entity projectile = ProjectileEntities.createProjectile(world,
                world.getComponent(parent, ProjectileComponent.class).get());

        assertTrue("Expected there to be a movement component", world
                .getComponent(projectile, MovementComponent.class).isPresent());
        assertTrue("Expected there to be a position component", world
                .getComponent(projectile, PositionComponent.class).isPresent());
        assertTrue("Expected there to be a sprite", world
                .getComponent(projectile, SpriteComponent.class).isPresent());
        assertTrue("Expected there to be a mass component", world
                .getComponent(projectile, MassComponent.class).isPresent());
        assertTrue("Expected there to be a gravity component", world
                .getComponent(projectile, GravityComponent.class).isPresent());
        assertTrue("Expected there to be a collision component",
                world.getComponent(projectile, CollisionComponent.class)
                        .isPresent());
        assertTrue("Expected there to be a projectile",
                world.getComponent(projectile, ProjectileComponent.class)
                        .isPresent());

        PositionComponent posc = world
                .getComponent(projectile, PositionComponent.class).get();
        PositionComponent parentPos = world
                .getComponent(parent, PositionComponent.class).get();
        assertTrue("Expected different x position",
                Math.abs(posc.getX() - parentPos.getX()) < 0.0001);
        assertTrue("Expected different y position",
                Math.abs(posc.getY() - parentPos.getY()) < 0.0001);

        ProjectileComponent pc = world
                .getComponent(projectile, ProjectileComponent.class).get();
        assertEquals("Expected different owner", projectile, pc.getEntity());
        assertEquals("Expected different definition", pdef, pc.getDefinition());
        assertTrue("Expected different owner",
                Math.abs(pc.ticksLeft()) < 0.0001);
        assertTrue("Expected not to time out", pc.timesOut());
        assertTrue("Expected to not be timed out", pc.timedOut());
    }
}