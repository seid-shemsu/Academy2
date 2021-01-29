package com.example.academy.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(layoutManager);
        final SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recycler);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                View view = snapHelper.findSnapView(layoutManager);
                int pos = layoutManager.getPosition(view);
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(pos);
                LinearLayout rl1 = viewHolder.itemView.findViewById(R.id.linear1);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    rl1.animate().setDuration(150).scaleX(1.1f).scaleY(1.1f).setInterpolator(new AccelerateInterpolator()).start();
                }
                else{
                    rl1.animate().setDuration(150).scaleX(0.90f).scaleY(0.90f).setInterpolator(new AccelerateInterpolator()).start();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        UsersDatabase usersDatabase = new UsersDatabase(this, "users");
        userObjects = usersDatabase.getAll();
        adapter = new UsersAdapter(this, userObjects);
        recycler.setAdapter(adapter);
        if (userObjects.size() == 0)
            no_acc.setVisibility(View.VISIBLE);
    }
}
