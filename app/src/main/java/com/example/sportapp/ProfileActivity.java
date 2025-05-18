package com.example.sportapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private EditText etName, etAge, etWeight, etHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        prefs = getSharedPreferences("FitnessPrefs", MODE_PRIVATE);
        initializeViews();
        loadProfileData();
    }

    private void initializeViews() {
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);

        findViewById(R.id.btnSave).setOnClickListener(v -> saveProfileData());
    }

    public void onBackButtonClick(View view) {
        finish();
    }

    private void loadProfileData() {
        etName.setText(prefs.getString("name", ""));
        etAge.setText(String.valueOf(prefs.getInt("age", 0)));
        etWeight.setText(String.valueOf(prefs.getFloat("weight", 0)));
        etHeight.setText(String.valueOf(prefs.getFloat("height", 0)));
    }

    private void saveProfileData() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("name", etName.getText().toString());

        try {
            editor.putInt("age", Integer.parseInt(etAge.getText().toString()));
            editor.putFloat("weight", Float.parseFloat(etWeight.getText().toString()));
            editor.putFloat("height", Float.parseFloat(etHeight.getText().toString()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        editor.apply();
        Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show();
    }
}