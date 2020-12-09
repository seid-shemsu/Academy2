package com.example.academy.tabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.academy.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CourseProgressAdapter extends RecyclerView.Adapter<CourseProgressAdapter.ImageViewHolder> {

    List<CourseObject> courseObjects;
    Context context;

    public CourseProgressAdapter(Context context, List<CourseObject> courseObjects){
        this.context = context;
        this.courseObjects = courseObjects;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_progresses, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        CourseObject currentObject = courseObjects.get(position);
        holder.course_name.setText(currentObject.getCourse_name());
        Picasso.with(context).load(currentObject.getImg_url()).placeholder(R.drawable.kitab).into(holder.course_img);
        int progress = Integer.parseInt(currentObject.getProgress());
        holder.completion_progress.setText(progress + " % completed");
        holder.progress_bar.setProgress(progress);
    }

    @Override
    public int getItemCount() {
        return courseObjects.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView course_img;
        TextView course_name, completion_progress;
        RatingBar course_rate;
        ProgressBar progress_bar;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            course_img = itemView.findViewById(R.id.course_img);
            course_name = itemView.findViewById(R.id.course_name);
            completion_progress = itemView.findViewById(R.id.completion_progress);
            course_rate = itemView.findViewById(R.id.course_rate);
            progress_bar = itemView.findViewById(R.id.progress_bar);
        }
    }
}
