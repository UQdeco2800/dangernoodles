package uq.deco2800.dangernoodles.systems;

import uq.deco2800.dangernoodles.components.noodles.NoodleComponent;
import uq.deco2800.dangernoodles.components.InputComponent;
import uq.deco2800.dangernoodles.components.MovementComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.components.PlayerComponent;
import uq.deco2800.dangernoodles.components.stats.ManaComponent;
import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;

/**
 * 
 * @author Paul Haley
 * @author Anant Tuli
 * 
 *         The NoodleSystem handles the changing of sprite images for noodles.
 *
 */
public class NoodleSystem implements System {

	/**
	 * The override run method in the Noodle System selects the appropriate
	 * sprite for all noodle entities.
	 * 
	 * @require world != null
	 * 
	 * @param world The world to be running the system on
	 * @param t Current game time
	 * @param dt Delta time between last call of system
	 */
	@Override
	public void run(World world, double t, double dt) {
		for (ComponentMap cm : world.getIterator(SpriteComponent.class, MovementComponent.class, NoodleComponent.class,
				PlayerComponent.class, InputComponent.class, ManaComponent.class)) {

			// Retrieves the individual components of the current entities
			// ComponentMap needed for sprite setting
			SpriteComponent sprite = cm.get(SpriteComponent.class);
			MovementComponent movement = cm.get(MovementComponent.class);
			NoodleComponent noodle = cm.get(NoodleComponent.class);
			PlayerComponent noodlePlayer = cm.get(PlayerComponent.class);
			InputComponent input = cm.get(InputComponent.class);
			ManaComponent mana = cm.get(ManaComponent.class);

			/*
			 * Noodle sprites are found through testing if parameters of the
			 * noodle with respect to team, noodle class, if it is moving and if
			 * it is moving left or right. The testing performs string
			 * Concatenation to produce the filename and path to the correct
			 * sprite.
			 */
			// check team and make string mutator to match file
			String teamID;
			switch (noodlePlayer.getTeamId()) {
			case 0:
				teamID = "_Bteam";
				break;
			case 1:
				teamID = "_Rteam";
				break;
			case 2:
				teamID = "";
				break;
			case 3:
				teamID = "_Yteam";
				break;
			default:// Default to green noodles
				teamID = "";
			}

			// Checking the noodle class
			String noodleClass;
			switch (noodle.getNoodleClassName()) {
			case "TANK":
				noodleClass = "Juggernaught";
				break;
			case "AGILITY":
				noodleClass = "Scout";
				break;
			case "WARRIOR":
				noodleClass = "Warrior";
				break;
			default:// If none of the previous classes matched or the noodle is
					// PLAIN, uses default noodle sprite
				noodleClass = "Civilian";
			}
			
			// Sets the moving and direction
			String moving;
			if (movement.isMovingY()) {
				moving = "_jump";
			} else if (movement.isMovingX()) {
				moving = "";
			} else {
				moving = "_idle";
			}
			
			String left;
			
			// Checks if the noodle is moving left or if the noodle is out of mana but told to face left
			if (movement.getVX() < 0.0 || (mana.getMana() == 0 && input.getLastX() == -1)) {
				left = "";
			} else {
				left = "I-";
			}

			// Setting the image path through concatenation
			String filePath = "/characters/" + left + noodleClass + moving + teamID + ".gif";
			
			/* Stores the noodle sprite in the NoodleComponent itself and in 
			 * the SpriteComponent; this is mostly for testing of the 
			 * NoodleComponent.
			 */
			noodle.setCurrentSprite(filePath);
			sprite.setImage(noodle.getCurrentSprite());
		}

	}

}
