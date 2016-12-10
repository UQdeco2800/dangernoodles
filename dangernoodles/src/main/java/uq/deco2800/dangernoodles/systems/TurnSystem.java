package uq.deco2800.dangernoodles.systems;

import javafx.scene.paint.Color;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import uq.deco2800.dangernoodles.AudioManager;
import uq.deco2800.dangernoodles.FrameHandler;

import uq.deco2800.dangernoodles.StaticFrameHandler;

import uq.deco2800.dangernoodles.components.ShopComponent;
import uq.deco2800.dangernoodles.components.displays.ShopDisplayComponent;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.ecs.Entity;

import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.components.PlayerComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.components.stats.HealthComponent;
import uq.deco2800.dangernoodles.components.stats.ManaComponent;

public class TurnSystem implements System {
    private FrameHandler handler;
    private StaticFrameHandler staticHandler;
    private World world;
    private Queue<Queue<TurnComponent>> turnQueue;

    public static final int TURN_DURATION = 15;    // seconds
    private static final int TURN_DELAY = 3;
    private static final int WARNING_TIME = 5;
    private TeamEnum winnerPointer;
    private boolean delayCurrentTurn;
    private double delayStartTime;
    private HealthComponent currentPlayerHealth;
    private TurnComponent currentTurn;
    private String fontVerdana = "Verdana";

    private boolean testing; //Set if running tests, stops timers

    // Turn time remaining when warning sound was last played.
    private int lastWarningTime = 0;

    public TurnSystem(World world, FrameHandler handler, StaticFrameHandler staticHandler, TeamEnum winnerPointer) {
        this.handler = handler;
        this.staticHandler = staticHandler;
        this.world = world;
        this.winnerPointer = winnerPointer;
        this.turnQueue = new LinkedBlockingQueue<>();

        HashMap<Integer, Queue<TurnComponent>> teams = new HashMap<>();
        List<TurnComponent> turns = world.getComponents(TurnComponent.class);

        // Find and build each team by players' teamId
        for (TurnComponent turn : turns) {
            Optional<PlayerComponent> player =
                    world.getComponent(turn.getEntity(), PlayerComponent.class);
            if (player.isPresent()) {
                int teamId = player.get().getTeamId();
                teams.putIfAbsent(teamId, new LinkedBlockingQueue<>());
                teams.get(teamId).add(turn);
            }
        }

        this.turnQueue.addAll(teams.values());

        //winnerPointer should only be null when testing
        testing = winnerPointer == null;
        delayStartTime = 0;
        this.startNextTurn(world, 0);
        if (testing) {
            currentTurn.setTurn(0);
            delayCurrentTurn = false;
        } else {
            delayCurrentTurn = true;
        }
    }

    @Override
    public void run(World world, double t, double dt) {
        countDownSound(t);
        if (handler != null) {
            drawTimer(t);
        }

        if (!delayCurrentTurn && isTurnOver(t)) {
            this.endTurn(t);
            delayStartTime = t;
            delayCurrentTurn = true;

            Optional<PositionComponent> noodlePosition;
            noodlePosition = world.getComponent(this.getCurrentTurn().getEntity(), PositionComponent.class);
            if (noodlePosition.isPresent()) {
                double xOffset = noodlePosition.get().getX();
                double yOffset = noodlePosition.get().getY();
                if (xOffset > 0 && yOffset > 0) {
                    handler.getCamera().saveXY(-xOffset + world.getWidth() / 2, -yOffset + world.getHeight() / 2);
                }
            }

            this.startNextTurn(world, t);

            //Check for winner, unless testing
            if (!testing && checkForWinner(world)) {
                return;
            } else if (testing && !turnInQueue()) {
                delayCurrentTurn = false;
                currentTurn.setTurn(t);
            }

            AudioManager.playSound("resources/sounds/chime1.wav", false);
        }

        if (delayCurrentTurn && t - delayStartTime >= TURN_DELAY) {
            delayCurrentTurn = false;
            currentTurn.setTurn(t);
        }

        if (!turnInQueue()) {
            Optional<PositionComponent> position =
                    world.getComponent(this.getCurrentTurn().getEntity(),
                            PositionComponent.class);
            if (position.isPresent() && handler != null) {
                drawCurrentPointer(position.get());
            }
        }
    }

    /**
     * Returns the current turn component. Can be used to retrieve the noodle entity.
     *
     * @return TurnComponent that occupies the current turn.
     */
    private TurnComponent getCurrentTurn() {
        return this.turnQueue.element().element();
    }

