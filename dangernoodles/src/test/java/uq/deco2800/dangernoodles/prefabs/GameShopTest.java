package uq.deco2800.dangernoodles.prefabs;

import org.junit.Test;

import uq.deco2800.dangernoodles.components.NameComponent;
import uq.deco2800.dangernoodles.components.displays.ShopDisplayComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.components.ShopComponent;

import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by minhnguyen on 12/09/2016.
 */
public class GameShopTest {


    // Testing shop instance creation and its components
    @Test
    public void createShop() throws Exception {
        World miniWorld = new World(0, 0);
        Entity shop = GameShop.createShop(miniWorld);

        Optional<ShopComponent> store = miniWorld.getComponent(shop, ShopComponent.class);
        Optional<ShopDisplayComponent> sdc = miniWorld.getComponent(shop, ShopDisplayComponent.class);
        Optional<NameComponent> name = miniWorld.getComponent(shop, NameComponent.class);

        assertTrue(store.isPresent());
        assertTrue(sdc.isPresent());
        assertTrue(name.isPresent());

    }

    public void componentCheck() {

    }
}