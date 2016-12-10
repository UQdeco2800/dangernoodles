package uq.deco2800.dangernoodles.components;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by minhnguyen on 18/09/2016.
 */
public class NameComponentTest {

    NameComponent nameComp;

    /**
     * Testing good initialized name and getName
     */
    @Test
    public void getGoodName(){
        nameComp = new NameComponent("Snek");
        String name = nameComp.getName();
        assertTrue("Snek".equals(name));
    }

    /**
     * Testing bad initialized name
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void getBadName() {
        nameComp = new NameComponent(null);
    }

    /**
     * Testing setting new name
     */
    @Test
    public void setGoodName() {
        nameComp = new NameComponent("Snek");
        nameComp.setName("snek");
        String name = nameComp.getName();
        assertTrue("snek".equals(name));
    }

    /**
     *  Testing setting new bad name
     */
    @Test(expected = NullPointerException.class)
    public void setBadName() {
        nameComp = new NameComponent("Snek");
        nameComp.setName(null);
    }

}