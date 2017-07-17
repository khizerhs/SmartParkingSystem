/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.smartparking.ejb.dto;

import edu.sjsu.smartparking.ejb.entities.Sensor;
import edu.sjsu.smartparking.ejb.entities.User;
import java.util.List;

/**
 *
 * @author Khizer
 */
public class ResponseWrapper {
        
    private List<Sensor> sensorList;
    private List<SensorDataWrapper> sensorDataWrapperList;
    private List<User> userList;

    /**
     * @return the sensorList
     */
    public List<Sensor> getSensorList() {
        return sensorList;
    }

    /**
     * @param sensorList the sensorList to set
     */
    public void setSensorList(List<Sensor> sensorList) {
        this.sensorList = sensorList;
    }

    /**
     * @return the sensorDataWrapperList
     */
    public List<SensorDataWrapper> getSensorDataWrapperList() {
        return sensorDataWrapperList;
    }

    /**
     * @param sensorDataWrapperList the sensorDataWrapperList to set
     */
    public void setSensorDataWrapperList(List<SensorDataWrapper> sensorDataWrapperList) {
        this.sensorDataWrapperList = sensorDataWrapperList;
    }

    /**
     * @return the userList
     */
    public List<User> getUserList() {
        return userList;
    }

    /**
     * @param userList the userList to set
     */
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

   
    
          
}
