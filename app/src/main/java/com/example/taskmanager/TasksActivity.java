package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.taskmanager.Adapters.ProfileProjectMeetingsVPagerAdapter;
import com.example.taskmanager.Adapters.TasksVPAdapter;
import com.example.taskmanager.Fragments.TasksTODOFragment;
import com.google.android.material.tabs.TabLayout;

public class TasksActivity extends AppCompatActivity implements TasksTODOFragment.OnFragmentInteractionListener {
    private final String TAG = "TasksActivity";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        tabLayout = (TabLayout)findViewById(R.id.profile_tablayout);
        viewPager = (ViewPager)findViewById(R.id.profile_viewpager);

        tabLayout.addTab(tabLayout.newTab().setText("TO DO"));
        tabLayout.addTab(tabLayout.newTab().setText("READY FOR TEST"));
        tabLayout.addTab(tabLayout.newTab().setText("DONE"));

        TasksVPAdapter pagerAdapter = new TasksVPAdapter(getSupportFragmentManager(), tabLayout.getTabCount() );
        viewPager.setAdapter(pagerAdapter );
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.i(TAG, tab.getPosition() + "");
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
