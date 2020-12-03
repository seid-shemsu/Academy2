package com.example.academy.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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

import com.example.academy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Test extends AppCompatActivity {
    ListView listView;
    Button submit;
    String quiz;
    List<String> answers;
    int[] answersCheck = new int[15];
    String[] answered = new String[15];
    String[] question;
    int pass;
    Snackbar snackbar;
    ArrayList<String> questions, answer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage();
        setContentView(R.layout.question_list);
        quiz = getIntent().getExtras().getString("quiz");
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
                            dialog.setContentView(R.layout.final_passed);
                            Button ok = dialog.findViewById(R.id.passed_btn);
                            /*editor.putInt("average", pass * 10);
                            editor.putString("date", getDate());
                            editor.putBoolean("quiz final", true);
                            editor.apply();*/
                            //saveToFirebase(pass * 10, getDate());
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //startActivity(new Intent(Test.this, ViewCertificate.class).putExtra("from", "final"));
                                    finish();
                                }
                            });
                            Button result = dialog.findViewById(R.id.view_final_result);
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
                        dialog.setContentView(R.layout.passed);
                        TextView result = dialog.findViewById(R.id.result);
                        String s = pass + "/" + questions.size();
                        result.setText(s);
                        Button ok = dialog.findViewById(R.id.passed_btn);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
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

            private String getDate() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                return simpleDateFormat.format(new Date());
            }
        });

    }

    private ArrayList<String> getDefaultAnswer() {
        return getIntent().getExtras().getStringArrayList("answers");
    }

    private void saveToFirebase(int i, String date) {
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(sharedPreferences.getString("phone", "null"));
        if (!sharedPreferences.getString("phone", "null").equals("null")) {
            databaseReference.child("score").setValue(Integer.toString(i));
            databaseReference.child("certified").setValue("yes");
            databaseReference.child("issuedDate").setValue(date);
        } else {
            Toast.makeText(this, "error 702", Toast.LENGTH_SHORT).show();
        }
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
