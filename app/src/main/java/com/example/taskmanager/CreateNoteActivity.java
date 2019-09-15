package com.example.taskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.taskmanager.Models.Note;
import com.example.taskmanager.Models.User;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateNoteActivity extends AppCompatActivity {
    private LinearLayout submitButton;
    private EditText textInput;
    private Intent parentIntent;
    private String meetingId;
    private String userName = "";

    FirebaseDatabase firebaseDatabaseInstance;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    DatabaseReference currentUserDatabaseReference;
    DatabaseReference noteDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        submitButton = findViewById(R.id.create_note_submit_button);
        textInput = findViewById(R.id.create_note_input);
        parentIntent = getIntent();
        meetingId = parentIntent.getStringExtra("Meeting id");
        setUpFirebase();



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNoteAndUpdateDatabase();
            }
        });
    }

    private void createNoteAndUpdateDatabase() {
        if (!textInput.getText().toString().isEmpty()){
            final String noteKey = noteDatabaseReference.push().getKey();

            currentUserDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userName += dataSnapshot.getValue(User.class).getName();

                    final Note note = new Note(userName,textInput.getText().toString(),meetingId);

                    noteDatabaseReference.child(noteKey).setValue(note);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            Intent intent = new Intent();
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    private void setUpFirebase(){
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseDatabaseInstance = FirebaseDatabase.getInstance();
        noteDatabaseReference = firebaseDatabaseInstance.getReference().child("notes");
        currentUserDatabaseReference = firebaseDatabaseInstance.getReference().child("users").child(currentUser.getUid());
    }

}
