package com.example.taskmanager.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;


public class Project implements Parcelable {
    public String name;
    public String projectId;
    public List<Collaborator> collaborators;
    public List<Task> tasks;
    public  Project(){
        name="Name-default";
        projectId="defaultid";
        collaborators = new ArrayList<>();
        tasks = new ArrayList<>();
    }
    public Project(String name, String projectId) {
        this.name = name;
        this.projectId = projectId;
        collaborators = new ArrayList<>();
        tasks = new ArrayList<>();
    }
    public Project(String name, String projectId, List<Collaborator> collaborators) {
        this.name = name;
        this.projectId = projectId;
        this.collaborators = collaborators;
        this.tasks = new ArrayList<Task>();

    }
    public Project(String name, String projectId, List<Collaborator> collaborators, List<Task> tasks) {
        this.name = name;
        this.projectId = projectId;
        this.collaborators = collaborators;
        this.tasks = tasks;
    }


    protected Project(Parcel in) {
        name = in.readString();
        projectId = in.readString();
        collaborators = in.createTypedArrayList(Collaborator.CREATOR);
        tasks = in.createTypedArrayList(Task.CREATOR);
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
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(projectId);
        dest.writeTypedList(collaborators);
        dest.writeTypedList(tasks);
    }
    /** This is where my defined methods are
     *
     * @param uID
     * @param type
     * @return
     */
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

    public void addCollaborator(Collaborator collab){
        if(collaborators == null){
            collaborators = new ArrayList<>();
        }
        collaborators.add(collab);
    }
    public void addTask(Task task){
        if(tasks == null){
            tasks = new ArrayList<>();
        }
        tasks.add(task);
    }

    public List<Task> getAllTasksFromState(String state){
        List<Task> tasks = new ArrayList<>();
        if(this.tasks != null) {
            for (Task t : this.tasks) {
                if (t.taskState.equals(state)) {
                    tasks.add(t);
                }
            }

        }
        return tasks;
    }
    public List<Task> getAllTaksksFromStateAndCollaborator(String state, String collabEmail){
        List<Task> tasks = new ArrayList<>();
        if(this.tasks != null) {
            for (Task t : this.tasks) {
                if (t.taskState.equals(state) && t.assignee.mail.equals(collabEmail)) {
                    tasks.add(t);
                }
            }

        }
        return tasks;
    }

    public boolean deleteCollaboratorWithEmail(String email) {
        boolean isDeleted = false;
        for(Collaborator c:collaborators){
            if(c.mail.equals(email)){
                collaborators.remove(c);
                isDeleted = true;
                break;
            }
        }
        return isDeleted;
    }

    public boolean deleteTaskForCollaborator(Collaborator collab){
        for(Task t:tasks){
            if(t.assignee.equals(collab)){
                tasks.remove(t);
                return true;
            }
        }
        return false;
    }

    public Task changeTaskState(Task task){
        for(Task t:tasks){
            if(t.equals(task)){
                t.changeState();
                return t;
            }
        }
        return null;
    }

    public boolean setTaskInProgress(Task task){
        for(Task t:tasks){
            if(t.equals(task)){
                t.inProgress = true;
                return true;
            }
        }
        return false;
    }


    public boolean removeTask(Task currentTask) {
        for(Task t:tasks){
            if(t.equals(currentTask)){
                tasks.remove(t);
                return true;
            }
        }
        return false;
    }
}
