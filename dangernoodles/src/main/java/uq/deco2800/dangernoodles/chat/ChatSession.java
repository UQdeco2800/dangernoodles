package uq.deco2800.dangernoodles.chat;

/**
 * Created by Siyu on 20/10/2016.
 */
/*Representation of a chat session, each chat session has an ID and a name*/
public class ChatSession {
    public final String sessionID;
    public final String sessionName;

    public ChatSession(String sessionID, String sessionName) {
        this.sessionID = sessionID;
        this.sessionName = sessionName;
    }

    /**
     * @return session name
     */
    public String getSessionName() {
        return sessionName;
    }
}
