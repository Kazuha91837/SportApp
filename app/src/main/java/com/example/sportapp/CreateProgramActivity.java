package com.example.sportapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateProgramActivity extends AppCompatActivity {
    private static final String TAG = "CreateProgramActivity";
    private DayAdapter adapter;
    private List<WorkoutDay> days = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_program);
        Log.d(TAG, "Activity created");

        try {
            EditText etTitle = findViewById(R.id.et_program_title);
            EditText etDescription = findViewById(R.id.et_program_desc);
            Spinner spinnerDifficulty = findViewById(R.id.spinner_difficulty);
            RecyclerView recyclerDays = findViewById(R.id.recycler_days);

            // Инициализация Spinner
            ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                    R.array.difficulty_levels, android.R.layout.simple_spinner_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDifficulty.setAdapter(spinnerAdapter);

            adapter = new DayAdapter(days);
            recyclerDays.setLayoutManager(new LinearLayoutManager(this));
            recyclerDays.setAdapter(adapter);

            findViewById(R.id.btn_add_day).setOnClickListener(v -> {
                days.add(new WorkoutDay(getString(R.string.day_prefix) + (days.size() + 1), new ArrayList<>()));
                adapter.notifyItemInserted(days.size() - 1);
            });

            findViewById(R.id.btn_save_program).setOnClickListener(v -> {
                if (validateInput(etTitle, etDescription)) {
                    saveProgram(etTitle, etDescription, spinnerDifficulty);
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Ошибка инициализации", e);
            Toast.makeText(this, "Ошибка инициализации", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean validateInput(EditText etTitle, EditText etDescription) {
        if (etTitle.getText().toString().trim().isEmpty()) {
            etTitle.setError("Введите название программы");
            return false;
        }
        if (days.isEmpty()) {
            Toast.makeText(this, "Добавьте хотя бы один день", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveProgram(EditText etTitle, EditText etDescription, Spinner spinnerDifficulty) {
        try {
            WorkoutProgram program = new WorkoutProgram(
                    UUID.randomUUID().toString(),
                    etTitle.getText().toString().trim(),
                    etDescription.getText().toString().trim(),
                    4,
                    new ArrayList<>(days),
                    spinnerDifficulty.getSelectedItem().toString(),
                    true
            );

            DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
            if (dbHelper == null) {
                throw new IllegalStateException("DatabaseHelper не инициализирован");
            }

            if (dbHelper.saveProgram(program)) {
                Toast.makeText(this, "Программа сохранена!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                throw new Exception("Не удалось сохранить программу в БД");
            }
        } catch (Exception e) {
            Log.e(TAG, "Ошибка сохранения", e);
            Toast.makeText(this, "Ошибка сохранения: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}