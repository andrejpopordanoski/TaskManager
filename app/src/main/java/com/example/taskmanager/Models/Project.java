package com.example.taskmanager.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


public class Project implements Parcelable {
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

    protected Project(Parcel in) {
        name = in.readString();
        collaborators = in.createTypedArrayList(Collaborator.CREATOR);
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(collaborators);
    }
}
