package uq.deco2800.dangernoodles.systems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import uq.deco2800.dangernoodles.components.AIComponent;
import uq.deco2800.dangernoodles.components.CollisionComponent;
import uq.deco2800.dangernoodles.components.InputComponent;
import uq.deco2800.dangernoodles.components.MovementComponent;
import uq.deco2800.dangernoodles.components.PlayerComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.RectangleComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.components.TileRenderComponent;
import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.components.noodles.NoodleComponent;
import uq.deco2800.dangernoodles.components.stats.HealthComponent;
import uq.deco2800.dangernoodles.components.stats.SpeedComponent;
import uq.deco2800.dangernoodles.components.weapons.WeaponComponent;
import uq.deco2800.dangernoodles.components.weather.WindComponent;
import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.WeaponEntities;

public class AISystem implements System {

    public void run(World world, double t, double dt) {

        // search for AI player whose turn to play is true
        Entity aiPlayer = activeAI(world);

        // stop all AI movements for all AI players that are not aiPlayer
        List<AIComponent> aiComponents = world.getComponents(AIComponent.class);

        if (aiComponents != null && !aiComponents.isEmpty()) {

            for (AIComponent aiComponent : aiComponents) {
                if (aiComponent.getEntity() != aiPlayer) {
                    setAIMovementDirection(Direction.STAY, Direction.STAY, aiComponent.getEntity(), world);
                    aiComponent.setFired(false);
                } else {
                    setAIMovementDirection(null, Direction.STAY, aiComponent.getEntity(), world);
                }
            }
        }

        // if no AI to control, return
        if (aiPlayer == null) {
            return;
        }

        // move the AI back and forth by a maximum of 100 pixels
        moveBackAndForth(aiPlayer, 200, world);

        // set AI's target by setting its AIComponent and select its weapon
        setTargetEntity(aiPlayer, getPotentialTargets(world, aiPlayer), world);

        // attack target
        shootTarget(aiPlayer, world);
    }

    /**
     * @author Tony Prusac
     *
     * @param aiPlayer
     *            The Ai Entity relative to which we are seeking targets from
     * 
     * @param potentialTargets
     *            Every enemy Noodle on the game board
     * 
     * @param world
     *            The world
     * 
     * @return The entity of the closest Enemy Noodle
     */

    public void setTargetEntity(Entity aiPlayer, List<PositionComponent> potentialTargets, World world) {
        PositionComponent aiPosition = world.getComponent(aiPlayer, PositionComponent.class).get();

        double x = aiPosition.getX();
        double y = aiPosition.getY();
        double distance = 999999;
        double latestDistance;

        for (PositionComponent targetPos : potentialTargets) {
            latestDistance = Math.sqrt(Math.pow(targetPos.getX() - x, 2) + Math.pow(targetPos.getY() - y, 2));

            if (latestDistance < distance) {
                distance = latestDistance;
                world.getComponent(aiPlayer, AIComponent.class).get().setTarget(targetPos);
            }
        }
    }

    /**
     * The AI will change out of the default pistol weapon depending on two possible situations 1. there is an
     * additional enemy noodle in close proximity to the target 2. the target is horizontally aligned with the AI player
     * 
     * @author Tony
     *
     * @param world
     *            the world
     * 
     * @param aiPlayer
     *            the chosen AI to execute its moves
     * 
     * @param targets
     *            A list of all possible targets position in the map
     */
    public void setAIWeapon(World world, Entity aiPlayer, List<PositionComponent> targets) {
        PositionComponent target = world.getComponent(aiPlayer, AIComponent.class).get().getTargetPosition();
        // Setup default AI weapons
        int[] weaponSet = { 4, 1 };
        // If difficulty is Hard, the default weapon for the AI will be the rocket launcher
        if (world.getComponent(aiPlayer, AIComponent.class).get().getDifficulty() == "top") {
            WeaponEntities.removePlayersWeapon(world, aiPlayer);
            WeaponEntities.createWeapon(world, world.getWeaponDefinitions().getWeaponByID(4), aiPlayer);
            weaponSet[0] = 3;
            weaponSet[1] = 6;
        }
        for (PositionComponent enemy : targets) {
            // If there are two enemies close to each other the AI will equip the rocket launcher
            if (target != enemy && Math.abs(enemy.getY() - target.getY()) < 50
                    && Math.abs(enemy.getX() - target.getX()) < 50) {
                WeaponEntities.removePlayersWeapon(world, aiPlayer);
                WeaponEntities.createWeapon(world, world.getWeaponDefinitions().getWeaponByID(weaponSet[0]), aiPlayer);
                return;
            }
        }
        // If the enemy is on a horizontal plane, the AI will equip a grenade
        if (world.getComponent(aiPlayer, PositionComponent.class).get().getY() - target.getY() < 50) {
            WeaponEntities.removePlayersWeapon(world, aiPlayer);
            WeaponEntities.createWeapon(world, world.getWeaponDefinitions().getWeaponByID(weaponSet[1]), aiPlayer);

        }
    }

    /**
     * This method will find which AI Entity will be receiving the AI commands
     * 
     * @author Tony Prusac
     *
     * @param world
     * 
     * @return an AI Entity if and only if it is that AI's turn otherwise return null
     */
    public Entity activeAI(World world) {
        for (TurnComponent turn : world.getComponents(TurnComponent.class)) {
            if (turn.getTurn() && world.hasComponent(turn.getEntity(), AIComponent.class)) {
                return turn.getEntity();
            }
        }

        return null;
    }