    /**
     * Determines whether there are any turns left in any Queue, hence determining
     * whether there are any noodles left in the game.
     * @return True if there is a Noodle remaining in the game. False otherwise
     */
    private boolean turnInQueue() {
        return this.turnQueue.isEmpty() || this.turnQueue.element().isEmpty();
    }

    /**
     * Returns the time remaining for the current turn entity.
     *
     * @param time Time value for comparison (generally time since the game
     *             started.)
     * @return Time remaining for current turn
     */
    private int getTimeRemaining(double time) {
        if (delayCurrentTurn) {
            return TURN_DURATION;
        }
        return (int) (TURN_DURATION + 1 - (time - currentTurn.getTurnStartTime()));
    }

    /**
     * Checks whether the current players turn is over.
     *
     * @param currentTime Time since the game started.
     * @return true if turn is over.
     */
    private boolean isTurnOver(double currentTime) {
        TurnComponent turn = this.turnQueue.element().element();
        Optional<HealthComponent> currentPlayerHealth =
                world.getComponent(turn.getEntity(), HealthComponent.class);
        Optional<ManaComponent> currentPlayerMana =
                world.getComponent(turn.getEntity(), ManaComponent.class);

        return !turn.getTurn() ||
                !currentPlayerHealth.isPresent() ||
                currentPlayerHealth.get().getHealth() <= 0 ||
                !currentPlayerMana.isPresent() ||
                currentPlayerMana.get().getMana() <= 0 ||
                currentTime - turn.getTurnStartTime() > TURN_DURATION;
    }

    /**
     * Sets the turn of the next player.
     *
     * @param world the world instance
     * @param startTime Time (relative to the game) that the turn commenced.
     */
    private void startNextTurn(World world, double startTime) {

        // set next players turn
        if (this.turnQueue.isEmpty()) {
            return;
        }

        if (this.turnQueue.element().isEmpty()) {
            this.turnQueue.remove();
            startNextTurn(world, startTime);
            return;
        }

        currentTurn = this.turnQueue.element().element();

        Optional<HealthComponent> healthOpt;
        healthOpt = world.getComponent(currentTurn.getEntity(), HealthComponent.class);

        if (healthOpt.isPresent()) {
            this.currentPlayerHealth = healthOpt.get();
            // if noodle is dead, skip its turn
            if (this.currentPlayerHealth.getHealth() <= 0) {
                this.turnQueue.element().remove();
                startNextTurn(world, startTime);
            }
        } else {
            this.turnQueue.element().remove();
            startNextTurn(world, startTime);
        }
    }

    /**
     * Removes dead noodles and teams
     */
    private void removeDead() {
        for (int teamIndex = 0; teamIndex < this.turnQueue.size(); teamIndex++) {
            Queue<TurnComponent> team = this.turnQueue.remove();
            for (int playerIndex = 0; playerIndex < team.size(); playerIndex++) {
                TurnComponent player = team.remove();
                Optional<HealthComponent> health =
                        world.getComponent(player.getEntity(), HealthComponent.class);
                if (health.isPresent() && health.get().getHealth() > 0) {
                    team.add(player);
                }
            }
            if (!team.isEmpty()) {
                this.turnQueue.add(team);
            }
        }
    }

    /**
     * Ends the current players turn by dequeueing the turn, and then enqueueing it again (if the noodle is alive)
     */
    private void endTurn(double t) {
        // remove turn (as currentTurn)
        TurnComponent currentTurn = this.turnQueue.element().remove();
        currentTurn.incrementTurnCount();
        currentTurn.clearTurn(t);

        Optional<HealthComponent> currentPlayerHealth =
                world.getComponent(currentTurn.getEntity(), HealthComponent.class);

        // add player back on the queue if not dead
        if (currentPlayerHealth.isPresent() && currentPlayerHealth.get().getHealth() > 0) {
            this.turnQueue.element().add(currentTurn);
            this.turnQueue.add(this.turnQueue.remove());
        } else if (this.turnQueue.size() >= 2 && this.turnQueue.element().isEmpty()) {
            this.turnQueue.remove();
        } else {
            this.turnQueue.add(this.turnQueue.remove());
        }


    }

    /**
     * Checks if the game has ended.
     *
     * @param world is the game world
     * @return boolean that indicates if the game has ended.
     */
    private boolean checkForWinner(World world) {
        //ensure no dead noodles are present
        removeDead();

        // If all teams are dead, last player to have a turn wins
        if (this.turnQueue.isEmpty()) {
            Optional<PlayerComponent> player =
                    world.getComponent(currentPlayerHealth.getEntity(), PlayerComponent.class);
            if (player.isPresent()) {
                this.winnerPointer.copyTeam(player.get().getTeam());
            }
            return true;
        }

        // If only one team remains, current player wins
        if (this.turnQueue.size() == 1) {
            Optional<PlayerComponent> player = world.getComponent(getCurrentTurn().getEntity(), PlayerComponent.class);
            if (player.isPresent()) {
                this.winnerPointer.copyTeam(player.get().getTeam());
            }
            return true;
        }

        return false;
    }

