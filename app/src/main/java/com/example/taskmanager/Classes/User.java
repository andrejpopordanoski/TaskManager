package com.example.taskmanager.Classes;

import android.widget.ArrayAdapter;

import com.example.taskmanager.Models.Project;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@IgnoreExtraProperties
public class User {
    public String email;
    public String name;
    public String phone;
    public  String address;
    List<Project> projectList;
//    public ArrayList<String> someStrings = new ArrayList<String>(Arrays.asList("Hello", "Yes hello"));
    public User() {

    }
    public User(String email, String name, String phone, String address){
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.address = address;

    }

    public void addProject(Project p){
        projectList.add(p);
    }
    @Override
    public String toString(){
        return email + name + phone + address;
    }
    public void initializeProjectList() {
        projectList = new ArrayList<>();
    }

}
