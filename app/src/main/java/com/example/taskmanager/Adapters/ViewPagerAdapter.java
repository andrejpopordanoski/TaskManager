package com.example.taskmanager.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.taskmanager.Fragments.ContactsFragment;
import com.example.taskmanager.Fragments.ProjectsFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private int numberOfTabs;
    public ViewPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ProjectsFragment();
            case 1:
                return new ContactsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.numberOfTabs;
    }
}
