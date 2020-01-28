package com.example.taskmanager.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

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
    public boolean inProgress;
    public boolean testFailed;


    public Task() {
        name = "default";
        description = "default";
        priority = "default";
        esimateDateTo = new CustomDate();
        assignee = new Collaborator();
        taskState = "default";
        projectID = "default";
        inProgress = false;
        testFailed = false;
    }

    public Task(String name, String description, String priority, Calendar esitmate, Collaborator assignee, String taskState, String projectID) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.esimateDateTo = new CustomDate(esitmate.get(Calendar.YEAR), esitmate.get(Calendar.MONTH), esitmate.get(Calendar.DAY_OF_MONTH), DateFormat.getDateInstance(DateFormat.FULL).format(esitmate.getTime()));

        this.assignee = assignee;
        this.taskState = taskState;
        this.projectID = projectID;
        inProgress = false;
        testFailed = false;
    }


    protected Task(Parcel in) {
        name = in.readString();
        description = in.readString();
        priority = in.readString();
        esimateDateTo = in.readParcelable(CustomDate.class.getClassLoader());
        assignee = in.readParcelable(Collaborator.class.getClassLoader());
        taskState = in.readString();
        projectID = in.readString();
        inProgress = in.readByte() != 0;
        testFailed = in.readByte() != 0;
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



    public void setInProgress() {
        inProgress = true;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Task other = (Task) obj;
        return this.projectID.equals(other.projectID) && this.name.equals(other.name)  && this.description.equals(other.description);
    }

    public void changeState(){
        if(this.taskState.equals("TODO")){
            this.taskState = "TEST";
            this.inProgress = false;
        }
        else if (this.taskState.equals("TEST")){
            this.taskState = "DONE";
            this.inProgress = false;

        }
        else {
            this.taskState = "TODO";
            this.inProgress = false;
        }
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
        dest.writeByte((byte) (inProgress ? 1 : 0));
        dest.writeByte((byte) (testFailed ? 1 : 0));
    }
}
