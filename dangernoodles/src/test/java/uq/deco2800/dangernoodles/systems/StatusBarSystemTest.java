package uq.deco2800.dangernoodles.systems;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import javafx.scene.paint.Color;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.StatusBarComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;

public class StatusBarSystemTest {
	World miniWorld = new World(0, 0);
	
	 /**
     * Tests setting bar to 0
     */
    @Test
    public void UpdateBarTest() {
    	StatusBarSystem status = new StatusBarSystem(null, null);
    	StatusBarComponent bar = new StatusBarComponent("test", 100, 10, 100, Color.GREEN, Color.RED,
    			false, new PositionComponent(0, 0));
    	
    	bar.setValue(0);
    	
    	assertEquals(bar.getCurrentColor(), bar.getStartColor());
    	
    	miniWorld.createEntity().addComponent(bar);
    	status.run(miniWorld, 0, 0);
    	
    	assertEquals(bar.getCurrentColor(), bar.getEndColor());
    }
}
