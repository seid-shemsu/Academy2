package com.example.academy.tabs;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class CoursesTab extends Fragment {


    public CoursesTab() {
        // Required empty public constructor
    }
    private RecyclerView recyclerView;
    private CircularProgressBar progressBar;
    private TextView no_course;
    private CourseProgressAdapter courseProgressAdapter;
    private List<CourseObject> courseObjects = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setLanguage();
        View root =  inflater.inflate(R.layout.fragment_courses_tab, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        progressBar = root.findViewById(R.id.progress_bar);
        no_course = root.findViewById(R.id.no_course);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getCourseObjects();
        return root;
    }

    private void getCourseObjects() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "unknown");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(phone).child("progress");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String name = snapshot.child("course_name").getValue().toString();
                        String img_url = snapshot.child("img_url").getValue().toString();
                        String progress = snapshot.child("progress").getValue().toString();
                        String code = snapshot.getKey();
                        double rating = 0;
                        try {
                            rating = Double.parseDouble(snapshot.child("rate").getValue().toString());
                        } catch (Exception e) {
                        }
                        courseObjects.add(new CourseObject(name, img_url, rating, code, progress));
                    }
                    courseProgressAdapter = new CourseProgressAdapter(getContext(), courseObjects);
                    recyclerView.setAdapter(courseProgressAdapter);
                }
                else
                    no_course.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setLanguage() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("lang", Context.MODE_PRIVATE);
        Locale locale = new Locale(sharedPreferences.getString("lang", "am"));
        Configuration configuration = new Configuration();
        Locale.setDefault(locale);
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }
}
