package com.example.taskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanager.Classes.User;
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
    private TextView registerEmailErrorText;
    private TextView registerPasswordErrorText;
    private TextView registerNameErrorText;
    private TextView registerPhoneErrorText;
    private TextView registerAddressErrorText;

    private ProgressBar registerProgressBar;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference baseRootRef = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registrationButton = (Button) findViewById(R.id.reg_user_button);

        registerEmailText = (EditText) findViewById(R.id.reg_user_email);
        registerPasswordText = (EditText) findViewById(R.id.reg_user_password);
        registerNameText = (EditText) findViewById(R.id.reg_user_name);
        registerPhoneText = (EditText) findViewById(R.id.reg_user_phone);
        registerAddressText = (EditText) findViewById(R.id.reg_user_address);

        registerSignInText = (TextView) findViewById(R.id.reg_sing_in_now);
        registerEmailErrorText = (TextView) findViewById(R.id.req_email_correct_format);
        registerPasswordErrorText = (TextView) findViewById(R.id.req_password_correct_format);
        registerProgressBar = (ProgressBar) findViewById(R.id.reg_progress_bar);
        registerNameErrorText = (TextView) findViewById(R.id.req_name_correct_format);
        registerPhoneErrorText = (TextView) findViewById(R.id.req_phone_correct_format);
        registerAddressErrorText = (TextView) findViewById(R.id.req_address_correct_format);



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
        if(name.length() == 0){
            registerNameErrorText.setVisibility(View.VISIBLE);
        }
        else {
            registerNameErrorText.setVisibility(View.INVISIBLE);
            nameOK = true;
        }
        if(phone.length() == 0){
            registerPhoneErrorText.setVisibility(View.VISIBLE);
        }
        else {
            registerPhoneErrorText.setVisibility(View.INVISIBLE);
            phoneOK = true;
        }
        if(address.length() == 0){
            registerAddressErrorText.setVisibility(View.VISIBLE);
        }
        else {
            registerAddressErrorText.setVisibility(View.INVISIBLE);
            addressOK = true;
        }

        if(emailOK && passwordOK && nameOK && phoneOK && addressOK)  {
            registerProgressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Registered sucessfully", Toast.LENGTH_SHORT).show();
                        registerProgressBar.setVisibility(View.INVISIBLE);

                        Log.i(TAG, "hello world yes>");
                        signUserInAndOpenProfile(email, password, name, phone, address);


                    }
                    else {
                        Toast.makeText(MainActivity.this, "Registered failed", Toast.LENGTH_SHORT).show();
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
                    User user = new User(firebaseAuth.getUid(), name, phone, address);
                    String userUid = firebaseAuth.getUid();
                    Log.i(TAG, task.getResult().getUser().getUid());


                    baseRootRef.child("users").child(email).setValue(user);

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
