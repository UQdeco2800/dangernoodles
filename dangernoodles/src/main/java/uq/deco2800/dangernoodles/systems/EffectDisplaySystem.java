package uq.deco2800.dangernoodles.systems;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.components.effects.*;
import uq.deco2800.dangernoodles.components.stats.HealthComponent;
import uq.deco2800.dangernoodles.components.stats.ManaComponent;
import uq.deco2800.dangernoodles.ecs.*;
import uq.deco2800.dangernoodles.ecs.System;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by khoi_truong on 31/08/2016.
 * <p>
 * This class is used to handle the displaying of effects inside the game.
 */
public class EffectDisplaySystem implements System {

    private StaticFrameHandler handler;

    /**
     * Creates a system that controls the mechanics behind displaying a profile
     * of noodle statistics.
     *
     * @param handler
     *         Frame handler to render static objects onto the canvas.
     */
    public EffectDisplaySystem(StaticFrameHandler handler) {
        this.handler = handler;
    }

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
        String defaultString = "default";
        List<StatusBarComponent> bars = world.getComponents(StatusBarComponent.class);
        for (ComponentMap cm : world.getIterator(
                PlayerComponent.class,
                SpriteComponent.class,
                NameComponent.class,
                ManaComponent.class,
                TurnComponent.class,
                HealthComponent.class)) {

            Entity entity = cm.getEntity();

            SpriteComponent sprite = cm.get(SpriteComponent.class);
            NameComponent name = cm.get(NameComponent.class);
            ManaComponent mana = cm.get(ManaComponent.class);
            TurnComponent turn = cm.get(TurnComponent.class);
            HealthComponent health = cm.get(HealthComponent.class);

            ArrayList<EffectComponent> buffs = new ArrayList<>();
            ArrayList<EffectComponent> debuffs = new ArrayList<>();

            Optional<SpeedEffectComponent> speedEffect;
            speedEffect = world.getComponent(entity, SpeedEffectComponent.class);

            Optional<DamageEffectComponent> damageEffect;
            damageEffect = world.getComponent(entity, DamageEffectComponent.class);

            Optional<ShieldEffectComponent> shieldEffect;
            shieldEffect = world.getComponent(entity, ShieldEffectComponent.class);

            Optional<ManaEffectComponent> manaEffect;
            manaEffect = world.getComponent(entity, ManaEffectComponent.class);

            Optional<InvulnerableEffectComponent> invulnerableEffect;
            invulnerableEffect = world.getComponent(entity, InvulnerableEffectComponent.class);

            Optional<WeatherSpeedEffectComponent> weatherEffect;
            weatherEffect = world.getComponent(entity, WeatherSpeedEffectComponent.class);


            if (turn.getTurn()) {

                if (speedEffect.isPresent()) {
                    SpeedEffectComponent effect = speedEffect.get();
                    if (effect.isBuff()) {
                        buffs.add(effect);
                    } else {
                        debuffs.add(effect);
                    }
                }

                if (shieldEffect.isPresent()) {
                    ShieldEffectComponent effect = shieldEffect.get();
                    if (effect.isBuff()) {
                        buffs.add(effect);
                    } else {
                        debuffs.add(effect);
                    }
                }

                if (invulnerableEffect.isPresent()) {
                    InvulnerableEffectComponent effect = invulnerableEffect.get();
                    if (effect.isBuff()) {
                        buffs.add(effect);
                    } else {
                        debuffs.add(effect);
                    }
                }

                if (manaEffect.isPresent()) {
                    ManaEffectComponent effect = manaEffect.get();
                    if (effect.isBuff()) {
                        buffs.add(effect);
                    } else {
                        debuffs.add(effect);
                    }
                }

                if (damageEffect.isPresent()) {
                    DamageEffectComponent effect = damageEffect.get();
                    if (effect.isBuff()) {
                        buffs.add(effect);
                    } else {
                        debuffs.add(effect);
                    }
                }

                if (weatherEffect.isPresent()) {
                    WeatherSpeedEffectComponent effect = weatherEffect.get();
                    if (effect.isBuff()) {
                        buffs.add(effect);
                    } else {
                        debuffs.add(effect);
                    }
                }

                if (bars.size() > 0) {
                    for (StatusBarComponent s : bars) {
                        if ("StatusPanel".equals(s.getId()) && s.getEntity().equals(cm.getEntity())) {
                            s.setWidth(215);
                            s.setHeight(10);
                            s.setValue(health.getHealth());

                        }
                        if ("ManaBar".equals(s.getId()) && s.getEntity().equals(cm.getEntity())) {
                            s.setWidth(125);
                            s.setHeight(10);
                            s.setValue(mana.getMana());

                        }
                        if ("compactManaBar".equals(s.getId()) && s.getEntity().equals(cm.getEntity())) {
                            s.setValue(mana.getMana());
                        }
                    }
                }

                // shows effect display
                drawBasePanel(sprite, name);

                int height = 0;
                int added = 0;

                // Display all of the buffs
                if (!buffs.isEmpty()) {
                    for (EffectComponent e : buffs) {
                        renderBuff(e, height, added, defaultString);
                        added++;
                        height += 20;
                    }
                }

                // Display all of the debuffs
                if (!debuffs.isEmpty()) {
                    for (EffectComponent e : debuffs) {
                        renderDebuff(e, height, added, defaultString);
                        ++added;
                        height += 20;
                    }
                }
            } else {
                if (bars.size() > 0) {
                    for (StatusBarComponent s : bars) {
                        if ("compactManaBar".equals(s.getId()) && s.getEntity().equals(cm.getEntity())) {
                            s.setValue(mana.getCapacity());
                        }
                    }
                }

            }
        }
    }

    /**
     * Renders the name, sprite, and duration of the given buff.
     *
     * @param e
     *         Effectcomponent that represents the buff being rendered
     * @param height
     *         position on y axis where the name of the effect will be rendered
     * @param added
     *         distance on the x axis between two rendered sprites
     * @param defaultString
     *         string representign the default font
     */
    private void renderBuff(EffectComponent e, int height, int added, String defaultString) {
        final String effectName = e.getName().getName();
        final int finalHeight = height;
        final int buffGap = added * 40;
        final String image = e.getName().getImageLocation();
        handler.addStaticRenderAction((context, frameHandler) -> {
            context.setFill(Color.FORESTGREEN);
            context.fillText(effectName, 215, 93 - 35 + finalHeight, 90);
            context.drawImage(frameHandler.loadImage(image), 90 + buffGap, 80 - 20, 35, 35);
            context.setFill(Color.FORESTGREEN);
            context.setFont(new Font(defaultString, 16));
            context.setTextAlign(TextAlignment.RIGHT);
            context.fillText(Integer.toString((int) e.getTurnDuration()), 132 + buffGap, 80 - 20 + 15);
            context.setFont(new Font(defaultString, 14));
            context.setTextAlign(TextAlignment.LEFT);
        });
    }

    /**
     * Renders the name, sprite, and duration of the given debuff.
     *
     * @param e
     *         Effectcomponent that represents the debuff being rendered
     * @param height
     *         position on y axis where the name of the effect will be rendered
     * @param added
     *         distance on the x axis between two rendered sprites
     * @param defaultString
     *         string representign the default font
     */
    private void renderDebuff(EffectComponent e, int height, int added, String defaultString) {
        final String effectName = e.getName().getName();
        final int finalHeight = height;
        final int buffGap = added * 40;
        final String image = e.getName().getImageLocation();
        handler.addStaticRenderAction((context, frameHandler) -> {
            context.setFill(Color.RED);
            context.fillText(effectName, 215, 93 - 35 + finalHeight, 90);
            context.drawImage(frameHandler.loadImage(image), 90 + buffGap, 80 - 20, 35, 35);
            context.setFill(Color.CRIMSON);
            context.setFont(new Font(defaultString, 16));
            context.setTextAlign(TextAlignment.RIGHT);
            context.fillText(Integer.toString((int) e.getTurnDuration()), 90 + buffGap + 42, 80 - 20 + 15);
            context.setFont(new Font(defaultString, 14));
            context.setTextAlign(TextAlignment.LEFT);
        });
    }

    /**
     * Renders a base panel for all the player specific statistic and
     * information to be drawn on top of.
     *
     * @param sprite
     *         Sprite of the current noodle's turn
     * @param name
     *         Name of the current noodle's turn
     */
    private void drawBasePanel(SpriteComponent sprite, NameComponent name) {

        int boxHeight = 95;
        // Add the tooltip box
        handler.addStaticRenderAction((context, frameHandler) -> {
            context.setFill(Color.LIGHTGREY);
            context.setStroke(Color.BLACK);
            context.fillRoundRect(10, 10, 300, boxHeight, 2, 2);
            context.strokeRoundRect(10, 10, 300, boxHeight, 2, 2);
            context.setFill(Color.LIGHTGREY);
            context.setStroke(Color.BLACK);
            context.fillRoundRect(10, 10, 75, 75, 2, 2);
            context.strokeRoundRect(10, 10, 75, 75, 2, 2);
            context.drawImage(frameHandler.loadImage(sprite.getImage()), 15, 15, 65, 65);
            context.setFill(Color.BLACK);
            context.setFont(new Font("Verdana", 16));
            context.setTextAlign(TextAlignment.LEFT);
            context.setFill(Color.GREEN);
            context.fillText(name.getName(), 90, 60 - 35);
        });
    }
}
