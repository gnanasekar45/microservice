package com.luminn.firebase.controller;

import com.luminn.firebase.dto.PhoneBookingNotification;
import com.luminn.firebase.dto.PhoneBookingSMS;
import com.luminn.firebase.model.MobileModel;
import com.luminn.firebase.model.NotificationModel;
import com.luminn.firebase.model.PushPhoneNotificationRequest;
import com.luminn.view.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/phoneBookingNotification2")
public class PhoneBookingNotificationController {

    @Autowired
    NotificationModel notificationModel;

    @Autowired
    MobileModel mobileModel;


    @PostMapping("/tripId/all/user/notice/todevice")
    public ResponseEntity<StatusResponse> sendNoticeToDeviceUserAll(@RequestBody PushPhoneNotificationRequest request,@RequestBody PhoneBookingSMS phoneBookingSMS) {
        //CastMap ({notification: {title: string, body: string}, data: {source: Newyork, tripId: e212da8b-6120-4c0b-9841-fad51e9d9f01,
        // userId: string, km: 44, body: string, dest: zurich, content: string}})


        StatusResponse sr = new StatusResponse();
        String response = notificationModel.addPhoneBooking(request,phoneBookingSMS);
        if (response != null) {
            //return new ResponseEntity<>(new NotificationResponse("send notification successfully!"), HttpStatus.OK);
            sr.setStatus(true);
            sr.setMessage("send notification successfully!");
            sr.setData("send notification successfully!");
            return new ResponseEntity<>(sr, HttpStatus.OK);

        }
        else {
            sr.setStatus(false);
            sr.setMessage("while send notification, it's failed!");
            sr.setData("while send notification, it's failed!");
            return new ResponseEntity<>(sr, HttpStatus.EXPECTATION_FAILED);
        }
        //return new ResponseEntity<>(new NotificationResponse("while send notification, it's failed!"), HttpStatus.EXPECTATION_FAILED);
    }





        @PostMapping("/app/v1/all/user/notice/todevices")
    public ResponseEntity<StatusResponse> sendNoticeToDeviceUserAlltomobile(@RequestBody PhoneBookingNotification phoneBookingSMS) {
        StatusResponse sr = new StatusResponse();

            PushPhoneNotificationRequest request = new PushPhoneNotificationRequest();
            request.setTitle(phoneBookingSMS.getTitle());
            request.setMessage(phoneBookingSMS.getMessage());
            request.setToken(phoneBookingSMS.getToken());
            request.setPhoneNumber(phoneBookingSMS.getPhoneNumber());

            //String response = notificationModel.addPhoneBooking(request,phoneBookingSMS);
            String response = notificationModel.addbackGroundBookingbyPhone(request,phoneBookingSMS);

        if (response != null) {
            //return new ResponseEntity<>(new NotificationResponse("send notification successfully!"), HttpStatus.OK);
            sr.setStatus(true);
            sr.setMessage("send notification successfully!");
            sr.setData("send notification successfully!");
            return new ResponseEntity<>(sr, HttpStatus.OK);

        }
        else {
            sr.setStatus(false);
            sr.setMessage("while send notification, it's failed!");
            sr.setData("while send notification, it's failed!");
            return new ResponseEntity<>(sr, HttpStatus.EXPECTATION_FAILED);
        }

    }
    @PostMapping("/app/v2/all/user/notice/todevices")
    public ResponseEntity<StatusResponse> sendNoticeToDeviceUserAlltomobiles(@RequestBody PushPhoneNotificationRequest request,@RequestBody PhoneBookingSMS phoneBookingSMS) {
        StatusResponse sr = new StatusResponse();


        //String response = notificationModel.addPhoneBooking(request,phoneBookingSMS);
        String response = notificationModel.addbackGroundBookingbyPhone(request,phoneBookingSMS);

        if (response != null) {
            //return new ResponseEntity<>(new NotificationResponse("send notification successfully!"), HttpStatus.OK);
            sr.setStatus(true);
            sr.setMessage("send notification successfully!");
            sr.setData("send notification successfully!");
            return new ResponseEntity<>(sr, HttpStatus.OK);

        }
        else {
            sr.setStatus(false);
            sr.setMessage("while send notification, it's failed!");
            sr.setData("while send notification, it's failed!");
            return new ResponseEntity<>(sr, HttpStatus.EXPECTATION_FAILED);
        }

    }
}
