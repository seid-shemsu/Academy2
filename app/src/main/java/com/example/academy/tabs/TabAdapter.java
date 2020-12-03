package com.example.academy.tabs;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.academy.R;
import com.squareup.picasso.Picasso;

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
        Picasso.with(context).load(object.getImg_url()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return certificates.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        RatingBar rate;
        public Holder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            rate = itemView.findViewById(R.id.rate);
        }
    }
}
