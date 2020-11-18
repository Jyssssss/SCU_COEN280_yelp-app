package jshih.model.models;

public class BusinessCategory {
    private String bid;
    private String categoryName;

    public BusinessCategory() {
    }

    public BusinessCategory(String bid, String categoryName) {
        this.bid = bid;
        this.categoryName = categoryName;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
