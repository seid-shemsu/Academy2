package com.example.academy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class Personal extends AppCompatActivity {
    PagerAdapter pagerAdapter;
    TabLayout tab;
    ViewPager view;
    TextView name, email, location;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setLanguage();
        setContentView(R.layout.activity_personal);
        tab = findViewById(R.id.tab);
        view = findViewById(R.id.viewpager);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        location = findViewById(R.id.location);
        image = findViewById(R.id.photo);
        setData();
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tab.getTabCount());
        view.setAdapter(pagerAdapter);
        view.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                view.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.settings){
            startActivity(new Intent(this, Setting.class));
            finish();
        }
        else {
            onBackPressed();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;    }

    private void setData() {
        SharedPreferences info = getSharedPreferences("userInfo", MODE_PRIVATE);
        name.setText(info.getString("name", ""));
        email.setText(info.getString("email", ""));
        location.setText(info.getString("location", ""));
        try {
            Picasso.Builder builder = new Picasso.Builder(this);
            builder.build().load(info.getString("uri", "")).into(image);
        }
        catch (Exception e){
        }
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
        super.onBackPressed();
    }
}
