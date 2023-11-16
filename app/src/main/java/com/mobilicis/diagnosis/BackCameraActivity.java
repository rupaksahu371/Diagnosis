package com.mobilicis.diagnosis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.video.VideoCapture;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

public class BackCameraActivity extends AppCompatActivity {

    Button visible, invisible;
    VideoView showVideo;
    Uri showVideoURI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_camera);

        showVideo = findViewById(R.id.showVideo);
        visible = findViewById(R.id.visible_btn);
        invisible = findViewById(R.id.notVisible_btn);

        visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BackCameraActivity.this, TestActivity.class);
                intent.putExtra("backCameraRecording", "Back camera is recording.");
                intent.putExtra("source", "backIntent");
                startActivity(intent);
                finish();
            }
        });

        invisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BackCameraActivity.this, TestActivity.class);
                intent.putExtra("backCameraRecording", "Back camera is not recording.");
                intent.putExtra("source", "backIntent");
                startActivity(intent);
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent!=null){
           String VideoURI = intent.getStringExtra("showURI");
           showVideoURI = Uri.parse(VideoURI);
        }
        Log.d("Rupak", "onCreate: "+ showVideoURI);
        showVideo.setVideoURI(showVideoURI);
        // starts the video
        showVideo.start();
        showVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }
}