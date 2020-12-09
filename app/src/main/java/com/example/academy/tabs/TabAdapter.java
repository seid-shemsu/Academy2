package com.example.academy.tabs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.academy.R;
import com.example.academy.ViewCertificate;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

public class TabAdapter extends RecyclerView.Adapter<TabAdapter.Holder> {
    Context context;
    List<CertificateObject> certificates;

    public TabAdapter(Context context, List<CertificateObject> certificates) {
        this.context = context;
        this.certificates = certificates;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.certificates, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        CertificateObject object = certificates.get(position);
        holder.name.setText(object.getCourse_name());
        holder.rate.setRating(object.getRating());
        Picasso.with(context).load(object.getImg_url()).placeholder(R.drawable.kitab).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return certificates.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView name;
        RatingBar rate;
        LinearLayout linear;
        Holder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            rate = itemView.findViewById(R.id.rate);
            linear = itemView.findViewById(R.id.linear);
            linear.setOnClickListener(this);
            image.setOnClickListener(this);
            rate.setOnClickListener(this);
            name.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            context.startActivity(new Intent(context, ViewCertificate.class)
                    .putExtra("name", certificates.get(getAdapterPosition()).getCourse_name())
                    .putExtra("img_url", certificates.get(getAdapterPosition()).getImg_url()));
        }
    }

}
