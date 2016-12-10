package uq.deco2800.dangernoodles.prefabs;

import static org.junit.Assert.*;
import org.junit.Test;
import uq.deco2800.dangernoodles.components.NameComponent;
import uq.deco2800.dangernoodles.components.PauseComponent;

import uq.deco2800.dangernoodles.ecs.World;

/**
 * Created by Jason on 20/10/16.
 */
public class SettingsMenuTest {
    private World testworld = new World(0, 0);
    private SettingsMenu settings = new SettingsMenu();

    @Test
    public void createPause() {

        Console console = new Console();
        testworld.createEntity()
                .addComponent(new NameComponent("Pause"))
                .addComponent(new PauseComponent());

        assertTrue(testworld.getComponents(NameComponent.class).size() == 1);
        assertTrue(testworld.getComponents(PauseComponent.class).size() == 1);

        settings.createPause(testworld);
        assertTrue(testworld.getComponents(NameComponent.class).size() == 2);
        assertTrue(testworld.getComponents(PauseComponent.class).size() == 2);

    }
    @Test (expected = NullPointerException.class)
    public void createBadPause() {

        Console console = new Console();
        testworld.createEntity()
                .addComponent(new NameComponent("Pause"))
                .addComponent(new PauseComponent());

        assertTrue(testworld.getComponents(NameComponent.class).size() == 1);
        assertTrue(testworld.getComponents(PauseComponent.class).size() == 1);

        settings.createPause(null);


    }

}