package com.luminn.firebase.model;

import com.google.common.collect.ImmutableMap;
import com.google.firebase.messaging.*;
import com.luminn.firebase.dto.PhoneBookingSMS;
import com.luminn.firebase.dto.TripPool;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.repository.TripPoolRepository;
import com.luminn.firebase.service.NotificationService;
import com.luminn.firebase.service.TripPoolService;
import com.luminn.firebase.service.UserMongoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class NotificationModel {

    @Autowired
    NotificationService notificationService;

    @Autowired
    TripPoolService tripPoolService;

    @Autowired
    TripPoolModel tripPoolModel;

    @Autowired
    TripPoolRepository tripPoolRepository;

    @Autowired
    UserModel userModel;

    @Autowired
    UserMongoService userMongoService;

    private static final Logger log = LoggerFactory.getLogger(NotificationModel.class);

    public String add(PushNotificationRequests notificationRequest){

        Notification notification = Notification.builder().setTitle(notificationRequest.getTitle())
                .setBody(notificationRequest.getMessage()).build();

        String source = notificationRequest.getSource();
        String des = notificationRequest.getDes();
        String km = notificationRequest.getKm();
        String token = notificationRequest.getToken();
        String tripId = UUID.randomUUID().toString();
        String dbTripId = null;
        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .putData("tripId", tripId)
                .putData("source", source)
                .putData("dest", des)
                .putData("km", km)
                .putData("content", notificationRequest.getTitle())
                .putData("body", notificationRequest.getMessage())
                .build();


         String response =   notificationService.sendPnsToDeviceAll(token,message);

        System.out.println(" response -->" + response);
        notificationRequest.setTripId(tripId);
        dbTripId =  tripPoolService.addTripPool(notificationRequest);
        return dbTripId;
    }

    public List<String> getPhonenumber(String str){
        //String str = "1,2,3,11,#5,#7,9";
        List<String> phoneNumber = Arrays.asList(str.split(","));
        /*if(phoneNumber != null && phoneNumber.size() > 0) {
            List<Integer> normalNumbers = phoneNumber.stream().filter(i -> !i.startsWith("#")).map(Integer::parseInt).collect(Collectors.toList());
            List<Integer> specialNumbers = phoneNumber.stream().filter(i -> i.startsWith("#")).map(i -> Integer.valueOf(i.substring(1))).collect(Collectors.toList());
            System.out.println("normalNumbers -->" + normalNumbers);
            System.out.println("specialNumbers -->" + specialNumbers);
        }*/

        return phoneNumber;
    }

    public String addNoticeAll(PushPhoneNotificationRequest notificationRequest){


        String userToken = null;
        Notification notification = Notification.builder().setTitle(notificationRequest.getTitle())
                .setBody(notificationRequest.getMessage()).build();
        List<String> notificationsToken = new ArrayList<>();
        String token = notificationRequest.getToken();

        if(notificationRequest.getPhoneNumber() != null) {
            List<String> phoneNumner =  getPhonenumber(notificationRequest.getPhoneNumber());

            for(String singlePh : phoneNumner) {
                User user = userMongoService.findByPhoneNumber(singlePh);
                if (user != null && user.getNotificationToken() != null) {
                    userToken = user.getNotificationToken();
                    if (!"".equalsIgnoreCase(userToken)) {
                        notificationsToken.add(userToken);
                    }
                }
            }

        }


        MulticastMessage message = MulticastMessage.builder()
                .setNotification(notification)
                .putData("token", notificationRequest.getToken())
                .putData("content",notificationRequest.getTitle())
                .putData("body", notificationRequest.getMessage())
                .putData("type", "OFFER")
                .addAllTokens(notificationsToken)
                .build();



        String response =   notificationService.sendPnsToMultpleDeviceAll(token,message);



        System.out.println(" response -->" + response);

        return "success";
    }

    public String addNotice(PushPhoneNotificationRequest notificationRequest){
        String userToken = null;
        String token = null;
        Notification notification = Notification.builder().setTitle(notificationRequest.getTitle())
                .setBody(notificationRequest.getMessage()).build();

        if(notificationRequest.getToken() != null)
         token = notificationRequest.getToken();

        if(token == null || "".equalsIgnoreCase(token) || "string".equalsIgnoreCase(token) && notificationRequest.getPhoneNumber() != null) {
            User user= userMongoService.findByPhoneNumber(notificationRequest.getPhoneNumber());
            if(user != null && user.getNotificationToken() != null) {
                userToken = user.getNotificationToken();
                token =userToken;
            }
        }

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .putData("token", token)
                .putData("content",notificationRequest.getTitle())
                .putData("body", notificationRequest.getMessage())
                .putData("type", "OFFER")
                .build();        //
        String response =   notificationService.sendPnsToDeviceAll(token,message);
        System.out.println(" response -->" + response);
        return "success";
    }

    public String addMultipleNotice(PushPhoneNotificationRequest notificationRequest){
        String userToken = null;
        String token = null;
        String tokens = null;
        Notification notification = Notification.builder().setTitle(notificationRequest.getTitle())
                .setBody(notificationRequest.getMessage()).build();

        if(notificationRequest.getToken() != null)
            token = notificationRequest.getToken();

        String[] tokenSplit = token.split(";");

        ///for loop
        String response = null;
        for (int i=0; i < tokenSplit.length; i++) {
             tokens = tokenSplit[i];
            if (tokens != null || !"".equalsIgnoreCase(tokens) || !"string".equalsIgnoreCase(tokens) ) {
                /*User user = userMongoService.findByPhoneNumber(notificationRequest.getPhoneNumber());
                if (user != null && user.getNotificationToken() != null) {
                    userToken = user.getNotificationToken();
                    token = userToken;
                }*/

                Message message = Message.builder()
                        .setToken(tokens)
                        .setNotification(notification)
                        .putData("token", tokens)
                        .putData("content", notificationRequest.getTitle())
                        .putData("body", notificationRequest.getMessage())
                        .putData("type", "OFFER")
                        .build();        //
                response = notificationService.sendPnsToDeviceAll(tokens, message);
            }
        }

        System.out.println(" response -->" + response);
        return "success";
    }

    public String addPhoneBooking(PushPhoneNotificationRequest notificationRequest, PhoneBookingSMS requestBody){
        String userToken = null;
        String token = null;
        Notification notification = Notification.builder().setTitle(notificationRequest.getTitle())
                .setBody(notificationRequest.getMessage()).build();

        if(notificationRequest.getToken() != null)
            token = notificationRequest.getToken();

        if(token == null || "".equalsIgnoreCase(token) || "string".equalsIgnoreCase(token) && notificationRequest.getPhoneNumber() != null) {
            User user= userMongoService.findByPhoneNumber(notificationRequest.getPhoneNumber());
            if(user != null && user.getNotificationToken() != null) {
                userToken = user.getNotificationToken();
                token =userToken;
            }
        }

        /*Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .putData("token", token)
                .putData("content",notificationRequest.getTitle())
                .putData("body", notificationRequest.getMessage())
                .putData("type", "PHONEBOOKING")
                .build();        //
        String response =   notificationService.sendPnsToDeviceAll(token,message);*/
        //System.out.println(" response -->" + response);
        //return "success";

        Map<String,String> data = new HashMap<>();
        data.put("BookingId", requestBody.getBookingId());
        data.put("source", requestBody.getSource());
        data.put("dest", requestBody.getDestination());
        data.put("km", String.valueOf(requestBody.getKm()));
        //data.put("coupons", notificationRequest.getCoupons());
        //data.put("isAndroid", notificationRequest.getIsAndroid());
        data.put("userName", requestBody.getUserName());
        data.put("driverPhoneNumber", requestBody.getDriverPhoneNumber());
        data.put("price",String.valueOf(requestBody.getTotalPrice()) );
        data.put("VechileNumber", requestBody.getVechileNumber());
        data.put("Contact", requestBody.getContact());

        data.put("content", notificationRequest.getTitle());
        data.put("body", notificationRequest.getMessage());
        data.put("type", "PHONEBOOKING");

        ArrayList<String> list = new ArrayList<>();
        list.add(token);


        try {
            MulticastMessage multicasrmessage = MulticastMessage.builder().setNotification(notification).
                    putAllData(data).
                    addAllTokens(list).build();
            BatchResponse response2 = FirebaseMessaging.getInstance().sendMulticast(multicasrmessage);
            System.out.println(" response -->" + response2);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return "success";
    }

    public String addbackGroundBookingbyPhone(PushPhoneNotificationRequest notificationRequest, PhoneBookingSMS requestBody){


        Notification notification = Notification.builder().setTitle(notificationRequest.getTitle())
                .setBody(notificationRequest.getMessage()).build();

        String token = notificationRequest.getToken();
        String tripId = UUID.randomUUID().toString();
        String dbTripId = null;

        //ImmutableMap.of("action", "request_order");
        Map<String,String> data = new HashMap<>();
        data.put("BookingId", requestBody.getBookingId());
        data.put("source", requestBody.getSource());
        data.put("dest", requestBody.getDestination());
        data.put("km", String.valueOf(requestBody.getKm()));
        //data.put("coupons", notificationRequest.getCoupons());
        //data.put("isAndroid", notificationRequest.getIsAndroid());
        data.put("userName", requestBody.getUserName());
        data.put("driverPhoneNumber", requestBody.getDriverPhoneNumber());
        data.put("price",String.valueOf(requestBody.getTotalPrice()) );
        data.put("VechileNumber", requestBody.getVechileNumber());
        data.put("Contact", requestBody.getContact());

        data.put("content", notificationRequest.getTitle());
        data.put("body", notificationRequest.getMessage());
        data.put("type", "PHONEBOOKING");

        ArrayList<String> list = new ArrayList<>();
        list.add(token);

        try {
            MulticastMessage multicasrmessage = MulticastMessage.builder().putAllData(data).addAllTokens(list).build();
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(multicasrmessage);
            System.out.println(" response -->" + response);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        //notificationRequest.setTripId(tripId);
        //dbTripId =  tripPoolService.addTripPool(notificationRequest);
        return dbTripId;
    }

    public String addbackGround(PushUserNotificationRequests notificationRequest){


        Notification notification = Notification.builder().setTitle(notificationRequest.getTitle())
                .setBody(notificationRequest.getMessage()).build();

        String source = notificationRequest.getSource();
        String des = notificationRequest.getDes();
        String km = notificationRequest.getKm();
        String token = notificationRequest.getToken();
        String tripId = UUID.randomUUID().toString();
        String dbTripId = null;

        //ImmutableMap.of("action", "request_order");
        Map<String,String> data = new HashMap<>();
        data.put("tripId", tripId);
        data.put("source", source);
        data.put("dest", des);
        data.put("km", km);
        data.put("isAndroid", notificationRequest.getIsAndroid());
        data.put("latitude", notificationRequest.getLatitude());
        data.put("longitude", notificationRequest.getLongitude());
        data.put("phoneNumber", notificationRequest.getPhoneNumber());
        data.put("paymentType", notificationRequest.getPaymentType());
        data.put("coupen", notificationRequest.getCoupen());
        data.put("action", notificationRequest.getAction());
        data.put("rideKeyLocal", notificationRequest.getRideKeyLocal());
        data.put("category", notificationRequest.getCategory());
        data.put("userToken", notificationRequest.getUserToken());
        data.put("token", notificationRequest.getToken());
        data.put("price", notificationRequest.getPrice());
        data.put("userId", notificationRequest.getUserId());
        data.put("content", notificationRequest.getTitle());
        data.put("body", notificationRequest.getMessage());

        ArrayList<String> list = new ArrayList<>();
        list.add(token);

        try {
            MulticastMessage multicasrmessage = MulticastMessage.builder().putAllData(data).addAllTokens(list).build();
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(multicasrmessage);
            System.out.println(" response -->" + response);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        notificationRequest.setTripId(tripId);
        dbTripId =  tripPoolService.addTripPool(notificationRequest);
        return dbTripId;
    }


    public String addbackGroundCoupan(UserNotificationRequests notificationRequest){


        Notification notification = Notification.builder().setTitle(notificationRequest.getTitle())
                .setBody(notificationRequest.getMessage()).build();

        String source = notificationRequest.getSource();
        String des = notificationRequest.getDes();
        String km = notificationRequest.getKm();
        String token = notificationRequest.getToken();
        String tripId = UUID.randomUUID().toString();
        String dbTripId = null;

        //ImmutableMap.of("action", "request_order");
        Map<String,String> data = new HashMap<>();
        data.put("tripId", tripId);
        data.put("source", source);
        data.put("dest", des);
        data.put("km", km);
        data.put("coupons", notificationRequest.getCoupons());
        data.put("isAndroid", notificationRequest.getIsAndroid());
        data.put("latitude", notificationRequest.getLatitude());
        data.put("longitude", notificationRequest.getLongitude());
        data.put("phoneNumber", notificationRequest.getPhoneNumber());
        data.put("paymentType", notificationRequest.getPaymentType());
        data.put("action", notificationRequest.getAction());
        data.put("rideKeyLocal", notificationRequest.getRideKeyLocal());
        data.put("category", notificationRequest.getCategory());
        data.put("userToken", notificationRequest.getUserToken());
        data.put("token", notificationRequest.getToken());
        data.put("price", notificationRequest.getPrice());
        data.put("userId", notificationRequest.getUserId());
        data.put("content", notificationRequest.getTitle());
        data.put("body", notificationRequest.getMessage());

        ArrayList<String> list = new ArrayList<>();
        list.add(token);

        try {
            MulticastMessage multicasrmessage = MulticastMessage.builder().putAllData(data).addAllTokens(list).build();
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(multicasrmessage);
            System.out.println(" response -->" + response);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        notificationRequest.setTripId(tripId);
        dbTripId =  tripPoolService.addTripPool(notificationRequest);
        return dbTripId;
    }

    public String addMultiple(PushNotificationRequests notificationRequest){
        //String tripId =  tripPoolService.addTrip(notificationRequest);
        String tripId = null;
        List<String> registrationTokens = new ArrayList<String>();
        registrationTokens.add(notificationRequest.getToken());

        Notification notification = Notification.builder().setTitle(notificationRequest.getTitle())
                .setBody(notificationRequest.getMessage()).build();

        List<String> registrationTokens2 = Arrays.asList(
                "YOUR_REGISTRATION_TOKEN_1",
                // ...
                "YOUR_REGISTRATION_TOKEN_n"
        );

        try {

            MulticastMessage message = MulticastMessage.builder()
                    .putData("tripId", tripId)
                    .setNotification(notification)
                    .addAllTokens(registrationTokens)
                    .build();

            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);

            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                List<String> failedTokens = new ArrayList<>();
                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        // The order of responses corresponds to the order of the registration tokens.
                        failedTokens.add(registrationTokens.get(i));
                    }
                }

                //log.info(" Success " + response.getSuccessCount());
                //log.info(" Failure " + response.getFailureCount());
            }
        } catch (FirebaseMessagingException e) {
            //log.error("Fail to send firebase notification", e);
            return null;
        }

        return "SUCCESS";
    }

    public Message addbasic(PushNotificationRequests notificationReques){

        Notification notification = Notification.builder().setTitle(notificationReques.getTitle())
                .setBody(notificationReques.getMessage()).build();

        String tripId =  tripPoolService.addTrip(notificationReques);
        Message response = tripPoolModel.add(notificationReques,notification,tripId);

        notificationService.sendPnsToDevice(response);
        return null;
    }

    public String addConfirmation(ConfirmationNotificationResponse notificationReques){

        Notification notification = Notification.builder().setTitle(notificationReques.getTitle())
                .setBody(notificationReques.getMessage()).build();

        //String tripId =  tripPoolService.addTrip(notificationReques);
        Message message = confirmation(notificationReques,notification);

        String status =  notificationService.sendPnsToDevice(message);
        if(status != null){
            TripPool tripPool =    tripPoolRepository.findByTripId(notificationReques.getTripId());
            tripPool.setMessage("Message send to drivers");
            tripPoolRepository.save(tripPool);
            return  ModelStatus.SUCCESS.name();
        }
        else
            return "ERROR";
    }

    public String tripReview(ReviewNotificationResponse notificationReques){

        Notification notification = Notification.builder().setTitle(notificationReques.getTitle())
                .setBody(notificationReques.getMessage()).build();

        //String tripId =  tripPoolService.addTrip(notificationReques);
        Message message = review(notificationReques,notification);

        String status =  notificationService.sendPnsToDevice(message);
        if(status != null){
            TripPool tripPool =    tripPoolRepository.findByTripId(notificationReques.getTripId());
            tripPool.setMessage("Message send to user Review");
            tripPoolRepository.save(tripPool);
            return  ModelStatus.SUCCESS.name();
        }
        else
            return "ERROR";
    }

    public Message confirmation(ConfirmationNotificationResponse confirmationNotificationResponse, Notification notification){


        Message message = Message.builder()
                .setToken(confirmationNotificationResponse.getToken())
                .setNotification(notification)
                .putData("content", confirmationNotificationResponse.getTitle())
                .putData("body", confirmationNotificationResponse.getMessage())
                .build();
        return message;
    }

    public Message review(ReviewNotificationResponse confirmationNotificationResponse, Notification notification){


        Message message = Message.builder()
                .setToken(confirmationNotificationResponse.getToken())
                .setNotification(notification)
                .putData("content", confirmationNotificationResponse.getTitle())
                .putData("body", confirmationNotificationResponse.getMessage())
                .build();
        return message;
    }
}
