package com.example.taskmanager.Classes;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public  String id;
    public String name;
    public String phone;
    public  String address;
    public User() {

    }
    public User(String id, String name, String phone, String address){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }


}
