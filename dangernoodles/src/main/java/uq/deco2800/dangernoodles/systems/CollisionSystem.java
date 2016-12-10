package uq.deco2800.dangernoodles.systems;

import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.ecs.*;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.physics.QuadTree;
import uq.deco2800.dangernoodles.physics.Rectangle;
import uq.deco2800.dangernoodles.util.ObjectPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A collision System which is called every tick to detect collisions
 * between elements of the game.
 */
public class CollisionSystem implements System {
    private QuadTree quadTree;

    @Override
    public void run(World world, double t, double dt) {
        findCollisions(world);

        for (ComponentMap cm : getIter(world)) {

            Optional<PlayerComponent> thisPlayerOptional = world.getComponent
                    (cm.getEntity(), PlayerComponent.class);
            
            //if entity doesn't have a collision component or cm doesn't have
            // a movement component then skip this cm
            if (!world.getComponent(cm.getEntity(), CollisionComponent.class)
                    .isPresent() || !world.getComponent(cm.getEntity(),
                    MovementComponent.class).isPresent()) {
                continue;
            }
            CollisionComponent c = cm.get(CollisionComponent.class);
            //if cm isn't solid then skip
            if (!c.isSolid()) {
                continue;
            }
            if (c.isCollided()) {
                for (Entity e : c.getCollisions()) {

                    Optional<PlayerComponent> playerOptional = world.getComponent
                            (e, PlayerComponent.class);
                    PlayerComponent player;
                    if (playerOptional.isPresent() && thisPlayerOptional.isPresent()) {
                        player = playerOptional.get();
                        //Don't make players on the same team collide
                        if (player.getTeamId() == thisPlayerOptional.get().getTeamId()) {
                            continue;
                        }
                    }

                    CollisionComponent collision = world.getComponent(e,
                            CollisionComponent.class).get();
                    //if the entity we collided into isn't solid or it should
                    // be ignoring this CM then skip
                    if (!collision.isSolid() || collision.getIgnoredEntities
                            ().contains(cm.getEntity())) {
                        continue;
                    } else {
                        //find out the information about all of the collisions
                        processCollisions(world, cm, e);
                    }
                }

                //alter the new rectangle to be its new position based on the
                // collisions, a successful step up overrides other rebounds
                // and friction and position checks because they've already
                // been processed
                if (!c.isStepUp()) {
                    //remove the previous rectangle from quadtree
                    quadTree.remove(getRect(cm));
                    handleCollisions(world, cm, dt);
                    //insert the new position into the quadtree
                    quadTree.insert(getRect(cm));
                    //check if new position is now clear of collisions
                    if (checkClear(world, getRect(cm))) {
                        //if it isn't clear of collisions then apply special
                        // collision handling
                        quadTree.remove(getRect(cm));
                        handleCollisions2(world, cm);
                        quadTree.insert(getRect(cm));
                    }
                }
            }
        }
    }

    /**
     * This method will iterate through every collidable entity in the world and
     * add all collisions to their CollisionComponent
     * @param world
     */
    private void findCollisions(World world) {
        if (quadTree == null) {
            quadTree = new QuadTree(
                    0.0, 0.0,
                    world.getWidth(), world.getHeight(),
                    new ObjectPool<>(100, QuadTree.class)
            );
        }

        quadTree.clear();

        world.getComponents(CollisionComponent.class)
                .forEach(CollisionComponent::resetCollisions);

        Iterable<ComponentMap> iter = getIter(world);
        for (ComponentMap cm : iter) {
            if (!isCollidable(cm)) {
                continue;
            }

            quadTree.insert(getRect(cm));
        }

        ArrayList<Rectangle> ret = new ArrayList<>();
        for (ComponentMap cm : getIter(world)) {
            //don't worry about adding collisions to objects which can't
            // move, collisions will be handles for the ones which do move
            Optional<MovementComponent> om = world.getComponent(cm.getEntity(), MovementComponent.class);
            if (!isCollidable(cm) || !om.isPresent()) {
                continue;
            }

            ret.clear();
            Rectangle toCheck = getRect(cm);
            quadTree.getCollisions(ret, toCheck);
            for (Rectangle rect : ret) {
                if (!cm.getEntity().equals(rect.getEntity())) {
                    if (doesCollide(rect, toCheck)) {
                        // Rex: Check if these two rectangles are meant to be
                        // ignoring each other. Sorry for this.
                        CollisionComponent checkCollision = world.getComponent
                                (toCheck.getEntity(), CollisionComponent.class).get();
                        List<Entity> ignoredList = checkCollision
                                .getIgnoredEntities();

                        CollisionComponent c = cm.get(CollisionComponent.class);
                        c.addCollision(rect.getEntity());

                    }
                }
            }
        }
    }

