package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DetailedMeetingActivity extends AppCompatActivity {
    private TextView meetingName, meetingLocation, meetingDescription, meetingDate, meetingAgenda;
    private Button viewNotesButton,addNotesButton;
    private String meetingId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_meeting);
        setViews();
        Intent parentIntent = getIntent();
        setValuesToViewsFromIntentValues(parentIntent);

        viewNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext() , NotesActivity.class);
                intent.putExtra("Meeting id", meetingId);
                startActivity(intent);
            }
        });
        addNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),CreateNoteActivity.class);
                intent.putExtra("Meeting id", meetingId);
                startActivityForResult(intent,3);
            }
        });
    }

    private void setValuesToViewsFromIntentValues(Intent parentIntent) {
        meetingName.setText(parentIntent.getStringExtra("Meeting name"));
        meetingAgenda.setText(parentIntent.getStringExtra("Meeting agenda"));
        meetingLocation.setText(parentIntent.getStringExtra("Meeting location"));
        meetingDescription.setText(parentIntent.getStringExtra("Meeting description"));
        meetingDate.setText(parentIntent.getStringExtra("Meeting date"));
        meetingId = parentIntent.getStringExtra("Meeting id");
    }

    private void setViews(){
        meetingName = findViewById(R.id.detailed_meeting_name);
        meetingLocation = findViewById(R.id.detailed_meeting_location);
        meetingDescription = findViewById(R.id.detailed_meeting_description);
        meetingAgenda = findViewById(R.id.detailed_meeting_agenda);
        meetingDate = findViewById(R.id.detailed_meeting_date);
        viewNotesButton = findViewById(R.id.detailed_meeting_view_notes);
        addNotesButton = findViewById(R.id.detailed_meetings_create_notes_button);
    }

}
