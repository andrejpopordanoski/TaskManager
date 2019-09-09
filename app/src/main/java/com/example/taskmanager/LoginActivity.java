package com.example.taskmanager;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private  String TAG = "LoginActivity";
    private Button loginButton;
    private EditText loginEmailText;
    private EditText loginPasswordText;
    private TextView loginSignUpText;
    private ProgressBar loginProgressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login_user_button);
        loginEmailText = (EditText) findViewById(R.id.login_user_email);
        loginPasswordText = (EditText) findViewById(R.id.login_user_password);
        loginSignUpText = (TextView) findViewById(R.id.login_sing_up_now);
        loginProgressBar = (ProgressBar) findViewById(R.id.login_progress_bar);

        firebaseAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(this);
        loginSignUpText.setOnClickListener(this);


    }

    public void openRegisterActivity(){
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }

    public void logUserToApp() {
        String email = loginEmailText.getText().toString().trim();
        String password = loginPasswordText.getText().toString().trim();

        loginProgressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Toast.makeText(LoginActivity.this, "Login was successful", Toast.LENGTH_SHORT).show();
                    loginProgressBar.setVisibility(View.INVISIBLE);
                    finish();
                    Log.i(TAG, task.toString() );

                    startActivity(new Intent(getApplicationContext(), ProfileProjectsMeetingsActivity.class));

                }
                else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    loginProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });



    }

        @Override
    public void onClick(View v) {
        if(v == loginButton){
            logUserToApp();
        }
        else {
            openRegisterActivity();
        }
    }
}
