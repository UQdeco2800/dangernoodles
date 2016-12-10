package uq.deco2800.dangernoodles.systems;

import static org.junit.Assert.*;
import org.junit.Test;
import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.components.displays.EffectDisplayComponent;
import uq.deco2800.dangernoodles.components.effects.*;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.components.stats.ManaComponent;
import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;
import uq.deco2800.dangernoodles.prefabs.PlayerEntities;

import java.util.List;
import java.util.Optional;

/**
 * Created by Jason on 20/10/16.
 */
public class EffectBoxSystemTest {


    @Test
    public void testSystem() {
        World testWorld = new World(0, 0);
        EffectBoxSystem effectBoxSystem = new EffectBoxSystem();


        Entity player = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);

        testWorld.addSystem(effectBoxSystem, 0);

        effectBoxSystem.run(testWorld, 0, 0);

        List<NameComponent> nameComponentList = testWorld.getComponents(NameComponent.class);

        int boxes = 0;

        for (int i = 0; i < nameComponentList.size(); i++) {
            if (("Mystery Box").equals(nameComponentList.get(i).getName())) {
                boxes++;
            }
        }

        assertEquals(boxes, 0);

        effectBoxSystem.run(testWorld, 0, 11);

        nameComponentList = testWorld.getComponents(NameComponent.class);

        boxes = 0;

        for (int i = 0; i < nameComponentList.size(); i++) {
            if (("Mystery Box").equals(nameComponentList.get(i).getName())) {
                boxes++;
            }
        }

