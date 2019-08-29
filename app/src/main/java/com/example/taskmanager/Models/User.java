package com.example.taskmanager.Models;

import java.util.ArrayList;
import java.util.List;

public class User {
    String name;
    String surname;
    String email;
    String password;
    List<Project> projectList;

    public  User(){}

    public User(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        projectList = new ArrayList<>();
    }

    public void addProject(Project p){
        projectList.add(p);
    }
}
