package com.example.taskmanager.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import android.widget.TextView;

import com.example.taskmanager.Adapters.SpinnerItemAdapter;
import com.example.taskmanager.Fragments.Dialogs.DatePickerFragment;
import com.example.taskmanager.Fragments.Dialogs.DescriptionDialog;
import com.example.taskmanager.Fragments.Dialogs.SingleChoiceDialog;
import com.example.taskmanager.Models.Collaborator;
import com.example.taskmanager.Models.Project;
import com.example.taskmanager.Models.Task;
import com.example.taskmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, SingleChoiceDialog.SingleChoiceListener, DescriptionDialog.DescriptionDialogListener {

    private String mCurrentUserEmail;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseCurrentUser;
    private DatabaseReference mDatabaseProjects;
    private Project currentProject;

    private Spinner spinner;
    private LinearLayout dateHolder;
    private LinearLayout priorityHolder;
    private LinearLayout descriptionHolder;

    private Calendar date;
    private TextView dateTextView;

    private TextView priorityTextView;
    private TextView descriptionTextView;
    private TextView taskName;

    private FloatingActionButton fab;

    private DatabaseReference mDatabaseCurrentProject;

    private Intent parentIntent;
    private TextView errorTextView;

    private RadioButton developer;


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

        taskName = (TextView) findViewById(R.id.meeting_item_meeting_name);
        dateTextView = (TextView) findViewById(R.id.chosen_date);
        errorTextView = (TextView) findViewById(R.id.error_textview);
        priorityHolder = (LinearLayout) findViewById(R.id.priority_layout);
        descriptionHolder = (LinearLayout) findViewById(R.id.description_holder);

        priorityTextView = (TextView) findViewById(R.id.etPriority);
        descriptionTextView = (TextView) findViewById(R.id.etDescription);
        parentIntent = getIntent();

        developer = (RadioButton) findViewById(R.id.developer);
        developer.setChecked(true);
        developer.performClick();

        mDatabaseCurrentProject = FirebaseDatabase.getInstance().getReference().child("projects").child(currentProject.projectId);


        fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.setEnabled(false);
                createTaskAndUpdateBase();

            }
        });



        errorTextView.setVisibility(View.INVISIBLE);
        dateHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();

                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        priorityHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment singleChoiceDialog = new SingleChoiceDialog();
                singleChoiceDialog.setCancelable(false);
                singleChoiceDialog.show(getSupportFragmentManager(), "Single choice dialog");
            }
        });

        descriptionHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DescriptionDialog descriptionDialog = new DescriptionDialog();
                descriptionDialog.setCancelable(false);
                descriptionDialog.show(getSupportFragmentManager(), "Description dialog");
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

    public void createTaskAndUpdateBase() {
        if(taskName.getText().toString().isEmpty()){
           taskName.setError("Must not leave this field blank!");
            fab.setEnabled(true);
            return;
        }
        if(spinner.getSelectedItem() == null){
            errorTextView.setText("Must select a collaborator! If no collaborators are available, please add them to your project");
            errorTextView.setVisibility(View.VISIBLE);
            fab.setEnabled(true);
            return;
        }
        if(date == null){
            dateTextView.setError("Must select a date for the task!");
            fab.setEnabled(true);
            return;
        }
        if(priorityTextView.getText().toString().isEmpty()){
            priorityTextView.setError("Must choose a priority for the task");
            fab.setEnabled(true);
            return;
        }

        Task task = new Task(taskName.getText().toString(), descriptionTextView.getText().toString(), priorityTextView.getText().toString(),
                date, (Collaborator) spinner.getSelectedItem(), "TODO", currentProject.projectId);

        Log.i("CreateTaskActivity", task.toString());

        currentProject.addTask(task);

        mDatabaseCurrentProject.setValue(currentProject).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                setResult(Activity.RESULT_OK, parentIntent);
                fab.setEnabled(true);
                finish();
            }
        });




    }

    public void openDescriptionDialog(){

    }

    public void changeSpinnerOptions(String collabType){
        List<Collaborator> collabs = currentProject.getAllCollabsFromType(collabType);
        SpinnerItemAdapter adapter = new SpinnerItemAdapter(this, collabs);

        if(collabs.size() == 0) {
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText("There are no " + collabType + "s assigned to this project");
        }
        else {
            errorTextView.setVisibility(View.GONE);
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
            case R.id.manager:
                if (checked)
                    changeSpinnerOptions("Project Manager");
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

        date = c;
        dateTextView.setText(currentDate);
        dateTextView.setError(null);

    }

    @Override
    public void onPositiveButtonClicked(String[] list, int position) {
        priorityTextView.setText(list[position]);
        priorityTextView.setError(null);
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void applyText(String desc) {
        descriptionTextView.setText(desc);
    }
}