    /**
     * Renders messages for time related events. These include time remaining message for the canvas, the shop
     * and the time until the turn ends.
     *
     * @param time Time since game commenced.
     */
    private void drawTimer(double time) {
        int timeRemaining = getTimeRemaining(time);

        staticHandler.addStaticRenderAction((context, handler1) -> {
            context.setFill(Color.LIGHTGREY);
            context.fillRoundRect(10, 110, 140, 30, 2, 2);
            context.strokeRoundRect(10, 110, 140, 30, 2, 2);
            context.setFill(Color.RED);
            context.setFont(Font.font(fontVerdana, 12));
            context.fillText("Time Remaining: " + timeRemaining, 20, 130);

            // Final count down
            if (timeRemaining <= WARNING_TIME) {
                context.setFont(Font.font(fontVerdana, 36));
                context.setTextAlign(TextAlignment.CENTER);
                context.fillText("Turn ending in " + timeRemaining, world.getWidth() / 2, world.getHeight() / 4);
            }

            //Draw delay timer
            if (delayCurrentTurn) {
                int delayRemaining = (int) (TURN_DELAY + 1 - (time - delayStartTime));
                String nextTeam = "";
                context.setTextAlign(TextAlignment.CENTER);

                Optional<PlayerComponent> player =
                        world.getComponent(getCurrentTurn().getEntity(), PlayerComponent.class);
                if (player.isPresent()) {
                    nextTeam = player.get().getTeamName();
                }

                context.setFont(Font.font(fontVerdana, 36));
                context.fillText("Team " + nextTeam + "'s turn starts in " + delayRemaining,
                        world.getWidth() / 2, world.getHeight() / 4);
            }

            //restore text alignment
            context.setTextAlign(TextAlignment.LEFT);
        });
        Entity shop = world.getComponents(ShopComponent.class).get(0).getEntity();
        ShopDisplayComponent shopSDC = world.getComponent(shop, ShopDisplayComponent.class).get();

        // Draws the timer for the shop when it is displaying
        if (shopSDC.isShowingDisplay()) {
            staticHandler.addStaticRenderAction((context, handler2) -> {
                context.setFont(Font.font(fontVerdana, 12));
                context.setFill(Color.WHITE);
                context.fillText(" TIME REMAINING", 235, 430);
                context.setFont(Font.font(fontVerdana, 90));
                if (timeRemaining < 10) {
                    context.setFill(Color.RED);
                    context.fillText(Integer.toString(timeRemaining), 263, 510);
                } else {
                    context.fillText(Integer.toString(timeRemaining), 235, 510);
                }
                context.setFont(Font.font(fontVerdana, 12));
            });

        }
    }

    /**
     * Draws an arrow on top of the noodle who owns the 'position' component to indicate its their turn. The arrow
     * is drawn only if the shop is not displaying.
     *
     * @param position position of the pointer on the canvas
     */
    private void drawCurrentPointer(PositionComponent position) {
        double x = position.getX() + 25;
        double y = position.getY() - 70;
        double[] xs = {x, x + 4, x + 4, x + 8, x + 2, x - 4, x};
        double[] ys = {y, y, y + 6, y + 6, y + 10, y + 6, y + 6};
        handler.addRenderAction((context, handler3) -> {

            Entity shop = world.getComponents(ShopComponent.class).get(0).getEntity();
            ShopDisplayComponent shopSDC = world.getComponent(shop, ShopDisplayComponent.class).get();

            if (!shopSDC.isShowingDisplay()) {
                context.setFill(Color.RED);
                context.fillPolygon(xs, ys, 7);
            }
        });
    }

    /**
     * Plays count down warning sound when player's turn time remaining is less than WARNING_TIME.
     *
     * @param t Game time.
     */
    private void countDownSound(double t) {
        // Time remaining for current turn.
        int timeLeft = getTimeRemaining(t);

        // Checks if the sound has already been played for the current integer number of seconds remaining.
        if (timeLeft <= WARNING_TIME && timeLeft != lastWarningTime) {
            // Updates last time warning sound was played.
            lastWarningTime = timeLeft;

            // Plays warning sound.
            AudioManager.playSound("resources/sounds/timer_final2.wav", false);
        }
    }
}