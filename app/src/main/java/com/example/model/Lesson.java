package com.example.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Lesson implements Serializable {
    private int id;
    private Bitmap image;
    private String nameItem;
    private int progress;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getNameItem() {
        return nameItem;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Lesson() {
    }

    public Lesson(int id, Bitmap image, String nameItem, int progress) {
        this.id = id;
        this.image = image;
        this.nameItem = nameItem;
        this.progress = progress;
    }
}
