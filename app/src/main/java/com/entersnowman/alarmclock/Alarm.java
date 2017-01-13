package com.entersnowman.alarmclock;

import java.sql.Time;

/**
 * Created by Valentin on 13.01.2017.
 */

public class Alarm {
    private boolean isOn;
    private int hour;
    private int minute;

    public Alarm(boolean isOn, int hour, int minute) {
        this.isOn = isOn;
        this.hour = hour;
        this.minute = minute;
    }

    public boolean isOn() {

        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
