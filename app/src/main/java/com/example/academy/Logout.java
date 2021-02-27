package com.example.academy;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.academy.users.UsersActivity;
import com.example.academy.users.UsersAdapter;

import java.io.File;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class Logout extends Fragment {


    public Logout() {
        // Required empty public constructor
    }
    private void setLanguage() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("lang", Context.MODE_PRIVATE);
        Locale locale = new Locale(sharedPreferences.getString("lang", "am"));
        Configuration configuration = new Configuration();
        Locale.setDefault(locale);
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }
    private Button cancel;
    private Button signout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setLanguage();
        View root = inflater.inflate(R.layout.logout, container, false);
        cancel = root.findViewById(R.id.cancel);
        signout = root.findViewById(R.id.signout);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearEverything();
            }
        });
        return root;
    }

    private void clearEverything() {
        SharedPreferences userInfo, has_quiz, lessons, passed;
        userInfo = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        has_quiz = getContext().getSharedPreferences("has_quiz", Context.MODE_PRIVATE);
        lessons = getContext().getSharedPreferences("lessons", Context.MODE_PRIVATE);
        passed = getContext().getSharedPreferences("passed", Context.MODE_PRIVATE);
        getContext().getSharedPreferences("1", Context.MODE_PRIVATE).edit().clear().apply();
        getContext().getSharedPreferences("2", Context.MODE_PRIVATE).edit().clear().apply();
        getContext().getSharedPreferences("3", Context.MODE_PRIVATE).edit().clear().apply();
        getContext().getSharedPreferences("4", Context.MODE_PRIVATE).edit().clear().apply();
        getContext().getSharedPreferences("5", Context.MODE_PRIVATE).edit().clear().apply();
        getContext().getSharedPreferences("6", Context.MODE_PRIVATE).edit().clear().apply();
        getContext().getSharedPreferences("7", Context.MODE_PRIVATE).edit().clear().apply();
        getContext().getSharedPreferences("8", Context.MODE_PRIVATE).edit().clear().apply();
        getContext().getSharedPreferences("9", Context.MODE_PRIVATE).edit().clear().apply();
        getContext().getSharedPreferences("10", Context.MODE_PRIVATE).edit().clear().apply();
        getContext().getSharedPreferences("11", Context.MODE_PRIVATE).edit().clear().apply();
        getContext().getSharedPreferences("12", Context.MODE_PRIVATE).edit().clear().apply();
        getContext().getSharedPreferences("13", Context.MODE_PRIVATE).edit().clear().apply();
        getContext().getSharedPreferences("14", Context.MODE_PRIVATE).edit().clear().apply();
        getContext().getSharedPreferences("15", Context.MODE_PRIVATE).edit().clear().apply();
        userInfo.edit().clear().apply();
        has_quiz.edit().clear().apply();
        lessons.edit().clear().apply();
        passed.edit().clear().apply();
        startActivity(new Intent(getContext(), UsersActivity.class));
        getActivity().finish();
    }

}
