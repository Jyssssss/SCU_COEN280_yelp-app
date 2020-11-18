package jshih.model.models;

public class BusinessAttribute {
    private String bid;
    private String name;

    public BusinessAttribute() {
    }

    public BusinessAttribute(String bid, String name) {
        this.bid = bid;
        this.name = name;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
