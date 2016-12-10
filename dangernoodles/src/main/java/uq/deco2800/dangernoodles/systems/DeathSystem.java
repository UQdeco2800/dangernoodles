package uq.deco2800.dangernoodles.systems;

import java.util.ArrayDeque;
import java.util.Deque;

import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.WeaponEntities;

public class DeathSystem implements System{
    
	@Override
	public void run(World world, double t, double dt){
		Deque<PositionComponent> toKill = tagForDeath(world);
		while (!toKill.isEmpty()) {
		    kill(world, toKill.pop());
		}
	}
	/**
	 * Deletes the weapon entity, then the player entity that falls off the map
	 * @param world
	 * 		the world
	 * @param position
	 * 		positionComponent of the object falling off the map
	 */
	public void kill(World world, PositionComponent position){
	    // Get rid of their weapon first
	    WeaponEntities.removePlayersWeapon(world, position.getEntity());
	    // Fully remove player
		world.destroyEntity(position.getEntity());
		
	}
	/**
	 * Returns a deque of out of bounds position components, to have their entities terminated
	 * @param world
	 * 		the world
	 * @return
	 * 		A Deque of positionComponents
	 */		
	public Deque<PositionComponent> tagForDeath(World world){
		Deque<PositionComponent> toKill = new ArrayDeque<PositionComponent>();
		// find every out of bounds position component
		for (PositionComponent position: world.getComponents(PositionComponent.class)){
			if (position.getY() > 4000){
				toKill.add(position);
			}
		}
		return toKill;
	}
}
