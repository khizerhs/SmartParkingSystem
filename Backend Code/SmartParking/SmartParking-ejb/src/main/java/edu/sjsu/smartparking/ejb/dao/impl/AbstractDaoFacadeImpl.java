/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.smartparking.ejb.dao.impl;

import edu.sjsu.smartparking.ejb.dao.interfaces.DaoFacade;
import edu.sjsu.smartparking.ejb.dao.interfaces.ObjectId;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.apache.log4j.Logger;



/**
 *
 * @author Khizer Hasan khizerhasans@gmail.com
 * @param <T>
 */
public abstract class AbstractDaoFacadeImpl<T> implements Serializable, DaoFacade<T> {

    private Class<T> entityClass;
    protected final Logger logger = Logger.getLogger("MACPU");
    protected Query q;
    protected String sql;
    //Contexto Desarrollo
    //@PersistenceContext(unitName = "CDEPU2")
    //Contexto producción
    @PersistenceContext(unitName = "SmartParkingPU")
    protected EntityManager em;
    protected BigInteger totalCounter;
    protected BigInteger filteredCounter;
    private Integer from;
    private Integer max;
    private Integer last;
    protected ColumnOrder order;
    protected List<FilterParam> params;
    private Boolean lowerLimit;
    private Boolean upperLimit;

    public AbstractDaoFacadeImpl() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
        

    @Override
    public void create(T entity) {
        try {
            em.persist(entity);
        } catch (Exception ex) {
            logger.error("Ocurrió un error al persistir el registro ", ex);
            throw new RuntimeException("Error al persistir " + entity.getClass() + ": " + ex.getMessage(), ex);
        }
    }

    @Override
    public void update(T entity) {
        try {
            em.merge(entity);
            em.flush();
        } catch (Exception ex) {
            logger.error("Ocurrió un error al actualizar el registro ", ex);
            throw new RuntimeException("Error al actualizar " + entity.getClass() + ": " + ex.getMessage(), ex);
        }
    }

    @Override
    public void remove(Object id) {
        try {
            T entity = em.find(entityClass, id );
            em.remove(entity);
        } catch (Exception ex) {
            logger.error("Ocurrió un error al remover el registro ", ex);
            throw new RuntimeException("Error al eliminar " + entityClass + ": " + ex.getMessage(), ex);
        }
    }

    @Override
    public T find(Object id) {
        T entity = null;
        try {
            entity = em.find(entityClass, id);
        } catch (Exception ex) {
            logger.error("Ocurrió un error al encontrar el registro con id " + id, ex);
            throw new RuntimeException("Ocurriò un error al obtener el registro con id: " + id);
        }
        return entity;
    }
    
    @Override
    public List<T> getFilteredWithRange(){
        List<T> res = null;
        createQuery();
        q.setFirstResult(from);
        q.setMaxResults(max);
        res = q.getResultList();
        return res;
    }
    
    public List getFiltered() {
        List<T> res = new ArrayList<T>();
        createQuery();
        res = q.getResultList();
        return res;
    }

    @Override
    public T getById(Object id) {
        return em.find(entityClass, id);
    }

    @Override
    public List<T> getAll() {        
        sql = "SELECT o FROM " + entityClass.getSimpleName() + " o";
        return em.createQuery(sql).getResultList();
    }

    @Override
    public List<T> getAllActivos() {
        sql = "SELECT o FROM " + entityClass.getSimpleName() + " o where o.activo = TRUE ";
        return em.createQuery(sql).getResultList();
    }

    /**
     * @return the entityClass
     */
    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

