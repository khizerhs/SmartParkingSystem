/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.smartparking.ejb.dao.impl;

import java.util.List;

/**
 *
 * @author Khizer Hasan khizerhasans@gmail.com
 */
public class FilterParam<T> {
    
    private String label;
    private String key;
    private String type;
    private Object value;
    private List opciones;
    private T tipoDato;
    private Boolean showParam = true;
    
    
    public FilterParam(){};
    
    public FilterParam(String label, String type, String key, T tipoDato){
        this.label = label;
        this.type = type;     
        this.key = key;
        this.tipoDato = tipoDato;
    }
    
    public FilterParam(String label, String type, String key, List opciones, T tipoDato){
        this.label = label;
        this.type = type;      
        this.key = key;
        this.opciones = opciones;
        this.tipoDato = tipoDato;
    }
        
    
    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * @return the opciones
     */
    public List getOpciones() {
        return opciones;
    }

    /**
     * @param opciones the opciones to set
     */
    public void setOpciones(List opciones) {
        this.opciones = opciones;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the tipoDato
     */
    public T getTipoDato() {
        return tipoDato;
    }

    /**
     * @param tipoDato the tipoDato to set
     */
    public void setTipoDato(T tipoDato) {
        this.tipoDato = tipoDato;
    }

    /**
     * @return the showParam
     */
    public Boolean getShowParam() {
        return showParam;
    }

    /**
     * @param showParam the showParam to set
     */
    public void setShowParam(Boolean showParam) {
        this.showParam = showParam;
    }
   

}
