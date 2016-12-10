package uq.deco2800.dangernoodles.systems.weapons;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.paint.Color;
import uq.deco2800.dangernoodles.FrameHandler;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.SpriteComponent;
import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.components.weapons.WeaponComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;

public class WeaponPowerBarSystem implements System {

    private static String thisClass = WeaponSystem.class.getName();
    private final Logger logger = LoggerFactory.getLogger(thisClass);
    private FrameHandler handler;

    /**
     * Should have a higher priority than most other rendering systems
     * 
     * @param handler The handler through which drawing occurs
     */
    public WeaponPowerBarSystem(FrameHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run(World world, double t, double dt) {
        List<WeaponComponent> weapons = 
                world.getComponents(WeaponComponent.class);
        // just leave if there aren't any weapons
        if (weapons == null || weapons.isEmpty()) {
            return;
        }
        for (WeaponComponent weapon : weapons) {
            Optional<TurnComponent> turnOpt = world.getComponent(
                    weapon.getParent(), TurnComponent.class);
            if (!turnOpt.isPresent()) {
                logger.warn("This player doesn't have a turn component");
                continue; // if it isn't there just skip
            }
            TurnComponent turn = turnOpt.get();
            // Check if it's this noodle's turn
            if (turn.getTurn()) {
                // check if this weapon is firing (and it's a powered weapon
                if (weapon.isFiring() && weapon.getDefinition().isPowered()) {
                    drawWeaponPower(weapon, world);
                }
            }
        }
    }
    
    /**
     * Draws the power bar for the weapon
     * NOTE: if testing flag is set, doesn't execute
     * 
     * @param weapon WeaponComponent associated with the weapon whose power is being drawn
     * @param world World in which to draw/weapon exists
     */
    private void drawWeaponPower(WeaponComponent weapon, World world) {
        // get the values you need
        Entity player = weapon.getParent();
        PositionComponent playerPos = world.getComponent(player, 
                PositionComponent.class).get();
        SpriteComponent playerSprite = world.getComponent(player, 
                SpriteComponent.class).get();
        
        double x;
        double y;
        double width;
        double height;
        
        // We're going to draw a [10 x sprite width] rectangle 10px below the
        // player with a 1/4 over hang each side

        x = playerPos.getX() - 0.25 * playerSprite.getWidth();
        y = playerPos.getY() + playerSprite.getHeight() + 10;
        width = 1.5 * playerSprite.getWidth();
        height = 10;
        
        // draw the power bar background
        handler.addRenderAction((c, h) -> {
            c.setFill(Color.BLACK);
            c.fillRect(x, y, width, height);
        });
        
        // We're going to draw a rectangle that is 1px inside of the other
        // with a length proportional to the power of the weapon
        double power;
        double powerWidth;
        double powerHeight;

        int red;
        int green;
        Color color;
        
        power = weapon.getPower();
        powerWidth = width * (power / 100) - 4; // take off 2 px at each end
        powerHeight = height - 4;
        
        // set the color values
        red = (int)(255 * (power / 100));
        green = (int)(255 * (1 - power / 100));
        color = Color.rgb(red, green, 0);
        
        // draw the power bar
        handler.addRenderAction((c, h) -> {
            c.setFill(color);
            c.fillRect(x + 2, y + 2, powerWidth, powerHeight);
        });
    }
}
