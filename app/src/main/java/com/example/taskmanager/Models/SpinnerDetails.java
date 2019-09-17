package com.example.taskmanager.Models;

public class SpinnerDetails {
    private String name;
    private String email;

    public SpinnerDetails() {

    }
    public SpinnerDetails(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
