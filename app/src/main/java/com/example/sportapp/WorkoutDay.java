package com.example.sportapp;

import java.util.List;

public class WorkoutDay {
    private String dayName;
    private List<ExerciseItem> exercises;

    public WorkoutDay(String dayName, List<ExerciseItem> exercises) {
        this.dayName = dayName;
        this.exercises = exercises;
    }

    public String getDayName() { return dayName; }
    public List<ExerciseItem> getExercises() { return exercises; }

    public void setExercises(List<ExerciseItem> exercises) { this.exercises = exercises; }
}