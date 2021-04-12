package com.example.academy.ui.home;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.academy.MainActivity;
import com.example.academy.PartAdapters.IconAdapter;
import com.example.academy.PartAdapters.NumberAdapter;
import com.example.academy.PartAdapters.TitleAdapter;
import com.example.academy.R;
import com.example.academy.database.DB;
import com.example.academy.database.PartObject;
import com.example.academy.test.Quiz;
import com.example.academy.test.Test;
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
public class CoursePartFragment extends Fragment {


    public CoursePartFragment() {
        // Required empty public constructor
    }

    private CircularProgressBar progress;
    private RecyclerView number, title, icon;
    private Button final_btn ;
    private List<String> titles = new ArrayList<>();
    private List<String> numbers = new ArrayList<>();
    private List<String> icons = new ArrayList<>();
    private String name, course_code, course_title;
    //LinearLayout lb;
    TitleAdapter titleAdapter;

    LinearLayout l;
    //private String[] numbersArray, titlesArray;
    private int i = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setLanguage();
        View root = inflater.inflate(R.layout.fragment_course_part, container, false);
        //number = root.findViewById(R.id.number_recycler);
        title = root.findViewById(R.id.title_recycler);
        //icon = root.findViewById(R.id.icon_recycler);
        final_btn = root.findViewById(R.id.final_btn);
        //lb = root.findViewById(R.id.lb);
        progress = root.findViewById(R.id.progress);
        l = root.findViewById(R.id.l);
        //icon.setHasFixedSize(true);
        //number.setHasFixedSize(true);
        title.setHasFixedSize(true);
        //icon.setLayoutManager(new LinearLayoutManager(getContext()));
        //number.setLayoutManager(new LinearLayoutManager(getContext()));
        title.setLayoutManager(new LinearLayoutManager(getContext()));
        name = getArguments().getString("course_name");
        course_code = getArguments().getString("course_code");
        course_title  = getArguments().getString("title");
        ((MainActivity) getActivity()).setActionBarTitle(course_title);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("lang", Context.MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang", "am");
        getItems();
        final_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences lesson = getContext().getSharedPreferences("lessons", Context.MODE_PRIVATE);
                if (lesson.getBoolean(course_code + i, false)){
                    if (!lesson.getBoolean("final_passed", false)) {
                        startActivity(new Intent(getContext(), Quiz.class)
                                .putExtra("course_code", course_code)
                                .putExtra("quiz", "final"));
                    } else {
                        Toast.makeText(getContext(), getContext().getString(R.string.final_exam_taken), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), getResources().getString(R.string.take_tests), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }
    /*private void getFromDatabase(){
        DB db = new DB(getContext());
        List<PartObject> partObjects = db.getParts(name, getContext().getSharedPreferences("lang", Context.MODE_PRIVATE).getString("lang", "am"));
        titles.clear();
        numbers.clear();
        icons.clear();
        i = 0;
        for (PartObject partObject : partObjects){
            titles.add(partObject.getName());
            numbers.add(partObject.getNumber());
            icons.add(course_code + partObject.getNumber());
        }
        FragmentManager fragmentManager = getFragmentManager();
        TitleAdapter titleAdapter = new TitleAdapter(getContext(), titles, fragmentManager, name, course_code);
        NumberAdapter numberAdapter = new NumberAdapter(getContext(), numbers, course_code);
        IconAdapter iconAdapter = new IconAdapter(getContext(), icons);
        icon.setAdapter(iconAdapter);
        number.setAdapter(numberAdapter);
        title.setAdapter(titleAdapter);
        final_btn.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
    }*/
    private void getItems(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("lang", Context.MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang", "am");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(lang).child(name);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    titles.clear();
                    numbers.clear();
                    icons.clear();
                    i = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if (snapshot.hasChildren()){
                            i++;
                            titles.add(snapshot.child("name").getValue().toString());
                            numbers.add(Integer.toString(i));
                            icons.add(course_code + i);
                        }
                    }
                    FragmentManager fragmentManager = getFragmentManager();
                    titleAdapter = new TitleAdapter(getContext(), titles, icons, numbers, fragmentManager, name, course_code);
                    //NumberAdapter numberAdapter = new NumberAdapter(getContext(), numbers, course_code);
                    //IconAdapter iconAdapter = new IconAdapter(getContext(), icons);
                    //icon.setAdapter(iconAdapter);
                    //number.setAdapter(numberAdapter);
                    title.setAdapter(titleAdapter);
                    l.setVisibility(View.VISIBLE);
                    final_btn.setVisibility(View.VISIBLE);
                    //lb.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                }
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
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //Write down your refresh code here, it will call every time user come to this fragment.
            //If you are using listview with custom adapter, just call notifyDataSetChanged().
            if (getFragmentManager() != null) {

                getFragmentManager()
                        .beginTransaction()
                        .detach(this)
                        .attach(this)
                        .commit();
            }
        }
    }

}
