package uq.deco2800.dangernoodles.camera;

import uq.deco2800.dangernoodles.windowhandlers.Camera;
import uq.deco2800.dangernoodles.windowhandlers.CameraEnum;

import static org.junit.Assert.*;

/**
 * Created by Jason on 12/09/2016.
 */
public class CameraTest {
    CameraEnum direction = CameraEnum.DONT_MOVE;
    private Camera cam = new Camera(0, 0);
    @org.junit.Test
    public void testInit(){
        assertTrue(cam.getX() == 0);
        assertTrue(cam.getY() == 0);
        assertTrue(cam.getSaveX() == 0);
        assertTrue(cam.getSaveY() == 0);
        cam.setX(5);
        cam.setY(6);
        assertTrue(cam.getX() == 5);
        assertTrue(cam.getY() == 6);
        assertTrue(cam.getSaveX() == 0);
        assertTrue(cam.getSaveY() == 0);
    }

    @org.junit.Test
    public void onTickTest(){
        //case UP
        direction = CameraEnum.UP;
        double oldX = cam.getX();
        double oldY = cam.getY();
        cam.onTick(direction);
        assertTrue(cam.getX() == oldX + 0);
        assertTrue(cam.getY() == oldY + 5);
        assertTrue(cam.getSaveX() == 0);
        assertTrue(cam.getSaveY() == cam.getY());
        //Case down
        direction = CameraEnum.DOWN;
        oldX = cam.getX();
        oldY = cam.getY();
        cam.onTick(direction);
        assertTrue(cam.getX() == oldX + 0);
        assertTrue(cam.getY() == oldY - 5);
        assertTrue(cam.getSaveX() == 0);
        assertTrue(cam.getSaveY() == cam.getY());
        //Case Left
        direction = CameraEnum.LEFT;
        oldX = cam.getX();
        oldY = cam.getY();
        cam.onTick(direction);
        assertTrue(cam.getX() == oldX + 5);
        assertTrue(cam.getY() == oldY + 0);
        assertTrue(cam.getSaveX() == cam.getX());
        assertTrue(cam.getSaveY() == cam.getY());
        //Case Right
        direction = CameraEnum.RIGHT;
        oldX = cam.getX();
        oldY = cam.getY();
        cam.onTick(direction);
        assertTrue(cam.getX() == oldX - 5);
        assertTrue(cam.getY() == oldY + 0);
        assertTrue(cam.getSaveX() == cam.getX());
        assertTrue(cam.getSaveY() == cam.getY());
        //Case Top Right
        direction = CameraEnum.TOP_RIGHT;
        oldX = cam.getX();
        oldY = cam.getY();
        cam.onTick(direction);
        assertTrue(cam.getX() == oldX - 5);
        assertTrue(cam.getY() == oldY + 5);
        assertTrue(cam.getSaveX() == cam.getX());
        assertTrue(cam.getSaveY() == cam.getY());
        //Case Top Left
        direction = CameraEnum.TOP_LEFT;
        oldX = cam.getX();
        oldY = cam.getY();
        cam.onTick(direction);
        assertTrue(cam.getX() == oldX + 5);
        assertTrue(cam.getY() == oldY + 5);
        assertTrue(cam.getSaveX() == cam.getX());
        assertTrue(cam.getSaveY() == cam.getY());
        //Case Bottom Left
        direction = CameraEnum.BOTTOM_LEFT;
        oldX = cam.getX();
        oldY = cam.getY();
        cam.onTick(direction);
        assertTrue(cam.getX() == oldX + 5);
        assertTrue(cam.getY() == oldY - 5);
        assertTrue(cam.getSaveX() == cam.getX());
        assertTrue(cam.getSaveY() == cam.getY());
        //Case Bottom Right
        direction = CameraEnum.BOTTOM_RIGHT;
        oldX = cam.getX();
        oldY = cam.getY();
        cam.onTick(direction);
        assertTrue(cam.getX() == oldX - 5);
        assertTrue(cam.getY() == oldY - 5);
        assertTrue(cam.getSaveX() == cam.getX());
        assertTrue(cam.getSaveY() == cam.getY());
        //Case Dont Move
        direction = CameraEnum.DONT_MOVE;
        cam.onTick(direction);
        assertTrue(cam.getX() == cam.getSaveX());
        assertTrue(cam.getY() == cam.getSaveY());
    }


}