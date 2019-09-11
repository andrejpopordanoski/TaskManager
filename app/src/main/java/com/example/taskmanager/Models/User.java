package com.example.taskmanager.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class User {
    public String email;
    public String name;
    public String phone;
    public  String address;
    public List<UserProject> projectList;
    public List<UserMeeting> meetingList;
//    public ArrayList<String> someStrings = new ArrayList<String>(Arrays.asList("Hello", "Yes hello"));
    public User() {

    }
    public User(String email, String name, String phone, String address){
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.projectList = new ArrayList<UserProject>();
        this.meetingList = new ArrayList<>();

    }

    public void addProject(UserProject p){
        if (projectList == null)
            projectList = new ArrayList<>();
        projectList.add(p);
    }
    public void addMeeting(UserMeeting meeting){
        if (meetingList == null){
            meetingList = new ArrayList<>();
        }
        meetingList.add(meeting);
    }

    public List<UserProject> getProjectList() {
        if (projectList != null){
            return projectList;
        }
        else return new ArrayList<UserProject>();
    }

    @Override
    public String toString(){
        return email + name + phone + address;
    }

}
