package com.example.taskmanager.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanager.Models.User;
import com.example.taskmanager.Models.UserProject;
import com.example.taskmanager.Models.Collaborator;
import com.example.taskmanager.Models.Project;
import com.example.taskmanager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateProjectActivity extends AppCompatActivity {

    private final static String TAG = "CreateProjectActivity";
    private FloatingActionButton fabAddProject;
    private Spinner roleSpinner;
    private EditText collaboratorEmail;
    private EditText projectName;
    private ProgressBar progressBar;

    private Button createProjectButton;

    private String mCurrentUserEmail;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseCurrentUser;
    private DatabaseReference mDatabaseProjects;

    private List<Collaborator> collaborators;
    private Intent parentIntent;

    private ImageButton newCollabButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        fabAddProject = (FloatingActionButton) findViewById(R.id.fab_new_collaborator);
        roleSpinner = (Spinner) findViewById(R.id.role_spinner);
        collaboratorEmail = (EditText) findViewById(R.id.collab_address);
        projectName = (EditText) findViewById(R.id.project_name);
//        createProjectButton = (Button) findViewById(R.id.create_project_button);
        newCollabButton = (ImageButton) findViewById(R.id.add_collab_button);
        progressBar = (ProgressBar) findViewById(R.id.project_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CreateProjectActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.roles));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(arrayAdapter);

        parentIntent = getIntent();


        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mCurrentUserEmail = mCurrentUser.getEmail();
        mDatabaseCurrentUser = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUser.getUid());
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabaseProjects = FirebaseDatabase.getInstance().getReference().child("projects");

        collaborators = new ArrayList<Collaborator>();
        Collaborator currentUser = new Collaborator(mCurrentUser.getUid(), "Project Manager", mCurrentUserEmail);
        collaborators.add(currentUser);
//        LayoutInflater inflater = getLayoutInflater();
//        View v = inflater.inflate(R.layout.collaborator_view,  null);
//        LinearLayout scrollView = (LinearLayout) findViewById(R.id.collaborators_wrap);
//        scrollView.addView(v, 1);

//        createProjectButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createProjectAndUpdateBase();
//            }
//        });


        newCollabButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                addUserToView();

            }
        });

        fabAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                fabAddProject.setEnabled(false);
                createProjectAndUpdateBase();
                progressBar.setVisibility(View.INVISIBLE);
                fabAddProject.setEnabled(true);

            }
        });

    }

    /**
     * Button on click event calls this method
     * It creates a project and pushes it into /projects of the firebase and then adds a UserProject to every collaborator with the project id
     * Then re
     */
    public void createProjectAndUpdateBase() {

        if (!projectName.getText().toString().isEmpty()) {

            final String projectKey = mDatabaseProjects.push().getKey();
            final Project project = new Project(projectName.getText().toString(), projectKey, collaborators);
            mDatabaseProjects.child(projectKey).setValue(project);


            for (final Collaborator c : collaborators) {
                final DatabaseReference collabRef = mDatabaseUsers.child(c.uId);




                collabRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        UserProject userProject = new UserProject(projectKey, c.collabType);
                        user.addProject(userProject);
                        collabRef.setValue(user);
                        Log.i(TAG, "Should have added the project to the user too");
                        if (collaborators.indexOf(c) == collaborators.size() - 1){

                            setResult(Activity.RESULT_OK, parentIntent);
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

//            Intent intent = new Intent(this, ProfileProjectsMeetingsActivity.class);
//            startActivity(intent);

        }
        else {
            projectName.setError("Must not be left blank!");
        }

    }



    public void addUserToView() {
        final String collabEmail = collaboratorEmail.getText().toString();
        final String collabRole = roleSpinner.getSelectedItem().toString();



        if(collabEmail.length()> 0) {
            final LayoutInflater inflater = getLayoutInflater();
            final View view = inflater.inflate(R.layout.collaborator_view, null);
            final ImageView closeButton = view.findViewById(R.id.close_button);



            final LinearLayout collabWrap = (LinearLayout) findViewById(R.id.collaborators_wrap);

            final TextView email = view.findViewById(R.id.collab_email);
            final TextView role = view.findViewById(R.id.collab_role);

            final Query getUsersQuery = mDatabaseUsers.orderByChild("email").equalTo(collabEmail);


            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure you want to remove the collaborator and all assigned tasks?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog

                            TextView email = view.findViewById(R.id.collab_email);
                            for(Collaborator c:collaborators){
                                if(c.mail.equals(collabEmail.trim())){
                                    collaborators.remove(c);
                                    break;
                                }
                            }
                            ((ViewManager) view.getParent()).removeView(view);
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });


            getUsersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                String  s = "";
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if(dataSnapshot.getValue() != null){
                        boolean isEmailOk = true;
                        String userID = "";
                        for(DataSnapshot childSnapshot:dataSnapshot.getChildren()){
                            userID = childSnapshot.getKey();
                            if(mCurrentUserEmail.trim().equals(childSnapshot.child("email").getValue().toString().trim())) {
                                isEmailOk = false;
                                Toast.makeText(getBaseContext(), "You cannot add yourself as a collaborator", Toast.LENGTH_LONG).show();
                            }


                        }

                        for (Collaborator c:collaborators){
                            if (c.mail.equals(collabEmail)){
                                isEmailOk = false;
                                Toast.makeText(getBaseContext(), "You have already added that collaborator", Toast.LENGTH_LONG).show();
                            }
                        }


                        if (isEmailOk) {
                            email.setText(collabEmail);
                            role.setText(roleSpinner.getSelectedItem().toString());


                            Collaborator collab = new Collaborator(userID, collabRole, collabEmail);
                            collaborators.add(collab);

                            collaboratorEmail.setText("");
                            hideKeyboard(CreateProjectActivity.this);
                            roleSpinner.setSelection(0);

                            collabWrap.addView(view);
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




        }
        else {
            Toast.makeText(CreateProjectActivity.this, "Enter email in there", Toast.LENGTH_LONG);
        }
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
}
