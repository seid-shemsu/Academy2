package com.izhar.academy.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.izhar.academy.MainActivity;
import com.izhar.academy.R;
import com.izhar.academy.tabs.CourseObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private SharedPreferences sharedPreferences, userProgress;
    private DatabaseReference databaseReference;
    private List<CourseObject> courseObjects = new ArrayList<>();
    private RecyclerView recyclerView;
    private CircularProgressBar progressBar;
    private TextView textView;
    private List<Integer> codes = new ArrayList<>();
    private int i = 1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setLanguage();
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = root.findViewById(R.id.progress_bar);
        textView = root.findViewById(R.id.no_course);
        sharedPreferences = getContext().getSharedPreferences("lang", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("lang", "am");
        databaseReference = FirebaseDatabase.getInstance().getReference().child(language).child("courses");
        ((MainActivity) getActivity()).setActionBarTitle(getContext().getResources().getString(R.string.app_name));
        addCourses();
        return root;
    }
    private void addCourses(){
        SharedPreferences user = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String phone = user.getString("phone", "");
        //String p;
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                i = 1;
                courseObjects.clear();
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()){
                    try {
                        userProgress = getContext().getSharedPreferences(phone + snapshot.getKey(), Context.MODE_PRIVATE);
                        String p = userProgress.getString("progress", "0");
                        String name = snapshot.child("name").getValue().toString();
                        String img_url = snapshot.child("img_url").getValue().toString();
                        double rating = Double.parseDouble(snapshot.child("rate").getValue().toString());
                        //courseObjects.add(new CourseObject(name, img_url, rating, i++, p));
                        courseObjects.add(new CourseObject(name, img_url, rating, snapshot.getKey(), p));
                        FragmentManager fragmentManager = getFragmentManager();
                        CourseAdapter courseAdapter = new CourseAdapter(getContext(), courseObjects, fragmentManager);
                        recyclerView.setAdapter(courseAdapter);
                        if (courseObjects.size() < 1 ){
                            textView.setVisibility(View.VISIBLE);
                        }
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }
}