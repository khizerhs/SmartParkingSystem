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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Khizer
 */
@Entity
@Table(name = "Log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Log.findAll", query = "SELECT l FROM Log l"),
    @NamedQuery(name = "Log.findByIdSensor", query = "SELECT l FROM Log l WHERE l.idSensor = :idSensor"),
    @NamedQuery(name = "Log.findByStatus", query = "SELECT l FROM Log l WHERE l.status = :status"),
    @NamedQuery(name = "Log.findByStatusUpdatetime", query = "SELECT l FROM Log l WHERE l.statusUpdatetime = :statusUpdatetime")})
public class Log implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_sensor")
    private Integer idSensor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "statusUpdate_time")
    @Temporal(TemporalType.TIME)
    private Date statusUpdatetime;
    @JoinColumn(name = "id_sensor", referencedColumnName = "id_sensor", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Sensor sensor;

    public Log() {
    }

    public Log(Integer idSensor) {
        this.idSensor = idSensor;
    }

    public Log(Integer idSensor, int status, Date statusUpdatetime) {
        this.idSensor = idSensor;
        this.status = status;
        this.statusUpdatetime = statusUpdatetime;
    }

    public Integer getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(Integer idSensor) {
        this.idSensor = idSensor;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getStatusUpdatetime() {
        return statusUpdatetime;
    }

    public void setStatusUpdatetime(Date statusUpdatetime) {
        this.statusUpdatetime = statusUpdatetime;
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
        hash += (idSensor != null ? idSensor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Log)) {
            return false;
        }
        Log other = (Log) object;
        if ((this.idSensor == null && other.idSensor != null) || (this.idSensor != null && !this.idSensor.equals(other.idSensor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.sjsu.smartparking.ejb.entities.Log[ idSensor=" + idSensor + " ]";
    }
    
}
