package com.example.taskmanager.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanager.Models.User;
import com.example.taskmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private  String TAG = "MainActivity";
    private Button registrationButton;
    private EditText registerEmailText;
    private EditText registerPasswordText;
    private EditText registerNameText;
    private EditText registerPhoneText;
    private EditText registerAddressText;

    private TextView registerSignInText;

    private ProgressBar registerProgressBar;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference baseRootRef = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registrationButton = (Button) findViewById(R.id.reg_user_button);

        registerEmailText = (EditText) findViewById(R.id.create_meeting_name);
        registerPasswordText = (EditText) findViewById(R.id.reg_user_password);
        registerNameText = (EditText) findViewById(R.id.reg_user_name);
        registerPhoneText = (EditText) findViewById(R.id.reg_user_phone);
        registerAddressText = (EditText) findViewById(R.id.reg_user_address);

        registerSignInText = (TextView) findViewById(R.id.reg_sing_in_now);

        registerProgressBar = (ProgressBar) findViewById(R.id.reg_progress_bar);



        firebaseAuth = FirebaseAuth.getInstance();

        registrationButton.setOnClickListener(this);
        registerSignInText.setOnClickListener(this);

    }

    public void registerUser() {
        final String email = registerEmailText.getText().toString().trim();
        final String password = registerPasswordText.getText().toString().trim();
        final String name = registerNameText.getText().toString().trim();
        final String phone = registerPhoneText.getText().toString().trim();
        final String address = registerAddressText.getText().toString().trim();



        boolean emailOK = false, passwordOK = false, nameOK = false, phoneOK = false, addressOK = false;
        if (!email.contains("@") || !email.contains(".com") ) {
//            registerEmailErrorText.setVisibility(View.VISIBLE);
             registerEmailText.setError("Email empty or not in correct format!");
        }

        else {
            emailOK = true;
        }

        if(password.length() < 8){
            registerPasswordText.setError("Password must be more than 8 characters");
        }
        else {

            passwordOK = true;
        }
        if(name.length() == 0){
            registerNameText.setError("Name must not be empty");

        }
        else {
            nameOK = true;
        }
        if(phone.length() == 0){
            registerPhoneText.setError("Phone must not be empty");
            return;
        }
        if(phone.length() != 9){
            registerPhoneText.setError("Phone number should be a 9 digit number");

        }
        try {
            Integer.parseInt(phone);
        }
        catch (Exception e){
            registerPhoneText.setError("Phone number should be a 9 digit number");

            return;
        }

        phoneOK = true;

        if(address.length() == 0){
            registerAddressText.setError("Address must not be empty");
        }
        else {

            addressOK = true;
        }

        if(emailOK && passwordOK && nameOK && phoneOK && addressOK)  {
            registerProgressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        registerProgressBar.setVisibility(View.INVISIBLE);

                        Log.i(TAG, "hello world yes>");
                        signUserInAndOpenProfile(email, password, name, phone, address);


                    }
                    else {
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        registerProgressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });

        }




    }

    public void signUserInAndOpenProfile(final String email, String password, final String name, final String phone, final String address) {
        registerProgressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Toast.makeText(MainActivity.this, "Login was successful", Toast.LENGTH_SHORT).show();
                    registerProgressBar.setVisibility(View.INVISIBLE);
                    finish();
                    Log.i(TAG, task.toString() );
//                    baseRef.setValue("users");
                    User user = new User(email, name, phone, address);
                    String userUid = firebaseAuth.getUid();
                    Log.i(TAG, task.getResult().getUser().getUid());


                    baseRootRef.child("users").child(userUid).setValue(user);

                    startActivity(new Intent(getApplicationContext(), ProfileProjectsMeetingsActivity.class));

                }
                else {
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    registerProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });


    }

    public void openSignInActivity () {
        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));

    }

    @Override
    public void onClick(View v) {
        if(v == registrationButton){
            registerUser();
        }
        else if (v == registerSignInText){
            openSignInActivity();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
