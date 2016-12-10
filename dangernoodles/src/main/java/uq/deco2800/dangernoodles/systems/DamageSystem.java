package uq.deco2800.dangernoodles.systems;

import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.components.stats.DamageComponent;
import uq.deco2800.dangernoodles.components.stats.InvulnerableComponent;
import uq.deco2800.dangernoodles.components.stats.ShieldComponent;
import uq.deco2800.dangernoodles.ecs.*;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.prefabs.PlayerEntities;
import uq.deco2800.dangernoodles.AudioManager;
import uq.deco2800.dangernoodles.AudioPlayer;
import uq.deco2800.dangernoodles.components.CollisionComponent;
import uq.deco2800.dangernoodles.components.PlayerComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.TileRenderComponent;
import uq.deco2800.dangernoodles.components.damages.InstantDamageComponent;
import uq.deco2800.dangernoodles.components.noodles.NoodleComponent;
import uq.deco2800.dangernoodles.components.stats.HealthComponent;
import uq.deco2800.dangernoodles.prefabs.WeaponEntities;

import java.util.List;
import java.util.Optional;
import java.util.Random;


/**
 * Created by khoi_truong on 2016/09/17.
 * <p>
 * This class is used to handle damage mechanism of the game.
 */
public class DamageSystem implements System {
	// Instance variables
	// Makes only one terrain destruction sound play at most during system run.
	private static boolean terrainDestroyedPreviously = false;

    /**
     * Is called every tick and runs with the t and dt parameters
     *
     * @param world
     * @param t
     *         Time since the beginning of the game
     * @param dt
     *         The delta time, i.e. the time since last frame
     */
    @Override
    public void run(World world, double t, double dt) {
        double baseDamage = 0;
        // Get the noodle who just shot the projectile
        for (ComponentMap cm : world.getIterator(
                TurnComponent.class,
                DamageComponent.class)) {
            // Grab this noodle by checking its turn
            TurnComponent turn = cm.get(TurnComponent.class);
            if (turn.getTurn()) {
                DamageComponent damage = cm.get(DamageComponent.class);
                baseDamage = damage.getDamage();
                break;
            }
        }


        // Get all entities that have an instant damage and collision component
        for (ComponentMap cm : world.getIterator(
                InstantDamageComponent.class,
                CollisionComponent.class)) {
            // Retrieve the components
            InstantDamageComponent damage = cm.get(InstantDamageComponent.class);
            CollisionComponent collision = cm.get(CollisionComponent.class);
            
            Optional<PositionComponent> position = world
                    .getComponent(cm.getEntity(), PositionComponent.class);
            // Get the list of collided entities with this entity
            List<Entity> entities = collision.getCollisions();


            // Start looping the list, if the entity hit any
            // player/noodle, then start inflicting damage on it.
            for (int i = 0; i < entities.size(); i++) {
                Entity entity = entities.get(i);
                Optional<PlayerComponent> playerOpt;
                playerOpt = world.getComponent(entity, PlayerComponent.class);
                Optional<HealthComponent> healthOpt;
                healthOpt = world.getComponent(entity, HealthComponent.class);
                Optional<InvulnerableComponent> invulnerableOpt;
                invulnerableOpt = world.getComponent(entity, 
                        InvulnerableComponent.class);
                Optional<ShieldComponent> shieldOpt;
                shieldOpt = world.getComponent(entity, ShieldComponent.class);
                
                Optional<TileRenderComponent> terrainOpt;
                terrainOpt = world.getComponent(entity, TileRenderComponent.class);
                
                // Attempt to retrieve the noodle component
                Optional<NoodleComponent> noodleComponentOptional = world.getComponent(entity, NoodleComponent.class);

                //Don't damage the parent of the weapon
                if (collision.getIgnoredEntities().contains(entity)) {
                    continue;
                }

                if (playerOpt.isPresent() &&
                        healthOpt.isPresent() &&
                        invulnerableOpt.isPresent() &&
                        shieldOpt.isPresent()) {

                    // If the target is currently invulnerable, then
                    // do nothing. Otherwise, reduce its health.
                    if (!invulnerableOpt.get().isInvulnerable()) {
                        // Get shield that this noodle is holding
                        ShieldComponent shield = shieldOpt.get();
                        double currentShield = shield.getShield();
                        // Get the current health of the collided noodle
                        // and update its health.
                        HealthComponent health = healthOpt.get();
                        int currentHealth = health.getHealth();
                        // Calculate the total damage, that is the damage
                        // from this weapon/projectile plus the base
                        // damage of the shooter.
                        double newDamage = baseDamage +
                                damage.getAmount() - currentShield;
                        if (newDamage < 0) {
                            newDamage = 0;
                        }
                        int newHealth = currentHealth - (int) newDamage;
                        // If the new health is more than zero, than set
                        // it. Otherwise, kill the noodle.
                        if (newHealth > 0) {
                        	// Plays noodle damage sound if damaged entity is a noodle.
                        	if (noodleComponentOptional.isPresent()) {
                        		playNoodleDamageSound(noodleComponentOptional.get(), damage.getAmount());
                        	}
                        	
                            health.setHealth(newHealth);
                        } else {
                            // the player has died so kill it
                            // remove the weapon first
                            WeaponEntities.removePlayersWeapon(world, entity);
                            
                            Optional<NoodleComponent> noodleComponent;
                            noodleComponent = world.getComponent(entity, NoodleComponent.class);
                            // If the NoodleComponent is present, the entity is assumed to be a noodle.
                            if (noodleComponent.isPresent()) {
                                PlayerEntities.createDeadPlayer(entity);
                            } else {
                                // Otherwise not noodle and entity just needs to be disposed of.
                                world.destroyEntity(entity);
                            }
                            break;
                        }
                    }
                }else if (terrainOpt.isPresent() && healthOpt.isPresent()) {
                    HealthComponent health = healthOpt.get();
                    int currentHealth = health.getHealth();
                    
                    double x = position.get().getX();
                    double y = position.get().getY();
                    
                    Optional<PositionComponent> posComponent;
                    posComponent = world.getComponent(entity, PositionComponent.class);
                    
                    double x2 = posComponent.get().getX();
                    double y2 = posComponent.get().getY();
                    
                    double distanceX = Math.abs(x2 -x);
                    double distanceY = Math.abs(y2 -y);
                    double distance = Math.sqrt(Math.pow(distanceX,2) + Math.pow(distanceY,2));
                	double newDamage = baseDamage +
                            damage.getAmount() - (distance/4);
                    if (newDamage < 0) {
                        newDamage = 0;
                    }
                    int newHealth = currentHealth - (int) newDamage;
                    if (newHealth > 0) {
                    	if (noodleComponentOptional.isPresent()) {
                    		//play terrain destroy sound
                    	}
                    	
                        health.setHealth(newHealth);
                    } else {
                    	//destroy terrain
                    	world.destroyEntity(entity);
                    	
                    	// Play sound for tile being destroyed at most once during system run.
                    	if (!terrainDestroyedPreviously) {
                    		Random random = new Random();
                    		AudioManager.playSound("resources/sounds/terrain" + (random.nextInt(4) + 1) + 
                    				".wav", false);
                    	}
                    }

                }
            }
        }
    }

