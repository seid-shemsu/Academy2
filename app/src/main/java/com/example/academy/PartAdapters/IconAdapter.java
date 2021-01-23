package com.example.academy.PartAdapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.academy.R;

import java.util.List;

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.Holder> {
    Context context;
    List<String> icons;
    SharedPreferences lessons, passed;
    public IconAdapter(Context context, List<String> icons) {
        this.context = context;
        this.icons = icons;

        try {
            lessons = context.getSharedPreferences("lessons", Context.MODE_PRIVATE);
            passed = context.getSharedPreferences("passed", Context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.icon, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (lessons.getBoolean(icons.get(position), false)){
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.unlocked));
        }
        else
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.locked));

        if (passed.getBoolean(icons.get(position), false))
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_correct));

    }

    @Override
    public int getItemCount() {
        return icons.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView icon;
        public Holder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}
