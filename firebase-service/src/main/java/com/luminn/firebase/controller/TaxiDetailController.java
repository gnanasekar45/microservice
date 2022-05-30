package com.luminn.firebase.controller;

import com.luminn.firebase.dto.*;
import com.luminn.firebase.entity.TaxiDetail;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.model.ModelStatus;
import com.luminn.firebase.model.TaxiDetailModel;
import com.luminn.firebase.model.TaxiModel;
import com.luminn.firebase.model.UserModel;
import com.luminn.firebase.repository.TaxiDetailRepository;
import com.luminn.firebase.request.AccountRequest;
import com.luminn.firebase.service.MessageByLocaleService;
import com.luminn.firebase.service.TaxiDetailService;
import com.luminn.firebase.util.ErrorNumber;
import com.luminn.firebase.util.Path;
import com.luminn.view.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/taxiDetail")
public class TaxiDetailController {

    @Autowired
    TaxiDetailService taxiDetailService;

    @Autowired
    TaxiDetailRepository taxiDetailRepository;

    @Autowired
    private MessageByLocaleService messageService;

    @Autowired
    TaxiModel taxiModel;

    @Autowired
    TaxiDetailModel taxiDetailModel;
    @RequestMapping(value="/v1/search",method = RequestMethod.GET)
    private List<Taxi> getUsersByLocationAdd(@RequestParam double latitude, @RequestParam double longitude, @RequestParam double distance) {
        List<Taxi> result = null;
       // result = this.taxiService.getUsersByLocation(latitude,longitude,distance);
        return result;
    }
    @RequestMapping(value="/v1/find",method = RequestMethod.GET)
    private void getByLocationAdd3(@RequestParam double longitude, @RequestParam double latitude, @RequestParam double distance) {
        List<Taxi> result = null;
        //this.taxiService.findNearest(74.7774223, 20.833993,10);
        //return aggregationOperation.;
    }

    @RequestMapping(value="/v1/find/token",method = RequestMethod.GET)
    private void getByLocation4(@RequestParam String token) {
        List<Taxi> result = null;
        //this.taxiService.findToken(token);
        //return aggregationOperation.;
    }

    @RequestMapping(value="/v1/get/token/{token}",method = RequestMethod.GET)
    private List<TaxiDetail> getByToken(@RequestParam String token) {

        List<TaxiDetail> result = new ArrayList<TaxiDetail>();
        //return this.taxiService.getToken(token);
        //return aggregationOperation.;
        return result;
    }


    @RequestMapping(value="/v1/add",method = RequestMethod.POST)
    private void addDelivery(@RequestBody TaxiDetail delivery) {
        List<Taxi> result = null;
        this.taxiDetailService.addTaxiDetails(delivery);
        // return result;
    }

