package uq.deco2800.dangernoodles.systems;

import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.EntityIterator;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.*;

import java.util.Optional;

import uq.deco2800.dangernoodles.components.CollisionComponent;
import uq.deco2800.dangernoodles.components.MovementComponent;
import uq.deco2800.dangernoodles.components.NameComponent;
import uq.deco2800.dangernoodles.components.PlayerComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.components.displays.EffectDisplayComponent;
import uq.deco2800.dangernoodles.components.noodles.NoodleComponent;
import uq.deco2800.dangernoodles.components.stats.HealthComponent;
import uq.deco2800.dangernoodles.components.weapons.ProjectileComponent;
import uq.deco2800.dangernoodles.components.weather.AsteroidComponent;
import uq.deco2800.dangernoodles.components.weather.LeafComponent;
import uq.deco2800.dangernoodles.components.weather.LightningComponent;
import uq.deco2800.dangernoodles.components.weather.RainComponent;
import uq.deco2800.dangernoodles.components.weather.RainingDuckComponent;
import uq.deco2800.dangernoodles.components.weather.SnowflakeComponent;
import uq.deco2800.dangernoodles.components.weather.WindComponent;

/**
 * Will handle the logic around generating weather states for the game
 * 	 - randomly generating which weather component will be used
 * 	 - generating weather strength/direction 
 *   - displaying weather state to screen
 * @author team-sitcom
 *
 */
public class WeatherMovementSystem implements System {
	
