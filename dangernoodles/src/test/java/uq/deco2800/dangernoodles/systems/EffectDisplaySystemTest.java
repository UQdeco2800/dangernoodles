package uq.deco2800.dangernoodles.systems;

import static org.junit.Assert.*;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.junit.Test;
import static org.mockito.Mockito.*;
import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.components.StatusBarComponent;
import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.components.effects.*;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;
import uq.deco2800.dangernoodles.prefabs.PlayerEntities;

import java.util.List;
import java.util.Optional;

/**
 * Created by Jason on 20/10/16.
 */
public class EffectDisplaySystemTest {


    @Test
    public void testrun() {

        World testWorld = new World(0, 0);
        Canvas canvas = mock(Canvas.class);
        GraphicsContext context = canvas.getGraphicsContext2D();
        StaticFrameHandler staticFrameHandler = new StaticFrameHandler(context);
        EffectDisplaySystem effectDisplaySystem = new EffectDisplaySystem(staticFrameHandler);

        testWorld.addSystem(effectDisplaySystem, 0);

        Entity player = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);

        player.addComponent(new SpeedEffectComponent(EffectEnum.SHIELD_BUFF, 5, 5));

        player.addComponent(new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 5, 5));

        player.addComponent(new ShieldEffectComponent(EffectEnum.SHIELD_BUFF, 5, 5));

        player.addComponent(new ManaEffectComponent(EffectEnum.MANA_BUFF, 5, 5));

        player.addComponent(new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, 5, true));

        player.addComponent(new WeatherSpeedEffectComponent(EffectEnum.FAST_RAIN_BUFF, 5, 5));



        List<StatusBarComponent> bars = testWorld.getComponents(StatusBarComponent.class);

        for (StatusBarComponent s : bars) {
            if ("StatusPanel".equals(s.getId())) {
                int value = s.getValue();
                assertTrue(value == 350);
                assertTrue(s.getWidth() == 215);

            }
            if ("ManaBar".equals(s.getId())) {
                int value = s.getValue();
                assertTrue(value == 500);
                assertTrue(s.getWidth() == 125);

            }
            if ("compactManaBar".equals(s.getId())) {
                int value = s.getValue();
                assertTrue(value == 500);
            }
        }

        bars = testWorld.getComponents(StatusBarComponent.class);

        effectDisplaySystem.run(testWorld, 0, 0);

        //Healthbars were changed according to buffs added and then reset at the end of the turn

        for (StatusBarComponent s : bars) {
            if ("StatusPanel".equals(s.getId())) {
                int value = s.getValue();
                assertTrue(value == 350);
                assertTrue(s.getWidth() == 215);

            }
            if ("ManaBar".equals(s.getId())) {
                int value = s.getValue();
                assertTrue(value == 500);
                assertTrue(s.getWidth() == 125);

            }
            if ("compactManaBar".equals(s.getId())) {
                int value = s.getMaxValue();
                assertTrue(value == 500);
            }
        }



    }

    @Test
    public void runwithTurn() {

        World testWorld = new World(0, 0);
        Canvas canvas = mock(Canvas.class);
        GraphicsContext context = canvas.getGraphicsContext2D();
        StaticFrameHandler staticFrameHandler = new StaticFrameHandler(context);
        EffectDisplaySystem effectDisplaySystem = new EffectDisplaySystem(staticFrameHandler);

        testWorld.addSystem(effectDisplaySystem, 0);

        Entity player = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);

        player.addComponent(new SpeedEffectComponent(EffectEnum.SHIELD_BUFF, 5, 5));

        player.addComponent(new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 5, 5));

        player.addComponent(new ShieldEffectComponent(EffectEnum.SHIELD_BUFF, 5, 5));

        player.addComponent(new ManaEffectComponent(EffectEnum.MANA_BUFF, 5, 5));

        player.addComponent(new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, 5, true));

        player.addComponent(new WeatherSpeedEffectComponent(EffectEnum.FAST_RAIN_BUFF, 5, 5));


        testWorld.getComponent(player, TurnComponent.class).get().setTurn(10);

        effectDisplaySystem.run(testWorld, 0, 10);

    }

    @Test
    public void runwithTurnDebuff() {

        World testWorld = new World(0, 0);
        Canvas canvas = mock(Canvas.class);
        GraphicsContext context = canvas.getGraphicsContext2D();
        StaticFrameHandler staticFrameHandler = new StaticFrameHandler(context);
        EffectDisplaySystem effectDisplaySystem = new EffectDisplaySystem(staticFrameHandler);

        testWorld.addSystem(effectDisplaySystem, 0);

        Entity player = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);

        player.addComponent(new SpeedEffectComponent(EffectEnum.SHIELD_DEBUFF, 5, -5));

        player.addComponent(new DamageEffectComponent(EffectEnum.DAMAGE_DEBUFF, 5, -5));

        player.addComponent(new ShieldEffectComponent(EffectEnum.SHIELD_DEBUFF, 5, -5));

        player.addComponent(new ManaEffectComponent(EffectEnum.MANA_DEBUFF, 5, -5));

        player.addComponent(new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_DEBUFF, 5, false));


        testWorld.getComponent(player, TurnComponent.class).get().setTurn(10);

        effectDisplaySystem.run(testWorld, 0, 10);

    }




}