package uq.deco2800.dangernoodles.prefabs;

import java.util.Optional;

import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.components.damages.InstantDamageComponent;
import uq.deco2800.dangernoodles.components.weapons.ExplosionComponent;
import uq.deco2800.dangernoodles.components.weapons.ProjectileComponent;
import uq.deco2800.dangernoodles.components.weapons.WeaponComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.weapons.ProjectileDefinition;

public final class ProjectileEntities {
    private static final double POWER_CONSTANT = 7.5;

    /**
     * Private constructor to hide the implicit public one.
     */
    private ProjectileEntities() {}

    /**
     * Creates a projectile from a cluster
     * 
     * @param world
     *            World that the entity will feature in
     * @param parentProjectile
     *            The original projectile that has just split into some other
     *            projectiles
     * @return The entity that was made
     */
    public static Entity createProjectile(World world,
            ProjectileComponent parentProjectile) {
        if (!parentProjectile.getDefinition().makesClusters()) {
            // Won't know what sort of projectile to create
            throw new IllegalArgumentException(
                    "You tried to shoot a gun that isn't a gun. Make sure it"
            + "makes projectiles");
        }
        ProjectileDefinition projectileDef = parentProjectile.getDefinition()
                .getClusterType();
        PositionComponent position = world.getComponent(
                parentProjectile.getEntity(), PositionComponent.class).get();
        return createProjectile(world, projectileDef, position.getX(),
                position.getY(), parentProjectile.getEntity());
    }

    /**
     * Creates a new projectile that has just been fired (i.e. from a weapon)
     * 
     * @param world
     *            World that the entity will feature in
     * @param parentWeapon
     *            The weapon that is firing the projectile
     * @return The projectile entity that was made
     */
    public static Entity createProjectile(World world,
            WeaponComponent parentWeapon) {
        if (!parentWeapon.getDefinition().makesProjectiles() || 
                parentWeapon.getDefinition().getProjectileType() == null) {
            // Won't know what sort of projectile to create
            throw new IllegalArgumentException(
                    "You tried to shoot a gun that isn't a gun. "
                    + "Make sure it makes projectiles");
        }
        ProjectileDefinition projectileDef = parentWeapon.getDefinition()
                .getProjectileType();
        PositionComponent position = world
                .getComponent(parentWeapon.getEntity(), PositionComponent.class)
                .get();
        
        double x;
        double y;
        double rotation;
        Entity parent;

        int width = parentWeapon.getDefinition().getWidth();
        rotation = parentWeapon.getDirection();
        x = position.getX() + Math.cos(Math.toRadians(rotation)) * width;
        y = position.getY() + Math.sin(Math.toRadians(rotation)) * width;
        parent = parentWeapon.getParent();
        
        if (Math.abs(rotation) > 90) {
            x -= width;
        }
        
        return createProjectile(world, projectileDef, x, y, parent);
    }

    /**
     * Creates a new projectile at an absolute position on the canvas
     * 
     * @param world
     *            World that the entity will feature in
     * @param projectileDef
     *            The definition of the projectile to create
     * @param x
     *            The x position at which to spawn the projectile
     * @param y
     *            The y position at which to spawn the projectile
     * @param parent
     *            The parent of the projectile
     * @return The entity that was made
     */
    public static Entity createProjectile(World world,
            ProjectileDefinition projectileDef, double x, double y, Entity parent) {
        Entity projectile = world.createEntity();
        addBasicProperties(projectile, x, y, projectileDef, parent, world);
        return projectile;
    }

    /**
     * Adds all the base components needed for a projectile
     * 
     * @param projectile
     * @param x
     * @param y
     * @param definition
     */
    private static void addBasicProperties(Entity projectile, double x,
            double y, ProjectileDefinition definition, Entity parent, World world) {
        int height;
        int width;
        double mass;
        height = definition.getHeight();
        width = definition.getWidth();
        mass = definition.getMass();
        projectile.addComponent(new ProjectileComponent(definition, parent))
                .addComponent(new MovementComponent(0, 0, 0, 0))
                .addComponent(new PositionComponent(x, y))
                .addComponent(new SpriteComponent(width, height,
                        definition.getSprite()))
                .addComponent(new MassComponent(mass))
                // Only give gravity if it's got mass
                .addComponent(new GravityComponent(
                        Math.abs(definition.getMass()) > 0.0001))
                .addComponent(new RectangleComponent(height, width));
        // only do damage if it doesn't explode
        if (!definition.explodes()) {
            projectile.addComponent(new InstantDamageComponent(
                    definition.getDamage()));
        }
        
        // should ignore collisions with cursor
        CursorComponent cursor = 
                world.getComponents(CursorComponent.class).get(0);
        CollisionComponent collision = new CollisionComponent(false,
                CollisionComponent.shape.RECTANGLE);
        collision.addIgnored(cursor.getEntity());
        projectile.addComponent(collision);
    }

