package com.luminn.firebase.dto;

public class TripCounts extends TripCount {
    //implements Comparable
    String dateId;

    public String getDateId() {
        return dateId;
    }

    public void setDateId(String dateId) {
        this.dateId = dateId;
    }

    public TripCounts(TripCount value,String id){
        this.amount = value.getAmount();
        this.count = value.getCount();
        this.endDate = value.getEndDate();
        this.startDate = value.getStartDate();
        this.name = value.getName();
        this.dateId = id;
    }

   /* Override
    public int compareTo(TripCount comparestu) {
        int compareage=((Student)comparestu).getStudentage();
        /* For Ascending order*/
        //return this.studentage-compareage;

        /* For Descending order do like this */
        //return compareage-this.studentage;
    //}*/

}
