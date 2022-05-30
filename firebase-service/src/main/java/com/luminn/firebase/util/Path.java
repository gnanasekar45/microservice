package com.luminn.firebase.util;

/**
 * Created by ch on 12/1/2015.
 */


public interface Path {

    public interface Url {
        String WEBSITE_DOMAIN ="TAXIDEALS";
        String INDEX = "/";
        String SEARCH = "/search";
        String SEARCHBYNAME = "/searchByName";
        String SUPPLIER_NAME_BYNAME = "/searchByName";
        String SEARCHVIEW = "/searchView";
        String TAXISVIEW = "/taxisView";
        String SEARCHVIEW2 = "/searchView2";
        String TAXIVIEW = "/taxi/view";
        String DETAILS = "/details";
        String TODAY="/today";
        String DETAILVIEW = "/detailview";
        String TYPE = "/type";
        String TAXIDETAILS = "/taxiDetails";
        String REVIEWS = "/reviews";
        String RIDE = "/ride";
        String DEALCOUPON = "/dealCoupon";
        String rideKey ="/tripKey"; //TODO CHange the UI to rideKey and then change the variable value
        String APP = "/app";
        String API = "/api";
        String LOGIN = "/login";
        String LOGOUT = "/logout";
        String SIGNUP = "/signup";
        String PRICE_LIST = "/priceList";
        String EMAIL = "/email";
        String MAIL = "/mail";
        String lN = "/licenseNumber";
        String ID = "/id";
        String LICENSE = "/license";
        String CHANGED_PASSWORD_BYUSER = "/changedPasswordByUser";
        String CHANGED_PASSWORD = "/changedPassword";
        String FORGOT_PASSWORD = "/forgotpassword";
        String USER = "/user";
        String USER_ACTIVATION = "/user/activation";
        String USERUPDATE = "/userUpdate";
        String USER_DETAIL = "/userDetail";
        String NAME = "/name";
        String ADDRESS = "/address";
        String PHONENUMBER = "/phoneNumber";
        String PROFILE_PIC = "/profilePicture";
        String SUPPLIER = "/supplier";
        String PRICE = "/price";
        String PRICEDETAIL = "/priceDetail";
        String SHOP = "/shop";
        String DRIVERSIGNUP = "/driverLogin";
        String DRIVERWAITING = "/oauth/driverWaiting";
        String DRIVERREGISTRATION = "/driverRegistration";
        String DRIVER = "/driver";
        String COUNTRY = "/country";
        String CITY = "/city";
        String CONFIG = "/config";
        String QUOTE = "/quote";
        String PAYTM = "/paytm";
        String REGION = "/region";
        String TAXI = "/taxi";
        String DELIVERY = "/delivery";
        String DEAL = "/deal";
        String GCM = "/gcm";
        String FCM = "/fcm";
        String message = "/message";
        String REVIEW = "/review";
        String BOOKING = "/booking";
        String DRIVER_BILLING = "/driverBilling";
        String RATING = "/ratingUser";
        String PHONEBOOKING = "/phoneBooking";
        String PHONE_VERIFICATION="/phoneVerification";
        String PHONE_CODE="/phoneCode";
        String REST_CODE="/restCode";
        String TWILLO="/twillo";
        String VEHICLETYPE="/VehicleType";
        String TAG="/Tag";
        String WEB="/web";
        String RADIUS="/radius";
        String ADMIN = "/admin";
        String PAYMENT ="/payment";
        String CANCEL ="/cancel";
        String CARD = "/card";


        String VERSION_V1 = "/v1";
        String VERSION_V2 = "/v2";
        String VERSION_V3 = "/v3";
        String LOCATION = "/location";
        String CONTACTUS = "/contactus";
        String TAXI_CONTACT = "/taxi_contactus";
        String PRIVACY_NOTICE = "/privacy_notice";
        String TERMS = "/terms";
        String HELP_PAGE = "/help_page";
    }

    public interface OperationUrl {

