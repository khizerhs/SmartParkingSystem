/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.smartparking.ejb.dao.impl;

/**
 *
 * @author Khizer Hasan khizerhasans@gmail.com
 */
public class ColumnOrder {

    private String columna;
    private boolean asc;
    
    public ColumnOrder (String columna, boolean asc){
        this.columna = columna;
        this.asc = asc;
    }

    /**
     * @return the columna
     */
    public String getColumna() {
        return columna;
    }

    /**
     * @param columna the columna to set
     */
    public void setColumna(String columna) {
        this.columna = columna;
    }

    /**
     * @return the asc
     */
    public boolean isAsc() {
        return asc;
    }

    /**
     * @param asc the asc to set
     */
    public void setAsc(boolean asc) {
        this.asc = ((this.asc == asc) ? !asc : asc);
    }
    
}
