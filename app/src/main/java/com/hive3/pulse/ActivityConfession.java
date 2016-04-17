package com.hive3.pulse;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.hive3.pulse.DataProviders.ConfessionModel;
import com.hive3.pulse.adapters.RVAdapterActivityConfession;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import java.util.ArrayList;
import java.util.List;

public class ActivityConfession extends AppCompatActivity {


    RecyclerView recyclerView;
    RVAdapterActivityConfession adapter;
    LinearLayoutManager layoutManager;

    EditText et_comment;
    ImageButton btn_comment;

    static public String intent_objId,intent_confession,intent_author;


    private ArrayList<ConfessionModel> confessionArray = new ArrayList<>();

    public static ArrayList<String> commentArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confession);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        commentArray.clear();

        recyclerView = (RecyclerView)findViewById(R.id.rv_activity_confession);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RVAdapterActivityConfession(commentArray, this);

        recyclerView.setAdapter(adapter);

        et_comment = (EditText) findViewById(R.id.et_comment);
        btn_comment = (ImageButton)findViewById(R.id.commentButton);



        Intent intent = getIntent();
        intent_objId=  intent.getStringExtra("objectId");
        intent_confession=  intent.getStringExtra("confession");
        intent_author=  intent.getStringExtra("author");



        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment");
        query.whereEqualTo("parent", intent_objId);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    commentArray.clear();
                    for (ParseObject object : list) {


                        commentArray.add(object.getString("content"));
                        adapter.updateData(commentArray);


                    }
                }
            }
        });


        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_comment.setEnabled(false);
                final String comment = et_comment.getText().toString();
                et_comment.setText("");
                if (comment.length()>1){
                    ParseObject myComment = new ParseObject("Comment");
                    myComment.put("parent", intent_objId);
                    myComment.put("content", comment);
                    myComment.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(getApplicationContext(), "commented", Toast.LENGTH_LONG).show();
                            commentArray.add(0,comment);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }else {
                    Snackbar.make(v, "comment should have 2 characters minimum   ", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                btn_comment.setEnabled(true);

            }
        });



    }





}
