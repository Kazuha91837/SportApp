package com.example.sportapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private CalendarView calendarView;
    private TextView tvWorkoutInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        prefs = getSharedPreferences("FitnessPrefs", MODE_PRIVATE);
        calendarView = findViewById(R.id.calendarView);
        tvWorkoutInfo = findViewById(R.id.tvWorkoutInfo);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String dateKey = String.format("%d-%d-%d", year, month + 1, dayOfMonth);
            String workout = prefs.getString(dateKey, "Нет тренировок в этот день");
            tvWorkoutInfo.setText(workout);


        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}


