package uq.deco2800.dangernoodles.chat;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 * Created by Siyu on 20/10/2016.
 */


public class ChatRoom {
    private ObservableList<ChatMessages> chatMessages = FXCollections.observableArrayList();
    private ObservableList<ChatSession> chatSessions = FXCollections.observableArrayList();
    private ObservableMap<String, ObservableList<ChatMessages>> chatMessagesMap = FXCollections.observableHashMap();//store all messages in the same chat session
    private ObservableMap<String, ChatSession> chatSessionMap = FXCollections.observableHashMap();//store chat sessions by ID

    public ChatRoom() {
        /*Keep mapping updated when new messages or new sessions are added*/
        chatMessages.addListener(new ListChangeListener<ChatMessages>() {
                                     @Override
                                     public void onChanged(Change<? extends ChatMessages> msg) {
                                         msg.getList().forEach(currentMsg -> {
                                             String sessionID = currentMsg.sessionID;
                                             chatMessagesMap.putIfAbsent(sessionID, FXCollections.observableArrayList());
                                             ObservableList<ChatMessages> messages = chatMessagesMap.get(sessionID);
                                             if (!(messages.contains(currentMsg))) {
                                                 messages.add(currentMsg);
                                             }
                                         });
                                     }
                                 }
        );
        chatSessions.addListener(new ListChangeListener<ChatSession>() {
            @Override
            public void onChanged(Change<? extends ChatSession> session) {
                session.getList().forEach(currentSession -> {
                    chatSessionMap.putIfAbsent(currentSession.sessionID, currentSession);
                    chatMessagesMap.putIfAbsent(currentSession.sessionID, FXCollections.observableArrayList());
                });
            }}
        );

    }

    /**
     * @param sessionID
     * @return A chat session associated to the provided sessionID
     */
    public ChatSession getChatSession(String sessionID) {
        ChatSession cSession = chatSessionMap.get(sessionID);
        return cSession;
    }

    /**
     * @param sessionID
     * @return All messages from a certain session
     */
    public ObservableList<ChatMessages> getChatMessagesFromSession(String sessionID) {
        return chatMessagesMap.getOrDefault(sessionID, FXCollections.emptyObservableList());
    }

    /**
     * @return All chat sessions
     */
    public ObservableList<ChatSession> getAllChatSessions() {
        return chatSessions;
    }

    /**
     * Remove a session from list
     * @param session
     */
    public void removeSession(ChatSession session) {
        chatSessions.remove(session);
    }

    /**
     * Add a new session to list
     * @param session
     */
    public void addSession(ChatSession session) {
        chatSessions.add(session);
    }

    /**
     * Add new messages to list
     * @param msg
     */
    public void addMessage(ChatMessages msg) {
        chatMessages.add(msg);
    }



}

