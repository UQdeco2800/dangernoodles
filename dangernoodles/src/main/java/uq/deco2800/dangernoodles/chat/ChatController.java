package uq.deco2800.dangernoodles.chat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.slf4j.Logger;
import uq.deco2800.dangernoodles.ClientManager;
import uq.deco2800.singularity.clients.dangernoodle.DangernoodleRestClient;
import uq.deco2800.singularity.clients.realtime.messaging.MessagingClient;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Siyu on 22/10/2016.
 */
public class ChatController implements Initializable{
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(ChatController.class);
    private ListView<ChatMessages> chatPane;
    private TextField msgBox;
    private Button sendBtn;
    private Label userLabel;
    private TextField searchBox;
    private ListView<ChatSession> userList;
    private ChatRoom myChatRoom;
    private MessagingClient messagingClient;
    private DangernoodleRestClient restClient;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myChatRoom = ClientManager.getChatManager().getChatRoom();
        messagingClient = ClientManager.getChatManager().getMessagingClient();
        restClient = ClientManager.getChatManager().getRestClient();
        userList.setItems(myChatRoom.getAllChatSessions());
        userList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ChatSession>() {
            @Override
            public void changed(ObservableValue<? extends ChatSession> observable, ChatSession oldItem, ChatSession newItem) {
                ChatController.this.selectSession(newItem);
            }
        });

    }

    /**
     * Send message action is triggered when msgBox has text being entered and send button is pressed
     * @param event
     */
    public void sendMessage(ActionEvent event) {
        String message = msgBox.getText();
        String selectedID = getSelectedSession();
        if (!(selectedID==null)) {
            messagingClient.sendMessage(selectedID, message);
        }
        msgBox.clear();
    }


    /**
     * Get the selected session from right list on screen
     * @return selected sessionID
     */
    private String getSelectedSession() {
        ChatSession chatSession = userList.getSelectionModel().getSelectedItem();
        return chatSession.sessionID;
    }

    /**
     * Get all messages associated with the selected session and change the userLabel to the user/session you are
     * chatting with
     * @param s
     */
    private void selectSession(ChatSession s) {
        ObservableList<ChatMessages> chatMessages = myChatRoom.getChatMessagesFromSession(s.sessionID);
        chatPane.setItems(chatMessages);
        userLabel.setText(s.getSessionName());
    }
}
