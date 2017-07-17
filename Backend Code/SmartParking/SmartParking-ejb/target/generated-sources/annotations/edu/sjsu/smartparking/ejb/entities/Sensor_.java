package edu.sjsu.smartparking.ejb.entities;

import edu.sjsu.smartparking.ejb.entities.SensorHistory;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-05-07T00:37:12")
@StaticMetamodel(Sensor.class)
public class Sensor_ { 

    public static volatile SingularAttribute<Sensor, Integer> zipcode;
    public static volatile SingularAttribute<Sensor, Double> cost;
    public static volatile ListAttribute<Sensor, SensorHistory> sensorHistoryList;
    public static volatile SingularAttribute<Sensor, Integer> idSensor;
    public static volatile SingularAttribute<Sensor, String> location;
    public static volatile SingularAttribute<Sensor, Integer> status;

}