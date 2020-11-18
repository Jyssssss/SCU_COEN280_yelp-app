package jshih.model.models;

public class CategoryRelation {
    private String mainCid;
    private String subCid;

    public CategoryRelation() {
    }

    public CategoryRelation(String mainCid, String subCid) {
        this.mainCid = mainCid;
        this.subCid = subCid;
    }

    public String getMainCid() {
        return mainCid;
    }

    public void setMainCid(String mainCid) {
        this.mainCid = mainCid;
    }

    public String getSubCid() {
        return subCid;
    }

    public void setSubCid(String subCid) {
        this.subCid = subCid;
    }
}
