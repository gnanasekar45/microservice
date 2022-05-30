package com.luminn.firebase.util;

public interface STEPS {
    //driver and user accept button
    public String PRECANCEL_BY_USER="pre_cancel_by_user";
    public String PRECANCEL_BY_DRIVER="pre_cancel_by_driver";
    public String BOOKING_CONFIRMED="booking_confirmed";
    //when ride is finisged before review screen
    public String RIDE_FINISH="RIDE_FINISH";
    public String RIDE_STOP="STOP";
    public String RIDE_LOGIN="LOGIN";

    public String PICKUP="PICKUP";
    //it is middle of ride before finish ride
    public String RIDE="RIDE";
    public String CANCEL="CANCEL";
    public String REJECT="REJECT";
    public String REQUEST_CHANGEDESTINATION="request_changeDestination";
    public String RIDE_REQUEST_CHANGEDESTINATION="ride_request_changeDestination";
    public String DRIVER_CANCEL_RIDE="DRIVER_CANCEL_RIDE";
    public String LOGIN="LOGIN";

    //ride start of Driver locationController and updating in Taxi table
    public String START="START";
    //ride stop of Driver locationController and updating in Taxi table
    public String STOP="STOP";

    //one flow
    //normal flow
    //booking_booking_confired (ACCPETED)
    //START AND STOP
    //RIDE_FINISH
    //LONGIN (REVIEW) to go home screem

    //flow two
    //change destination and normal flow
    //booking_booking_confired
    //request_changedestination


    //START AND STOP (IN NVAIGATION)
    //RIDE (CHANGE SETINATION)
    //RIDE_FINISH (UPDATE PRICE)
    //LONGIN (REVIEW) to go home screem

}
