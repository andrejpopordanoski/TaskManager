package com.example.taskmanager.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class MeetingAttendee implements Parcelable {
    private String userId;
    private String email;


    public MeetingAttendee(){}
    public MeetingAttendee(String uid, String email){
        this.userId = uid;
        this.email = email;
    }
    protected MeetingAttendee(Parcel in) {
        userId = in.readString();
        email = in.readString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static final Creator<MeetingAttendee> CREATOR = new Creator<MeetingAttendee>() {
        @Override
        public MeetingAttendee createFromParcel(Parcel in) {
            return new MeetingAttendee(in);
        }

        @Override
        public MeetingAttendee[] newArray(int size) {
            return new MeetingAttendee[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(email);
    }
}
