package com.luminn.firebase.controller;

import com.google.firebase.database.DatabaseException;
import com.luminn.firebase.dto.DriverBillingDTO;
import com.luminn.firebase.entity.DriverInfo;
import com.luminn.firebase.repository.DriverInfoRepository;
import com.luminn.firebase.util.Path;
import com.luminn.view.StatusResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/driverInfo")
public class DriverInfoController {

    @Autowired
    DriverInfoRepository driverInfoRepository;

    @PostMapping(value = Path.Url.APP  + Path.Url.VERSION_V1 + "/add",produces = {"application/json" })
    public ResponseEntity<StatusResponse> addDriverInfo(@RequestBody DriverInfo driverInfo) throws IOException {
        StatusResponse sr = new StatusResponse();
        String ids = null;
        Optional<DriverInfo> aadhar = driverInfoRepository.findByAadhar(driverInfo.getAadhar());
        Optional<DriverInfo> licence = driverInfoRepository.findByLicenceNumber(driverInfo.getLicenceNumber());
        Optional<DriverInfo> phoneNumber = driverInfoRepository.findByPhoneNumber(driverInfo.getPhoneNumber());

        if(!aadhar.isPresent()){
            if(!licence.isPresent() && !phoneNumber.isPresent()) {
                DriverInfo info = new DriverInfo();
                info.setPhoneNumber(driverInfo.phoneNumber);
                info.setAadhar(driverInfo.getAadhar());
                info.setLicenceNumber(driverInfo.getLicenceNumber());
                info.setSupplierId(driverInfo.getSupplierId());

                DriverInfo obs = driverInfoRepository.save(info);
                System.out.println("id " + obs.getId());
                Optional<DriverInfo> optinal = driverInfoRepository.findByAadhar(driverInfo.getAadhar());
               // if(optinal.isPresent())

                sr.setData(obs.getId());
            }
            sr.setMessage("Success");
            sr.setStatus(true);

        }

        else {
            sr.setMessage("failure");
            sr.setStatus(false);
            sr.setData("Either aadhar or licence or duplicate are duplicate");
        }


        return new ResponseEntity<>(sr, HttpStatus.OK);
    }


    @ResponseBody
    @RequestMapping(value = Path.Url.APP  + Path.Url.VERSION_V1 + "/phoneNumber" +"/{phoneNumber}", method = RequestMethod.GET, produces = {"application/json" })
    public ResponseEntity<StatusResponse> getPhoneNUmber1(@PathVariable String phoneNumber) throws DatabaseException {
        StatusResponse sr = new StatusResponse();
        Optional<DriverInfo> opt = driverInfoRepository.findByPhoneNumber(phoneNumber);
        sr.setStatus(true);
        if(opt.isPresent()){
            sr.setData(opt.get());
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }
    @ResponseBody
    @RequestMapping(value = Path.Url.APP  + Path.Url.VERSION_V1 + "/aadhar" +"/{aadhar}", method = RequestMethod.GET, produces = {"application/json" })
    public ResponseEntity<StatusResponse> getPhoneNUmber2(@PathVariable String aadhar) throws DatabaseException {
        StatusResponse sr = new StatusResponse();
        Optional<DriverInfo> opt = driverInfoRepository.findByAadhar(aadhar);
        sr.setStatus(true);
        if(opt.isPresent()){
            sr.setData(opt.get());
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP  + Path.Url.VERSION_V1 + "/licencenumber" +"/{licencenumber}", method = RequestMethod.GET, produces = {"application/json" })
    public ResponseEntity<StatusResponse> getLicencenumber(@PathVariable String licencenumber) throws DatabaseException {
        StatusResponse sr = new StatusResponse();
        Optional<DriverInfo> opt = driverInfoRepository.findByLicenceNumber(licencenumber);
        sr.setStatus(true);
        if(opt.isPresent()){
            sr.setData(opt.get());
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP  + Path.Url.VERSION_V1 + "/supplierId" +"/{supplierId}", method = RequestMethod.GET, produces = {"application/json" })
    public ResponseEntity<StatusResponse> getSupplierId(@PathVariable String supplierId) throws DatabaseException {
        StatusResponse sr = new StatusResponse();
        List<DriverInfo> opt = driverInfoRepository.findBySupplierId(supplierId);
        sr.setStatus(true);
        if(opt != null){
            sr.setData(opt);
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }
    @ResponseBody
    @RequestMapping(value = Path.Url.APP  + Path.Url.VERSION_V1 + "/supplierId/all" +"/{supplierId}", method = RequestMethod.GET, produces = {"application/json" })
    public ResponseEntity<StatusResponse> getSupplierIdAll(@PathVariable String supplierId) throws DatabaseException {
        StatusResponse sr = new StatusResponse();
        List<DriverInfo> opt = driverInfoRepository.findAll();
        sr.setStatus(true);
        if(opt != null){
            sr.setData(opt);
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }
}
