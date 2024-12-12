package com.example.BookLibraryTemi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Button;
import android.view.View;

public class LibraryTourActivity extends AppCompatActivity {

    private ImageView mapImageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_tour);

        mapImageView = findViewById(R.id.img_floor_map);

        Button logoutButton=findViewById(R.id.btn_out);
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(LibraryTourActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });


        Button btnFloor1 = findViewById(R.id.btn_floor_1);
        btnFloor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapImageView.setImageResource(R.drawable.library_map_1f);
            }
        });

        Button btnFloor2 = findViewById(R.id.btn_floor_2);
        btnFloor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapImageView.setImageResource(R.drawable.library_map_2f);
            }
        });

        Button btnFloor3 = findViewById(R.id.btn_floor_3);
        btnFloor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapImageView.setImageResource(R.drawable.library_map_3f);
            }
        });

        Button btnFloor4 = findViewById(R.id.btn_floor_4);
        btnFloor4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapImageView.setImageResource(R.drawable.library_map_4f);
            }
        });

        Button btnFloorB1 = findViewById(R.id.btn_floor_B1);
        btnFloorB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapImageView.setImageResource(R.drawable.library_map_b1);
            }
        });

        Button btnFloorB2 = findViewById(R.id.btn_floor_B2);
        btnFloorB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapImageView.setImageResource(R.drawable.library_map_b2);
            }
        });
    }
}

