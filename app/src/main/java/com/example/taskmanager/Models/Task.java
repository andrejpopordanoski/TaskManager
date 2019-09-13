package com.example.taskmanager.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class Task implements Parcelable {
    public String name;
    public String description;
    public String priority;
    public CustomDate esimateDateTo;
    public Collaborator assignee;
    public String taskState;
    public String projectID;


    public Task() {
    }

    public Task(String name, String description, String priority, Calendar esitmate, Collaborator assignee, String taskState, String projectID) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.esimateDateTo = new CustomDate(esitmate.get(Calendar.YEAR), esitmate.get(Calendar.MONTH), esitmate.get(Calendar.DAY_OF_MONTH), DateFormat.getDateInstance(DateFormat.FULL).format(esitmate.getTime()));

        this.assignee = assignee;
        this.taskState = taskState;
        this.projectID = projectID;

    }


    protected Task(Parcel in) {
        name = in.readString();
        description = in.readString();
        priority = in.readString();
        esimateDateTo = in.readParcelable(CustomDate.class.getClassLoader());
        assignee = in.readParcelable(Collaborator.class.getClassLoader());
        taskState = in.readString();
        projectID = in.readString();
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

    /**
     *  My defined methods here
     */

    public String getDateFormatted() {
        return esimateDateTo.toString();
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
        dest.writeParcelable(esimateDateTo, flags);
        dest.writeParcelable(assignee, flags);
        dest.writeString(taskState);
        dest.writeString(projectID);
    }
}
