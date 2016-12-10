package uq.deco2800.dangernoodles.systems;


import uq.deco2800.dangernoodles.AudioManager;
import uq.deco2800.dangernoodles.components.CollisionComponent;
import uq.deco2800.dangernoodles.components.InputComponent;
import uq.deco2800.dangernoodles.components.MovementComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.components.stats.SpeedComponent;
import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;

import java.util.Optional;
import java.util.Random;

/**
 * This function processes the new velocities and new positions for the movement components of entities each tick.
 * Currently it works by assigning the positionComponent's nextX & nextY variables to the coordinates that they would
 * move to if undisturbed. Collision Checks need to be performed before the positions can actually be given to them to
 * draw.
 * 
 * @author Luke
 *
 */
public class MovementSystem implements System {
    @Override
    public void run(World world, double t, double dt) {
        for (ComponentMap cm : world.getIterator(
                MovementComponent.class,
                InputComponent.class,
                SpeedComponent.class,
                CollisionComponent.class)) {
            MovementComponent m = cm.get(MovementComponent.class);
            InputComponent i = cm.get(InputComponent.class);
            SpeedComponent s = cm.get(SpeedComponent.class);
            CollisionComponent c = cm.get(CollisionComponent.class);
            if (m.isMovable()) {
                m.setVx(m.getVX() + i.getX() * s.getSpeed() * dt);
                if (c.isCollided()) {
                	// Plays a jumping sound if a jump is detected
                    if (m.getVY() <= 0 && i.getY() < -0.1) {
                    	Random random = new Random();
                    	AudioManager.playSound("resources/sounds/jump" + (random.nextInt(2) + 1)+ ".wav", 
                    		false);
                    }
                    
                    if (jumpCollision(world, c)) {
                    	m.setVy(m.getVY() + i.getY() * 10000 * dt);
                    }
                }
            } else {
                m.setVx(0);
            }
        }

        for (MovementComponent m : world.getComponents(MovementComponent.class)) {

            // If the noodle is out of mana, it will not be able to move.
            if (m.isMovable()) {
                // Determine new velocities based on acceleration on the entities
                if (Math.abs(m.getVX()) < 0.5) { // If Noodle has
                    // insignificant x velocity, halt it.
                    m.setIsMovingX(false);
                    m.setVx(0);
                } else { // entity is moving horizontally (x)
                    m.setIsMovingX(true);
                    m.setLastMovingLeft(m.getVX() < 0.0);
                }
                
                if (Math.abs(m.getVY()) < 50.0) { // If Noodle has insignificant y velocity, set parameter accordingly
                	m.setIsMovingY(false);
                } else {
                	m.setIsMovingY(true);
                }
                
                m.setVelocity(m.getVX() + m.getAX() * dt, m.getVY() + m.getAY() * dt);
            } else {
                m.setVelocity(0, m.getVY() + m.getAY() * dt);
            }
        }

        // Assign the new positions to the positionComponents
        for (ComponentMap cm : world.getIterator(MovementComponent.class, PositionComponent.class)) {
            MovementComponent m = cm.get(MovementComponent.class);
            PositionComponent p = cm.get(PositionComponent.class);

            p.setNextX(p.getX() + m.getVX() * dt);
            p.setNextY(p.getY() + m.getVY() * dt);
        }
    }

    private boolean jumpCollision(World world, CollisionComponent c) {
        boolean returnValue = false;

        Optional<PlayerComponent> thisPlayerOptional = world.getComponent(c
                .getEntity(), PlayerComponent.class);

        for (Entity collision : c.getCollisions()) {
            Optional<TileRenderComponent> tileOptional = world.getComponent
                    (collision, TileRenderComponent.class);
            Optional<PlayerComponent> playerOptional = world.getComponent
                    (collision, PlayerComponent.class);

            if (tileOptional.isPresent()) {
                returnValue = true;
                break;
            }

            if (playerOptional.isPresent() && thisPlayerOptional.isPresent()) {
                PlayerComponent otherPlayer = playerOptional.get();
                PlayerComponent thisPlayer = thisPlayerOptional.get();
                if (otherPlayer.getTeamId() != thisPlayer.getTeamId()) {
                    returnValue = true;
                    break;
                }
            }
        }

        return returnValue;
    }
}
