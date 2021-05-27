package com.izhar.academy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class Asses extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    Button done;
    RadioButton r1, r2, r3, r4, r5, r6, r7, r8,r9;
    static int teach = 0, course = 0, self = 0;
    RatingBar rate;
    String course_code;
    private void setLanguage() {
        SharedPreferences sharedPreferences = getSharedPreferences("lang", Context.MODE_PRIVATE);
        Locale locale = new Locale(sharedPreferences.getString("lang", "am"));
        Configuration configuration = new Configuration();
        Locale.setDefault(locale);
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage();
        setContentView(R.layout.asses);
        init();
        course_code = getIntent().getExtras().getString("course_code");
        r1.setOnCheckedChangeListener(this);
        r2.setOnCheckedChangeListener(this);
        r3.setOnCheckedChangeListener(this);
        r4.setOnCheckedChangeListener(this);
        r5.setOnCheckedChangeListener(this);
        r6.setOnCheckedChangeListener(this);
        r7.setOnCheckedChangeListener(this);
        r8.setOnCheckedChangeListener(this);
        r9.setOnCheckedChangeListener(this);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teach == 0 || course == 0 || self == 0){
                    Toast.makeText(Asses.this,R.string.complete_assessment, Toast.LENGTH_SHORT).show();
                }
                else {
                    setRate((course + self + teach) / (double) 60);
                    startActivity(new Intent(Asses.this, MainActivity.class));
                    finish();
                }
            }
        });
        rate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    int am_rate, en_rate, ar_rate;
    double am_tot, ar_tot, en_tot;
    private void setRate(final double v) {
        final DatabaseReference am = FirebaseDatabase.getInstance().getReference("am").child("courses").child(course_code);
        final DatabaseReference en = FirebaseDatabase.getInstance().getReference("en").child("courses").child(course_code);
        final DatabaseReference ar = FirebaseDatabase.getInstance().getReference("ar").child("courses").child(course_code);
        am.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("total_rate")){
                    am_tot = Double.parseDouble(dataSnapshot.child("total_rate").getValue().toString());
                    am_rate = Integer.parseInt(dataSnapshot.child("total_rated_person").getValue().toString());
                    am_rate += 1;
                    am_tot += v;
                    am.child("total_rated_person").setValue(am_rate);
                    am.child("total_rate").setValue(am_tot);
                    am.child("rate").setValue(am_tot/am_rate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        en.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("total_rate")){
                    en_tot = Double.parseDouble(dataSnapshot.child("total_rate").getValue().toString());
                    en_rate = Integer.parseInt(dataSnapshot.child("total_rated_person").getValue().toString());
                    en_rate += 1;
                    en_tot += v;
                    en.child("total_rated_person").setValue(en_rate);
                    en.child("total_rate").setValue(en_tot);
                    am.child("rate").setValue(en_tot/en_rate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ar.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("total_rate")){
                    ar_tot = Double.parseDouble(dataSnapshot.child("total_rate").getValue().toString());
                    ar_rate = Integer.parseInt(dataSnapshot.child("total_rated_person").getValue().toString());
                    ar_rate += 1;
                    ar_tot += v;
                    ar.child("total_rated_person").setValue(ar_rate);
                    ar.child("total_rate").setValue(ar_tot);
                    ar.child("rate").setValue(ar_tot/ar_rate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void init() {
        rate = findViewById(R.id.rate);
        done = findViewById(R.id.done);
        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);
        r4 = findViewById(R.id.r4);
        r5 = findViewById(R.id.r5);
        r6 = findViewById(R.id.r6);
        r7 = findViewById(R.id.r7);
        r8 = findViewById(R.id.r8);
        r9 = findViewById(R.id.r9);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()){
                case R.id.r1:
                    teach = 100;
                    rate.setRating((float) (course + self + teach)/60);
                    break;
                case R.id.r2:
                    teach = 60;
                    rate.setRating((float) (course + self + teach)/60);
                    break;
                case R.id.r3:
                    teach = 30;
                    rate.setRating((float) (course + self + teach)/60);
                    break;
                case R.id.r4:
                    course = 100;
                    rate.setRating((float) (course + self + teach)/60);
                    break;
                case R.id.r5:
                    course = 60;
                    rate.setRating((float) (course + self + teach)/60);
                    break;
                case R.id.r6:
                    course = 30;
                    rate.setRating((float) (course + self + teach)/60);
                    break;
                case R.id.r7:
                    self = 100;
                    rate.setRating((float) (course + self + teach)/60);
                    break;
                case R.id.r8:
                    self = 60;
                    rate.setRating((float) (course + self + teach)/60);
                    break;
                case R.id.r9:
                    self = 30;
                    rate.setRating((float) (course + self + teach)/60);
                    break;
            }
        }
    }
}
