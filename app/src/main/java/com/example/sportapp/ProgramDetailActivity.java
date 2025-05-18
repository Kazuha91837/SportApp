package com.example.sportapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProgramDetailActivity extends AppCompatActivity {
    private static final String TAG = "ProgramDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_detail);
        Log.d(TAG, "Activity created");

        try {
            // Инициализация UI элементов
            TextView tvTitle = findViewById(R.id.tv_title);
            TextView tvDesc = findViewById(R.id.tv_description);
            TextView tvDuration = findViewById(R.id.tv_duration);
            RecyclerView recyclerDays = findViewById(R.id.recycler_days);

            // Проверка инициализации элементов
            if (tvTitle == null || tvDesc == null || tvDuration == null || recyclerDays == null) {
                throw new IllegalStateException("Не все UI элементы инициализированы");
            }

            // Получение данных из Intent
            String programId = getIntent().getStringExtra("program_id");
            if (programId == null) {
                Toast.makeText(this, "Программа не выбрана", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "No program_id in intent");
                finish();
                return;
            }

            // Получаем экземпляр DatabaseHelper
            DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
            if (dbHelper == null) {
                throw new IllegalStateException("DatabaseHelper not initialized");
            }

            // Загрузка программы из БД
            WorkoutProgram program = dbHelper.getProgramById(programId);
            if (program == null) {
                Toast.makeText(this, "Программа не найдена", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Program not found in DB for id: " + programId);
                finish();
                return;
            }

            // Отображение данных
            tvTitle.setText(program.getTitle());
            tvDesc.setText(program.getDescription());
            tvDuration.setText(getString(R.string.weeks_count, program.getDurationWeeks()));

            // Настройка RecyclerView
            recyclerDays.setLayoutManager(new LinearLayoutManager(this));
            recyclerDays.setAdapter(new DayAdapter(program.getDays()));

            Log.d(TAG, "Program displayed: " + program.getTitle());

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Ошибка загрузки программы", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}