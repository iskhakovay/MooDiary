package com.example.moodairy_v1;

public class Phone { //מחלקה שיוצרת משתנה מטיפוס חדש
    public Phone(String phone, String uid, String key) {
        this.phone = phone;
        this.uid = uid;
        this.key = key;
    }

    public Phone() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String phone, uid, key;

}
