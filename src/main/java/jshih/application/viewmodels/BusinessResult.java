package jshih.application.viewmodels;

public class BusinessResult {
    private String businessUid;
    private String name;
    private String address;
    private String city;
    private String state;
    private float stars;
    private int reviews;
    private int checkins;

    public BusinessResult() {
    }

    public BusinessResult(String businessUid, String name, String address, String city, String state, float stars,
            int reviews, int checkins) {
        this.businessUid = businessUid;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.stars = stars;
        this.reviews = reviews;
        this.checkins = checkins;
    }

    public String getBusinessUid() {
        return businessUid;
    }

    public void setBusinessUid(String businessUid) {
        this.businessUid = businessUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reivews) {
        this.reviews = reivews;
    }

    public int getCheckins() {
        return checkins;
    }

    public void setCheckins(int checkins) {
        this.checkins = checkins;
    }
}
