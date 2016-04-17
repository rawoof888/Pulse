package com.hive3.pulse.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hive3.pulse.DataProviders.DPCourseDetails;
import com.hive3.pulse.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterCourseDetails extends ArrayAdapter {


    private List list = new ArrayList<>();

    public AdapterCourseDetails(Context context, int resource) {
        super(context, resource);

    }

    @Override
    public void add(Object object) {
        list.add(object);
        super.add(object);
    }

    static class DataHolder{
        TextView course;
        TextView course_info;
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
            row = inflater.inflate(R.layout.custom_row_course_details,parent,false);

            dataHolder = new DataHolder();

            dataHolder.course = (TextView) row.findViewById(R.id.course);
            dataHolder.course_info = (TextView) row.findViewById(R.id.course_info);
            row.setTag(dataHolder);

        }else {
            dataHolder = (DataHolder) row.getTag();
        }

        DPCourseDetails courseDataProvider = (DPCourseDetails) getItem(position);
        dataHolder.course.setText(courseDataProvider.getCourse());
        dataHolder.course_info.setText(courseDataProvider.getCourseInfo());


        return row;
    }






}