    /**
     * @param entityClass the entityClass to set
     */
    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * @param order
     * @param params
     * @return the totalCounter
     */
    @Override
    public BigInteger getTotalCounter() {
        try {
            if (totalCounter == null) {
                sql = "select count(p) from " + entityClass.getSimpleName() + " p where 1 = 1";
                if (params != null) {
                    for (int i = 0; i < params.size(); i++) {
                        FilterParam param = params.get(i);
                        if (param.getValue() != null && !param.getValue().equals("")) {
                            if (param.getTipoDato().equals(String.class)) {
                                sql += " and UPPER(p." + param.getKey() + ") like ?" + (i + 1);
                            } else if (param.getTipoDato().equals(ObjectId.class)) {
                                sql += " and p." + param.getKey() + " = ?" + (i + 1);
                            } else if (param.getTipoDato().equals(Date.class)) {
                                //fecha desde
                                sql += " and p." + param.getKey() + "  >= ?" + (i + 1);
                                i++;
                                sql += " and p." + param.getKey() + "  <= ?" + (i + 1);
                            } else if (param.getTipoDato().equals(List.class)) {
                                sql += " and p." + param.getKey() + " IN ?" + (i + 1);
                            } else {
                                sql += " and p." + param.getKey() + " = ?" + (i + 1);
                            }
                        }

                    }
                }

                q = em.createQuery(sql);
                if (params != null) {
                    for (int i = 0; i < params.size(); i++) {
                        FilterParam param = params.get(i);
                        if (param.getValue() != null && !param.getValue().equals("")) {
                            if (param.getTipoDato().equals(Integer.class)) {
                                q.setParameter(i + 1, Integer.parseInt(param.getValue().toString()));
                            } else if (param.getTipoDato().equals(Long.class)) {
                                q.setParameter(i + 1, Long.parseLong(param.getValue().toString()));
                            } else if (param.getTipoDato().equals(String.class)) {
                                q.setParameter(i + 1, "%" + param.getValue().toString().toUpperCase() + "%");
                            } else if (param.getTipoDato().equals(Boolean.class)) {
                                q.setParameter(i + 1, Boolean.valueOf(param.getValue().toString()));
                            } else if (param.getTipoDato().equals(ObjectId.class)) {
                                q.setParameter(i + 1, ((ObjectId) param.getValue()).getId());
                            } else if (param.getTipoDato().equals(List.class)) {
                                q.setParameter(i + 1, (List) param.getValue());
                            } else if (param.getTipoDato().equals(Date.class)) {
                                //fecha desde
                                q.setParameter(i + 1, new Timestamp(((Date) param.getValue()).getTime()), TemporalType.TIMESTAMP);
                                //Obtener el siguiente valor para la fecha hasta  
                                Date fechaHasta = (Date) params.get(++i).getValue();
                                q.setParameter(i + 1, new Timestamp(fechaHasta.getTime()), TemporalType.TIMESTAMP);
                            } else {
                                q.setParameter(i + 1, param.getValue());
                            }

                        }
                    }
                }
                Long x = new Long("0");
                x = (Long) q.getSingleResult();
                totalCounter = new BigInteger(x.toString());
            }
        } catch (NoResultException ex) {
            logger.info("No existen entidades para los parametros enviados");
        } catch (Exception ex) {
            logger.error("Ocurrió un error al intentar obtener el total.", ex);
        }

        return totalCounter == null ? BigInteger.ZERO : totalCounter;
    }

    /**
     * @param totalCounter the totalCounter to set
     */
    @Override
    public void setTotalCounter(BigInteger totalCounter) {
        this.totalCounter = totalCounter;
    }
    
    public BigInteger getFilteredCounter() {
        if (filteredCounter != null) {
            int filtrado = ((from == null ? 0 : from.intValue()) + max.intValue());
            if (filtrado < getTotalCounter().intValue()) {
                filteredCounter = new BigInteger("" + filtrado);
            } else {
                filteredCounter = totalCounter;
            }
        } else {
            filteredCounter = BigInteger.valueOf(max.longValue());
        }
        return filteredCounter == null ? BigInteger.ZERO : filteredCounter;
    }
    
