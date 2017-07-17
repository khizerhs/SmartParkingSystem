/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.smartparking.ejb.bean.interfaces;


import edu.sjsu.smartparking.ejb.dto.SensorDataWrapper;
import edu.sjsu.smartparking.ejb.dto.UserLocation;
import edu.sjsu.smartparking.ejb.entities.Sensor;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Khizer
 */
@Local
public interface SensorFacade {
    public List<Sensor> getSensorListByLocationAndStatus(UserLocation userLocation, Integer parkingStatus);
    public List<Sensor> getSensorListByLocationAndRange(UserLocation userLocation);
    public List<SensorDataWrapper> getNumberOfSensorsByTime(String date);
}
