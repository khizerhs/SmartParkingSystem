package edu.sjsu.smartparking.ejb.entities;

import edu.sjsu.smartparking.ejb.entities.Sensor;
import edu.sjsu.smartparking.ejb.entities.SensorHistoryPK;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-05-07T00:37:12")
@StaticMetamodel(SensorHistory.class)
public class SensorHistory_ { 

    public static volatile SingularAttribute<SensorHistory, SensorHistoryPK> sensorHistoryPK;
    public static volatile SingularAttribute<SensorHistory, Integer> hour;
    public static volatile SingularAttribute<SensorHistory, Sensor> sensor;
    public static volatile SingularAttribute<SensorHistory, Integer> status;

}