    protected void createQuery() {
        try {
            sql = "select p from " + entityClass.getSimpleName() + " p where 1 = 1";
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    FilterParam param = params.get(i);
                    if (param.getValue() != null && !param.getValue().equals("")) {
                        if (param.getTipoDato().equals(String.class)) {
                            sql += " and UPPER(p." + param.getKey() + ") like ?" + (i + 1);
                        } else if (param.getTipoDato().equals(ObjectId.class)) {
                            sql += " and p." + param.getKey() + " = ?" + (i + 1);
                        } else if (param.getTipoDato().equals(Date.class)) {
                            //fecha desde
                            sql += " and p." + param.getKey() + "  >= ?" + (i + 1);
                            i++;
                            sql += " and p." + param.getKey() + "  <= ?" + (i + 1);
                        } else if (param.getTipoDato().equals(List.class)) {
                            sql += " and p." + param.getKey() + " IN ?" + (i + 1);
                        } else {
                            sql += " and p." + param.getKey() + " = ?" + (i + 1);
                        }
                    }

                }
            }
            if (order != null) {
                if (order.isAsc()) {
                    sql += " order by p." + order.getColumna() + " asc";
                } else {
                    sql += " order by p." + order.getColumna() + " desc";
                }
            }
            q = em.createQuery(sql);
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    FilterParam param = params.get(i);
                    if (param.getValue() != null && !param.getValue().equals("")) {
                        if (param.getTipoDato().equals(Integer.class)) {
                            q.setParameter(i + 1, Integer.parseInt(param.getValue().toString()));
                        } else if (param.getTipoDato().equals(Long.class)) {
                            q.setParameter(i + 1, Long.parseLong(param.getValue().toString()));
                        } else if (param.getTipoDato().equals(String.class)) {
                            q.setParameter(i + 1, "%" + param.getValue().toString().toUpperCase() + "%");
                        } else if (param.getTipoDato().equals(Boolean.class)) {
                            q.setParameter(i + 1, Boolean.valueOf(param.getValue().toString()));
                        } else if (param.getTipoDato().equals(ObjectId.class)) {
                            q.setParameter(i + 1, ((ObjectId) param.getValue()).getId());
                        } else if (param.getTipoDato().equals(List.class)) {
                            q.setParameter(i + 1, (List) param.getValue());
                        } else if (param.getTipoDato().equals(Date.class)) {
                            //fecha desde
                            q.setParameter(i + 1, new Timestamp(((Date) param.getValue()).getTime()), TemporalType.TIMESTAMP);
                            //Obtener el siguiente valor para la fecha hasta  
                            Date fechaHasta = (Date) params.get(++i).getValue();
                            q.setParameter(i + 1, new Timestamp(fechaHasta.getTime()), TemporalType.TIMESTAMP);
                        } else {
                            q.setParameter(i + 1, param.getValue());
                        }

                    }
                }
            }            
        } catch (NoResultException ex) {
            logger.info("No existen entidades para los parametros enviados");
        } catch (Exception ex) {
            logger.error("Ocurrió un error al intentar obtener la lista de usuarios.", ex);
        }
    }

    /**
     * @return the from
     */
    @Override
    public Integer getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(Integer from) {
        this.from = from;
    }

    /**
     * @return the max
     */
    public Integer getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    @Override
    public void setMax(Integer max) {
        this.max = max;
    }

    /**
     * @return the last
     */
    public Integer getLast() {
        return last;
    }

    /**
     * @param last the last to set
     */
    public void setLast(Integer last) {
        this.last = last;
    }

    /**
     * @return the order
     */
    public ColumnOrder getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(ColumnOrder order) {
        this.order = order;
    }

    /**
     * @return the params
     */
    public List<FilterParam> getParams() {
        return params;
    }

    /**
     * @param params the params to set
     */
    @Override
    public void setParams(List<FilterParam> params) {
        this.params = params;
    }

    /**
     * @return the lowerLimit
     */
    @Override
    public Boolean getLowerLimit() {
        lowerLimit = (getFilteredCounter().intValue() <= max) ? true : false;
        return lowerLimit;
    }

    /**
     * @param lowerLimit the lowerLimit to set
     */
    public void setLowerLimit(Boolean lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    /**
     * @return the upperLimit
     */
    public Boolean getUpperLimit() {
        upperLimit = (getFilteredCounter() == getTotalCounter()) ? true : false;
        return upperLimit;
    }

    /**
     * @param upperLimit the upperLimit to set
     */
    public void setUpperLimit(Boolean upperLimit) {
        this.upperLimit = upperLimit;
    }

}
