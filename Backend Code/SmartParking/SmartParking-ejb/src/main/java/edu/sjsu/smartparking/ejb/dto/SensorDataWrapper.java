/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.smartparking.ejb.dto;

/**
 *
 * @author KhizerHasan
 */
public class SensorDataWrapper {
    
    
    private String id;
    private String status;
    private Integer numberSensorsFree;
    private Integer numberSensorsOccupied;
    private Integer numberSensorsUnavailable;
    private Integer numberSensorsInactive;
    
        

    /**
     * @return the location
     */
    public String getId() {
        return id;
    }
     
     public void setId(String id){
     this.id = id;
     }
    
    
    public String getStatus() {
        return status;
    }
   
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the numberSensorsFree
     */
    public Integer getNumberSensorsFree() {
        return numberSensorsFree;
    }

    /**
     * @param numberSensorsFree the numberSensorsFree to set
     */
    public void setNumberSensorsFree(Integer numberSensorsFree) {
        this.numberSensorsFree = numberSensorsFree;
    }

    /**
     * @return the numberSensorsOccupied
     */
    public Integer getNumberSensorsOccupied() {
        return numberSensorsOccupied;
    }

    /**
     * @param numberSensorsOccupied the numberSensorsOccupied to set
     */
    public void setNumberSensorsOccupied(Integer numberSensorsOccupied) {
        this.numberSensorsOccupied = numberSensorsOccupied;
    }

    /**
     * @return the numberSensorsUnavailable
     */
    public Integer getNumberSensorsUnavailable() {
        return numberSensorsUnavailable;
    }

    /**
     * @param numberSensorsUnavailable the numberSensorsUnavailable to set
     */
    public void setNumberSensorsUnavailable(Integer numberSensorsUnavailable) {
        this.numberSensorsUnavailable = numberSensorsUnavailable;
    }

    /**
     * @return the numberSensorsInactive
     */
    public Integer getNumberSensorsInactive() {
        return numberSensorsInactive;
    }

    /**
     * @param numberSensorsInactive the numberSensorsInactive to set
     */
    public void setNumberSensorsInactive(Integer numberSensorsInactive) {
        this.numberSensorsInactive = numberSensorsInactive;
    }
    
}
