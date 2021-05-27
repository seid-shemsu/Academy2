package com.izhar.academy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.izhar.academy.users.UsersDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class Setting extends AppCompatActivity {
    private EditText name, email, phone;
    private SharedPreferences userInfo, lang, img;
    SharedPreferences.Editor langEdit, userEdit;
    Button save;
    TextView amh, ara, eng, oro, language, pp;
    private ImageView image;
    int permission = 0, PICK_IMAGE_REQUEST = 1;
    Uri imgUri;
    UsersDatabase usersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setLanguage();
        setContentView(R.layout.activity_settings);
        setTitle(getResources().getString(R.string.setting));
        usersDatabase = new UsersDatabase(this, "users");
        userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        lang = getSharedPreferences("lang", MODE_PRIVATE);
        img = getSharedPreferences("image", MODE_PRIVATE);
        userEdit = userInfo.edit();
        langEdit = lang.edit();
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        language = findViewById(R.id.language);
        save = findViewById(R.id.save);
        pp = findViewById(R.id.pp_change);
        image = findViewById(R.id.circle);
        Picasso.Builder builder = new Picasso.Builder(this);
        try {
            UsersDatabase database = new UsersDatabase(this, "users");
            SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
            String gender = sharedPreferences.getString("gender", "");
            String uri = database.getUser(userInfo.getString("phone", ""));
            if (uri.isEmpty()) {
                if (gender.equalsIgnoreCase("male"))
                    builder.build().load(R.drawable.man).into(image);
                else if (gender.equalsIgnoreCase("female"))
                    builder.build().load(R.drawable.woman).into(image);
            } else
                builder.build().load(uri).into(image);
        } catch (Exception e) {
        }
        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        setValues();
        setLang();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                langEdit.apply();
                if (imgUri != null)
                    setDatabase();
                startActivity(new Intent(Setting.this, MainActivity.class));
                finish();
            }
        });

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Setting.this);
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
                fab.setVisibility(View.GONE);
                amh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*amh.setBackgroundResource(R.drawable.langselected);
                        ara.setBackgroundResource(R.drawable.lang_bg);
                        eng.setBackgroundResource(R.drawable.lang_bg);
                        oro.setBackgroundResource(R.drawable.lang_bg);
                        permission = 1;*/
                        langEdit.putString("lang", "am");
                        language.setText("አማርኛ");
                        dialog.dismiss();
                    }
                });
                oro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*oro.setBackgroundResource(R.drawable.langselected);
                        amh.setBackgroundResource(R.drawable.lang_bg);
                        ara.setBackgroundResource(R.drawable.lang_bg);
                        eng.setBackgroundResource(R.drawable.lang_bg);
                        permission = 1;*/
                        langEdit.putString("lang", "om");
                        language.setText("Afaan oromoo");
                        dialog.dismiss();
                    }
                });
                ara.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*ara.setBackgroundResource(R.drawable.langselected);
                        amh.setBackgroundResource(R.drawable.lang_bg);
                        eng.setBackgroundResource(R.drawable.lang_bg);
                        oro.setBackgroundResource(R.drawable.lang_bg);
                        permission = 1;*/
                        langEdit.putString("lang", "ar");
                        language.setText("العربية");
                        dialog.dismiss();
                    }
                });
                eng.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*eng.setBackgroundResource(R.drawable.langselected);
                        ara.setBackgroundResource(R.drawable.lang_bg);
                        amh.setBackgroundResource(R.drawable.lang_bg);
                        oro.setBackgroundResource(R.drawable.lang_bg);
                        permission = 1;*/
                        langEdit.putString("lang", "en");
                        language.setText("English");
                        dialog.dismiss();
                    }
                });
                /*fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getPermission(permission)){
                            dialog.dismiss();
                        }
                        else
                            Toast.makeText(Setting.this, getResources().getString(R.string.select_language), Toast.LENGTH_SHORT).show();
                    }
                });*/
                dialog.show();
            }
        });
    }

    private void setDatabase() {
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        usersDatabase.updateUser(name.getText().toString(), phone.getText().toString(), imgUri.toString(), sharedPreferences.getString("gender", "male"));
    }

    private boolean getPermission(int permission) {
        return permission == 1;
    }


    private void setLang() {
        String l = lang.getString("lang", "am");
        switch (l) {
            case "am":
                language.setText("አማርኛ");
                break;
            case "ar":
                language.setText("العربية");
                break;
            case "en":
                language.setText("English");
                break;
            case "om":
                language.setText("Afaan oromoo");
        }
    }

    private void setValues() {
        name.setText(userInfo.getString("name", ""));
        email.setText(userInfo.getString("email", ""));
        phone.setText(userInfo.getString("phone", ""));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();
            Picasso.Builder builder = new Picasso.Builder(this);
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    Toast.makeText(Setting.this, "" + exception.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            builder.build().load(imgUri).into(image);
            SharedPreferences image = getSharedPreferences("image", MODE_PRIVATE);
            image.edit().putString("uri", imgUri.toString()).apply();
        } else {
            Toast.makeText(this, "" + resultCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
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