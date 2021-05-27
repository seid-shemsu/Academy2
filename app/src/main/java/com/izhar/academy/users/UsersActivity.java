package com.izhar.academy.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.izhar.academy.MainActivity;
import com.izhar.academy.R;
import com.izhar.academy.Welcome;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;

public class UsersActivity extends AppCompatActivity {
    RecyclerView recycler;
    UsersAdapter adapter;
    List<UserObject> userObjects;
    Button add;
    TextView no_acc;
    TextView amh, ara, eng, oro;
    int permission = 0;
    private SharedPreferences lang;
    SharedPreferences.Editor langEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage();
        setContentView(R.layout.activity_users);
        setTitle(R.string.app_name);

        lang = getSharedPreferences("lang", MODE_PRIVATE);

        langEdit = lang.edit();
        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        if (userInfo.getBoolean("registered", false)){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        /*UsersDatabase usersDatabase = new UsersDatabase(this, "users");
        SQLiteDatabase db = usersDatabase.getWritableDatabase();
        usersDatabase.onUpgrade(db, 1, 1);*/
        recycler = findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(layoutManager);
        final SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recycler);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                try {
                    View view = snapHelper.findSnapView(layoutManager);
                    int pos = layoutManager.getPosition(view);
                    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(pos);
                    LinearLayout rl1 = viewHolder.itemView.findViewById(R.id.linear1);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE){
                        rl1.animate().setDuration(150).scaleX(1.1f).scaleY(1.1f).setInterpolator(new AccelerateInterpolator()).start();
                    }
                    else{
                        rl1.animate().setDuration(150).scaleX(1).scaleY(1).setInterpolator(new AccelerateInterpolator()).start();
                    }
                } catch (Exception e) {
                    Toast.makeText(UsersActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        add = findViewById(R.id.add);
        no_acc = findViewById(R.id.no_account);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UsersActivity.this, Welcome.class));
                finish();
            }
        });

        UsersDatabase usersDatabase = new UsersDatabase(this, "users");
        userObjects = usersDatabase.getAll();
        adapter = new UsersAdapter(this, userObjects);
        recycler.setAdapter(adapter);
        /*if (userObjects.size() == 0)
            no_acc.setVisibility(View.VISIBLE);*/
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.settings) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.language_setting);
            dialog.setCanceledOnTouchOutside(false);
            FloatingActionButton fab;
            amh = dialog.findViewById(R.id.amharic);
            ara = dialog.findViewById(R.id.arabic);
            eng = dialog.findViewById(R.id.english);
            oro = dialog.findViewById(R.id.oromo);
            fab = dialog.findViewById(R.id.fab);
            //fab.setVisibility(View.GONE);
            amh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    amh.setBackgroundResource(R.drawable.langselected);
                    ara.setBackgroundResource(R.drawable.lang_bg);
                    eng.setBackgroundResource(R.drawable.lang_bg);
                    oro.setBackgroundResource(R.drawable.lang_bg);
                    permission = 1;
                    langEdit.putString("lang", "am");
                    //language.setText("አማርኛ");
                    //dialog.dismiss();
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
                    langEdit.putString("lang", "om");
                    //language.setText("Afaan oromoo");
                    //dialog.dismiss();
                }
            });
            ara.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ara.setBackgroundResource(R.drawable.langselected);
                    amh.setBackgroundResource(R.drawable.lang_bg);
                    eng.setBackgroundResource(R.drawable.lang_bg);
                    oro.setBackgroundResource(R.drawable.lang_bg);
                    permission = 1;
                    langEdit.putString("lang", "ar");
                    //language.setText("العربية");
                    //dialog.dismiss();
                }
            });
            eng.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eng.setBackgroundResource(R.drawable.langselected);
                    ara.setBackgroundResource(R.drawable.lang_bg);
                    amh.setBackgroundResource(R.drawable.lang_bg);
                    oro.setBackgroundResource(R.drawable.lang_bg);
                    permission = 1;
                    langEdit.putString("lang", "en");
                    //language.setText("English");
                    //dialog.dismiss();
                }
            });
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (getPermission(permission)){
                        langEdit.apply();
                        startActivity(new Intent(UsersActivity.this, UsersActivity.class));
                        finish();
                    }
                    else
                        Toast.makeText(UsersActivity.this, getResources().getString(R.string.select_language), Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        } else {
            onBackPressed();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