        assertEquals(boxes, 1);



    }

    @Test
    public void testGetBuff() {
        World testWorld = new World(0, 0);
        EffectBoxSystem effectBoxSystem = new EffectBoxSystem();

        Entity effectbox = testWorld.createEntity()
                .addComponent(new RectangleComponent(30, 30))
                .addComponent(new EffectDisplayComponent())
                .addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
                .addComponent(new MovementComponent(0.0, 0.0, 1.0, 0.0))
                .addComponent(new ManaComponent(1))
                .addComponent(new NameComponent("Mystery Box"));
        
        List<NameComponent> nameComponentList = testWorld.getComponents(NameComponent.class);

        int boxes = 0;
        for (int i = 0; i < nameComponentList.size(); i++) {
            if (("Mystery Box").equals(nameComponentList.get(i).getName())) {
                boxes++;
            }

        }

        assertTrue(boxes == 1);

        testWorld.addSystem(effectBoxSystem, 0);
        effectBoxSystem.run(testWorld, 0, 0);

        boxes = 0;
        for (int i = 0; i < nameComponentList.size(); i++) {
            if (("Mystery Box").equals(nameComponentList.get(i).getName())) {
                boxes++;
            }

        }

        assertTrue(boxes == 1);


        //Test that if there is no collisions there is still 1 box
        Entity player = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);

        for (ComponentMap cm : testWorld.getIterator(
                PlayerComponent.class,
                CollisionComponent.class)) {

            CollisionComponent collisions = cm.get(CollisionComponent.class);


        }

        effectBoxSystem.run(testWorld, 0, 0);

        boxes = 0;
        for (int i = 0; i < nameComponentList.size(); i++) {
            if (("Mystery Box").equals(nameComponentList.get(i).getName())) {
                boxes++;
            }

        }

        assertTrue(boxes == 1);


        //Test that if there is a collisions there is 0 boxes


        for (ComponentMap cm : testWorld.getIterator(
                PlayerComponent.class,
                CollisionComponent.class)) {

            CollisionComponent collisions = cm.get(CollisionComponent.class);
            collisions.addCollision(effectbox);


        }

        effectBoxSystem.run(testWorld, 0, 0);

        boxes = 0;
        for (int i = 0; i < nameComponentList.size(); i++) {
            if (("Mystery Box").equals(nameComponentList.get(i).getName())) {
                boxes++;
            }

        }

        assertTrue(boxes == 0);



    }

    @Test (expected = NullPointerException.class)
    public void testbadEffectApplication() {

        World testWorld = new World(0, 0);
        EffectBoxSystem effectBoxSystem = new EffectBoxSystem();

        testWorld.addSystem(effectBoxSystem, 0);

        Entity effectbox = testWorld.createEntity()
                .addComponent(new RectangleComponent(30, 30))
                .addComponent(new EffectDisplayComponent())
                .addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
                .addComponent(new MovementComponent(0.0, 0.0, 1.0, 0.0))
                .addComponent(new ManaComponent(1))
                .addComponent(new NameComponent("Mystery Box"));

        Entity player = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);


        for (ComponentMap cm : testWorld.getIterator(
                PlayerComponent.class,
                CollisionComponent.class)) {

            CollisionComponent collisions = cm.get(CollisionComponent.class);
            collisions.addCollision(effectbox);


        }

        effectBoxSystem.run(null, 0, 0);



    }

    @Test
    public void testBuffsAddedDamage() {

        World testWorld = new World(0, 0);
        EffectBoxSystem effectBoxSystem = new EffectBoxSystem();

        testWorld.addSystem(effectBoxSystem, 0);

        Entity effectbox = testWorld.createEntity()
                .addComponent(new RectangleComponent(30, 30))
                .addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
                .addComponent(new NameComponent("Mystery Box"));

        Entity player = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);


        for (ComponentMap cm : testWorld.getIterator(
                PlayerComponent.class,
                CollisionComponent.class)) {

            CollisionComponent collisions = cm.get(CollisionComponent.class);
            collisions.addCollision(effectbox);


        }
        Optional<DamageEffectComponent> damageEffectComponentOptional;
        damageEffectComponentOptional = testWorld.getComponent(player, DamageEffectComponent.class);
        assertEquals(damageEffectComponentOptional.isPresent(), false);

        effectbox.addComponent(new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 5, 10));
        effectBoxSystem.run(testWorld, 0, 0);

        damageEffectComponentOptional = testWorld.getComponent(player, DamageEffectComponent.class);
        assertEquals(damageEffectComponentOptional.isPresent(), true);



    }

    @Test
    public void testBuffsAddedSpeed() {

        World testWorld = new World(0, 0);
        EffectBoxSystem effectBoxSystem = new EffectBoxSystem();

        testWorld.addSystem(effectBoxSystem, 0);

        Entity effectbox = testWorld.createEntity()
                .addComponent(new GravityComponent(true))
                .addComponent(new RectangleComponent(30, 30))
                .addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
                .addComponent(new NameComponent("Mystery Box"));

        Entity player = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);


        for (ComponentMap cm : testWorld.getIterator(
                PlayerComponent.class,
                CollisionComponent.class)) {

            CollisionComponent collisions = cm.get(CollisionComponent.class);
            collisions.addCollision(effectbox);

        }
        Optional<SpeedEffectComponent> speedEffectComponentOptional;
        speedEffectComponentOptional = testWorld.getComponent(player, SpeedEffectComponent.class);
        assertEquals(speedEffectComponentOptional.isPresent(), false);

        effectbox.addComponent(new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 5, 10));
        effectBoxSystem.run(testWorld, 0, 0);

        speedEffectComponentOptional = testWorld.getComponent(player, SpeedEffectComponent.class);
        assertEquals(speedEffectComponentOptional.isPresent(), true);
    }

    @Test
    public void testBuffsAddedShield() {
        World testWorld = new World(0, 0);
        EffectBoxSystem effectBoxSystem = new EffectBoxSystem();

        testWorld.addSystem(effectBoxSystem, 0);

        Entity effectbox = testWorld.createEntity()
                .addComponent(new RectangleComponent(30, 30))
                .addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
                .addComponent(new NameComponent("Mystery Box"));

        Entity player = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);


        for (ComponentMap cm : testWorld.getIterator(
                PlayerComponent.class,
                CollisionComponent.class)) {

            CollisionComponent collisions = cm.get(CollisionComponent.class);
            collisions.addCollision(effectbox);

        }
        Optional<ShieldEffectComponent> shieldEffectComponentOptional;
        shieldEffectComponentOptional = testWorld.getComponent(player, ShieldEffectComponent.class);
        assertEquals(shieldEffectComponentOptional.isPresent(), false);

        effectbox.addComponent(new ShieldEffectComponent(EffectEnum.SHIELD_BUFF, 5, 10));
        effectBoxSystem.run(testWorld, 0, 0);

        shieldEffectComponentOptional = testWorld.getComponent(player, ShieldEffectComponent.class);
        assertEquals(shieldEffectComponentOptional.isPresent(), true);
    }

    @Test
    public void testBuffsAddedMana() {

        World testWorld = new World(0, 0);
        EffectBoxSystem effectBoxSystem = new EffectBoxSystem();

        testWorld.addSystem(effectBoxSystem, 0);

        Entity effectbox = testWorld.createEntity()
                .addComponent(new RectangleComponent(30, 30))
                .addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
                .addComponent(new NameComponent("Mystery Box"));

        Entity player = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);


        for (ComponentMap cm : testWorld.getIterator(
                PlayerComponent.class,
                CollisionComponent.class)) {

            CollisionComponent collisions = cm.get(CollisionComponent.class);
            collisions.addCollision(effectbox);

        }
        Optional<ManaEffectComponent> manaEffectComponentOptional;
        manaEffectComponentOptional = testWorld.getComponent(player, ManaEffectComponent.class);
        assertEquals(manaEffectComponentOptional.isPresent(), false);

        effectbox.addComponent(new ManaEffectComponent(EffectEnum.MANA_BUFF, 5, 10));

        effectBoxSystem.run(testWorld, 0, 0);

        manaEffectComponentOptional = testWorld.getComponent(player, ManaEffectComponent.class);
        assertEquals(manaEffectComponentOptional.isPresent(), true);

    }

    @Test
    public void testBuffsAddedInlvunerable() {

        World testWorld = new World(0, 0);
        EffectBoxSystem effectBoxSystem = new EffectBoxSystem();

        testWorld.addSystem(effectBoxSystem, 0);

        Entity effectbox = testWorld.createEntity()
                .addComponent(new RectangleComponent(30, 30))
                .addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
                .addComponent(new NameComponent("Mystery Box"));

        Entity player = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);


        for (ComponentMap cm : testWorld.getIterator(
                PlayerComponent.class,
                CollisionComponent.class)) {

            CollisionComponent collisions = cm.get(CollisionComponent.class);
            collisions.addCollision(effectbox);

        }

        Optional<InvulnerableEffectComponent> invulnerableEffectComponentOptional;
        invulnerableEffectComponentOptional
                = testWorld.getComponent(player, InvulnerableEffectComponent.class);
        assertEquals(invulnerableEffectComponentOptional.isPresent(), false);

        effectbox.addComponent(new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, 5, true));
        effectBoxSystem.run(testWorld, 0, 0);
        invulnerableEffectComponentOptional
                = testWorld.getComponent(player, InvulnerableEffectComponent.class);
        assertEquals(invulnerableEffectComponentOptional.isPresent(), true);

    }

    @Test
    public void testBuffsAddedwithComponentsDamage() {
        World testWorld = new World(0, 0);
        EffectBoxSystem effectBoxSystem = new EffectBoxSystem();

        testWorld.addSystem(effectBoxSystem, 0);

        Entity effectbox = testWorld.createEntity()
                .addComponent(new RectangleComponent(30, 30))
                .addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
                .addComponent(new NameComponent("Mystery Box"));
        
        Entity player = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);


        for (ComponentMap cm : testWorld.getIterator(
                PlayerComponent.class,
                CollisionComponent.class)) {

            CollisionComponent collisions = cm.get(CollisionComponent.class);
            collisions.addCollision(effectbox);


        }


        player.addComponent(new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 5, 10));

        assertEquals(testWorld.getComponent(player,
                DamageEffectComponent.class).get().getTurnDuration(), 5);

        effectbox.addComponent(new DamageEffectComponent(EffectEnum.DAMAGE_BUFF, 5, 10));

        effectBoxSystem.run(testWorld, 0, 0);

    }

    @Test
    public void testBuffsAddedwithComponentsSpeed() {
        World testWorld = new World(0, 0);
        EffectBoxSystem effectBoxSystem = new EffectBoxSystem();

        testWorld.addSystem(effectBoxSystem, 0);

        Entity effectbox = testWorld.createEntity()
                .addComponent(new RectangleComponent(30, 30))
                .addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
                .addComponent(new NameComponent("Mystery Box"));
        
        Entity player = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);


        for (ComponentMap cm : testWorld.getIterator(
                PlayerComponent.class,
                CollisionComponent.class)) {

            CollisionComponent collisions = cm.get(CollisionComponent.class);
            collisions.addCollision(effectbox);


        }


        player.addComponent(new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 5, 10));

        assertEquals(testWorld.getComponent(player,
                SpeedEffectComponent.class).get().getTurnDuration(), 5);

        effectbox.addComponent(new SpeedEffectComponent(EffectEnum.SPEED_BUFF, 5, 10));

        effectBoxSystem.run(testWorld, 0, 0);

    }

    @Test
    public void testBuffsAddedwithComponentsShield() {
        World testWorld = new World(0, 0);
        EffectBoxSystem effectBoxSystem = new EffectBoxSystem();

        testWorld.addSystem(effectBoxSystem, 0);

        Entity effectbox = testWorld.createEntity()
                .addComponent(new RectangleComponent(30, 30))
                .addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
                .addComponent(new NameComponent("Mystery Box"));
        
        Entity player = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);


        for (ComponentMap cm : testWorld.getIterator(
                PlayerComponent.class,
                CollisionComponent.class)) {

            CollisionComponent collisions = cm.get(CollisionComponent.class);
            collisions.addCollision(effectbox);


        }

        player.addComponent(new ShieldEffectComponent(EffectEnum.SHIELD_BUFF, 5, 10));

        assertEquals(testWorld.getComponent(player,
                ShieldEffectComponent.class).get().getTurnDuration(), 5);


        effectbox.addComponent(new ShieldEffectComponent(EffectEnum.SHIELD_BUFF, 5, 10));

        effectBoxSystem.run(testWorld, 0, 0);

    }

    @Test
    public void testBuffsAddedwithComponentsMana() {
        World testWorld = new World(0, 0);
        EffectBoxSystem effectBoxSystem = new EffectBoxSystem();

        testWorld.addSystem(effectBoxSystem, 0);

        Entity effectbox = testWorld.createEntity()
                .addComponent(new RectangleComponent(30, 30))
                .addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
                .addComponent(new NameComponent("Mystery Box"));
        
        Entity player = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);


        for (ComponentMap cm : testWorld.getIterator(
                PlayerComponent.class,
                CollisionComponent.class)) {

            CollisionComponent collisions = cm.get(CollisionComponent.class);
            collisions.addCollision(effectbox);


        }

        player.addComponent(new ManaEffectComponent(EffectEnum.MANA_BUFF, 1, 5));

        assertEquals(testWorld.getComponent(player,
                ManaEffectComponent.class).get().getTurnDuration(), 1);

        effectbox.addComponent(new ManaEffectComponent(EffectEnum.MANA_BUFF, 5, 10));

        effectBoxSystem.run(testWorld, 0, 0);

    }

    @Test
    public void testBuffsAddedwithComponentsInvulnerable() {
        World testWorld = new World(0, 0);
        EffectBoxSystem effectBoxSystem = new EffectBoxSystem();

        testWorld.addSystem(effectBoxSystem, 0);

        Entity effectbox = testWorld.createEntity()
                .addComponent(new RectangleComponent(30, 30))
                .addComponent(new CollisionComponent(true, CollisionComponent.shape.RECTANGLE))
                .addComponent(new NameComponent("Mystery Box"));
        
        Entity player = PlayerEntities.createPlayer(testWorld, NoodleEnum.NOODLE_TANK,
                false, TeamEnum.TEAM_ALPHA, 0, 50);


        for (ComponentMap cm : testWorld.getIterator(
                PlayerComponent.class,
                CollisionComponent.class)) {

            CollisionComponent collisions = cm.get(CollisionComponent.class);
            collisions.addCollision(effectbox);


        }


        player.addComponent(new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, 5, true));

        assertEquals(testWorld.getComponent(player,
                InvulnerableEffectComponent.class).get().getTurnDuration(), 5);

        effectbox.addComponent(new InvulnerableEffectComponent(EffectEnum.INVULNERABLE_BUFF, 5, true));

        effectBoxSystem.run(testWorld, 0, 0);

    }




}
