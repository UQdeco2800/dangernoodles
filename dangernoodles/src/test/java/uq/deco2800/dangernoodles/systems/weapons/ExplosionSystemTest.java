package uq.deco2800.dangernoodles.systems.weapons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uq.deco2800.dangernoodles.components.CollisionComponent;
import uq.deco2800.dangernoodles.components.CursorComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.RectangleComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.components.damages.InstantDamageComponent;
import uq.deco2800.dangernoodles.components.weapons.ExplosionComponent;
import uq.deco2800.dangernoodles.components.weapons.ProjectileComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.ProjectileEntities;
import uq.deco2800.dangernoodles.weapons.ProjectileDefinition;

public class ExplosionSystemTest {
    
    @Test
    public void testBasicExplosion() {
        // Create the world and a projectile
        World world = new World(100, 100);
        
        world.createEntity().addComponent(new CursorComponent());
        
        ProjectileDefinition pdef = new ProjectileDefinition(
                0, "", "", 
                4, 6, // 6 x 4 size
                10, 11, true, 10, true, 
                10 // blast radius of 10px
           );
        Entity projectile = ProjectileEntities.createProjectile(world, pdef, 
                50, 40, // projectile w/ top corner @ (50, 40)
                null);
        ProjectileComponent pcomp = world.getComponent(projectile, 
                ProjectileComponent.class).get();
        
        world.addSystem(new ExplosionSystem(), 1);
        
        // Make the explosion
        Entity explosion = ProjectileEntities.createExplosion(world, pcomp);
        assertTrue("Should have an explosion component", 
                world.hasComponent(explosion, ExplosionComponent.class));
        assertTrue("Should have an explosion component", 
                world.hasComponent(explosion, PositionComponent.class));
        assertTrue("Should have an explosion component", 
                world.hasComponent(explosion, InstantDamageComponent.class));
        assertTrue("Should have an explosion component", 
                world.hasComponent(explosion, SpriteComponent.class));
        assertTrue("Should have an explosion component", 
                world.hasComponent(explosion, CollisionComponent.class));
        assertTrue("Should have an explosion component", 
                world.hasComponent(explosion, RectangleComponent.class));
        
        PositionComponent position = world.getComponent(explosion, 
                PositionComponent.class).get();
        assertTrue("Should have position of x = 42", 
                Math.abs(position.getX() - 42) < 0.1);
        assertTrue("Should have position of y = 33", 
                Math.abs(position.getY() - 33) < 0.1);
        
        
        
        // Run Explosion system once
        ExplosionComponent explosionComp = world.getComponent(
                explosion, ExplosionComponent.class).get();
        double totalLife = explosionComp.lifeLeft();
        
        assertTrue("Should be the first run", explosionComp.isFirstRun());
        
        assertTrue("Damage should be 11", Math.abs(explosionComp.getDamage() - 11) < 0.1);
        assertEquals("Blast radius should be 10", 10, explosionComp.getBlastRadius());
        
        world.process(0, 0.01);
        
        assertFalse("Shouldn't be the first run", explosionComp.isFirstRun());
        
        assertTrue("Incorrect life left", 
                Math.abs(totalLife - explosionComp.lifeLeft() - 0.01) < 0.001);
        totalLife -= 0.01;
        
        // Run it until it runs out
        double time = 0.01;
        while (totalLife > 0) {
            assertTrue("Should be alive", explosionComp.isAlive());
            world.process(time, 0.01);
            time += 0.01;
            totalLife -= 0.01;
        }
        assertFalse("Should be dead", explosionComp.isAlive());
        
    }
}
