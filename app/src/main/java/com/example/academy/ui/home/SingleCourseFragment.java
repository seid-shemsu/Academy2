package com.example.academy.ui.home;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academy.MainActivity;
import com.example.academy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class SingleCourseFragment extends Fragment {


    public SingleCourseFragment() {
        // Required empty public constructor
    }

    TextView course_name;
    Button start, cont;
    String name;
    String course_code;
    SharedPreferences lesson;
    private void setLanguage() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("lang", Context.MODE_PRIVATE);
        Locale locale = new Locale(sharedPreferences.getString("lang", "am"));
        Configuration configuration = new Configuration();
        Locale.setDefault(locale);
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setLanguage();
        View root = inflater.inflate(R.layout.fragment_single_course, container, false);
        final Bundle bundle = this.getArguments();
        name = bundle.getString("course_name");
        final SharedPreferences sharedPreferences = getContext().getSharedPreferences("lang", Context.MODE_PRIVATE);
        final String lang = sharedPreferences.getString("lang", "am");
        course_name = root.findViewById(R.id.course_name);
        start = root.findViewById(R.id.start);
        cont = root.findViewById(R.id.cont);
        course_code = bundle.getString("course_code");
        lesson = getContext().getSharedPreferences("lessons", Context.MODE_PRIVATE);
        SharedPreferences passed = getContext().getSharedPreferences("passed", Context.MODE_PRIVATE);
        passed.edit().putBoolean(getArguments().getString("course_code") + 0, true).apply();
        lesson.edit().putBoolean(getArguments().getString("course_code") + 1, true).apply();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(lang).child(name).child("name");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                course_name.setText(dataSnapshot.getValue().toString());
                if (!lesson.getBoolean(course_code + "2", false)) {
                    start.setVisibility(View.VISIBLE);
                } else {
                    cont.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ((MainActivity) getActivity()).setActionBarTitle(name);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent(getArguments().getString("course_code"));
                Fragment coursePartFragment = new CoursePartFragment();
                FragmentManager fragmentManager = getFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("course_name", name);
                bundle.putString("title", course_name.getText().toString());
                bundle.putString("course_code", getArguments().getString("course_code"));
                coursePartFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, coursePartFragment).addToBackStack(null).commit();
            }

        });
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment coursePartFragment = new CoursePartFragment();
                FragmentManager fragmentManager = getFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("course_name", name);
                bundle.putString("title", course_name.getText().toString());
                bundle.putString("course_code", getArguments().getString("course_code"));
                coursePartFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, coursePartFragment).addToBackStack(null).commit();
            }
        });
        return root;
    }

    private void addStudent(String course_code) {
        SharedPreferences lang = getContext().getSharedPreferences("lang", Context.MODE_PRIVATE);
        SharedPreferences user = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String phone = user.getString("phone", "");
        if (lang.getString("lang", "am").equals("om")) {
            DatabaseReference om = FirebaseDatabase.getInstance().getReference().child("courses").child("om").child(course_code);
            om.child("attendants").child(phone).setValue(user.getString("name", " "));

        } else {
            DatabaseReference am = FirebaseDatabase.getInstance().getReference().child("courses").child("am").child(course_code);
            DatabaseReference en = FirebaseDatabase.getInstance().getReference().child("courses").child("en").child(course_code);
            DatabaseReference ar = FirebaseDatabase.getInstance().getReference().child("courses").child("ar").child(course_code);
            am.child("attendants").child(phone).setValue(user.getString("name", " "));
            en.child("attendants").child(phone).setValue(user.getString("name", " "));
            ar.child("attendants").child(phone).setValue(user.getString("name", " "));
        }

    }


}