        String USER = "/user";
        String CREATE = "/create";
        String VERIFY = "/verify";
        String LOG = "/log";
        String UPDATE = "/update";
        String UPDATE_SETTING = "/updateSetting";
        String UPDATEDETAILS = "/updateDetail";
        String UPDATEINFO = "/updateInfo";
        String UPDATEPRICE = "/updatePrice";
        String UPDATEIMAGE = "/updateImage";
        String EDIT = "/edit";
        String DRIVER = "/driver";
        String DRIVER_SEARCH = "/driver/search";
        String GET = "/get";
        String GETALL = "/getAll";
        String GETINVITATION = "/getInvitation";
        String SENTINVITATION = "/sentInvitation";
        String MYTAXIES = "/mytaxies";

        String LIST = "/list";
        String LISTREVIEW = "/listReview";
        String DELETE = "/delete";
        String STATUS = "/status";
        String SPEED = "/speed";
        String STATUS_LOCATION = "/status/location";
        String SEARCH = "/search";
        String LOCATION = "/location";

    }

    public interface Jsp {
        String INDEX_PAGE = "common/index";
        String SEARCH_PAGE = "common/search";
        String ERROR_PAGE = "common/error";
        String DETAILS_PAGE = "common/details";
        String LOGIN_PAGE = "common/login";
        String CONFIRM_SOCIAL_LOGIN_PAGE = "common/confirm";
        String CONFIRM_SOCIAL_LOGIN_SUPPLIER_PAGE = "supplier/confirm";
        String SIGNUP_PAGE = "common/signup";
        String CONTACTUS_PAGE = "common/contactus";
        String PRIVACY_PAGE = "common/privacy";
        String TERMSOFSERVICE_PAGE = "common/termsofservice";
        String HELP="common/help";

        String USER_PAGE = "admin/user";
        String ADMIN_COUNTRY_PAGE = "admin/country";
        String ADMIN_CITY_PAGE = "admin/city";
        String ADMIN_REGION_PAGE = "admin/region";
        String ADMIN_TAXI_PAGE = "admin/taxi";
        String ADMIN_TAXI_UPDATE_PAGE = "admin/taxi-update";
        String ADMIN_TAXI_LIST_PAGE = "admin/taxilist";
        String ADMIN_TAXI_APPROVE_PAGE = "admin/taxiapprove";
        String ADMIN_TAXI_DETAILS_PAGE = "admin/details";

        String SUPPLIER_LOGIN_PAGE = "supplier/login";
        String SUPPLIER_SIGNUP_PAGE = "supplier/signup";
        String TAXIDRIVER_SIGNUP_PAGE = "taxidriver/signup";
        String SUPPLIER_EDIT_PROFILE_PAGE = "supplier/editprofile";
        String SUPPLIER_TAXI_PAGE = "supplier/taxi";
        String SUPPLIER_TAXI_UPDATE_PAGE = "supplier/taxi-update";
        String SUPPLIER_TAXI_LIST_PAGE = "supplier/taxilist";
        String SUPPLIER_TAXI_DETAILS_PAGE = "supplier/details";
        String SUPPLIER_ADDDRIVER_PAGE = "supplier/add-driver";
        String SUPPLIER_LISTDRIVER_PAGE = "supplier/list-driver";

    }
    public interface STATUS {
        long EXISTING = -2l;
        long NO_USER_VALUE = -3l;

    }
    public interface CARTYPE {
        String Taxi="Taxi";
    }

    public interface RADIUS {
        int radius=8000;
    }

    public interface LANG {
        String lang="en";
        String lang_de="de";
        String Lang_it="it";
        String Lang_fr="fr";
    }
    public interface ROLE {
        String ADMIN = "ROLE_ADMIN";
        String USER = "ROLE_USER";
        String DRIVER = "ROLE_DRIVER";
        String DELIVERY = "ROLE_DELIVERY";
        String SUPPLIER = "ROLE_SUPPLIER";
        String USER_IT = "ROLE_USER_IT";
        String ROLE = "ROLE";
    }
    public interface DOMAIN {
        String MYDOMAIN = "TAXIDEALS";
        int amount= 100;

    }

    public interface Image {
        String URL = "/driver-photo/id/";

    }
    public interface PAYMENT {
        String CASH = "CASH";

    }
    public interface MESSAGE {
        String SUCCESS = "SUCCESS";
        String UPDATE = "UPDATED";
        String FAILURE = "FAILURE";
        String VERIFIED = "1";
        String PHONENUMBER = "PHONENUMBER";
        String EMAIL = "EMAIL";
    }



}

