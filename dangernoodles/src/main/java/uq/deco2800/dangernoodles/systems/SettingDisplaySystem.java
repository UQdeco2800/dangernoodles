package uq.deco2800.dangernoodles.systems;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uq.deco2800.dangernoodles.AudioManager;
import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.components.CursorComponent;
import uq.deco2800.dangernoodles.components.PauseComponent;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.inputhandlers.KeyboardHandler;
import uq.deco2800.dangernoodles.inputhandlers.MouseHandler;

/**
 * Created by khoi_truong on 2016/10/04.
 * <p>
 * This class is used to handle the displaying of the game settings menu
 * inside the game. Also it does not conform the normal system structure.
 */
public class SettingDisplaySystem implements System {
    // Frame handler
    private static StaticFrameHandler staticHandler;
    private static MouseHandler mouseHandler;
    private static KeyboardHandler keyboardhandler;
    // Position and dimension of the COG
    private static final double COG_X = 325;
    private static final double COG_Y = 15;
    private static final double COG_W = 25;
    private static final double COG_H = 25;
    // Position and dimension of the panel
    private static final double MAIN_PANEL_X = 300;
    private static final double MAIN_PANEL_Y = 100;
    private static final double MAIN_PANEL_W = 500;
    private static final double MAIN_PANEL_H = 500;
    // Position and dimension of the exit button
    private static final double EXIT_BUTTON_X = 675;
    private static final double EXIT_BUTTON_Y = 540;
    private static final double EXIT_BUTTON_W = 100;
    private static final double EXIT_BUTTON_H = 40;
    // Private field to store the image of the COGwheel
    private static final String COG_IMAGE = "game.settings/settings-COG.png";
    private static final String ON_SWITCH = "In_game_option_panel/on.png";
    // Private field to store the position of muting switches
    private static final double SOUND_MUTE_SWITCH_X = EXIT_BUTTON_X-350;
    private static final double SOUND_MUTE_SWITCH_Y = EXIT_BUTTON_Y;
    private static final double MUSIC_MUTE_SWITCH_X = EXIT_BUTTON_X-300;
    private static final double MUSIC_MUTE_SWITCH_Y = SOUND_MUTE_SWITCH_Y;
    private static final double MUTE_SWITCH_W = 50;
    private static final double MUTE_SWITCH_H = 30;
    // Field to check if sound for highlighting COGwheel has been played.
    private boolean lastHoveredCog = false;

    /**
     * Default constructor for the system
     *
     * @param staticHandler
     *         static frame handler to handle the drawing task
     * @param mouseHandler
     *         mouse handler to handle mouse-related events
     *
     * @throws NullPointerException
     *         if staticHandler or mouseHandler is null
     * @require staticHandler != null && mouseHandler != null
     * @ensure an instance of this class is constructed.
     */
    public SettingDisplaySystem(StaticFrameHandler staticHandler,
                                MouseHandler mouseHandler, KeyboardHandler keyHandler) {
        if (staticHandler == null || mouseHandler == null) {
            throw new NullPointerException("Static frame handler and " +
                    "mouseHandler cannot be null");
        }
        SettingDisplaySystem.staticHandler = staticHandler;
        SettingDisplaySystem.mouseHandler = mouseHandler;
        this.keyboardhandler = keyHandler;
    }

    /**
     * Is called every tick and runs with the t and dt parameters
     *
     * @param world
     * @param t
     *         Time since the beginning of the game
     * @param dt
     *         The delta time, i.e. the time since last frame
     */
    @Override
    public void run(World world, double t, double dt) {
        double mX = mouseHandler.getMouseX();
        double mY = mouseHandler.getMouseY();


        // Highlight box if mouse is hovering
        if (mX >= COG_X - 5 && mX <= COG_X + COG_W + 5 &&
                mY >= COG_Y - 5 && mY <= COG_Y + COG_H + 5) {
        	// Play sound for hovering mouse over cog if not already played.
        	if (!lastHoveredCog) {
        		lastHoveredCog = true;
        		AudioManager.playSound("resources/sounds/drop1.wav", false);
        	}
        	
            staticHandler.addStaticRenderAction((context, handler) -> {
                // Button around the COG image
                context.setFill(Color.RED);
                context.fillRoundRect(COG_X - 7, COG_Y - 7, COG_W + 14, COG_H +
                        14, 2, 2);
            });
        } else {
        	// Cog not hovered over, reset for next time.
            lastHoveredCog = false;
        }

        // Display the COG image
        staticHandler.addStaticRenderAction((context, handler) -> {
            // Button around the COG image
            context.setFill(Color.LIGHTGREY);
            context.fillRoundRect(COG_X - 5, COG_Y - 5, COG_W + 10, COG_H + 10, 2, 2);
            context.strokeRoundRect(COG_X - 5, COG_Y - 5, COG_W + 10, COG_H + 10, 2, 2);
            context.drawImage(staticHandler.loadImage(COG_IMAGE), COG_X, COG_Y, COG_W, COG_H);
        });
        
        // Get the "global" pause component
        PauseComponent p = world.getComponents(PauseComponent.class).get(0);

        // Once the mouse has clicked the COGwheel, proceeding on pausing the
        // game. The staticRun method will taken over from here.
        if (mouseHandler.isClicked()) {
            if (mX >= COG_X - 5 && mX <= COG_X + COG_W + 5 &&
                    mY >= COG_Y - 5 && mY <= COG_Y + COG_H + 5) {
                p.setPaused(true);
                // Play sound for clicking cog.
                AudioManager.playSound("resources/sounds/click2.wav", false);
            }
            
            if (p.isPaused()) {
                mouseHandler.setClicked(false);
                // consume the click for the cursor component as well
                CursorComponent cursor = world.getComponents(
                        CursorComponent.class).get(0);
                cursor.consumeClick();
            }
        }
    }

