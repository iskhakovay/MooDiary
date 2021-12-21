package com.example.moodairy_v1;

public class Mood {//מחלקה שיוצרת משתנה מטיפוס חדש

    public int getMood_id() {
        return mood_id;
    }

    public void setMood_id(int mood_id) {
        this.mood_id = mood_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public int mood_id;
    public String uid, key,date;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Mood(int mood_id, String date, String uid, String key) {
        this.date = date;
        this.mood_id = mood_id;
        this.uid = uid;
        this.key = key;
    }
    public Mood(){}


}
