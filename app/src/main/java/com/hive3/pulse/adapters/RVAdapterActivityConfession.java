package com.hive3.pulse.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hive3.pulse.ActivityConfession;
import com.hive3.pulse.ActivityMain;
import com.hive3.pulse.R;
import com.hive3.pulse.Utils.MyApplication;
import com.hive3.pulse.Utils.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class RVAdapterActivityConfession extends RecyclerView.Adapter<RVAdapterActivityConfession.MyViewHolder> {


    LayoutInflater inflater;
    Context context;

    ArrayList<String> commentsArray = new ArrayList<>();


    public RVAdapterActivityConfession(ArrayList<String> arrayList, Context context) {
        this.commentsArray = arrayList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void updateData(ArrayList<String> commentsArray) {
        this.commentsArray = commentsArray;
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        EditText et_confession;
        Button btn_edit_confession,btn_update,btn_discard,btn_delete_confession;


        //--feed--//
        TextView confession, comment,author,objectId;
        Button  btn_comment;


        public MyViewHolder(View view, int type) {
            super(view);
            bindFields(view);
            if (type == 0) {
                confession = (TextView) view.findViewById(R.id.confession);
                confession.setText(ActivityConfession.intent_confession);
                btn_comment = (Button) view.findViewById(R.id.btn_comment);
                btn_edit_confession = (Button)view.findViewById(R.id.btn_edit_confession);
                btn_delete_confession = (Button)view.findViewById(R.id.btn_delete_confession);
                author = (TextView)view.findViewById(R.id.tv_author);
                author.setText(ActivityConfession.intent_author);
                objectId = (TextView)view.findViewById(R.id.objID);
                objectId.setText(ActivityConfession.intent_objId);
                et_confession = (EditText) view.findViewById(R.id.et_confession);
                btn_update = (Button) view.findViewById(R.id.btn_update);
                btn_discard = (Button) view.findViewById(R.id.btn_discard);


            } else if (type > 0) {


                comment = (TextView) view.findViewById(R.id.tv_comment);


            }
        }

        @Override
        public void onClick(View v) {


        }


        private void bindFields(View view) {
            et_confession = (EditText) view.findViewById(R.id.et_confession);
            btn_update = (Button) view.findViewById(R.id.btn_confess);
            btn_discard = (Button) view.findViewById(R.id.btn_discard);
        }


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int itemType = getItemViewType(viewType);
        View view;
        MyViewHolder myViewHolder;
        switch (itemType) {
            case 0:
                view = inflater.inflate(R.layout.rv_activity_confession_first_row_layout, parent, false);
                myViewHolder = new MyViewHolder(view, viewType);
                return myViewHolder;
            default:
                view = inflater.inflate(R.layout.rv_activity_confession_row_layout, parent, false);
                myViewHolder = new MyViewHolder(view, viewType);
                return myViewHolder;

        }

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        if (holder.getItemViewType()==0){

            if (holder.author.getText().equals(ParseUser.getCurrentUser().getUsername())){
                holder.btn_edit_confession.setVisibility(View.VISIBLE);
                holder.btn_delete_confession.setVisibility(View.VISIBLE);

                holder.btn_edit_confession.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String confession = holder.confession.getText().toString();
                        holder.confession.setVisibility(View.GONE);
                        holder.et_confession.setVisibility(View.VISIBLE);
                        holder.et_confession.setText(confession);

                        holder.btn_edit_confession.setVisibility(View.GONE);
                        holder.btn_delete_confession.setVisibility(View.GONE);
                        holder.btn_update.setVisibility(View.VISIBLE);
                        holder.btn_discard.setVisibility(View.VISIBLE);


                        holder.btn_update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String con = holder.et_confession.getText().toString();
                                holder.et_confession.setVisibility(View.GONE);
                                holder.confession.setVisibility(View.VISIBLE);
                                holder.et_confession.setText("");
                                ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.CONFESSION);
                                query.whereEqualTo("objectId", holder.objectId.getText());
                                query.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> list, ParseException e) {
                                        if (e == null) {
                                            for (ParseObject object : list) {
                                                Log.e("up",object.toString());
                                                object.put("confession", con);
                                                object.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        Toast.makeText(context, "Updated", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                            holder.btn_update.setVisibility(View.GONE);
                                            holder.btn_discard.setVisibility(View.GONE);
                                            holder.btn_edit_confession.setVisibility(View.VISIBLE);
                                            holder.btn_delete_confession.setVisibility(View.VISIBLE);
                                            holder.confession.setText(con);
                                        }
                                    }
                                });
                            }
                        });



                        holder.btn_discard.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.btn_update.setVisibility(View.GONE);
                                holder.btn_discard.setVisibility(View.GONE);
                                holder.btn_edit_confession.setVisibility(View.VISIBLE);
                                holder.btn_delete_confession.setVisibility(View.VISIBLE);

                            }
                        });


                    }
                });


                holder.btn_delete_confession.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ParseObject.createWithoutData(ParseConstants.CONFESSION,holder.objectId.getText().toString()).deleteEventually();
                        Intent intent = new Intent(context, ActivityMain.class);
                        context.startActivity(intent);

                    }
                });







            }else {
                holder.btn_edit_confession.setVisibility(View.GONE);
                holder.btn_delete_confession.setVisibility(View.GONE);
            }



        }

        if (holder.getItemViewType() > 0) {
            holder.comment.setText(ActivityConfession.commentArray.get(position - 1));
        }


    }

    @Override
    public int getItemCount() {
        return commentsArray.size() + 1;
    }


    @Override
    public int getItemViewType(int position) {

        int viewType = 1;//default feed
        if (position == 0) viewType = 0;

        return viewType;

    }



}


















































