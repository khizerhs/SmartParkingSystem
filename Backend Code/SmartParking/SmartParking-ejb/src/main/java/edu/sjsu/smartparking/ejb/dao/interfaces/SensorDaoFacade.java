/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.smartparking.ejb.dao.interfaces;


import java.util.List;
import edu.sjsu.smartparking.ejb.entities.Sensor;
import javax.ejb.Local;


/**
 *
 * @author Khizer
 */
@Local
public interface SensorDaoFacade extends DaoFacade<Sensor>{
    public List<Sensor> getAllSensorsByStatus(Integer parkingStatus);
    public Integer getNumberSensorsByStatus(Integer parkingStatus);
    public Integer getNumberSensorsByStatusAndTime(Integer parkingStatus, String date);
}
