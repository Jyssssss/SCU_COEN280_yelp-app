package jshih.model.models;

public class Category {
    private String cid;
    private String name;
    private boolean isMain;

    public Category() {
    }

    public Category(String cid, String name, boolean isMain) {
        this.cid = cid;
        this.name = name;
        this.isMain = isMain;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsMain() {
        return isMain;
    }

    public void setIsMain(boolean isMain) {
        this.isMain = isMain;
    }
}