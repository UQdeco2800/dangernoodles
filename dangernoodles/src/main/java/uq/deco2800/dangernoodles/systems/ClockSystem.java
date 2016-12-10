package uq.deco2800.dangernoodles.systems;


import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;

/**
 * Created by Park on 10/12/2016.
 */
public class ClockSystem implements System {
    private StaticFrameHandler staticFrame;

    /**
     * Creates a system that renders the amount of time that has elapsed. Game time is rendered in the bottom left hand
     * of the screen
     *
     * @param staticFrame
     *          Frame handler to render static objects onto the canvas.
     */
    public ClockSystem(StaticFrameHandler staticFrame) {
        this.staticFrame = staticFrame;
    }

    /**
     * Retrieves the time, in minutes and seconds, that has elapsed and renders it on the screen.
     *
     * @param world World object
     * @param t     Time since the beginning of the game
     * @param dt    Delta time - changing time
     */
    @Override
    public void run(World world, double t, double dt) {
        // Convert time elapsed in game to minutes and seconds
        int tInt = (int) Math.round(t);
        int min = tInt / 60 % 60;
        int sec = tInt % 60;
        String timeDisplay = "Game time: " + minStr(min) + " min " + secStr(sec) + " sec";

        // Render the base panel for the clock
        staticFrame.addStaticRenderAction((context, frameHandler) -> {
            context.setFill(Color.LIGHTGREY);
            context.fillRoundRect(0, 675, 290, 25, 5, 5);
            context.strokeRoundRect(0, 675, 290, 270, 5, 5);
        });

        // Render the elapsed game time on the panel
        staticFrame.addStaticRenderAction((context, frameHandler) -> {
            context.setFill(Color.BLACK);
            context.setFont(Font.font("Verdana", 22));
            context.fillText(timeDisplay, 0, 700);
            context.setFill(Color.color(0, 0, 0, 0));
        });
    }

    /**
     * Changes the integer value of seconds to string
     *
     * @param sec
     *          integer representing the seconds that has elapsed
     * @return  String representation of the seconds that has elapsed
     */
    public String secStr(int sec) {
        return String.valueOf(sec);
    }

    /**
     * Changes the integer value of minutes to string
     *
     * @param min
     *          integer representing the minutes that has elapsed
     * @return  String representation of the minutes that has elapsed
     */
    public String minStr(int min) { return String.valueOf(min); }
}