/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.smartparking.ejb.dao.impl;

import edu.sjsu.smartparking.ejb.dao.interfaces.UserDaoFacade;
import edu.sjsu.smartparking.ejb.entities.Role;
import edu.sjsu.smartparking.ejb.entities.User;
import java.security.MessageDigest;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;

/**
 *
 * @author Khizer
 */
@Stateless
public class UserDaoFacadeImpl extends AbstractDaoFacadeImpl<User> implements UserDaoFacade {

    @Resource
    private SessionContext context;

    @Override
    public User login(String userName, String password) {
        String pass = encrypt(password).trim();        
        sql = "select u from User u where u.loginName = :userName and u.password = :userPassword";
        User user = null;
        try {
            q = em.createQuery(sql);
            q.setParameter("userName", userName);
            q.setParameter("userPassword", pass);
            user = (User) q.getSingleResult();
        } catch (NoResultException ex) {
            logger.info("It doesn't exist an user with userName: " + userName);
        }
        return user;
    }

    @Override
    public User findByLoginName(String loginName) {
        sql = "select u from User u where UPPER(u.loginName) = UPPER(:loginName)";
        User user = null;
        try {
            q = em.createQuery(sql);
            q.setParameter("loginName", loginName);
            user = (User) q.getSingleResult();
        } catch (NoResultException ex) {
            logger.info("The user with login name: " + loginName+" doesn't exist.");
        } catch (Exception e) {
            logger.error("There was an error retrieving user name with login name: " + loginName, e);
        }
        return user;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveUserProfile(User user, List<Role> listRole) throws Exception {
        try {

            //persist user
            user.setPassword(encrypt(user.getPassword()).trim());        
            em.persist(user);
            em.flush();

            //persiste tabla intermedia user_perfil
            sql = "insert into Role_User (id_user, id_role) values (?1, ?2)";
            q = em.createNativeQuery(sql);
            q.setParameter(1, user.getIdUser());
            for (Role role : listRole) {
                q.setParameter(2, role.getIdRole());
                q.executeUpdate();
            }

            logger.info("User was created " + user.getName());
            logger.info("Rol_user was created. ");

        } catch (Exception e) {
            context.setRollbackOnly();
            throw e;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateUserProfile(User user, List<Role> listRole) throws Exception {
        try {

            //update user
            user.setPassword(encrypt(user.getPassword()).trim());       
            em.merge(user);
            em.flush();

            //delete tabla intermedia user_perfil para ingresar los nuevos perfiles del user.
            sql = "delete from Role_User where id_user = ?1";
            q = em.createNativeQuery(sql);
            q.setParameter(1, user.getIdUser());
            q.executeUpdate();

            //persiste tabla intermedia user_perfil con los nuevos perfiles del user.
            sql = "insert into Role_User (id_user, id_role) values (?1, ?2)";
            q = em.createNativeQuery(sql);
            q.setParameter(1, user.getIdUser());
            for (Role role : listRole) {
                q.setParameter(2, role.getIdRole());
                q.executeUpdate();
            }

            logger.info("The user was updated " + user.getName());
            logger.info("Role_user was updated. ");

        } catch (Exception e) {
            context.setRollbackOnly();
            throw e;
        }
    }

    /**
     * Method that encrpyts the string received as parameter
     *
     * @param pass String que va a ser encriptado
     * @return string encriptado
     *
     */
    private String encrypt(String pass) {
        //String sqlaEjecutar = "select MD5('" + pass + "')";
        String respuesta = "";
        try {
            byte[] bytesOfMessage = pass.getBytes("UTF-8");

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(bytesOfMessage);

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            respuesta = sb.toString();

            //respuesta = (String) em.createNativeQuery(sqlaEjecutar).getSingleResult();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return respuesta;
    }

}
