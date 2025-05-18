package com.example.sportapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class ExercisesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        setupExerciseButtons();
    }

    public void onBackButtonClick(View view) {
        finish();
    }

    private void setupExerciseButtons() {
        int[] buttonIds = {R.id.btnWarmUp, R.id.btnPushups, R.id.btnSquats,
                R.id.btnPullup, R.id.btnPlank, R.id.btnMeditation};

        String[] exercises = {"Зарядка", "Отжимание", "Приседание",
                "Турник", "Планка", "Медитация"};

        for (int i = 0; i < buttonIds.length; i++) {
            int finalI = i;
            findViewById(buttonIds[i]).setOnClickListener(v ->
                    openExercise(exercises[finalI]));
        }
    }

    private void openExercise(String name) {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("exerciseName", name);
        startActivity(intent);
    }
}