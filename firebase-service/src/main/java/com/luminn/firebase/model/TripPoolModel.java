package com.luminn.firebase.model;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.luminn.firebase.dto.*;
import com.luminn.firebase.entity.RIDESTATUS;
import com.luminn.firebase.entity.Ride;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.helper.Converter;
import com.luminn.firebase.repository.TripPoolRepository;
import com.luminn.firebase.request.TripPoolRequest;
import com.luminn.firebase.service.NotificationService;
import com.luminn.firebase.service.RideService;
import com.luminn.firebase.service.TripPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TripPoolModel {

    @Autowired
    RideService rideService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    TripPoolService tripPoolService;

    @Autowired
    TripPoolRepository tripPoolRepository;

    @Autowired
    RideModel rideModel;

    @Autowired
    UserModel userModel;

    public ModelStatus update(TripPoolRequest tripPoolRequest){

        TripPool tripPool = tripPoolRepository.findByTripId(tripPoolRequest.getTripId());

        if(tripPool != null && tripPool.getStatus() != null && tripPool.getStatus().equals(RIDESTATUS.ACCEPTED.name())){
            //log.info(" BEFORE GO ---->" );
            System.out.println(" Already booked by other driver ---->" );
            return ModelStatus.EXISTS;
        }
        else {
            //ONCE ACCEPTED, create a ride
            cycle(tripPoolRequest);
            return tripPoolService.updateTripId(tripPoolRequest);
        }
    }

    public Message add(PushNotificationRequests notificationRequest, Notification notification,String tripId){

        Message message = Message.builder()
                .setToken(notificationRequest.getToken())
                .setNotification(notification)
                .putData("tripId", tripId)
                .putData("content", notificationRequest.getTitle())
                .putData("body", notificationRequest.getMessage())
                .build();

            return message;

    }






    public void cycle(TripPoolRequest tripPoolRequest){

        TripPool tripPool = tripPoolRepository.findByTripId(tripPoolRequest.getTripId());
        //
        ShortRide shorRide = new ShortRide();
        shorRide.setTripId(tripPoolRequest.getTripId());
        shorRide.setUserId(tripPool.getUserId());
        shorRide.setDriverId(tripPoolRequest.getDriverId());

        if(tripPoolRequest.getStatus() != null && tripPoolRequest.getStatus().equalsIgnoreCase(RIDESTATUS.ACCEPTED.name()))
                     shorRide.setStatus(RIDESTATUS.ACCEPTED);

        rideModel.create(shorRide,"SYSTEM");
    }

    public List<TripDTO> getTripDTO(List<TripPool> trips)  {

        List<TripDTO> rideDTOList = new ArrayList<>();
        for(TripPool tripPool : trips) {
            if ( tripPool != null) {

                TripDTO tripDTO = Converter.entityTripToDTO(tripPool, null);

                if (tripPool.getDriverId() != null) {
                    tripDTO.setDriverName(getUserorDriverName(tripPool.getDriverId()));
                }
                if (tripPool.getUserId() != null) {
                    tripDTO.setUserName(getUserorDriverName(tripPool.getUserId()));
                }
                rideDTOList.add(tripDTO);
            }
        }
        return rideDTOList;
    }

    public List<TripDetailDTO> getTripDetailDTO(List<TripPool> trips)  {

        List<TripDetailDTO> rideDTOList = new ArrayList<>();
        for(TripPool tripPool : trips) {
            if ( tripPool != null) {

                TripDetailDTO tripDTO = Converter.entityTripDetailToDTO(tripPool, null);

                tripDTO.setStatus(tripPool.getStatus());
                if (tripPool.getDriverId() != null) {

                    User driver =  userModel.getById(tripPool.getDriverId());
                    tripDTO.setDriverName(driver.getFirstName());
                    tripDTO.setDriverPhoneNumber(driver.getPhoneNumber());

                }
                if (tripPool.getUserId() != null) {
                    User user =  userModel.getById(tripPool.getUserId());
                    tripDTO.setUserName(user.getFirstName());
                    tripDTO.setUserPhoneNumber(user.getPhoneNumber());
                }

                rideDTOList.add(tripDTO);
            }
        }
        return rideDTOList;
    }

    private String getUserorDriverName(String id) {
        try {
            User user =  userModel.getById(id);
            if(user != null)
                return user.getFirstName();
        } catch (Exception e) {
            return "NO NAME";
        }
        return "NO NAME";
    }

    private User getUserorDriver(String id) {
        try {
            return userModel.getById(id);
        } catch (Exception e) {

        }
        return null;
    }
}
