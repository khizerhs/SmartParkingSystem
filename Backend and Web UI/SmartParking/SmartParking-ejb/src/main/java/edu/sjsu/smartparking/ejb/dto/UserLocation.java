/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.smartparking.ejb.dto;

/**
 *
 * @author Khizer
 */
public class UserLocation {
    
    private String location;
    private Integer range;
    private Integer zipcode;    
    private String date;

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the range
     */
    public Integer getRange() {
        return range;
    }

    /**
     * @param range the range to set
     */
    public void setRange(Integer range) {
        this.range = range;
    }

    /**
     * @return the zipcode
     */
    public Integer getZipcode() {
        return zipcode;
    }

    /**
     * @param zipcode the zipcode to set
     */
    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }
    
}
