package com.example.academy.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.academy.R;
import com.example.academy.tabs.CourseObject;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
        final CourseObject courseObject = courseObjects.get(position);
        holder.course_name.setText(courseObject.getCourse_name());
        holder.course_rate.setRating((float) courseObject.getRating());
        //holder.attendants.setText(courseObject.getStudents() + "");
        holder.progressBar.setProgress(Integer.parseInt(courseObject.getProgress()));
        File img = context.getApplicationContext().getFileStreamPath(String.valueOf(courseObject.getCode()));
        if (img.exists()){
            holder.course_img.setImageBitmap(loadImage(context, String.valueOf(courseObject.getCode())));
        }
        else {
            Picasso.with(context).load(courseObject.getImg_url()).placeholder(R.drawable.kitab).into(holder.course_img);
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Looper.prepare();
                            saveImage(context, Picasso.with(context).load(courseObject.getImg_url()).get(), String.valueOf(courseObject.getCode()));
                        } catch (IOException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public int getItemCount() {
        return courseObjects.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView course_img;
        TextView course_name, attendants;
        RatingBar course_rate;
        FragmentManager fragmentManager;
        ProgressBar progressBar;
        private ImageViewHolder(@NonNull View itemView, FragmentManager fragmentManager) {
            super(itemView);
            course_img = itemView.findViewById(R.id.course_img);
            course_name = itemView.findViewById(R.id.course_name);
            course_rate = itemView.findViewById(R.id.course_rate);
            //attendants = itemView.findViewById(R.id.attendants);
            progressBar = itemView.findViewById(R.id.progress_bar);

            this.fragmentManager = fragmentManager;
            course_rate.setOnClickListener(this);
            course_name.setOnClickListener(this);
            course_img.setOnClickListener(this);
            //attendants.setOnClickListener(this);
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

    private Bitmap loadImage(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fiStream;
        try {
            fiStream    = context.openFileInput(imageName);
            bitmap      = BitmapFactory.decodeStream(fiStream);
            fiStream.close();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return bitmap;
    }

    private void saveImage(Context context, Bitmap b, String imageName) {
        FileOutputStream foStream;
        try {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.close();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
