package com.izhar.academy.semester;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.academy.MainActivity;
import com.izhar.academy.R;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SemesterFragment extends Fragment {
    private List<SemesterObject> semesterObjects = new ArrayList<>();
    private RecyclerView recyclerView;
    private CircularProgressBar progressBar;
    DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setLanguage();
        setHasOptionsMenu(true);
        final View root = inflater.inflate(R.layout.fragment_semester, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = root.findViewById(R.id.progress_bar);
        databaseReference = FirebaseDatabase.getInstance().getReference("new").child("am").child("semesters");
        ((MainActivity) getActivity()).setActionBarTitle(getContext().getResources().getString(R.string.app_name));
        addCourses();
        return root;
    }

    private void addCourses() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                semesterObjects.clear();
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue().toString();
                    String img_url = snapshot.child("img").getValue().toString();
                    semesterObjects.add(new SemesterObject(name, img_url));
                }
                FragmentManager fragmentManager = getFragmentManager();
                SemesterAdapter semesterAdapter = new SemesterAdapter(getContext(), semesterObjects, fragmentManager);
                recyclerView.setAdapter(semesterAdapter);
                progressBar.setVisibility(View.GONE);

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
    /*TextView count;
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.message, menu);
        final MenuItem menuItem = menu.findItem(R.id.message);
        View actionView = menuItem.getActionView();
        count = (TextView) actionView.findViewById(R.id.count);
        count.setText("2");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.message){
            startActivity(new Intent(getContext(), MessageActivity.class));
        }
        return true;
    }*/
}