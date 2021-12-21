package com.example.moodairy_v1;

public class Notes {//מחלקה שיוצרת משתנה מטיפוס חדש
    public String key;
    public String note, date, uid;


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Notes(String note, String date, String uid, String key) {
        this.note = note;
        this.date = date;
        this.uid = uid;
        this.key = key;
    }
    public Notes (){}
}
