package uq.deco2800.dangernoodles.systems.weapons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import uq.deco2800.dangernoodles.components.CollisionComponent;
import uq.deco2800.dangernoodles.components.CursorComponent;
import uq.deco2800.dangernoodles.components.GravityComponent;
import uq.deco2800.dangernoodles.components.MassComponent;
import uq.deco2800.dangernoodles.components.MovementComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.RectangleComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.components.damages.InstantDamageComponent;
import uq.deco2800.dangernoodles.components.weapons.ExplosionComponent;
import uq.deco2800.dangernoodles.components.weapons.ProjectileComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.ProjectileEntities;
import uq.deco2800.dangernoodles.systems.weapons.ProjectileSystem;
import uq.deco2800.dangernoodles.weapons.ProjectileDefinition;

public class ProjectileSystemTest {
    
    /**
     * Creates a world with a cursor and ProjectileSystem in it
     * 
     * @return A World object that contains only a cursor and ProjectileSystem
     */
    public World makeWorld() {
        World world = new World(1000, 1000);
        world.createEntity().addComponent(new CursorComponent());
        
        ProjectileSystem projectileSystem = new ProjectileSystem();
        world.addSystem(projectileSystem, 1);
        
        return world;
    }

    @Test
    public void TestInitialiseWithNoProjectiles() {
        World world = new World(1000, 1000);
        ProjectileSystem projectileSystem = new ProjectileSystem();
        world.addSystem(projectileSystem, 1);

        // No projectiles in world, error would be result of projectileSystem
        // interfering with world without projectiles

        for (double t = 0; t < 10; t += 0.1) {
            world.process(t, 0.1);
        }

    }

    @Test
    public void testHelperMethods() {
        World world = new World(1000, 1000);
        ProjectileSystem projectileSystem = new ProjectileSystem();
        world.addSystem(projectileSystem, 1);
        // since it's random, test a bunch of times
        double direction;
        for (int i = 0; i < 1E6; i++) {
            direction = Math.abs(ProjectileSystem.createRandomDirection());
            assertTrue("direction should be between -180 and 180",
                    direction <= 180);
        }
    }

    @Test
    public void testInitialiseWithOneProjectile() {
        World world = makeWorld();

        // base projectile
        ProjectileDefinition projectileDefinition = new ProjectileDefinition(0,
                "testProjectileBaseCase", "resources/projectiles/test.png", 0,
                0, 1, 30, true, 10, true, 10);

        ProjectileEntities.createProjectile(world,
                projectileDefinition, 30, 30, null);

        // system runs, no errors
        for (double t = 0; t < 10; t += 0.1) {
            world.process(t, 0.1);
        }
    }
    
    /**
     * Tests that when an exploding projectile collides with an Entity, 
     * it explodes
     */
    @Test
    public void testCollisionCreatesExplosion() {
        // set up the world
        World world = makeWorld();
        
        // make the projectile
        ProjectileDefinition projectileDefinition = new ProjectileDefinition(0,
                "testProjectileBaseCase", "resources/projectiles/test.png", 0,
                0, 1, 30, true, 10, true, 10);

        Entity projectile = ProjectileEntities.createProjectile(world,
                projectileDefinition, 30, 30, null);
        
        // "Collide" with the projectile
        CollisionComponent collision = world.getComponent(projectile, 
                CollisionComponent.class).get();
        collision.addCollision(world.createEntity());
        
        // check that there's no explosion and a projectile
        assertTrue("There shouldn't be an explosion",
                world.getComponents(ExplosionComponent.class) == null);
        
        assertTrue("There should be a projectile that's alive", 
                isProjectileAlive(world, projectile));
        
        // Run the system
        world.process(0, 0.1);
        
        // check that there's an explosion and a projectile
        assertTrue("There should be an explosion",
                world.getComponents(ExplosionComponent.class).size() == 1);
        
        assertTrue("There should be a projectile that's alive", 
                isProjectileAlive(world, projectile));
        
        // Run the system
        world.process(0, 0.1);
        
        // check that there's no projectile
        
        assertTrue("Projectile should be destroyed", 
                isProjectileDestroyed(world, projectile));   
    }
    
