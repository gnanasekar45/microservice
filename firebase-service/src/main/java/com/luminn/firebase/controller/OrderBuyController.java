package com.luminn.firebase.controller;

import com.luminn.firebase.controller.exception.RecordNotFoundException;
import com.luminn.firebase.entity.Order;
import com.luminn.firebase.entity.OrderBuy;
import com.luminn.firebase.model.ProductDetailModel;
import com.luminn.firebase.repository.OrderBuyRepository;
import com.luminn.firebase.repository.OrderRepository;
import com.luminn.firebase.repository.UserRepository;
import com.luminn.firebase.service.UserMongoService;
import com.luminn.view.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orderBuy")
public class OrderBuyController {

    @Autowired
    UserMongoService userMongoService;


    @Autowired
    ProductDetailModel productDetailModel;
    //User user = this.userMongoService.findByPhoneNumber(phonenumber);

    //addUserOneRegistration
    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderBuyRepository orderBuyRepository;

   @RequestMapping(value="/app/v1/add",method = RequestMethod.POST)
    private ResponseEntity<StatusResponse>  add(@RequestBody OrderBuy order)
            throws RecordNotFoundException {
        StatusResponse statusResponse = new StatusResponse();
       orderBuyRepository.save(order);
        statusResponse.setStatus(true);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @RequestMapping(value="/app/v1/get/{userId}",method = RequestMethod.GET)
    private ResponseEntity<StatusResponse>  getItem(@PathVariable String userId) throws RecordNotFoundException {
        StatusResponse statusResponse = new StatusResponse();
        List<OrderBuy> orders = orderBuyRepository.findByUserId(userId);
        statusResponse.setData(orders);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @RequestMapping(value="/app/v1/get/{orderId}",method = RequestMethod.GET)
    private ResponseEntity<StatusResponse>  getCategory(@PathVariable String item) throws RecordNotFoundException {
        StatusResponse statusResponse = new StatusResponse();
        OrderBuy orders = orderBuyRepository.findByOrderId(item);
        statusResponse.setData(orders);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @RequestMapping(value="/app/v1/get/name/{name}",method = RequestMethod.GET)
    private ResponseEntity<StatusResponse>  getName(@PathVariable String name) throws RecordNotFoundException {
        StatusResponse statusResponse = new StatusResponse();
        List<OrderBuy> orders = orderBuyRepository.findByname(name);
        statusResponse.setData(orders);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }
    }
