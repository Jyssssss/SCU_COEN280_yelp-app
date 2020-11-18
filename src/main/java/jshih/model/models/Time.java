package jshih.model.models;

public class Time {
    private int hour;
    private int minute;

    public Time() {
    }

    public Time(int time) {
        setTime(time);
    }

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
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

    public int getTime() {
        return this.hour * 60 + this.minute;
    }

    public void setTime(int time) {
        this.hour = time / 60;
        this.minute = time % 60;
    }
}