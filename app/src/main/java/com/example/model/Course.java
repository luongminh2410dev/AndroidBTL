package com.example.model;


import android.graphics.Bitmap;

import java.io.Serializable;

public class Course implements Serializable {
    Bitmap icon;

    public Course(Bitmap icon, String nameCourse) {
        this.icon = icon;
        this.nameCourse = nameCourse;
    }

    String nameCourse;



    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getNameCourse() {
        return nameCourse;
    }

    public void setNameCourse(String nameCourse) {
        this.nameCourse = nameCourse;
    }

    public Course() {
    }


}
