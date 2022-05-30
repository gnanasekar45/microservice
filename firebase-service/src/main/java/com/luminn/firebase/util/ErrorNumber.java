package com.luminn.firebase.util;

public interface ErrorNumber {

    public int CITY_WRONG_CODE=700;
    public int ROLE_NOT_EXISTING=700;
    public int EMAIL_IS_EXISTING=700;
    public int DEVICE_ID_EXISTING=700;
    public int PHONENUMBER_IS_EXISTING=700;

    public int DRIVER_CREATED=500;
    public int SUPPLIER_CREATED=500;
    public int USER_CREATED=500;
    public int NO_TAG_CODE=700;
    public int CAR_TYPE_NOT_EXISTING=700;
    public int NO_SUPPLIER_ID_WHEN_ADD_TAXI=701;

    public int NUMBER_IS_ALREADY_EXISTING=501;
    public int NUMBER_IS_ALREADY_EXISTING2=501;

    public int THIS_IS_NEW_NUMBER_FAILED=102;
    public int FAILURE=201;
    public int PHONE_NUMBER_IS_EXISTING=203;

    public int EMAIL_NOR_PASSWORD_NOT_EXISTING=302;
    public int EMAIL_NOT_EXISTING = 401;

    public int FORGOTPASSWORD_RETRIVEL=401;
    public int FORGOTPASSWORD_CORRECT_RETRIVEL=500;
    public int NEW_NUMBER_IS_VERIFIED=500;
    public int PHONE_NUMBER_IS_VERIFIED=500;

    public int PASSWORD_HAS_BEEN_CHANGED_SUCCESSFULL=500;
    public int PASSWORD_NOT_HAS_BEEN_CHANGED_SUCCESSFULL=501;
    public int OTP_FAILED=601;
    public int OTP_EXPIRED=602;
    public int WRONG_TOKEN=701;
    public int TOKEN_SUCCESS= 500;
    public int USER_DETAILS=500;
    public int CONTACT_US_MAIL=500;
    public int USER_TRIP_LIST=500;
    public int USER_TRIP_LIST_NO_USER=1500;
    public int USER_LOGOUT_STATUS=500;
    public int USER_LOGOUT_ERROR=2000;
    public int LANGUAGE_UPDATE=500;
    public int LANGUAGE_UPDATE_ERROR=3500;
    public int FCM_STATUS_UPDATE=500;
    public int FCM_STATUS_UPDATE_ERROR=4500;
    public int REVIEW_CREATED=500;
    public int REVIEW_CREATED_ERROR=5500;
    public int RIDE_IS_NOT_EXISTING=5501;
    public int ALL_PRICE=500;
    //mobile verification
    public int PHONENUMBER_UPDATE_PASS=500;
    public int PHONENUMBER_UPDATE_FAIL=6000;
    public int UPDATE_TAXI_DETAIL_SUCCESS=500;
    //vechile UPDATE
    public int DRIVER_ID_NOT_EXISTING=6065;
    public int NO_SUPPLIER_EXISTING = 6060;
    public int NO_TAXIDETAILS_ID=701;
    public int UPDATE_TAXI_INFO=6062;
    public int NO_SUPPLIER_NOT_MATCHING=8063;
    public int PHONENUMBER_IS_NOT_VALID=102;
    public int RIDE_UPDATE=500;
    public int RIDE_EXCEPTION=900;
    public int RIDE_CREATE=500;
    public int RIDE_UPDATE_CHANGE_DESTINATION=500;
    public int RIDE_ID_NOT_EXISTING=888;
    public int DRIVER_IS_NOT_EXISTING= 799;
    public int USER_IS_NOT_EXISTING=801;
    public int RIDE_NOTEXISTS = 805;




}
