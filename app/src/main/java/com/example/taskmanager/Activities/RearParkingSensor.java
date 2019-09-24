package com.example.taskmanager.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.taskmanager.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RearParkingSensor extends AppCompatActivity {
    TextView distanceMeter;
    LinearLayout wall;
    DatabaseReference distanceLeft;
    float distanceRight;
    float differenceBetweenSensors;
    ImageView leftIndicator,rightIndicator;

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rear_parking_sensor);
        distanceLeft = FirebaseDatabase.getInstance().getReference().child("distance");
        distanceMeter = findViewById(R.id.distance_meter);
        wall = findViewById(R.id.wall);
        leftIndicator = findViewById(R.id.left_indicator);
        rightIndicator = findViewById(R.id.right_indicator);
        distanceRight = 50f;
        differenceBetweenSensors = 30;

        distanceLeft.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float distanceLeft = dataSnapshot.getValue(Float.class);
                distanceMeter.setText(distanceLeft + "cm");

                float difference = Math.abs(distanceLeft - distanceRight);
                double hipotenuse = Math.sqrt( Math.pow(difference, 2) + Math.pow(differenceBetweenSensors, 2));
                double degree = Math.toDegrees(Math.sin(difference/hipotenuse));
                Log.i("SINUSE", degree + "");
                if(distanceLeft > distanceRight)
                    wall.setRotation((float) degree);
                else
                    wall.setRotation((float) -degree);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
