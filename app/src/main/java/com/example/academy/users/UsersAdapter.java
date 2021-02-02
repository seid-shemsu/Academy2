package com.example.academy.users;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.academy.Login;
import com.example.academy.MainActivity;
import com.example.academy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.Holder> {

    Context context;
    List<UserObject> userObjects;

    public UsersAdapter(Context context, List<UserObject> userObjects) {
        this.context = context;
        this.userObjects = userObjects;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.users_card, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        UserObject object = userObjects.get(position);
        holder.name.setText(object.getName());
        try {
            Picasso.with(context).load(object.getUri()).placeholder(R.drawable.person).into(holder.pp);
        }
        catch (Exception e){
        }
        finally {
            if (object.getUri().isEmpty()){
                if (object.getGender().equals("female"))
                    holder.pp.setImageDrawable(context.getResources().getDrawable(R.drawable.woman));
                else
                    holder.pp.setImageDrawable(context.getResources().getDrawable(R.drawable.man));
                //Picasso.with(context).load(R.drawable.person).into(holder.pp);
            }

        }
    }

    @Override
    public int getItemCount() {
        return userObjects.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView name;
        ImageView pp;
        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            pp = itemView.findViewById(R.id.pp);
            pp.setOnClickListener(this);
            pp.setOnClickListener(this);
            pp.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            v.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus){
                        Animation anim = AnimationUtils.loadAnimation(context, R.anim.in);
                        v.startAnimation(anim);
                        anim.setFillAfter(true);
                    }
                    else {
                        Animation anim = AnimationUtils.loadAnimation(context, R.anim.out);
                        v.startAnimation(anim);
                        anim.setFillAfter(true);
                    }
                }
            });
            final String phone = userObjects.get(getAdapterPosition()).getPhone();
            DatabaseReference mdata = FirebaseDatabase.getInstance().getReference().child("users").child(phone);
            final Dialog wait = new Dialog(context);
            wait.setCanceledOnTouchOutside(false);
            wait.requestWindowFeature(Window.FEATURE_NO_TITLE);
            wait.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            wait.setContentView(R.layout.wait);
            wait.show();
            mdata.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("registered", true);
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            switch (dataSnapshot1.getKey()) {
                                case "name":
                                    editor.putString("name", dataSnapshot1.getValue().toString());
                                    break;
                                case "phone":
                                    editor.putString("phone", dataSnapshot1.getValue().toString());
                                    break;
                                case "email":
                                    editor.putString("email", dataSnapshot1.getValue().toString());
                                    break;
                                case "country":
                                    editor.putString("location", dataSnapshot1.getValue().toString());
                                    break;
                                case "gender":
                                    editor.putString("gender", dataSnapshot1.getValue().toString());
                                    break;
                            }
                        }
                        setProgresses(phone);
                        editor.apply();
                        context.startActivity(new Intent(context, MainActivity.class));
                        wait.dismiss();
                        ((Activity)context).finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public boolean onLongClick(View v) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.user_option);
            dialog.setCanceledOnTouchOutside(false);
            Button remove = dialog.findViewById(R.id.remove);
            Button cancel = dialog.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UsersDatabase usersDatabase = new UsersDatabase(context, "users");
                    usersDatabase.delete(userObjects.get(getAdapterPosition()).getPhone());
                    Toast.makeText(context, R.string.user_removed, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    removeUser(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyDataSetChanged();
                }

                private void removeUser(int adapterPosition) {
                    userObjects.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                    notifyItemRangeChanged(0, userObjects.size());
                }
            });
            dialog.show();
            return false;
        }
    }
    private void setProgresses(String phone) {
        DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("users").child(phone).child("progress");
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if (snapshot.hasChild("lesson"))
                            putProgress(snapshot.getKey(), snapshot.child("lesson").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void putProgress(String key, String lesson) {
        SharedPreferences lessons = context.getSharedPreferences("lessons", Context.MODE_PRIVATE);
        SharedPreferences passed = context.getSharedPreferences("passed", Context.MODE_PRIVATE);
        int j = Integer.parseInt(lesson);
        for (int i = 0; i <= j; i++){
            lessons.edit().putBoolean(key+i, true).apply();
            passed.edit().putBoolean(key+i, true).apply();
        }
        passed.edit().putBoolean((key+j), false).apply();
    }
}
