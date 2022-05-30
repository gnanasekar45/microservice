package com.luminn.firebase.controller;

import com.luminn.firebase.dto.Taxi;
import com.luminn.firebase.dto.TaxiDTO;
import com.luminn.firebase.dto.TaxiSearchDTO;
import com.luminn.firebase.dto.TaxisDTO;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.model.TaxiModel;
import com.luminn.firebase.model.UserModel;
import com.luminn.firebase.repository.TaxiRepository;
import com.luminn.firebase.service.MessageByLocaleService;
import com.luminn.firebase.service.TaxiService;
import com.luminn.firebase.util.DRIVERSTATUS;
import com.luminn.firebase.util.Path;
import com.luminn.view.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/taxi")

public class TaxiController {


    @Autowired
    private MessageByLocaleService messageService;

    @Autowired
    TaxiService taxiService;

    @Autowired
    UserModel userModel;

    @Autowired
    TaxiModel taxiModel;

    @Autowired
    TaxiRepository taxiRepository;

    @RequestMapping(value="/v1/search",method = RequestMethod.GET)
    private ResponseEntity<StatusResponse> getUsersByLocationAdd(@RequestParam float latitude, @RequestParam float longitude, @RequestParam float distance) {

        StatusResponse sr = new StatusResponse();
        List<TaxiSearchDTO> result = null;
        result = this.taxiService.getUsersByLocation(latitude,longitude,distance);

        sr.setData(result);
        sr.setStatus(true);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

   /* @RequestMapping(value="/v1/image/search",method = RequestMethod.GET)
    private ResponseEntity<StatusResponse> getByGeoLocationAdd(@RequestParam double latitude, @RequestParam double longitude, @RequestParam double distance) {

        StatusResponse sr = new StatusResponse();
        List<TaxisDTO> result = null;
        result = this.taxiService.getUsersImageByLocation(latitude,longitude,distance);

        sr.setData(result);
        sr.setStatus(true);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }*/

    @RequestMapping(value="/v1/find",method = RequestMethod.GET)
    private void getByLocationAdd3(@RequestParam double longitude, @RequestParam double latitude, @RequestParam double distance) {
        List<Taxi> result = null;
        this.taxiService.findNearest(74.7774223, 20.833993,10);
        //return aggregationOperation.;
    }

    @RequestMapping(value="/v1/find/token",method = RequestMethod.GET)
    private void getByLocation4(@RequestParam String token) {
        List<Taxi> result = null;
        this.taxiService.findToken(token);
        //return aggregationOperation.;
    }

    @RequestMapping(value="/v1/find/{id}",method = RequestMethod.GET)
    private Taxi  getById(@RequestParam String id) {
        List<Taxi> result = null;

        Taxi taxis =  taxiService.findId(id,null);

        if(taxis != null)
            return taxis;

        Optional<Taxi> taxi = this.taxiRepository.findById(id);
        //return aggregationOperation.;
        if(taxi.isPresent())
           return taxi.get();
        else
            return new Taxi();


    }

    @RequestMapping(value="/v1/get/token/{token}",method = RequestMethod.GET)
    private Taxi getByToken(@RequestParam String token) {
        List<Taxi> result = null;
        return this.taxiService.getToken(token);
        //return aggregationOperation.;
    }





    @RequestMapping(value="/v1/taxiDetail/add",method = RequestMethod.POST)
    private void addTaxiandTaxiDetail(@RequestBody Taxi delivery) {
        List<Taxi> result = null;
        this.taxiService.addTaxiDetails(delivery);
        // return result;
    }


    @RequestMapping(value="/v1/update",method = RequestMethod.POST)
    private void updateDelivery(@RequestBody Taxi delivery) {
        List<Taxi> result = null;
        this.taxiService.updateDriver(delivery);
        // return result;
    }


    @RequestMapping(value="/v1/update/token",method = RequestMethod.POST)
    private void updateName(@RequestBody Taxi taxi) {
        List<Taxi> result = null;
        //this.taxiService.updateName(taxi.getId(),taxi.getName());
        this.taxiService.updateName(taxi.getName(),taxi.getToken());
        // return result;
    }

    @RequestMapping(value="/v1/update/status",method = RequestMethod.POST)
    private void updateStatus(@RequestBody Taxi taxi) {
        List<Taxi> result = null;
        //this.taxiService.updateName(taxi.getId(),taxi.getName());
        this.taxiService.updateStatus(taxi.getName(),taxi.getToken());
        // return result;
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.VERSION_V1 + Path.OperationUrl.STATUS+"/{taxiId}"+"/{userId}"+"/{latitude}"+"/{longitude}"+"/{driverStatus}", method = RequestMethod.PUT, produces = {"application/json" })
    public ResponseEntity<StatusResponse> statusOfTaxi(@PathVariable("taxiId") String taxiId,@PathVariable("userId") String userId, @PathVariable("latitude") float latitude, @PathVariable("longitude") float longitude, @PathVariable("driverStatus") String driverStatus)  {

        StatusResponse sr = new StatusResponse();



        if(taxiId == null || userId == null){
            sr.setMessage(messageService.getMessage("user_is_not_existing",userId));
            sr.setInfoId(600);
            //sr.setData(status.name());
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
        if (taxiId == null){
            sr.setMessage(messageService.getMessage("taxi_id_not_valid", taxiId));
            sr.setData(false);
            sr.setStatus(false);
            sr.setInfoId(600);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }


        User user  = userModel.validate(userId);
        if(user == null) {
            sr.setMessage(messageService.getMessage("user_is_not_existing",userId));
            //sr.setData(status.name());
            sr.setInfoId(600);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }

        DRIVERSTATUS status = taxiModel.addGeoLocationWithStatus(user,taxiId,latitude,longitude,driverStatus);

        if(!status.equals(DRIVERSTATUS.NOTEXISTS)) {

            sr.setMessage(messageService.getMessage("driver_status",status ));
            sr.setData(status.name());
            sr.setStatus(true);
            sr.setInfoId(500);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }

        else {
            sr.setMessage(messageService.getMessage("taxi_id_not_valid", taxiId));
            sr.setData(status.name());
            sr.setStatus(false);
            sr.setInfoId(600);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
    }
    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.TAXI + Path.Url.VERSION_V1 + Path.Url.DETAILS+"/{taxiId}", method = RequestMethod.GET,produces = { "application/json" })
    public ResponseEntity<StatusResponse> taxiDetailsPage(@PathVariable("taxiId") String taxiId)  {
        StatusResponse sr = new StatusResponse();
        TaxiDTO taxiDTO = taxiModel.getTaxiDetail(taxiId);
        if(taxiDTO.getTaxiId() != null) {
            sr.setMessage(Path.MESSAGE.SUCCESS);
            sr.setStatus(true);
            sr.setMessage(messageService.getMessage("success"));
            sr.setData(taxiDTO);
            sr.setInfoId(500);
        }
        else {
            sr.setMessage(Path.MESSAGE.FAILURE);
            sr.setStatus(false);
            sr.setMessage(messageService.getMessage("taxi_id_not_valid"));
            sr.setData(taxiDTO);
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }
}
