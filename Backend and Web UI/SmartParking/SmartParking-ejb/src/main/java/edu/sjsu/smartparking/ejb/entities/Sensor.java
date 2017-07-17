/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.smartparking.ejb.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Khizer
 */
@Entity
@Table(name = "Sensor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sensor.findAll", query = "SELECT s FROM Sensor s"),
    @NamedQuery(name = "Sensor.findByIdSensor", query = "SELECT s FROM Sensor s WHERE s.idSensor = :idSensor"),
    @NamedQuery(name = "Sensor.findByLocation", query = "SELECT s FROM Sensor s WHERE s.location = :location"),
    @NamedQuery(name = "Sensor.findByStatus", query = "SELECT s FROM Sensor s WHERE s.status = :status"),
    @NamedQuery(name = "Sensor.findByZipcode", query = "SELECT s FROM Sensor s WHERE s.zipcode = :zipcode")})
public class Sensor implements Serializable {
            
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_sensor")
    private Integer idSensor;
    @Size(max = 100)
    @Column(name = "location")
    private String location;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "zipcode")
    private int zipcode;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cost")
    private Double cost;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sensor", fetch = FetchType.LAZY)
    private List<SensorHistory> sensorHistoryList;
    
    public Sensor() {
    }

    public Sensor(Integer idSensor) {
        this.idSensor = idSensor;
    }

    public Sensor(Integer idSensor, int status, int zipcode) {
        this.idSensor = idSensor;
        this.status = status;
        this.zipcode = zipcode;
    }

    public Integer getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(Integer idSensor) {
        this.idSensor = idSensor;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
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
        if (!(object instanceof Sensor)) {
            return false;
        }
        Sensor other = (Sensor) object;
        if ((this.idSensor == null && other.idSensor != null) || (this.idSensor != null && !this.idSensor.equals(other.idSensor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.sjsu.cmpe282.smartparking.ejb.entities.Sensor[ idSensor=" + idSensor + " ]";
    }

    /**
     * @return the sensorHistoryList
     */
    public List<SensorHistory> getSensorHistoryList() {
        return sensorHistoryList;
    }

    /**
     * @param sensorHistoryList the sensorHistoryList to set
     */
    public void setSensorHistoryList(List<SensorHistory> sensorHistoryList) {
        this.sensorHistoryList = sensorHistoryList;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
   
  
    
}
