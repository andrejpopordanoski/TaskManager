package com.example.taskmanager.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Meeting {
    private String id;
    private String name;
    private String location;
    private String description;
    private String agenda;
    private List<Note> notes;
    private String time;
    private List<User> users;

    public  Meeting(){}

    public Meeting(String id,String name, String location, String description, String agenda, String time) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.agenda = agenda;
        this.notes = new ArrayList<>();
        this.time = time;
        this.users = new ArrayList<>();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return name.equals(meeting.name) &&
                location.equals(meeting.location) &&
                description.equals(meeting.description) &&
                agenda.equals(meeting.agenda) &&
                notes.equals(meeting.notes) &&
                time.equals(meeting.time) &&
                users.equals(meeting.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location, description, agenda, notes, time, users);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
