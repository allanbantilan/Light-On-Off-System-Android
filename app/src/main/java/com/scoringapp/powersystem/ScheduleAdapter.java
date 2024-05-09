package com.scoringapp.powersystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<ScheduleModel> scheduleList;

    public void setScheduleList(List<ScheduleModel> scheduleList) {
        this.scheduleList = scheduleList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sched_list2, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        ScheduleModel schedule = scheduleList.get(position);
        holder.bind(schedule);
    }

    @Override
    public int getItemCount() {
        return scheduleList != null ? scheduleList.size() : 0;
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTimeOn;
        private TextView textViewDateOn;
        private TextView textViewTimeOff;
        private TextView textViewDateOff;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTimeOn = itemView.findViewById(R.id.textViewTimeOn); // Replace with your actual IDs
            textViewDateOn = itemView.findViewById(R.id.textViewDateOn);
            textViewTimeOff = itemView.findViewById(R.id.textViewTimeOff);
            textViewDateOff = itemView.findViewById(R.id.textViewDateOff);
        }

        public void bind(ScheduleModel schedule) {
            // Format and set schedule details in TextViews
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String formattedTimeOn = schedule.getTimeOn(); // Format as needed
            String formattedDateOn = schedule.getDateOn(); // Format as needed
            String formattedTimeOff = schedule.getTimeOff(); // Format as needed
            String formattedDateOff = schedule.getDateOff(); // Format as needed

            textViewTimeOn.setText(formattedTimeOn);
            textViewDateOn.setText(formattedDateOn);
            textViewTimeOff.setText(formattedTimeOff);
            textViewDateOff.setText(formattedDateOff);
        }
    }
}