    /**
     * Static method which is called to handle setting menu once the game is
     * paused. Since the pausing does not entirely pause the whole game with
     * some loop, instead the processing of the world is shifted toward this
     * method and all other aspects of the game is effectively "paused".
     *
     * @param world
     *         game parent world
     *
     * @throws NullPointerException
     *         if world is null
     * @require world != null
     * @ensure setting panel will be drawn and handled.
     */
    public static void staticRun(World world) {
        double mX = mouseHandler.getMouseX();
        double mY = mouseHandler.getMouseY();

        // Display the COG image
        staticHandler.addStaticRenderAction((context, handler) -> {
            // Main panel
            context.setFill(Color.LIGHTGREY);
            context.fillRoundRect(MAIN_PANEL_X, MAIN_PANEL_Y, MAIN_PANEL_W,
                    MAIN_PANEL_H, 2, 2);
            context.strokeRoundRect(MAIN_PANEL_X, MAIN_PANEL_Y, MAIN_PANEL_W,
                    MAIN_PANEL_H, 2, 2);
        });

        // Highlight box if mouse is hovering
        if (mX >= EXIT_BUTTON_X && mX <= EXIT_BUTTON_X + EXIT_BUTTON_W &&
                mY >= EXIT_BUTTON_Y && mY <= EXIT_BUTTON_Y + EXIT_BUTTON_H) {
            staticHandler.addStaticRenderAction((context, handler) -> {
                // Button around the exit button
                context.setFill(Color.RED);
                context.fillRoundRect(EXIT_BUTTON_X - 2, EXIT_BUTTON_Y - 2,
                        EXIT_BUTTON_W + 4, EXIT_BUTTON_H + 4, 2, 2);
            });
        }
        else if(mX >= 335 && mX <= 335 + 430 &&
                mY >= 125 && mY <= 125 + 70){
            staticHandler.addStaticRenderAction((context, handler) -> {
                //Play music button
                context.setFill(Color.RED);
                context.fillRoundRect(335 - 2, 125 - 2,
                        430 + 4, 70 + 4, 2, 2);
            });
        }
        else if(mX >= 335 && mX <= 335 + 430 &&
                mY >= 205 && mY <= 205 + 70){
            staticHandler.addStaticRenderAction((context, handler) -> {
                //stop music button
                context.setFill(Color.RED);
                context.fillRoundRect(335 - 2, 205 - 2,
                        430 + 4, 70 + 4, 2, 2);
            });
        }
        else if(mX >= 335 && mX <= 335 + 430 &&
                mY >= 285 && mY <= 285 + 70){
            staticHandler.addStaticRenderAction((context, handler) -> {
                // Reset Game Glow effect button
                context.setFill(Color.RED);
                context.fillRoundRect(335 - 2, 285 - 2,
                        430 + 4, 70 + 4, 2, 2);
            });
        }

        else if(mX >= 335 && mX <= 335 + 430 &&
                mY >= 365 && mY <= 365 + 70) {
            staticHandler.addStaticRenderAction((context, handler) -> {
                // Reset weapon glow effect button
                context.setFill(Color.RED);
                context.fillRoundRect(335 - 2, 365 - 2,
                        430 + 4, 70 + 4, 2, 2);
            });

        }
        else if(mX >= EXIT_BUTTON_X-350 && mX <= EXIT_BUTTON_X-350 + 50 &&
                mY >= EXIT_BUTTON_Y && mY <= EXIT_BUTTON_Y + 30) {
            staticHandler.addStaticRenderAction((context, handler) -> {
                // Switch on glow effect
                context.setFill(Color.LIGHTBLUE);
                context.fillRoundRect(EXIT_BUTTON_X-350 - 2, EXIT_BUTTON_Y - 2,
                        50 + 4, 30 + 4, 2, 2);
            });
        }
        else if(mX >= EXIT_BUTTON_X-300 && mX <= EXIT_BUTTON_X-300 + 50 &&
                mY >= EXIT_BUTTON_Y && mY <= EXIT_BUTTON_Y + 30) {
            staticHandler.addStaticRenderAction((context, handler) -> {
                // Switch off glow effect
                context.setFill(Color.LIGHTBLUE);
                context.fillRoundRect(EXIT_BUTTON_X -350- 2, EXIT_BUTTON_Y - 2,
                        50 + 4, 30 + 4, 2, 2);
            });

        }


        // Display the exit button
        staticHandler.addStaticRenderAction((context, handler) -> {
            // Main panel
            context.setFill(Color.LIGHTGRAY);
            context.fillRoundRect(EXIT_BUTTON_X, EXIT_BUTTON_Y, EXIT_BUTTON_W,
                    EXIT_BUTTON_H, 2, 2);
            context.strokeRoundRect(EXIT_BUTTON_X, EXIT_BUTTON_Y, EXIT_BUTTON_W,
                    EXIT_BUTTON_H, 2, 2);
            context.fillRoundRect(340-5, 120+5, 430, 70, 5, 5);
            context.strokeRoundRect(340-5, 120+5, 430, 70, 5, 5);
            context.fillRoundRect(340-5, 200+5, 430, 70, 5, 5);
            context.strokeRoundRect(340-5,  200+5, 430, 70, 5, 5);
            context.fillRoundRect(340-5, 280+5, 430, 70, 5, 5);
            context.strokeRoundRect(340-5, 280+5, 430, 70, 5, 5);
            context.fillRoundRect(340-5, 360+5, 430, 70, 5, 5);
            context.strokeRoundRect(340-5,  360+5, 430, 70, 5, 5);
            context.setFill(Color.RED);
            context.setFont(new Font("Verdana", 17));
            context.fillText("Exit", EXIT_BUTTON_X + 35, EXIT_BUTTON_Y + 26);
            context.fillText("Play Music",490,170);
            context.fillText("Stop Music",490,245);
            context.fillText("Reset Game",490,326);
            context.fillText("Reset Weapon",490,405);
            context.drawImage(staticHandler.loadImage(ON_SWITCH), EXIT_BUTTON_X-350, EXIT_BUTTON_Y, 50, 30);

        });



        // Once the mouse has clicked the outside the panel, stop the pause
        // and resume the game.
        if (mouseHandler.isClicked()) {
            if (!(mX >= MAIN_PANEL_X && mX <= MAIN_PANEL_X + MAIN_PANEL_W &&
                    mY >= MAIN_PANEL_Y && mY <= MAIN_PANEL_Y + MAIN_PANEL_H)) {
                PauseComponent p = world.getComponents(PauseComponent.class).get(0);
                p.setPaused(false);
                mouseHandler.setClicked(false);
            }
            
            // Checking if the exit button has been pushed
            if (mX >= EXIT_BUTTON_X && mX <= EXIT_BUTTON_X + EXIT_BUTTON_W &&
                    mY >= EXIT_BUTTON_Y && mY <= EXIT_BUTTON_Y + EXIT_BUTTON_H) {
                PauseComponent p = world.getComponents(PauseComponent.class).get(0);
                p.setPaused(false);
                mouseHandler.setClicked(false);
            }
            
            if (mX >= SOUND_MUTE_SWITCH_X && mX <= SOUND_MUTE_SWITCH_X + MUTE_SWITCH_W &&
                    mY >= SOUND_MUTE_SWITCH_Y && mY <= SOUND_MUTE_SWITCH_Y + MUTE_SWITCH_H){
            	AudioManager.toggleMuteSound();
            	mouseHandler.setClicked(false);
            }
            
            if (mX >= MUSIC_MUTE_SWITCH_X && mX <= MUSIC_MUTE_SWITCH_X + MUTE_SWITCH_W &&
                    mY >= MUSIC_MUTE_SWITCH_Y && mY <= MUSIC_MUTE_SWITCH_Y + MUTE_SWITCH_H){
            	AudioManager.toggleMuteMusic();
            	mouseHandler.setClicked(false);
            }
        } else if (keyboardhandler.isPressed(KeyCode.P) || keyboardhandler.isPressed(KeyCode.ESCAPE)) {
            PauseComponent p = world.getComponents(PauseComponent.class).get(0);
            p.setPaused(false);
        }
    }
    

    public boolean isLastHoveredCog() {
        return lastHoveredCog;
    }}