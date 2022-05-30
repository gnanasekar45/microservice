package com.luminn.firebase.service;

import com.google.firebase.messaging.*;
import com.luminn.firebase.controller.NotificationController;
import com.luminn.firebase.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.firebase.messaging.*;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
//@Log4j2
public class NotificationService {

    //https://stackoverflow.com/questions/60131380/can-a-message-be-sent-to-a-batch-of-tokens-to-fcm-clients-using-the-firebase-adm
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    TripPoolService tripPoolService;


    @Autowired
    TripPoolModel tripPoolModel;

    @Autowired
    NotificationModel notificationModel;

    public String sendPnsToDevice(PushNotificationRequests notificationRequest) {
        Notification notification = Notification.builder().setTitle(notificationRequest.getTitle())
                .setBody(notificationRequest.getMessage()).build();

        Message message = notificationModel.addbasic(notificationRequest);
        String  response = null;

        try {
            response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            //log.error("Fail to send firebase notification", e);
            return null;
        }
        return response;
    }

    public String sendPnsToDevice(Message message) {

        String response = null;
        try {
            response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            //log.error("Fail to send firebase notification", e);
            return null;
        }
        return response;
    }

    public String sendPnsToDeviceAll(String token,Message message) {

        String response = null;
        if(token != null && !"".equalsIgnoreCase(token))
        try {
            log.info(" message Display " + message.toString());
            response = FirebaseMessaging.getInstance().send(message);
            log.info(" response " + response);
        } catch (FirebaseMessagingException e) {
            log.error("Fail to send firebase notification", e);
            return null;
        }
        return response;
    }

    public String sendPnsToMultpleDeviceAll(String token,MulticastMessage message) {

        BatchResponse response = null;


        try {
             response = FirebaseMessaging.getInstance().sendMulticast(message);
        }
        catch (FirebaseMessagingException e) {
            log.error("Fail to send firebase notification", e);
            return null;
        }
// See the BatchResponse reference documentation
// for the contents of response.
        System.out.println(response.getSuccessCount() + " messages were sent successfully");
        return "success";
    }
    //https://firebase.google.com/docs/cloud-messaging/send-message#java
    public String sendPnsTripIdToDevice(MulticastMessage message) {

            try {
                List<String> registrationTokens = new ArrayList<String>();
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

                    log.info(" Success " + response.getSuccessCount());
                    log.info(" Failure " + response.getFailureCount());
                }
            }
            catch (Exception exception){

            }

        return "SUCCESS";
    }


    public String sendPnsToTopic(PushNotificationRequest notificationRequest) {
    	Notification notification = Notification.builder().setTitle(notificationRequest.getTitle())
    			.setBody(notificationRequest.getMessage()).build();
        Message message = Message.builder()
                .setTopic(notificationRequest.getTopic())
                .setNotification(notification)
                .putData("content", notificationRequest.getTitle())
                .putData("body", notificationRequest.getMessage())
                .build();

        String response = null;
        try {
        	response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("Fail to send firebase notification", e);
            return null;
        }
        return response;
    }
}
