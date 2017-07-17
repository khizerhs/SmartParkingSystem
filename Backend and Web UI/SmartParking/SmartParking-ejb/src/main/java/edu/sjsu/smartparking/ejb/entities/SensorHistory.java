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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Khizer
 */
@Entity
@Table(name = "sensor_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SensorHistory.findAll", query = "SELECT s FROM SensorHistory s"),
    @NamedQuery(name = "SensorHistory.findByIdSensor", query = "SELECT s FROM SensorHistory s WHERE s.sensorHistoryPK.idSensor = :idSensor"),
    @NamedQuery(name = "SensorHistory.findByStatus", query = "SELECT s FROM SensorHistory s WHERE s.status = :status"),
    @NamedQuery(name = "SensorHistory.findByHour", query = "SELECT s FROM SensorHistory s WHERE s.hour = :hour"),
    @NamedQuery(name = "SensorHistory.findByTime", query = "SELECT s FROM SensorHistory s WHERE s.sensorHistoryPK.time = :time")})
public class SensorHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SensorHistoryPK sensorHistoryPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hour")
    private int hour;
    @JoinColumn(name = "id_sensor", referencedColumnName = "id_sensor", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Sensor sensor;

    public SensorHistory() {
    }

    public SensorHistory(SensorHistoryPK sensorHistoryPK) {
        this.sensorHistoryPK = sensorHistoryPK;
    }

    public SensorHistory(SensorHistoryPK sensorHistoryPK, int status, int hour) {
        this.sensorHistoryPK = sensorHistoryPK;
        this.status = status;
        this.hour = hour;
    }

    public SensorHistory(int idSensor, Date time) {
        this.sensorHistoryPK = new SensorHistoryPK(idSensor, time);
    }

    public SensorHistoryPK getSensorHistoryPK() {
        return sensorHistoryPK;
    }

    public void setSensorHistoryPK(SensorHistoryPK sensorHistoryPK) {
        this.sensorHistoryPK = sensorHistoryPK;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sensorHistoryPK != null ? sensorHistoryPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SensorHistory)) {
            return false;
        }
        SensorHistory other = (SensorHistory) object;
        if ((this.sensorHistoryPK == null && other.sensorHistoryPK != null) || (this.sensorHistoryPK != null && !this.sensorHistoryPK.equals(other.sensorHistoryPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.sjsu.smartparking.ejb.entities.SensorHistory[ sensorHistoryPK=" + sensorHistoryPK + " ]";
    }
    
}
