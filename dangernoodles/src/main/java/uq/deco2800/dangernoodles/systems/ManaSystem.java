package uq.deco2800.dangernoodles.systems;

import uq.deco2800.dangernoodles.components.MovementComponent;
import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.components.stats.ManaComponent;
import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;

/**
 * Created by khoi_truong on 2016/09/17.
 * <p>
 * This class is used to handle mana mechanism of noodles inside the game.
 */
public class ManaSystem implements System {

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
        // Get all noodles that have a mana component.
        for (ComponentMap cm : world.getIterator(
                ManaComponent.class,
                MovementComponent.class,
                TurnComponent.class)) {

            // Retrieving all the necessary components.
            TurnComponent turn = cm.get(TurnComponent.class);
            MovementComponent movement = cm.get(MovementComponent.class);
            ManaComponent mana = cm.get(ManaComponent.class);

            // Check if this is the current noodle's turn. Once its turn is
            // over, then reset its mana as well as making it be able to move
            // again.
            if (turn.getTurn()) {
                // Check if the noodle is still able to move. If yes, then
                // check if it's moving on the X axis.
                // Also check whether the noodle is allowed to have its mana adjusted.
                if (movement.isMovable() && movement.isMovingX() && mana.getManaUpdateableStatus()) {
                    // Update the new mana
                    int currentMana = mana.getMana();
                    int newMana = currentMana - 1;

                    // If the new mana is less than 0, than this noodle is no
                    // longer movable for the rest of the turn.
                    if (newMana >= 0) {
                        mana.setMana(newMana);
                    } else {
                        movement.setMovable(false);
                    }
                }
            } else {
                mana.reset();
                movement.setMovable(true);
            }
        }
    }
}
