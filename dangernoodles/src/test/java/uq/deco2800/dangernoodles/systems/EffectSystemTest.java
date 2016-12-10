package uq.deco2800.dangernoodles.systems;

import static org.junit.Assert.*;
import org.junit.Test;
import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.components.effects.*;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.components.stats.*;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;
import uq.deco2800.dangernoodles.prefabs.PlayerEntities;

/**
 * Created by Jason on 23/10/16.
 */
public class EffectSystemTest {
    World world = new World(0, 0);

    EffectSystem effectSystem = new EffectSystem();



    @Test
    public void testSpeedEffect() {
        world.addSystem(effectSystem, 0);
        Entity player = PlayerEntities.createPlayer(world, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);


        player.addComponent(new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 10, 10));
        assertTrue(world.getComponent(player, SpeedComponent.class).get().getSpeed() == 200);
        effectSystem.run(world, 0, 0);
        assertEquals(world.getComponent(player, SpeedComponent.class).get().getSpeed(), 0, 210);


    }
    @Test
    public void weatherEffect() {
        world.addSystem(effectSystem, 0);
        Entity player = PlayerEntities.createPlayer(world, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);


        player.addComponent(new WeatherSpeedEffectComponent(EffectEnum.FAST_RAIN_BUFF, 10, 10));
        assertEquals(world.getComponent(player, SpeedComponent.class).get().getSpeed(),200, 0);
        effectSystem.run(world, 0, 0);
        assertEquals(world.getComponent(player, SpeedComponent.class).get().getSpeed(),200, 210);

    }
    @Test
    public void testShieldEffect() {
        world.addSystem(effectSystem, 0);
        Entity player = PlayerEntities.createPlayer(world, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);

        assertEquals(world.getComponent(player, ShieldComponent.class).get().getShield(), 5, 0);
        player.addComponent(new ShieldEffectComponent(EffectEnum.SHIELD_BUFF, 10, 10));

        effectSystem.run(world, 0, 0);
        assertEquals(world.getComponent(player, ShieldComponent.class).get().getShield(), 0, 15);

    }
    @Test
    public void testmanaEffect() {
        world.addSystem(effectSystem, 0);
        Entity player = PlayerEntities.createPlayer(world, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);

        player.addComponent(new ManaComponent(5));
        assertEquals(world.getComponent(player, ManaComponent.class).get().getMana(),500, 0);
        player.addComponent(new ManaEffectComponent(EffectEnum.MANA_BUFF, 10, 10));

        effectSystem.run(world, 0, 0);
        assertEquals(world.getComponent(player, ManaComponent.class).get().getMana(),0 , 510);


    }
    @Test
    public void testinvulnEffect() {
        world.addSystem(effectSystem, 0);
        Entity player = PlayerEntities.createPlayer(world, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);

        assertEquals(world.getComponent(player, InvulnerableComponent.class).get().isInvulnerable(), false);
        player.addComponent(new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, 10, true));

        effectSystem.run(world, 0, 0);
        assertTrue(world.getComponent(player, InvulnerableComponent.class).get().isInvulnerable() == false);

    }

    @Test
    public void testDamageEffect() {
        world.addSystem(effectSystem, 0);
        Entity player = PlayerEntities.createPlayer(world, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);

        assertEquals(world.getComponent(player, DamageComponent.class).get().getDamage(),0, 0);
        player.addComponent(new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 10, 10));

        effectSystem.run(world, 0, 0);
        assertEquals(world.getComponent(player, DamageComponent.class).get().getDamage(), 0, 10);

    }
    @Test
    public void testSpeedEffectTurn() {
        world.addSystem(effectSystem, 0);
        Entity player = PlayerEntities.createPlayer(world, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);

        player.addComponent(new SpeedComponent(5));
        player.addComponent(new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 10, 10));

        world.getComponent(player, TurnComponent.class).get().setTurn(10);

        assertTrue(world.getComponent(player, SpeedComponent.class).get().getSpeed() == 200);
        effectSystem.run(world, 0, 10);
        effectSystem.run(world, 0, 10);
        assertEquals(world.getComponent(player, SpeedComponent.class).get().getSpeed(), 0, 210);



    }
    @Test
    public void weatherEffectTurn() {
        world.addSystem(effectSystem, 0);
        Entity player = PlayerEntities.createPlayer(world, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);

        player.addComponent(new SpeedComponent(5));
        player.addComponent(new WeatherSpeedEffectComponent(EffectEnum.FAST_RAIN_BUFF, 10, 10));

        world.getComponent(player, TurnComponent.class).get().setTurn(10);

        assertEquals(world.getComponent(player, SpeedComponent.class).get().getSpeed(),200, 0);
        effectSystem.run(world, 0, 10);
        effectSystem.run(world, 0, 10);
        assertEquals(world.getComponent(player, SpeedComponent.class).get().getSpeed(),200, 210);

    }
    @Test
    public void testShieldEffectTurn() {
        world.addSystem(effectSystem, 0);
        Entity player = PlayerEntities.createPlayer(world, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);

        player.addComponent(new ShieldComponent(5));
        player.addComponent(new WeatherSpeedEffectComponent(EffectEnum.SHIELD_BUFF, 10, 10));

        world.getComponent(player, TurnComponent.class).get().setTurn(10);

        assertEquals(world.getComponent(player, ShieldComponent.class).get().getShield(), 0, 5);
        effectSystem.run(world, 0, 10);
        effectSystem.run(world, 0, 10);
        assertEquals(world.getComponent(player, ShieldComponent.class).get().getShield(), 0, 15);


    }
    @Test
    public void testmanaEffectTurn() {
        world.addSystem(effectSystem, 0);
        Entity player = PlayerEntities.createPlayer(world, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);

        player.addComponent(new ManaComponent(5));
        player.addComponent(new ManaEffectComponent(EffectEnum.MANA_BUFF, 10, 10));

        world.getComponent(player, TurnComponent.class).get().setTurn(10);

        assertEquals(world.getComponent(player, ManaComponent.class).get().getMana(),0 , 500);
        effectSystem.run(world, 0, 10);
        effectSystem.run(world, 0, 10);
        assertEquals(world.getComponent(player, ManaComponent.class).get().getMana(),0 , 510);


    }
    @Test
    public void testinvulnEffectTurn() {
        world.addSystem(effectSystem, 0);
        Entity player = PlayerEntities.createPlayer(world, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);

        player.addComponent(new InvulnerableComponent());
        player.addComponent(new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, 10, true));

        world.getComponent(player, TurnComponent.class).get().setTurn(10);
        assertEquals(world.getComponent(player, InvulnerableComponent.class).get().isInvulnerable(), false);
        player.addComponent(new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, 10, true));
        effectSystem.run(world, 0, 10);
        effectSystem.run(world, 0, 10);
        assertTrue(world.getComponent(player, InvulnerableComponent.class).get().isInvulnerable() == true);

    }

    @Test
    public void testDamageEffectTurn() {
        world.addSystem(effectSystem, 0);
        Entity player = PlayerEntities.createPlayer(world, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);

        player.addComponent(new DamageComponent(10));
        player.addComponent(new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 10, 10));

        world.getComponent(player, TurnComponent.class).get().setTurn(10);

        assertEquals(world.getComponent(player, DamageComponent.class).get().getDamage(), 0, 0);
        effectSystem.run(world, 0, 10);
        effectSystem.run(world, 0, 10);
        assertEquals(world.getComponent(player, DamageComponent.class).get().getDamage(), 0, 10);

    }

}