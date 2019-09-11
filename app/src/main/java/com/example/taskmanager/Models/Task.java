package com.example.taskmanager.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;

public class Task implements Parcelable {
    public String name;
    public String description;
    public String priority;
    public Calendar esitmate;
    public Collaborator assignee;
    public String taskState;
    public String projectID;


    public Task() {
    }

    public Task(String name, String description, String priority, Calendar esitmate, Collaborator assignee, String taskState, String projectID) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.esitmate = esitmate;

        this.assignee = assignee;
        this.taskState = taskState;
        this.projectID = projectID;

    }

    protected Task(Parcel in) {
        name = in.readString();
        description = in.readString();
        priority = in.readString();
        assignee = in.readParcelable(Collaborator.class.getClassLoader());
        taskState = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public String toString(){
        return name + " " + description + " " + priority +  " " + esitmate.toString() + " " + assignee.mail + " " + taskState;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(priority);
        dest.writeParcelable(assignee, flags);
        dest.writeString(taskState);
    }

    /**
     *  My defined methods here
     */

    public String getDateFormatted() {
        return esitmate.get(Calendar.YEAR) + ", " + esitmate.get(Calendar.MONTH) + ", " + esitmate.get(Calendar.DAY_OF_MONTH);
    }
}
