package com.example.academy.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.academy.R;
import com.example.academy.tabs.CourseObject;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ImageViewHolder> {
    private FragmentManager fragmentManager;
    private Context context;
    private List<CourseObject> courseObjects;

    public CourseAdapter(Context context, List<CourseObject> courseObjects, FragmentManager fragmentManager){
        this.context = context;
        this.courseObjects = courseObjects;
        this.fragmentManager = fragmentManager;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_course_item, parent, false);
        return new ImageViewHolder(view, fragmentManager);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        CourseObject courseObject = courseObjects.get(position);
        holder.course_name.setText(courseObject.getCourse_name());
        holder.course_rate.setRating(courseObject.getRating());
        Picasso.with(context).load(courseObject.getImg_url()).placeholder(R.drawable.kitab).into(holder.course_img);
    }

    @Override
    public int getItemCount() {
        return courseObjects.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView course_img;
        TextView course_name;
        RatingBar course_rate;
        FragmentManager fragmentManager;
        private ImageViewHolder(@NonNull View itemView, FragmentManager fragmentManager) {
            super(itemView);
            course_img = itemView.findViewById(R.id.course_img);
            course_name = itemView.findViewById(R.id.course_name);
            course_rate = itemView.findViewById(R.id.course_rate);
            this.fragmentManager = fragmentManager;
            course_rate.setOnClickListener(this);
            course_name.setOnClickListener(this);
            course_img.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            SharedPreferences lessons = context.getSharedPreferences("lessons", Context.MODE_PRIVATE);
            lessons.edit().putBoolean(getAdapterPosition()+1 + "1", true).apply();
            SharedPreferences passed  = context.getSharedPreferences("passed", Context.MODE_PRIVATE);
            passed.edit().putBoolean(getAdapterPosition()+1 + "0", true).apply();
            Fragment singleCourseFragment = new SingleCourseFragment();
            Bundle bundle = new Bundle();
            bundle.putString("course_name", courseObjects.get(getAdapterPosition()).getCourse_name());
            bundle.putString("course_code", Integer.toString(getAdapterPosition()+1));
            singleCourseFragment.setArguments(bundle);
            courseObjects.clear();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, singleCourseFragment).addToBackStack(null).commit();

        }
    }
}
