package com.hive3.pulse.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.hive3.pulse.ActivityConfession;
import com.hive3.pulse.ActivityMain;
import com.hive3.pulse.DataProviders.ConfessionModel;
import com.hive3.pulse.FragmentConfession;
import com.hive3.pulse.R;
import com.hive3.pulse.Utils.MyApplication;
import com.hive3.pulse.Utils.ParseConstants;
import com.hive3.pulse.Utils.Utils;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterFragmentConfession extends RecyclerView.Adapter<RVAdapterFragmentConfession.MyViewHolder>  {


    private ArrayList<ConfessionModel> confessionArray = null;
    Context context;
    LayoutInflater inflater;



    public RVAdapterFragmentConfession(ArrayList<ConfessionModel> arrayList, Context context) {
        this.confessionArray = arrayList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void updateData(ArrayList<ConfessionModel> confessionArray) {
        this.confessionArray = confessionArray;
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        EditText et_confession;
        Button btn_confess, btn_discard;

        //--feed--//
        TextView confession, objID, author;
        Button btn_comment;


        public MyViewHolder(View view, int type) {
            super(view);
            bindFields(view);
            if (type == 0) {

            } else if (type > 0) {

                confession = (TextView) view.findViewById(R.id.confession);
                btn_comment = (Button) view.findViewById(R.id.btn_comment);
                objID = (TextView) view.findViewById(R.id.objID);
                author = (TextView) view.findViewById(R.id.tv_author);


            }
        }

        @Override
        public void onClick(View v) {

        }


        private void bindFields(View view) {
            et_confession = (EditText) view.findViewById(R.id.et_confession);
            btn_confess = (Button) view.findViewById(R.id.btn_confess);
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
                view = inflater.inflate(R.layout.rv_fragment_confession_first_row_layout, parent, false);
                myViewHolder = new MyViewHolder(view, viewType);
                return myViewHolder;
            default:
                view = inflater.inflate(R.layout.rv_fragment_confession_row_layout, parent, false);
                myViewHolder = new MyViewHolder(view, viewType);
                return myViewHolder;

        }

    }


    @Override
    public int getItemViewType(int position) {
        int viewType = 1;//default feed
        if (position == 0) viewType = 0;

        return viewType;
    }


    @Override
    public int getItemCount() {
//        if (confessionArray.size()==0){
//            return 1;
//        }else {
//            return (confessionArray.size());
//        }
        while (confessionArray.size() == 0) {
            return 1;
        }if (FragmentConfession.my_confessions){
            return confessionArray.size();
        }
        return confessionArray.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final ParseObject confession = new ParseObject(ParseConstants.CONFESSION);


        if (holder.getItemViewType() == 0) {


            holder.btn_confess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Utils.hideKeyboard(v);

                    if (holder.et_confession.getText().length() > 0) {

                        final String cf = holder.et_confession.getText().toString();
                        holder.et_confession.setText("");


                        confession.put("confession", cf);
                        confession.put("username", ParseUser.getCurrentUser().getUsername());
                        confession.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                if (e == null) {

                                    Toast.makeText(context, "confessed", Toast.LENGTH_LONG).show();

                                    //-------- add instantly to the recyclerview
                                    ConfessionModel model = new ConfessionModel();
                                    model.setConfession(cf);
                                    model.setAuthor(ParseUser.getCurrentUser().getUsername());
                                    model.setObjID(confession.getObjectId());
                                    FragmentConfession.data.add(1,model);
                                    FragmentConfession.adapter.notifyDataSetChanged();
                                    //--------


                                }else {
                                    Toast.makeText(MyApplication.getAppContext(),"Error",Toast.LENGTH_LONG).show();
                                }

                            }
                        });


                    }

                }
            });
            holder.btn_discard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.hideKeyboard(v);
                    holder.et_confession.setText("");
                }
            });

            holder.et_confession.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus){
                        Utils.hideKeyboard(v);
                    }
                }
            });

        } else if (holder.getItemViewType() == 1) {
            holder.confession.setText(confessionArray.get(position).getConfession());
            holder.objID.setText(confessionArray.get(position).getObjID());
            holder.author.setText(confessionArray.get(position).getAuthor());


            holder.btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.hideKeyboard(v);
                    Intent intent = new Intent(context, ActivityConfession.class);
                    intent.putExtra("objectId", confessionArray.get(position).getObjID());
                    intent.putExtra("confession", confessionArray.get(position).getConfession());
                    intent.putExtra("author", confessionArray.get(position).getAuthor());
                    context.startActivity(intent);
                }

            });


        }




    }





}





















