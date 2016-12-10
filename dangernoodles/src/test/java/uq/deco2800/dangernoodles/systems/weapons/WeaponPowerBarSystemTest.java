package uq.deco2800.dangernoodles.systems.weapons;

import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import uq.deco2800.dangernoodles.FrameHandler;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.components.weapons.WeaponComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.weapons.WeaponDefinition;

public class WeaponPowerBarSystemTest {
    
    @Test
    public void testNoErrorOnRun() {
        // No errors should occur
        World world = new World(0,0);
        world.addSystem(new WeaponPowerBarSystem(null), 0);
        world.process(0, 1);
    }
    
    @Test
    public void testWeaponNotFiring() {
        World world = new World(0, 0);
        FrameHandler handler = Mockito.mock(FrameHandler.class);
        world.addSystem(new WeaponPowerBarSystem(handler), 0);
        
        // create all the entities
        Entity player = world.createEntity();
        player.addComponent(new TurnComponent());
        WeaponDefinition wdef = new WeaponDefinition(0, "", "", "", 0, 0, true);
        Entity weapon = world.createEntity();
        WeaponComponent weaponComp = new WeaponComponent(wdef, player);
        weapon.addComponent(weaponComp);
        
        weaponComp.setFiring(false);
        
        world.process(0, 1);
        
        verify(handler, times(0)).addRenderAction(any());
    }
    
    @Test
    public void testWeaponNotPowered() {
        World world = new World(0, 0);
        FrameHandler handler = mock(FrameHandler.class);
        world.addSystem(new WeaponPowerBarSystem(handler), 0);
        
        // create all the entities
        Entity player = world.createEntity();
        player.addComponent(new TurnComponent());
        WeaponDefinition wdef = new WeaponDefinition(0, "", "", "", 0, 0, false);
        Entity weapon = world.createEntity();
        WeaponComponent weaponComp = new WeaponComponent(wdef, player);
        weapon.addComponent(weaponComp);
        
        weaponComp.setFiring(true);
        
        world.process(0, 1);
        
        verify(handler, times(0)).addRenderAction(any());
    }
    
    @Test
    public void testWeaponFiring() {
        World world = new World(0, 0);
        FrameHandler handler = Mockito.mock(FrameHandler.class);
        world.addSystem(new WeaponPowerBarSystem(handler), 0);
        
        // create all the entities
        Entity player = world.createEntity();
        TurnComponent turn = new TurnComponent();
        turn.setTurn(0);
        player.addComponent(turn)
                .addComponent(new PositionComponent(0, 0))
                .addComponent(new SpriteComponent(0, 0, ""));
        
        WeaponDefinition wdef = new WeaponDefinition(0, "", "", "", 0, 0, true);
        Entity weapon = world.createEntity();
        WeaponComponent weaponComp = new WeaponComponent(wdef, player);
        weapon.addComponent(weaponComp);
        
        weaponComp.setFiring(true);
        
        world.process(0, 1);
        
        verify(handler, times(2)).addRenderAction(any());
    }
    
    @Test
    public void notPlayersTurn() {
        World world = new World(0, 0);
        FrameHandler handler = Mockito.mock(FrameHandler.class);
        world.addSystem(new WeaponPowerBarSystem(handler), 0);
        
        // create all the entities
        Entity player = world.createEntity();
        player.addComponent(new TurnComponent());
        WeaponDefinition wdef = new WeaponDefinition(0, "", "", "", 0, 0, true);
        Entity weapon = world.createEntity();
        WeaponComponent weaponComp = new WeaponComponent(wdef, player);
        weapon.addComponent(weaponComp);
        
        weaponComp.setFiring(true);
        
        world.process(0, 1);
        
        verify(handler, times(0)).addRenderAction(any());
    }
    
    @Test
    public void testMultipleWeapons() {
        World world = new World(0, 0);
        FrameHandler handler = Mockito.mock(FrameHandler.class);
        world.addSystem(new WeaponPowerBarSystem(handler), 0);
        
        List<WeaponComponent> weapons = new ArrayList<WeaponComponent>();
        List<TurnComponent> turns = new ArrayList<TurnComponent>();
        WeaponDefinition wdef = new WeaponDefinition(0, "", "", "", 0, 0, true);
        // make 100 players/weapons
        for (int i = 0; i < 100; i++) {
            // create a player and weapon
            Entity player = world.createEntity();
            TurnComponent turn = new TurnComponent();
            player.addComponent(turn)
                    .addComponent(new PositionComponent(0, 0))
                    .addComponent(new SpriteComponent(0, 0, ""));
            
            Entity weapon = world.createEntity();
            WeaponComponent weaponComp = new WeaponComponent(wdef, player);
            weapon.addComponent(weaponComp);
            
            // add turn and weapon to list to be used later
            weapons.add(weaponComp);
            turns.add(turn);
        }
        
        for (int i = 0; i < 100; i++) {
            boolean firing = i % 3 == 0; // be firing every third iteration
            boolean isTurn = i % 4 == 0; // have turn every fourth iteration
            if (firing) {
                weapons.get(i).setFiring(true);
            }
            if (isTurn) {
                turns.get(i).setTurn(0);
            }
            
            world.process(0, 1);
            
            // should happen twice if firing and it's their turn, otherwise
            // should happen no times
            verify(handler, times(firing && isTurn ? 2 : 0)).addRenderAction(any());
            
            // clean up
            weapons.get(i).setFiring(false);
            turns.get(i).clearTurn(0);
            reset(handler);
        }
    }
}
