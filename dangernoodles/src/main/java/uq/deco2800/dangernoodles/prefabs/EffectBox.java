package uq.deco2800.dangernoodles.prefabs;

import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.components.displays.EffectDisplayComponent;
import uq.deco2800.dangernoodles.components.effects.*;
import uq.deco2800.dangernoodles.components.stats.ManaComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

import java.util.ArrayList;
import java.util.Random;

import static uq.deco2800.dangernoodles.components.effects.EffectEnum.*;

/**
 * Created by Jason on 3/09/2016.
 * Modified by khoi_truong and Jayson.
 * <p>
 * This class is used to create an effect box in the world.
 */
public class EffectBox {
    // Private
    private static ArrayList<EffectComponent> effects = new ArrayList<>();

    /**
     * Private constructor to hide the implicit public one.
     */
    private EffectBox() {}

    public static void initialiseEffects() {
        // Damage
        effects.add(new DamageEffectComponent(DAMAGE_BUFF, 3, 300));
        effects.add(new DamageEffectComponent(DAMAGE_DEBUFF, 3, -1));
        // Speed
        effects.add(new SpeedEffectComponent(SPEED_BUFF, 3, 500));
        effects.add(new SpeedEffectComponent(SPEED_DEBUFF, 3, -300));
        // Shield
        effects.add(new ShieldEffectComponent(SHIELD_BUFF, 3, 5));
        effects.add(new ShieldEffectComponent(SHIELD_DEBUFF, 3, -5));
        // Invulnerable
        effects.add(new InvulnerableEffectComponent(INVULNERABLE_BUFF, 3,true));
        effects.add(new InvulnerableEffectComponent(INVULNERABLE_DEBUFF, 3, false));
        // Mana
        effects.add(new ManaEffectComponent(MANA_BUFF, 3, 100));
        effects.add(new ManaEffectComponent(MANA_DEBUFF, 3, -100));
    }

    /**
     * Create new effect box in the world.
     *
     * @param world
     *         Parent world object
     * @param x
     *         a double representing the x coordinate of this entity
     * @param y
     *         a double representing the y coordinate of this entity
     *
     * @return The created world entity
     *
     * @throws NullPointerException
     *         if world is null
     * @require world != null
     * @ensure an effect box that is added into given world and coordinates
     */
    public static Entity createBox(World world, double x, double y) {

        initialiseEffects();
        Random r = new Random();
        int randomNumber = (int) ( r.nextDouble() * effects.size());

        Entity buffBox = world.createEntity()
                .addComponent(new PositionComponent(x, y))
                .addComponent(new SpriteComponent(30, 30, "/BuffCrate/BuffCrate.png"))
                .addComponent(new InputComponent())
                .addComponent(new MassComponent(5.0))
                .addComponent(new GravityComponent(true))
                .addComponent(new RectangleComponent(30, 30))
                .addComponent(new EffectDisplayComponent())
                .addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
                .addComponent(new MovementComponent(0.0, 0.0, 1.0, 0.0))
                .addComponent(new ManaComponent(1))
                .addComponent(new NameComponent("Mystery Box"))
                .addComponent(effects.get(randomNumber));

        // Reset the list to eliminate the chance of overflowing
        effects.clear();

        return buffBox;
    }
}
