package com.mobilicis.diagnosis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class BackCameraActivity extends AppCompatActivity {

    Button visible, invisible;
    ImageView showImg;
    Uri showImgURI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_camera);

        showImg = findViewById(R.id.showImg);
        visible = findViewById(R.id.visible_btn);
        invisible = findViewById(R.id.notVisible_btn);

        visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BackCameraActivity.this, TestActivity.class);
                intent.putExtra("backCameraWorking", "Back camera is working.");
                intent.putExtra("source", "backIntent");
                startActivity(intent);
                finish();
            }
        });

        invisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BackCameraActivity.this, TestActivity.class);
                intent.putExtra("backCameraWorking", "Back camera is not working.");
                intent.putExtra("source", "backIntent");
                startActivity(intent);
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent!=null){
            showImgURI = intent.getData();
        }
        showImg.setImageURI(showImgURI);
    }
}