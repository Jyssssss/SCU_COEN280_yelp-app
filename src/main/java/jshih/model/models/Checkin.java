package jshih.model.models;

public class Checkin {
    private String cid;
    private String businessUid;
    private int day;
    private int hour;
    private int count;

    public Checkin() {
    }

    public Checkin(String cid, String businessUid, int day, int hour, int count) {
        this.cid = cid;
        this.businessUid = businessUid;
        this.day = day;
        this.hour = hour;
        this.count = count;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getBusinessUid() {
        return businessUid;
    }

    public void setBusinessUid(String businessUid) {
        this.businessUid = businessUid;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
