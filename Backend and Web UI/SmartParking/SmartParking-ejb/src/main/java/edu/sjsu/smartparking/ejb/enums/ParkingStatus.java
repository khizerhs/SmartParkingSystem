/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.smartparking.ejb.enums;

/**
 *
 * @author Khizer
 */
public enum ParkingStatus {
    
    FREE(1, "Free parking spot"),
    OCUPPIED(2, "Occupied parking spot"),
    UNAVAILABLE(3, "Unavailable parking spot"),
    INACTIVE(4, "Inactive parking spot");
    
    
    private Integer simbol;
    private String label;
    
    private ParkingStatus(Integer simbol, String label){
        this.simbol = simbol;
        this.label = label;
    }

    /**
     * @return the simbol
     */
    public Integer getSimbol() {
        return simbol;
    }

    /**
     * @param simbol the simbol to set
     */
    public void setSimbol(Integer simbol) {
        this.simbol = simbol;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }
    
}
