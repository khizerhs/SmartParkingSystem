/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.smartparking.web.api.impl;

import edu.sjsu.smartparking.ejb.dao.interfaces.LogDaoFacade;
import edu.sjsu.smartparking.ejb.dao.interfaces.SensorDaoFacade;
import edu.sjsu.smartparking.ejb.dto.ResponseWrapper;
import edu.sjsu.smartparking.ejb.entities.Log;
import edu.sjsu.smartparking.ejb.entities.Sensor;
import edu.sjsu.smartparking.ejb.enums.ParkingStatus;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

/**
 * REST Web Service
 *
 * @author Khizer
 */
@Path("/sensor")
@Stateless
public class SensorService {

    @Context
    private UriInfo context;
    
    @EJB
    SensorDaoFacade sensorDaoFacade;
    
//    @EJB
//    LogDaoFacade logDaoFacade;
    

    private final Logger logger = Logger.getLogger("SmartParking");
    
    /**
     * Creates a new instance of SensorService
     */
    
    public SensorService() {
    }
    
    @POST
    @Path("/updateSensorStatus")
    @Consumes("application/json")   
    public Response updateSensorStatus(Sensor sensor ) {
        try {
            //List<Sensor> sensorList = sensorFacade.getSensorListByLocationAndStatus(userLocation, ParkingStatus.FREE.getSimbol());                        
            //code to update in the database                
            //updateSensorFacade.updateSensorStatus(sensor);
            Sensor sensorFromDatabase = sensorDaoFacade.find(sensor.getIdSensor());
            sensorFromDatabase.setStatus(sensor.getStatus());
            sensorDaoFacade.update(sensorFromDatabase);      
//            //Log sensor history
//            Log log = new Log();
//            log.setIdSensor(sensor.getIdSensor());
//            log.setStatus(sensor.getStatus());
//            log.setStatusUpdatetime(new Date());
//            logDaoFacade.update(log);
            return Response.status(Response.Status.OK).build();
        } catch (Exception ex) {
            logger.error("There was an error in the service updateSensorStatus", ex);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @POST
    @Path("/getAllSensors")
    @Produces("application/json")
    public ResponseWrapper getAllSensors() {
        ResponseWrapper response = new ResponseWrapper();
        try {
            List<Sensor> sensorList = sensorDaoFacade.getAll();
            for(Sensor sensor : sensorList){
                sensor.setSensorHistoryList(null);
            }
            response.setSensorList(sensorList);
        } catch (Exception ex) {
            logger.error("There was an error in the service getAllSensors", ex);            
        }
        return response;
    }
    
        

    /**
     * Retrieves representation of an instance of edu.sjsu.smartparking.web.api.impl.SensorService
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of SensorService
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }
}
