package uq.deco2800.dangernoodles.systems;

import uq.deco2800.dangernoodles.AudioManager;
import uq.deco2800.dangernoodles.AudioPlayer;
import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;


/**
 * Created by Jason on 4/09/2016, modified by khoi_truong.
 * <p>
 * This system is used to handle the deletion of the parachute once the box has
 * landed.
 */
public class ParachuteSystem implements System {

    /**
     * Is called every tick and runs with the t and dt parameters
     *
     * @param world
     *         parent world
     * @param t
     *         Time since the beginning of the game
     * @param dt
     *         The delta time, i.e. the time since last frame
     */
    @Override
    public void run(World world, double t, double dt) {
        // Retrieve all of the parachute which has name and collision components in the world.
        for (ComponentMap cm : world.getIterator(
                NameComponent.class,
                CollisionComponent.class)) {
            // Retrieve the name component
            NameComponent name = cm.get(NameComponent.class);
            // Check if this is actually a parachute
            if ("Parachute".equals(name.getName())) {
                // Retrieve collision component.
                CollisionComponent collision = cm.get(CollisionComponent.class);
                // If collision happens, destroy the parachute.
                if(!collision.getCollisions().isEmpty()) {
                    world.destroyEntity(name.getEntity());
                    //When parachute is destroyed, the crate has hit the ground
                    // Plays sound of dropping crate
                    AudioPlayer parachuteAudioPlayer;
                    parachuteAudioPlayer = AudioManager.playSound("resources/sounds/crate2.wav", false);

                }
                collision.resetCollisions();
                break;
            }
        }
    }
}



