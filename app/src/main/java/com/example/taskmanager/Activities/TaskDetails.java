package com.example.taskmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.taskmanager.Models.Project;
import com.example.taskmanager.Models.Task;
import com.example.taskmanager.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class TaskDetails extends AppCompatActivity {

    public final String TAG = "TaskDetails";
    TextView taskName;
    TextView taskDate;
    TextView priority;
    TextView assigneeEmail;
    TextView assigneeRole;
    TextView description;
    Button actionButton;
    Button setInProgressButton;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    Task currentTask;
    Project currentProject;
    Intent parentIntent;
    DatabaseReference mDatabaseCurrentProject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        taskName = (TextView) findViewById(R.id.task_name);
        taskDate = (TextView) findViewById(R.id.details_due_date);
        priority = (TextView) findViewById(R.id.details_priority);
        assigneeEmail = (TextView) findViewById(R.id.details_collab_email);
        assigneeRole = (TextView) findViewById(R.id.details_collab_role);
        description = (TextView) findViewById(R.id.details_description_text);

        actionButton = (Button) findViewById(R.id.details_action_button);
        setInProgressButton = (Button) findViewById(R.id.details_set_in_progress_buttton);

        parentIntent = getIntent();

        currentTask = parentIntent.getParcelableExtra("currentTask");
        currentProject = parentIntent.getParcelableExtra("currentProject");



        taskName.setText(currentTask.name);
        taskDate.setText(currentTask.getDateFormatted());
        priority.setText(currentTask.priority);
        assigneeEmail.setText(currentTask.assignee.mail);
        assigneeRole.setText(currentTask.assignee.collabType);
        description.setText(currentTask.description);


        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        if(!assigneeEmail.getText().toString().trim().equals(mCurrentUser.getEmail())){
            actionButton.setVisibility(View.INVISIBLE);
            setInProgressButton.setVisibility(View.INVISIBLE);
        }


        if(currentTask.taskState.equals("DONE")){
            setInProgressButton.setText("DELETE TASK");
        }
        if(currentTask.taskState.equals("DONE") && currentProject.isUserFromType(mCurrentUser.getUid(), "Project Manager")){
            actionButton.setVisibility(View.VISIBLE);
            actionButton.setText("SET AS \"TO DO\"");
            setInProgressButton.setVisibility(View.VISIBLE);
        }



        if(currentTask.taskState.equals("TEST")){
            actionButton.setText("TEST FAILED");
            setInProgressButton.setText("TEST PASSED");
            if(currentProject.isUserFromType(mCurrentUser.getUid(), "Tester")){
                actionButton.setVisibility(View.INVISIBLE);
                setInProgressButton.setVisibility(View.INVISIBLE);
            }
        }



        mDatabaseCurrentProject = FirebaseDatabase.getInstance().getReference().child("projects").child(currentProject.projectId);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Clicking on the action button");


                    Task t = currentProject.changeTaskState(currentTask);
                    if(currentTask.taskState.equals("TEST")){
                        t.testFailed = true;
                    }

                    mDatabaseCurrentProject.setValue(currentProject);

                    finish();



            }
        });

        setInProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentTask.taskState.equals("TODO")) {
                    currentProject.setTaskInProgress(currentTask);
                }
                else if (currentTask.taskState.equals("TEST")){
                    Task t = currentProject.changeTaskState(currentTask);
                    t.testFailed = false;
                }
                else {
                    currentProject.removeTask(currentTask);

                }
                mDatabaseCurrentProject.setValue(currentProject);
                finish();
            }



        });




    }
}
