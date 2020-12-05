package com.example.academy.PartAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.academy.R;
import com.example.academy.detail.Detail;

import java.util.List;

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.Holder> {

    private FragmentManager fragmentManager;
    Context context;
    List<String> titles;
    String name;
    String course_code;
    public TitleAdapter(Context context, List<String> titles, FragmentManager fragmentManager, String name, String course_code) {
        this.context = context;
        this.titles = titles;
        this.fragmentManager = fragmentManager;
        this.name = name;
        this.course_code = course_code;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.title, parent, false);
        return new Holder(view, fragmentManager);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.title.setText(titles.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        LinearLayout linear;
        FragmentManager fragmentManager;
        public Holder(@NonNull View itemView, FragmentManager fragmentManager) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            linear = itemView.findViewById(R.id.linear);
            this.fragmentManager = fragmentManager;
            title.setOnClickListener(this);
            linear.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            SharedPreferences passed = context.getSharedPreferences("passed", Context.MODE_PRIVATE);
            if (passed.getBoolean(course_code + (getAdapterPosition()), false)){
                Fragment detail = new Detail();
                Bundle bundle = new Bundle();
                bundle.putString("part_number", Integer.toString(getAdapterPosition() + 1));
                bundle.putString("course_name", name);
                bundle.putString("title", titles.get(getAdapterPosition()));
                bundle.putString("course_code", course_code);
                detail.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, detail).addToBackStack(null).commit();
            }
            else {
                Toast.makeText(context, "please pass the previous part first", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
