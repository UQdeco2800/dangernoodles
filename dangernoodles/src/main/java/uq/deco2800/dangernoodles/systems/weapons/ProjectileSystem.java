package uq.deco2800.dangernoodles.systems.weapons;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uq.deco2800.dangernoodles.components.CollisionComponent;
import uq.deco2800.dangernoodles.components.MovementComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.components.weapons.ProjectileComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.ProjectileEntities;
import uq.deco2800.dangernoodles.weapons.ProjectileDefinition;

public class ProjectileSystem implements System {
    private World world;
    private static String thisClass = ProjectileSystem.class.getName();
    private final Logger logger = LoggerFactory.getLogger(thisClass);

    @Override
    public void run(World world, double t, double dt) {
        this.world = world;
        List<ProjectileComponent> projectiles = this.world
                .getComponents(ProjectileComponent.class);
        Deque<Entity> destroyEntities = new ArrayDeque<Entity>();
        
        if (projectiles == null) {
            return;
        }
        
        //copy the list
        projectiles = new ArrayList<ProjectileComponent>(projectiles);
        
        for (ProjectileComponent projectile : projectiles) {
            // check if current projectile should be destroyed
            if (projectile.shouldDestroy()) {
                destroyEntities.addFirst(projectile.getEntity());
                continue; // skip to next
            }
            projectile.decrementTicks(dt); // decrement the time this has alive
            // check if it's time is up
            if (projectile.timedOut()) {
                checkClusters(projectile, world);
                continue;
            }
            
            // Get all the components wee need as optionals
            Optional<CollisionComponent> collisionOpt = world.getComponent(
                    projectile.getEntity(), CollisionComponent.class);
            Optional<PositionComponent> positionOpt = world.getComponent(
                    projectile.getEntity(), PositionComponent.class);
            Optional<MovementComponent> movementOpt = world.getComponent(
                    projectile.getEntity(), MovementComponent.class);
            Optional<SpriteComponent> spriteOpt = world.getComponent(
                    projectile.getEntity(), SpriteComponent.class);
            if (!collisionOpt.isPresent() || !positionOpt.isPresent() || 
                    !movementOpt.isPresent() || !spriteOpt.isPresent()) {

                logger.info("Projectile didn't have all of " +
                "required components");
                continue;
            }
            CollisionComponent collision = collisionOpt.get();
            PositionComponent position = positionOpt.get();
            MovementComponent movement = movementOpt.get();
            SpriteComponent sprite = spriteOpt.get();
            
            // rotate the sprite 
            double rotation = Math.toDegrees(
                    Math.atan2(movement.getVY(), movement.getVX()));
            sprite.setRotation(rotation);
            
            // If the project doesn't think it's cleared its parent yet it
            // doesn't currently collide with its parent then fix that
            if (!projectile.hasClearedParent() && !collision.getCollisions()
                    .contains(projectile.getParent())) {
                collision.removeIgnored(projectile.getParent());
                projectile.clearedParent();
            }
            
            // Check if the projectile has collided with something which it
            // isn't ignoring, if so then explode the projectile
            if (collision.isCollided()) {
                explode(projectile, world);
            }
            
            // This is a temporary fix to get rid of old projectiles
            // Until we have a skybox or something
            if (position.getY() > 1E4) {
                destroyEntities.addFirst(projectile.getEntity());
            }
        }
        
        //destroy all the entities in the stack
        while (!destroyEntities.isEmpty()) {
            world.destroyEntity(destroyEntities.removeFirst());
        }
    }

    /**
     * Returns a random direction between -180 and 180 degrees
     * 
     * @return a random angle between 
     */
    public static double createRandomDirection() {
        double direction = Math.random();
        
        direction = -180 + 360 * direction;

        return direction;
    }
    
    /**
     * Wraps an angle (in degrees) to equivalent angle between -180 and 180
     * 
     * @param angle
     *            angle to perform wrap on
     * @return The equivalent angle between -180 and 180
     */
    public static double wrapAngle(double angle) {
        double newAngle = angle;
        // check for multiple rotations first
        int rotations = newAngle > 0 ? (int) Math.floor(newAngle / 360) : 
                (int) Math.ceil(newAngle / 360);
        if (rotations >= 1) {
            newAngle -= rotations * 360;
        } else if (rotations <= -1) {
            newAngle -= rotations * 360;
        }
        
        // should now be between -360 and 360
        if (newAngle > 180) {
            newAngle -= 360;
        } else if (newAngle < -180) {
            newAngle += 360;
        }
        return newAngle;
    }
    
