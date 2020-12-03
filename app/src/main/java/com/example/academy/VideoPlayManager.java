package com.example.academy;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideoPlayManager extends AppCompatActivity {
    String videoId;
    private YouTubePlayerView youTubePlayerView;
    private FullScreenHelper fullScreenHelper = new FullScreenHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play_manager);
        setTitle(getIntent().getExtras().getString("title"));
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        videoId = getIntent().getExtras().getString("link");

        initYouTubePlayerView();
        /*WebView webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/"+ part +".html");*/
    }

    private void initYouTubePlayerView() {


        // The player will automatically release itself when the activity is destroyed.
        // The player will automatically pause when the activity is stopped
        // If you don't add YouTubePlayerView as a lifecycle observer, you will have to release it manually.
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer,
                        getLifecycle(),
                        videoId,
                        0f
                );
                addFullScreenListenerToPlayer();
            }
        });
    }

    private void addFullScreenListenerToPlayer() {
        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                fullScreenHelper.enterFullScreen();

            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                fullScreenHelper.exitFullScreen();
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (youTubePlayerView.isFullScreen())
            youTubePlayerView.exitFullScreen();
        else
            super.onBackPressed();
    }
    /*private String getVideoId(String part) {
        switch (part){
            case "one":
                return videoLinks[0];
            case "two":
                return videoLinks[1];
            case "three":
                return videoLinks[2];
            case "four":
                return videoLinks[3];
            case "five":
                return videoLinks[4];
            case "six":
                return videoLinks[5];
            case "seven":
                return videoLinks[6];
            case "eight":
                return videoLinks[7];
            case "nine":
                return videoLinks[8];
            case "ten":
                return videoLinks[9];
            case "eleven":
                return videoLinks[10];
            case "twelve":
                return videoLinks[11];
            case "thirteen":
                return videoLinks[12];
            case "fourteen":
                return videoLinks[13];
            case "fifteen":
                return videoLinks[14];
        }
        return null;
    }*/
}
