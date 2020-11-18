package jshih.model.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class YelpUser {
    private String uid;
    private String userUid;
    private String name;

    public YelpUser() {
    }

    public YelpUser(String uid, String userUid, String name) {
        this.uid = uid;
        this.userUid = userUid;
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserUid() {
        return userUid;
    }

    @JsonProperty("user_id")
    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }
}
