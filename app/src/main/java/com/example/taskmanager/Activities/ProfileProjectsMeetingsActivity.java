package com.example.taskmanager.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.taskmanager.Adapters.ProfileProjectMeetingsVPagerAdapter;
import com.example.taskmanager.Adapters.ProjectListAdapter;
import com.example.taskmanager.Fragments.MeetingsFragment;
import com.example.taskmanager.Fragments.ProjectsFragment;
import com.example.taskmanager.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileProjectsMeetingsActivity extends AppCompatActivity implements MeetingsFragment.OnFragmentInteractionListener, ProjectsFragment.OnFragmentInteractionListener {

    public final String TAG = "ProfileProjectsMeetings";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView welcomeText;
    private FirebaseAuth firebaseAuth;
    private ProjectListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_tasks);

        tabLayout = (TabLayout)findViewById(R.id.profile_tablayout);
        viewPager = (ViewPager)findViewById(R.id.profile_viewpager);

        tabLayout.addTab(tabLayout.newTab().setText("Projects"));
        tabLayout.addTab(tabLayout.newTab().setText("Meetings"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        ProfileProjectMeetingsVPagerAdapter pagerAdapter = new ProfileProjectMeetingsVPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount() );
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

//        welcomeText = (TextView) findViewById(R.id.welcome_text);
        firebaseAuth = FirebaseAuth.getInstance();

//        welcomeText.setText("Welcome " + firebaseAuth.getCurrentUser().getUid());

    }

    public void setAdapter(ProjectListAdapter adapter){
        this.adapter = adapter;
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.open_tasks:
                adapter.openTask(item.getGroupId());
                return true;
            case R.id.edit_settings:
                adapter.openEditActivity(item.getGroupId());
                return true;


        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult for parent");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void random(){

    }
}
