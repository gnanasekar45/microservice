package com.luminn.firebase.dto;

import java.util.ArrayList;
import java.util.List;

public class Location {

    List<Coordinates> list= new ArrayList<Coordinates>();

    public List<Coordinates> getList() {
        return list;
    }

    public void setList(List<Coordinates> list) {
        this.list = list;
    }
}
