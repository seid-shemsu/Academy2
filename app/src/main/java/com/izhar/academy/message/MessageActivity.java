package com.izhar.academy.message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.academy.R;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private TextView no_message;
    private ProgressBar progress;
    List<MessageObject> messageObjects = new ArrayList<>();
    MessageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Messages");
        setContentView(R.layout.activity_message);
        recycler = findViewById(R.id.recycler);
        no_message = findViewById(R.id.no_message);
        progress = findViewById(R.id.progress);
        FirebaseDatabase.getInstance().getReference("new").child("Messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                messageObjects.add(dataSnapshot.getValue(MessageObject.class));
                            }
                            adapter = new MessageAdapter(MessageActivity.this, messageObjects);
                            recycler.setAdapter(adapter);
                        }
                        else {
                            no_message.setVisibility(View.VISIBLE);
                        }
                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}