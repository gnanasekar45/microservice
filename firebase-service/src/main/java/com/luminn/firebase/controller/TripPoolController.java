package com.luminn.firebase.controller;

import com.luminn.firebase.dto.Taxi;
import com.luminn.firebase.dto.TripId;
import com.luminn.firebase.dto.TripPool;
import com.luminn.firebase.entity.RIDESTATUS;
import com.luminn.firebase.model.ModelStatus;
import com.luminn.firebase.model.PushNotificationRequests;
import com.luminn.firebase.model.TripPoolModel;
import com.luminn.firebase.request.TripPoolRequest;
import com.luminn.firebase.service.TripPoolService;
import com.luminn.view.StatusResponse;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/trip")
public class TripPoolController {

    @Autowired
    TripPoolService tripPool;


    @Autowired
    TripPoolModel tripModel;



    private static final Logger log = LoggerFactory.getLogger(TripPoolController.class);

    @PostMapping("/v1/add")
    private ResponseEntity<StatusResponse> addTrip(@RequestBody PushNotificationRequests tripPool) {
        List<Taxi> result = null;
        String uniqueID = this.tripPool.addTrip(tripPool);
        StatusResponse sr = new StatusResponse();
        sr.setData(uniqueID);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @PostMapping("/v1/update")
    private void update(@RequestBody TripPool tripPool) {
        List<Taxi> result = null;
        this.tripPool.updateTrip(tripPool);
    }

    @GetMapping("/v1/get/date/{token}")
    private ResponseEntity<StatusResponse> getDate(@RequestParam String token) {
        List<TripPool> result = null;
        Document document =  this.tripPool.findDate(token);
        StatusResponse sr = new StatusResponse();
        sr.setData(document);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @GetMapping("/v1/get/tripId/{tripId}")
    private ResponseEntity<StatusResponse> getUnique(@RequestParam String tripId) {
        List<TripPool> result = null;
        Document document =  this.tripPool.findTripId(tripId);
        StatusResponse sr = new StatusResponse();
        sr.setData(document);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }
    @GetMapping("/v1/get/tripId/{tripId}/{status}")
    private ResponseEntity<StatusResponse> getUnique(@RequestParam String tripId,@RequestParam String status) {
        List<TripPool> result = null;
        Document document =  this.tripPool.findTripId(tripId,status);
        StatusResponse sr = new StatusResponse();
        sr.setData(document);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    public boolean getStatusofTrip(String status){
        if(status != null && status.equalsIgnoreCase(RIDESTATUS.ACCEPTED.name())){
            return true;
        }
        log.info("Different status");
        return false;

    }

    /*@PutMapping("/v1/update/tripId/{tripId}/{status}")
    private ResponseEntity<StatusResponse> updatetripId(@RequestBody TripPool tripPool) {

        List<TripPool> result = null;
        Document document = new Document();
        StatusResponse sr = new StatusResponse();
        ModelStatus modelstatus = null;


        if(getStatusofTrip(tripPool.getStatus())) {

            modelstatus = this.tripModel.update(tripPool.getTripId(), tripPool.getStatus());
            if(modelstatus.name().equalsIgnoreCase(ModelStatus.SUCCESS.name())) {
                sr.setStatus(true);
                sr.setMessage("Success -->"  + modelstatus.name());
            }
            else {
                sr.setMessage("Failure -->" + modelstatus.name());
            }
        }
        else {
            document = null;
            sr.setMessage("Trip is cancelled by driver -->" + tripPool.getStatus());
        }
        sr.setData(document);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }*/

    @PutMapping("/v1/update/tripId/driverId")
    private ResponseEntity<StatusResponse> updatetripIds(@RequestBody TripPoolRequest tripPool) {

        List<TripPool> result = null;
        Document document = new Document();
        StatusResponse sr = new StatusResponse();
        ModelStatus modelstatus = null;


        if(getStatusofTrip(tripPool.getStatus())) {

            modelstatus = this.tripModel.update(tripPool);
            if(modelstatus.name().equalsIgnoreCase(ModelStatus.SUCCESS.name())) {
                //call notificatio
                sr.setStatus(true);
                sr.setMessage("Success -->"  + modelstatus.name());
            }
            else {
                sr.setMessage("Failure -->" + modelstatus.name());
            }
        }
        else {
            document = null;
            sr.setMessage("Trip is cancelled by driver -->" + tripPool.getStatus());
        }
        sr.setData(document);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    //updateTripId

    @GetMapping("/v1/get/{token}")
    private ResponseEntity<StatusResponse> getTrips(@RequestParam String token) {
        List<TripPool> result = null;
        Document document =  this.tripPool.getToken(token);
        StatusResponse sr = new StatusResponse();
        sr.setData(document);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @GetMapping("/v1/get/status/{status}")
    private ResponseEntity<StatusResponse> getTrip(@RequestParam String status) {
        List<TripPool> result = null;
        Document docuemnt =  this.tripPool.getStatus(status);
        StatusResponse sr = new StatusResponse();
        sr.setData(docuemnt);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }
}
