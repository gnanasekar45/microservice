package com.luminn.firebase.entity;

import com.google.firebase.database.DatabaseReference;

public class DriverSearchs extends Drivers {
    public double distance;
    public String key;


    public DriverSearchs(){
        super();
    }
    public DriverSearchs(String k, String nam, double lat, double lon, int st){
        super(nam, lat, lon, st);
        key = k;
    }

    public DriverSearchs(String k, String nam, double lat, double lon,  String catType){
        super(nam, lat, lon, catType);

    }
    public DriverSearchs(boolean online, String nam, String catType){
        super(online, nam, catType);
    }

    public String getDBRefString(){
        Degree dgrLat = new Degree(Degree.DegreeType.Latitude, latitude);
        Degree dgrLon = new Degree(Degree.DegreeType.Longitude, longitude);
        return dgrLat.getDBRefString()+dgrLon.getDBRefString();
    }
    public DatabaseReference getDBRef(DatabaseReference parent){
        Degree dgrLat = new Degree(Degree.DegreeType.Latitude, latitude);
        Degree dgrLon = new Degree(Degree.DegreeType.Longitude, longitude);
        return dgrLon.getDBRef(dgrLat.getDBRef(parent));
    }
    public String getNamedDBrefString(){
        String res = "";
        for(int i=0; i<name.length();i++){
            String name_char = ""+name.charAt(i);
            res += "/" + name_char;
        }
        return res;
    }
    public DatabaseReference getNamedDBRef(DatabaseReference parent){
        return getNamedDBRef(parent, name);
    }
    public static DatabaseReference getNamedDBRef(DatabaseReference parent, String name){
        DatabaseReference dbref = parent;
        for(int i=0; i<name.length();i++){
            String name_char = ""+name.charAt(i);
            dbref = dbref.child(name_char);
        }
        return dbref;
    }
}
