package com.example.taskmanager.Models;

public class UserMeeting {
    public String meetingId;
    public String meetingName;
    public String meetingDescription;
    public UserMeeting() {
    }

    public UserMeeting(String meetingId, String meetingName, String meetingDescription) {
        this.meetingId = meetingId;
        this.meetingName = meetingName;
        this.meetingDescription = meetingDescription;
    }

}
