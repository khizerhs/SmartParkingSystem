/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.smartparking.ejb.bean.impl;

import edu.sjsu.smartparking.ejb.bean.interfaces.SensorFacade;
import edu.sjsu.smartparking.ejb.dao.interfaces.SensorDaoFacade;
import edu.sjsu.smartparking.ejb.dto.SensorDataWrapper;
import edu.sjsu.smartparking.ejb.dto.UserLocation;
import edu.sjsu.smartparking.ejb.entities.Sensor;
import edu.sjsu.smartparking.ejb.enums.ParkingStatus;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import org.apache.log4j.Logger;

/**
 *
 * @author Khizer
 */
@Stateless
public class SensorFacadeImpl implements SensorFacade {

    private final Logger logger = Logger.getLogger("SmartParking");
    
    @EJB
    SensorDaoFacade sensorDaoFacade;
     
    @Resource
    private SessionContext context;
    
    @Override
    public List<Sensor> getSensorListByLocationAndStatus(UserLocation userLocation, Integer parkingStatus) {
        List<Sensor> sensorList = sensorDaoFacade.getAllSensorsByStatus(parkingStatus);        
        List<Sensor> freeParkingSpotsWithinRange = new ArrayList<Sensor>();
        for(Sensor sensor : sensorList){
            Double distance = calculateDistance(userLocation.getLocation(), sensor.getLocation());
            if(distance < (1609.344*userLocation.getRange()))
                freeParkingSpotsWithinRange.add(sensor);
        }
        
        return freeParkingSpotsWithinRange; 
    }
    
    private Double calculateDistance (String location1, String location2){
       // Double distance = new Double(0.5);
        
       //this method, calculates the distance between two points, in meters.
       
       //Splitting the location string into longitude and latitude
        String loc1[] = location1.split(",");
        String loc2[] = location2.split(",");
        
        Double lat1= Double.parseDouble(loc1[0]);
        Double lat2= Double.parseDouble(loc2[0]);
        
        Double lon1 = Double.parseDouble(loc1[1]);
        Double lon2 = Double.parseDouble(loc2[1]);
        
        final int R = 6371; // Radius of the earth

        Double latDistance;
        latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = 0.0;
        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        return Math.sqrt(distance);
        
    }

    @Override
    public List<Sensor> getSensorListByLocationAndRange(UserLocation userLocation) {
        List<Sensor> sensorList = sensorDaoFacade.getAll();
        List<Sensor> parkingSpotsWithinRange = new ArrayList<Sensor>();
        for(Sensor sensor : sensorList){
            Double distance = calculateDistance(userLocation.getLocation(), sensor.getLocation());
            if(distance < (1609.344*userLocation.getRange()))
                parkingSpotsWithinRange.add(sensor);
        }
        
        return parkingSpotsWithinRange; 
    }
    
    @Override
    public List<SensorDataWrapper> getNumberOfSensorsByTime(String date) {
        List<SensorDataWrapper> numberOfSensorsByTime = new ArrayList<SensorDataWrapper>();
        SensorDataWrapper sensorDataWrapper;
        for(int i = 0; i < 24; i++){
            sensorDataWrapper = new SensorDataWrapper();
            sensorDataWrapper.setNumberSensorsFree(sensorDaoFacade.getNumberSensorsByStatusAndTime(ParkingStatus.FREE.getSimbol(), date+" "+i+":00"));
            sensorDataWrapper.setNumberSensorsOccupied(sensorDaoFacade.getNumberSensorsByStatusAndTime(ParkingStatus.OCUPPIED.getSimbol(), date+" "+i+":00"));
            numberOfSensorsByTime.add(sensorDataWrapper);
        }
                
        return numberOfSensorsByTime; 
    }
}