    /**
     * This method will process the possible collisions each entity has, and
     * set its variables appropriately depending on what type of collision it
     * is actually experiencing
     * @param world
     * @param cm
     * @param e
     */
    private void collisionDirection(World world, ComponentMap cm, Entity e) {

        CollisionComponent thisCollision = cm.get(CollisionComponent.class);

        PositionComponent thisPosition = cm.get(PositionComponent.class);
        PositionComponent otherPosition = world.getComponent(e,
                PositionComponent.class).get();

        RectangleComponent thisRectangle = cm.get(RectangleComponent.class);
        RectangleComponent otherRectangle = world.getComponent(e,
                RectangleComponent.class).get();

        double otherTopY = otherPosition.getY();

        double otherLeftX = otherPosition.getX();

        Rectangle otherRect = new Rectangle(e, otherLeftX, otherTopY,
                otherRectangle.getWidth(), otherRectangle.getHeight());

        Rectangle thisRectX = new Rectangle(cm.getEntity(), thisPosition
                .getNextX(), thisPosition.getY(), thisRectangle.getWidth(),
                thisRectangle.getHeight());

        Rectangle thisRectY = new Rectangle(cm.getEntity(), thisPosition.getX
                (), thisPosition.getNextY(), thisRectangle.getWidth(),
                thisRectangle.getHeight());

        if (doesCollide(thisRectX, otherRect)) {
            //check for stepUp
            if (tryStep(world, cm, e)) {
                return;
            } else {
                //rebound X
                thisCollision.setReboundX(true);
            }
        }

        if (doesCollide(thisRectY, otherRect)) {
            //rebound Y
            thisCollision.setReboundY(true);
        }

        if (!thisCollision.isReboundX() && !thisCollision.isReboundY()) {
            if (doesCollide(getRect(cm), otherRect)) {
                thisCollision.setReboundCorner(true);
            }
        }
    }

    /**
     * This method uses two helper methods which process both the rebounds
     * and the friction of collisions
     * @param world
     * @param cm
     * @param e
     */
    public void processCollisions(World world, ComponentMap cm, Entity e) {
        //Find out which type of collision happened
        collisionDirection(world, cm, e);
        //process the friction of the collision
        processFriction(world, cm, e);
    }

    /**
     * This method will check if an entity can appropriately step above the
     * block it has rebounded with
     * @param world
     * @param cm
     * @param e
     * @return
     */
    private boolean tryStep(World world, ComponentMap cm, Entity e) {
        Optional<MovementComponent> om = world.getComponent(cm.getEntity(),
                MovementComponent.class);

        MovementComponent thisMovement = om.get();
        CollisionComponent thisCollision = cm.get(CollisionComponent.class);
        RectangleComponent thisRectangle = cm.get(RectangleComponent.class);
        PositionComponent thisPosition = cm.get(PositionComponent.class);
        PositionComponent otherPosition = world.getComponent(e,
                PositionComponent.class).get();
        RectangleComponent otherRectangle = world.getComponent(e,
                RectangleComponent.class).get();

        double thisBottom = thisPosition.getNextY() + thisRectangle.getHeight();
        double otherBottom = otherPosition.getY() + otherRectangle.getHeight();
        double otherMid = otherPosition.getY() + otherRectangle.getHeight()/2;

        Optional<PlayerComponent> op = world.getComponent(cm.getEntity(),
                PlayerComponent.class);
        Optional<AIComponent> oa = world.getComponent(cm.getEntity(),
                AIComponent.class);

        if (!op.isPresent() || oa.isPresent()) {
            return false;
        }

        if (thisBottom < otherMid || thisBottom > otherBottom + 10) {
            return false;
        }

        //If the jump is too large then don't try to step
        if (Math.abs(thisBottom - otherPosition.getY()) > 30) {
            return false;
        }

        //create a rectangle for the next position if the step goes through
        Rectangle nextPosition = new Rectangle(cm.getEntity(), thisPosition
                .getNextX(), otherPosition.getY() - thisRectangle.getHeight()
                - 1, thisRectangle.getWidth(), thisRectangle.getHeight());

        //if the next position is clear then update the entity to have that
        // next position
        if (!checkClear(world, nextPosition)) {
            //remove the old position from the quadTree
            quadTree.remove(getRect(cm));
            thisCollision.setStepUp(true);
            thisPosition.setNextY(otherPosition.getY() - thisRectangle.getHeight()
                    - 1);
            //insert the new Position to the QuadTree
            quadTree.insert(getRect(cm));
            thisMovement.setVx(thisMovement.getVX() * 0.5);
            thisMovement.setVy(thisMovement.getVY() * 0.5);
            return true;
        } else {
            return false;
        }
    }

