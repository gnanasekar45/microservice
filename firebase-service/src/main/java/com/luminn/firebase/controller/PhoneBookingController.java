package com.luminn.firebase.controller;

import com.luminn.firebase.dto.*;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.entity.UserOffline;
import com.luminn.firebase.model.ModelStatus;
import com.luminn.firebase.model.PhoneBookingModel;
import com.luminn.firebase.model.UserModel;
import com.luminn.firebase.service.MessageByLocaleService;
import com.luminn.firebase.util.Path;
import com.luminn.view.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/phoneBooking")
public class PhoneBookingController {

    @Autowired
    PhoneBookingModel phoneBookingModel;

    @Autowired
    UserModel userModel;

    @Autowired
    private MessageByLocaleService messageService;

    @ResponseBody
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V1 + Path.OperationUrl.CREATE, method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<StatusResponse> createew(@RequestBody PhoneBookingDTO phoneBookingDTO) {

        StatusResponse sr = new StatusResponse();
        ModelStatus status = null;
        User user = userModel.validate(phoneBookingDTO.getDriverId());

        if(user != null || user == null ) {

            //userId = userModel.createUser(userOffline);
            // phoneBookingModel.createPhoneBooking(userId, userOffline);

            status = phoneBookingModel.create(phoneBookingDTO);
        }

        if (status.equals(ModelStatus.CREATED)) {
            sr.setMessage(messageService.getMessage("booked_ride_by_sms", phoneBookingDTO.getDriverId()));
            sr.setStatus(true);
        } else {
            //(status.equals(ModelStatus.USER_IS_NOT_EXISTING))
            sr.setMessage(messageService.getMessage("user_is_not_existing"));
        }

        sr.setStatus(false);

        sr.setData(phoneBookingDTO.getDriverId());
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }


    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.PHONEBOOKING + Path.Url.VERSION_V1 + "/userOffline" + Path.OperationUrl.CREATE, method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<StatusResponse> createUserOffline(@RequestBody UserOffline userOffline)  {
        StatusResponse sr = new StatusResponse();
        UserOfflineResponse response = new UserOfflineResponse();;
        ModelStatus status = null;
        String userId = null;
        status =  phoneBookingModel.findDriver(userOffline.getDriverId());

        System.out.println("status " + status.name() + " userOffline.getDriverId()" + userOffline.getDriverId());

        if(status.equals(ModelStatus.CREATED)) {

            userId = userModel.createUser(userOffline);
            phoneBookingModel.createPhoneBooking(userId, userOffline);

            response.setUserId(userId);
            sr.setData(response);
            sr.setStatus(true);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
        else {
            response.setUserId(userId);
            sr.setData(response);
            sr.setStatus(false);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }

    }





    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.PHONEBOOKING + Path.Url.VERSION_V1 + "/userOffline/sms"+Path.OperationUrl.CREATE, method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<StatusResponse> createUserOffline2(@RequestBody PhoneBookingSMS phoneBookingDTO)  {

        StatusResponse sr = new StatusResponse();
        ModelStatus status = null;

        User user = userModel.validate(phoneBookingDTO.getDriverId());

        if(user != null || user == null ) {

            //finding a user
            String userId = userModel.createUser(phoneBookingDTO);
            //create and saving to DB
            String bookingId = phoneBookingModel.createPhoneBookingSMS(userId, phoneBookingDTO);

            //sending to sms
            phoneBookingDTO.setBookingId(bookingId);
            status = phoneBookingModel.createUserOfflineSMS(phoneBookingDTO);
        }

        if (status.equals(ModelStatus.CREATED)) {
            sr.setMessage(messageService.getMessage("booked_ride_by_sms", phoneBookingDTO.getDriverId()));
            sr.setStatus(true);
        } else {
            sr.setMessage(messageService.getMessage("user_is_not_existing"));
        }

        sr.setStatus(false);

        sr.setData(phoneBookingDTO.getDriverId());
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.PHONEBOOKING + Path.Url.VERSION_V1 + "/userOffline/final"+Path.OperationUrl.CREATE, method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<StatusResponse> finallySend(@RequestBody PhoneBookingsSMS phoneBookingDTO)  {

        StatusResponse sr = new StatusResponse();
        ModelStatus status = null;
        User user = userModel.validate(phoneBookingDTO.getDriverId());


        status = phoneBookingModel.rideFinsehdSMS(phoneBookingDTO);


        if (status.equals(ModelStatus.CREATED)) {
            sr.setMessage(messageService.getMessage("booked_ride_by_sms", phoneBookingDTO.getDriverId()));
            sr.setStatus(true);
        } else {
            //(status.equals(ModelStatus.USER_IS_NOT_EXISTING))
            sr.setMessage(messageService.getMessage("user_is_not_existing"));
        }

        sr.setStatus(false);

        sr.setData(phoneBookingDTO.getDriverId());
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.PHONEBOOKING + Path.Url.VERSION_V1 + "/allBookings/{supplierId}", method = RequestMethod.GET, produces = {"application/json" })
    public ResponseEntity<StatusResponse> getAllDrivers(@PathVariable("supplierId") String supplierId)  {
        StatusResponse sr = new StatusResponse();
        List<PhoneBookingDTO> listDTO = phoneBookingModel.getAllPhoneBooking(supplierId);
        sr.setStatus(true);
        sr.setData(listDTO);
        sr.setMessage(messageService.getMessage("driver_billing", listDTO.size()));
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.PHONEBOOKING + Path.Url.VERSION_V1 + "/driverBookings/{driverId}", method = RequestMethod.GET, produces = {"application/json" })
    public ResponseEntity<StatusResponse> getDrivers(@PathVariable("driverId") String driverId)  {
        StatusResponse sr = new StatusResponse();
        List<PhoneBookingDTO> listDTO = phoneBookingModel.getAlldrivers(driverId);
        sr.setStatus(true);
        sr.setData(listDTO);
        sr.setMessage(messageService.getMessage("driver_billing", listDTO.size()));
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.PHONEBOOKING + Path.Url.VERSION_V1 + "/bookingId/{id}", method = RequestMethod.GET, produces = {"application/json" })
    public ResponseEntity<StatusResponse> getBookingId(@PathVariable("id") String id)  {
        StatusResponse sr = new StatusResponse();
        PhoneBookingDTO listDTO = phoneBookingModel.getBooking(id);
        sr.setStatus(true);
        sr.setData(listDTO);
        sr.setMessage(messageService.getMessage("driver_billing", listDTO.getId()));
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }
    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.PHONEBOOKING + Path.Url.VERSION_V1 + "/bookingStatus", method = RequestMethod.PUT, produces = {"application/json" })
    public ResponseEntity<StatusResponse> getBookingIdStauts(@RequestBody PhoneBookingsStatus PhoneBookingsStatus)  {
        StatusResponse sr = new StatusResponse();
        phoneBookingModel.update(PhoneBookingsStatus);
        sr.setStatus(true);
        sr.setData("succwss");
        sr.setMessage(messageService.getMessage("driver_billing", 1));
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }
}