    /**
     * Tests that a projectile that doesn't explode, doesn't when it has
     * collides with an Entity
     */
    @Test
    public void testDoesntCreateExplosion() {
        // set up the world
        World world = makeWorld();
        
        // make the projectile
        ProjectileDefinition projectileDefinition = new ProjectileDefinition(0,
                "testProjectileBaseCase", "resources/projectiles/test.png", 0,
                0, 1, 30, true, 10, false, 0);

        Entity projectile = ProjectileEntities.createProjectile(world,
                projectileDefinition, 30, 30, null);
        
        // "Collide" with the projectile
        CollisionComponent collision = world.getComponent(projectile, 
                CollisionComponent.class).get();
        collision.addCollision(world.createEntity());
        
        // check that there's no explosion and a projectile
        assertTrue("There shouldn't be an explosion",
                world.getComponents(ExplosionComponent.class) == null);
        
        assertTrue("There should be a projectile that's alive", 
                isProjectileAlive(world, projectile));
        
        // Run the system
        world.process(0, 0.1);
        
        // check that there's no explosion and a projectile
        assertTrue("There shouldn't be an explosion",
                world.getComponents(ExplosionComponent.class) == null);
        
        assertTrue("There should be a projectile that's alive", 
                isProjectileAlive(world, projectile));
        
        // Run the system
        world.process(0, 0.1);
        
        // check that there's no projectile or explosion
        assertTrue("There shouldn't be an explosion",
                world.getComponents(ExplosionComponent.class) == null);
        
        assertTrue("Projectile should be destroyed", 
                isProjectileDestroyed(world, projectile));
    }
    
    /**
     * Tests that it destroys itself when it's told to 
     */
    @Test
    public void testDestroysWhenTold() {
        // set up the world
        World world = makeWorld();
        
        // make the projectile
        ProjectileDefinition projectileDefinition = new ProjectileDefinition(0,
                "testProjectileBaseCase", "resources/projectiles/test.png", 0,
                0, 1, 30, true, 10, false, 0);

        Entity projectile = ProjectileEntities.createProjectile(world,
                projectileDefinition, 30, 30, null);
        
        ProjectileComponent projComp = world.getComponent(projectile, 
                ProjectileComponent.class).get();
        
        // run the world
        world.process(0, 1);
        
        // the projectile should be there
        assertTrue("There should be a projectile that's alive", 
                isProjectileAlive(world, projectile));
        
        // tell it to die next run
        projComp.destroyNextRun();
        
        world.process(1, 1);
        
        assertTrue("The projectile should be destroyed", 
                isProjectileDestroyed(world, projectile));
    }
    
    /**
     * Tests that a projectile dies when it times out with the time going over
     * its life ticks (i.e. life of 10s, 10.2s of time passed)
     */
    @Test
    public void testTimeOutOver() {
        // set up the world
        World world = makeWorld();
        
        // make the projectile
        ProjectileDefinition projectileDefinition = new ProjectileDefinition(0,
                "testProjectileBaseCase", "resources/projectiles/test.png", 0,
                0, 1, 30, true, 10000, false, 0);

        Entity projectile = ProjectileEntities.createProjectile(world,
                projectileDefinition, 30, 30, null);
        
        world.getComponent(projectile, ProjectileComponent.class).get();
        
        // Go just below the 10
        for (double total = 0; Math.abs(total - 10) > 0.01 && total < 10;
                total += 0.3) {
            // run the world
            world.process(total, 0.3);
            
            // the projectile should be there
            assertTrue("There should be a projectile that's alive", 
                    isProjectileAlive(world, projectile));
        }
        
        world.process(10.2, 0.3);
        
        assertTrue("The projectile should be destroyed", 
                isProjectileDestroyed(world, projectile));
    }
    
    /**
     * Tests that a projectile dies when it times out with the time hitting
     * exactly its life ticks (i.e. life of 10s, 10s of time passed)
     */
    @Test
    public void testTimeOutOn() {
        // set up the world
        World world = makeWorld();
        
        // make the projectile
        ProjectileDefinition projectileDefinition = new ProjectileDefinition(0,
                "testProjectileBaseCase", "resources/projectiles/test.png", 0,
                0, 1, 30, true, 10000, false, 0);

        Entity projectile = ProjectileEntities.createProjectile(world,
                projectileDefinition, 30, 30, null);
        
        ProjectileComponent pc = world.getComponent(projectile, ProjectileComponent.class).get();
        
        // Go just below the 10
        for (double total = 0; Math.abs(total - 10) > 0.01; total += 0.2) {
            // run the world
            world.process(total, 0.2);
            
            // the projectile should be there
            assertTrue("There should be a projectile that's alive", 
                    isProjectileAlive(world, projectile));
        }
        
        world.process(10, 0.2);
        
        assertTrue("The projectile should be destroyed", 
                isProjectileDestroyed(world, projectile));
    }
    
