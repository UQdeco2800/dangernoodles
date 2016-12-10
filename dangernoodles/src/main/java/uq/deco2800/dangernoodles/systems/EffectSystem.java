package uq.deco2800.dangernoodles.systems;

import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.components.effects.*;
import uq.deco2800.dangernoodles.components.stats.*;
import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;

import java.util.HashMap;

/**
 * Created by khoi_truong on 2016/08/30.
 * <p>
 * This class is used to handle effects that an entity has.
 */
public class EffectSystem implements System {

    // These hash maps are used to prevent continuous effect application
    // during one turn.
    private HashMap<Entity, Boolean> appliedSpeed = new HashMap<>();
    private HashMap<Entity, Boolean> appliedWeatherSpeed = new HashMap<>();
    private HashMap<Entity, Boolean> appliedShield = new HashMap<>();
    private HashMap<Entity, Boolean> appliedDamage = new HashMap<>();
    private HashMap<Entity, Boolean> appliedMana = new HashMap<>();
    private HashMap<Entity, Boolean> appliedInvulnerable = new HashMap<>();

    // These hash maps are used to prevent continuous effect application over
    // multiple turns
    private HashMap<Entity, Boolean> appliedSpeedOnce = new HashMap<>();
    private HashMap<Entity, Boolean> appliedWeatherSpeedOnce = new HashMap<>();
    private HashMap<Entity, Boolean> appliedShieldOnce = new HashMap<>();
    private HashMap<Entity, Boolean> appliedDamageOnce = new HashMap<>();
    private HashMap<Entity, Boolean> appliedManaOnce = new HashMap<>();
    private HashMap<Entity, Boolean> getAppliedInvulnerableOnce = new HashMap<>();

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

        for (ComponentMap cm : world.getIterator(
                SpeedComponent.class,
                SpeedEffectComponent.class,
                TurnComponent.class)) {
            Entity entity = cm.getEntity();
            if (!appliedSpeed.containsKey(entity)) {
                appliedSpeed.put(entity, false);
                appliedSpeedOnce.put(entity, false);
            }
            SpeedComponent speed = cm.get(SpeedComponent.class);
            SpeedEffectComponent effect = cm.get(SpeedEffectComponent.class);
            TurnComponent turn = cm.get(TurnComponent.class);
            checkSpeed(world, speed, effect, turn, false);
        }

        for (ComponentMap cm : world.getIterator(
                SpeedComponent.class,
                WeatherSpeedEffectComponent.class,
                TurnComponent.class)) {
            Entity entity = cm.getEntity();
            if (!appliedWeatherSpeedOnce.containsKey(entity)) {
                appliedWeatherSpeedOnce.put(entity, false);
            }
            SpeedComponent speed = cm.get(SpeedComponent.class);
            WeatherSpeedEffectComponent effect = cm.get(WeatherSpeedEffectComponent.class);
            TurnComponent turn = cm.get(TurnComponent.class);
            checkSpeed(world, speed, effect, turn, true);
        }

        for (ComponentMap cm : world.getIterator(
                ShieldComponent.class,
                ShieldEffectComponent.class,
                TurnComponent.class)) {
            Entity entity = cm.getEntity();
            if (!appliedShield.containsKey(entity)) {
                appliedShield.put(entity, false);
                appliedShieldOnce.put(entity, false);
            }
            ShieldComponent shield = cm.get(ShieldComponent.class);
            ShieldEffectComponent effect = cm.get(ShieldEffectComponent.class);
            TurnComponent turn = cm.get(TurnComponent.class);
            checkShield(world, shield, effect, turn);
        }

        for (ComponentMap cm : world.getIterator(
                ManaComponent.class,
                ManaEffectComponent.class,
                TurnComponent.class)) {
            Entity entity = cm.getEntity();
            if (!appliedMana.containsKey(entity)) {
                appliedMana.put(entity, false);
                appliedManaOnce.put(entity, false);
            }
            ManaComponent mana = cm.get(ManaComponent.class);
            ManaEffectComponent effect = cm.get(ManaEffectComponent.class);
            TurnComponent turn = cm.get(TurnComponent.class);
            checkMana(world, mana, effect, turn);
        }

        for (ComponentMap cm : world.getIterator(
                InvulnerableComponent.class,
                InvulnerableEffectComponent.class,
                TurnComponent.class)) {
            Entity entity = cm.getEntity();
            if (!appliedInvulnerable.containsKey(entity)) {
                appliedInvulnerable.put(entity, false);
                getAppliedInvulnerableOnce.put(entity, false);
            }
            InvulnerableComponent invulnerable = cm.get(InvulnerableComponent.class);
            InvulnerableEffectComponent effect = cm.get(InvulnerableEffectComponent.class);
            TurnComponent turn = cm.get(TurnComponent.class);
            checkInvulnerable(world, invulnerable, effect, turn);
        }

