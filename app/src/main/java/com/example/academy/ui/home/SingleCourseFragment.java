package com.example.academy.ui.home;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.academy.MainActivity;
import com.example.academy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_single_course, container, false);
        Bundle bundle = this.getArguments();
        name = bundle.getString("course_name");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("lang", Context.MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang", "am");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(lang).child(name);
        course_name = root.findViewById(R.id.course_name);
        start = root.findViewById(R.id.start);
        cont = root.findViewById(R.id.cont);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("name"))
                    course_name.setText(dataSnapshot.child("name").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ((MainActivity) getActivity()).setActionBarTitle(name);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.clear_progress);
                Button clear = dialog.findViewById(R.id.clear);
                Button canel = dialog.findViewById(R.id.cancel);
                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        SharedPreferences lesson = getContext().getSharedPreferences("lessons", Context.MODE_PRIVATE);
                        SharedPreferences passed = getContext().getSharedPreferences("passed", Context.MODE_PRIVATE);
                        SharedPreferences has_test = getContext().getSharedPreferences("has_test", Context.MODE_PRIVATE);
                        has_test.edit().clear().apply();
                        lesson.edit().clear().apply();
                        passed.edit().clear().apply();
                        passed.edit().putBoolean(getArguments().getString("course_code") + 0, true).apply();
                        lesson.edit().putBoolean(getArguments().getString("course_code") +1, true).apply();
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
                canel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
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

}