    @Test
    public void testAngleWrapping() {
        assertTrue("Unexpected result from angle wrapping", 
                Math.abs(ProjectileSystem.wrapAngle(190) + 170) < 0.1);
        assertTrue("Unexpected result from angle wrapping", 
                Math.abs(ProjectileSystem.wrapAngle(-190) - 170) < 0.1);
        assertTrue("Unexpected result from angle wrapping", 
                Math.abs(ProjectileSystem.wrapAngle(370) - 10) < 0.1);
        assertTrue("Unexpected result from angle wrapping", 
                Math.abs(ProjectileSystem.wrapAngle(-370) + 10) < 0.1);
        assertTrue("Unexpected result from angle wrapping", 
                Math.abs(ProjectileSystem.wrapAngle(550) + 170) < 0.1);
        assertTrue("Unexpected result from angle wrapping", 
                Math.abs(ProjectileSystem.wrapAngle(-550) - 170) < 0.1);
        assertTrue("Unexpected result from angle wrapping", 
                Math.abs(ProjectileSystem.wrapAngle(180) - 180) < 0.1);
        assertTrue("Unexpected result from angle wrapping", 
                Math.abs(ProjectileSystem.wrapAngle(-180) + 180) < 0.1);
        assertTrue("Unexpected result from angle wrapping", 
                Math.abs(ProjectileSystem.wrapAngle(45) - 45) < 0.1);
        assertTrue("Unexpected result from angle wrapping", 
                Math.abs(ProjectileSystem.wrapAngle(-45) + 45) < 0.1);
    }
    
    @Test
    public void testGetClusteredSpeed() {
        double actual, expected;
        List<Integer> inputClusters = Arrays.asList(
                1, 2, 3,
                1, 2, 3,
                5, 10, 11
        );
        List<Double> inputSpeeds = Arrays.asList(
                10.1, 11.9, 301.5, 
                50.5, 90.4, 0.5,
                30.1, 80.1, 10.9
        );
        for (int i = 0; i < 9; i++) {
            int clusters = inputClusters.get(i);
            double speed = inputSpeeds.get(i);
            actual = ProjectileSystem.getClusteredSpeed(clusters, speed);
            
            expected = speed * (1.0 / 6.0 
                    + 5.0 / (6.0 * Math.pow(clusters, 2.0 / 3.0)));
            assertTrue("Unexpected result from getClusteredSpeed", 
                    Math.abs(actual - expected) < 0.01);
        }
    }
    
    @Test
    public void testGetParentSpeed() {
        World world = new World(0,0);
        Entity projectile = world.createEntity();
        
        ProjectileDefinition pdef = new ProjectileDefinition(0, "","", 0, 0, 0, 0);
        
        MovementComponent movement = new MovementComponent(0,0,0,0);
        ProjectileComponent projComp = new ProjectileComponent(pdef);
        projectile.addComponent(movement)
                .addComponent(projComp);
        
        double actual, expected;
        List<Double> inputSpeeds = Arrays.asList(
                 10.1,    11.0,   100.3,  0.3, -10.1,   11.5, 99.7, -54.73,
                -10.1, -345.21, -3.8912, 2.34, 919.3, -901.9, 84.3,  98.33
        );
        
        for (int i = 0; i < 8; i++) {
            double vx, vy;
            vx = inputSpeeds.get(2 * i);
            vy = inputSpeeds.get(2 * i + 1);
            movement.setVelocity(vx, vy);
            actual = ProjectileSystem.getParentSpeed(projComp, world);
            expected = Math.sqrt(vx * vx + vy * vy);
            assertTrue("Unexpected result from getParentSpeed", 
                    Math.abs(actual - expected) < 0.01);
        }
    }

