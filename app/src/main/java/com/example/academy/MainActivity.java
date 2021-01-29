package com.example.academy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.example.academy.help.HelpFragment;
import com.example.academy.users.UserObject;
import com.example.academy.users.UsersActivity;
import com.example.academy.users.UsersDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer = findViewById(R.id.drawer_layout);

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentManager fm = getSupportFragmentManager();
                switch (item.getItemId()){
                    case R.id.share:
                        ApplicationInfo api = getApplicationContext().getApplicationInfo();
                        String path = api.sourceDir;
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("application/vnd.android.package-archive");
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
                        startActivity(Intent.createChooser(intent, "share via"));
                        break;
                    case R.id.ask:
                        Ask_Fragment ask_fragment = new Ask_Fragment();
                        fm.beginTransaction().replace(R.id.nav_host_fragment, ask_fragment).addToBackStack(null).commit();
                        break;
                    case R.id.about:
                        About about = new About();
                        fm.beginTransaction().replace(R.id.nav_host_fragment, about).addToBackStack(null).commit();
                        break;
                    case R.id.help:
                        dialogHelp();
                        break;
                    case R.id.log_out:
                        dialogLog();
                        break;
                }
                drawer.close();
               return true;
            }


        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.ask, R.id.about, R.id.help)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);


        View header = navigationView.getHeaderView(0);
        TextView name = header.findViewById(R.id.name);
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        name.setText(sharedPreferences.getString("name", ""));
        Button setting = header.findViewById(R.id.settings);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Setting.class));
                finish();
            }
        });
        ImageView imageView = header.findViewById(R.id.imageView);
        try {
            UsersDatabase database = new UsersDatabase(this, "users");
            String uri = database.getUser(sharedPreferences.getString("phone", ""));
            database.close();
            Picasso.Builder builder = new Picasso.Builder(this);
            builder.build().load(uri).into(imageView);
        } catch (Exception e) {
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Personal.class));
                finish();
            }
        });
    }




    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void setLanguage() {
        SharedPreferences sharedPreferences = getSharedPreferences("lang", MODE_PRIVATE);
        Locale locale = new Locale(sharedPreferences.getString("lang", "am"));
        Configuration configuration = new Configuration();
        Locale.setDefault(locale);
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }


    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isOpen()) {
            drawer.close();
        } else
            super.onBackPressed();
    }

    private void dialogLog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.logout);
        dialog.setCanceledOnTouchOutside(false);
        Button logout, cancel;
        cancel = dialog.findViewById(R.id.cancel);
        logout = dialog.findViewById(R.id.signout);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearEverything();
            }
        });
        dialog.show();
    }
    private void clearEverything() {
        SharedPreferences userInfo, has_quiz, lessons, passed;
        userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        has_quiz = getSharedPreferences("has_quiz", Context.MODE_PRIVATE);
        lessons = getSharedPreferences("lessons", Context.MODE_PRIVATE);
        passed = getSharedPreferences("passed", Context.MODE_PRIVATE);
        userInfo.edit().clear().apply();
        has_quiz.edit().clear().apply();
        lessons.edit().clear().apply();
        passed.edit().clear().apply();
        startActivity(new Intent(this , UsersActivity.class));
        finish();
    }
    private void dialogHelp() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.help);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

}