	/**
     * Overrides the parent method Run
     * 
     * @param world is the parent world object
     * 
     * @param t Time since the beginning of the game
     * 
     * @param dt The delta time, i.e. the time since last frame
     */
	@Override
	public void run(World world, double t, double dt) {
		WindComponent windComponent = world.getComponents(WindComponent.class).get(0);
		//Iterates over every rain drop and makes wind affect it
		//Gets a iterator of every entity that has RainComponent 
		EntityIterator rainIterator = world.getIterator(RainComponent.class);
		
		//While there is entities give each water drop a movement speed and direction
		while (rainIterator.hasNext()) {
			Entity rainEntity = rainIterator.next().getEntity();
			world.getComponent(rainEntity, MovementComponent.class).get()
			.setVx(windComponent.getDirection()*windComponent.getStrength()*500);
			// destroy when it moves off the screen
			if (world.getComponent(rainEntity, PositionComponent.class).get().getY() > 1500) {
				world.destroyEntity(rainEntity);
			}
		}
		
		//Iterates over every snowflake and makes wind affect it
		EntityIterator snowIterator = world.getIterator(SnowflakeComponent.class);
		while (snowIterator.hasNext()) {
			Entity snowEntity = snowIterator.next().getEntity();
			world.getComponent(snowEntity, MovementComponent.class).get()
					.setVx(windComponent.getDirection()*windComponent.getStrength()*500);
			// destroy when it moves off the screen
			if (world.getComponent(snowEntity, PositionComponent.class).get().getY() > 1500) {
				world.destroyEntity(snowEntity);
			}
		}
		
		//Iterates over every leaf and makes wind affect it
		EntityIterator leafIterator = world.getIterator(LeafComponent.class);
		while (leafIterator.hasNext()) {
			Entity leafEntity = leafIterator.next().getEntity();
			world.getComponent(leafEntity, MovementComponent.class).get()
					.setVx(windComponent.getDirection()*windComponent.getStrength()*500);
			// if entity is fallen far enough, destroy it
			if (world.getComponent(leafEntity, PositionComponent.class).get().getY() > 1500) {
				world.destroyEntity(leafEntity);
			}
		}
		
		//Iterates over every duck and makes wind affect it
		EntityIterator duckIterator = world.getIterator(RainingDuckComponent.class);
		while (duckIterator.hasNext()) {
			Entity duckEntity = duckIterator.next().getEntity();
			world.getComponent(duckEntity, MovementComponent.class).get()
			.setVx(windComponent.getDirection()*windComponent.getStrength()*500);
			// if entity is fallen far enough, destroy it
			double initialRotation = world.getComponent(duckEntity, SpriteComponent.class)
					.get().getRotation();
			world.getComponent(duckEntity, SpriteComponent.class).get()
					.setRotation(initialRotation+(10*(Math.random()-0.5)));
			if (world.getComponent(duckEntity, PositionComponent.class).get().getY() > 1500) {
				world.destroyEntity(duckEntity);
			}
		}
		
		//iterates over every asteroid and destroys it if it has moved enough
		EntityIterator asteroidIterator = world.getIterator(AsteroidComponent.class);
		while (asteroidIterator.hasNext()) {
			Entity asteroidEntity = asteroidIterator.next().getEntity();
			world.getComponent(asteroidEntity, MovementComponent.class).get()
			.setVx(windComponent.getDirection()*windComponent.getStrength()*500);
			// if entity is fallen far enough, destroy it
			if (world.getComponent(asteroidEntity, PositionComponent.class).get().getY() > 1500) {
				world.destroyEntity(asteroidEntity);
			}
		}
		
		//Iterates over every projectile and makes wind affect it
		EntityIterator projectilesIterator = world.getIterator(ProjectileComponent.class);
		while (projectilesIterator.hasNext()) {
			Entity projectileEntity = projectilesIterator.next().getEntity();
			double velocity = world.getComponent(projectileEntity, MovementComponent.class)
					.get().getAX();
			world.getComponent(projectileEntity, MovementComponent.class).get()
					.setAx(velocity + windComponent.getDirection()*windComponent.getStrength()*20);
		}
		
		// Control lightning movement on screen
		EntityIterator lightningIterator = world.getIterator(LightningComponent.class);
		while (lightningIterator.hasNext()) {
			Entity lightningBolt = lightningIterator.next().getEntity();
			EntityIterator boxIterator = world.getIterator(EffectDisplayComponent.class);
			while (boxIterator.hasNext()) {
				Entity boxEntity = boxIterator.next().getEntity();
				world.getComponent(lightningBolt, CollisionComponent.class)
					.get().addIgnored(boxEntity);
			}
			// remove lightning 
			if (world.getComponent(lightningBolt, LightningComponent.class)
					.get().getDuration() >= 7) {
				world.destroyEntity(lightningBolt);
			} else {
				world.getComponent(lightningBolt, LightningComponent.class)
					.get().IncrementDuration();
			}
		}
		
		// Get all the noodles in the world
        for (ComponentMap componentMap : world.getIterator(
                PlayerComponent.class,
                CollisionComponent.class)) {
            // Retrieve the collision component
            CollisionComponent collisionComponent = componentMap.get(CollisionComponent.class);
            // For each of the entities that this noodle is colliding, see if
            // one of them is an effect box.
            for (int i = 0; i < collisionComponent.getCollisions().size(); i++) {
                Entity bolt = collisionComponent.getCollisions().get(i);
                Optional<NameComponent> nameComponent;
                nameComponent = world.getComponent(bolt, NameComponent.class);
                
                // If yes, than start applying effect to the noodle.
                if (nameComponent.isPresent() && "LightningBolt".equals(nameComponent.get().getName())) {
                    int oldHealth = world.getComponent
                    		(componentMap.getEntity(), HealthComponent.class).get().getHealth();
                    int newHealth = oldHealth - 
                    		(int)world.getComponent(bolt, LightningComponent.class)
                    			.get().getDamage();
                    
                    if (newHealth > 0) {
                    	world.getComponent(componentMap.getEntity(), HealthComponent.class)
                    		.get().setHealth(newHealth);
                    } else {
                        // the player has died so kill it
                        // remove the weapon first
                        WeaponEntities.removePlayersWeapon(world, componentMap.getEntity());
                        
                        Optional<NoodleComponent> noodleComponent;
                        noodleComponent = 
                        		world.getComponent(componentMap.getEntity(), NoodleComponent.class);
                        // If the NoodleComponent is present, the entity is assumed to be a noodle.
                        if (noodleComponent.isPresent()) {
                            PlayerEntities.createDeadPlayer(componentMap.getEntity());
                        } else {
                            // Otherwise not noodle and entity just needs to be disposed of.
                            world.destroyEntity(componentMap.getEntity());
                        }
                        break;
                    }
              
                }
            }
        }
	}
}
	
	