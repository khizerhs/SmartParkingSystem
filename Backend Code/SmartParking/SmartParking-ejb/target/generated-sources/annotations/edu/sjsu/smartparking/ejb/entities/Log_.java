package edu.sjsu.smartparking.ejb.entities;

import edu.sjsu.smartparking.ejb.entities.Sensor;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-05-07T00:37:12")
@StaticMetamodel(Log.class)
public class Log_ { 

    public static volatile SingularAttribute<Log, Integer> idSensor;
    public static volatile SingularAttribute<Log, Sensor> sensor;
    public static volatile SingularAttribute<Log, Date> statusUpdatetime;
    public static volatile SingularAttribute<Log, Integer> status;

}