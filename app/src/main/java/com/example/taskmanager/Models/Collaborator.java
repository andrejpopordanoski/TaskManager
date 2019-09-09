package com.example.taskmanager.Models;

import androidx.annotation.Nullable;

import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Collaborator {

    public String uId;
    public String collabType;

    public Collaborator() {

    }

    public Collaborator(String id, String collabType) {
        this.uId = id;
        this.collabType = collabType;
    }

    public String getId() {
        return uId;
    }

    public String getCollabType() {
        return collabType;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Collaborator other = (Collaborator) obj;
        return other.uId.equals(uId) && other.collabType.equals(collabType);
    }
}
