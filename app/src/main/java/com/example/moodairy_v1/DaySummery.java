package com.example.moodairy_v1;

public class DaySummery { //מחלקה זאת מייצרת את משתנה שמכיל נתוני המשתמש
    Meditations meditation_time;
    Notes note;
    Mood avarge_mood;

    public Meditations getMeditation_time() {
        return meditation_time;
    }

    public void setMeditation_time(Meditations meditation_time) {
        this.meditation_time = meditation_time;
    }

    public Notes getNote() {
        return note;
    }

    public void setNote(Notes note) {
        this.note = note;
    }

    public Mood getAvarge_mood() {
        return avarge_mood;
    }

    public void setAvarge_mood(Mood avarge_mood) {
        this.avarge_mood = avarge_mood;
    }

    public DaySummery(Meditations meditation_time, Notes note, Mood avarge_mood) {
        this.meditation_time = meditation_time;
        this.note = note;
        this.avarge_mood = avarge_mood;
    }
}
