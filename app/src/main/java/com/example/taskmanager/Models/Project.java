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

    public boolean isUserFromType(String uID, String type){
        for (Collaborator c:
                this.collaborators) {
            if(c.uId.equals(uID)){
                if (c.collabType.equals(type)){
                    return true;
                }
            }
        }
        return false;
    }

    public List<Collaborator> getAllCollabsFromType(String type){
        List<Collaborator> collabs = new ArrayList<>();
        for (Collaborator c:
             this.collaborators) {
            if (c.collabType.equals(type)){
                collabs.add(c);
            }
        }
        return collabs;
    }

}
