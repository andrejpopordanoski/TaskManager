package com.example.taskmanager.Models;

import com.google.firebase.database.IgnoreExtraProperties;

// enum collaboratorType  {
//     Developer,
//     Tester,
//     Manager
// }
@IgnoreExtraProperties
public class Collaborator {
    public String id;
    public String collabType;

    public Collaborator() {

    }

    public Collaborator(String id, String collabType) {
        this.id = id;
        this.collabType = collabType;
    }

    public String getId() {
        return id;
    }

    public String getCollabType() {
        return collabType;
    }
}
