package com.hive3.pulse;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import com.hive3.pulse.DataProviders.DPCourseDetails;
import com.hive3.pulse.adapters.AdapterCourseDetails;

import java.util.ArrayList;


public class ActivityCourseDetails extends AppCompatActivity {

    ListView listView;
    AdapterCourseDetails adapter;

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        listView = (ListView)findViewById(R.id.courseListView);
        adapter = new AdapterCourseDetails(this,R.layout.custom_row_course_details);
        listView.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();

        switch (intent.getIntExtra("course",0)){
            case 0:
                setAdapter(ActivityMain.tinyDB.getListString("course1"));

                break;
            case 1:
                setAdapter(ActivityMain.tinyDB.getListString("course2"));

                break;
            case 2:
                setAdapter(ActivityMain.tinyDB.getListString("course3"));

                break;
            case 3:
                setAdapter(ActivityMain.tinyDB.getListString("course4"));

                break;
            case 4:
                setAdapter(ActivityMain.tinyDB.getListString("course5"));

                break;
            case 5:
                setAdapter(ActivityMain.tinyDB.getListString("course6"));

                break;
            case 6:
                setAdapter(ActivityMain.tinyDB.getListString("course7"));


                break;
            case 7:
                setAdapter(ActivityMain.tinyDB.getListString("course8"));


                break;
            case 8:
                setAdapter(ActivityMain.tinyDB.getListString("course9"));


                break;
            case 9:
                setAdapter(ActivityMain.tinyDB.getListString("course10"));


                break;
            case 10:
                setAdapter(ActivityMain.tinyDB.getListString("course11"));

                break;
            case 11:
                setAdapter(ActivityMain.tinyDB.getListString("course12"));


                break;
            case 12:
                setAdapter(ActivityMain.tinyDB.getListString("course13"));

                break;
            case 13:
                setAdapter(ActivityMain.tinyDB.getListString("course14"));

                break;
            case 14:
                setAdapter(ActivityMain.tinyDB.getListString("course15"));

                break;
            case 15:
                setAdapter(ActivityMain.tinyDB.getListString("course16"));

                break;

            default:
                break;

        }


        //-------------//




    }



    //set adapter and add results
    public void setAdapter(ArrayList<String> course){

        ArrayList<String>id = ActivityMain.tinyDB.getListString("course0");
        ArrayList<String>resultsId = ActivityMain.tinyDB.getListString("courseResultsId"+intent.getIntExtra("course",0));
        id.addAll(resultsId);
        ArrayList<String>info = course;
        ArrayList<String>resultsInfo = ActivityMain.tinyDB.getListString("courseResultsInfo"+intent.getIntExtra("course",0));
        info.addAll(resultsInfo);

        int i = 0;
        for (String s : id) {

            DPCourseDetails obj = new DPCourseDetails(s,info.get(i));
            adapter.add(obj);
            i++;
        }


    }





}
