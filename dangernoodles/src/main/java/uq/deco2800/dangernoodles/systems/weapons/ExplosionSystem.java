package uq.deco2800.dangernoodles.systems.weapons;

import java.util.List;
import java.util.Random;
import java.util.Stack;

import uq.deco2800.dangernoodles.AudioManager;
import uq.deco2800.dangernoodles.components.damages.InstantDamageComponent;
import uq.deco2800.dangernoodles.components.weapons.ExplosionComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;

public class ExplosionSystem implements System {

    @Override
    public void run(World world, double t, double dt) {
        // stack of explosions to get rid of at the end
        Stack<Entity> toKill = new Stack<Entity>();
        // Get all the explosions and loop over them
        List<ExplosionComponent> explosions = 
                world.getComponents(ExplosionComponent.class);
        // check there even are explosions to process
        if (explosions == null || explosions.isEmpty()) {
            return;
        }
        // loop over them
        for (ExplosionComponent explosion : explosions) {
            if (explosion.isFirstRun()) {
            	// Plays an explosion sound
            	Random random = new Random();
            	AudioManager.playSound("resources/sounds/explode" + (random.nextInt(3) + 1) + ".wav", 
            			false);
            	
                // get rid of the damage after the first time
                world.removeComponent(explosion.getEntity(),
                        InstantDamageComponent.class);
                explosion.firstRunHappened(); // don't do this again
            }
            explosion.decrementLife(dt);
            if (!explosion.isAlive()) {
                toKill.push(explosion.getEntity());
            }
        }
        // kill all of the entities that have died
        while (!toKill.isEmpty()) {
            world.destroyEntity(toKill.pop());
        }
    }

}