    @RequestMapping(value="/v1/get/taxiid/{taxiId}",method = RequestMethod.GET)
    private ResponseEntity<StatusResponse> getByTokenId(@PathVariable("taxiId") String taxiId) {

        StatusResponse sr = new StatusResponse();
        List<TaxiDetail> result = new ArrayList<TaxiDetail>();
        sr.setData(this.taxiDetailService.findTaxiId(taxiId));
        sr.setStatus(true);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @RequestMapping(value="/v1/add/account",method = RequestMethod.POST)
    private ResponseEntity<StatusResponse> addAccount(@RequestBody AccountRequest accountRequest) {

        StatusResponse sr = new StatusResponse();
        List<TaxiDetail> result = new ArrayList<TaxiDetail>();
        boolean success = this.taxiDetailService.updateAccount(accountRequest.getTaxiId(),accountRequest.getAccountNumber());
                if(success) {
                    sr.setData(success);
                    sr.setStatus(true);
                }
                else {
                    sr.setStatus(false);
                }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @RequestMapping(value="/v1/get/account/{taxiId}",method = RequestMethod.GET)
    private ResponseEntity<StatusResponse> addAccount(@PathVariable("taxiId") String taxiId) {

        StatusResponse sr = new StatusResponse();
        TaxiDetail td = taxiDetailService.findTaxiId(taxiId);
        if(td != null && td.getAccount() != null) {
            sr.setData(td.getAccount());
            sr.setStatus(true);
        }
        else {
            sr.setStatus(false);
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @RequestMapping(value="/v1/get/supplierId/{supplierId}",method = RequestMethod.GET)
    private ResponseEntity<StatusResponse> getBySupplierId(@PathVariable("supplierId") String id) {

        StatusResponse sr = new StatusResponse();
        List<TaxiDetail> result = new ArrayList<TaxiDetail>();
        sr.setData(this.taxiDetailService.findSupplierId(id));
        sr.setStatus(true);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @RequestMapping(value="/v1/get/driverId/{driverId}",method = RequestMethod.GET)
    private ResponseEntity<StatusResponse> getByDriverId(@PathVariable("driverId") String driverId) {

        StatusResponse sr = new StatusResponse();
        List<TaxiDetail> result = new ArrayList<TaxiDetail>();
        //sr.setData(this.taxiDetailRepository.findByDriverId(driverId));
        sr.setData(this.taxiDetailService.findDriverId(driverId));
        sr.setStatus(true);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

  /*  @RequestMapping(value="/v1/get/active/{driverId}",method = RequestMethod.GET)
    private ResponseEntity<StatusResponse> getActive(@PathVariable("driverId") String driverId) {

        StatusResponse sr = new StatusResponse();
        List<TaxiDetail> result = new ArrayList<TaxiDetail>();
        //sr.setData(this.taxiDetailRepository.findByDriverId(driverId));
        sr.setData(this.taxiDetailService.updateActive(null,null));
        sr.setStatus(true);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }*/



    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.TAXI +  Path.Url.VERSION_V1 + Path.OperationUrl.UPDATEINFO, method = RequestMethod.POST, produces = {"application/json" })
    public ResponseEntity<StatusResponse> updateTaxiInfo(@RequestBody TaxiDTO taxiTto)  {

        User user = null;
        ModelStatus status = null;
        StatusResponse sr = new StatusResponse();

        TaxiUpdateDTO dto = new TaxiUpdateDTO();

        dto.setCarType(taxiTto.getCarType());
        dto.setUserId(taxiTto.getUserId());


        //dto.getCarType(),dto.getUserId(),dto.getTaxiId(),dto.getSupplierId(),dto.getHour(),dto.getKm(),dto.getSeats()
        status = taxiModel.validationTaxiDetailbyIdSupplierID(dto);

        //log.info("--status--" + status);

        if(status.equals(ModelStatus.NOTEXISTS)){
            sr.setStatus(false);
            sr.setMessage(messageService.getMessage("driver_id_not_exiting", dto.getTaxiId()));
            sr.setInfoId(ErrorNumber.DRIVER_ID_NOT_EXISTING);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
        else if(status.equals(ModelStatus.NO_SUPPLIER_EXISTING)) {
            sr.setStatus(false);
            sr.setMessage(messageService.getMessage("supplier_invalid",dto.getSupplierId()));
            sr.setInfoId(ErrorNumber.NO_SUPPLIER_EXISTING);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
        else if(status.equals(ModelStatus.NO_TAXIDETAILS_ID)) {
            sr.setStatus(false);
            sr.setInfoId(ErrorNumber.NO_TAXIDETAILS_ID);
            sr.setMessage(messageService.getMessage("taxi_id_not_valid",dto.getTaxiId()));
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
        else if(status.equals(ModelStatus.SUCCESS)) {
            ModelStatus updateStatus = taxiModel.updateTaxiInfo(taxiTto);
            if(updateStatus.equals(ModelStatus.UPDATED)){
                sr.setStatus(true);
                sr.setMessage(messageService.getMessage("taxi_details_updated", dto.getTaxiId()));
                sr.setInfoId(ErrorNumber.UPDATE_TAXI_DETAIL_SUCCESS);
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            else if(updateStatus.equals(ModelStatus.NO_SUPPLIER_EXISTING)) {
                sr.setStatus(false);
                sr.setMessage(messageService.getMessage("tag_code_is_not_existing",dto.getTaxiId()));
                sr.setInfoId(ErrorNumber.NO_SUPPLIER_EXISTING);
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            else if(updateStatus.equals(ModelStatus.NO_TAXIDETAILS_ID)) {
                sr.setStatus(false);
                sr.setMessage(messageService.getMessage("tag_code_is_not_existing",dto.getTaxiId()));
                sr.setInfoId(ErrorNumber.NO_TAXIDETAILS_ID);
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            else if(updateStatus.equals(ModelStatus.NO_SUPPLIER_NOT_MATCHING)) {
                sr.setStatus(false);
                sr.setMessage(messageService.getMessage("tag_code_is_not_existing",dto.getTaxiId()));
                sr.setInfoId(ErrorNumber.NO_SUPPLIER_NOT_MATCHING);
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
        }
        else {
            sr.setInfoId(600);
            sr.setMessage(messageService.getMessage("user_is_not_existing", dto.getUserId()));
            sr.setData(dto.getTaxiId());
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }
    @Autowired
    UserModel userModel;

   /* @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.TAXI +  "/userTesting" + Path.OperationUrl.UPDATEINFO, method = RequestMethod.POST, produces = {"application/json" })
    public ResponseEntity<StatusResponse> updateTaxiUserInfo2(@RequestBody TaxiDetailDTO taxiUserDTO)  {
        User user = null;
        ModelStatus status = null;
        userModel.changeNamePhoneNumber(taxiUserDTO.getUserId(),taxiUserDTO.getFirstName(),taxiUserDTO.getLastName(),taxiUserDTO.phoneNumber);
        StatusResponse sr = new StatusResponse();
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }*/
    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.TAXI +  "/user" + Path.OperationUrl.UPDATEINFO, method = RequestMethod.POST, produces = {"application/json" })
    public ResponseEntity<StatusResponse> updateTaxiUserInfo(@RequestBody TaxiDetailDTO taxiTto)  {

        User user = null;
        ModelStatus status = null;
        StatusResponse sr = new StatusResponse();

        TaxiUpdateDTO dto = new TaxiUpdateDTO();

        dto.setCarType(taxiTto.getCarType());
        dto.setUserId(taxiTto.getUserId());
        dto.setTaxiId(taxiTto.getTaxiId());
        dto.setSupplierId(taxiTto.getSupplierId());

        dto.setVehicleBrand(taxiTto.getVehicleBrand());
        dto.setHour(taxiTto.getHour());
        dto.setKm(taxiTto.getKm());
        dto.setSeats(taxiTto.getSeats());
        dto.setTaxiNumber(taxiTto.getTaxiNumber());
        dto.setVehicleYear(taxiTto.getVehicleYear());

        status = taxiModel.validationTaxiDetailbyIdSupplierID(dto);
        //log.info("--status--" + status);

        if(status.equals(ModelStatus.NOTEXISTS)){
            sr.setStatus(false);
            sr.setMessage(messageService.getMessage("driver_id_not_exiting", dto.getTaxiId()));
            sr.setInfoId(ErrorNumber.DRIVER_ID_NOT_EXISTING);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
        else if(status.equals(ModelStatus.NO_SUPPLIER_EXISTING)) {
            sr.setStatus(false);
            sr.setMessage(messageService.getMessage("supplier_invalid",dto.getSupplierId()));
            sr.setInfoId(ErrorNumber.NO_SUPPLIER_EXISTING);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
        else if(status.equals(ModelStatus.NO_TAXIDETAILS_ID)) {
            sr.setStatus(false);
            sr.setInfoId(ErrorNumber.NO_TAXIDETAILS_ID);
            sr.setMessage(messageService.getMessage("taxi_id_not_valid",dto.getTaxiId()));
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
        else if(status.equals(ModelStatus.SUCCESS)) {
           // ModelStatus updateStatus = taxiModel.updateTaxiInfo(taxiTto);
           /* if(updateStatus.equals(ModelStatus.UPDATED)){
                sr.setStatus(true);
                sr.setMessage(messageService.getMessage("taxi_details_updated", dto.getTaxiId()));
                sr.setInfoId(ErrorNumber.UPDATE_TAXI_DETAIL_SUCCESS);
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            else if(updateStatus.equals(ModelStatus.NO_SUPPLIER_EXISTING)) {
                sr.setStatus(false);
                sr.setMessage(messageService.getMessage("tag_code_is_not_existing",dto.getTaxiId()));
                sr.setInfoId(ErrorNumber.NO_SUPPLIER_EXISTING);
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            else if(updateStatus.equals(ModelStatus.NO_TAXIDETAILS_ID)) {
                sr.setStatus(false);
                sr.setMessage(messageService.getMessage("tag_code_is_not_existing",dto.getTaxiId()));
                sr.setInfoId(ErrorNumber.NO_TAXIDETAILS_ID);
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            else if(updateStatus.equals(ModelStatus.NO_SUPPLIER_NOT_MATCHING)) {
                sr.setStatus(false);
                sr.setMessage(messageService.getMessage("tag_code_is_not_existing",dto.getTaxiId()));
                sr.setInfoId(ErrorNumber.NO_SUPPLIER_NOT_MATCHING);
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }*/
        }
        else {
            sr.setInfoId(600);
            sr.setMessage(messageService.getMessage("user_is_not_existing", dto.getUserId()));
            sr.setData(dto.getTaxiId());
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }
}
