package uq.deco2800.dangernoodles.systems;

import static org.junit.Assert.*;
import org.junit.Test;
import uq.deco2800.dangernoodles.components.CollisionComponent;
import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.components.damages.InstantDamageComponent;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.components.stats.InvulnerableComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;
import uq.deco2800.dangernoodles.prefabs.PlayerEntities;
import uq.deco2800.dangernoodles.prefabs.TerrainPrefabs;

/**
 * Created by Jason on 23/10/16.
 */
public class DamageSystemTest {

    private World world = new World(0, 0);

    @Test
    public void Run() {
        DamageSystem damageSystem = new DamageSystem();
        world.addSystem(damageSystem, 0);
        Entity player = PlayerEntities.createPlayer(world, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);


        world.getComponent(player, TurnComponent.class).get().setTurn(10);

        damageSystem.run(world, 0, 10);


    }
    @Test
    public void Run2() {
        DamageSystem damageSystem = new DamageSystem();
        world.addSystem(damageSystem, 0);
        Entity player = PlayerEntities.createPlayer(world, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);

        Entity player2 = PlayerEntities.createPlayer(world, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);

        world.getComponent(player, TurnComponent.class).get().setTurn(10);
        world.addComponent(player, new InstantDamageComponent(5));
        world.addComponent(player2, new InstantDamageComponent(5));
        world.getComponent(player, CollisionComponent.class).get().getCollisions().add(0, player2);
        world.getComponent(player2, CollisionComponent.class).get().getCollisions().add(0, player);

        damageSystem.run(world, 0, 10);


    }
    @Test
    public void invulnerable() {
        DamageSystem damageSystem = new DamageSystem();
        world.addSystem(damageSystem, 0);
        Entity player = PlayerEntities.createPlayer(world, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);

        Entity player2 = PlayerEntities.createPlayer(world, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);

        world.getComponent(player, TurnComponent.class).get().setTurn(10);
        world.addComponent(player, new InstantDamageComponent(5));
        world.addComponent(player2, new InstantDamageComponent(5));
        world.getComponent(player, CollisionComponent.class).get().getCollisions().add(0, player2);
        world.getComponent(player2, CollisionComponent.class).get().getCollisions().add(0, player);
        world.addComponent(player2, new InvulnerableComponent());
        world.getComponent(player2, InvulnerableComponent.class).get().setInvulnerable(true);


        damageSystem.run(world, 0, 10);


    }
    @Test
    public void Runcollisionground() {
        DamageSystem damageSystem = new DamageSystem();
        world.addSystem(damageSystem, 0);
        Entity player = PlayerEntities.createPlayer(world, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);

        Entity player2 = PlayerEntities.createPlayer(world, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);

        world.getComponent(player, TurnComponent.class).get().setTurn(10);
        Entity terrain = TerrainPrefabs.createDirt(world, 50, 50, 25);
        world.addComponent(player, new InstantDamageComponent(5));
        world.addComponent(player2, new InstantDamageComponent(5));

        world.getComponent(player, CollisionComponent.class).get().getCollisions().add(0, terrain);




        damageSystem.run(world, 0, 10);


    }

}