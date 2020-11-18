package jshih.model.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Review {
    private String rid;
    private String reviewUid;
    private String userUid;
    private String businessUid;
    private int stars;
    private Date reviewDate;
    private int funnyVotes;
    private int coolVotes;
    private int usefulVotes;
    private String text;

    public Review() {
    }

    public Review(String rid, String reviewUid, String userUid, String businessUid, int stars, Date reviewDate,
            int funnyVotes, int coolVotes, int usefulVotes, String text) {
        this.rid = rid;
        this.reviewUid = reviewUid;
        this.userUid = userUid;
        this.businessUid = businessUid;
        this.stars = stars;
        this.reviewDate = reviewDate;
        this.funnyVotes = funnyVotes;
        this.coolVotes = coolVotes;
        this.usefulVotes = usefulVotes;
        this.text = text;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getReviewUid() {
        return reviewUid;
    }

    @JsonProperty("review_id")
    public void setReviewUid(String reviewUid) {
        this.reviewUid = reviewUid;
    }

    public String getUserUid() {
        return userUid;
    }

    @JsonProperty("user_id")
    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getBusinessUid() {
        return businessUid;
    }

    @JsonProperty("business_id")
    public void setBusinessUid(String businessUid) {
        this.businessUid = businessUid;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    @JsonProperty("date")
    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public int getFunnyVotes() {
        return funnyVotes;
    }

    public void setFunnyVotes(int funnyVotes) {
        this.funnyVotes = funnyVotes;
    }

    public int getCoolVotes() {
        return coolVotes;
    }

    public void setCoolVotes(int coolVotes) {
        this.coolVotes = coolVotes;
    }

    public int getUsefulVotes() {
        return usefulVotes;
    }

    public void setUsefulVotes(int usefulVotes) {
        this.usefulVotes = usefulVotes;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
