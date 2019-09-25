package com.example.taskmanager.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.TypedValue;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
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
    TextView distanceMeterLeft, distanceMeterRight;
    LinearLayout wall;
    DatabaseReference distanceLeftRef, distanceRightRef;

    float differenceBetweenSensors, distanceLeft, distanceRight, closestDistance;
    boolean leftIsCloser;
    ImageView leftIndicator,rightIndicator;
    LinearLayout car;

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rear_parking_sensor);
        distanceLeftRef = FirebaseDatabase.getInstance().getReference().child("distanceRight");
        distanceRightRef = FirebaseDatabase.getInstance().getReference().child("distanceLeft");

        distanceMeterLeft = findViewById(R.id.distance_meter_left);
        distanceMeterRight = findViewById(R.id.distance_meter_right);

        wall = findViewById(R.id.wall);
        leftIndicator = findViewById(R.id.left_indicator);
        rightIndicator = findViewById(R.id.right_indicator);
        car = findViewById(R.id.car);

        differenceBetweenSensors = 22;
        distanceLeftRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                distanceLeft = dataSnapshot.getValue(Float.class);
                distanceMeterLeft.setText(distanceLeft + "cm");
                closestDistance = (distanceLeft>distanceRight)?distanceRight:distanceLeft;
                leftIsCloser = (distanceLeft<distanceRight)?true:false;
                if (closestDistance<=60){
                    activateCloseDistance(closestDistance, leftIsCloser);
                    float dps = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130 - closestDistance*3, getResources().getDisplayMetrics());
                    car.setTranslationY(dps);

                }
                if(closestDistance>60){
                    vehicleSafeZone(true);
                }
                if(distanceLeft >60){
                    resetBySide(true);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        distanceRightRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                distanceRight = dataSnapshot.getValue(Float.class);
                distanceMeterRight.setText(distanceRight + "cm");
                closestDistance = (distanceLeft>distanceRight)?distanceRight:distanceLeft;
                leftIsCloser = (distanceLeft<distanceRight)?true:false;

                if (closestDistance<=60){
                    activateCloseDistance(closestDistance, leftIsCloser);
                    float dps = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130 - closestDistance*3, getResources().getDisplayMetrics());
                    car.setTranslationY(dps);

                }
                if(closestDistance>60){
                    vehicleSafeZone(false);
                }
                if(distanceRight >60){
                    resetBySide(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void activateCloseDistance(double distanceLeft, boolean leftIsCloser) {


        float difference = Math.abs(this.distanceLeft - this.distanceRight);
        double hipotenuse = Math.sqrt( Math.pow(difference, 2) + Math.pow(differenceBetweenSensors, 2));
        double degree = Math.toDegrees(Math.sin(difference/hipotenuse));

        if(this.distanceLeft > this.distanceRight)
            car.setRotation((float) degree);
        else
            car.setRotation((float) -degree);

        Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
        animation.setDuration((long)distanceLeft*20); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        if(leftIsCloser) {
            leftIndicator.startAnimation(animation); //to start animation
            leftIndicator.setColorFilter(Color.argb(255, 173, 0, 63));
            distanceMeterLeft.setTextColor(Color.argb(255, 173, 0, 63));
        }
        else {
            rightIndicator.startAnimation(animation); //to start animation
            rightIndicator.setColorFilter(Color.argb(255, 173, 0, 63));
            distanceMeterRight.setTextColor(Color.argb(255, 173, 0, 63));
        }


//                    distanceMeter.startAnimation(animation);
    }

    public void vehicleSafeZone(boolean left) {

            car.setRotation(0);
            car.setTranslationY(0);
            leftIndicator.clearAnimation();
            leftIndicator.setColorFilter(Color.argb(255,163,171,184));
            distanceMeterLeft.setTextColor(Color.argb(255, 255, 255, 255));


            rightIndicator.clearAnimation();
            rightIndicator.setColorFilter(Color.argb(255, 163, 171, 184));
            distanceMeterRight.setTextColor(Color.argb(255, 255, 255, 255));


    }

    public void resetBySide(boolean left) {
        if(left) {
            leftIndicator.clearAnimation();
            leftIndicator.setColorFilter(Color.argb(255, 163, 171, 184));
            distanceMeterLeft.setTextColor(Color.argb(255, 255, 255, 255));
        }
        else {
            rightIndicator.clearAnimation();
            rightIndicator.setColorFilter(Color.argb(255, 163, 171, 184));
            distanceMeterRight.setTextColor(Color.argb(255, 255, 255, 255));
        }
    }
}
