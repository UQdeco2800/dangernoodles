package uq.deco2800.dangernoodles.components.game.settings;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Created by Jason on 23/10/16.
 */
public class SettingDisplayComponentTest {

    SettingDisplayComponent settingDisplayComponent = new SettingDisplayComponent();

    @Test
    public void isDisplaying(){
        assertEquals(settingDisplayComponent.isDisplaying(), false);

    }

    @Test
    public void setDisplaying(){
        assertEquals(settingDisplayComponent.isDisplaying(), false);
        settingDisplayComponent.setDisplaying(true);
        assertEquals(settingDisplayComponent.isDisplaying(), true);
        settingDisplayComponent.setDisplaying(false);
        assertEquals(settingDisplayComponent.isDisplaying(), false);

    }

}