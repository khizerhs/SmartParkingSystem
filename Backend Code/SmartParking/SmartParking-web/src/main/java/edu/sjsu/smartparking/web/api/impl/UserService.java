/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.smartparking.web.api.impl;

import edu.sjsu.smartparking.ejb.bean.interfaces.SensorFacade;
import edu.sjsu.smartparking.ejb.dto.ResponseWrapper;
import edu.sjsu.smartparking.ejb.dto.UserLocation;
import edu.sjsu.smartparking.ejb.enums.ParkingStatus;
import edu.sjsu.smartparking.ejb.entities.Sensor;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import org.apache.log4j.Logger;

/**
 * REST Web Service
 *
 * @author Khizer
 */
@Path("/user")
@Stateless
public class UserService {
    
    @Context
    private UriInfo context;
    
    @EJB
    SensorFacade sensorFacade;

    private final Logger logger = Logger.getLogger("SmartParking");
    
    /**
     * Creates a new instance of ApiResource
     */
    public UserService() {
    }
    
    @POST
    @Path("/requestFreeParkingSpots")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseWrapper requestFreeParkingSpots(UserLocation userLocation) {
        ResponseWrapper response = new ResponseWrapper();
        try {
            List<Sensor> sensorList = sensorFacade.getSensorListByLocationAndStatus(userLocation, ParkingStatus.FREE.getSimbol());            
            response.setSensorList(sensorList);            
        } catch (Exception ex) {
            logger.error("There was an error in the service requestFreeParkingSpots", ex);            
        }
        return response;
    }
    
    @POST
    @Path("/getAllSensorsByRange")
    @Produces("application/json")
    public ResponseWrapper getAllSensorsByRange(UserLocation userLocation) {
        ResponseWrapper response = new ResponseWrapper();
        try {
            List<Sensor> sensorList = sensorFacade.getSensorListByLocationAndRange(userLocation);      
            for(Sensor sensor : sensorList){
                sensor.setSensorHistoryList(null);
            }
            response.setSensorList(sensorList);            
        } catch (Exception ex) {
            logger.error("There was an error in the service requestFreeParkingSpots", ex);            
        }
        return response;
    }

    /**
     * Retrieves representation of an instance of
     * edu.sjsu.smartparking.web.api.impl.ApiResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of ApiResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
