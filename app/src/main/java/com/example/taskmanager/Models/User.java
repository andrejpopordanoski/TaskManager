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
        meetingList = new ArrayList<>();
        projectList = new ArrayList<>();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setProjectList(List<UserProject> projectList) {
        this.projectList = projectList;
    }

    public List<UserMeeting> getMeetingList() {
        if (meetingList != null)
            return meetingList;
        return new ArrayList<>();

    }

    public void setMeetingList(List<UserMeeting> meetingList) {
        this.meetingList = meetingList;
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
