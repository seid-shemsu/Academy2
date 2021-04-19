package com.example.academy.tabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.academy.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
        final CourseObject currentObject = courseObjects.get(position);
        holder.course_name.setText(currentObject.getCourse_name());
        File img = context.getApplicationContext().getFileStreamPath(String.valueOf(currentObject.getCode()));
        if (img.exists())
            holder.course_img.setImageBitmap(loadImage(context, String.valueOf(currentObject.getCode())));
        else {
            Picasso.with(context).load(currentObject.getImg_url()).placeholder(R.drawable.kitab).into(holder.course_img);
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Looper.prepare();
                            saveImage(context, Picasso.with(context).load(currentObject.getImg_url()).get(), String.valueOf(currentObject.getCode()));
                        } catch (IOException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        int progress = Integer.parseInt(currentObject.getProgress());
        holder.completion_progress.setText(progress + context.getResources().getString(R.string.complered));
        holder.progress_bar.setProgress(progress);
        holder.course_rate.setRating((float) currentObject.getRating());
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
