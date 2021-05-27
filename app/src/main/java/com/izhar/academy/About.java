package com.izhar.academy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class About  extends Fragment implements View.OnClickListener {
    ImageView tg, fb, tw, yt, in;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.about, container, false);
        tg = root.findViewById(R.id.img_tg);
        fb = root.findViewById(R.id.img_fb);
        //tw = root.findViewById(R.id.img_twit);
        yt = root.findViewById(R.id.img_you);
        //in = root.findViewById(R.id.img_insta);
        tg.setOnClickListener(this);
        fb.setOnClickListener(this);
        //tw.setOnClickListener(this);
        yt.setOnClickListener(this);
        //in.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_tg:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/Africa_Academy1")));
                break;
            case R.id.img_fb:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com/Africa_Academy1")));
                break;
            case R.id.img_you:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCOY5oCke6jNy2Fpl-pe7EmA")));
                break;
        }
    }
}