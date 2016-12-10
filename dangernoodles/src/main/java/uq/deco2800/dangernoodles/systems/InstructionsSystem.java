package uq.deco2800.dangernoodles.systems;

import javafx.scene.input.KeyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.dangernoodles.AudioManager;
import uq.deco2800.dangernoodles.ClientManager;
import uq.deco2800.dangernoodles.Game;
import uq.deco2800.dangernoodles.StaticFrameHandler;
import uq.deco2800.dangernoodles.components.PauseComponent;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.inputhandlers.KeyboardHandler;
import uq.deco2800.dangernoodles.inputhandlers.MouseHandler;
import uq.deco2800.singularity.clients.dangernoodle.DangernoodleEventListener;
import uq.deco2800.singularity.common.representations.dangernoodle.SimpleMessage;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Jason on 22/10/16.
 */
public class InstructionsSystem implements System {

    private static StaticFrameHandler staticHandler;
    private static MouseHandler mouseHandler;
    private static KeyboardHandler keyboardHandler;
    private static boolean newGame = true;
    private static int page = 1;
    private static boolean open = true;
    private static boolean ispressed = false;
    private static final double EXITX1 = 850;
    private static final double EXITX2 = 880;
    private static final double EXITY = 165;
    private static final double EXITY2 = 190;
    private static final double NEXTX = 826;
    private static final double NEXTX2 = 880;
    private static final double NEXTY = 328;
    private static final double NEXTY2 = 383;
    private static final double PREVIOUSX = 275;
    private static final double PREVIOUSX2 = 326;
    private static final double PREVIOUSY = 328;
    private static final double PREVIOUSY2 = 383;
    private static String clickSoundLocation = "resources/sounds/click2.wav";

    private static Thread currentThread;

    private static Lock lock = new ReentrantLock();

    private static PauseComponent p;

    private static boolean guard = false;

    private static final String CLASS = InstructionsSystem.class.getName();
    private static final Logger LOGGER = LoggerFactory.getLogger(CLASS);

    public InstructionsSystem(StaticFrameHandler staticHandler,
                              MouseHandler mouseHandler, KeyboardHandler keyboardHandler) {
        InstructionsSystem.mouseHandler = mouseHandler;
        InstructionsSystem.staticHandler = staticHandler;
        InstructionsSystem.keyboardHandler = keyboardHandler;
        if (Game.isMultiplayer()) {
            ClientManager.getClientManager().getRestClient().getCurrentLobby
                    ().addInstructionReleasedListener(new InstructionReleasedListener());
        }
        currentThread = Thread.currentThread();
    }

    public static void resetForTest() {
        newGame = true;
        open = true;
        page = 1;
    }

    /**
     * Is called every tick and runs with the t and dt parameters
     *
     * @param world
     *         is the game world
     * @param t
     *         Time since the beginning of the game
     * @param dt
     *         The delta time, i.e. the time since last frame
     */
    @Override
    public void run(World world, double t, double dt) {
        PauseComponent p = world.getComponents(PauseComponent.class).get(0);
        if (newGame) {
            p.setPaused(true);
        }

    }

    /**
     * Is called every tick while the game is paused and the pausecomponents
     * instructions equals true
     *
     * @param world
     *         is the world the game operates from
     */
    public static void staticRun(World world) throws InterruptedException {
        lock.lock();
        double mX = mouseHandler.getMouseX();
        double mY = mouseHandler.getMouseY();

        p = world.getComponents(PauseComponent.class).get(0);

        if (newGame) {
            p.setPaused(true);
        }

        if (open) {
            staticHandler.addStaticRenderAction((context, handler) -> context.drawImage(handler.loadImage(
                    "INSTRUCTIONS/INSTRUCTIONS_PAGE" + page + ".png"), 250, 150));
            if (keyboardHandler.isPressed(KeyCode.I)) {
                open = false;
                newGame = false;
                // If this is a multiplayer game, need to wait until all
                // players have done reading the instructions. So need to
                // send to the server saying that this client has done its
                // reading.

                if (!Game.isMultiplayer()) {
                    p.setInstructions(false);
                    p.setPaused(false);
                } else {
                    if (!guard) {
                        ClientManager.getClientManager()
                                .getRestClient()
                                .getCurrentLobby()
                                .sendToServer(SimpleMessage.INSTRUCTION_RELEASED);
                        guard = true;
                    }
                }

                try {
                    Thread.sleep(150);
                    page = 1;
                } catch (InterruptedException ignored) {
                    LOGGER.info("Interrupted Exception is found!");
                    Thread.currentThread().interrupt();


                }
            }
            if (keyboardHandler.isPressed(KeyCode.RIGHT)) {
                if (page < 8 && !ispressed) {
                    page++;
                    ispressed = true;
                    AudioManager.playSound(clickSoundLocation, false);
                }
                // Play sound for clicking

            } else if (keyboardHandler.isPressed(KeyCode.LEFT)) {
                if (page > 1 && !ispressed) {
                    page--;
                    ispressed = true;
                    AudioManager.playSound(clickSoundLocation, false);
                }
                // Play sound for clicking

            } else {
                ispressed = false;
            }
        }

        if (mouseHandler.isClicked()) {
            if (mX >= EXITX1 && mX <= EXITX2 && mY >= EXITY && mY <= EXITY2) {
                open = false;
                newGame = false;
                // If this is a multiplayer game, need to wait until all
                // players have done reading the instructions. So need to
                // send to the server saying that this client has done its
                // reading.
                if (!Game.isMultiplayer()) {
                    p.setInstructions(false);
                    p.setPaused(false);
                } else {
                    if (!guard) {
                        ClientManager.getClientManager()
                                .getRestClient()
                                .getCurrentLobby()
                                .sendToServer(SimpleMessage.INSTRUCTION_RELEASED);
                        guard = true;
                    }
                }

                page = 1;
                // Play sound for clicking
                AudioManager.playSound(clickSoundLocation, false);
                mouseHandler.setClicked(false);

            } else if (mX >= NEXTX && mX <= NEXTX2 && mY >= NEXTY && mY <= NEXTY2) {
                if (page < 8) {
                    page++;
                }
                // Play sound for clicking
                AudioManager.playSound(clickSoundLocation, false);
                mouseHandler.setClicked(false);
            }
            if (mX >= PREVIOUSX && mX <= PREVIOUSX2 && mY >= PREVIOUSY && mY <= PREVIOUSY2) {
                if (page > 1) {
                    page--;
                }
                // Play sound for clicking
                AudioManager.playSound(clickSoundLocation, false);
                mouseHandler.setClicked(false);
            }
        }
        lock.unlock();
    }

    /**
     * This method returns the current instructions page
     * Used for testing purposes
     *
     * @return the page number currently visible.
     */

    public int getPage() {
        return page;
    }

    /**
     * This method sets the page the instrutions should show
     * Used for testing purposes
     *
     * @param page
     *         page you wish to set it too
     */

    public static void setPage(int page) {
        InstructionsSystem.page = page;
    }

    /**
     * This class is used to signal the main class that the server has sent
     * back the signal to indicate that players can now start playing the game.
     */
    private class InstructionReleasedListener extends DangernoodleEventListener {
        // Once the server has sent back the start playing signal, start the
        // game for real by unpausing instructions.
        @Override
        public void notifyListener() {
            if (p != null) {
                p.setPaused(false);
                p.setInstructions(false);
            }
        }
    }
}
