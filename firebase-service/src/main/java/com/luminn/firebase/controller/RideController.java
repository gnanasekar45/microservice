package com.luminn.firebase.controller;

import com.luminn.firebase.dto.*;
import com.luminn.firebase.entity.RIDESTATUS;
import com.luminn.firebase.entity.Ride;
import com.luminn.firebase.helper.Converter;
import com.luminn.firebase.model.ModelStatus;
import com.luminn.firebase.model.PriceMap;
import com.luminn.firebase.model.RideModel;
import com.luminn.firebase.repository.RideRepository;
import com.luminn.firebase.service.MessageByLocaleService;
import com.luminn.firebase.service.RideService;
import com.luminn.firebase.util.ErrorNumber;
import com.luminn.firebase.util.Path;
import com.luminn.view.StatusModelResponse;
import com.luminn.view.StatusResponse;
import com.luminn.view.StatusResponseJson;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/ride")
public class RideController {

    @Autowired
    RideService rideService;

    @Autowired
    RideRepository rideRepository;

    @Autowired
    RideModel rideModel;

    @Autowired
    private MessageByLocaleService messageService;

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(RideController.class);

    @PostMapping("/v1/add")
    private void addDelivery(@RequestBody RideDTO rideDTO) {
        this.rideService.save(rideDTO);
    }

    @PostMapping("/v1/dummy/add")
    private ResponseEntity<StatusResponse> addDummyTest(@RequestBody ShortRide rideDTO) {

        ModelStatus status = null;

        StatusModelResponse statusResponse = this.rideModel.create(rideDTO,"USER");
        status = statusResponse.getStatus();

        StatusResponse sr = new StatusResponse();

        if (status.equals(ModelStatus.PRECANCEL_BY_USER)) {
            sr.setData(rideModel.globalRideId);
            sr.setMessage(messageService.getMessage("ride_created", statusResponse.getId()));
            sr.setStatus(true);
            sr.setInfoId(ErrorNumber.RIDE_CREATE);
        }
        else if (status.equals(ModelStatus.CREATED)) {
            sr.setData(rideModel.getGlobalRideId());
            sr.setMessage(messageService.getMessage("ride_created", statusResponse.getId()));
            sr.setStatus(true);
            sr.setInfoId(ErrorNumber.RIDE_CREATE);
        } else if (status.equals(ModelStatus.USER_IS_NOT_EXISTING)) {
            sr.setMessage(messageService.getMessage("user_id_not_existing"));
            sr.setStatus(false);
        } else if (status.equals(ModelStatus.EXCEPTION)) {
            sr.setMessage(messageService.getMessage("ride_not_existing"));
            sr.setStatus(false);
        }
        return new ResponseEntity<StatusResponse>(sr, HttpStatus.OK);
    }

