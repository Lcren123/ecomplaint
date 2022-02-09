package com.utem.mobile.ecomplaint.model;

public class Solution {

    private Staff staff;
    private Complaint complaint;

    private int solutionID=0;
    private String completeDateTime;
    private String solutionStatus;


    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Complaint getComplaint() {
        return complaint;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public int getSolutionID() {
        return solutionID;
    }

    public void setSolutionID(int solutionID) {
        this.solutionID = solutionID;
    }



}
