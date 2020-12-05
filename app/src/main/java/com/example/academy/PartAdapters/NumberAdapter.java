package com.example.academy.PartAdapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.academy.R;

import java.util.List;

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.Holder> {
    Context context;
    List<String> numbers;
    String course_code;
    public NumberAdapter(Context context, List<String> numbers, String course_code) {
        this.context = context;
        this.numbers = numbers;
        this.course_code = course_code;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.number, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        SharedPreferences lessons = context.getSharedPreferences("lessons", Context.MODE_PRIVATE);
        SharedPreferences passed = context.getSharedPreferences("passed", Context.MODE_PRIVATE);
        if (lessons.getBoolean(course_code + (position +1), false) && !passed.getBoolean(course_code + (position +1), false)){
            holder.number.setBackground(context.getResources().getDrawable(R.drawable.number_bg));
            holder.number.setTextColor(context.getResources().getColor(R.color.white));
        }

        holder.number.setText(numbers.get(position));
    }

    @Override
    public int getItemCount() {
        return numbers.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView number;
        public Holder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
        }
    }
}
