package com.example.taskmanager.Models;

public class User {
    String name;
    String surname;
    String email;
    String password;

    public  User(){}

    public User(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }
}
