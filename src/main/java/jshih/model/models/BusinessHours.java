package jshih.model.models;

public class BusinessHours {
    private String bid;
    private String day;
    private Time open;
    private Time close;

    public BusinessHours() {
    }

    public BusinessHours(String bid, String day, Time open, Time close) {
        this.bid = bid;
        this.day = day;
        this.open = open;
        this.close = close;
    }

    public BusinessHours(String bid, String day, int openHour, int openMinute, int closeHour, int closeMinute) {
        this.bid = bid;
        this.day = day;
        this.open = new Time(openHour, openMinute);
        this.close = new Time(closeHour, closeMinute);
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Time getOpen() {
        return open;
    }

    public void setOpen(Time open) {
        this.open = open;
    }

    public Time getClose() {
        return close;
    }

    public void setClose(Time close) {
        this.close = close;
    }

    public int getOpenTime() {
        return this.open.getTime();
    }

    public void setOpenTime(int time) {
        this.open = new Time(time);
    }

    public int getCloseTime() {
        return this.close.getTime();
    }

    public void setCloseTime(int time) {
        this.close = new Time(time);
    }
}
