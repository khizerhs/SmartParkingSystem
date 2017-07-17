    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.smartparking.web.api.impl;

import edu.sjsu.smartparking.ejb.bean.interfaces.SensorFacade;
import edu.sjsu.smartparking.ejb.dao.interfaces.RoleDaoFacade;
import edu.sjsu.smartparking.ejb.dao.interfaces.SensorDaoFacade;
import edu.sjsu.smartparking.ejb.dao.interfaces.UserDaoFacade;
import edu.sjsu.smartparking.ejb.dto.ResponseWrapper;
import edu.sjsu.smartparking.ejb.dto.SensorDataWrapper;
import edu.sjsu.smartparking.ejb.dto.UserLocation;
import edu.sjsu.smartparking.ejb.entities.Role;
import edu.sjsu.smartparking.ejb.entities.Sensor;
import edu.sjsu.smartparking.ejb.entities.User;
import edu.sjsu.smartparking.ejb.enums.ParkingStatus;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
@Path("/admin")
@Stateless
public class AdminService {

    @Context
    private UriInfo context;

    @EJB
    UserDaoFacade userDaoFacade;
    
    @EJB
    RoleDaoFacade roleDaoFacade;
    
    @EJB
    SensorDaoFacade sensorDaoFacade;
    
    @EJB
    SensorFacade sensorFacade;
    
    private final Logger logger = Logger.getLogger("SmartParking");
    
    /**
     * Creates a new instance of AdminService
     */
    public AdminService() {
    }
    
    @POST
    @Path("/authenticate")
    @Consumes("application/json")
    public Response authenticate(User user) {
        try{
            User userLoggued = userDaoFacade.login(user.getLoginName(), user.getPassword());
            if(userLoggued != null)
                return Response.status(Response.Status.OK).build();
            else
                return Response.status(Response.Status.BAD_REQUEST).build();
        }catch(Exception ex){
            logger.error("There was an error in the service authenticate", ex);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @POST
    @Path("/getNumberSensorsByStatus")
    @Produces("application/json")
    public SensorDataWrapper getNumberSensorsByStatus() {
        SensorDataWrapper response = new SensorDataWrapper();
        try {
            response.setNumberSensorsFree(sensorDaoFacade.getNumberSensorsByStatus(ParkingStatus.FREE.getSimbol()));
            response.setNumberSensorsOccupied(sensorDaoFacade.getNumberSensorsByStatus(ParkingStatus.OCUPPIED.getSimbol()));
            response.setNumberSensorsUnavailable(sensorDaoFacade.getNumberSensorsByStatus(ParkingStatus.UNAVAILABLE.getSimbol()));
            response.setNumberSensorsInactive(sensorDaoFacade.getNumberSensorsByStatus(ParkingStatus.INACTIVE.getSimbol()));            
        } catch (Exception ex) {
            logger.error("There was an error in the service getAllSensors", ex);            
        }
        return response;
    }    
    
    @POST
    @Path("/getNumberSensorsByTime")
    @Produces("application/json")
    @Consumes("application/json")
    public ResponseWrapper getNumberSensorsByTime(UserLocation userLocation) {
        ResponseWrapper response = new ResponseWrapper();
        try {
            response.setSensorDataWrapperList(sensorFacade.getNumberOfSensorsByTime(userLocation.getDate()));                     
        } catch (Exception ex) {
            logger.error("There was an error in the service getAllSensors", ex);            
        }
        return response;
    }  
    
    @POST
    @Path("/getAllUsers")
    @Produces("application/json")
    public ResponseWrapper getAllUsers() {
        ResponseWrapper response = new ResponseWrapper();
        try {
            List<User> userList = userDaoFacade.getAll();
            for(User user : userList){
                user.setRoleList(null);
            }
            response.setUserList(userList);
        } catch (Exception ex) {
            logger.error("There was an error in the service getAllUsers", ex);            
        }
        return response;   
    }
    
    @POST
    @Path("/addNewUser")
    @Consumes("application/json")
    public Response addNewUser(User user) {
        try {
            List<Role> roles = roleDaoFacade.getAll();
            userDaoFacade.saveUserProfile(user, roles);
            return Response.status(Response.Status.OK).build();
        } catch (Exception ex) {
            logger.error("There was an error in the service addNewUser", ex);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }        
    }
    
    @POST
    @Path("/updateUser")
    @Consumes("application/json")
    public Response updateUser(User user) {
        try {        
            List<Role> roles = roleDaoFacade.getAll();
            userDaoFacade.updateUserProfile(user, roles);    
            return Response.status(Response.Status.OK).build();
        } catch (Exception ex) {
            logger.error("There was an error in the service updateUser", ex);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }  
    }
    
    @POST
    @Path("/deleteUser")
    @Consumes("application/json")
    public Response deleteUser(User user) {
        try {           
            userDaoFacade.remove(user.getIdUser());             
            return Response.status(Response.Status.OK).build();
        } catch (Exception ex) {
            logger.error("There was an error in the service deleteUser", ex);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }  
    }
    
    @POST
    @Path("/addNewSensor")
    @Consumes("application/json")
    public Response addNewSensor(Sensor sensor) {
        try {           
            sensorDaoFacade.create(sensor);             
            return Response.status(Response.Status.OK).build();
        } catch (Exception ex) {
            logger.error("There was an error in the service addNewSensor", ex);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }  
    }
    
    @POST
    @Path("/updateSensor")
    @Consumes("application/json")
    public Response updateSensor(Sensor sensor) {
        try {           
            sensorDaoFacade.update(sensor);             
            return Response.status(Response.Status.OK).build();
        } catch (Exception ex) {
            logger.error("There was an error in the service updateSensor", ex);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }  
    }
    
    @POST
    @Path("/deleteSensor")
    @Consumes("application/json")
    public Response deleteSensor(Sensor sensor) {
        try {           
            sensorDaoFacade.remove(sensor.getIdSensor());             
            return Response.status(Response.Status.OK).build();
        } catch (Exception ex) {
            logger.error("There was an error in the service deleteSensor", ex);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }  
    }

    /**
     * Retrieves representation of an instance of edu.sjsu.smartparking.web.api.impl.AdminService
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of AdminService
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
