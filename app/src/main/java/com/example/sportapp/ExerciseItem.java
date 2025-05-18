package com.example.sportapp;

public class ExerciseItem {
    private String exerciseId;
    private int sets;
    private int reps;
    private int restSec;

    public ExerciseItem(String exerciseId, int sets, int reps, int restSec) {
        this.exerciseId = exerciseId;
        this.sets = sets;
        this.reps = reps;
        this.restSec = restSec;
    }

    public String getExerciseId() { return exerciseId; }
    public int getSets() { return sets; }
    public int getReps() { return reps; }
    public int getRestSec() { return restSec; }
}