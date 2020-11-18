package jshih.application.viewmodels;

import java.util.List;

import jshih.model.models.Time;

public class BusinessDaysTimes {

    private List<String> days;
    private List<Time> froms;
    private List<Time> tos;

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public List<Time> getFroms() {
        return froms;
    }

    public void setFroms(List<Time> froms) {
        this.froms = froms;
    }

    public List<Time> getTos() {
        return tos;
    }

    public void setTos(List<Time> tos) {
        this.tos = tos;
    }
}
