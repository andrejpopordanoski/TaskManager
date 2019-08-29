package com.example.taskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanager.Classes.User;
import com.example.taskmanager.Models.Collaborator;
import com.example.taskmanager.Models.Project;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddProjectActivity extends AppCompatActivity {

    private final static String TAG = "AddProjectActivity";
    private FloatingActionButton fabAddProject;
    private Spinner roleSpinner;
    private EditText collaboratorEmail;
    private EditText projectName;

    private Button createProjectButton;

    private String mCurrentUserEmail;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseCurrentUser;
    private DatabaseReference mDatabaseProjects;

    private List<Collaborator> collaborators;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        fabAddProject = (FloatingActionButton) findViewById(R.id.fab_new_collaborator);
        roleSpinner = (Spinner) findViewById(R.id.role_spinner);
        collaboratorEmail = (EditText) findViewById(R.id.collab_address);
        projectName = (EditText) findViewById(R.id.project_name);

        createProjectButton = (Button) findViewById(R.id.create_project_button);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddProjectActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.roles));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(arrayAdapter);


        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mCurrentUserEmail = mCurrentUser.getEmail();
        mDatabaseCurrentUser = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUser.getUid());
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabaseProjects = FirebaseDatabase.getInstance().getReference().child("projects");

        collaborators = new ArrayList<Collaborator>();

//        LayoutInflater inflater = getLayoutInflater();
//        View v = inflater.inflate(R.layout.collaborator_view,  null);
//        LinearLayout scrollView = (LinearLayout) findViewById(R.id.collaborators_wrap);
//        scrollView.addView(v, 1);

        createProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProjectAndUpdateBase();
            }
        });


        fabAddProject.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(AddProjectActivity.this, "Clicks??", Toast.LENGTH_SHORT);

                addUserToView();

            }
        });
    }

    // CAN INSTAD DO THIS -----> https://stackoverflow.com/questions/37094631/get-the-pushed-id-for-specific-value-in-firebase-android
    public void createProjectAndUpdateBase() {
        final Project project = new Project(projectName.getText().toString(), collaborators);

        Log.i(TAG,"We are here in this methodan");
        for (Collaborator c:collaborators){
            final DatabaseReference collabRef  = mDatabaseUsers.child(c.id);

            final TextView random = (TextView) findViewById(R.id.some_random_textview);
            Log.i(TAG,"We are here in the collabsand");

            collabRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    user.initializeProjectList();
                    user.addProject(project);
                    collabRef.setValue(user);

//                    Log.i(TAG, u.toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    public void addUserToView() {
        final String collabEmail = collaboratorEmail.getText().toString();
        final String collabRole = roleSpinner.getSelectedItem().toString();

        if(collabEmail.length()> 0) {
            LayoutInflater inflater = getLayoutInflater();
            final View view = inflater.inflate(R.layout.collaborator_view, null);
            final LinearLayout collabWrap = (LinearLayout) findViewById(R.id.collaborators_wrap);

            final TextView email = view.findViewById(R.id.collab_email);
            final TextView role = view.findViewById(R.id.collab_role);

            final Query getUsersQuery = mDatabaseUsers.orderByChild("email").equalTo(collabEmail);

            final TextView random = (TextView) findViewById(R.id.some_random_textview);

            getUsersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                String  s = "";
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if(dataSnapshot.getValue() != null){
                        boolean isEmailOk = true;
                        String userID = "";
                        for(DataSnapshot childSnapshot:dataSnapshot.getChildren()){
                            userID = childSnapshot.getKey();
//                            random.setText(childSnapshot.child("email").getValue().toString() + " " + mCurrentUserEmail );
                            if(mCurrentUserEmail.trim().equals(childSnapshot.child("email").getValue().toString().trim())) {
                                isEmailOk = false;
                                Toast.makeText(getBaseContext(), "You cannot add yourself as a collaborator", Toast.LENGTH_LONG).show();
                            }


                        }

                        if (isEmailOk) {
                            email.setText(collabEmail);
                            role.setText(roleSpinner.getSelectedItem().toString());


                            Collaborator collab = new Collaborator(userID, collabRole);
                            collaborators.add(collab);

                            collaboratorEmail.setText("");
                            hideKeyboard(AddProjectActivity.this);
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
            Toast.makeText(AddProjectActivity.this, "Enter email in there", Toast.LENGTH_LONG);
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
