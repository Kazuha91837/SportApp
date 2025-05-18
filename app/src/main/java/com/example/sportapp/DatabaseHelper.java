package com.example.sportapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "workout_programs.db";
    private static final int DATABASE_VERSION = 2; // Увеличена версия для пересоздания таблицы

    private static final String TABLE_PROGRAMS = "programs";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_DIFFICULTY = "difficulty";
    private static final String COLUMN_IS_CUSTOM = "is_custom";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String CREATE_PROGRAMS_TABLE = "CREATE TABLE " + TABLE_PROGRAMS + "("
                    + COLUMN_ID + " TEXT PRIMARY KEY,"
                    + COLUMN_TITLE + " TEXT NOT NULL,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_DURATION + " INTEGER DEFAULT 4,"
                    + COLUMN_DIFFICULTY + " TEXT,"
                    + COLUMN_IS_CUSTOM + " INTEGER"
                    + ")";

            db.execSQL(CREATE_PROGRAMS_TABLE);
            Log.d(TAG, "Таблица программ создана");
        } catch (Exception e) {
            Log.e(TAG, "Ошибка создания таблицы", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRAMS);
            onCreate(db);
            Log.d(TAG, "База данных обновлена");
        } catch (Exception e) {
            Log.e(TAG, "Ошибка обновления БД", e);
        }
    }

    public boolean saveProgram(WorkoutProgram program) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, program.getId());
            values.put(COLUMN_TITLE, program.getTitle());
            values.put(COLUMN_DESCRIPTION, program.getDescription());
            values.put(COLUMN_DURATION, program.getDurationWeeks());
            values.put(COLUMN_DIFFICULTY, program.getDifficulty());
            values.put(COLUMN_IS_CUSTOM, program.isCustom() ? 1 : 0);

            long result = db.insertWithOnConflict(TABLE_PROGRAMS, null, values,
                    SQLiteDatabase.CONFLICT_REPLACE);

            if (result == -1) {
                Log.e(TAG, "Ошибка сохранения программы");
                return false;
            }
            Log.d(TAG, "Программа сохранена, ID: " + result);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Ошибка сохранения программы", e);
            return false;
        } finally {
            db.close();
        }
    }

    // Метод для получения программы по ID
    public WorkoutProgram getProgramById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        WorkoutProgram program = null;

        try (Cursor cursor = db.query(
                TABLE_PROGRAMS,
                null,
                COLUMN_ID + " = ?",
                new String[]{id},
                null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                program = new WorkoutProgram(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION)),
                        new ArrayList<>(), // Дни пока пустые
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIFFICULTY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_CUSTOM)) == 1
                );
                Log.d(TAG, "Программа найдена: " + program.getTitle());
            }
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при получении программы по ID: " + id, e);
        }

        return program;
    }

    // Метод для получения всех программ
    public List<WorkoutProgram> getAllPrograms() {
        List<WorkoutProgram> programs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor cursor = db.query(
                TABLE_PROGRAMS,
                null,
                null,
                null,
                null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    WorkoutProgram program = new WorkoutProgram(
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION)),
                            new ArrayList<>(), // Дни пока пустые
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIFFICULTY)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_CUSTOM)) == 1
                    );
                    programs.add(program);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при получении всех программ", e);
        }

        Log.d(TAG, "Загружено программ: " + programs.size());
        return programs;
    }

    // Метод для удаления программы
    public boolean deleteProgram(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int rowsAffected = db.delete(TABLE_PROGRAMS,
                    COLUMN_ID + " = ?",
                    new String[]{id});
            return rowsAffected > 0;
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при удалении программы", e);
            return false;
        } finally {
            db.close();
        }
    }
}