        for (ComponentMap cm : world.getIterator(
                DamageComponent.class,
                DamageEffectComponent.class,
                TurnComponent.class)) {
            Entity entity = cm.getEntity();
            if (!appliedDamage.containsKey(entity)) {
                appliedDamage.put(entity, false);
                appliedDamageOnce.put(entity, false);
            }
            DamageComponent damage = cm.get(DamageComponent.class);
            DamageEffectComponent effect = cm.get(DamageEffectComponent.class);
            TurnComponent turn = cm.get(TurnComponent.class);
            checkDamage(world, damage, effect, turn);
        }
    }

    /**
     * This method checks and ensure that given effect component will be
     * applied to the correct component.
     *
     * @param speed
     *         a speed component to be altered
     * @param effect
     *         a speed effect to alter
     * @param turn
     *         a turn component to check for expiration
     * @param isWeather
     *         if the effect passed in is a weather effect component
     *
     * @throws NullPointerException
     *         if speed, effect or turn is null
     * @require speed != null && effect != null && turn != null
     * @ensure given effect component will be applied to the correct component
     */
    @SuppressWarnings("unchecked")
    private void checkSpeed(World world,
                            SpeedComponent speed,
                            SpeedEffectComponent effect,
                            TurnComponent turn,
                            boolean isWeather) {

        Entity entity = speed.getEntity();

        // If the current noodles' dead, simply ignore it.
        if (entity != null) {

            double currentSpeed = speed.getSpeed();
            double newSpeed = currentSpeed + effect.getSpeed();
            if (newSpeed < 0) {
                newSpeed = 0;
            }

            int effectDuration = effect.getTurnDuration();

            // Check if it's noodle's turn
            if (turn.getTurn()) {
                if (isWeather) {
                    // If this is the weather speed effect, and it's snowing,
                    // simply set speed to 0.
                    WeatherSpeedEffectComponent weatherEffect = (WeatherSpeedEffectComponent) effect;
                    if (weatherEffect.getName() == EffectEnum.FROZEN_NOODLE_DEBUFF) {
                        speed.setSpeed(0);
                    } else {
                        // Check if the noodle has been applied with the effect for this
                        // turn
                        if (!appliedWeatherSpeedOnce.get(entity)) {
                            speed.setSpeed(newSpeed);
                            appliedWeatherSpeedOnce.put(entity, true);
                        }
                    }
                } else {
                    // Check if the noodle has been applied with the effect for this
                    // turn
                    if (!appliedSpeed.get(entity)) {
                        // Check if the effect is still available
                        if (effectDuration > 0) {
                            effect.setTurnDuration(--effectDuration);
                            if (!appliedSpeedOnce.isEmpty() && !appliedSpeedOnce.get(entity)) {
                                speed.setSpeed(newSpeed);
                                appliedSpeedOnce.put(entity, true);
                            }
                            appliedSpeed.put(entity, true);
                        } else {
                            speed.resetDefault();
                            appliedSpeed.remove(entity);
                            appliedSpeedOnce.remove(entity);
                            world.removeComponent(entity, SpeedEffectComponent.class);
                        }
                    }
                }
            } else {
                appliedSpeed.put(entity, false);
                appliedWeatherSpeedOnce.put(entity, false);
            }
        }
    }

    /**
     * This method checks and ensure that given effect component will be
     * applied to the correct component.
     *
     * @param shield
     *         a shield component to be altered
     * @param effect
     *         a shield effect to alter
     * @param turn
     *         a turn component to check for expiration
     *
     * @throws NullPointerException
     *         if shield, effect or turn is null
     * @require shield != null && effect != null && turn != null
     * @ensure given effect component will be applied to the correct component
     */
    @SuppressWarnings("unchecked")
    private void checkShield(World world,
                             ShieldComponent shield,
                             ShieldEffectComponent effect,
                             TurnComponent turn) {

        Entity entity = shield.getEntity();

        // If the current noodles' dead, simply ignore it.
        if (entity != null) {

            double currentShield = shield.getShield();
            double newShield = currentShield + effect.getShield();
            int effectDuration = effect.getTurnDuration();

            // Check if it's noodle's turn
            if (turn.getTurn()) {
                // Check if the noodle has been applied with the effect for this
                // turn
                if (!appliedShield.get(entity)) {
                    // Check if the effect is still available
                    if (effectDuration > 0) {
                        effect.setTurnDuration(--effectDuration);
                        if (!appliedShieldOnce.get(entity)) {
                            shield.setShield(newShield);
                            appliedShieldOnce.put(entity, true);
                        }
                        appliedShield.put(entity, true);
                    } else {
                        shield.resetDefault();
                        appliedShield.remove(entity);
                        appliedShieldOnce.remove(entity);
                        world.removeComponent(entity, ShieldEffectComponent.class);
                    }

                }
            } else {
                appliedShield.put(entity, false);
            }
        }
    }

    /**
     * This method checks and ensure that given effect component will be
     * applied to the correct component.
     *
     * @param mana
     *         a mana component to be altered
     * @param effect
     *         a mana effect to alter
     * @param turn
     *         a turn component to check for expiration
     *
     * @throws NullPointerException
     *         if mana, effect or turn is null
     * @require mana != null && effect != null && turn != null
     * @ensure given effect component will be applied to the correct component
     */
    @SuppressWarnings("unchecked")
    private void checkMana(World world,
                           ManaComponent mana,
                           ManaEffectComponent effect,
                           TurnComponent turn) {

        Entity entity = mana.getEntity();

        // If the current noodles' dead, simply ignore it.
        if (entity != null) {

            int currentMana = mana.getMana();
            double newMana = currentMana + effect.getMana();
            if (newMana < 0) {
                newMana = 0;
            }
            int effectDuration = effect.getTurnDuration();

            // Check if it's noodle's turn
            if (turn.getTurn()) {
                // Check if the noodle has been applied with the effect for this
                // turn
                if (!appliedMana.get(entity)) {
                    // Check if the effect is still available
                    if (effectDuration > 0) {
                        effect.setTurnDuration(--effectDuration);
                        if (!appliedManaOnce.get(entity)) {
                            mana.setMana((int) newMana);
                            appliedManaOnce.put(entity, true);
                        }
                        appliedMana.put(entity, true);
                    } else {
                        mana.resetDefault();
                        appliedMana.remove(entity);
                        world.removeComponent(entity, ManaEffectComponent.class);
                    }
                }
            } else {
                appliedMana.put(entity, false);
            }
        }
    }

    /**
     * This method checks and ensure that given effect component will be
     * applied to the correct component.
     *
     * @param invulnerable
     *         a invulnerable component to be altered
     * @param effect
     *         a invulnerable effect to alter
     * @param turn
     *         a turn component to check for expiration
     *
     * @throws NullPointerException
     *         if invulnerable, effect or turn is null
     * @require invulnerable != null && effect != null && turn != null
     * @ensure given effect component will be applied to the correct component
     */
    @SuppressWarnings("unchecked")
    private void checkInvulnerable(World world,
                                   InvulnerableComponent invulnerable,
                                   InvulnerableEffectComponent effect,
                                   TurnComponent turn) {

        Entity entity = invulnerable.getEntity();

        // If the current noodles' dead, simply ignore it.
        if (entity != null) {

            int effectDuration = effect.getTurnDuration();

            // Check if it's noodle's turn
            if (turn.getTurn()) {
                // Check if the noodle has been applied with the effect for this
                // turn
                if (!appliedInvulnerable.get(entity)) {
                    // Check if the effect is still available
                    if (effectDuration > 0) {
                        effect.setTurnDuration(--effectDuration);
                        invulnerable.setInvulnerable(effect.isInvulnerable());
                        appliedInvulnerable.put(entity, true);
                    } else {
                        invulnerable.resetDefault();
                        appliedInvulnerable.remove(entity);
                        getAppliedInvulnerableOnce.remove(entity);
                        world.removeComponent(entity, InvulnerableEffectComponent.class);
                    }
                }
            } else {
                appliedInvulnerable.put(entity, false);
            }
        }
    }

    /**
     * This method checks and ensure that given effect component will be
     * applied to the correct component.
     *
     * @param damage
     *         a damage component to be altered
     * @param effect
     *         a speed effect to alter
     * @param turn
     *         a turn component to check for expiration
     *
     * @throws NullPointerException
     *         if damage, effect or turn is null
     * @require damage != null && effect != null && turn != null
     * @ensure given effect component will be applied to the correct component
     */
    @SuppressWarnings("unchecked")
    private void checkDamage(World world,
                             DamageComponent damage,
                             DamageEffectComponent effect,
                             TurnComponent turn) {

        Entity entity = damage.getEntity();

        // If the current noodles' dead, simply ignore it.
        if (entity != null) {

            double currentDamage = damage.getDamage();

            double newDamage = currentDamage + effect.getDamage();
            if (newDamage < 0) {
                newDamage = 0;
            }
            int effectDuration = effect.getTurnDuration();

            // Check if it's noodle's turn
            if (turn.getTurn()) {
                // Check if the noodle has been applied with the effect for this
                // turn
                if (!appliedDamage.get(entity)) {
                    // Check if the effect is still available
                    if (effectDuration > 0) {
                        effect.setTurnDuration(--effectDuration);
                        if (!appliedDamageOnce.get(entity)) {
                            damage.setDamage(newDamage);
                            appliedDamageOnce.put(entity, true);
                        }
                        appliedDamage.put(entity, true);
                    } else {
                        damage.resetDefault();
                        appliedDamage.remove(entity);
                        appliedDamageOnce.remove(entity);
                        world.removeComponent(entity, DamageEffectComponent.class);
                    }
                }
            } else {
                appliedDamage.put(entity, false);
            }
        }
    }
}
