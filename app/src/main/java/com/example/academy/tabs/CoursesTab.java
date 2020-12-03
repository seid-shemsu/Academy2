package com.example.academy.tabs;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.academy.CourseProgressAdapter;
import com.example.academy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CoursesTab extends Fragment {


    public CoursesTab() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;
    CourseProgressAdapter courseProgressAdapter;
    List<CourseObject> courseObjects;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_courses_tab, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        courseObjects = getCourseObjects();
        courseProgressAdapter = new CourseProgressAdapter(getContext(), courseObjects);
        recyclerView.setAdapter(courseProgressAdapter);
        return root;
    }

    private List<CourseObject> getCourseObjects() {
        final List<CourseObject> courseObjects = new ArrayList<>();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "unknown");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(phone);
        databaseReference.child("finished_courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        courseObjects.add((CourseObject) snapshot.getValue());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        String current_course_name = sharedPreferences.getString("current_course", "null");
        String progress = sharedPreferences.getString("current_course_progress", "0");
        String current_course_img_url = sharedPreferences.getString("current_course_img_url","");
        int rating = sharedPreferences.getInt("current_course_rating", 0);
        courseObjects.add(new CourseObject(current_course_name, progress, current_course_img_url, rating));
        return courseObjects;
    }

}
