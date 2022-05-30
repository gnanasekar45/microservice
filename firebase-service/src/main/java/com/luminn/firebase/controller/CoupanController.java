package com.luminn.firebase.controller;

import com.luminn.firebase.entity.Coupon;
import com.luminn.firebase.repository.CouponRepository;
import com.luminn.view.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/coupan")
public class CoupanController {

    @Autowired
    CouponRepository coupanRepository;

    @PostMapping("/v1/add")
    private ResponseEntity<StatusResponse> addDelivery(@RequestBody Coupon dto) {
        StatusResponse statusResponse = new StatusResponse();
        statusResponse.setStatus(true);
        coupanRepository.save(dto);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }
    @PostMapping("/v1/find/supplierId/{coupon}")
    private ResponseEntity<StatusResponse> check(@PathVariable String coupon) {
        StatusResponse statusResponse = new StatusResponse();
        Coupon coupon1 = coupanRepository.findByCoupan(coupon);
            System.out.println(" coupon1 --->" + coupon1);
        if(coupon1 != null){
            statusResponse.setStatus(true);
        }
        else {
            statusResponse.setStatus(false);
        }
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @GetMapping("/v1/find/copon/{coupon}/{amount}")
    private ResponseEntity<StatusResponse> coupan(@PathVariable String coupon,@PathVariable String amount) {
        StatusResponse statusResponse = new StatusResponse();
        Coupon coupon1 = coupanRepository.findByCoupan(coupon);
        System.out.println(" coupon1 --->" + coupon1);
        if(coupon1 != null && amount != null){

            float amounts = Float.valueOf(amount);
            float percentages = (amounts * coupon1.getPercentage()/100);
            float totalamount = amounts - percentages;
            statusResponse.setData(totalamount);
            statusResponse.setStatus(true);
        }
        else {
            statusResponse.setStatus(false);
        }
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @PostMapping("/v1/find/{supplierId}")
    private ResponseEntity<StatusResponse> supplierId(@PathVariable String supplierId) {
        StatusResponse statusResponse = new StatusResponse();
        List<Coupon> coupon1 = coupanRepository.findBySupplierId(supplierId);
        System.out.println(" coupon1 --->" + coupon1);
        if(coupon1 != null){
            statusResponse.setStatus(true);
            statusResponse.setData(coupon1);
        }
        else {
            statusResponse.setStatus(false);
        }
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }
}
