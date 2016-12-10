package uq.deco2800.dangernoodles.systems;

import javafx.scene.input.KeyCode;
import uq.deco2800.dangernoodles.components.*;
import uq.deco2800.dangernoodles.components.displays.ShopDisplayComponent;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.GameShop;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;
import uq.deco2800.dangernoodles.prefabs.PlayerEntities;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 * Created by Jason on 19/10/16.
 */
public class CursorSystemTest {

    World testworld = new World(0, 0);
    private CursorSystem cursorSystem = new CursorSystem();


    @Test
    public void cursorTest() {
        Entity shop = GameShop.createShop(this.testworld);
        testworld.addSystem(cursorSystem, 0);

        this.testworld.createEntity().addComponent(new NameComponent("Cursor"))
                .addComponent(new PositionComponent(0, 0))
                .addComponent(new RectangleComponent(30, 30))
                .addComponent(new CollisionComponent(false, CollisionComponent.shape.RECTANGLE))
                .addComponent(new CursorComponent());

        Entity player = PlayerEntities.createPlayer(testworld,
                NoodleEnum.NOODLE_TANK, false, TeamEnum.TEAM_ALPHA, 0, 1);

        CursorComponent c = testworld.getComponents(CursorComponent.class).get(0);
        CollisionComponent col = testworld.getComponent(c.getEntity(), CollisionComponent.class).get();

        c.setClicked(true);
        col.addCollision(player);
        cursorSystem.run(testworld, 0, 0);

        assertEquals(testworld.getComponent(player, PlayerComponent.class).get().isSelected(), true);
        assertEquals(testworld.getComponent(shop, ShopDisplayComponent.class)
                .get().isShowingDisplay(), false);
        c.setClicked(true);
        col.addCollision(shop);
        cursorSystem.run(testworld, 0, 0);

        assertEquals(testworld.getComponent(shop, ShopDisplayComponent.class)
                .get().isShowingDisplay(), true);
        assertEquals(testworld.getComponent(player, PlayerComponent.class).get().isSelected(), false);


        c.setClicked(false);
        cursorSystem.run(testworld, 0, 0);


    }

}