    @Test
    public void testAddIgnoredCollisions() {
        final int NUM_OF_CLUSTERS = 50;
        World world = new World(0, 0);
        List<Entity> clusters = new ArrayList<Entity>();
        List<CollisionComponent> collisions = 
                new ArrayList<CollisionComponent>();
        Entity parent = world.createEntity();
        
        // Make a bunch of cluster entities
        for (int i = 0; i < NUM_OF_CLUSTERS; i++) {
            Entity cluster = world.createEntity();
            CollisionComponent collision = new CollisionComponent(true, null);
            cluster.addComponent(collision);
            clusters.add(cluster);
            collisions.add(collision);
        }
        
        ProjectileSystem.addIgnoreCollisionsToClusters(clusters, parent, world);
        
        for (int i = 0; i < NUM_OF_CLUSTERS; i++) {
            Entity cluster = clusters.get(i);
            CollisionComponent collision = collisions.get(i);
            // check that it's ignoring its parent
            assertTrue("Cluster should be ignoring parent " + i, 
                    collision.getIgnoredEntities()
                    .contains(parent)
            );
            for (Entity clusterToCheck : clusters) {
                if (cluster.equals(clusterToCheck)) {
                    assertFalse("Cluster shouldn't be ignoring itself", 
                            collision.getIgnoredEntities()
                            .contains(clusterToCheck)
                    );
                } else {
                    assertTrue("Cluster should be ignoring other cluster", 
                            collision.getIgnoredEntities()
                            .contains(clusterToCheck)
                    );
                }
            }
        }
    }
    
    @Test
    public void testCheckClusters() {
        ProjectileDefinition clusterDef = new ProjectileDefinition(0, "","",
                0, 0, 0, 0);
        
        ProjectileDefinition pdef = new ProjectileDefinition(0, "","",
                0, 0, 0, 0, false, 0, false, 0, true, 5, clusterDef);
        
        World world = makeWorld();
        
        Entity projectile = world.createEntity();
        ProjectileComponent projComp = new ProjectileComponent(pdef);
        projectile.addComponent(projComp)
                .addComponent(new CollisionComponent(true, null))
                .addComponent(new MovementComponent(0,0,10,10))
                .addComponent(new PositionComponent(0, 0));
        
        ProjectileSystem.checkClusters(projComp, world);
        
        assertEquals("There should be 6 projectiles", 6, 
                world.getComponents(ProjectileComponent.class).size());
    }
    
    @Test
    public void testCheckClustersDoesntCluster() {
        ProjectileDefinition pdef = new ProjectileDefinition(0, "","",
                0, 0, 0, 0);
        
        World world = new World(0,0);
        
        Entity projectile = world.createEntity();
        ProjectileComponent projComp = new ProjectileComponent(pdef);
        projectile.addComponent(projComp);
        
        ProjectileSystem.checkClusters(projComp, world);
        
        assertEquals("There should be 1 projectile", 1, 
                world.getComponents(ProjectileComponent.class).size());
    }
    
    /**
     * Checks if the given projectile has been destroyed in the given World
     * 
     * @param world
     *            The World in which to check
     * @param entity
     *            The projectile Entity for which to check
     * @return Whether or not the projectile Entity is destroyed in the World
     */
    public boolean isProjectileDestroyed(World world, Entity entity) {
        // Return false if entity has AT LEAST ONE of the following components
        return !(world.hasComponent(entity, ProjectileComponent.class)
                || world.hasComponent(entity, PositionComponent.class)
                || world.hasComponent(entity, InstantDamageComponent.class)
                || world.hasComponent(entity, MovementComponent.class)
                || world.hasComponent(entity, CollisionComponent.class)
                || world.hasComponent(entity, SpriteComponent.class)
                || world.hasComponent(entity, MassComponent.class)
                || world.hasComponent(entity, GravityComponent.class)
                || world.hasComponent(entity, RectangleComponent.class));
    }

    /**
     * Checks if the given projectile exists in the given World
     * 
     * @param world
     *            The World in which to check
     * @param entity
     *            The projectile Entity for which to check
     * @return Whether or not the projectile Entity exist in the World
     */
    public boolean isProjectileAlive(World world, Entity entity) {
        // return true iff the entity has all of the following components
        return world.hasComponent(entity, ProjectileComponent.class)
                && world.hasComponent(entity, PositionComponent.class)
                && world.hasComponent(entity, MovementComponent.class)
                && world.hasComponent(entity, CollisionComponent.class)
                && world.hasComponent(entity, SpriteComponent.class)
                && world.hasComponent(entity, MassComponent.class)
                && world.hasComponent(entity, GravityComponent.class)
                && world.hasComponent(entity, RectangleComponent.class);
    }

}
