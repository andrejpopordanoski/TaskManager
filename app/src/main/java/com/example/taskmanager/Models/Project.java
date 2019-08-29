package com.example.taskmanager.Models;

import java.util.ArrayList;
import java.util.List;

public class Project {
    public String name;
    public List<Collaborator> collaborators;

    public Project(String name, List<Collaborator> collaborators) {
        this.name = name;
        this.collaborators = collaborators;
    }

    public  Project(){}
    public Project(String name) {
        this.name = name;
        collaborators = new ArrayList<>();
    }
}