    /**
     * This method makes the AI jump if it is stuck and there is still some room to leap forward in the given direction.
     * 
     * @author Irsan
     * 
     * @param direction
     *            the current horizontal direction of travel
     * 
     * @param aiPlayer
     *            the AI player
     * 
     * @param world
     *            the game world
     */
    public void jumpIfStuck(Direction direction, Entity aiPlayer, World world) {

        PositionComponent aiPos = world.getComponent(aiPlayer, PositionComponent.class).get();
        RectangleComponent aiRect = world.getComponent(aiPlayer, RectangleComponent.class).get();

        // map x-coordinate end-points
        List<Double> mapEndPoints = mapEndPoints(world);
        double mapLeftEnd = mapEndPoints.get(0);
        double mapRightEnd = mapEndPoints.get(1);

        // check if AI is stuck
        boolean stuck = isStuck(aiPlayer, aiPos, aiRect, world);

        // check if there is still some distance to the either end of the map
        boolean condition1 = direction == Direction.LEFT && aiPos.getX() - mapLeftEnd > 100;
        boolean condition2 = direction == Direction.RIGHT && mapRightEnd - aiPos.getX() > 100;

        // jump if required
        if ((condition1 || condition2) && stuck) {
            setAIMovementDirection(direction, Direction.UP, aiPlayer, world);
        }
    }

    private static final String WATER = "water";

    /**
     * This method checks whether the given AI player is stuck. There are three conditions for AI to be stuck:
     * 
     * 1. AI sits in between two topological boundaries such that the distance between the boundaries is less than 2 *
     * AI's width.
     * 
     * 2. AI has collided with a player on one side, and on the other side, AI is bounded by a topological boundary such
     * that the distance to the boundary is less than 2 * AI's width.
     * 
     * 3. AI sits in between two collidable entities such that the distance between the two entities is less than 2 *
     * AI's width.
     * 
     * @param aiPlayer
     *            the AI player
     * 
     * @param aiPos
     *            the position of AI player
     * 
     * @param aiRect
     *            the RectangleComponent of AI player
     * 
     * @param world
     *            this game world
     * 
     * @return condition1 || condition2 || condition3
     */
    private boolean isStuck(Entity aiPlayer, PositionComponent aiPos, RectangleComponent aiRect, World world) {

        double distanceToBoundaryLeft = distanceToBoundary(Direction.LEFT, aiPlayer, world);
        double distanceToBoundaryRight = distanceToBoundary(Direction.RIGHT, aiPlayer, world);
        double distanceToBothBoundaries = distanceToBoundaryRight + distanceToBoundaryLeft - aiRect.getWidth();

        boolean stuckCondition1 = distanceToBothBoundaries < 2 * aiRect.getWidth();

        boolean stuckCondition2 = hasCollided(aiPlayer, Direction.LEFT, world)
                && distanceToBoundaryRight < 2 * aiRect.getWidth();

        boolean stuckCondition3 = hasCollided(aiPlayer, Direction.RIGHT, world)
                && distanceToBoundaryLeft < 2 * aiRect.getWidth();

        boolean stuckCondition4 = surrounded(aiPos, aiRect, world);

        return stuckCondition1 || stuckCondition2 || stuckCondition3 || stuckCondition4;
    }

    /**
     * A helper method to isStuck() method. This method checks whether the AI player with the given position and
     * rectangle components is surrounded on two sides (left and right) by entities that can collide with it.
     *
     * @author Irsan
     * 
     * @param aiPos
     *            the position of the AI
     * 
     * @param aiRect
     *            the rectangle component of the AI
     * 
     * @param world
     *            the game world
     * 
     * @return true if and only if AI sits in between two collidable entities such that the distance between the two
     *         entities is less than 2 * AI's width.
     */
    private boolean surrounded(PositionComponent aiPos, RectangleComponent aiRect, World world) {
        double leftBound = -1;
        double rightBound = 9999;
        double aiLeft = aiPos.getX();
        double aiTop = aiPos.getY();
        double aiRight = aiLeft + aiRect.getWidth();
        double aiBottom = aiTop + aiRect.getHeight();

        // for every collidable entity, check if its position is eligible for updating the left and right bounds
        for (ComponentMap cm : world.getIterator(PositionComponent.class, RectangleComponent.class,
                CollisionComponent.class, SpriteComponent.class)) {

            // do not consider water entities
            SpriteComponent entSprite = cm.get(SpriteComponent.class);
            if (entSprite.getImage().contains(WATER)) {
                continue;
            }

            // entity left, right, top, and bottom coordinates
            PositionComponent entPos = cm.get(PositionComponent.class);
            RectangleComponent entRect = cm.get(RectangleComponent.class);
            double entLeft = entPos.getX();
            double entTop = entPos.getY();
            double entRight = entLeft + entRect.getWidth();
            double entBottom = entTop + entRect.getHeight();

            // update left and right bounds
            if ((entTop <= aiBottom && entTop >= aiTop) || (entBottom >= aiTop && entBottom <= aiBottom)) {

                // update the closest coordinate of collidable entity located to the left of AI
                if (entRight < aiLeft && entRight > leftBound) {
                    leftBound = entRight;

                    // update the closest coordinate of collidable entity located to the right of AI
                } else if (entLeft > aiRight && entLeft < rightBound) {
                    rightBound = entLeft;
                }
            }
        }

        return rightBound - leftBound < 2 * aiRect.getWidth();
    }

    /**
     * This method returns a list of left-most and right-most end-points of the terrain in terms of their x-coordinates.
     * 
     * @author Irsan
     * 
     * @param world
     *            this game world
     * 
     * @return a list of x-coordinates such that list.get(0) is the left-most end-point of the terrain and the other
     *         element is the right-most end-point
     */
    private List<Double> mapEndPoints(World world) {
        double mapLeftEnd = 0; // the left-most end-point x-coordinate
        double mapRightEnd = 0; // the right-most end-point x-coordinate

        // find the left- and right-most end-points of the terrain
        for (ComponentMap cm : world.getIterator(TileRenderComponent.class, PositionComponent.class,
                RectangleComponent.class, SpriteComponent.class)) {

            // do not consider water tiles as they do not collide obviously
            SpriteComponent tileSprite = cm.get(SpriteComponent.class);
            if (tileSprite.getImage().contains(WATER)) {
                continue;
            }

            PositionComponent tilePos = cm.get(PositionComponent.class);
            RectangleComponent tileRect = cm.get(RectangleComponent.class);

            // the x-coordinates of the left and right edges of the tile
            double tileLeft = tilePos.getX();
            double tileRight = tileLeft + tileRect.getWidth();

            // update left- and right-most end-points
            if (tileLeft < mapLeftEnd) {
                mapLeftEnd = tileLeft;
            } else if (tileRight > mapRightEnd) {
                mapRightEnd = tileRight;
            }
        }

        // and the end-point coordinates into the list and return
        List<Double> endPoints = new ArrayList<>();
        endPoints.add(0, mapLeftEnd);
        endPoints.add(1, mapRightEnd);

        return endPoints;
    }

