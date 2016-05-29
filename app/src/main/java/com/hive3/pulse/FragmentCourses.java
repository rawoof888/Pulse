package com.hive3.pulse;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.hive3.pulse.DataProviders.DPCourses;
import com.hive3.pulse.adapters.AdapterCourses;


import java.util.ArrayList;


public class FragmentCourses extends Fragment {

    ListView listView;
    public static AdapterCourses adapterCourses;


    public static ArrayList<String> courseCodes = new ArrayList<>();
    public static ArrayList<String> coursePercentages = new ArrayList<>();
    public static ArrayList<String> courseTitles = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_attendance,container,false);

        listView = (ListView)rootView.findViewById(R.id.listView);
        adapterCourses = new AdapterCourses(getContext(),R.layout.custom_row_courses);
        listView.setAdapter(adapterCourses);


        int courseListsSize = ActivityMain.tinyDB.getInt("courseListsSize",0);
        if (courseListsSize>0){

            coursePercentages =ActivityMain.tinyDB.getListString("coursePercentages");
            courseTitles = ActivityMain.tinyDB.getListString("courseTitles");
            Log.e("courseTitles",courseTitles.toString());
            setCustomAdapter();

        }



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ActivityCourseDetails.class);
                intent.putExtra("course", position);
                startActivity(intent);
            }
        });


        return rootView;
    }



    public void setCustomAdapter(){

        int i = 0;
        for (String coursePercentage : coursePercentages) {
            DPCourses obj = new DPCourses(coursePercentage,courseTitles.get(i));
            adapterCourses.add(obj);
            i++;
        }
        adapterCourses.notifyDataSetChanged();

        coursePercentages.clear();
        courseTitles.clear();

    }





}






























