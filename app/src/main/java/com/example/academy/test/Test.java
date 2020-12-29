package com.example.academy.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academy.Asses;
import com.example.academy.R;
import com.example.academy.tabs.CertificateObject;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Test extends AppCompatActivity {
    private ListView listView;
    private Button submit;
    private String quiz, course_code;
    private List<String> answers;
    private int[] answersCheck = new int[15];
    private String[] answered = new String[15];
    private String[] question;
    private int pass;
    private Snackbar snackbar;
    private ArrayList<String> questions, answer;
    private SharedPreferences passed, lessons;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage();
        setContentView(R.layout.question_list);
        quiz = getIntent().getExtras().getString("quiz");
        course_code = getIntent().getExtras().getString("course_code");
        setTitle(getResources().getString(R.string.take_quiz) + " " +course_code);
        listView = findViewById(R.id.question_list);
        //question = getQuestion(quiz);
        questions = getIntent().getExtras().getStringArrayList("questions");
        final QuestionAdapter questionAdapter = new QuestionAdapter(this, questions, 0);
        listView.setAdapter(questionAdapter);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("quiz " + quiz, true);
                editor.apply();*/
                answers = questionAdapter.getAnswer();
                if (answers.contains("-")) {
                    Toast.makeText(Test.this, getResources().getString(R.string.attemt_all_question), Toast.LENGTH_SHORT).show();
                }
                else {

                    ArrayList<String> defaultAnswerString = getDefaultAnswer();
                    for (int i = 0; i < answers.size(); i++)
                        if (answers.get(i).equals(defaultAnswerString.get(i))) {
                            pass++;
                            answersCheck[i] = 1;
                            answered[i] = answers.get(i);
                        } else {
                            answersCheck[i] = 0;
                            answered[i] = answers.get(i);
                        }

                    final Dialog dialog = new Dialog(Test.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCanceledOnTouchOutside(false);
                    if (quiz.contains("final")) {
                        if (connectionCheck()) {
                            setCertificate();
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setContentView(R.layout.final_passed);
                            Button ok = dialog.findViewById(R.id.asses);
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(Test.this, Asses.class)
                                            .putExtra("course_code", course_code));
                                    finish();

                                }
                            });
                            Button result = dialog.findViewById(R.id.answer);
                            result.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    final QuestionAdapter questionAdapter = new QuestionAdapter(getApplicationContext(), answered, questions, answersCheck, 1);
                                    listView.setAdapter(questionAdapter);
                                    submit.setEnabled(false);
                                }
                            });
                            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    pass = 0;
                                }
                            });
                            //editor.apply();
                            dialog.show();
                        }
                        else {
                            snackbar = Snackbar.make(v, getResources().getString(R.string.connet_internet), Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction(getResources().getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                        }
                    }
                    else {
                        lessons = getSharedPreferences("lessons", MODE_PRIVATE);
                        passed = getSharedPreferences("passed", MODE_PRIVATE);
                        editor = lessons.edit();
                        editor.putBoolean(course_code + (Integer.parseInt(quiz) +1), true);
                        editor.apply();
                        editor = passed.edit();
                        editor.putBoolean(course_code+quiz, true);
                        editor.apply();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setContentView(R.layout.passed);
                        TextView result = dialog.findViewById(R.id.result);
                        String s = pass + "/" + questions.size();
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
                                final QuestionAdapter questionAdapter = new QuestionAdapter(getApplicationContext(), answered, questions, answersCheck, 1);
                                listView.setAdapter(questionAdapter);
                                submit.setVisibility(View.GONE);
                            }
                        });
                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                pass = 0;
                            }
                        });
                        dialog.show();
                    }
                }
            }
        });

    }
    private void setCertificate() {
        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        String phone = userInfo.getString("phone", "");
        final DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("users").child(phone).child("certificates").child(course_code);
        DatabaseReference cert = FirebaseDatabase.getInstance().getReference().child("certificates").child(course_code);
        cert.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    final String img_url = dataSnapshot.child("img_url").getValue().toString();
                    SharedPreferences lang = getSharedPreferences("lang", MODE_PRIVATE);
                    DatabaseReference course = FirebaseDatabase.getInstance().getReference().child(lang.getString("lang", "am")).child("courses").child(course_code);
                    course.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.child("name").getValue().toString();
                            String rate = dataSnapshot.child("rate").getValue().toString();
                            user.setValue(new CertificateObject(name, img_url, Double.parseDouble(rate)));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<String> getDefaultAnswer() {
        return getIntent().getExtras().getStringArrayList("answers");
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

    private void setLanguage() {
        SharedPreferences sharedPreferences = getSharedPreferences("lang", MODE_PRIVATE);
        Locale locale = new Locale(sharedPreferences.getString("lang", "am"));
        Configuration configuration = new Configuration();
        Locale.setDefault(locale);
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }
}
