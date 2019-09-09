package com.example.taskmanager.Adapters;

import android.nfc.Tag;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.taskmanager.Fragments.ContactsFragment;
import com.example.taskmanager.Fragments.ProjectsFragment;
import com.example.taskmanager.Fragments.TasksTODOFragment;

public class TasksVPAdapter extends FragmentStatePagerAdapter {

    private int numberOfTabs;
    public TasksVPAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("TasksVPAdapter", position + "");

        switch (position){
            case 0:
                return new TasksTODOFragment();
            case 1:
                return new TasksTODOFragment();
            case 2:
                return new TasksTODOFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return this.numberOfTabs;
    }
}
