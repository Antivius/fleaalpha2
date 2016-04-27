package fi.benson.models;


import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by bkamau on 4/9/16.
 */
@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String SENDER_ID_KEY = "senderId";
    public static final String RECIEVER_ID_KEY = "recieverId";
    public static final String BODY_KEY = "body";


    public String getSenderId() {
        return getString(SENDER_ID_KEY);
    }

    public String getRecieverId() {
        return getString(RECIEVER_ID_KEY);
    }

    public String getBody() {
        return getString(BODY_KEY);
    }

    public void setSenderId(String senderId) {
        put(SENDER_ID_KEY, senderId);
    }

    public void setRecieverId(String recieverId) {
        put(RECIEVER_ID_KEY, recieverId);
    }

    public void setBody(String body) {
        put(BODY_KEY, body);
    }
}