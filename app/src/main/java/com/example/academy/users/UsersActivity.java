package com.example.academy.users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academy.MainActivity;
import com.example.academy.R;
import com.example.academy.Welcome;

import java.util.List;

public class UsersActivity extends AppCompatActivity {
    RecyclerView recycler;
    UsersAdapter adapter;
    List<UserObject> userObjects;
    Button add;
    TextView no_acc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        setTitle(R.string.app_name);
        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        if (userInfo.getBoolean("registered", false)){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        recycler = findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        add = findViewById(R.id.add);
        no_acc = findViewById(R.id.no_account);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UsersActivity.this, Welcome.class));
                finish();
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        UsersDatabase usersDatabase = new UsersDatabase(this, "users");
        userObjects = usersDatabase.getAll();
        adapter = new UsersAdapter(this, userObjects);
        recycler.setAdapter(adapter);
        if (userObjects.size() == 0)
            no_acc.setVisibility(View.VISIBLE);
    }
}