    /**
     * Plays the appropriate noodle injury sound based on the noodle type that was hurt and how much damage was done.
     * @require noodleComponent != null && damage > 0
     * @ensure Appropriate noodle injury sound is played with proportional volume.
     * @param noodleComponent Noodle Component of damaged noodle.
     * @param damage Damage done to the noodle.
     */
	private void playNoodleDamageSound(NoodleComponent noodleComponent, double damage) {
		String file;
		// There are two versions of each damage sound, pick at random one.
		int fileVersion = (int)Math.ceil(Math.random() * 2);
		
		// Finding the appropriate file name for noodle damage by character class.
		switch (noodleComponent.getNoodleClass()) {
			case NOODLE_TANK: // Heavy metal hit sound for tanks.
				file = "hit_metal_heavy" + fileVersion;
				break;
			case NOODLE_WARRIOR: // Lighter metal hit for warriors.
				file = "hit_metal_light" + fileVersion;
				break;
			case NOODLE_AGILITY: // Hissing from agility noodles.
				file = "hiss_" + (fileVersion > 1 ? "heavy1" : "light1");
				break;
			default: // Duck sounds for civilians of anything else. Could be changed to hissing as well.
				file = "duck" + fileVersion;
				break;
		}
		
		// Play the audio file, filepath is formed through String concatenation. Damage gain if proportional to damage
		AudioPlayer damageSound = AudioManager.playSound("resources/sounds/" + file + ".wav", false, 
				1.3f + (float)damage * 0.1f);
	}
}