    /**
     * Shoots a projectile
     * 
     * @param projectile
     *            projectile to shoot
     * @param world
     *            world to shoot the projectile in
     * @param power
     *            power to shoot the projectile with
     * @param direction
     *            direction to shoot the projectile in
     */
    public static void push(Entity projectile, World world, double power,
            double direction) {
        MovementComponent movement = world
                .getComponent(projectile, MovementComponent.class).get();
        double vx;
        double vy;
        
        // set initial rotation
        SpriteComponent sprite = world.getComponent(projectile, 
                SpriteComponent.class).get();
        sprite.setRotation(direction);
        
        vx = calculateVx(power, direction);
        vy = calculateVy(power, direction);
        movement.setVelocity(vx, vy);
    }
    
    /**
     * Calculates the x-component of the velocity when shooting a projectile
     * 
     * @param power
     *            the power the projectile is being shot with
     * @param direction
     *            the direction the projectile is aiming
     * @return the x-component of the projectile's velocity
     */
    public static double calculateVx(double power, double direction) {
        double finalPower = power;
        if (Math.abs(power) < 0.1) { // if power is zero, make it full power
            finalPower = 100;
        }
        return finalPower * Math.cos(Math.toRadians(direction)) * POWER_CONSTANT;
    }

    /**
     * Calculates the y-component of the velocity when shooting a projectile
     * 
     * @param power
     *            the power the projectile is being shot with
     * @param direction
     *            the direction the projectile is aiming
     * @return the y-component of the projectile's velocity
     */
    public static double calculateVy(double power, double direction) {
        double finalPower = power;
        if (Math.abs(power) < 0.1) { // if power is zero, make it full power
            finalPower = 100;
        }
        return finalPower * Math.sin(Math.toRadians(direction)) * POWER_CONSTANT;
    }
    
    /**
     * Shoots a projectile based on a given speed (rather than power)
     * 
     * @param projectile
     *            projectile to shoot
     * @param world
     *            world to shoot the projectile in
     * @param speed
     *            speed with which to shoot the projectile
     * @param direction
     *            direction to shoot the projectile in
     */
    public static void pushWithSpeed(Entity projectile, World world, 
            double speed, double direction) {
        MovementComponent movement = world
                .getComponent(projectile, MovementComponent.class).get();
        
        double vx;
        double vy;
        
        // get the vx and vy (remove power constant factor)
        vx = calculateVx(speed, direction) / POWER_CONSTANT;
        vy = calculateVy(speed, direction) / POWER_CONSTANT;
        
        movement.setVelocity(vx, vy);
    }
    
    /**
     * Creates an explosion in a given world based on the given projectile
     * 
     * @param world
     *            The world in which to create the explosion
     * @param projectile
     *            The projectile based on which to create the explosion
     * @return The entity representing the explosion that was created
     */
    public static Entity createExplosion(World world,
            ProjectileComponent projectile) {
        
        // Create the entity and get base data out first
        Entity explosion = world.createEntity();
        ProjectileDefinition pdef = projectile.getDefinition();
        int blastRadius = pdef.getBlastRadius();
        double damage = pdef.getDamage();
        
        // Check if the projectile has a position (it should - just in case)
        Optional<PositionComponent> positionOpt = world
                .getComponent(projectile.getEntity(), PositionComponent.class);
        
        if (!positionOpt.isPresent()) {
            return null; // don't make a big fuss about it
        }
        
        PositionComponent position = positionOpt.get();

        // Make sure the explosion has all the components it needs
        explosion.addComponent(new ExplosionComponent(blastRadius, damage))
                .addComponent(new MovementComponent(0,0,0,0))
                .addComponent(
                        new SpriteComponent(blastRadius * 2, blastRadius * 2,
                                "weapons/explosion_small.gif"))
                .addComponent(new CollisionComponent(true,
                        CollisionComponent.shape.RECTANGLE))
                .addComponent(new RectangleComponent(blastRadius * 2,
                        blastRadius * 2))
                .addComponent(new InstantDamageComponent(damage));
        PositionComponent pos = new PositionComponent(
                position.getX() - blastRadius + pdef.getWidth() / 2, 
                position.getY() - blastRadius + pdef.getHeight() / 2
                );
        pos.setNextX(position.getX() - blastRadius + pdef.getWidth() / 2);
        pos.setNextY(position.getY() - blastRadius + pdef.getHeight() / 2);
        explosion.addComponent(pos);
        
        return explosion;
    }
}
