package fi.benson.views;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import fi.benson.R;
import fi.benson.models.ChatList;

public class ChatListActivity extends Activity {



    List<ChatList> myList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);


    }


    @Override
    protected void onResume() {
        super.onResume();


    }
}