    /**
     * This method controls the given AI player so that it moves back and forth. For each horizontal direction, the AI
     * moves for at most the specified distance before turning around and travel in the other direction for the same
     * amount of distance. Calling this method repeatedly will move the AI back and forth indefinitely long.
     * 
     * Before reaching the maximum distance per travel direction, if the AI bumps into another player or the distance
     * between it and an obstacle (such as an edge of a gap, an end of the terrain, or a blocking tile) is less than the
     * allowable safety distance (calculated based on speed and acceleration), then the AI will turn around and travel
     * for at most the given distance.
     * 
     * @author Irsan Winarto
     * 
     * @param aiPlayer
     *            the AI player to be controlled
     * 
     * @param distance
     *            the maximum distance per horizontal direction of movement
     * 
     * @param world
     *            this game world
     */
    public void moveBackAndForth(Entity aiPlayer, double distance, World world) {

        AIComponent aiComponent = world.getComponent(aiPlayer, AIComponent.class).get();
        InputComponent aiInput = world.getComponent(aiPlayer, InputComponent.class).get();
        PositionComponent aiPosition = world.getComponent(aiPlayer, PositionComponent.class).get();
        PositionComponent destination = aiComponent.getDestination();

        double directionX = aiInput.getX(); // AI's current horizontal movement direction
        double currentX = aiPosition.getX(); // AI's current position
        double destinationX; // AI's next horizontal destination
        final double limit = 10; // Error limit

        // if destination has not been set default the noodle to move to the left
        if (destination == null) {
            destinationX = currentX - distance;
        } else {
            destinationX = destination.getX();

            // else if the destination is to the left but noodle has turned, then set new destination to the right
            if (Double.compare(destinationX, currentX) < 0 && directionX > 0) {
                destinationX = currentX + distance;

                // else if the destination is to the right but noodle has turned, then set new destination to the left
            } else if ((Double.compare(destinationX, currentX) > 0 && directionX < 0)
                    || (Math.abs(destinationX - currentX) < limit && Double.compare(directionX, 0) == 0)) {
                destinationX = currentX - distance;

            }
        }

        moveAI(aiPlayer, destinationX, "turn", world);
    }

    /**
     * This method sets the direction of AI's movement toward the given horizontal destination coordinate. It also turns
     * around the AI player if the AI encountered an obstacle (a gap, a collision, or an edge of the terrain) before it
     * reaches its destination, only if the turn mode is "turn"; otherwise, the AI simply stops moving.
     * 
     * @author Irsan
     * 
     * @param aiPlayer
     *            is the AI player entity
     * 
     * @param destinationX
     *            is the horizontal coordinate of AI's destination
     * 
     * @param handleMode
     *            if set to "turn", AI player will turn around if it encounters an obstacle (a gap, a collision, or an
     *            edge of the terrain)
     * 
     * @param world
     *            is the game world
     * 
     * @require aiPlayer != null && world != null && destinationX is the destination x coordinate && handleMode != null
     *          && aiPlayer has an AIComponent, PositionComponent, and a MovementComponent
     * 
     * @ensure aiPlayer stops at the given x coordinate if there is no collision or if it is not standing on a cliff;
     *         otherwise, aiPlayer either stops or turn away as specified by handleMode.
     */
    public void moveAI(Entity aiPlayer, double destinationX, String handleMode, World world) {

        AIComponent aiComponent = world.getComponent(aiPlayer, AIComponent.class).get();
        InputComponent aiInput = world.getComponent(aiPlayer, InputComponent.class).get();
        MovementComponent aiMovement = world.getComponent(aiPlayer, MovementComponent.class).get();
        PositionComponent currentPos = world.getComponent(aiPlayer, PositionComponent.class).get();
        RectangleComponent aiRect = world.getComponent(aiPlayer, RectangleComponent.class).get();
        SpeedComponent aiSpeed = world.getComponent(aiPlayer, SpeedComponent.class).get();

        // update destination
        PositionComponent destination = aiComponent.getDestination();

        // set ai's new destination
        if (destination == null) {
            destination = new PositionComponent(destinationX, 0);
            aiComponent.setDestination(destination);
        } else if (Double.compare(destination.getX(), destinationX) != 0) {
            destination.setX(destinationX);
        }

        // calculate safe distance to boundary
        final double distanceLimit = estimateSafeDistanceToBoundary(10, aiMovement, aiInput, aiSpeed);
        // error limit in pixels
        final double limit = calculateBreakDistance(destinationX, aiPlayer, world);
        // current x coordinate position
        double currentX = currentPos.getX();
        // the horizontal displacement to destination
        double difference = (currentX + aiRect.getWidth() / 2) - destinationX;

        // movement directions
        Direction moveTo;
        Direction turnTo;

        if (difference < -limit) {
            // the destination is to the right; turn around if there is an obstacle in between
            moveTo = Direction.RIGHT;
            turnTo = Direction.LEFT;
        } else if (difference > limit) {
            // the destination is to the left; turn around if there is an obstacle in between
            moveTo = Direction.LEFT;
            turnTo = Direction.RIGHT;
        } else {

            applyBreak(difference, aiPlayer, world);
            return;
        }

        // move the AI toward the direction set
        setAIMovementDirection(moveTo, null, aiPlayer, world);

        // if AI has collided or is on a cliff, either turn away or stay where it is
        if (hasCollided(aiPlayer, moveTo, world) || distanceToBoundary(moveTo, aiPlayer, world) <= distanceLimit) {

            jumpIfStuck(turnTo, aiPlayer, world);

            if ("turn".equals(handleMode)) {
                // turn AI player around
                setAIMovementDirection(turnTo, null, aiPlayer, world);
            } else {
                // stop AI player
                setAIMovementDirection(Direction.STAY, null, aiPlayer, world);
            }
        }
    }

