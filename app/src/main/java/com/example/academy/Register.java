package com.example.academy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.academy.users.UsersDatabase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class Register extends AppCompatActivity {
    EditText name, phone, email;
    Spinner country, gender;
    CircularProgressBar progressBar;
    Button register;
    Snackbar snackbar;
    SharedPreferences sharedPreferences;
    private DatabaseReference databaseReference;
    UsersDatabase usersDatabase;
    TextInputLayout nameInput, phoneInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setLanguage();
        setContentView(R.layout.registration);
        setTitle(getString(R.string.register));
        usersDatabase = new UsersDatabase(this, "users");
        nameInput = findViewById(R.id.nameInput);
        phoneInput = findViewById(R.id.phoneInput);
        name = findViewById(R.id.name);
        SharedPreferences lang = getSharedPreferences("lang", MODE_PRIVATE);
        if (lang.getString("lang", "am").equals("ar")){
            nameInput.setHint(Html.fromHtml("<font color =\"#ff0000\">" + "* " + "</font>" + getResources().getString(R.string.name)));
            phoneInput.setHint(Html.fromHtml("<font color =\"#ff0000\">" + "* " + "</font>" + getResources().getString(R.string.phone_number)));
        }
        else {
            nameInput.setHint(Html.fromHtml(getResources().getString(R.string.name) + "<font color =\"#ff0000\">" + "* " + "</font>"));
            phoneInput.setHint(Html.fromHtml(getResources().getString(R.string.phone_number) + "<font color =\"#ff0000\">" + "* " + "</font>"));
        }
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        country = findViewById(R.id.country);
        gender = findViewById(R.id.gender);
        //code = findViewById(R.id.code);
        progressBar = findViewById(R.id.progress_bar);
        register = findViewById(R.id.register);
        ArrayList<String> countries = new ArrayList<>();
        countries.addAll(Arrays.asList(getResources().getStringArray(R.array.country)));
        Collections.sort(countries);
        countries.add(0, getResources().getString(R.string.ethiopia));
        countries.add(0, getResources().getString(R.string.select_country));
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_item, countries);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        country.setAdapter(adapter);
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionCheck()){
                    if(check(name, phone, email)){
                        progressBar.setVisibility(View.VISIBLE);
                        register.setVisibility(View.GONE);
                        RegisterToDatabase(name, email, phone, country, gender, progressBar, register);
                    }
                }
                else {
                    snackbar = Snackbar.make(v, getResources().getString(R.string.connet_internet), Snackbar.LENGTH_INDEFINITE);
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
        assert connectivityManager != null;
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

    private boolean check(EditText name, EditText phone, EditText email) {
        if (name.getText().toString().length() < 5 && !name.getText().toString().contains(" ")){
            name.setError(getResources().getString(R.string.name_required));
            return false;
        }
        if (phone.getText().toString().length() < 9 ){
            phone.setError(getResources().getString(R.string.invalid_format));
            return false;
        }
        if (email.getText().toString().length()>0 && !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            email.setError(getResources().getString(R.string.invalid_email));
            return false;
        }
        if (country.getSelectedItemPosition() == 0){
            Toast.makeText(this, getString(R.string.select_country), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (gender.getSelectedItemPosition() == 0){
            Toast.makeText(this, getString(R.string.select_gender), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void RegisterToDatabase(final EditText name, final EditText email, final EditText phone, final Spinner country, final Spinner gender, final CircularProgressBar progressBar, final Button register) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String fullPhone = phone.getText().toString();
        DatabaseReference mdata = FirebaseDatabase.getInstance().getReference().child("users").child(fullPhone);
        final String g = gender.getSelectedItemPosition() == 1 ? "male" : "female";
        final User user = new User(name.getText().toString(), fullPhone, email.getText().toString(), country.getSelectedItem().toString(), g);
        mdata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    phone.setError(getResources().getString(R.string.registerd_phone));
                    progressBar.setVisibility(View.GONE);
                    register.setVisibility(View.VISIBLE);
                }
                else {
                    editor.putBoolean("registered", true);
                    editor.putString("name", name.getText().toString());
                    editor.putString("phone", fullPhone);
                    editor.putString("email", email.getText().toString());
                    editor.putString("location", country.getSelectedItem().toString());
                    editor.putString("gender", g);
                    editor.apply();
                    usersDatabase.insert(name.getText().toString(), fullPhone, "", g);
                    databaseReference.child(fullPhone).setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(Register.this, getResources().getString(R.string.registered_successfully), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Register.this, MainActivity.class));
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Register.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                register.setVisibility(View.VISIBLE);
            }

        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Welcome.class));
        finish();
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
