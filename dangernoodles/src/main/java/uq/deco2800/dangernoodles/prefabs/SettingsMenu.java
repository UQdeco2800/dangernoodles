package uq.deco2800.dangernoodles.prefabs;

import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

/**
 * Created by khoi_truong on 2016/10/04.
 * <p>
 * This class is used to draw out a button which can be clicked to display
 * game option menu inside the game.
 */
public class SettingsMenu {

    public SettingsMenu(){
        //This method is empty because it simply returns a new entity

    }

    /**
     * Create an abstract entity to hold pause component, which is used to
     * pause the game when setting menu is displayed.
     *
     * @param world
     *         game parent world
     *
     * @return abstract entity that holds the pause component
     *
     * @throws NullPointerException
     *         if world is null
     * @require world != null
     * @ensure abstract entity that holds the pause component
     */
    public static Entity createPause(World world) {
        if (world == null) {
            throw new NullPointerException("World cannot be null.");
        }
        return world.createEntity()
                .addComponent(new NameComponent("Pause"))
                .addComponent(new PauseComponent());
    }
}
