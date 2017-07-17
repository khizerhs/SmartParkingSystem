/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.smartparking.ejb.dao.interfaces;

import edu.sjsu.smartparking.ejb.entities.Role;
import edu.sjsu.smartparking.ejb.entities.User;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Khizer
 */
@Local
public interface UserDaoFacade extends DaoFacade<User>{
    public User login(String userName, String password);
    public User findByLoginName(String loginName);
    public void saveUserProfile(User user, List<Role> listRole) throws Exception;
    public void updateUserProfile(User user, List<Role> listRole) throws Exception;
    
}
