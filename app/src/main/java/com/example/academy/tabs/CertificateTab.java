package com.example.academy.tabs;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CertificateTab extends Fragment {


    public CertificateTab() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;
    CircularProgressBar progressBar;
    TextView no;
    List<CertificateObject> certificates = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_certificate_tab, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = view.findViewById(R.id.progress_bar);
        no = view.findViewById(R.id.no_certificate);
        getCertificates();
        return view;
    }

    private void getCertificates() {
        SharedPreferences info = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String phone = info.getString("phone", "");
        final DatabaseReference user = FirebaseDatabase.getInstance().getReference("users").child(phone).child("certificates");
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String name = snapshot.child("course_name").getValue().toString();
                        String rate = snapshot.child("rating").getValue().toString();
                        String img_url = snapshot.child("img_url").getValue().toString();
                        certificates.add(new CertificateObject(name, img_url, Double.parseDouble(rate)));
                    }
                    TabAdapter tabAdapter = new TabAdapter(getContext(), certificates);
                    recyclerView.setAdapter(tabAdapter);
                }
                else
                    no.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
