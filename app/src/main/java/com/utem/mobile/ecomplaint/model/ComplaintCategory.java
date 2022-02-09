package com.utem.mobile.ecomplaint.model;

public class ComplaintCategory {

    private int complaintCategoryID =0;
    private String categoryName;

    public int getComplaintCategoryID() {
        return complaintCategoryID;
    }

    public void setComplaintCategoryID(int complaintCategoryID) {
        this.complaintCategoryID = complaintCategoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
