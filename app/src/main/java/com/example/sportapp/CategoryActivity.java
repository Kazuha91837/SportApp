package com.example.sportapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CategoryActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private String exerciseName;
    private ProgressBar progressBar;
    private CheckBox checkBox;
    private TextView tvReps, tvTimer;
    private Button btnTimerControl, btnAddRep;
    private boolean isRunning = false;
    private CountDownTimer countDownTimer;
    private int repsCount = 0;
    private long timeLeftInMillis = 60000;
    private static final long DEFAULT_TIME = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        prefs = getSharedPreferences("FitnessPrefs", MODE_PRIVATE);
        exerciseName = getIntent().getStringExtra("exerciseName");
        if (exerciseName == null) {
            finish();
            return;
        }

        initializeViews();
        setupExerciseUI();
    }

    private void initializeViews() {
        progressBar = findViewById(R.id.progressBar);
        checkBox = findViewById(R.id.checkBox);
        tvReps = findViewById(R.id.tvReps);
        tvTimer = findViewById(R.id.tvTimer);
        btnTimerControl = findViewById(R.id.btnTimerControl);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupExerciseUI() {
        TextView tvExerciseName = findViewById(R.id.tvExerciseName);
        tvExerciseName.setText(exerciseName);

        if (isTimedExercise(exerciseName)) {
            setupTimedExercise();
        } else {
            setupRepsExercise();
        }
    }

    private void setupRepsExercise() {
        tvReps.setVisibility(View.VISIBLE);
        tvReps.setText("Повторений: 0");
        progressBar.setVisibility(View.VISIBLE);
        checkBox.setVisibility(View.VISIBLE);
        tvTimer.setVisibility(View.GONE);
        btnTimerControl.setVisibility(View.GONE);

        repsCount = prefs.getInt(exerciseName + "_reps", 0);
        updateRepsUI();

        btnAddRep = new Button(this);
        btnAddRep.setText("+1 повторение");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 16;
        btnAddRep.setLayoutParams(params);
        btnAddRep.setOnClickListener(v -> {
            repsCount++;
            updateRepsUI();
            saveProgress();
        });

        LinearLayout layout = (LinearLayout) findViewById(R.id.progressBar).getParent();
        int progressBarIndex = layout.indexOfChild(findViewById(R.id.progressBar));
        layout.addView(btnAddRep, progressBarIndex + 1);
    }

    private void updateRepsUI() {
        tvReps.setText("Повторений: " + repsCount);
        progressBar.setProgress(Math.min(repsCount, 100));
        checkBox.setChecked(repsCount >= 10);
    }

    private boolean isTimedExercise(String exerciseName) {
        return exerciseName.equals("Зарядка") ||
                exerciseName.equals("Планка") ||
                exerciseName.equals("Медитация");
    }

    private void setupTimedExercise() {
        progressBar.setVisibility(View.VISIBLE);
        checkBox.setVisibility(View.GONE);
        tvReps.setVisibility(View.GONE);
        tvTimer.setVisibility(View.VISIBLE);
        btnTimerControl.setVisibility(View.VISIBLE);

        timeLeftInMillis = prefs.getLong(exerciseName + "_time", DEFAULT_TIME);
        updateTimerUI();

        btnTimerControl.setOnClickListener(v -> {
            if (isRunning) {
                stopTimer();
                btnTimerControl.setText("Старт");
            } else {
                startTimer();
                btnTimerControl.setText("Стоп");
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerUI();
            }

            @Override
            public void onFinish() {
                isRunning = false;
                timeLeftInMillis = 0;
                updateTimerUI();
                btnTimerControl.setText("Старт");
                progressBar.setProgress(100);
                showCompletionDialog();
            }
        }.start();

        isRunning = true;
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isRunning = false;
        saveProgress();
    }

    private void updateTimerUI() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        tvTimer.setText(timeFormatted);

        int progress = (int) ((DEFAULT_TIME - timeLeftInMillis) * 100 / DEFAULT_TIME);
        progressBar.setProgress(progress);
    }

    private void showCompletionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Упражнение завершено")
                .setMessage("Вы успешно выполнили упражнение!")
                .setPositiveButton("OK", null)
                .show();
    }

    private void saveProgress() {
        SharedPreferences.Editor editor = prefs.edit();
        if (isTimedExercise(exerciseName)) {
            editor.putLong(exerciseName + "_time", timeLeftInMillis);
        } else {
            editor.putInt(exerciseName + "_reps", repsCount);
        }
        editor.apply();
    }

    public void onResetButtonClick(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Сброс прогресса")
                .setMessage("Вы уверены, что хотите сбросить прогресс?")
                .setPositiveButton("Да", (dialog, which) -> {
                    if (isTimedExercise(exerciseName)) {
                        timeLeftInMillis = DEFAULT_TIME;
                        updateTimerUI();
                    } else {
                        repsCount = 0;
                        updateRepsUI();
                    }
                    saveProgress();
                })
                .setNegativeButton("Нет", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}