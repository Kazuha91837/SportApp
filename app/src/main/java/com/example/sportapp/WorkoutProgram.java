package com.example.sportapp;

import java.util.List;

public class WorkoutProgram {
    private String id;
    private String title;
    private String description;
    private int durationWeeks;
    private List<WorkoutDay> days;
    private String difficulty;
    private boolean isCustom;

    public WorkoutProgram(String id, String title, String description,
                          int durationWeeks, List<WorkoutDay> days,
                          String difficulty, boolean isCustom) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.durationWeeks = durationWeeks;
        this.days = days;
        this.difficulty = difficulty;
        this.isCustom = isCustom;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getDurationWeeks() { return durationWeeks; }
    public List<WorkoutDay> getDays() { return days; }
    public String getDifficulty() { return difficulty; }
    public boolean isCustom() { return isCustom; }

    public void setDays(List<WorkoutDay> days) { this.days = days; }
}
