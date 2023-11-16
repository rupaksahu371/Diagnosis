package com.mobilicis.diagnosis;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class FrontCameraActivity extends AppCompatActivity {

    Button visible, invisible;
    VideoView showVideo;
    Uri showVideoURI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_camera);

        showVideo = findViewById(R.id.showVideo1);
        visible = findViewById(R.id.visible_btn1);
        invisible = findViewById(R.id.notVisible_btn1);

        visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FrontCameraActivity.this, TestActivity.class);
                intent.putExtra("frontCameraRecording", "Front camera is Recording.");
                intent.putExtra("source", "frontIntent");
                startActivity(intent);
                finish();
            }
        });

        invisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FrontCameraActivity.this, TestActivity.class);
                intent.putExtra("frontCameraRecording", "Front camera is not Recording.");
                intent.putExtra("source", "frontIntent");
                startActivity(intent);
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent!=null){
           String VideoURI = intent.getStringExtra("showURI");
            showVideoURI = Uri.parse(VideoURI);
        }
        showVideo.setVideoURI(showVideoURI);

        showVideo.start();
        showVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }
}