package com.izhar.academy;

import android.media.MediaPlayer;

public class MusicManager {
    public static MediaPlayer mediaPlayer;
    public static void Player(MediaPlayer mp){
        mediaPlayer = mp;
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }
    public static void stop(){
        if (mediaPlayer != null)
            if (mediaPlayer.isPlaying())
                mediaPlayer.pause();
    }
}
