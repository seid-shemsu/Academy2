package com.example.academy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class Personal extends AppCompatActivity {
    PagerAdapter pagerAdapter;
    TabLayout tab;
    ViewPager view;
    TextView name, email, location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        tab = findViewById(R.id.tab);
        view = findViewById(R.id.viewpager);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        location = findViewById(R.id.location);
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
        startActivity(new Intent(this, Setting.class));
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
    }

}