    /**
     * This Method will process the friction associated with a collision and
     * set a CollisionComponents inner variable appropriately so facilitate it.
     * @param world
     * @param cm
     * @param e
     */
    private void processFriction(World world, ComponentMap cm, Entity e) {
        CollisionComponent c = cm.get(CollisionComponent.class);
        Optional<FrictionComponent> f = world.getComponent(e, FrictionComponent.class);
        if (f.isPresent()) {
            FrictionComponent friction = f.get();
            if (c.isReboundY()) {
                c.setFrictionHorizontal(true);
                //The friction experienced by the entity should be that of
                // the maximum friction level it experiences this tick
                if (c.getHorizontalFrictionValue() < friction.getFriction()) {
                    c.setHorizontalFrictionValue(friction.getFriction());
                }
            }
            if (c.isReboundX()) {
                c.setFrictionVertical(true);
                //The friction experienced by the entity should be that of
                // the maximum friction level it experiences this tick
                if (c.getVerticalFrictionValue() < friction.getFriction()) {
                    c.setVerticalFrictionValue(friction.getFriction());
                }
            }

        }
    }

    /**
     * This method will alterr an entities movement and velocity based on all
     * of the collisions it has experienced this tick.
     * @param world
     * @param cm
     * @param dt
     */
    private void handleCollisions(World world, ComponentMap cm, double dt) {

        Optional<MovementComponent> om = world.getComponent(cm.getEntity(), MovementComponent.class);
        MovementComponent movement = om.get();
        Optional<PositionComponent> op = world.getComponent(cm.getEntity(),
                PositionComponent.class);
        PositionComponent position = op.get();
        CollisionComponent collision = world.getComponent(cm.getEntity(),
                CollisionComponent.class).get();

        //Handle Friction
        movement.setVy(movement.getVY() - (movement.getVY() * collision
                .getVerticalFrictionValue() * (dt / 0.016)));

        movement.setVx(movement.getVX() - (movement.getVX() * collision
                .getHorizontalFrictionValue() * (dt / 0.016)));

        //Handle Rebounds
        if (collision.isReboundY()) {
            position.setNextY(position.getY() - 0.1);
            movement.setVy(movement.getVY() * - 0.4);
        }

        if (collision.isReboundX()) {
            position.setNextX(position.getX());
            movement.setVx(movement.getVX() * -0.4);
        }

        if (collision.isReboundCorner() && !collision.isReboundX() &&
                !collision.isReboundY()) {
            position.setNextY(position.getY());
            movement.setVy(Math.abs(movement.getVY()) * -0.4);
            position.setNextX(position.getX());
            movement.setVx(Math.abs(movement.getVX()) * -0.4);
        }
    }

