package com.example.model;

import android.graphics.Bitmap;

public class LessonItem {
    private int id;
    private String question;
    private Bitmap image;
    private String correctAnswer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public LessonItem() {
    }

    public LessonItem(int id, String question, Bitmap image, String correctAnswer) {
        this.id = id;
        this.question = question;
        this.image = image;
        this.correctAnswer = correctAnswer;
    }
}
