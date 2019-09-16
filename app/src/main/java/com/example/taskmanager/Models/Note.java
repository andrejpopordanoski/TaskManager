package com.example.taskmanager.Models;

public class Note {

    private String userName;
    private String content;
    private String meetingId;

    public Note(){}
    public Note(String userName, String content, String meetingId) {
        this.userName = userName;
        this.content = content;
        this.meetingId = meetingId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }
}
