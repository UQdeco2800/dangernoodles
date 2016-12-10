package uq.deco2800.dangernoodles.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.dangernoodles.ClientManager;
import uq.deco2800.dangernoodles.components.NameComponent;
import uq.deco2800.dangernoodles.components.PlayerComponent;
import uq.deco2800.dangernoodles.components.PositionComponent;
import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.singularity.clients.dangernoodle.DangernoodleEventListener;
import uq.deco2800.singularity.common.representations.dangernoodle.PositionUpdate;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by khoi_truong on 2016/10/23.
 * <p>
 * This class is used to handle communication between the game and the
 * server. It will poll information needed and send it to the server.
 */
public class NetworkSystem implements System {
    private static final String CLASS = NetworkSystem.class.getName();
    private static final Logger LOGGER = LoggerFactory.getLogger(CLASS);
    // Information about noodles.
    private String currentNoodle;
    private double positionX;
    private double positionY;
    // Instrinsic lock to make sure that no concurrent read and write occur
    // when the server sent something back.
    private final Lock lock = new ReentrantLock();

    /**
     * Default constructor for class.
     */
    public NetworkSystem() {
        // Add new listener for noodle position.
        ClientManager.getClientManager()
                .getRestClient()
                .getCurrentLobby()
                .addNoodlePositionListener(new NoodlePositionListener());
        // Add new listener for noodle disconnect.
        ClientManager.getClientManager()
                .getRestClient()
                .getCurrentLobby()
                .addDisconnectPlayerListener(new NoodlePositionListener());
    }

    /**
     * Is called every tick and runs with the t and dt parameters
     *
     * @param world
     *         the game parent world
     * @param t
     *         Time since the beginning of the game
     * @param dt
     *         The delta time, i.e. the time since last frame
     */
    @Override
    public void run(World world, double t, double dt) {
        for (ComponentMap cm : world.getIterator(
                NameComponent.class,
                PlayerComponent.class,
                TurnComponent.class,
                PositionComponent.class)) {
            NameComponent nameComponent = cm.get(NameComponent.class);
            TurnComponent turnComponent = cm.get(TurnComponent.class);
            PositionComponent positionComponent = cm.get(PositionComponent.class);
            // Simply check if the current noodle at turn is the one the
            // current client by check name
            if (turnComponent.getTurn()) {
                // Check if this noodle is the current client or not. If not,
                // update its position. If yes, tell other clients about its
                // position.
                if (nameComponent.getName() == null) {
                    LOGGER.error("Name is null.");
                } else if (ClientManager.getClientManager().getRestClient()
                        .getCurrentUser() == null) {
                    LOGGER.error("Current User is null.");
                } else {
                    if (nameComponent.getName().equals(ClientManager.getClientManager().getRestClient().getCurrentUser())) {
                        // Now that this client is at turn, every it does here will
                        // be broadcast to other players. Prepare the necessary
                        // information and package it inside a container and send it
                        // to the server.
                        String noodleName = nameComponent.getName();
                        double positionX = positionComponent.getX();
                        double positionY = positionComponent.getY();
                        PositionUpdate positionUpdate = new PositionUpdate();
                        positionUpdate.setPlayerId(noodleName);
                        positionUpdate.setPositionX(positionX);
                        positionUpdate.setPositionY(positionY);
                        ClientManager.getClientManager()
                                .getRestClient()
                                .getCurrentLobby()
                                .sendToServer(positionUpdate);
                        break;
                    } else {
                        // Here means that it's other noodles turn from the client
                        // point of view. So the game needs to listen for input
                        // from the server.
                        // Simply skip when the the values are still null
                        if (currentNoodle == null) {
                            continue;
                        }
                        // Once here check to see if the current noodle in the
                        // loop is also the one who sent the message.
                        if (nameComponent.getName().equals(currentNoodle)) {
                            // Acquire lock so that there will no new update from the
                            // server during this process of updating of new position.
                            lock.lock();
                            positionComponent.setX(positionX);
                            positionComponent.setY(positionY);
                            lock.unlock();
                        }
                        // Update the position of the noodle who is at turn.
                    }
                }
            }
        }
    }

    /**
     * Private class which is used to communicate between the real time
     * client and this system when there is update of information about other
     * noodle. These information will then be converted so that JavaFX can
     * pick them up.
     */
    private class NoodlePositionListener extends DangernoodleEventListener {
        @Override
        public void notifyListener(Object object) {
            try {
                // Acquire lock when updating.
                lock.lock();
                // Convert the object and update private fields.
                PositionUpdate positionUpdate = (PositionUpdate) object;
                LOGGER.info("Updating with [{}].", positionUpdate);
                currentNoodle = positionUpdate.getPlayerId();
                positionX = positionUpdate.getPositionX();
                positionY = positionUpdate.getPositionY();
                // Release lock once done.
                lock.unlock();
            } catch (ClassCastException e) {
                LOGGER.error("Position update cannot be received. Detail: {}", e);
            }
        }
    }

    /**
     * Private class which is used to communicate between
     */
    private class DisconnectPlayerListener extends DangernoodleEventListener {
        @Override
        public void notifyListener() {
            ClientManager.getClientManager().getRestClient().requestLeaveLobby();
            java.lang.System.exit(0);
        }
    }
}
