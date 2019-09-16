package com.example.taskmanager.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanager.Models.Collaborator;
import com.example.taskmanager.Models.Project;
import com.example.taskmanager.Models.User;
import com.example.taskmanager.Models.UserProject;
import com.example.taskmanager.R;
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

public class EditProjectInfoActivity extends AppCompatActivity {


    private boolean changeNameEnabled = false;

    private EditText projectName;
    private ImageView enableNameButton;
    private Spinner spinner;
    private EditText collaboratorEmail;
    private ImageView addUserButton;
    private Button saveUserButton;
    private Project currentProject;

    private String mCurrentUserEmail;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseCurrentUser;
    private DatabaseReference mDatabaseProjects;

    private List<Collaborator> newCollabs;
    private Intent parentIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project_info);

        projectName = (EditText) findViewById(R.id.project_name);
        enableNameButton = (ImageView) findViewById(R.id.enable_button);
        spinner = (Spinner) findViewById(R.id.role_spinner);
        collaboratorEmail = (EditText) findViewById(R.id.collab_address);
        addUserButton = (ImageView) findViewById(R.id.add_collab_button);
        saveUserButton = (Button) findViewById(R.id.save_changes_button);
        parentIntent = getIntent();

        currentProject = parentIntent.getParcelableExtra("currentProject");
        projectName.setEnabled(changeNameEnabled);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mCurrentUserEmail = mCurrentUser.getEmail();
        mDatabaseCurrentUser = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUser.getUid());
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabaseProjects = FirebaseDatabase.getInstance().getReference().child("projects");

        newCollabs = new ArrayList<>();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(EditProjectInfoActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.roles));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);




        initializeView();


        enableNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNameEnabled = !changeNameEnabled;
                projectName.setEnabled(changeNameEnabled);
            }
        });

        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserToView();
            }
        });

        saveUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveUserButton.setEnabled(false);
                createProjectAndUpdateBase();

                saveUserButton.setEnabled(true);
            }
        });

    }


    public void createProjectAndUpdateBase() {

        if (!projectName.getText().toString().isEmpty()) {

            currentProject.name = projectName.getText().toString().trim();

            mDatabaseProjects.child(currentProject.projectId).setValue(currentProject);


            for (final Collaborator c : newCollabs) {


                final DatabaseReference collabRef = mDatabaseUsers.child(c.uId);

                collabRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.i("EditProjectInfoActivity", "in datachange");
                        User user = dataSnapshot.getValue(User.class);
                        UserProject userProject = new UserProject(currentProject.projectId, c.collabType);
                        user.addProject(userProject);
                        collabRef.setValue(user);

                        if (newCollabs.indexOf(c) == newCollabs.size() - 1){

                            setResult(Activity.RESULT_OK, parentIntent);
                            finish();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            if(newCollabs.size() == 0){
                Log.i("TAG", "yes we are here and seting result ok>?");

                setResult(Activity.RESULT_OK, parentIntent);
                finish();
            }

//            Intent intent = new Intent(this, ProfileProjectsMeetingsActivity.class);
//            startActivity(intent);

        }
        else {
            projectName.setError("Must not be left blank!");
        }

    }

    private void initializeView() {
        projectName.setText(currentProject.name);
        final LayoutInflater inflater = getLayoutInflater();
        final LinearLayout collabWrap = (LinearLayout) findViewById(R.id.collaborators_wrap);

        for (final Collaborator c:currentProject.collaborators){
            if (c.mail.equals(mCurrentUserEmail))
                continue;
            final View view = inflater.inflate(R.layout.collaborator_view, null);
            TextView email = view.findViewById(R.id.collab_email);
            TextView role = view.findViewById(R.id.collab_role);

            email.setText(c.mail);
            role.setText(c.collabType);

            collabWrap.addView(view);

            ImageView closeButton = view.findViewById(R.id.close_button);

            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentProject.deleteCollaboratorWithEmail(c.mail);
                    currentProject.deleteTaskForCollaborator(c);

                    mDatabaseProjects.child(currentProject.projectId).setValue(currentProject);
                    ((ViewManager) view.getParent()).removeView(view);
                    final DatabaseReference userRef = mDatabaseUsers.child(c.uId);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            user.removeUserProjectWithId(currentProject.projectId);
                            userRef.setValue(user);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            });


        }
    }


    public void addUserToView() {
        final String collabEmail = collaboratorEmail.getText().toString();
        final String collabRole = spinner.getSelectedItem().toString();



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
                    TextView email = view.findViewById(R.id.collab_email);
                    for(Collaborator c:currentProject.collaborators){
                        if(c.mail.equals(collabEmail.trim())){
                            currentProject.collaborators.remove(c);
                            newCollabs.remove(c);
                            break;
                        }
                    }

                    ((ViewManager) view.getParent()).removeView(view);
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

                        for (Collaborator c:currentProject.collaborators){
                            if (c.mail.equals(collabEmail)){
                                isEmailOk = false;
                                Toast.makeText(getBaseContext(), "You have already added that collaborator", Toast.LENGTH_LONG).show();
                            }
                        }


                        if (isEmailOk) {
                            email.setText(collabEmail);
                            role.setText(spinner.getSelectedItem().toString());


                            Collaborator collab = new Collaborator(userID, collabRole, collabEmail);
                            currentProject.collaborators.add(collab);
                            newCollabs.add(collab);

                            collaboratorEmail.setText("");
                            hideKeyboard(EditProjectInfoActivity.this);
                            spinner.setSelection(0);

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
            Toast.makeText(EditProjectInfoActivity.this, "Enter email in there", Toast.LENGTH_LONG);
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
