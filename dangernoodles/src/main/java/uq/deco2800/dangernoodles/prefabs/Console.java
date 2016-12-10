package uq.deco2800.dangernoodles.prefabs;

import uq.deco2800.dangernoodles.components.console.ConsoleDisplayComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

/**
 * Created by khoi_truong on 2016/10/04.
 * <p>
 * This class is used to set up console window for the game.
 */
public class Console {


    public Console(){
        // This is empty because it simply creates a console entity

    }

    /**
     * Create an abstract entity to store the console display component.
     *
     * @param world
     *         the game parent world
     *
     * @return the abstract entity that is storing the console display component
     */
    public static Entity createConsoleEntity(World world) {
        return world.createEntity()
                .addComponent(new ConsoleDisplayComponent());
    }

}
