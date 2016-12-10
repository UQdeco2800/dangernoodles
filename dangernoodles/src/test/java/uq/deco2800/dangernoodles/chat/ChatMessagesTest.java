package uq.deco2800.dangernoodles.chat;

import org.junit.Assert;

import static org.junit.Assert.*;

/**
 * Created by Park on 10/22/2016.
 */


public class ChatMessagesTest {

    String messageTest;
    String channelTest;
    String myUserTest;
    String myUserNameTest;
    String userTest;

    public void Test1(String message, String channelID, String myUserID, String myUserName, String userID){

        Assert.assertEquals(messageTest,message);
        Assert.assertEquals(channelTest,channelID);
        Assert.assertEquals(myUserTest,myUserID);
        Assert.assertEquals(myUserNameTest,myUserName);
        Assert.assertEquals(userTest,userID);
    }
}