package com.mobilicis.diagnosis;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FrontCameraActivity extends AppCompatActivity {

    Button visible, invisible;
    ImageView showImg;
    Uri showImgURI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_camera);

        showImg = findViewById(R.id.showImg1);
        visible = findViewById(R.id.visible_btn1);
        invisible = findViewById(R.id.notVisible_btn1);

        visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FrontCameraActivity.this, TestActivity.class);
                intent.putExtra("frontCameraWorking", "Front camera is working.");
                intent.putExtra("source", "frontIntent");
                startActivity(intent);
                finish();
            }
        });

        invisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FrontCameraActivity.this, TestActivity.class);
                intent.putExtra("frontCameraWorking", "Front camera is not working.");
                intent.putExtra("source", "frontIntent");
                startActivity(intent);
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent!=null){
           String ImgURI = intent.getStringExtra("showUri");
           showImgURI = Uri.parse(ImgURI);
        }
        showImg.setImageURI(showImgURI);
    }
}