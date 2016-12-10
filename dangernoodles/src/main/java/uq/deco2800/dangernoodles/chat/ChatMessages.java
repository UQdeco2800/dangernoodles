package uq.deco2800.dangernoodles.chat;

/**
 * Created by Siyu on 20/10/2016.
 */
public class ChatMessages {
    public final String message;
    public final String sessionID;
    public final String myUserID;
    public final String myUserName;
    public final String userID;

    public ChatMessages (String message, String sessionID, String myUserID, String myUserName, String userID) {
        this.message = message;
        this.sessionID = sessionID;
        this.myUserID = myUserID;
        this.myUserName = myUserName;
        this.userID = userID;
    }

}
