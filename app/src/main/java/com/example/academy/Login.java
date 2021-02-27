package com.example.academy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.academy.users.UsersDatabase;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.Locale;

public class Login extends AppCompatActivity {
    CircularProgressBar progressBar;
    Button signIn;
    EditText phone;
    Snackbar snackbar;
    UsersDatabase usersDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setLanguage();
        setContentView(R.layout.activity_login);
        setTitle(getResources().getString(R.string.sign_in));
        usersDatabase = new UsersDatabase(this, "users");
        phone = findViewById(R.id.login_phone);
        signIn = findViewById(R.id.sign_in);
        progressBar = findViewById(R.id.sign_in_progress_bar);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionCheck()) {
                    if (phone.getText().toString().length() < 10) {
                        phone.setError(getResources().getString(R.string.invalid_format));
                    } else {
                        signIn.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        account_exist(phone.getText().toString(), signIn, progressBar);
                    }

                } else {
                    snackbar = Snackbar.make(v, getResources().getString(R.string.connet_internet), Snackbar.LENGTH_SHORT);
                    snackbar.setAction(getResources().getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                }

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private boolean connectionCheck() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
        int connected = 0;
        for (NetworkInfo networkInfo : info) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                connected = 1;
            }
        }
        if (connected == 0){
            return false;
        }
        else{
            return true;
        }
    }

    String name, phone_, uri = "", gender;


    private void account_exist(String phone_number, final Button signIn, final CircularProgressBar progressBar) {
        DatabaseReference mdata = FirebaseDatabase.getInstance().getReference().child("users").child(phone_number);
        mdata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    SharedPreferences sh = getSharedPreferences("userInfo", MODE_PRIVATE);
                    sh.edit().putBoolean("registered", true).apply();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        switch (dataSnapshot1.getKey()) {
                            case "name":
                                sh.edit().putString("name", dataSnapshot1.getValue().toString()).apply();
                                name = dataSnapshot1.getValue().toString();
                                break;
                            case "phone":
                                sh.edit().putString("phone", dataSnapshot1.getValue().toString()).apply();
                                phone_ = dataSnapshot1.getValue().toString();
                                break;
                            case "email":
                                sh.edit().putString("email", dataSnapshot1.getValue().toString()).apply();
                                break;
                            case "country":
                                sh.edit().putString("location", dataSnapshot1.getValue().toString()).apply();
                                break;
                            case "gender":
                                sh.edit().putString("gender", dataSnapshot1.getValue().toString()).apply();
                                gender = dataSnapshot1.getValue().toString();
                                break;
                        }
                    }
                    setProgresses();
                    usersDatabase.insert(name, phone_, "", gender);
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                }
                else {
                    phone.setError(getResources().getString(R.string.unregistered_number));
                    signIn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Login.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setProgresses() {
        DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("users").child(phone.getText().toString()).child("progress");
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if (snapshot.hasChild("lesson")){
                            putProgress(snapshot.getKey(), snapshot.child("lesson").getValue().toString());
                            final SharedPreferences userProgress = getSharedPreferences(snapshot.getKey(), Context.MODE_PRIVATE);
                            userProgress.edit().putString("progress", snapshot.child("progress").getValue().toString()).apply();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void putProgress(String key, String lesson) {
        SharedPreferences lessons = getSharedPreferences("lessons", MODE_PRIVATE);
        SharedPreferences passed = getSharedPreferences("passed", MODE_PRIVATE);
        int j = Integer.parseInt(lesson);
        for (int i = 0; i <= j; i++){
            lessons.edit().putBoolean(key+i, true).apply();
            passed.edit().putBoolean(key+i, true).apply();
        }
        passed.edit().putBoolean((key+j), false).apply();
    }

    private void setLanguage() {
        SharedPreferences sharedPreferences = getSharedPreferences("lang", MODE_PRIVATE);
        Locale locale = new Locale(sharedPreferences.getString("lang", "am"));
        Configuration configuration = new Configuration();
        Locale.setDefault(locale);
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Welcome.class));
        finish();
    }

}
