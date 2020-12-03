package com.example.academy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.academy.intro.MainIntro;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Start extends AppCompatActivity {
    TextView amh, ara, eng;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FloatingActionButton fab;
    int permission = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setTitle(getResources().getString(R.string.app_name));
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.getBoolean("registered", false)){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        sharedPreferences = getSharedPreferences("lang", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        amh = findViewById(R.id.amharic);
        ara = findViewById(R.id.arabic);
        eng = findViewById(R.id.english);
        fab = findViewById(R.id.fab);
        amh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amh.setBackgroundResource(R.drawable.langselected);
                ara.setBackgroundResource(R.drawable.lang_bg);
                eng.setBackgroundResource(R.drawable.lang_bg);
                permission = 1;
                editor.putString("lang", "am");
            }
        });
        ara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ara.setBackgroundResource(R.drawable.langselected);
                amh.setBackgroundResource(R.drawable.lang_bg);
                eng.setBackgroundResource(R.drawable.lang_bg);
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
            }
        });
    }

    private boolean getPermission(int permission) {
        return permission == 1;
    }
}
