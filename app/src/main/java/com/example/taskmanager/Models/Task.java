package com.example.taskmanager.Models;

import java.util.Date;

public class Task {
    String name;
    String description;
    int priority;
    Date esitmate;
    boolean includeEstimate;
    int phase;
    String type;
    boolean readyForTest;
    boolean testSuccess;
    String comment;
    String projectId;

    public Task() {
    }

    public Task(String name, String description, int priority, Date esitmate, boolean includeEstimate, int phase, String type, boolean readyForTest, boolean testSuccess, String comment, String projectId) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.esitmate = esitmate;
        this.includeEstimate = includeEstimate;
        this.phase = phase;
        this.type = type;
        this.readyForTest = readyForTest;
        this.testSuccess = testSuccess;
        this.comment = comment;
        this.projectId = projectId;
    }
}
