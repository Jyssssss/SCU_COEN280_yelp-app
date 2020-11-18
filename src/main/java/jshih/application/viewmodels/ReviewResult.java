package jshih.application.viewmodels;

import java.util.Date;

public class ReviewResult {
    private int stars;
    private Date reviewDate;
    private int funnyVotes;
    private int coolVotes;
    private int usefulVotes;
    private String text;
    private String userName;

    public ReviewResult() {
    }

    public ReviewResult(int stars, Date reviewDate, int funnyVotes, int coolVotes, int usefulVotes, String text,
            String userName) {
        this.stars = stars;
        this.reviewDate = reviewDate;
        this.funnyVotes = funnyVotes;
        this.coolVotes = coolVotes;
        this.usefulVotes = usefulVotes;
        this.text = text;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
