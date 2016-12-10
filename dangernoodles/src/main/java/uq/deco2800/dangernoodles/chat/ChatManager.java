package uq.deco2800.dangernoodles.chat;


import uq.deco2800.singularity.clients.dangernoodle.DangernoodleRestClient;
import uq.deco2800.singularity.clients.realtime.messaging.MessagingClient;

/**
 * Created by Siyu on 22/10/2016.
 */
public class ChatManager {
    private ChatRoom myChatRoom;
    private MessagingClient messagingClient;
    private DangernoodleRestClient restClient;

    public ChatManager(MessagingClient messagingClient, DangernoodleRestClient restClient){
        this.messagingClient = messagingClient;
        this.restClient = restClient;
    }

    /**
     * @return messaging client
     */
    public MessagingClient getMessagingClient() {
        return messagingClient;
    }

    /**
     * @return rest client
     */
    public DangernoodleRestClient getRestClient(){
        return restClient;
    }

    /**
     * @return chat room
     */
    public ChatRoom getChatRoom() {
        return myChatRoom;
    }
}
