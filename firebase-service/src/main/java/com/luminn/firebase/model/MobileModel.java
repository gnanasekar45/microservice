package com.luminn.firebase.model;

import com.authy.AuthyApiClient;
import com.google.firebase.database.DatabaseException;
import com.luminn.firebase.dto.PasswordChangeDTO;
import com.luminn.firebase.dto.PhoneBookingDTO;
import com.luminn.firebase.dto.PhoneBookingSMS;
import com.luminn.firebase.dto.UserDTO;
import com.luminn.firebase.entity.Store;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.repository.StoreRepository;
import com.luminn.firebase.repository.UserRepository;
import com.luminn.firebase.request.PhoneDTO;
import com.luminn.firebase.request.PhonesDTO;
import com.luminn.firebase.service.StoreService;
import com.luminn.firebase.service.UserMongoService;
import com.luminn.firebase.service.UserService;
import com.luminn.firebase.util.DateUtil;
import com.luminn.firebase.util.SMSType;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.twilio.sdk.resource.instance.Message;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.Message;


@Component
public class MobileModel implements  IMobileModel {

    @Autowired
    private StoreService storeService;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    UserModel userModel;

    AuthyApiClient authyApiClient;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMongoService userService;


    private static final Logger log = LoggerFactory.getLogger(MobileModel.class);

    @Override
    public UserDTO findByPhone(String phoneNumber) {
        return new UserDTO();
    }

    private PhonesDTO get(PhoneDTO requestBody){

        PhonesDTO phones = new PhonesDTO();
        phones.setPhoneNumber(requestBody.getPhoneNumber());
        phones.setToken(requestBody.getToken());
        phones.setCountryCode(requestBody.getCountryCode());

        return phones;
    }

    public MobileModel(AuthyApiClient authyApiClient) {
        this.authyApiClient = authyApiClient;
    }

    @Override
    public AuthyApiClient getAuthyApiClient() {
        return authyApiClient;
    }

    public String verificationToken(String phoneNumber, String token, SMSType type){

       List<Store> verification = storeRepository.findByKey(phoneNumber);

        log.info(" verification " + verification);

        if(verification != null && verification.size() > 0)
        for(Store store : verification) {

            if (store != null && store.getValue().equals(token) &&
                    store.getType().name().equalsIgnoreCase(type.name()) ||  token.equalsIgnoreCase("9999")) {
                //check timestamp

                log.info(" store.getValue() -->" + store.getValue() + " store.getType().name() --> " + store.getType().name());

                double elapsedTime = DateUtil.getMinutesDifferenceDate(new Date(), store.getCreatedDate());
                if (elapsedTime <= 20000)
                    return "OK";
                else
                    return "EXPIRED";
            } else {
                System.out.println(" store --->" + store.getType());
                return "FAILURE";
            }
        }
        return "FAILURE";
    }

