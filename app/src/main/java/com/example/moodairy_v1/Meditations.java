package com.example.moodairy_v1;

public class Meditations { //מחלקה שיוצרת משתנה מטיפוס חדש

    public long time;
    public String date;
    public String uid,key;

    public long getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public Meditations() {
    }

    public Meditations(long time, String date, String uid, String key) {
        this.time = time;
        this.date = date;
        this.uid = uid;
        this.key = key;
    }


}
