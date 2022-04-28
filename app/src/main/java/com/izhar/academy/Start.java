package com.izhar.academy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;
import com.izhar.academy.intro.MainIntro;
import com.izhar.academy.users.UsersActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class Start extends AppCompatActivity {
    TextView amh, ara, eng, oro;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FloatingActionButton fab;
    int permission = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage();
        setContentView(R.layout.activity_start);
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.getBoolean("started", false)){
            startActivity(new Intent(this, Login.class));
            finish();
        }
        sharedPreferences = getSharedPreferences("lang", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        amh = findViewById(R.id.amharic);
        ara = findViewById(R.id.arabic);
        eng = findViewById(R.id.english);
        oro = findViewById(R.id.oromo);
        fab = findViewById(R.id.fab);
        amh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amh.setBackgroundResource(R.drawable.langselected);
                ara.setBackgroundResource(R.drawable.lang_bg);
                eng.setBackgroundResource(R.drawable.lang_bg);
                oro.setBackgroundResource(R.drawable.lang_bg);
                permission = 1;
                editor.putString("lang", "am");
            }
        });
        oro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oro.setBackgroundResource(R.drawable.langselected);
                amh.setBackgroundResource(R.drawable.lang_bg);
                ara.setBackgroundResource(R.drawable.lang_bg);
                eng.setBackgroundResource(R.drawable.lang_bg);
                permission = 1;
                editor.putString("lang", "om");
            }
        });
        ara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ara.setBackgroundResource(R.drawable.langselected);
                amh.setBackgroundResource(R.drawable.lang_bg);
                eng.setBackgroundResource(R.drawable.lang_bg);
                oro.setBackgroundResource(R.drawable.lang_bg);
                editor.putString("lang", "ar");
                permission = 1;
            }
        });
        eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eng.setBackgroundResource(R.drawable.langselected);
                ara.setBackgroundResource(R.drawable.lang_bg);
                amh.setBackgroundResource(R.drawable.lang_bg);
                oro.setBackgroundResource(R.drawable.lang_bg);
                editor.putString("lang", "en");
                permission = 1;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPermission(permission)){
                    editor.apply();
                    startActivity(new Intent(Start.this, MainIntro.class));
                    finish();
                }
                else {
                    Toast.makeText(Start.this, getResources().getString(R.string.select_language), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean getPermission(int permission) {
        return permission == 1;
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
