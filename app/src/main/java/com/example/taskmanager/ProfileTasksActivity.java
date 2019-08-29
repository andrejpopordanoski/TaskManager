package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.taskmanager.Adapters.ViewPagerAdapter;
import com.example.taskmanager.Fragments.ContactsFragment;
import com.example.taskmanager.Fragments.ProjectsFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileTasksActivity extends AppCompatActivity implements ContactsFragment.OnFragmentInteractionListener, ProjectsFragment.OnFragmentInteractionListener {

    public final String TAG = "ProfileTasksActivity";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView welcomeText;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_tasks);

        tabLayout = (TabLayout)findViewById(R.id.profile_tablayout);
        viewPager = (ViewPager)findViewById(R.id.profile_viewpager);

        tabLayout.addTab(tabLayout.newTab().setText("Projects"));
        tabLayout.addTab(tabLayout.newTab().setText("Contacts"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount() );
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
