package com.example.sportapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ViewHolder> {
    private List<WorkoutProgram> programs;
    private ProgramClickListener listener;

    public ProgramAdapter(List<WorkoutProgram> programs, ProgramClickListener listener) {
        this.programs = programs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_program, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WorkoutProgram program = programs.get(position);
        holder.title.setText(program.getTitle());
        holder.duration.setText(program.getDurationWeeks() + " недель");
        holder.difficulty.setText(program.getDifficulty());
        holder.itemView.setOnClickListener(v -> listener.onProgramClick(program));
    }

    @Override
    public int getItemCount() {
        return programs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, duration, difficulty;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_program_title);
            duration = itemView.findViewById(R.id.tv_program_duration);
            difficulty = itemView.findViewById(R.id.tv_program_difficulty);
        }
    }

    public interface ProgramClickListener {
        void onProgramClick(WorkoutProgram program);
    }
}
