package com.example.taskmanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.taskmanager.Fragments.Dialogs.DatePickerFragment;
import com.example.taskmanager.Models.Meeting;
import com.example.taskmanager.Models.MeetingAttendee;
import com.example.taskmanager.Models.User;
import com.example.taskmanager.Models.UserMeeting;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class CreateMeetingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText meetingName;
    private EditText location;
    private EditText meetingDescription;
    private EditText agenda;
    private String dateString="";
    private String timeString="";
    private LinearLayout dateLayout;
    private LinearLayout timeLayout;
    private TimePickerDialog picker;
    private TextView selectedTime;
    private TextView selectedDate;
    private EditText attendeeEmail;
    private FloatingActionButton addAttendeeFab;
    private List<MeetingAttendee> attendees;
    private Button createMeeting;
    private LinearLayout addAttendee;



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
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        setUpViews();

        parentIntent = getIntent();

        setUpFirebase();

        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();

                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(CreateMeetingActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                if (timeString.equals("")){
                                    timeString +=sHour + ":" + sMinute;
                                    selectedTime.setText(timeString);
                                }else {
                                    timeString = "" + sHour + ":" + sMinute;
                                    selectedTime.setText(timeString);
                                }

                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });


        addAttendee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMeetingAttendees();
            }
        });
        createMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMeetingAndUpdateDatabase();
            }
        });





    }

    private void addMeetingAttendees() {
        final String atnEmail = attendeeEmail.getText().toString();

        if (atnEmail.length() > 0){
            LayoutInflater inflater = getLayoutInflater();
            final View view = inflater.inflate(R.layout.attendee_view, null);
            final LinearLayout attendeeWrap = findViewById(R.id.create_meeting_attendees_wrap);

            final TextView email = view.findViewById(R.id.attendee_email);

            final Query getUsersQuery = dbReferenceUsers.orderByChild("email").equalTo(atnEmail);

            for (MeetingAttendee meetingAttendee: attendees){
                if (meetingAttendee.getEmail().equals(atnEmail)){
                    Toast.makeText(getBaseContext(),"Attendee already added!",Toast.LENGTH_LONG).show();
                    return;
                }

            }

            getUsersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                String  s = "";
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if(dataSnapshot.getValue() != null){
                        boolean isEmailOk = true;
                        String userID = "";
                        for(DataSnapshot childSnapshot:dataSnapshot.getChildren()){
                            userID = childSnapshot.getKey();
                            if(currentUserEmail.trim().equals(childSnapshot.child("email").getValue().toString().trim())) {
                                isEmailOk = false;
                                Toast.makeText(getBaseContext(), "You already are an attendee", Toast.LENGTH_LONG).show();
                            }


                        }

                        if (isEmailOk) {
                            email.setText(atnEmail);

                            attendees.add(new MeetingAttendee(userID,atnEmail));

                            attendeeEmail.setText("");
                            hideKeyboard(CreateMeetingActivity.this);

                            attendeeWrap.addView(view);
                        }
                    }
                    else {
                        Toast.makeText(getBaseContext(), "There is no user with that e-mail in the database, please try again", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getBaseContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }

            });

        }else {
            Toast.makeText(getBaseContext(),"Enter an email address",Toast.LENGTH_SHORT).show();
        }
    }

    private void createMeetingAndUpdateDatabase() {

        if (checkIfFormIsValid()){
            //create new meeting and add it to database

            final String meetingKey = dbReferenceMeetings.push().getKey();

            Meeting meeting = new Meeting(meetingKey ,meetingName.getText().toString(),location.getText().toString()
                    , meetingDescription.getText().toString(),agenda.getText().toString(),dateString + " " + timeString);
            dbReferenceMeetings.child(meetingKey).setValue(meeting);

            //create UserMeeting to store as a "relation" in users
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
        if (dateString.equals("")){
            validity = false;
            Toast.makeText(CreateMeetingActivity.this, "Meeting must have a date",Toast.LENGTH_SHORT).show();
        }
        if (timeString.equals("")){
            validity = false;
            Toast.makeText(CreateMeetingActivity.this, "Meeting must have a time set",Toast.LENGTH_SHORT).show();
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
        meetingDescription = findViewById(R.id.create_meeting_description);
        agenda = findViewById(R.id.create_meeting_agenda);
        createMeeting = findViewById(R.id.create_meeting_button);
        attendeeEmail = findViewById(R.id.create_meeting_collaborator_email);
        dateLayout = findViewById(R.id.create_meeting_date_layout);
        timeLayout = findViewById(R.id.create_meeting_time_layout);
        selectedDate = findViewById(R.id.create_meeting_selected_date);
        selectedTime = findViewById(R.id.create_meeting_selected_time);
        addAttendee = findViewById(R.id.create_meeting_submit_attendee);

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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        if(dateString.equals("")){
            dateString += currentDate;
            selectedDate.setText(dateString);
        }else {
            dateString = currentDate;
            selectedDate.setText(dateString);
        }
    }
}
