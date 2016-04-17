package com.hive3.pulse;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import com.hive3.pulse.DataProviders.DPCourseDetails;
import com.hive3.pulse.adapters.AdapterCourseDetails;

import java.util.ArrayList;


public class ActivityCourseDetails extends AppCompatActivity {

    ListView listView;
    AdapterCourseDetails adapter;


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

        Intent intent = getIntent();

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




    public void setAdapter(ArrayList<String> course){
        int i = 0;

        for (String s : ActivityMain.tinyDB.getListString("course0")) {
            while (ActivityMain.tinyDB.getListString("course0").size() != course.size()){
                course.add("");
            }
            DPCourseDetails obj = new DPCourseDetails(s,course.get(i));
            adapter.add(obj);
            i++;
        }

    }




}
