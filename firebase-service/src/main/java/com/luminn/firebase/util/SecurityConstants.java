package com.luminn.firebase.util;

public class SecurityConstants {

    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    public static final String SIGN_UP_URL = "/userLogin/app/user/v1/mobile/login";
    public static final String SIGN_UP_USER = "/userLogin/app/user/v1/mobile/user/login";
    public static final String SIGN_UP_DRIVER ="/userLogin/app/user/v1/mobile/driver/login";
    public static final String SIGN_IN = "/userLogin/app/user/v1/mobile/full/registration";
    public static final String SIGN_EMAIL = "/user/v1/get/email/";

    public static final String SEARCH = "/search/v1/geo/search";


    public static final String USERTABLE = "_ah/admin/datastore?kind=USER";
    public static final String FORGOT ="/app/user/v1/forgotpassword/retrieve/**";
    public static final String SMS_NEW ="/app/phoneNumber/v1/twillo/new/sms";
    public static final String SMS_PASSWORD ="/app/user/twillo/v1/password/token";
    public static final String SMS_PASSWORD2 ="/app/phoneNumber/v1/twillo/forgotPassword/sms";
    public static final String DETAILS ="/app/taxi/v1/detailview/**";
    public static final String BYDETAILS ="/app/review/v1/ByTaxiDetailId/**";
    public static final String CONFIG ="/app/config/create";
    public static final String CONFIG_ALL ="/app/config/getAll";
    public static final String CONTACT ="/app/contactus/mail/create";



}
