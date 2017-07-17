/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.smartparking.ejb.dao.impl;

import edu.sjsu.smartparking.ejb.dao.interfaces.SensorDaoFacade;
import java.util.ArrayList;
import java.util.List;
import edu.sjsu.smartparking.ejb.entities.Sensor;
import java.text.SimpleDateFormat;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TemporalType;

/**
 *
 * @author Khizer
 */

@Stateless
public class SensorDaoFacadeImpl extends AbstractDaoFacadeImpl<Sensor> implements SensorDaoFacade {

    @Override
    public List<Sensor> getAllSensorsByStatus(Integer parkingStatus) {
        sql = "select s from Sensor s where s.status = :parkingStatus";
        List<Sensor> sensorList = new ArrayList<Sensor>();
        try {
            em.clear();
            q = em.createQuery(sql);
            q.setParameter("parkingStatus", parkingStatus);
            sensorList = q.getResultList();
        } catch (NoResultException ex) {
            logger.info("There aren't sensors with status: " + parkingStatus);
        } catch (Exception e) {
            logger.error("There was an error retrieving sensors with status: " + parkingStatus, e);
        }
        return sensorList;
    }

    @Override
    public Integer getNumberSensorsByStatus(Integer parkingStatus) {
        sql = "select count(s) from Sensor s where s.status = :parkingStatus";
        Long numberSensors = null;
        try {
            em.clear();
            q = em.createQuery(sql);
            q.setParameter("parkingStatus", parkingStatus);
            numberSensors = (Long) q.getSingleResult();
        } catch (NoResultException ex) {
            logger.info("There aren't sensors with status: " + parkingStatus);
        } catch (Exception e) {
            logger.error("There was an error retrieving sensors with status: " + parkingStatus, e);
        }
        return numberSensors.intValue();
    }
    
    @Override
    public Integer getNumberSensorsByStatusAndTime(Integer parkingStatus, String date) {
        sql = "select count(*) from sensor_history s where s.time = ?1 and s.status = ?2";
        Long numberSensors = null;
        try {
            em.clear();
            q = em.createNativeQuery(sql);
            q.setParameter(1, date);
            q.setParameter(2, parkingStatus);
            numberSensors = (Long) q.getSingleResult();
        } catch (NoResultException ex) {
            logger.info("There aren't sensors with status: " + parkingStatus);
        } catch (Exception e) {
            logger.error("There was an error retrieving sensors with status: " + parkingStatus, e);
        }
        return numberSensors.intValue();
    }
        
    
}
