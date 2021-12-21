package com.example.moodairy_v1;

public class Name {//מחלקה שיוצרת משתנה מטיפוס חדש
    public Name(String name, String uid, String key) {
        this.name = name;
        this.uid = uid;
        this.key = key;
    }
    public Name(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String name, uid, key;
}