    @PostMapping("/v2/dummy/add")
    private ResponseEntity<StatusResponse> addDummyComment(@RequestBody ShortRides rideDTO) {

        ModelStatus status = null;

        StatusModelResponse statusResponse = this.rideModel.createComment(rideDTO,"USER");
        status = statusResponse.getStatus();

        StatusResponse sr = new StatusResponse();

        if (status.equals(ModelStatus.PRECANCEL_BY_USER)) {
            sr.setData(rideModel.globalRideId);
            sr.setMessage(messageService.getMessage("ride_created", statusResponse.getId()));
            sr.setStatus(true);
            sr.setInfoId(ErrorNumber.RIDE_CREATE);
        }
        else if (status.equals(ModelStatus.CREATED)) {
            sr.setData(rideModel.getGlobalRideId());
            sr.setMessage(messageService.getMessage("ride_created", statusResponse.getId()));
            sr.setStatus(true);
            sr.setInfoId(ErrorNumber.RIDE_CREATE);
        } else if (status.equals(ModelStatus.USER_IS_NOT_EXISTING)) {
            sr.setMessage(messageService.getMessage("user_id_not_existing"));
            sr.setStatus(false);
        } else if (status.equals(ModelStatus.EXCEPTION)) {
            sr.setMessage(messageService.getMessage("ride_not_existing"));
            sr.setStatus(false);
        }
        return new ResponseEntity<StatusResponse>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V1 + "/PriceCalculation/gps/", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<StatusResponse> updateRidesFare(@RequestBody RideRequest rideRequest) {
        float price = 0;
        StatusResponse sr = new StatusResponse();
        List<RideDTO> rideDTO = null;
        String rideId = rideRequest.getRideId();
        float distance = rideRequest.getDistance();
        float googleKm = rideRequest.getGoogleKm();
        String region = rideRequest.getRegion();
        String status = rideRequest.getStatus();
        String domain = rideRequest.getDomain();
        float travelTime = rideRequest.getTravelTime();
        float waitingTIme = rideRequest.getWaitingTime();

        if(googleKm > 5)
            distance = getBestKm(distance,googleKm);



        String category = rideRequest.getCategory();

        if("".equalsIgnoreCase(region) || region == null ){
            region = "ZURICH";
        }
        //check category

        PriceMap map = rideModel.updateCalculation(category,rideRequest.getType(),rideRequest.getRegion(),0,distance,waitingTIme,
                "CASH",rideRequest.getDiscount(),domain,rideRequest.getElapsedTime(),travelTime,null,null);
        rideModel.setJsonValue(null,map);
        //sr.setData(rideModel.getJsonValue());
        sr.setData(map);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V1 + "/PriceCalculation/coupan/gps/", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<StatusResponse> updateRidesFareCoupan(@RequestBody RidesRequest rideRequest) {
        float price = 0;
        StatusResponse sr = new StatusResponse();
        List<RideDTO> rideDTO = null;
        String rideId = rideRequest.getRideId();
        float distance = rideRequest.getDistance();
        float googleKm = rideRequest.getGoogleKm();
        String region = rideRequest.getRegion();
        String status = rideRequest.getStatus();
        String domain = rideRequest.getDomain();
        float travelTime = rideRequest.getTravelTime();
        float waitingTIme = rideRequest.getWaitingTime();

        if(googleKm > 5)
            distance = getBestKm(distance,googleKm);



        String category = rideRequest.getCategory();

        if("".equalsIgnoreCase(region) || region == null ){
            region = "ZURICH";
        }
        //check category


        PriceMap map = rideModel.updateCalculation(category,rideRequest.getType(),rideRequest.getRegion(),0,distance,waitingTIme,
                "CASH",0,domain,rideRequest.getElapsedTime(),travelTime,null,rideRequest.getCoupan());
        rideModel.setJsonValue(null,map);
        //sr.setData(rideModel.getJsonValue());
        sr.setData(map);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }


    private float getBestKm(float distance,float googleKm){
        if(distance > 0) {
            if(distance > 700)
                return googleKm;
            else return distance;
        }
        else return googleKm;
    }
    public JSONObject getJsonValue(Ride ride,float km){

        System.out.println(" JSON " + ride.getId() + "KM " + ride.getKm());

        JSONObject json = new JSONObject();
        json.put("Km",km);
        json.put("Total Price",new Float(ride.getPrice()));
        json.put("User Total Price",new Float(ride.getUserTotalPrice()));
        json.put("Base Price",new Float(ride.getBasePrice()));
        //json.put("Total Time",ride.getEstimateTime());
        json.put("Total Time",new Float(ride.getTotalwaitTime()));
        json.put("TotalTravelTimePrice ",new Float(ride.getTotalTravelTimePrice()));
        log.info(" json jobject" + json);

        //StringWriter out = new StringWriter();
        //return json.write(out);
        //String jsonText = out.toString();
        return json;
    }

    public JsonDTO getJsonValueDTO(Ride ride,float km){

        System.out.println(" JSON " + ride.getId() + "KM " + ride.getKm());

        JsonDTO dto = new JsonDTO();
        dto.setKm(km);
        JSONObject json = new JSONObject();

        dto.setTotalPrice(ride.getPrice());

        dto.setUserTotalPrice(ride.getUserTotalPrice());
        dto.setBasePrice(ride.getBasePrice());

        dto.setTotalTime(ride.getTotalwaitTime());
        dto.setTotalTravelTimePrice(ride.getTotalTravelTimePrice());
        log.info(" json jobject" + json);
        return dto;
    }


    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.VERSION_V1 + Path.OperationUrl.GET + "/check/id/{driverId}", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<StatusResponse> getDriverRideId(@PathVariable("driverId") String driverId)  {
        StatusResponse sr = new StatusResponse();
        Long rideId = 1l;
        List<RideDTO> rideDTO = null;
        log.info(" *** ---driverId ---" + driverId);
        if (driverId != null && rideModel.getUserRole(driverId) != null) {
            rideDTO = rideModel.getRideIdByDTO(driverId);
        }
        sr.setData(rideDTO);
        sr.setMessage(messageService.getMessage("ride_updated", rideModel.globalRideId));
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.VERSION_V1 + Path.OperationUrl.GET + "/count/{driverId}", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<StatusResponse> getDriverCount(@PathVariable("driverId") String driverId)  {
        StatusResponse sr = new StatusResponse();
        Long rideId = 1l;
        List<RideDTO> rideDTO = null;
        //HashMap<String,TripCount> result =

        List<TripCounts> counts = new ArrayList<>();
        log.info(" *** ---driverId ---" + driverId);
        if (driverId != null && rideModel.getUserRole(driverId) != null) {
            sr.setData(rideModel.getRideCount(driverId));
        }
        else
         sr.setData(counts);
        sr.setMessage(messageService.getMessage("ride_updated", rideModel.globalRideId));
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.VERSION_V1 + Path.OperationUrl.GET + "/count/supplier/{supplierId}", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<StatusResponse> getDriverCountbySupplier(@PathVariable("supplierId") String supplierId)  {
        StatusResponse sr = new StatusResponse();
        Long rideId = 1l;
        List<RideDTO> rideDTO = null;
        HashMap<String,TripCount> result =  new HashMap<String,TripCount>();;
        log.info(" *** ---driverId ---" + supplierId);
        if (supplierId != null && rideModel.getUserRole(supplierId) != null) {
            result = rideModel.getAllDriversBySupplierCount(supplierId,null);
        }
        sr.setData(result);
        sr.setMessage(messageService.getMessage("ride_updated", rideModel.globalRideId));
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.VERSION_V1 + "/user" + Path.OperationUrl.GET + "/{userId}", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<StatusResponse> getUser(@PathVariable("userId") String userId) {
        StatusResponse sr = new StatusResponse();
        List<RideDTO> rideDTO = null;


        if (userId != null  && rideModel.getUserRole(userId) != null && rideModel.getUserRole(userId).equalsIgnoreCase(Path.ROLE.USER)) {
            rideDTO = rideModel.getAllDriversByUser(userId, null);
            sr.setStatus(true);
            sr.setMessage("SUCCESS");
            sr.setInfoId(ErrorNumber.USER_TRIP_LIST);
        }
        else if(userId != null){
            rideDTO = rideModel.getAllDriversByUser(userId, null);
            sr.setStatus(true);
            sr.setData(rideDTO);
            sr.setMessage("SUCCESS");
            sr.setInfoId(ErrorNumber.USER_TRIP_LIST);
        }
        else {
            sr.setMessage(messageService.getMessage("taxi_ids_not_valid", userId));
            sr.setInfoId(ErrorNumber.USER_TRIP_LIST_NO_USER);
        }
        sr.setData(rideDTO);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    /*@GetMapping("/v1/get/all")
    private ResponseEntity<StatusResponse> getRideAll(@RequestParam String id) {

        List<Ride> result = null;
        StatusResponse sr = new StatusResponse();
        // return result;
        sr.setData(this.rideService.getAll());
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }*/

    @PostMapping("/v1/update")
    private void updateRide(@RequestBody RideDTO rideDTO) {
        this.rideService.updateName(rideDTO.getId(),rideDTO.getDesc());
    }

    public float validateKm(float distance,float googleKm){

        distance = getBestKm(distance,googleKm);

        if(distance == googleKm){
            log.info("correct google & distance are  equal-->" +distance);
            return distance;
        }
        else if(distance + 2 >  googleKm  && googleKm < 700) {
            log.info("almost correct distance(+2 added)  and taking google km  -->" +distance);
            return  Math.max(distance, googleKm);
        }
        else if(googleKm > distance && googleKm < 700) {
            log.info("something , km issue..correct google km-->" +distance);
             return  Math.max(distance, googleKm);
        }
        else if(distance > 700 || googleKm > 700  ){
            log.info("some thing wrong in km-->" + distance);
            distance = 100;
        }
        return distance;
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V2 + "/rideUpdateFinished/gps/", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<StatusResponse> updateRidesBillingCoupan(@RequestBody RidesRequest rideRequest)  {
        float price = 0;
        StatusResponseJson sr = new StatusResponseJson();
        List<RideDTO> rideDTO = null;
        String rideId = rideRequest.getRideId();
        float distance = rideRequest.getDistance();
        float googleKm = rideRequest.getGoogleKm();
        String region = rideRequest.getRegion();
        String status = rideRequest.getStatus();
        float travelTime = rideRequest.getTravelTime();
        String coupan =rideRequest.getCoupan();

        log.info("STATUS rideId -->" + rideRequest.getRideId() + " distance.." + distance + "googleKm ---" + googleKm  + " Waiting time" +
                rideRequest.getWaitingTime() + " elaspsed time" + rideRequest.getElapsedTime() + " region  " + region);

        distance = validateKm(distance,googleKm);

        if (rideId == null ) {
            sr.setMessage(messageService.getMessage("ride_not_existing", rideId));
            sr.setStatus(false);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
        if (price > 1000) {

            sr.setMessage(messageService.getMessage("ride_id_price_limit", price));
            sr.setStatus(false);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
        if(distance > googleKm + 2000){
            sr.setMessage(messageService.getMessage("ride_id_km_limit_google", googleKm));
            sr.setStatus(false);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }

        if("".equalsIgnoreCase(region) || region == null ){
            region = "ZURICH";
        }
        ModelStatus resStatus = rideModel.rideUpdate(rideRequest.getCategory(),rideId, distance, price,rideRequest.getElapsedTime(),rideRequest.getWaitingTime(),
                region,status,rideRequest.getLatitude(),rideRequest.getLongitude(),rideRequest.getSourceLatitude(),rideRequest.getSourceLongitude(),travelTime,0,coupan);
        log.info("STATUS rideId --" + resStatus);
        if (resStatus.name().equals(ModelStatus.RIDE_UPDATE.name())) {
            sr.setMessage(Path.MESSAGE.SUCCESS);
            //sr.setData(setJsonValue(rideModel.getRide(rideId),distance).toString());
            //setJsonValueDTO
            //JSONArray jsonArray = new JSONArray();
            JSONObject objects = getJsonValue(rideModel.getRide(rideId),distance);
            //jsonArray.put(objects);
            List<JsonDTO> list = new ArrayList<>();
            list.add(getJsonValueDTO(rideModel.getRide(rideId),distance));
            sr.setData(list);

            sr.setInfoId(ErrorNumber.RIDE_UPDATE);
            sr.setStatus(true);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        } else if (resStatus.name().equals(ModelStatus.RIDE_EXCEPTION.name())) {
            sr.setMessage(Path.MESSAGE.FAILURE);
            //sr.setData(messageService.getMessage("user_id_not_existing"));

            //CK
            //sr.setData(rideModel.getJsonValue());
            sr.setInfoId(ErrorNumber.RIDE_EXCEPTION);
            sr.setStatus(false);
        }
        if("".equalsIgnoreCase(sr.getMessage()))
            sr.setData(Path.MESSAGE.FAILURE);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V1 + "/rideUpdateFinished/gps/", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<StatusResponse> updateRidesBillingL(@RequestBody RideRequest rideRequest)  {
        float price = 0;
        StatusResponseJson sr = new StatusResponseJson();
        List<RideDTO> rideDTO = null;
        String rideId = rideRequest.getRideId();
        float distance = rideRequest.getDistance();
        float googleKm = rideRequest.getGoogleKm();
        String region = rideRequest.getRegion();
        String status = rideRequest.getStatus();
        float travelTime = rideRequest.getTravelTime();


        log.info("STATUS rideId -->" + rideRequest.getRideId() + " distance.." + distance + "googleKm ---" + googleKm  + " Waiting time" +
                rideRequest.getWaitingTime() + " elaspsed time" + rideRequest.getElapsedTime() + " region  " + region);

        distance = validateKm(distance,googleKm);

        if (rideId == null ) {
            sr.setMessage(messageService.getMessage("ride_not_existing", rideId));
            sr.setStatus(false);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
        if (price > 1000) {

            sr.setMessage(messageService.getMessage("ride_id_price_limit", price));
            sr.setStatus(false);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
        if(distance > googleKm + 2000){
            sr.setMessage(messageService.getMessage("ride_id_km_limit_google", googleKm));
            sr.setStatus(false);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }

        if("".equalsIgnoreCase(region) || region == null ){
            region = "ZURICH";
        }
        ModelStatus resStatus = rideModel.rideUpdate(rideRequest.getCategory(),rideId, distance, price,rideRequest.getElapsedTime(),rideRequest.getWaitingTime(),
                region,status,rideRequest.getLatitude(),rideRequest.getLongitude(),rideRequest.getSourceLatitude(),rideRequest.getSourceLongitude(),travelTime,0,null);
        log.info("STATUS rideId --" + resStatus);
        if (resStatus.name().equals(ModelStatus.RIDE_UPDATE.name())) {
            sr.setMessage(Path.MESSAGE.SUCCESS);
            //sr.setData(setJsonValue(rideModel.getRide(rideId),distance).toString());
            //setJsonValueDTO
            //JSONArray jsonArray = new JSONArray();
            JSONObject objects = getJsonValue(rideModel.getRide(rideId),distance);
            //jsonArray.put(objects);
            List<JsonDTO> list = new ArrayList<>();
            list.add(getJsonValueDTO(rideModel.getRide(rideId),distance));
            sr.setData(list);

            sr.setInfoId(ErrorNumber.RIDE_UPDATE);
            sr.setStatus(true);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        } else if (resStatus.name().equals(ModelStatus.RIDE_EXCEPTION.name())) {
            sr.setMessage(Path.MESSAGE.FAILURE);
            //sr.setData(messageService.getMessage("user_id_not_existing"));

            //CK
            //sr.setData(rideModel.getJsonValue());
            sr.setInfoId(ErrorNumber.RIDE_EXCEPTION);
            sr.setStatus(false);
        }
        if("".equalsIgnoreCase(sr.getMessage()))
            sr.setData(Path.MESSAGE.FAILURE);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }



    @ResponseBody
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V1 + "/supplier" + Path.OperationUrl.GET + "/{supplierId}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
    public ResponseEntity<StatusResponse> getAllDriverBySupplier(@PathVariable("supplierId") String supplierId)  {
        StatusResponse sr = new StatusResponse();
        List<RideDTO> rideDTO = null;
        if (supplierId != null  && rideModel.getUserRole(supplierId) != null ) {
            rideDTO = rideModel.listBySupplierId(supplierId);
            sr.setStatus(true);
            sr.setMessage("SUCCESS");
        }
        else {
            sr.setStatus(false);
            sr.setMessage(messageService.getMessage("taxi_ids_not_valid",supplierId));
        }
        sr.setData(supplierId);
        sr.setData(rideDTO);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V1 + "/supplier" + Path.OperationUrl.GET + "/{supplierId}/{status}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
    public ResponseEntity<StatusResponse> getAllDriverBySupplierStatus(@PathVariable("supplierId") String supplierId,
                                                                       @PathVariable("status") String status)  {
        StatusResponse sr = new StatusResponse();
        List<RideDTO> rideDTO = null;
        if (supplierId != null  && rideModel.getUserRole(supplierId) != null ) {
            rideDTO = rideModel.listBySupplierIdStatus(supplierId,status);
            sr.setStatus(true);
            sr.setMessage("SUCCESS");
        }
        else {
            sr.setStatus(false);
            sr.setMessage(messageService.getMessage("taxi_ids_not_valid",supplierId));
        }
        sr.setData(supplierId);
        sr.setData(rideDTO);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V1 + "/date" + Path.OperationUrl.GET + "/{supplierId}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
    public ResponseEntity<StatusResponse> getAllDriverBycreationDate(@PathVariable("supplierId") String supplierId)  {
        StatusResponse sr = new StatusResponse();
        List<RideDTO> rideDTO = null;
        if (supplierId != null ) {
            rideDTO = rideModel.listByCreationDate(supplierId);
            sr.setStatus(true);
            sr.setMessage("SUCCESS");
        }
        else {
            sr.setStatus(false);
            sr.setMessage(messageService.getMessage("taxi_ids_not_valid",supplierId));
        }
        sr.setData(supplierId);
        sr.setData(rideDTO);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }


    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.RIDE + Path.Url.VERSION_V1 + "/invoice" + "/{rideId}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
    public ResponseEntity<StatusResponse> rideinVoice(@PathVariable("rideId") String rideId) throws IOException {
        StatusResponse sr = new StatusResponse();
        List<RideDTO> rideDTO = null;

        if (rideId == null ) {
            sr.setMessage(messageService.getMessage("ride_not_existing"));
            sr.setStatus(false);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        } else {

            sr.setData(rideModel.getRideId(rideId));
            rideModel.sendEmail(rideId);
            sr.setMessage(new Date().toString());
            sr.setStatus(true);
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.RIDE + Path.Url.VERSION_V1 + "/driver" + Path.OperationUrl.GET + "/{driverId}", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<StatusResponse> getDriver(@PathVariable("driverId") String driverId)  {
        StatusResponse sr = new StatusResponse();
        List<RideDTO> rideDTO = null;

        log.info(" *** ---driverId ---" + driverId);

        if (rideModel.getUserRole(driverId) != null && rideModel.getUserRole(driverId).equalsIgnoreCase(Path.ROLE.DRIVER)) {
            sr.setStatus(true);
            sr.setInfoId(500);
            rideDTO = rideModel.getAllUsersByDriver(driverId, null);
        }

        //sr.setData(rideModel.rideId);
        sr.setData(rideDTO);
        sr.setMessage(Path.MESSAGE.SUCCESS);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }



}
