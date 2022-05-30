package com.luminn.firebase.controller;

import com.luminn.firebase.dto.*;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.model.TaxiModel;
import com.luminn.firebase.model.UserModel;
import com.luminn.firebase.request.AdvanceSearchDTO;
import com.luminn.firebase.request.SearchDTO;
import com.luminn.firebase.request.SearchsDTO;
import com.luminn.firebase.service.MessageByLocaleService;
import com.luminn.firebase.service.TaxiService;
import com.luminn.firebase.util.DRIVERSTATUS;
import com.luminn.firebase.util.Path;
import com.luminn.view.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/search")

public class SearchTaxiController {


    @Autowired
    private MessageByLocaleService messageService;

    @Autowired
    TaxiService taxiService;

    @Autowired
    UserModel userModel;

    @Autowired
    TaxiModel taxiModel;

    String message = null;

    @RequestMapping(value="/v1/image/search",method = RequestMethod.POST)
    private ResponseEntity<StatusResponse> getByGeoLocationAdd(@RequestBody SearchDTO searchDTO) {

        StatusResponse sr = new StatusResponse();
        List<TaxisDTO> result = null;
        result = this.taxiModel.getUsersImageByLocation(searchDTO);

        sr.setData(result);
        sr.setMessage("Taxi4:2 Taxi6:0 Transport:0 Taxi:2 Auto:0");
        sr.setStatus(true);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @RequestMapping(value="/v1/image/advanceSearch",method = RequestMethod.POST)
    private ResponseEntity<StatusResponse> getByGeoLocationAdvance(@RequestBody AdvanceSearchDTO searchDTO) {

        StatusResponse sr = new StatusResponse();
        List<TaxiPriceDTO> result = null;
        result = this.taxiModel.getUsersByLocationAdvance(searchDTO);

        sr.setData(result);
        sr.setMessage("Taxi4:2 Taxi6:0 Transport:0 Taxi:2 Auto:0");
        sr.setStatus(true);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @RequestMapping(value="/v2/image/advanceSearch",method = RequestMethod.POST)
    private ResponseEntity<StatusResponse> getByGeoLocationAdvanceV2(@RequestBody AdvanceSearchDTO searchDTO) {

        StatusResponse sr = new StatusResponse();
        List<TaxiCompanyDTO> result = null;
        result = this.taxiModel.getUsersByLocationAdvanceV2(searchDTO);

        sr.setData(result);
        sr.setMessage("Taxi4:2 Taxi6:0 Transport:0 Taxi:2 Auto:0");
        sr.setStatus(true);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @RequestMapping(value="/v3/image/advanceSearch",method = RequestMethod.POST)
    private ResponseEntity<StatusResponse> getByGeoLocationAdvanceV3(@RequestBody AdvanceSearchDTO searchDTO) {

        StatusResponse sr = new StatusResponse();
        List<TaxiCompanyDTO> result = null;
        result = this.taxiModel.getUsersByLocationAdvanceV3(searchDTO);

        //Long count = result.stream().filter(s-> s.getCarType().startsWith("Ta")).count();

        Integer[] evenNumbersArr = result.stream().filter(s-> s.getCarType().startsWith("Ta")).toArray(Integer[]::new);
        System.out.println(" evenNumbersArr--> " + evenNumbersArr.length);


        sr.setData(result);
        sr.setMessage("Taxi4:2 Taxi6:0 Transport:0 Taxi:2 Auto:0");
        sr.setStatus(true);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @RequestMapping(value="/v1/geo/search",method = RequestMethod.POST)
    private ResponseEntity<StatusResponse> getByGeoLocationsearch(@RequestBody SearchDTO searchDTO) {

        StatusResponse sr = new StatusResponse();
        List<TaxisDTO> result = null;
        result = this.taxiModel.getUsersGeoByLocation(searchDTO);

        sr.setData(result);
        sr.setMessage("Taxi4:2 Taxi6:0 Transport:0 Taxi:2 Auto:0");
        sr.setStatus(true);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }
    @RequestMapping(value="/v1/geo/waiting/search",method = RequestMethod.POST)
    private ResponseEntity<StatusResponse> getByGeoLocationStatus(@RequestBody SearchDTO searchDTO) {

        StatusResponse sr = new StatusResponse();
        String message = null;
        List<TaxisDTO> result = null;
        result = this.taxiModel.getUsersGeoByLocationstatus(searchDTO);
        sr.setData(result);

        message = getCount(result,message);

        if(result != null && result.size() > 0)
            System.out.println("count" + result.size());
            //log.info("count" + taxiViewList.size());
        else
            //log.info("Zero count" + taxiViewList)
            System.out.println("Zero count " + result);

        sr.setMessage(message);
        sr.setStatus(true);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @RequestMapping(value="/v1/geo/waiting/coupan/search",method = RequestMethod.POST)
    private ResponseEntity<StatusResponse> getByGeoLocationStatus(@RequestBody SearchsDTO searchDTO) {

        StatusResponse sr = new StatusResponse();
        String message = null;
        List<TaxisDTO> result = null;
        result = this.taxiModel.getUsersGeoByLocationstatus(searchDTO);
        sr.setData(result);

        message = getCount(result,message);

        if(result != null && result.size() > 0)
            System.out.println("count" + result.size());
            //log.info("count" + taxiViewList.size());
        else
            //log.info("Zero count" + taxiViewList)
            System.out.println("Zero count " + result);

        sr.setMessage(message);
        sr.setStatus(true);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    //https://howtodoinjava.com/java-8/how-to-use-predicate-in-java-8/
    private String getCount(List<TaxisDTO>  taxiView,String message){


        if(taxiView != null && taxiView.size() > 0){

            long taxi4 = 0;
            long taxi6 =0;
            long transport = 0;
            long Taxi = 0;
            long auto = 0;

            Predicate<TaxisDTO> predicate1 =   taxiModel.findTypeFromList("Taxi4");
            if(predicate1 != null){
                taxi4= taxiView.stream().filter(predicate1).count();
            //log.info("Total Taxi4 count ---->" + taxi4);
            }


            Predicate<TaxisDTO> predicate2 =   taxiModel.findTypeFromList("Taxi6");
            if(predicate2 != null)
                taxi6 = taxiView.stream().filter(predicate2).count();
            //log.info("Total Taxi6 coun2t ---->" + taxi6);


            Predicate<TaxisDTO> predicate3 =   taxiModel.findTypeFromList("Transport");
            if(predicate3 != null)
                transport= taxiView.stream().filter(predicate3).count();
            //log.info("Total transport coun3 ---->" + transport);


            Predicate<TaxisDTO> predicate4 =   taxiModel.findTypeFromList("Taxi");
            if(predicate4 != null)
                Taxi= taxiView.stream().filter(predicate4).count();
            //log.info("Total Taxi coun4 ---->" + Taxi);


            Predicate<TaxisDTO> predicate5 =   taxiModel.findTypeFromList("Auto");
            if(predicate5 != null)
                auto= taxiView.stream().filter(predicate5).count();
            //log.info("Total auto coun5 ---->" + auto);

            message = "Taxi4:" + String.valueOf(taxi4) + "  Taxi6:" + String.valueOf(taxi6) + " Transport:" +String.valueOf(transport)  + " Taxi:" + String.valueOf(Taxi)
                    + " Auto:" + String.valueOf(auto);

            //log.info("---" + message);
            return message;
        }
        else {
            message = "Taxi4:" + "0" + "  Taxi6:" + "0" + " Transport:" +"0"  + " Taxi:" + "0"    + " Auto:" + "0";
            return message;
        }


    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.TAXI + Path.Url.VERSION_V1 + Path.OperationUrl.STATUS+"/{taxiId}"+"/{userId}"+"/{latitude}"+"/{longitude}"+"/{driverStatus}", method = RequestMethod.PUT, produces = {"application/json" })
    public ResponseEntity<StatusResponse> statusOfTaxi(@PathVariable("taxiId") String taxiId,@PathVariable("userId") String userId, @PathVariable("latitude") float latitude, @PathVariable("longitude") float longitude, @PathVariable("driverStatus") String driverStatus)  {

        StatusResponse sr = new StatusResponse();

        if(userId == null){
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

}
