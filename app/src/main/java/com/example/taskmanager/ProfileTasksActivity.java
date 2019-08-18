package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileTasksActivity extends AppCompatActivity {


    private TextView welcomeText;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_tasks);
        welcomeText = (TextView) findViewById(R.id.welcome_text);
        firebaseAuth = FirebaseAuth.getInstance();

        welcomeText.setText("Welcome " + firebaseAuth.getCurrentUser().getEmail());

    }
}