    /**
     * This method checks whether an entity is currently colliding with any
     * other entities, it returns true if so.
     * @param world
     * @param nextPos
     * @return
     */
    private boolean checkClear(World world, Rectangle nextPos) {
        ArrayList<Rectangle> ret = new ArrayList<>();
        quadTree.getCollisions(ret, nextPos);
        boolean collision = false;

        Optional<PlayerComponent> thisPlayer = world.getComponent(nextPos
                .getEntity(), PlayerComponent.class);

        for (Rectangle rect : ret) {
            if (!nextPos.getEntity().equals(rect.getEntity())) {
                if (doesCollide(rect, nextPos)) {
                    // Rex: Check if these two rectangles are meant to be
                    // ignoring each other. Sorry for this.
                    CollisionComponent checkCollision = world.getComponent
                            (nextPos.getEntity(), CollisionComponent.class).get();
                    List<Entity> ignoredList = checkCollision
                            .getIgnoredEntities();
                    CollisionComponent checkCollision2 = world.getComponent
                            (rect.getEntity(), CollisionComponent.class).get();
                    List<Entity> ignoredList2 = checkCollision2
                            .getIgnoredEntities();

                    Optional<PlayerComponent> otherPlayer = world.getComponent
                            (rect.getEntity(), PlayerComponent.class);

                    boolean sameTeam = false;

                    if (thisPlayer.isPresent() && otherPlayer.isPresent()) {
                        if (thisPlayer.get().getTeamId() == otherPlayer.get()
                                .getTeamId()) {
                            sameTeam = true;
                        }
                    }

                    // If not, proceeding on making the collision happen
                    if (!ignoredList.contains(rect.getEntity()) &&
                            !ignoredList2.contains(nextPos.getEntity()) && !sameTeam) {
                        collision = true;
                    }
                }
            }
        }
        return collision;
    }

    /**
     * This  method is called if the previous collision handler has still
     * left the entity within a collision, this method resets the entity back
     * to its prior position with zero velocity
     * @param world
     * @param cm
     */
    private void handleCollisions2(World world, ComponentMap cm) {
        //Reset the entity's position back to where it was before it tried to
        // move into a collision, reset velocity to zero
        PositionComponent thisPosition = cm.get(PositionComponent.class);
        Optional<MovementComponent> om = world.getComponent(cm.getEntity(),
                MovementComponent.class);

        if (!om.isPresent()) {
            return;
        }
        MovementComponent thisMovement = om.get();
        thisMovement.setVx(0);
        thisMovement.setVy(0);
        thisPosition.setNextX(thisPosition.getX());
        thisPosition.setNextY(thisPosition.getY());
    }

    /**
     * Takes a component map and returns a rectangle
     *
     * @param cm The component map which must have a Postion + Rectangle
     *           Component
     * @return A new rectangle
     */
    private Rectangle getRect(ComponentMap cm) {
        PositionComponent pos = cm.get(PositionComponent.class);
        RectangleComponent rec = cm.get(RectangleComponent.class);
        return new Rectangle(
                cm.getEntity(),
                pos.getNextX(),
                pos.getNextY(),
                rec.getWidth(),
                rec.getHeight()
        );
    }

    /**
     * Checks if two rectangles collide
     *
     * @param rect1 The first rectangle
     * @param rect2 The second rectangle
     * @return Whether the to rectangles collided
     */
    private boolean doesCollide(Rectangle rect1, Rectangle rect2) {

        boolean overlapX = ((rect1.getX() <= rect2.getX()) && ((rect1.getX()
                + rect1.getWidth()) >= rect2.getX()))
                ||
                ((rect2.getX() <= rect1.getX()) && ((rect2.getX() +
                        rect2.getWidth()) >= rect1.getX()));

        boolean overlapY = ((rect1.getY() <= rect2.getY()) && (rect1.getY()
                + rect1.getHeight()) >= rect2.getY())
                ||
                (rect2.getY() <= rect1.getY() && (rect2.getY() +
                        rect2.getHeight() >= rect1.getY()));

        return overlapX && overlapY;
    }

    /**
     * Returns an iterator commonly used in the system consisting of
     * Collision, Position, and Rectangle components
     *
     * @param world The world from which to build it
     * @return The iterator
     */
    private Iterable<ComponentMap> getIter(World world) {
        return world.getIterator(
                CollisionComponent.class,
                PositionComponent.class,
                RectangleComponent.class
        );
    }

    /**
     * Checks if a componentmap can be collided with
     *
     * @param cm The component map
     * @return If it can be collided with
     */
    private boolean isCollidable(ComponentMap cm) {
        CollisionComponent c = cm.get(CollisionComponent.class);
        return c.isCollidable();
    }
}
