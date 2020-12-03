package com.example.academy;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setLanguage();
        setContentView(R.layout.activity_login);
        setTitle(getResources().getString(R.string.sign_in));
        phone = findViewById(R.id.login_phone);
        signIn = findViewById(R.id.sign_in);
        progressBar = findViewById(R.id.sign_in_progress_bar);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionCheck()) {
                    if (phone.getText().toString().length() < 10) {
                        phone.setError("invalid format");
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

    private void account_exist(String phone, final Button signIn, final CircularProgressBar progressBar) {

        DatabaseReference mdata = FirebaseDatabase.getInstance().getReference().child("users").child(phone);
        mdata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("registered", true);
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        switch (dataSnapshot1.getKey()) {
                            case "name":
                                editor.putString("name", dataSnapshot1.getValue().toString());
                                break;
                            case "phone":
                                editor.putString("phone", dataSnapshot1.getValue().toString());
                                break;
                            case "email":
                                editor.putString("email", dataSnapshot1.getValue().toString());
                                break;
                        }
                    }
                    editor.apply();
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                }
                else {
                    snackbar = Snackbar.make(signIn, getResources().getString(R.string.unregistered_phone_number), Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction(getResources().getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
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

    private void setLanguage() {
        SharedPreferences sharedPreferences = getSharedPreferences("lang", MODE_PRIVATE);
        Locale locale = new Locale(sharedPreferences.getString("lang", "am"));
        Configuration configuration = new Configuration();
        Locale.setDefault(locale);
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }

}