    /**
     * Checks if the projectile should cluster. If it should, it clusters it,
     * else it makes it explode
     * 
     * @param projectile
     *            The ProjectileComponet associated with the projectile to check
     */
    public static void checkClusters(ProjectileComponent projectile, World world) {
        ProjectileDefinition pdef = projectile.getDefinition();
        if (pdef.makesClusters()) {
            projectile.destroyNextRun(); // should destroy it next run
            
            // Get the directions of the clusters
            double direction = createRandomDirection();
            int numOfClusters = pdef.getNumOfClusters();
            double anglePerCluster = 360.0 / numOfClusters;
            
            // Get the parent's speed
            double parentSpeed = getParentSpeed(projectile, world);
            
            // Get the speed of the clusters
            double speed = getClusteredSpeed(numOfClusters, parentSpeed);
            
            Entity cluster; // store the entity that's created
            List<Entity> allClusters = new ArrayList<Entity>();
            
            for (int i = 0; i < numOfClusters; i++) {
                // Create the projectile
                cluster = ProjectileEntities.createProjectile(
                        world, projectile);
                // push it in the correct direction
                ProjectileEntities.pushWithSpeed(cluster, world, speed, 
                        direction);
                // "increment" the direction
                direction = wrapAngle(direction + anglePerCluster);
                allClusters.add(cluster);
            }
            
            // Make the clusters all ignore each other and their parent
            addIgnoreCollisionsToClusters(allClusters, projectile.getEntity(), 
                    world);
        } else {
            // doesn't cluster so explode it
            explode(projectile, world);
        }
    }
    
    /**
     * Adds parent and all other clusters to the ignored collisions list of each
     * cluster in list
     * 
     * @param clusters
     *            List of clusters to process
     * @param parent
     *            ProjectileComponent associated to the parent of the clusters
     */
    public static void addIgnoreCollisionsToClusters(List<Entity> clusters,
            Entity parent, World world) {
        // Do it for all of the clusters
        for (int thisCluster = 0; thisCluster < clusters
                .size(); thisCluster++) {
            // Get cluster entity and collision component
            Entity cluster = clusters.get(thisCluster);
            CollisionComponent collision = world.getComponent(cluster, 
                    CollisionComponent.class).get();
            // Add parent to ignored list
            collision.addIgnored(parent);
            // add all other clusters to ignored
            for (int otherCluster = 0; otherCluster < clusters
                    .size(); otherCluster++) {
                // Check if this is the cluster we're doing
                if (otherCluster == thisCluster) {
                    continue; // skip it
                }
                collision.addIgnored(clusters.get(otherCluster));
            }
        }
    }

    /**
     * Gets the speed of the given projectile
     * 
     * @param projectile
     *            The ProjectileComponent associated with the projectile to
     *            check the speed of
     * @param world
     *            The world in which the projectile lives
     * @return The velocity (as a double) of the projectile
     */
    public static double getParentSpeed(ProjectileComponent projectile, World world) {
        // Get current the speed of the projectile
        MovementComponent movement = world.getComponent(
                projectile.getEntity(), MovementComponent.class).get();
        
        double vx = movement.getVX();
        double vy = movement.getVY();
        
        return Math.sqrt(vx * vx + vy * vy);
    }

    /**
     * Gets a speed for clusters given the number of clusters NOTE: Returns a
     * speed proportional to parentSpeed and f(n) where
     * f(n) = 1/6 + 5/(6 * n ^ (2/3)) This gives a curve that approaches 1/6, 
     * with f(1) = 1 and f(4) ~= 0.5 The idea is that the projectiles get a 
     * little slower as there are more clusters
     * 
     * @require numOfClusters > 0 && parentSpeed > 0
     * 
     * @param numOfClusters
     *            The number of clusters to be created
     * @param parentSpeed
     *            The speed of the projectile splitting into the clusters
     * @return The speed at which the new clusters should be pushed
     */
    public static double getClusteredSpeed(int numOfClusters,
            double parentSpeed) {
        return parentSpeed * (1.0 / 6.0
                + 5.0 / (6.0 * Math.pow((double) numOfClusters, 2.0 / 3.0)));
    }

    /**
     * Creates an explosion at the projectile and kills the projectile
     * Called when a projectile has timed out.
     * 
     * @param projectile
     */
    public static void explode(ProjectileComponent projectile, World world) {
        projectile.destroyNextRun(); // flag it to be destroyed next run
        
        // do explosion only if this projectile should explode
        ProjectileDefinition pdef = projectile.getDefinition();
        if (pdef.explodes()) {
            ProjectileEntities.createExplosion(world, projectile);
        }
    }

}
