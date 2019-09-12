package com.example.taskmanager.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Collaborator implements Parcelable {

    public String uId;
    public String collabType;
    public String mail;

    public Collaborator() {

    }

    public Collaborator(String id, String collabType, String mail) {
        this.uId = id;
        this.collabType = collabType;
        this.mail = mail;
    }

    protected Collaborator(Parcel in) {
        uId = in.readString();
        collabType = in.readString();
        mail = in.readString();
    }

    public static final Creator<Collaborator> CREATOR = new Creator<Collaborator>() {
        @Override
        public Collaborator createFromParcel(Parcel in) {
            return new Collaborator(in);
        }

        @Override
        public Collaborator[] newArray(int size) {
            return new Collaborator[size];
        }
    };

    public String getId() {
        return uId;
    }

    public String getCollabType() {
        return collabType;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Collaborator other = (Collaborator) obj;
        return other.uId.equals(uId);
    }
    @Override
    public String toString() {
        return mail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uId);
        dest.writeString(collabType);
        dest.writeString(mail);
    }
}
