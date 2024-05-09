package com.scoringapp.powersystem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

    public class ScheduleModel {
        private String timeOn;
        private String dateOn;
        private String timeOff;
        private String dateOff;

        public ScheduleModel() {
            // Default constructor required for Firebase
        }

        public ScheduleModel(String timeOn, String dateOn, String timeOff, String dateOff) {
            this.timeOn = timeOn;
            this.dateOn = dateOn;
            this.timeOff = timeOff;
            this.dateOff = dateOff;
        }

        public String getTimeOn() {
            return timeOn;
        }

        public String getDateOn() {
            return dateOn;
        }

        public String getTimeOff() {
            return timeOff;
        }

        public String getDateOff() {
            return dateOff;
        }
    }


