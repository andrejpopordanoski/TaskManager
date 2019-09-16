package com.example.taskmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanager.Adapters.NoteListAdapter;
import com.example.taskmanager.Models.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity {

    private Intent parentIntent;
    private String meetingId;
    private List<Note> notes;
    private FloatingActionButton fab;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference currentUserDatabaseReference;
    private DatabaseReference notesDatabaseReference;
    private DatabaseReference currentMeetingDatabaseReference;
    private FirebaseDatabase firebaseDatabaseInstance;

    private RecyclerView recyclerView;
    private NoteListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        parentIntent = getIntent();
        meetingId = parentIntent.getStringExtra("Meeting id");
        recyclerView = findViewById(R.id.notes_activity_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab = findViewById(R.id.notes_activity_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),CreateNoteActivity.class);
                intent.putExtra("Meeting id", meetingId);
                startActivityForResult(intent,4);
            }
        });
        notes = new ArrayList<>();
        setUpFirebase();
        getNotesFromDatabase();

    }

    private void getNotesFromDatabase() {
        notes = null;
        notes = new ArrayList<>();
        notesDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()){
                    Note note = noteSnapshot.getValue(Note.class);
                    if (note.getMeetingId().equals(meetingId)){
                        notes.add(noteSnapshot.getValue(Note.class));
                        setAdapter();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setAdapter() {
        adapter = new NoteListAdapter(notes);
        recyclerView.setAdapter(adapter);

    }


    private void setUpFirebase(){
        firebaseDatabaseInstance = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        currentUserDatabaseReference = firebaseDatabaseInstance.getReference().child("users").child(currentUser.getUid());
        notesDatabaseReference = firebaseDatabaseInstance.getReference().child("notes");
        currentMeetingDatabaseReference = firebaseDatabaseInstance.getReference().child("meetings").child(meetingId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            getNotesFromDatabase();
        }
    }
}
