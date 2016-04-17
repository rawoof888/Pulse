package com.hive3.pulse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class ActivityChat extends AppCompatActivity implements View.OnClickListener {

    private static String CURRENT_USER = ParseUser.getCurrentUser().getUsername();
    private String buddy;
    private EditText msg_edittext;
    private ArrayList<Conversation> convList;
    private Date lastMsgDate;
    private ChatAdapter chatAdapter;


    ListView msgListView;


    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ParseUser.getCurrentUser().put("status", "searching");
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

            }
        });

        chatAdapter = new ChatAdapter();
        convList = new ArrayList<Conversation>();


        msg_edittext = (EditText) findViewById(R.id.messageEditText);
        msg_edittext.setEnabled(false);
        msgListView = (ListView) findViewById(R.id.msgListView);
        ImageButton sendButton = (ImageButton) findViewById(R.id.sendMessageButton);
        sendButton.setOnClickListener(this);

        // ----Set autoscroll of listview when a new message arrives----//
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);


        convList = new ArrayList<Conversation>();
        msgListView.setAdapter(chatAdapter);


        msg_edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        ParseQuery<ParseUser> searchQuery = ParseQuery.getUserQuery();
        searchQuery.whereMatches("status", "searching");
        searchQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(final List<ParseUser> list, ParseException e) {
                if (e == null) {

                    if (list.size() > 0) {
                        Collections.shuffle(list);

                        msg_edittext.setEnabled(true);
                        list.get(0).put("status", "active");
                        buddy = list.get(0).getUsername();
                        list.get(0).saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(getApplicationContext(), "No of users found : " + String.valueOf(list.size()), Toast.LENGTH_LONG).show();

                            }
                        });

                        ParseUser.getCurrentUser().put("status", "active");
                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                if (e == null) {
                                    Toast.makeText(getApplicationContext(), "Status is active", Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(getApplicationContext(), "Error occured", Toast.LENGTH_LONG).show();

                                }

                            }
                        });
                    } else {
                        msg_edittext.setEnabled(false);
                    }


                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        receiveMessage();


        ////////////------------//////////

        ParseUser.getCurrentUser().put("status", "searching");
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

            }
        });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sendMessageButton:
                hideKeyboard(v);
                sendTextMessage(v);

        }

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this, ActivityMain.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }


    private void receiveMessage() {


        ParseQuery<ParseObject> q = ParseQuery.getQuery("Chat");
        if (convList.size() == 0) {
            // load all messages...
            ArrayList<String> al = new ArrayList<String>();
            al.add(buddy);
//			al.add(UserList.user.getUsername());
            q.whereContainedIn("sender", al);
            q.whereContainedIn("receiver", al);


        } else {
            // load only newly received message..
            if (lastMsgDate != null)
                q.whereGreaterThan("createdAt", lastMsgDate);
            q.whereEqualTo("sender", buddy);
            q.whereEqualTo("receiver", CURRENT_USER);

            String messageReceiving = convList.get(0).getMsg();


        }
        q.orderByDescending("createdAt");
        q.setLimit(30);
        q.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> li, ParseException e) {
                if (li != null && li.size() > 0) {
                    for (int i = li.size() - 1; i >= 0; i--) {
                        ParseObject po = li.get(i);
                        Conversation c = new Conversation(po
                                .getString("message"), po.getCreatedAt(), po
                                .getString("sender"));


                        convList.add(c);

                        if (lastMsgDate == null
                                || lastMsgDate.before(c.getDate()))
                            lastMsgDate = c.getDate();
                        chatAdapter.notifyDataSetChanged();
                    }
                }
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        receiveMessage();
                    }
                }, 1000);
            }
        });


    }

    public void sendTextMessage(View v) {

        String messageSending = msg_edittext.getText().toString();


//        ChatMessage message = new ChatMessage(CURRENT_USER,buddy,messageSending,MESSAGE_ID,true);
//        chatList.add(message);
        msg_edittext.setText("");


        Conversation conversation = new Conversation();
        conversation.setMsg(messageSending);
        conversation.setDate(new Date());
        conversation.setSender(CURRENT_USER);
        conversation.setReceiver(buddy);


        convList.add(conversation);


        ParseObject po = new ParseObject("Chat");
        po.put("sender", CURRENT_USER);
        po.put("receiver", buddy);
        // po.put("createdAt", "");
        po.put("message", messageSending);
        po.saveEventually(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                if (e == null) {
                    chatAdapter.notifyDataSetChanged();
                }

            }
        });


    }

    private class ChatAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return convList.size();
        }

        @Override
        public Conversation getItem(int position) {
            return convList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //Conversation conversation = (Conversation) conversations.get(position);
            Conversation conversation = getItem(position);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View vi = convertView;
            if (convertView == null)
                vi = inflater.inflate(R.layout.chatbubble, null);
            TextView msg = (TextView) vi.findViewById(R.id.message_text);
            msg.setText(conversation.getMsg());

            LinearLayout layout = (LinearLayout) vi
                    .findViewById(R.id.bubble_layout);
            LinearLayout parent_layout = (LinearLayout) vi
                    .findViewById(R.id.bubble_layout_parent);

            if (conversation.isSent()) {
                layout.setBackgroundResource(R.drawable.bubble2);
                parent_layout.setGravity(Gravity.RIGHT);
            }
            // If not mine then align to left
            else {
                layout.setBackgroundResource(R.drawable.bubble1);
                parent_layout.setGravity(Gravity.LEFT);
            }
            msg.setTextColor(Color.BLACK);
            return vi;

        }


    }


}