    /**
     * This method calculates the distance required to apply horizontal input in the opposite direction of travel for
     * slowing the AI down.
     * 
     * @author Irsan
     * 
     * @param destinationX
     *            the AI's horizontal destination
     * 
     * @param aiPlayer
     *            the AI player
     * 
     * @param world
     *            the game world
     * 
     * @return the leftover distance to destinationX at which point it is optimal to apply a new horizontal input
     *         opposite to the AI's current travel direction
     */
    private double calculateBreakDistance(double destinationX, Entity aiPlayer, World world) {

        PositionComponent aiPos = world.getComponent(aiPlayer, PositionComponent.class).get();
        MovementComponent aiMovement = world.getComponent(aiPlayer, MovementComponent.class).get();
        SpeedComponent aiSpeed = world.getComponent(aiPlayer, SpeedComponent.class).get();

        double diffX = Math.abs(destinationX - aiPos.getX());
        double vx = aiMovement.getVX();
        double ax = aiSpeed.getSpeed();
        double safetyFactor = 1.1;
        double determinant = Math.pow(vx, 2) - 2 * ax * diffX;

        // if determinant is non-negative, return the following
        return determinant >= 0 ? Math.max(diffX * safetyFactor, 30) : 30;
    }

    /**
     * This method applies a new horizontal input direction that is opposite to the AI's current travel direction, which
     * will slow it down.
     * 
     * @param difference
     *            the displacement between AI's position and its destination.
     * 
     * @param aiPlayer
     *            the AI player
     * 
     * @param world
     *            the game world
     */
    private void applyBreak(double difference, Entity aiPlayer, World world) {

        // the break direction
        Direction turnTo;

        // this is a safety distance that indicates AI should stop when it is within limit distance to its destination
        double limit = 30;

        // get break direction
        if (difference < -limit) {
            turnTo = Direction.LEFT;
        } else if (difference > limit) {
            turnTo = Direction.RIGHT;
        } else {
            // stop AI when it is close enough to its destination
            turnTo = Direction.STAY;
        }

        // break or stop
        setAIMovementDirection(turnTo, null, aiPlayer, world);
    }

    /**
     * This method estimates the horizontal distance that the AI player will cover if it moves horizontally for a
     * specified amount of game ticks.
     * 
     * @author Irsan
     * 
     * @param ticks
     *            the number of game ticks for which length of time the distance traversed is to be estimated
     * @param aiMovement
     *            the MovementComponent of AI
     * @param aiInput
     *            the InputComponent of AI
     * @param aiSpeed
     *            the SpeedComponent of AI
     * @return the estimated distance that would be traversed over ticks number of game ticks
     */
    private double estimateSafeDistanceToBoundary(int ticks, MovementComponent aiMovement, InputComponent aiInput,
            SpeedComponent aiSpeed) {

        double timeLength = ticks * 0.016;

        double estimatedDistance = Math.abs(
                aiMovement.getVX() * timeLength + aiInput.getX() * aiSpeed.getSpeed() * Math.pow(timeLength, 2) / 2);

        return Math.max(estimatedDistance, 12.5);
    }

