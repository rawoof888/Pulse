package com.hive3.pulse.DataProviders;

/**
 * Created by rawoof on 26/03/16.
 */
public class DPCourseDetails {


    private String course;
    private String course_Info;

    public DPCourseDetails(String course,String course_Info){
        super();
        this.setCourse(course);
        this.setCourseInfo(course_Info);
    }



    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCourseInfo() {
        return course_Info;
    }

    public void setCourseInfo(String courseInfo) {
        this.course_Info = courseInfo;
    }




}
