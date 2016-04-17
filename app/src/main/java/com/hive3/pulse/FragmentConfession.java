package com.hive3.pulse;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.hive3.pulse.DataProviders.ConfessionModel;
import com.hive3.pulse.adapters.RVAdapterFragmentConfession;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

public class FragmentConfession extends Fragment {

    public static boolean my_confessions = false;


    RecyclerView recyclerView;
    public static RVAdapterFragmentConfession adapter;
    LinearLayoutManager layoutManager;

    public static  ArrayList<ConfessionModel> data = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_confession, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.confession_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RVAdapterFragmentConfession(data, getActivity());

        recyclerView.setAdapter(adapter);

        recyclerView.setOnScrollListener(new EndlessRecyclerView(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                int limit = current_page * 5;
                if (my_confessions) {
                    loadMoreUserData(limit);
                } else {
                    loadMoreData(limit);
                }
//                loadMoreData(limit);   remove this

            }
        });


        if (adapter.getItemCount() == 0) {

        }


        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (my_confessions) {
            recyclerView.setOnScrollListener(new EndlessRecyclerView(layoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    int limit = current_page * 5;
                    loadMoreUserData(limit);

                }
            });

            loadUserData();

        } else {
            recyclerView.setOnScrollListener(new EndlessRecyclerView(layoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    int limit = current_page * 5;
                    loadMoreData(limit);
                }
            });
            loadData();

        }


    }


    public void loadMoreData(int limit) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Confession");
        query.setLimit(limit).orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                //               dialog.dismiss();
                if (e == null) {
                    data.clear();
                    for (int i = 0; i < list.size(); i++) {
                        ParseObject object = list.get(i);
                        ConfessionModel model = new ConfessionModel();
                        model.setConfession(String.valueOf(object.get("confession")));
                        model.setAuthor(object.getString("username"));
                        model.setObjID(object.getObjectId());

                        data.add(model);
                    }
                    adapter.updateData(data);
                } else if (e.getCode() == ParseException.CONNECTION_FAILED) {
                    Toast.makeText(getActivity(), "No internetConnection,Please check your connection", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });


    }

    public void loadData() {

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Confession");
        query.setLimit(5).orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                if (e == null) {
                    data.clear();
                    for (int i = 0; i < list.size(); i++) {
                        ParseObject object = list.get(i);
                        ConfessionModel model = new ConfessionModel();
                        model.setConfession(String.valueOf(object.get("confession")));
                        model.setAuthor(object.getString("username"));
                        model.setObjID(object.getObjectId());

                        data.add(model);

                    }


                    recyclerView.setAdapter(adapter);
                    adapter.updateData(data);


                } else if (e.getCode() == ParseException.CONNECTION_FAILED) {
                    Toast.makeText(getActivity(), "No internetConnection,Please check your connection", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();

                }

            }
        });

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_refresh:
                return false;
            case R.id.chat:
                return false;
            case R.id.logout:
                return false;
            case R.id.confession:
                Toast.makeText(getContext(), "confession", Toast.LENGTH_LONG).show();
                my_confessions = false;
                FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                ft1.detach(this).attach(this).commit();
                return true;
            case R.id.my_confession:
                Toast.makeText(getContext(), " Your confession", Toast.LENGTH_LONG).show();
                my_confessions = true;
                FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                ft2.detach(this).attach(this).commit();
                return true;
            default:
                break;
        }


        return super.onOptionsItemSelected(item);


    }


    private void loadUserData() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Confession");
        query.whereContains("username", ParseUser.getCurrentUser().getUsername());
        query.setLimit(5).orderByAscending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                if (e == null) {
                    data.clear();
                    for (int i = 0; i < list.size(); i++) {
                        ParseObject object = list.get(i);
                        ConfessionModel model = new ConfessionModel();
                        model.setConfession(String.valueOf(object.get("confession")));
                        model.setAuthor(object.getString("username"));
                        model.setObjID(object.getObjectId());

                        data.add(model);

                    }
                    Log.e("abc", Integer.toString(data.size()));
                    recyclerView.setAdapter(adapter);
                    adapter.updateData(data);


                } else if (e.getCode() == ParseException.CONNECTION_FAILED) {
                    Toast.makeText(getActivity(), "No internetConnection,Please check your connection", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();

                }

            }
        });
    }


    private void loadMoreUserData(int limit) {

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Confession");
        query.whereContains("username", ParseUser.getCurrentUser().getUsername());
        query.setLimit(limit).orderByAscending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                //               dialog.dismiss();
                if (e == null) {
                    data.clear();
                    for (int i = 0; i < list.size(); i++) {
                        ParseObject object = list.get(i);
                        ConfessionModel model = new ConfessionModel();
                        model.setConfession(String.valueOf(object.get("confession")));
                        model.setAuthor(object.getString("username"));
                        model.setObjID(object.getObjectId());

                        data.add(model);
                    }
                    adapter.updateData(data);
                } else if (e.getCode() == ParseException.CONNECTION_FAILED) {
                    Toast.makeText(getActivity(), "No internetConnection,Please check your connection", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });

    }





}








































































