    /**
     * This method checks whether the given AI player has collided with another player in the given direction of
     * movement.
     * 
     * @param aiPlayer
     *            the AI player to be checked
     * @param direction
     *            the direction toward which the collision is to be checked
     * @param world
     *            the game world
     * @return true if and only if the AI player has collided in the given direction
     */
    private boolean hasCollided(Entity aiPlayer, Direction direction, World world) {
        List<Entity> collisions = world.getComponent(aiPlayer, CollisionComponent.class).get().getCollisions();

        // check if entity collided
        if (collisions == null || collisions.isEmpty()) {
            return false;
        }

        PositionComponent aiPosition = world.getComponent(aiPlayer, PositionComponent.class).get();
        Optional<PositionComponent> collisionPosition;

        for (Entity collidedEntity : collisions) {

            // location of collided entity
            collisionPosition = world.getComponent(collidedEntity, PositionComponent.class);

            // check if collision happened with a player or a weapon
            if (collisionPosition.isPresent() && (world.hasComponent(collidedEntity, PlayerComponent.class)
                    || world.hasComponent(collidedEntity, NoodleComponent.class))) {

                // check whether collision has happened in the given direction
                if ((direction == Direction.UP || direction == Direction.STAY)
                        && Double.compare(aiPosition.getY(), collisionPosition.get().getY()) <= 0) {
                    return true;
                }

                if ((direction == Direction.RIGHT || direction == Direction.STAY)
                        && Double.compare(aiPosition.getX(), collisionPosition.get().getX()) <= 0) {
                    return true;
                }

                if ((direction == Direction.LEFT || direction == Direction.STAY)
                        && Double.compare(aiPosition.getX(), collisionPosition.get().getX()) >= 0) {
                    return true;
                }

                if ((direction == Direction.DOWN || direction == Direction.STAY)
                        && Double.compare(aiPosition.getY(), collisionPosition.get().getY()) >= 0) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * This method sets the movement input of ai player.
     * 
     * @author Irsan
     * 
     * @param horizontal
     *            the horizontal input direction; if null, then the horizontal input of InputComponent is not modified
     * @param vertical
     *            the vertical input direction; if null, then the vertical input of InputComponent is not modified
     * @param aiPlayer
     *            the AI player whose movement is to be controlled by this method
     * @param world
     *            the game world
     */
    public void setAIMovementDirection(Direction horizontal, Direction vertical, Entity aiPlayer, World world) {
        // the input component of ai to control the direction of movement
        InputComponent aiInputComponent = world.getComponent(aiPlayer, InputComponent.class).get();

        // set movement direction
        if (horizontal != null) {
            aiInputComponent.setX(horizontal.getDirection());
        }

        if (vertical != null) {
            aiInputComponent.setY(vertical.getDirection());
        }
    }

    /**
     * 
     * This method returns a list of PositionComponents belonging to entities that have PlayerComponents that belong to
     * teams different from the team of aiComponent
     * 
     * @author Irsan winarto
     *
     * @param aiEntity
     *            the AI player
     * 
     * @param world
     *            the game world
     * 
     * @return a list of PositionComponents of coordinates of viable attack targets
     */
    public List<PositionComponent> getPotentialTargets(World world, Entity aiEntity) {
        // get all the PlayerComponents in the world; only players have both components
        List<PlayerComponent> playerComps = world.getComponents(PlayerComponent.class);

        // the list of potential target coordinates to be returned
        ArrayList<PositionComponent> potentialTargets = new ArrayList<>();

        Entity playerEntity; // an entity that owns a PlayerComponent

        /*
         * For every PlayerComponent, if its owner entity does not belong in the same team as the AIEntity then the
         * entity is a potential target.
         */
        for (PlayerComponent playerComponent : playerComps) {

            // the entity that owns the given playerComponent
            playerEntity = playerComponent.getEntity();

            // check playerEntity is not the AIEntity and does not belong in the same team as AIEntity
            if (!playerEntity.equals(aiEntity) && !sameTeam(world, aiEntity, playerComponent)
                    && world.hasComponent(playerEntity, HealthComponent.class)) {

                HealthComponent playerHealth = world.getComponent(playerEntity, HealthComponent.class).get();

                // playerEntity's position
                Optional<PositionComponent> positionComponent = world.getComponent(playerEntity,
                        PositionComponent.class);

                // found enemy; add it's position to the list
                if (positionComponent.isPresent() && playerHealth.getHealth() > 0) {
                    potentialTargets.add(positionComponent.get());
                }
            }
        }

        return potentialTargets;
    }

    /**
     * This method checks whether aiEntity is in the same team as the entity that owns the given playerComponent.
     * 
     * @author Irsan Winarto
     *
     * @param world
     *            this game world
     * 
     * @param aiEntity
     *            the AI player to be checked
     * 
     * @param playerComponent
     *            AI player's PlayerComponent
     *
     * @ensure true if and only if the PlayerComponent of AIEntity has the same teamID as playerComponent.getTeamID()
     */
    public boolean sameTeam(World world, Entity aiEntity, PlayerComponent playerComponent) {

        // the PlayerComponent of the AIEntity
        Optional<PlayerComponent> aiPlayerComponent = world.getComponent(aiEntity, PlayerComponent.class);

        // check whether AIEntity's teamID is the same as the given playerComponent's teamID
        if (aiPlayerComponent.get().getTeamId() == playerComponent.getTeamId()) {
            return true;
        }

        // AIEntity does not belong in the same team as the entity that owns the given playerComponent
        return false;
    }

    /**
     * This method shoots the target of AI player as specified by its AIComponent.
     * 
     * @author Irsan and Tony
     * 
     * @param aiPlayer
     *            the AI Player
     * @param world
     *            the game world
     */
    public void shootTarget(Entity aiPlayer, World world) {
        // get AIComponent
        AIComponent aiComponent = world.getComponent(aiPlayer, AIComponent.class).get();

        // get player's weapon
        List<WeaponComponent> weapons = world.getComponents(WeaponComponent.class);
        WeaponComponent weapon = null;
        if (!aiComponent.hasFired()) {
            setAIWeapon(world, aiPlayer, getPotentialTargets(world, aiPlayer));
        }

        // get weapon's position for trajectory calculation
        for (WeaponComponent w : weapons) {
            if (w.getParent() == aiPlayer) {
                weapon = w;
                aiComponent.setWeaponPosition(world.getComponent(w.getParent(), PositionComponent.class).get());
            }
        }

        // fire the weapon if weapon exists and it has not been fired
        if (weapon != null && !aiComponent.hasFired()) {

            if (weapon.getDefinition().isPowered()) {
                // sets the power and angle of attack for a non-bullet projectile
                setPowerAndAngleOfNonBullet(aiPlayer, weapon, world);

            } else {
                // sets the power and angle of attack for a bullet projectile
                setPowerAndAngleOfBullet(aiPlayer, weapon, world);
            }

            // fire
            aiComponent.setFired(true);
            weapon.setFired(true);
        }
    }

    /**
     * This method sets the power and angle of attack of the given weapon. The given weapon must be a so called
     * "powered" weapon as defined by the WeaponDefinition of it.
     * 
     * This method also gives randomness to the power and angle of attack, and the amount of randomness is determined by
     * and has been set in the AIComponent based on the difficulty level specified upon the creation of the AI player.
     * 
     * @author Irsan and Tony and rentalspace04 (LHexagon)
     * @param aiPlayer
     *            the AI player firing the given weapon
     * @param weapon
     *            the weapon whose power can be set
     * @param world
     *            the game world
     */
    private void setPowerAndAngleOfNonBullet(Entity aiPlayer, WeaponComponent weapon, World world) {

        AIComponent aiComponent = world.getComponent(aiPlayer, AIComponent.class).get();
        PositionComponent weaponPos = aiComponent.getWeaponPosition();
        PositionComponent targetPos = aiComponent.getTargetPosition();

        // set angle to default 45 degrees left or right for now; will upgrade this
        double angle = targetPos.getX() > weaponPos.getX() ? -45 : -135;
        double angleRad = Math.toRadians(angle);

        // calculate the would be position of the projectile
        double projectileX = weaponPos.getX() + weapon.getDefinition().getWidth() * Math.cos(angleRad);
        double projectileY = weaponPos.getY() + weapon.getDefinition().getWidth() * Math.sin(angleRad);

        // the distances along x and y axes between the target and the projectile to be shot
        double diffX = targetPos.getX() - projectileX;
        double diffY = targetPos.getY() - projectileY;

        // increase angle if needed
        angleRad = getAngle(diffX, diffY, false);
        angle = Math.toDegrees(angleRad);

        double power;

        WindComponent wind = world.getComponents(WindComponent.class).get(0);
        if (Double.compare(wind.getStrength(), 0) == 0) {
            // calculate exact power when there is no wind; check the wiki for explanation
            power = (diffX / (7.5 * Math.cos(angleRad))) * Math.sqrt(250 / (diffY - diffX * Math.tan(angleRad)));
        } else {
            // else if there is wind, then approximate the power required
            power = calculateWindAffectedPower(world, diffX, diffY, angleRad);
        }

        // add in some randomness based on difficulty as set in the AIComponent
        Random random = new Random();
        double randomFactor = 1 - aiComponent.getRandomness() + aiComponent.getRandomness() * random.nextFloat();

        // set weapon power and shot angle
        weapon.setDirection(angle * randomFactor);
        // set weapon power
        weapon.setPower(power * randomFactor);
    }

    /**
     * This method returns the angle of attack (in radians) required to shoot target. The default is 45 degrees above
     * the ground if the enemy is within reach. If not, the angle is increased to an appropriate one.
     * 
     * NOTE: clockwise is positive in JavaFx and counter-clockwise corresponds to a negative angle
     * 
     * @author Irsan
     * 
     * @param diffX
     *            the horizontal displacement from the target to the AI
     * 
     * @param diffY
     *            the vertical displacement from the target to the AI
     * 
     * @return the angle of attack in radians
     */
    public double getAngle(double diffX, double diffY, boolean powered) {

        double angleRad = calculateAngle(diffX, diffY);
        double angleDeg = Math.toDegrees(angleRad);
        double finalAngRad;

        // weapon is a hand gun or it is a so called "powered" weapon
        if (powered) {
            return angleRad;
        }

        double bonusAngle = 5;

        if (Math.abs(90 + angleDeg) > bonusAngle) {

            if (angleDeg >= -90 && angleDeg < -45) {
                // add 5 degrees extra above ground if the difference between max and angleDeg is greater than 5
                finalAngRad = Math.toRadians(Math.max(-90, angleDeg - bonusAngle));

            } else if (angleDeg <= -90 && angleDeg > -135) {
                // add 5 degrees extra above ground if the difference between max and angleDeg is greater than 5
                finalAngRad = Math.toRadians(Math.min(-90, angleDeg + bonusAngle));

            } else {
                // else if the angle is not at least 45 above ground, set it to 45 degrees above ground
                finalAngRad = diffX <= 0 ? Math.toRadians(-135) : Math.toRadians(-45);
            }

        } else {
            // else, don't add bonus angle
            finalAngRad = angleDeg >= -90 ? Math.toRadians(Math.max(-90, angleDeg))
                    : Math.toRadians(Math.min(-90, angleDeg));
        }

        return finalAngRad;
    }

    /**
     * This method turns the ratio diffY / diffX into the corresponding angle in radian.
     * 
     * @author Irsan
     * 
     * @param diffX
     *            the x-axis length
     * @param diffY
     *            the y-axis length
     * @return the angle in radians
     */
    private double calculateAngle(double diffX, double diffY) {
        // the angle to be returned
        double angleRad = 0;

        // get the correct angle in radians
        if (Double.compare(diffX, 0) != 0) {
            angleRad = Math.atan(diffY / diffX);
        }

        // angle correction
        if (diffX < 0 && diffY >= 0) {
            angleRad = Math.PI + angleRad;
        } else if (diffX < 0 && diffY < 0) {
            angleRad = -Math.PI + angleRad;
        }

        return angleRad;
    }

    /**
     * This method sets the angle of attack of a hand gun. The angle is directed toward the target enemy as set in
     * AIComponent. No randomness is added to the final angle of attack as the damage from a bullet is typically low.
     * 
     * @author Irsan
     * 
     * @param aiPlayer
     *            the AI holding the gun
     * @param weapon
     *            the gun
     * @param world
     *            the world
     * 
     * @require the target position set in AIComponent is not null && all parameters are not null
     * @ensure that angle of weapon and power of weapon are set
     */
    private void setPowerAndAngleOfBullet(Entity aiPlayer, WeaponComponent weapon, World world) {

        AIComponent aiComponent = world.getComponent(aiPlayer, AIComponent.class).get();
        PositionComponent aiPos = world.getComponent(aiPlayer, PositionComponent.class).get();
        PositionComponent targetPos = aiComponent.getTargetPosition();

        // the x and y displacements
        double diffX = targetPos.getX() - aiPos.getX();
        double diffY = targetPos.getY() - aiPos.getY();

        double angle = Math.toDegrees(getAngle(diffX, diffY, true));

        // set direction of shot
        weapon.setDirection(angle);
    }

    /**
     * Takes in the target/shooter, x & y differential and performs a string of mathematical computations required to
     * adjust shot power relative to wind speed
     *
     * @author Tony and Irsan
     *
     * @param world
     *            the world
     * @param diffX
     *            the x difference between the target and shooters weapon position
     * @param diffY
     *            the y difference between the target and shooters weapon position
     * @param angle
     *            the shot angle
     * @return A variable that will be applied to the initial shot power to accurately adjust for wind power & direction
     */
    public double calculateWindAffectedPower(World world, double diffX, double diffY, double angle) {
        // The cubic polynomial required to calculate shot time requires 3 constants a,b,d
        // Awaiting access to the wind component
        WindComponent wind = world.getComponents(WindComponent.class).get(0);
        double windAcceleration = wind.getStrength() * wind.getDirection();

        // approximate the time solution
        double time = shotTimeApproximation(windAcceleration, angle, diffX, diffY);
        double u = (diffY - 500 / 2 * Math.pow(time, 2)) / (time * Math.sin(angle));
        return u / 7.5;
    }

    /**
     * This method uses Newton's method to approximate the solution of the cubic equation fOfT = 0.
     * 
     * @author Irsan and Tony
     * 
     * @param wind
     *            is the wind strength * its direction
     * 
     * @param angle
     *            is the angle of the shot in radians
     * 
     * @param diffX
     *            s the X displacement between AI's target and the AI itself
     * 
     * @param diffY
     *            is the Y displacement between AI's target and the AI itself
     * 
     * @return the approximate time solution
     */
    private double shotTimeApproximation(double wind, double angle, double diffX, double diffY) {

        double nextT = 2; // first time solution guess
        double t; // time to be approximated
        double fOfT; // the polynomial whose root is to be approximated
        double fDashT; // the derivative of fOfT

        do {
            // the time in game tick unit
            t = nextT;

            // the function to be approximately solved
            fOfT = (-10 / 3) * wind * Math.tan(angle) * Math.pow(t, 3) + 250 * Math.pow(t, 2) + diffX * Math.tan(angle)
                    - diffY;

            // the derivative of the function above
            fDashT = -10 * wind * Math.tan(angle) * Math.pow(t, 2) + 500 * t;

            // Newton's method of finding the zero of a polynomial
            nextT = t - fOfT / fDashT;

            // repeat Newton's method until the difference between two successive time is less than 0.01 second
        } while (Math.abs(nextT - t) > 0.001 && nextT > 0);

        // return the approximated time solution
        if (nextT > 0) {
            return nextT;
        } else {
            return t;
        }
    }

    /**
     * This method calculates the distance between the AI player and the nearest obstacle (a gap, a collision block, or
     * an end of the terrain).
     * 
     * @author Irsan and Tony
     * 
     * @param direction
     *            the direction toward which the distance between the AI player and the obstacle is to be returned.
     * 
     * @param aiPlayer
     *            the AI player
     * 
     * @param world
     *            the game world
     * 
     * @return the available distance of travel before reaching the nearest obstacle
     */
    public double distanceToBoundary(Direction direction, Entity aiPlayer, World world) {

        double playerLeftEdge = world.getComponent(aiPlayer, PositionComponent.class).get().getX();
        double playerTopEdge = world.getComponent(aiPlayer, PositionComponent.class).get().getY();
        double playerWidth = world.getComponent(aiPlayer, RectangleComponent.class).get().getWidth();
        double playerHeight = world.getComponent(aiPlayer, RectangleComponent.class).get().getHeight();
        double playerRightEdge = playerLeftEdge + playerWidth;
        double playerBottomEdge = playerTopEdge + playerHeight;

        List<PositionComponent> tiles = getPossibleBoundaryTiles(direction, aiPlayer, world);
        sortTiles(tiles, direction);

        double distanceToBoundary;
        if ((distanceToBoundary = isEdgeCase(tiles, direction, aiPlayer, world)) > 0) {
            return distanceToBoundary;
        }

        int numberOfTiles = tiles.size();

        // find a too-wide gap, an edge, or a blocking tile and return the available distance to the obstacle
        for (int i = 0; i < numberOfTiles - 1; ++i) {

            double tileLeftEdge = tiles.get(i).getX();
            double tileWidth = world.getComponent(tiles.get(i).getEntity(), RectangleComponent.class).get().getWidth();
            double tileRightEdge = tileLeftEdge + tileWidth;

            RectangleComponent nextTileRect = world.getComponent(tiles.get(i + 1).getEntity(), RectangleComponent.class)
                    .get();
            double nextTopEdge = tiles.get(i + 1).getY();
            double nextBottomEdge = nextTopEdge + nextTileRect.getHeight();
            double nextLeftEdge = tiles.get(i + 1).getX();
            double nextWidth = nextTileRect.getWidth();
            double nextRightEdge = nextLeftEdge + nextWidth;

            double gap;

            if (direction == Direction.RIGHT) {
                // the gap between tile(i) and tile(i + 1); 0 if no gap
                gap = nextLeftEdge - tileRightEdge;

                // if the gap is too big to cross over, return the distance to the gap
                if (gap >= playerWidth) {
                    distanceToBoundary = tileRightEdge - playerLeftEdge;
                    break;

                }

                // if tile(i) is the first tile on the right such that if the AI moves towards it and the AI will
                // definitely collide with it, then return the available distance between AI and the tile
                if ((nextTopEdge < playerBottomEdge && nextTopEdge > playerTopEdge)
                        || (nextBottomEdge > playerTopEdge && nextBottomEdge < playerBottomEdge)) {
                    distanceToBoundary = nextLeftEdge - playerRightEdge;
                    break;
                }

                // if tile(i) is the last one and the above condition is false, then it must be the right-most tile
                // right at the edge of the terrain
                if (i == numberOfTiles - 2) {
                    distanceToBoundary = nextRightEdge - playerLeftEdge;
                }
            }

            if (direction == Direction.LEFT) {
                // the gap between tile(i) and tile(i + 1); 0 if no gap
                gap = tileLeftEdge - nextRightEdge;

                // if the gap is too big to cross over, return the distance to the gap
                if (gap >= playerWidth) {
                    distanceToBoundary = playerRightEdge - tileLeftEdge;
                    break;
                }

                // if tile(i) is the first tile on the left such that if the AI moves towards it and the AI will
                // definitely collide with it, then return the available distance between AI and the tile
                if ((nextTopEdge < playerBottomEdge && nextTopEdge > playerTopEdge)
                        || (nextBottomEdge > playerTopEdge && nextBottomEdge < playerBottomEdge)) {
                    distanceToBoundary = playerLeftEdge - nextRightEdge;
                    break;
                }

                // if tile(i) is the last one and the above condition is false, then it must be the left-most tile
                // right at the edge of the terrain
                if (i == numberOfTiles - 2) {
                    distanceToBoundary = playerRightEdge - nextLeftEdge;
                }
            }
        }

        return distanceToBoundary;
    }

    /**
     * This method returns the distance to the nearest obstacle in the given direction if the given list of tile
     * positions has only one element or the first tile in the list would collide with the AI player if it moved toward
     * it.
     * 
     * This method returns 9999 which is greater than any typical pixel resolution if the list of tile positions is
     * empty as is the case if the AI player is not standing on any tile at all.
     * 
     * All else, it returns 0 to indicate the list is not an edge case.
     * 
     * @author Irsan and Tony
     * 
     * @param tiles
     *            the list of tile positions composed by getPossibleBoundaryTiles() method
     * 
     * @param direction
     *            the direction toward which the nearest terrain obstacle is to be found
     * 
     * @param aiPlayer
     *            the AI player
     * 
     * @param world
     *            the game world
     * 
     * @return the distance to the nearest obstacle (an edge of a gap, a blocking tile, or an end of the terrain) or 0
     *         if the given tiles is not an edge case
     */
    private double isEdgeCase(List<PositionComponent> tiles, Direction direction, Entity aiPlayer, World world) {

        // if tiles is empty, then player is not standing on any tile and there is no close tiles in the vicinity
        if (tiles.isEmpty()) {
            return 9999;
        }

        RectangleComponent playerRect = world.getComponent(aiPlayer, RectangleComponent.class).get();
        double playerTopEdge = world.getComponent(aiPlayer, PositionComponent.class).get().getY();
        double playerBottomEdge = playerTopEdge + playerRect.getHeight();
        double playerLeftEdge = world.getComponent(aiPlayer, PositionComponent.class).get().getX();
        double playerRightEdge = playerLeftEdge + playerRect.getWidth();

        RectangleComponent tileRect = world.getComponent(tiles.get(0).getEntity(), RectangleComponent.class).get();
        double tileTopEdge = tiles.get(0).getY();
        double tileBottomEdge = tileTopEdge + tileRect.getHeight();
        double tileLeftEdge = tiles.get(0).getX();
        double tileRightEdge = tileLeftEdge + tileRect.getWidth();

        // if there is only one tile in the possible list of collidable tiles, then return the distance
        if (tiles.size() == 1) {
            if (direction == Direction.RIGHT && tileLeftEdge <= playerRightEdge) {
                return tileRightEdge - playerLeftEdge;
            } else if (direction == Direction.LEFT && playerLeftEdge <= tileRightEdge) {
                return playerRightEdge - tileLeftEdge;
            }
        }

        // requires this check since the calling method doesn't check the first tile
        if ((tileBottomEdge > playerTopEdge && tileBottomEdge < playerBottomEdge)
                || (tileTopEdge < playerBottomEdge && tileTopEdge > playerTopEdge)) {

            if (direction == Direction.RIGHT) {
                return tileLeftEdge - playerRightEdge;
            } else if (direction == Direction.LEFT) {
                return playerLeftEdge - tileRightEdge;
            }
        }

        // else, it is not an edge case
        return 0.0;
    }

    /**
     * This method sorts the given list of PositionComponent in ascending order if the given direction is RIGHT; else,
     * it sorts the list in descending order.
     * 
     * @author Irsan
     * 
     * @param boundaryTiles
     *            the tiles that are processed by getPossibleBoundaryTiles() method
     * @param direction
     *            the direction where the AI player is moving in
     */
    private void sortTiles(List<PositionComponent> boundaryTiles, Direction direction) {
        // sort ascending by X if direction == LEFT; else, sort descending
        boundaryTiles.sort(new Comparator<PositionComponent>() {
            @Override
            public int compare(PositionComponent pos1, PositionComponent pos2) {
                if (pos1.getX() < pos2.getX()) {
                    return direction == Direction.LEFT ? 1 : -1;
                } else if (pos1.getX() > pos2.getX()) {
                    return direction == Direction.LEFT ? -1 : 1;
                } else {
                    return 0;
                }
            }
        });
    }

    /**
     * @author Irsan
     * 
     * @param direction
     *            is the horizontal direction that the AI is moving in
     * 
     * @param aiPlayer
     *            the AI player Entity
     * 
     * @param world
     *            the world
     * 
     * @return a list of PositionComponent of tiles that are situated such that if the aiPlayer were to move toward any
     *         one of the tiles, it would definitely collide with it. This method returns an empty list if no such tiles
     *         exist, for example, when the AI is jumping / flying.
     */
    private List<PositionComponent> getPossibleBoundaryTiles(Direction direction, Entity aiPlayer, World world) {

        PositionComponent playerPos = world.getComponent(aiPlayer, PositionComponent.class).get();
        RectangleComponent playerRect = world.getComponent(aiPlayer, RectangleComponent.class).get();

        // the parameters of AI player's rectangle
        double playerTop = playerPos.getY();
        double playerBottom = playerTop + playerRect.getHeight();
        double playerLeft = playerPos.getX();
        double playerRight = playerLeft + playerRect.getWidth();

        // the list of tile positions to be returned
        List<PositionComponent> tiles = new ArrayList<>();

        // error limits in x and y axes
        final double errLimitY = 10;
        final double errLimitX = 10;

        // find all tiles that are not positioned lower or higher than the AI player in the given direction
        for (ComponentMap cm : world.getIterator(TileRenderComponent.class, RectangleComponent.class,
                PositionComponent.class, SpriteComponent.class)) {

            PositionComponent tilePos = cm.get(PositionComponent.class);
            RectangleComponent tileRect = cm.get(RectangleComponent.class);
            SpriteComponent tileSprite = cm.get(SpriteComponent.class);

            double tileTop = tilePos.getY();
            double tileBottom = tileTop + tileRect.getHeight();
            double tileLeft = tilePos.getX();
            double tileRight = tileLeft + tileRect.getWidth();

            boolean condition1 = (tileTop - errLimitY <= playerBottom && tileTop >= playerTop)
                    || (tileBottom + errLimitY >= playerTop && tileBottom <= playerBottom);

            boolean condition2 = (direction == Direction.LEFT && tileLeft - errLimitX <= playerRight)
                    || (direction == Direction.RIGHT && tileRight + errLimitX >= playerLeft);

            boolean condition3 = !tileSprite.getImage().contains(WATER);

            if (condition1 && condition2 && condition3) {
                tiles.add(tilePos);
            }
        }

        return tiles;
    }

    /**
     * @author Irsan Winarto
     *
     *         An enum class to define default acceleration in four directions
     */
    public enum Direction {
        UP(-1), DOWN(1), LEFT(-1), RIGHT(1), STAY(0);

        private int orientation;

        Direction(int orientation) {
            this.orientation = orientation;
        }

        public int getDirection() {
            return orientation;
        }
    }
}