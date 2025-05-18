package com.example.sportapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnExercises = findViewById(R.id.btnExercises);
        Button btnProfile = findViewById(R.id.btnProfile);
        Button btnCalendar = findViewById(R.id.btnCalendar);
        Button btnPrograms = findViewById(R.id.btnPrograms); // новая кнопка

        btnExercises.setOnClickListener(v -> {
            startActivity(new Intent(this, ExercisesActivity.class));
        });

        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });

        btnCalendar.setOnClickListener(v -> {
            startActivity(new Intent(this, CalendarActivity.class));
        });

        btnPrograms.setOnClickListener(v -> {
            startActivity(new Intent(this, ProgramsActivity.class)); // переход на экраны программ
        });
    }
}
