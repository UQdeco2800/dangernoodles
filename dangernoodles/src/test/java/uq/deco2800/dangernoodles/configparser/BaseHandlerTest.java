package uq.deco2800.dangernoodles.configparser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test of the BaseHandler using WeaponHandler, because BaseHandler is
 * an abstract class.
 * 
 * @author torusse
 *
 */
public class BaseHandlerTest {
    
    /**
     * Test the getter methods of the BaseHandler
     */
    @Test
    public void testGetters() {
        WeaponHandler baseHandler = new WeaponHandler(null);
        assertEquals("Create blank ID", baseHandler.getId(), 0);
        assertEquals("Create weapon entry type", 
                baseHandler.getEntryTypeName(), "weapon");
        assertEquals("Create taglist", 
                baseHandler.getTaglist().toString(), "[name, category, "
                        + "spritesheet, HEIGHT, WIDTH, powered, "
                        + "makesProjectiles, numOfProjectiles, "
                        + "projectileType]");
        assertEquals("Create tags", 
                baseHandler.getTags().toString(), "{projectileType=false, "
                        + "powered=false, numOfProjectiles=false, name=false, "
                        + "WIDTH=false, makesProjectiles=false, "
                        + "category=false, HEIGHT=false, spritesheet=false}");
    }

}
