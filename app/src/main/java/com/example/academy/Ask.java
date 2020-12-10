package com.example.academy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Ask extends Fragment {

    Button send;
    EditText question;
    SharedPreferences user;
    String phone;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.ask_question, container, false);
        send = root.findViewById(R.id.send);
        question = root.findViewById(R.id.question);
        user = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        phone = user.getString("phone", "unknown");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question.getText().toString().trim().length() < 25){
                    question.setError("minimum word length");
                    Toast.makeText(getContext(), "please use more words", Toast.LENGTH_SHORT).show();
                }
                else {
                    Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.wait);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    DatabaseReference questions = FirebaseDatabase.getInstance().getReference().child("questions");
                    SimpleDateFormat smf = new SimpleDateFormat("dd-MM-yyyy");
                    String date = smf.format(new Date());
                    questions.child(date).child(System.currentTimeMillis() + "").setValue(question.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "your question has been sent successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getContext(), MainActivity.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
        return root;
    }
}