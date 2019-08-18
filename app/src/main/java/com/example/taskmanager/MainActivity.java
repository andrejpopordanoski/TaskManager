package com.example.taskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private  String TAG = "MainActivity";
    private Button registrationButton;
    private EditText registerEmailText;
    private EditText registerPasswordText;
    private TextView registerSignInText;
    private TextView registerEmailErrorText;
    private TextView registerPasswordErrorText;
    private ProgressBar registerProgressBar;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registrationButton = (Button) findViewById(R.id.reg_user_button);
        registerEmailText = (EditText) findViewById(R.id.reg_user_email);
        registerPasswordText = (EditText) findViewById(R.id.reg_user_password);
        registerSignInText = (TextView) findViewById(R.id.reg_sing_in_now);
        registerEmailErrorText = (TextView) findViewById(R.id.req_email_correct_format);
        registerPasswordErrorText = (TextView) findViewById(R.id.req_password_correct_format);
        registerProgressBar = (ProgressBar) findViewById(R.id.reg_progress_bar);


        firebaseAuth = FirebaseAuth.getInstance();

        registrationButton.setOnClickListener(this);
        registerSignInText.setOnClickListener(this);

    }

    public void registerUser() {
        final String email = registerEmailText.getText().toString().trim();
        final String password = registerPasswordText.getText().toString().trim();
        boolean emailOK = false, passwordOK = false;
        if (!email.contains("@") || !email.contains(".com") ) {
            registerEmailErrorText.setVisibility(View.VISIBLE);
        }
        else {
            registerEmailErrorText.setVisibility(View.INVISIBLE);
            emailOK = true;
        }

        if(password.length() < 8){
            registerPasswordErrorText.setVisibility(View.VISIBLE);
        }
        else {
            registerPasswordErrorText.setVisibility(View.INVISIBLE);
            passwordOK = true;
        }

        if(emailOK && passwordOK)  {
            registerProgressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Registered sucessfully", Toast.LENGTH_SHORT).show();
                        registerProgressBar.setVisibility(View.INVISIBLE);
                        signUserInAndOpenProfile(email, password);


                    }
                    else {
                        Toast.makeText(MainActivity.this, "Registered failed", Toast.LENGTH_SHORT).show();
                        registerProgressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });

        }




    }

    public void signUserInAndOpenProfile(String email, String password) {
        registerProgressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Toast.makeText(MainActivity.this, "Login was successful", Toast.LENGTH_SHORT).show();
                    registerProgressBar.setVisibility(View.INVISIBLE);
                    finish();
                    Log.i(TAG, task.toString() );

                    startActivity(new Intent(getApplicationContext(), ProfileTasksActivity.class));

                }
                else {
                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
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
