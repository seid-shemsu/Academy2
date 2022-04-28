package com.izhar.academy.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.izhar.academy.Asses;
import com.izhar.academy.R;
import com.izhar.academy.tabs.CertificateObject;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Quiz extends AppCompatActivity {
    RecyclerView recyclerView;
    Button submit;
    CircularProgressBar progressBar;
    List<String> default_answer = new ArrayList<>();
    List<Object> objects = new ArrayList<>();
    QA qa;
    Snackbar snackbar;
    String course_code, quiz;
    int res = 0;
    private SharedPreferences passed;
    private SharedPreferences.Editor editor;
    private TextView no;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage();
        quiz = getIntent().getExtras().getString("quiz");
        setContentView(R.layout.activity_quiz);
        setTitle(quiz);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recycler);
        no = findViewById(R.id.no);
        recyclerView.setHasFixedSize(true);
        dialog = new Dialog(Quiz.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        submit = findViewById(R.id.submit);
        progressBar = findViewById(R.id.progress_bar);
        course_code = getIntent().getExtras().getString("course_code");

        String part = getIntent().getExtras().getString("course_code") + "_" + getIntent().getExtras().getString("quiz");
        getQ(part);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qa.getAnswer().contains("100")) {
                    Toast.makeText(Quiz.this, R.string.attemt_all_question, Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < qa.getAnswer().size(); i++) {
                        if (qa.getAnswer().get(i).equalsIgnoreCase(default_answer.get(i)))
                            res++;
                    }
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //code for final exam
                    if (getIntent().getExtras().getString("quiz").contains("final")) {
                        if (connectionCheck()) {
                            if (res * 100.0 / default_answer.size() >= 70.0) {
                                double m = res * 100.0 / default_answer.size();
                                int mark = (int) m;
                                setCertificate(mark);
                                getSharedPreferences("lessons", MODE_PRIVATE).edit().putBoolean(course_code + "final_passed", true).apply();
                                dialog.setContentView(R.layout.final_passed);
                                TextView resultText = dialog.findViewById(R.id.result);
                                String s = res + "/" + default_answer.size();
                                resultText.setText(s);
                                Button ok = dialog.findViewById(R.id.asses);
                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(Quiz.this, Asses.class).putExtra("course_code", course_code));
                                        finish();
                                    }
                                });
                                Button result = dialog.findViewById(R.id.answer);
                                result.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        List<String> answers = qa.getAnswer();
                                        qa = new QA(getApplicationContext(), objects, answers, default_answer, 1);
                                        recyclerView.setAdapter(qa);
                                        submit.setVisibility(View.GONE);
                                    }
                                });
                                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        res = 0;
                                    }
                                });
                            } else {
                                dialog.setContentView(R.layout.final_failed);
                                TextView resultText = dialog.findViewById(R.id.result);
                                String s = res + "/" + default_answer.size();
                                resultText.setText(s);
                                Button back = dialog.findViewById(R.id.back);
                                back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        onBackPressed();
                                    }
                                });
                            }
                            //editor.apply();
                            dialog.show();
                        } else {
                            snackbar = Snackbar.make(v, getResources().getString(R.string.connet_internet), Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction(getResources().getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                        }
                    }
                    //code for quizzes
                    else {

                        getSharedPreferences("passed", MODE_PRIVATE).edit().putBoolean(course_code + quiz, true).apply();
                        //setRate(res, default_answer.size());
                        dialog.setContentView(R.layout.passed);
                        TextView result = dialog.findViewById(R.id.result);
                        String s = res + "/" + default_answer.size();
                        result.setText(s);
                        Button ok = dialog.findViewById(R.id.passed_btn);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();

                            }
                        });
                        Button check = dialog.findViewById(R.id.check);
                        check.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                List<String> answers = qa.getAnswer();
                                qa = new QA(getApplicationContext(), objects, answers, default_answer, 1);
                                recyclerView.setAdapter(qa);
                                submit.setVisibility(View.GONE);
                            }
                        });
                        dialog.show();
                    }

                }
            }
        });
    }


    private void getQ(String part) {
        final DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("tests").child(part);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                objects.clear();
                default_answer.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String question = snapshot.child("question").getValue().toString();
                    String answer = snapshot.child("answer").getValue().toString();
                    default_answer.add(answer);
                    List<String> choices = new ArrayList<>();
                    for (DataSnapshot snapshot1 : snapshot.child("choices").getChildren()) {
                        choices.add(snapshot1.getValue().toString());
                    }
                    objects.add(new Object(question, answer, choices));
                }
                progressBar.setVisibility(View.GONE);

                if (objects.size() == 0) {
                    no.setVisibility(View.VISIBLE);
                    getSharedPreferences("passed", MODE_PRIVATE).edit().putBoolean(course_code + quiz, true).apply();
                } else {
                    qa = new QA(Quiz.this, objects, 0);
                    recyclerView.setAdapter(qa);
                    submit.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setCertificate(final int mark) {
        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        final String phone = userInfo.getString("phone", "");
        final DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("users").child(phone).child("certificates").child(course_code);
        DatabaseReference cert = FirebaseDatabase.getInstance().getReference().child(course_code.substring(0, 2)).child("courses").child(course_code);
        final String id = System.currentTimeMillis() + "";
        final DatabaseReference certificateRequest = FirebaseDatabase.getInstance().getReference("certificateRequest").child(id);
        cert.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    final String img_url = dataSnapshot.child("img_url").getValue().toString();
                    String name = dataSnapshot.child("name").getValue().toString();
                    String rate = dataSnapshot.child("rate").getValue().toString();
                    certificateRequest.child("id").setValue(id);
                    certificateRequest.child("mark").setValue(mark);
                    certificateRequest.child("name").setValue(getSharedPreferences("userInfo", MODE_PRIVATE).getString("name", ""));
                    certificateRequest.child("code").setValue(course_code);
                    certificateRequest.child("phone").setValue(phone);
                    user.setValue(new CertificateObject(name, img_url, Double.parseDouble(rate), course_code, (double) mark));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean connectionCheck() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
        int connected = 0;
        for (NetworkInfo networkInfo : info) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                connected = 1;
            }
        }
        return connected != 0;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        dialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
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
