package com.example.BookLibraryTemi;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.cardview.widget.CardView;
import androidx.appcompat.app.AppCompatActivity;

import com.robotemi.sdk.Robot;

public class MainActivity extends AppCompatActivity {

    private Robot robot;
    CardView tour;
    CardView search;
    CardView about;
    CardView borrow;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tour = findViewById(R.id.btn_library_tour);
        search = findViewById(R.id.btn_onesearch);
        borrow = findViewById(R.id.btn_borrowbooks);
        about = findViewById(R.id.btn_temi);
        robot = Robot.getInstance();


        //TtsRequest ttsRequest = TtsRequest.create("Welcome to Feng Chia Library", true);
        //robot.speak(ttsRequest);
        //setContentView(R.layout.activity_main);

        tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LibraryTourActivity.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OneSearchActivity.class);
                startActivity(intent);
            }
        });

        borrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutT.class);
                startActivity(intent);
            }
        });
    }
}
