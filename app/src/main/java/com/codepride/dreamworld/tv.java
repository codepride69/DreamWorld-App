package com.codepride.dreamworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.media.MediaPlayer;
import androidx.appcompat.app.AppCompatActivity;

public class tv extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv);

        // Initialize the MediaPlayer and start playing audio
        mediaPlayer = MediaPlayer.create(this, R.raw.crowd); // Replace "your_audio_file" with your audio file's name
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        // Create a handler to switch to the "error" activity after a delay
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Stop the audio before moving to the next activity
                mediaPlayer.stop();
                mediaPlayer.release();

                // Start the "error" activity
                Intent intent = new Intent(tv.this, error.class);
                startActivity(intent);
                finish();
            }
        }, 12000); // Delay for 12 seconds
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Release the MediaPlayer when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
