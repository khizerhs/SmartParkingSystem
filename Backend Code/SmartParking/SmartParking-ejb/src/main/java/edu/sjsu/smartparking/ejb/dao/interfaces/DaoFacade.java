/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.sjsu.smartparking.ejb.dao.interfaces;

import edu.sjsu.smartparking.ejb.dao.impl.ColumnOrder;
import edu.sjsu.smartparking.ejb.dao.impl.FilterParam;
import java.math.BigInteger;
import java.util.List;



/**
 *
 * @author Khizer Hasan khizerhasans@gmail.com
 */
public interface DaoFacade<T> {
    
    public void create(T entity);
    public void update (T entity);
    public void remove (Object id);
    public Class<T> getEntityClass();
    public List<T> getFiltered();    
    public List<T> getAll();
    public List<T> getAllActivos();
    public T getById(Object id);
    public T find(Object id);
    public List<T> getFilteredWithRange();
    public BigInteger getTotalCounter();
    public void setFrom(Integer from);
    public Integer getFrom();
    public void setMax(Integer max);
    public void setTotalCounter(BigInteger counter);
    public void setOrder(ColumnOrder order);
    public void setParams(List<FilterParam> params);
    public Boolean getLowerLimit();
    public Boolean getUpperLimit();
}
