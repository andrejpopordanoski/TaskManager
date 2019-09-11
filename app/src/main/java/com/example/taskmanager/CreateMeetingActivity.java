package com.example.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.taskmanager.Models.Meeting;
import com.example.taskmanager.Models.MeetingAttendee;
import com.example.taskmanager.Models.User;
import com.example.taskmanager.Models.UserMeeting;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CreateMeetingActivity extends AppCompatActivity {
    private EditText meetingName;
    private EditText location;
    private EditText date;
    private EditText meetingDescription;
    private EditText agenda;
    private List<MeetingAttendee> attendees;
    private Button createMeeting;

    //Firebase stuff
    private FirebaseDatabase firebaseDatabaseInstance;
    private String currentUserEmail;
    private FirebaseAuth firebaseAuthInstance;
    private FirebaseUser currentFirebaseUser;
    private DatabaseReference dbReferenceUsers;
    private DatabaseReference dbReferenceCurrentUser;
    private DatabaseReference dbReferenceMeetings;

    private Intent parentIntent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        setUpViews();

        parentIntent = getIntent();

        setUpFirebase();

        createMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMeetingAndUpdateDatabase();
            }
        });



    }

    private void createMeetingAndUpdateDatabase() {

        if (checkIfFormIsValid()){
            //create new meeting and add it to database
            Meeting meeting = new Meeting(meetingName.getText().toString(),location.getText().toString()
                    , meetingDescription.getText().toString(),agenda.getText().toString(),date.getText().toString());
            final String meetingKey = dbReferenceMeetings.push().getKey();
            dbReferenceMeetings.child(meetingKey).setValue(meeting);
            final UserMeeting userMeeting = new
                    UserMeeting(meetingKey,meetingName.getText().toString(),meetingDescription.getText().toString());

            //update the users/attendees with the new meeting

            for (final MeetingAttendee meetingAttendee : attendees){
                final DatabaseReference dbReferenceAttendee = dbReferenceUsers.child(meetingAttendee.getUserId());

                dbReferenceAttendee.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        user.addMeeting(userMeeting);
                        dbReferenceAttendee.setValue(user);

                        if(attendees.indexOf(meetingAttendee) == attendees.size()-1){
                            setResult(Activity.RESULT_OK, parentIntent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    private boolean checkIfFormIsValid() {
        boolean validity = true;
        if (meetingName.getText().toString().isEmpty()){
            validity = false;
            meetingName.setError("Meeting must have a name");
        }
        if (location.getText().toString().isEmpty()){
            validity = false;
            location.setError("Meeting must have a location");
        }
        if (date.getText().toString().isEmpty()){
            validity = false;
            date.setError("Meeting must have a date");
        }
        if (meetingDescription.getText().toString().isEmpty()){
            validity = false;
            meetingDescription.setError("Meeting must have a description");
        }
        if (agenda.getText().toString().isEmpty()){
            validity = false;
            agenda.setError("Meeting must have an agenda");
        }
        return validity;
    }

    /**
     * Links the views to instance variables
     */
    private void setUpViews(){
        meetingName =  findViewById(R.id.create_meeting_name);
        location =  findViewById(R.id.create_meeting_location);
        date = findViewById(R.id.create_meeting_date);
        meetingDescription = findViewById(R.id.create_meeting_description);
        agenda = findViewById(R.id.create_meeting_agenda);
        createMeeting = findViewById(R.id.create_project_button);
    }
    /**
     * set up firebase method, sets up the instance variables related to the database
     */
    private void setUpFirebase(){
        firebaseAuthInstance = FirebaseAuth.getInstance();
        firebaseDatabaseInstance = FirebaseDatabase.getInstance();
        currentFirebaseUser = firebaseAuthInstance.getCurrentUser();
        currentUserEmail = currentFirebaseUser.getEmail();
        dbReferenceUsers = firebaseDatabaseInstance.getReference().child("users");
        dbReferenceMeetings = firebaseDatabaseInstance.getReference().child("meetings");
        dbReferenceCurrentUser = firebaseDatabaseInstance.getReference().child("users").child(currentFirebaseUser.getUid());

        //add the user creating the meeting as the initial attendee
        attendees = new ArrayList<>();
        attendees.add(new MeetingAttendee(currentFirebaseUser.getUid(),currentUserEmail));


    }
}
