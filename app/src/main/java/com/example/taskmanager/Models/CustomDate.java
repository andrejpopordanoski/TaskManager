package com.example.taskmanager.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomDate implements Parcelable {
    public int year;
    public int month;
    public int dayOfMonth;
    public String dateFormat;

    public CustomDate () {

    }
    public CustomDate(int year, int month, int dayOfMonth, String dateFormat) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.dateFormat = dateFormat;
    }


    protected CustomDate(Parcel in) {
        year = in.readInt();
        month = in.readInt();
        dayOfMonth = in.readInt();
        dateFormat = in.readString();
    }

    public static final Creator<CustomDate> CREATOR = new Creator<CustomDate>() {
        @Override
        public CustomDate createFromParcel(Parcel in) {
            return new CustomDate(in);
        }

        @Override
        public CustomDate[] newArray(int size) {
            return new CustomDate[size];
        }
    };

    @Override
    public String toString() {
        return dateFormat;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(dayOfMonth);
        dest.writeString(dateFormat);
    }
}