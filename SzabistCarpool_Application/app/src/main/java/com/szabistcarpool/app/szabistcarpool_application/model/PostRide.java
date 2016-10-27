package com.szabistcarpool.app.szabistcarpool_application.model;

/**
 * Created by ttwyf on 5/7/2016.
 */
public class PostRide {

    private Long postrideId;
    private String postridetitle;
    private String postridedescription;
    private String sourcelocation;
    private String destinationlocation;
    private String date;
    private String fair;
    private String totalseat;
    private String availableseat;
    private String carname;
    private String carmodel;
    private Long fkUserId;
//	private Long fkCarpoolRequestsID;


    public String getPostridetitle() {
        return postridetitle;
    }

    public void setPostridetitle(String postridetitle) {
        this.postridetitle = postridetitle;
    }

    public Long getPostrideId() {
        return postrideId;
    }

    public void setPostrideId(Long postrideId) {
        this.postrideId = postrideId;
    }

    public String getPostridedescription() {
        return postridedescription;
    }

    public void setPostridedescription(String postridedescription) {
        this.postridedescription = postridedescription;
    }

    public String getSourcelocation() {
        return sourcelocation;
    }

    public void setSourcelocation(String sourcelocation) {
        this.sourcelocation = sourcelocation;
    }

    public String getDestinationlocation() {
        return destinationlocation;
    }

    public void setDestinationlocation(String destinationlocation) {
        this.destinationlocation = destinationlocation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFair() {
        return fair;
    }

    public void setFair(String fair) {
        this.fair = fair;
    }

    public String getTotalseat() {
        return totalseat;
    }

    public void setTotalseat(String totalseat) {
        this.totalseat = totalseat;
    }

    public String getAvailableseat() {
        return availableseat;
    }

    public void setAvailableseat(String availableseat) {
        this.availableseat = availableseat;
    }

    public String getCarname() {
        return carname;
    }

    public void setCarname(String carname) {
        this.carname = carname;
    }

    public String getCarmodel() {
        return carmodel;
    }

    public void setCarmodel(String carmodel) {
        this.carmodel = carmodel;
    }

    public Long getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(Long fkUserId) {
        this.fkUserId = fkUserId;
    }
}
