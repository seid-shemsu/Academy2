package com.example.academy.PartAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.academy.R;

import java.util.List;

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.Holder> {
    Context context;
    List<String> numbers;

    public NumberAdapter(Context context, List<String> numbers) {
        this.context = context;
        this.numbers = numbers;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.number, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
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
