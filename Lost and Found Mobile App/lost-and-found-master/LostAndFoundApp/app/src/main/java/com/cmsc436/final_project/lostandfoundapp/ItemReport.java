package com.cmsc436.final_project.lostandfoundapp;

import android.location.Address;
import android.location.Location;

import java.util.Date;
import com.google.android.gms.maps.model.LatLng;

public class ItemReport {

    public enum ReportType{
        LOST, FOUND
    };

    public enum ReportStatus {
        PENDING, RESOLVED
    };

    public enum ReportMethod{
        ADDRESS, LOCATION
    }

    private String mTitle;
    private String mDescription;
    private String mAuthor;
    private Date mDateOccurred;
    private Date mDateAuthored;
    private LatLng mLatLng;
    private String mAddress;
    private ReportType mType;
    private ReportMethod mMethod;
    private ReportStatus mStatus;
    public String reportId;
    public String authorEmailAddress;


    public ItemReport(String mTitle, String mDescription, String mAuthor, Date mDateOccurred, Date mDateAuthored, LatLng mLatLng, String mAddress, boolean isFound
    , String reportId, String authorEmailAddress) {
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mAuthor = mAuthor;
        this.mDateOccurred = mDateOccurred;
        this.mDateAuthored = mDateAuthored;
        this.mLatLng = mLatLng;
        this.mAddress = mAddress;
        this.mType = (isFound) ? (ReportType.FOUND) : (ReportType.LOST);
        this.mMethod = (mAddress == null) ? (ReportMethod.LOCATION) : (ReportMethod.ADDRESS);
        this.mStatus = ReportStatus.PENDING;
        this.reportId = reportId;
        this.authorEmailAddress = authorEmailAddress;
    }

    // Default Constructor
    public ItemReport() {

    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public Date getDateOccurred() {
        return mDateOccurred;
    }

    public void setDateOccurred(Date mDateOccurred) {
        this.mDateOccurred = mDateOccurred;
    }

    public Date getDateAuthored() {
        return mDateAuthored;
    }

    public void setDateAuthored(Date mDateAuthored) {
        this.mDateAuthored = mDateAuthored;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLatLng(LatLng mLatLng) {
        this.mLatLng = mLatLng;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public ReportType getType() {
        return mType;
    }

    public void setType(ReportType mType) {
        this.mType = mType;
    }

    public ReportMethod getMethod() {
        return mMethod;
    }

    public void setMethod(ReportMethod mMethod) {
        this.mMethod = mMethod;
    }

    public ReportStatus getStatus() {
        return mStatus;
    }

    public void setStatus(ReportStatus mStatus) {
        this.mStatus = mStatus;
    }
    public String getID(){
        return this.reportId;
    }

    public String getAuthorEmailAddress() {
        return this.authorEmailAddress;
    }

    // TODO: implement consistent system of tagging
    // (possibly store Tag objects in Firebase with report references)
}
