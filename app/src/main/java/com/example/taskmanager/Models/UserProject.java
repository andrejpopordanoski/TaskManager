package com.example.taskmanager.Models;

public class UserProject {
    public String projectID;
    public String role;

    public UserProject() {

    }
    public UserProject(String projectID, String role) {
        this.projectID = projectID;
        this.role = role;
    }
}
