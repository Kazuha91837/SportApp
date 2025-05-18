package com.example.sportapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProgramsActivity extends AppCompatActivity {
    private static final String TAG = "ProgramsActivity";
    private RecyclerView recyclerView;
    private ProgramAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs);
        Log.d(TAG, "Activity created");

        try {
            // Инициализация RecyclerView
            recyclerView = findViewById(R.id.recycler_programs);
            if (recyclerView == null) {
                throw new IllegalStateException("RecyclerView not found");
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Загрузка программ
            List<WorkoutProgram> programs = loadPrograms();
            Log.d(TAG, "Loaded programs: " + programs.size());

            // Настройка адаптера
            adapter = new ProgramAdapter(programs, program -> {
                try {
                    Log.d(TAG, "Selected program: " + program.getTitle());
                    Intent intent = new Intent(this, ProgramDetailActivity.class);
                    intent.putExtra("program_id", program.getId());
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, "Error opening program: " + e.getMessage(), e);
                    Toast.makeText(this, "Ошибка открытия программы", Toast.LENGTH_SHORT).show();
                }
            });

            recyclerView.setAdapter(adapter);

            // Обработчик кнопки создания
            findViewById(R.id.btn_create_program).setOnClickListener(v -> {
                try {
                    Log.d(TAG, "Create program button clicked");
                    startActivity(new Intent(this, CreateProgramActivity.class));
                } catch (Exception e) {
                    Log.e(TAG, "Error starting CreateProgramActivity: " + e.getMessage(), e);
                    Toast.makeText(this, "Ошибка создания программы", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Ошибка инициализации", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Activity resumed");
        // Здесь можно добавить обновление данных
    }

    private List<WorkoutProgram> loadPrograms() {
        List<WorkoutProgram> programs = new ArrayList<>();

        try {
            // 1. Попробуем загрузить из БД
            programs = DatabaseHelper.getInstance(this).getAllPrograms();

            // 2. Если БД пуста, используем демо-данные
            if (programs.isEmpty()) {
                Log.w(TAG, "No programs in DB, loading demo data");

                List<ExerciseItem> day1Exercises = Arrays.asList(
                        new ExerciseItem("pushups", 3, 10, 60),
                        new ExerciseItem("squats", 3, 12, 45)
                );

                WorkoutDay day1 = new WorkoutDay("День 1", day1Exercises);
                WorkoutProgram beginnerProgram = new WorkoutProgram(
                        "beginner_fullbody",
                        "Фуллбади для начинающих",
                        "3 тренировки в неделю",
                        4,
                        Arrays.asList(day1),
                        "Начинающий",
                        false
                );

                programs.add(beginnerProgram);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading programs: " + e.getMessage(), e);
        }

        return programs;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Activity destroyed");
    }
}