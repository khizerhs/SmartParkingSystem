/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.smartparking.ejb.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Khizer
 */
@Embeddable
public class SensorHistoryPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_sensor")
    private int idSensor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    public SensorHistoryPK() {
    }

    public SensorHistoryPK(int idSensor, Date time) {
        this.idSensor = idSensor;
        this.time = time;
    }

    public int getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(int idSensor) {
        this.idSensor = idSensor;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idSensor;
        hash += (time != null ? time.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SensorHistoryPK)) {
            return false;
        }
        SensorHistoryPK other = (SensorHistoryPK) object;
        if (this.idSensor != other.idSensor) {
            return false;
        }
        if ((this.time == null && other.time != null) || (this.time != null && !this.time.equals(other.time))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.sjsu.smartparking.ejb.entities.SensorHistoryPK[ idSensor=" + idSensor + ", time=" + time + " ]";
    }
    
}
