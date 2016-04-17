package com.hive3.pulse.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hive3.pulse.DataProviders.DPCourses;
import com.hive3.pulse.R;

import java.util.ArrayList;

public class AdapterCourses extends ArrayAdapter {

    private ArrayList<Object> list = new ArrayList();


    public AdapterCourses(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);

    }

    static class DataHolder{
        TextView course;
        TextView courseInfo;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View row = convertView;
        DataHolder dataHolder;

        if (convertView == null){

            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.custom_row_courses,parent,false);

            dataHolder = new DataHolder();
            dataHolder.course = (TextView) row.findViewById(R.id.course);
            dataHolder.courseInfo = (TextView)row.findViewById(R.id.courseInfo);
            row.setTag(dataHolder);

        }else {

            dataHolder = (DataHolder) row.getTag();

        }

        DPCourses dpCourses = (DPCourses) getItem(position);

        dataHolder.course.setText(dpCourses.getCourse());
        dataHolder.courseInfo.setText(dpCourses.getCourse_info());

        return row;
    }



}















