package com.project.team.parking.smart.smartparkingadmin;

/**
 * Created by ChenYu Wu on 4/24/2016.
 */
public class JSONObjectHelper {
    private String out;

    JSONObjectHelper() {
        out = "{";
    }









    public void add(String name, String value) {
        out += "\"" + name + "\":\"" + value + "\",";
    }

    public String getResult() {
        //Remove last ,
        String temp = out.substring(0, out.length() - 1);

        return temp + "}";
    }
}