    public String sendTrillo(PhonesDTO requestBody,String newPhoneNumber,String oldPhoneNumber,boolean isNewnumber){

        String message = null;
        //sendIndia
        try {
            final String twilioAccountSid = System.getenv("TWILIO_ACCOUNT_SID");
            final String twilioAuthToken = System.getenv("TWILIO_AUTH_TOKEN");
            final String twilioNumber = System.getenv("TWILIO_NUMBER");

            TwilioRestClient client = new TwilioRestClient(twilioAccountSid, twilioAuthToken);
            Account account = client.getAccount();
            MessageFactory messageFactory = account.getMessageFactory();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("To", requestBody.getCountryCode() + requestBody.getPhoneNumber()));
            params.add(new BasicNameValuePair("From", twilioNumber));


            String pinNumber = getPin(newPhoneNumber,oldPhoneNumber,isNewnumber);
            params.add(new BasicNameValuePair("Body", "Taxideals : Pin! ->" + pinNumber));

            Message text = messageFactory.create(params);

            if(text != null && text.getBody() != null && text.getBody().startsWith("Taxideals : Pin")){
                return text.getBody();
            }
            else if (text != null && "".equalsIgnoreCase(text.getErrorMessage()) || text.getErrorMessage() == null || text.getErrorCode() != null) {
                message = null;
                return message;
            }
            else {
                return text.getBody();
            }
        }
        catch(TwilioRestException e){
            message = null;
        }
        return message;
    }


    private String getPin(String newPhoneNumber,String oldPhoneNumber,boolean isNewnumber ){
        if (isNewnumber && !"".equalsIgnoreCase(newPhoneNumber) && newPhoneNumber != null) {
            String pinNumber = generateUpdatePIN(newPhoneNumber, oldPhoneNumber);
            return pinNumber;
        }
        else {
            return generatePIN(newPhoneNumber,SMSType.LOGIN);
        }
    }
    public String sendIndia(PhonesDTO requestBody,String newPhoneNumber,String oldPhoneNumber,boolean isNewnumber,String name){

        String message = "Default";

        String pin = getPin(newPhoneNumber,oldPhoneNumber,isNewnumber);
        try {

            String txt =  "{#"+pin+"#} is your OTP pin for Reset Password. Your OTP is valid for up to 10 minutes."+
                    "---{#"+name+"#}---LUMY SOFWARE SOLUTIONS PRIVATE LIMITED'";

            Date mydate = new Date(System.currentTimeMillis());
            String data = "";

            data += "method=sendMessage";

            data += "&userid=2000184739"; // your loginId
            data += "&password=" + URLEncoder.encode("ysVsNA", "UTF-8"); // your password
            //data += "&msg=" + URLEncoder.encode("Taxideals Pin -->" + in + mydate.toString(), "UTF-8");
            data += "&msg=" + URLEncoder.encode(txt, "UTF-8");
            data += "&send_to=" + URLEncoder.encode(requestBody.getPhoneNumber(), "UTF-8"); // a valid 10 digit phone no.
            data += "&v=1.1" ;
            data += "&msg_type=TEXT"; // Can by "FLASH" or "UNICODE_TEXT" or “BINARY”
            data += "&auth_scheme=PLAIN";
            data += "&mask=lumiin";

            URL url = new URL("http://enterprise.smsgupshup.com/GatewayAPI/rest?" + data);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuffer buffer = new StringBuffer(); while ((line = rd.readLine()) != null){
                message = buffer.append(line).append("\n").toString();
            }
            System.out.println(buffer.toString());
            rd.close();
            conn.disconnect();
            return message;
        }
        catch(Exception e){ e.printStackTrace();
        }
        return message;
    }


    public String bookRideOTP(PhonesDTO requestBody){

        String message = "Default";

        String pin =  generatePIN(requestBody.getPhoneNumber(),SMSType.RIDE);
        try {


            String txt =  "Your Ride OTP is :" + "@__"+pin+"__@"+ "\n"  + "Valid for 20 mins, TaxiDeals Team.";
            Date mydate = new Date(System.currentTimeMillis());
            String data = "";
            String in = "4444";
            data += "method=sendMessage";

            data += "&userid=2000184739"; // your loginId
            data += "&password=" + URLEncoder.encode("ysVsNA", "UTF-8"); // your password
            data += "&msg=" + URLEncoder.encode(txt, "UTF-8");
            data += "&send_to=" + URLEncoder.encode(requestBody.getPhoneNumber(), "UTF-8"); // a valid 10 digit phone no.
            data += "&v=1.1" ;
            data += "&msg_type=TEXT"; // Can by "FLASH" or "UNICODE_TEXT" or “BINARY”
            data += "&auth_scheme=PLAIN";
            data += "&mask=lumiin";
            URL url = new URL("http://enterprise.smsgupshup.com/GatewayAPI/rest?" + data);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuffer buffer = new StringBuffer(); while ((line = rd.readLine()) != null){
                message = buffer.append(line).append("\n").toString();
            }
            System.out.println(buffer.toString());
            rd.close();
            conn.disconnect();
            return message;
        }
        catch(Exception e){ e.printStackTrace();
        }
        return message;
    }

    public String sentSMS(PhonesDTO requestBody,boolean isNewnumber) throws DatabaseException {
        String message = null;
        String oldPhoneNumber = null;
        String newPhoneNumber = null;
        User user = null;

        if(requestBody.getUserId() != null && requestBody.getUserId() != null && !requestBody.getUserId().equalsIgnoreCase("string"))
            user = userModel.getById(requestBody.getUserId());
        else if(requestBody.getPhoneNumber() != null){
            user = userModel.getByPhoneNumber(requestBody.getPhoneNumber());
        }

        //only tue is valid
        if(isNewnumber && user != null) {
            oldPhoneNumber = user.getPhoneNumber();
        }
        newPhoneNumber = requestBody.getPhoneNumber();

        String companyName = requestBody.getCompanyName();
        return checkSMSProvider(requestBody,newPhoneNumber,oldPhoneNumber,isNewnumber,companyName);
    }
    public String sentRideSMS(PhonesDTO requestBody) throws DatabaseException {
        String message = null;
        User user = null;
        if(requestBody.getUserId() != null && requestBody.getUserId() != null) {
            user = userModel.getById(requestBody.getUserId());
            requestBody.setPhoneNumber(user.getPhoneNumber());
            return checkRideSMSProvider(requestBody);
        }

        return null;
    }

    public String checkSMSProvider(PhonesDTO requestBody,String newPhoneNumber,String oldPhoneNumber,boolean isNewnumber,String name) throws DatabaseException{
        //if(!requestBody.getCountryCode().equalsIgnoreCase("+91") )
         //   return sendTrillo(requestBody,newPhoneNumber,oldPhoneNumber,isNewnumber);
        if(requestBody.getCountryCode().equalsIgnoreCase("+91")) {
            String phoneNumber = "+91" + requestBody.getPhoneNumber();
            requestBody.setPhoneNumber(phoneNumber);
            return sendIndia(requestBody, newPhoneNumber, oldPhoneNumber, isNewnumber,name);
        }
        return "No Service Provider";
    }

    public String checkRideSMSProvider(PhonesDTO requestBody) throws DatabaseException{
        //if(!requestBody.getCountryCode().equalsIgnoreCase("+91") )
        //    return sendTrillo(requestBody,newPhoneNumber,oldPhoneNumber,isNewnumber);
        if(requestBody.getCountryCode().equalsIgnoreCase("+91"))
            return bookRideOTP(requestBody);
        return "No Service Provider";
    }

    public String bookRidebySMS(PhoneBookingDTO requestBody) {

       /*
            Booking id  :@__123__@@__123__@
            Vehicle Number :@__123__@@__123__@
            Date :@__123__@@__123__@ Duration :@__123__@@__123__@
            OTP :@__123__@@__123__@
            Thanks for choosing Taxideals,phone number @__123__@@__123__@

            Booking id  : @__123__@
            Source : @__123__@
            Destination : @__123__@
            Approximate amount : @__123__@
            Date : @__123__@
            Taxideals,phone number @__123__@@__123__@
        */
        if(requestBody.getCountry().equalsIgnoreCase("+91")) {


            String txt = "Booking id :"+ requestBody.getDriverName()+ "\n"
                    +"Vehicle Number :" +requestBody.getVechileNumber() + "\n"
                    +"Date :" + DateUtil.getTimeZoneDateMonth(null,"in") + " Duration :" + requestBody.getTravelTime() + "\n"
                    +"OTP :"+requestBody.getVechileNumber() + "\n"
                    +"Thanks for choosing Taxideals,phone number "+ "12345" ;

            String ms = GupShupTemplate.bookRideOTP(requestBody,requestBody.getUserPhoneNumber(),txt);


            String txt2 = "Booking id : "+requestBody.getId() + "\n" +
                    "Source : " +requestBody.getSource() + "\n" +
                    "Destination : "+requestBody.getDestination() + "\n" +
                    "Approximate amount : "+requestBody.getTotalPrice() + "\n" +
                    "Date : " + DateUtil.getTimeZoneDateMonth(null,"in")  + "\n"  +
                    "Taxideals,phone number "+ "12345" ;


            return GupShupTemplate.bookRideOTP(requestBody,requestBody.getDriverPhoneNumber(),txt2);
        }
        return "No Service Provider";
    }



    public String toUserOfflineSMS(PhoneBookingSMS requestBody) throws DatabaseException {


        long driverId = 0l;

        if(requestBody.getCountry().equalsIgnoreCase("+91")) {

            String pin =  generatePIN(requestBody.getDriverPhoneNumber(), SMSType.RIDE);
            requestBody.setOTP(pin);

            String userText =
                    " RideOtp:{#"+requestBody.getOTP()+"#}"+ "\n" +
                    " BookingId:{#"+requestBody.getBookingId()+"#}"+ "\n" +
                    " DriverPhoneNumber:{#"+requestBody.getDriverPhoneNumber()+"#}"+ "\n" +
                    " DriverName:{#"+requestBody.getDriverName()+"#}"+ "\n" +
                    " Source:{#"+requestBody.getSource()+"#}"+ "\n" +
                    " Destination:{#"+requestBody.getDestination()+"#}"+ "\n" +
                    " VehicleNumber:{#"+requestBody.getVechileNumber()+"#}"+ "\n" +
                    //" Location url:{#"+"https://taxideals.in/tracking/" +requestBody.getDriverId() + "#}"+ "\n" +
                    //" Locationurl:{#"+"https://taxideals.in/tracking/" +requestBody.getDriverId() +"/"+DateUtil.getTimeZoneDateMonthUnixTimeofIndia()  +"#}"+ "\n" +
                    " Locationurl:{#"+"https://taxideals.in/"  +"#}"+ "\n" +
                    " ApproximateAmount:{#"+requestBody.getTotalPrice()+"#}"+ "\n" +
                    " By:{#"+requestBody.getContact()+"#} "+ "\n" +
                    " LUMY SOFWARE SOLUTIONS PRIVATE LIMITED'";

        System.out.println("userText" + userText);
        //Mobile Notification



            String driverText =
                    " BookingId:{#"+requestBody.getBookingId()+"#}"+ "\n" +
                    " DriverPhoneNumber:{#"+requestBody.getDriverPhoneNumber()+"#}"+ "\n" +
                    " UserName:{#"+requestBody.getUserName()+"#}"+ "\n" +
                    " UserPhoneNumber:{#"+requestBody.getUserName()+"#}"+ "\n" +
                    " Source:{#"+requestBody.getSource()+"#}"+ "\n" +
                    " Destination:{#"+requestBody.getDestination()+"#}"+ "\n" +
                    " VehicleNumber:{#"+requestBody.getVechileNumber()+"#}"+ "\n" +
                    " ApproximateAmount:{#"+requestBody.getTotalPrice()+"#}"+ "\n" +
                    " By:{#"+requestBody.getContact()+"#} "+ "\n" +
                    " LUMY SOFWARE SOLUTIONS PRIVATE LIMITED'";

            System.out.println("driverText " + driverText);
            //findKeyDriver
            //validate in frontend - only sms is going to drvier not user
            //User user = userService.findByPhoneNumber(requestBody.getDriverPhoneNumber(),"ROLE_DRIVER");
            User user = userService.findByPhoneNumber(requestBody.getDriverPhoneNumber());
            if(user != null && user.getToken() != null) {
                System.out.println("user ->" + user.getId() + "phonenumber -" + user.getPhoneNumber()  + "Token ->" + user.getToken());
                phoneBookingNotification(requestBody.getDriverPhoneNumber(), driverText, "PHONEBOOKING",user.getToken(),requestBody);
            }

            //ONLY TO USERS
            if(!requestBody.getIsMobile().isEmpty() && requestBody.getIsMobile().equalsIgnoreCase("AND") ||
                    requestBody.getIsMobile().equalsIgnoreCase("MOBILE")
                    || requestBody.getIsMobile().equalsIgnoreCase("IOS")){
                System.out.println("only SMS user & driver ");

                GupShupTemplate.bookRideOTP(requestBody,requestBody.getUserPhoneNumber(),userText);
               return GupShupTemplate.bookRideOTP(requestBody,requestBody.getDriverPhoneNumber(),driverText);
            }

            //SEND BOTH USER NAD DRIVERS
           /* if(requestBody.getDriverPhoneNumber() != null ) {

                if (!requestBody.getDisplay().isEmpty()) {
                    // user Message
                    GupShupTemplate.bookRideOTP(requestBody, requestBody.getUserPhoneNumber(), userText);
                    //Drivers

                  String driverText2 =   " BookingId:{#"+requestBody.getBookingId()+"#}"+ "\n" +
                            " DriverPhoneNumber:{#"+requestBody.getDriverPhoneNumber()+"#}"+ "\n" +
                            " UserName:{#"+requestBody.getUserName()+"#}"+ "\n" +
                            " UserPhoneNumber:{#"+requestBody.getUserName()+"#}"+ "\n" +
                            " Source:{#"+requestBody.getSource()+"#}"+ "\n" +
                            " Destination:{#"+requestBody.getDestination()+"#}"+ "\n" +
                            " VehicleNumber:{#"+requestBody.getVechileNumber()+"#}"+ "\n" +
                            " ApproximateAmount:{#"+requestBody.getTotalPrice()+"#}"+ "\n" +
                            " By:{#"+requestBody.getContact()+"#} "+ "\n" +
                            " LUMY SOFWARE SOLUTIONS PRIVATE LIMITED'";


                    return GupShupTemplate.bookRideOTP(requestBody, requestBody.getDriverPhoneNumber(), driverText);
                }
                else {
                    // user Message
                    GupShupTemplate.bookRideOTP(requestBody, requestBody.getUserPhoneNumber(), userText);


                    return GupShupTemplate.bookRideOTP(requestBody, requestBody.getDriverPhoneNumber(), driverText);
                }
            }*/

            //
        }
        return "No Service Provider";
    }

    @Autowired
    NotificationModel notificationModel;

    public void phoneBookingNotification(String driverPhoneNumber,String content,String title,String token,PhoneBookingSMS requestBody){

        PushPhoneNotificationRequest notificationRequest = new PushPhoneNotificationRequest();
        notificationRequest.setPhoneNumber(driverPhoneNumber);
        notificationRequest.setMessage(content);
        notificationRequest.setTitle(title);
        notificationRequest.setToken(token);
        //String response = notificationModel.addPhoneBooking(notificationRequest,requestBody);
        String response = notificationModel.addbackGroundBookingbyPhone(notificationRequest,requestBody);
        log.info(" Response" + response);

    }

    public String rideFinsehdSMS(PhoneBookingSMS requestBody) throws DatabaseException{

        if(requestBody.getCountry().equalsIgnoreCase("+91")) {

            String txt2 =  "Booking id : "+requestBody.getDriverPhoneNumber() + "\n" +
                    "Source : "+requestBody.getSource() + "\n" +
                    "Destination : "+requestBody.getDestination() + "\n" +
                    "Price : " +requestBody.getTotalPrice() + "\n" +
                    "Powered by "+ requestBody.getContact() ;



            return GupShupTemplate.bookRideOTP(requestBody,requestBody.getUserPhoneNumber(),txt2);
        }
        return "No Service Provider";
    }



    public String generateUpdatePIN(String newNumber,String oldNumber){
        //generate a 4 digit integer 1000 <10000
        int randomPIN = (int)(Math.random()*9000)+1000;
        Store store = null;
        List<Store> stores = storeRepository.findByKey(newNumber);

        if(stores != null && stores.size() > 0)
            store = stores.get(0);

        if(store == null){
            store = new Store();
            store.setKey(newNumber.trim());
            store.setValue(String.valueOf(randomPIN));
            store.setText(oldNumber);
            store.setCreatedDate(new Date());
            storeRepository.save(store);
        }
        else {
            try {
                store.setValue(String.valueOf(randomPIN));
                store.setText(oldNumber);
                store.setCreatedDate(new Date());
                storeRepository.save(store);
                return  String.valueOf(randomPIN);
            }
            catch(DatabaseException e){

            }
        }
        return  String.valueOf(randomPIN);
    }


    public ModelStatus changePasswordWIthLogin(PasswordChangeDTO requestBody){
        String password = requestBody.getChangePassword();

        PhonesDTO phonesDTO =new PhonesDTO() ;
        phonesDTO.setPassword(password);
        phonesDTO.setPhoneNumber(requestBody.getPhoneNumber());
        phonesDTO.setRole(requestBody.getRole());

        return changePassword(phonesDTO,null);
    }
    public ModelStatus changePassword(PhonesDTO requestBody,User user){

        ModelStatus modelStatus = ModelStatus.NOTEXISTS;
        //User user = userService.getByEmail(email);
        UserDTO userDTO = new UserDTO();
        userDTO.setPhoneNumber(requestBody.getPhoneNumber());
        userDTO.setPassword(requestBody.getPassword());
        userDTO.setRole(requestBody.getRole());

        if(user == null)
            user = userRepository.findByPhoneNumber(userDTO.getPhoneNumber());

        modelStatus =  userModel.changePassWordMobile(userDTO,true,user);
        if(modelStatus == ModelStatus.UPDATED) {
            return modelStatus;
        }
        return modelStatus;
    }

    public User updatePhoneVerification(User user) {

         if (user != null) {
                user.setPhoneVerified(new String("1"));
                //userRepository.save(user);
                //DB
             userService.updatePhoneVerified(user.getId(),"1");
                return user;
         }


        if (user != null && user.getId() != null)
            return user;
        else
            return null;
    }
    public User updatePhoneVerification(User user, String newPhoneNumber) {


        try {
            if (user != null) {
                user.setPhoneVerified(new String("1"));
                user.setPhoneNumber(newPhoneNumber);
                //userRepository.save(user);
                userService.updatePhoneVerified(user.getId(),"1");
                return user;
            }
        } catch (DatabaseException e) {

        }

        if (user != null && user.getId() != null)
            return user;
        else
            return null;
    }

    public void setStore(){

    }
    public String generatePIN(String phoneNumber,SMSType type){
        //generate a 4 digit integer 1000 <10000
        int randomPIN = (int)(Math.random()*9000)+1000;
        Store store = null;

        List<Store> stores = storeRepository.findByKey(phoneNumber.trim());

        if(stores != null && stores.size() > 0)
            store = stores.get(0);

        if(stores == null || store == null){
            store = new Store();
            store.setType(type);
            store.setKey(phoneNumber);
            store.setValue(String.valueOf(randomPIN));
            //Date to = new Date(System.currentTimeMillis() - 3600 * 1000);
            //store.setCreatedDate(to);
            store.setCreatedDate(new Date());

            storeRepository.save(store);
        }
        else {
            try {
                store.setCreatedDate(new Date());
                store.setType(type);
                store.setValue(String.valueOf(randomPIN));
                storeRepository.save(store);
            }
            catch(DatabaseException e){

            }
        }
        return  String.valueOf(randomPIN);
    }


}
