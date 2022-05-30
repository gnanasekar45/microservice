package com.luminn.firebase.entity;

import com.google.firebase.database.DatabaseReference;

public class Degree {
    public enum DegreeType{
        Latitude,
        Longitude
    }
    private final DegreeType dt;
    private final int sign;
    private final int deg;
    private final int min;
    private final double sec;
    public Degree(DegreeType dt ,int sign, int deg, int min){
        this.dt = dt;
        this.sign = sign;
        this.deg = deg;
        this.min = min;
        this.sec = 0;
    }
    public Degree(DegreeType dt ,double value){
        this.dt = dt;
        if(value<0){
            sign = -1;
            value = -value;
        }else{
            sign = 1;
        }
        deg = (int)value; 
        value = ( value -deg ) * 60;
        min = (int)value;
        value = ( value -min ) * 60;
        sec = value;
    }
    public String getDBRefString(){
        String dir = null;
        switch(dt){
            case Latitude:
                if(sign>0) dir = "N"; else dir = "S";
                break;
            case Longitude:
                if(sign>0) dir = "E"; else dir = "W";
                break;
        }
        return "/"+dir+"/"+deg+"/"+min;
    }
    public DatabaseReference getDBRef(DatabaseReference parent){
        String dir = null;
        switch(dt){
            case Latitude:
                if(sign>0) dir = "N"; else dir = "S";
                break;
            case Longitude:
                if(sign>0) dir = "E"; else dir = "W";
                break;
        }
        return parent.child(dir).child(""+deg).child(""+min);
    }
    public double getValue(){
        return (double)deg + (double)min/60 + sec/3600;
    }
}
