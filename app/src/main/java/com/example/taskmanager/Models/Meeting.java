package com.example.taskmanager.Models;

import java.util.Date;

public class Meeting {
    String location;
    String description;
    String agenda;
    String momentsOfMeeting;
    Date time;

    public  Meeting(){}

    public Meeting(String location, String description, String agenda, String momentsOfMeeting, Date time) {
        this.location = location;
        this.description = description;
        this.agenda = agenda;
        this.momentsOfMeeting = momentsOfMeeting;
        this.time = time;
    }
}
