package com.example.pillapplication;
public class History extends Pill {

    String pillName;
    int pillTiming;
    int hourTaken;
    int minTaken;
    public History(String pillName, int pillTiming) {
        super(pillName, pillTiming);
    }

    public History(String pillName, int pillTiming, int hourTaken, int minTaken) {
        super(pillName, pillTiming);
        this.pillName = pillName;
        this.pillTiming = pillTiming;
        this.hourTaken = hourTaken;
        this.minTaken = minTaken;
    }

}
