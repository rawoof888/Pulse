package com.hive3.pulse.DataProviders;

public class DPCourses {



    private String course;
    private String course_info;

    public DPCourses(String course, String course_info){

        super();
        this.setCourse(course);
        this.setCourse_info(course_info);

    }



    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCourse_info() {
        return course_info;
    }

    public void setCourse_info(String course_info) {
        this.course_info = course_info;
    }






}
