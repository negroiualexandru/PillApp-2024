package com.example.pillapplication;

public class Appointment {
    String doctorName;
    int year;
    int month;
    int day;
    int hour;
    int min;
    public Appointment (String doctorName, int year, int month, int day, int hour, int min) {
        this.doctorName = doctorName;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.min = min;
    }
}
