package fi.benson.views;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fi.benson.R;
import fi.benson.adapters.ChatListAdapter;
import fi.benson.models.Message;

public class ChatActivity extends AppCompatActivity {

    static final String TAG = ChatActivity.class.getSimpleName();

    ListView lvChat;
    ArrayList<Message> mMessages;
    ChatListAdapter mAdapter;

    // Keep track of initial load to scroll to the bottom of the ListView
    boolean mFirstLoad;

    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    EditText etMessage;
    Button btSend;
    private static ParseUser currentUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        currentUser = ParseUser.getCurrentUser();

        setupMessagePosting();
        mHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

    }



    void setupMessagePosting() {

        // Find the text field and button
        etMessage = (EditText) findViewById(R.id.etMessage);
        btSend = (Button) findViewById(R.id.btSend);
        lvChat = (ListView) findViewById(R.id.lvChat);
        mMessages = new ArrayList<>();


        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.
        lvChat.setTranscriptMode(1);
        mFirstLoad = true;
        final String userId = ParseInstallation.getCurrentInstallation().getObjectId();
        final String userName = ParseUser.getCurrentUser().getUsername();
        mAdapter = new ChatListAdapter(ChatActivity.this, userId, mMessages);
        lvChat.setAdapter(mAdapter);


        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String data = etMessage.getText().toString();

                ParseObject message = ParseObject.create("Message");
                message.put(Message.RECIEVER_ID_KEY, "rECIEVERiD");
                message.put(Message.SENDER_ID_KEY, currentUser.getObjectId());
                message.put(Message.BODY_KEY, data);

                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(ChatActivity.this, "Successfully created message on Parse",
                                Toast.LENGTH_SHORT).show();
                        sendAnnouncement();
                        refreshMessages();
                    }
                });
                etMessage.setText(null);
            }
        });
    }


    // Query messages from Parse so we can load them into the chat adapter
    void refreshMessages() {

        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);

        // Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByAscending("createdAt");

        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {

                    mMessages.clear();
                    mMessages.addAll(messages);
                    mAdapter.notifyDataSetChanged(); // update adapter

                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        lvChat.setSelection(mAdapter.getCount() - 1);
                        mFirstLoad = false;
                    }
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });
    }



    // Create a handler which can run code periodically
    static final int POLL_INTERVAL = 100; // milliseconds
    Handler mHandler = new Handler();  // android.os.Handler
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            mHandler.postDelayed(this, POLL_INTERVAL);
        }
    };



    public static void sendAnnouncement() {


        HashMap<String, String> payload = new HashMap<>();
        payload.put("customData", "launch");
        payload.put("action", "launch");
        payload.put("message", "launch");
        ParseCloud.callFunctionInBackground("pushChannelTest", payload);
    }

}





