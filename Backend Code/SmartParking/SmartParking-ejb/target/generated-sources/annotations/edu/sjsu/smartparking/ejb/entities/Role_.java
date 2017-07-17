package edu.sjsu.smartparking.ejb.entities;

import edu.sjsu.smartparking.ejb.entities.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-05-07T00:37:12")
@StaticMetamodel(Role.class)
public class Role_ { 

    public static volatile ListAttribute<Role, User> userList;
    public static volatile SingularAttribute<Role, Integer> idRole;
    public static volatile SingularAttribute<Role, String> name;
    public static volatile SingularAttribute<Role, String> description;

}