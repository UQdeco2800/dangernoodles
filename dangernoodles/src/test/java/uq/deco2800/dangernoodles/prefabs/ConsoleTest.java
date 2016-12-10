package uq.deco2800.dangernoodles.prefabs;

import static org.junit.Assert.*;
import org.junit.Test;
import uq.deco2800.dangernoodles.components.console.ConsoleDisplayComponent;
import uq.deco2800.dangernoodles.ecs.World;

/**
 * Created by Jason on 19/10/16.
 */
public class ConsoleTest {

    World testworld = new World(0, 0);
    @Test
    public void consoleTest() {
        Console console = new Console();
        testworld.createEntity().addComponent(new ConsoleDisplayComponent());
        assertTrue(testworld.getComponents(ConsoleDisplayComponent.class).size() == 1);
        console.createConsoleEntity(testworld);
        assertTrue(testworld.getComponents(ConsoleDisplayComponent.class).size() == 2);

    }

}