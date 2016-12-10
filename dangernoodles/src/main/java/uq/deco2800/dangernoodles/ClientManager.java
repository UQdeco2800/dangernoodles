package uq.deco2800.dangernoodles;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uq.deco2800.dangernoodles.chat.ChatManager;
import uq.deco2800.singularity.clients.dangernoodle.DangerNoodleRealTimeClient;
import uq.deco2800.singularity.clients.dangernoodle.DangernoodleRestClient;
import uq.deco2800.singularity.clients.realtime.messaging.MessagingClient;
import uq.deco2800.singularity.common.ServerConstants;
import uq.deco2800.singularity.common.SessionType;
import uq.deco2800.singularity.common.representations.User;
import uq.deco2800.singularity.common.representations.realtime.RealTimeSessionConfiguration;

public class ClientManager {
    private final String client = ClientManager.class.getName();
    private final Logger logger = LoggerFactory.getLogger(client);
    // Clients each of which is handling different task.
    private DangernoodleRestClient restClient;
    private MessagingClient messagingClient;
    private DangerNoodleRealTimeClient realTimeClient;
    private static ClientManager clientManager;
    private ChatManager chatManager;

    /**
     * Default constructor for the client manager. It's private to ensure
     * that there's always one instance of this class.
     */
    private ClientManager() {
        restClient = new DangernoodleRestClient();
    }

    /**
     * Return the current instance of the client manager.
     *
     * @return an instance of the ClientManager
     *
     * @ensure an instance of the ClientManager
     */
    public static ClientManager getClientManager() {
        if (clientManager == null) {
            clientManager = new ClientManager();
        }
        return clientManager;
    }

    /**
     * Set the client ID that is logging in this game.
     *
     * @param clientId
     *         the string representing the ID of the player who is logging in
     *         this game
     *
     * @throws NullPointerException
     *         if clientId is null
     * @require clientId != null
     * @ensure getClientId() == clientId
     */
    public void setClientId(String clientId) {
        if (clientId == null) {
            throw new NullPointerException("Client ID cannot be null.");
        }
        restClient.setCurrentUser(clientId);
    }

    /**
     * Only use this method once the restClient has retrieved a token and
     * authentication is valid.
     */
    public void initialiseMessagingClient() {
        RealTimeSessionConfiguration config = new RealTimeSessionConfiguration();
        config.setPort(ServerConstants.MESSAGING_PORT);
        try {
            messagingClient = new MessagingClient(config, restClient,
                    SessionType.DANGER_NOODLES);
            logger.info("Message client is successfully created.");
        } catch (IOException exception) {
            logger.error("Could not initiate messaging Client", exception);
        }
    }

    /**
     * Return the rest client that this client manager is holding.
     *
     * @return the rest client that this client manager is holding
     *
     * @ensure the rest client that this client manager is holding
     */
    public DangernoodleRestClient getRestClient() {
        return restClient;
    }

    public void requestLeaveLobby() {
        restClient.requestLeaveLobby();
    }

    /**
     * Return the message client that this client manager is holding
     *
     * @return the message client that this client manager is holding
     *
     * @ensure the message client that this client manager is holding
     */
    public MessagingClient getMessagingClient() {
        return messagingClient;
    }

    /**
     * Return the real time client of the lobby that this client manager is
     * holding.
     *
     * @return the real time client that this client manager is holding
     *
     * @ensure the real time client that this client manager is holding
     */
    public DangerNoodleRealTimeClient getRealTimeClient() {
        return realTimeClient;
    }

    /**
     * You can use this method to make a new user... It'll be easier this way
     *
     * @param username
     *         The username of the user to create
     * @param password
     *         The password of the user to create
     */
    public static void createUser(String username, String password) {
        User user = new User(username, "", "", "", password);
        try {
            getClientManager().restClient.createUser(user);
        } catch (Exception e) {

        }
    }
    public static ChatManager getChatManager() {
        ClientManager clientManager = getClientManager();
        if (clientManager.chatManager == null) {
            DangernoodleRestClient restClient = clientManager.getRestClient();
            MessagingClient messagingClient = clientManager.getMessagingClient();
            clientManager.chatManager = new ChatManager(messagingClient, restClient);
        }
        return clientManager.chatManager;
    }

}
