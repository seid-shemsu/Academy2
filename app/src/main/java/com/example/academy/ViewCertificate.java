package com.example.academy;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class ViewCertificate extends AppCompatActivity {
    String img_url, course, code;
    ImageView image;
    Button save;
    TextView name;
    RelativeLayout relativeLayout;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setLanguage();
        setContentView(R.layout.activity_view_certificate);
        img_url = getIntent().getExtras().getString("img_url");
        course = getIntent().getExtras().getString("name");
        code = getIntent().getExtras().getString("course_code");
        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        save = findViewById(R.id.save);
        relativeLayout = findViewById(R.id.relative);
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        name.setText(sharedPreferences.getString("name", ""));
        loadImage();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23){
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)  == PackageManager.PERMISSION_GRANTED &&  checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        saveToPhone();
                    }
                    else {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                }
                else {
                    saveToPhone();
                }
            }
        });
    }
    private void loadImage(){
        //Picasso.with(this).load(img_url).into(image);
        File img = getApplicationContext().getFileStreamPath((code + "certificate"));
        if (img.exists()){
            image.setImageBitmap(loadImage(this, (code + "certificate")));
        }
        else {
            Picasso.with(this).load(img_url).into(image);
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Looper.prepare();
                            saveImage(ViewCertificate.this, Picasso.with(ViewCertificate.this).load(img_url).get(), (code + "certificate"));
                        } catch (IOException e) {
                            Toast.makeText(ViewCertificate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();
            } catch (Exception e) {
                Toast.makeText(ViewCertificate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        name.setVisibility(View.VISIBLE);
    }
    private void saveToPhone() {
        Bitmap bitmap = loadBitmapFromView(relativeLayout);
        String root = Environment.getExternalStorageDirectory().toString();
        File dir = new File(root + "/africa/certificates");
        dir.mkdirs();
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, course+".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Toast.makeText(ViewCertificate.this, getResources().getString(R.string.saved_to_phone), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ViewCertificate.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }


    private Bitmap loadImage(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fiStream;
        try {
            fiStream    = context.openFileInput(imageName);
            bitmap      = BitmapFactory.decodeStream(fiStream);
            fiStream.close();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return bitmap;
    }

    private void saveImage(Context context, Bitmap b, String imageName) {
        FileOutputStream foStream;
        try {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.close();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
