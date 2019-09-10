package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.taskmanager.Fragments.DatePickerFragment;
import com.example.taskmanager.Models.Collaborator;
import com.example.taskmanager.Models.Project;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private String mCurrentUserEmail;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseCurrentUser;
    private DatabaseReference mDatabaseProjects;
    private Project currentProject;

    private Spinner spinner;
    private LinearLayout dateHolder;
    private TextView dateTextView;
    private TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);


        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mCurrentUserEmail = mCurrentUser.getEmail();
        mDatabaseCurrentUser = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUser.getUid());
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabaseProjects = FirebaseDatabase.getInstance().getReference().child("projects");

        spinner = (Spinner) findViewById(R.id.asignee_spinner);
        currentProject = getIntent().getParcelableExtra("currentProject");
        dateHolder = (LinearLayout) findViewById(R.id.date_holder);
        dateTextView = (TextView) findViewById(R.id.chosen_date);
        errorTextView = (TextView) findViewById(R.id.error_textview);
        errorTextView.setVisibility(View.INVISIBLE);
        dateHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();

                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 Collaborator collab = (Collaborator) parent.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//        List<Collaborator>

    }

    public void changeSpinnerOptions(String collabType){
        List<Collaborator> collabs = currentProject.getAllCollabsFromType(collabType);
        ArrayAdapter<Collaborator> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, collabs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        if(collabs.size() == 0) {
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText("There are no " + collabType + "s assigned to this project");
        }
        else {
            errorTextView.setVisibility(View.INVISIBLE);
        }
        spinner.setAdapter(adapter);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.developer:
                if (checked)
                    changeSpinnerOptions("Developer");
                    break;
            case R.id.tester:
                if (checked)
                    changeSpinnerOptions("Tester");
                    break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        dateTextView.setText(currentDate);
    }
}
