package com.example.academy.ui.home;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.academy.MainActivity;
import com.example.academy.PartAdapters.NumberAdapter;
import com.example.academy.PartAdapters.TitleAdapter;
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
public class CoursePartFragment extends Fragment {


    public CoursePartFragment() {
        // Required empty public constructor
    }

    RecyclerView number, title;
    List<String> titles = new ArrayList<>();
    List<String> numbers = new ArrayList<>();
    List<String> youtubes = new ArrayList<>();
    List<String> musics = new ArrayList<>();
    List<String> htmls = new ArrayList<>();
    String name;
    String[] numbersArray, titlesArray;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_course_part, container, false);
        number = root.findViewById(R.id.number_recycler);
        title = root.findViewById(R.id.title_recycler);
        number.setHasFixedSize(true);
        number.setLayoutManager(new LinearLayoutManager(getContext()));
        title.setHasFixedSize(true);
        title.setLayoutManager(new LinearLayoutManager(getContext()));
        name = getArguments().getString("course_name");
        ((MainActivity) getActivity()).setActionBarTitle(getArguments().getString("title"));
        numbersArray = getResources().getStringArray(R.array.numbers);
        titlesArray = getResources().getStringArray(R.array.parts);
        getItems();
        return root;
    }
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
                    int i = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if (snapshot.hasChildren()){
                            titles.add(snapshot.child("name").getValue().toString());
                            numbers.add(numbersArray[i++]);
                        }
                    }
                    FragmentManager fragmentManager = getFragmentManager();
                    TitleAdapter titleAdapter = new TitleAdapter(getContext(), titles, fragmentManager, name, getArguments().getString("course_code"));
                    NumberAdapter numberAdapter = new NumberAdapter(getContext(), numbers);
                    number.setAdapter(numberAdapter);
                    title.setAdapter(titleAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
