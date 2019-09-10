package com.example.taskmanager.Adapters;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.taskmanager.Fragments.TasksDONEFragment;
import com.example.taskmanager.Fragments.TasksTESTSFragment;
import com.example.taskmanager.Fragments.TasksTODOFragment;
import com.example.taskmanager.Models.Project;

public class TasksVPAdapter extends FragmentStatePagerAdapter {

    private int numberOfTabs;
    private Project currentProject;
    public TasksVPAdapter(FragmentManager fm, int numberOfTabs, Project currentProject) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
        this.currentProject = currentProject;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("TasksVPAdapter", position + "");

        switch (position){
            case 0:
                return new TasksTODOFragment(this.currentProject);
            case 1:
                return new TasksTESTSFragment(this.currentProject);
            case 2:
                return new TasksDONEFragment(this.currentProject);
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return this.numberOfTabs;
    }
}
