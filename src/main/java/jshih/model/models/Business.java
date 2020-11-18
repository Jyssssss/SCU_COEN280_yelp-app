package jshih.model.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Business {
    private String bid;
    private String businessUid;
    private String name;
    private String address;
    private String city;
    private String state;
    private float stars;
    private int reviewCount;
    private boolean isOpen;
    private List<BusinessHours> hours;
    private List<BusinessCategory> categories;
    private List<BusinessAttribute> attributes;

    public Business() {
    }

    public Business(String bid, String businessUid, String name, String address, String city, String state, float stars,
            int reviewCount, boolean isOpen) {
        this.bid = bid;
        this.businessUid = businessUid;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.stars = stars;
        this.reviewCount = reviewCount;
        this.isOpen = isOpen;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBusinessUid() {
        return businessUid;
    }

    @JsonProperty("business_id")
    public void setBusinessUid(String businessUid) {
        this.businessUid = businessUid;
    }

    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    @JsonProperty("full_address")
    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    public float getStars() {
        return stars;
    }

    @JsonProperty("stars")
    public void setStars(float stars) {
        this.stars = stars;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    @JsonProperty("review_count")
    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public boolean getIsOpen() {
        return this.isOpen;
    }

    @JsonProperty("open")
    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public List<BusinessHours> getHours() {
        return hours;
    }

    @JsonIgnore
    public void setHours(List<BusinessHours> hours) {
        this.hours = hours;
    }

    public List<BusinessCategory> getCategories() {
        return categories;
    }

    @JsonIgnore
    public void setCategories(List<BusinessCategory> categories) {
        this.categories = categories;
    }

    public List<BusinessAttribute> getAttributes() {
        return attributes;
    }

    @JsonIgnore
    public void setAttributes(List<BusinessAttribute> attributes) {
        this.attributes = attributes;
    }
}
