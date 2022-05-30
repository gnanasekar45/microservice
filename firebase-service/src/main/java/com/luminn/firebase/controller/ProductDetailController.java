package com.luminn.firebase.controller;

import com.luminn.firebase.controller.exception.RecordNotFoundException;
import com.luminn.firebase.dto.LoginDTO;
import com.luminn.firebase.dto.ProductDetail;
import com.luminn.firebase.dto.UserSocialDTO;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.model.ProductDetailModel;
import com.luminn.firebase.model.SocialModel;
import com.luminn.firebase.repository.ProductDetailRepository;
import com.luminn.firebase.repository.UserRepository;
import com.luminn.firebase.service.SupplierService;
import com.luminn.firebase.service.UserMongoService;
import com.luminn.view.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductDetailController {

    @Autowired
    UserMongoService userMongoService;

    @Autowired
    SupplierService supplierService;
     //return this.userMongoService.findByEmail(email);

    @Autowired
    ProductDetailModel productDetailModel;
    //User user = this.userMongoService.findByPhoneNumber(phonenumber);

    //addUserOneRegistration
    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductDetailRepository productDetailRepository;

    @RequestMapping(value="/app/v1/add/product",method = RequestMethod.POST)
    private ResponseEntity<StatusResponse>  addSupplier(@RequestBody ProductDetail productDetail)
            throws RecordNotFoundException {
        StatusResponse statusResponse = new StatusResponse();
        productDetailRepository.save(productDetail);
        statusResponse.setStatus(true);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @RequestMapping(value="/app/v1/get/{itemId}",method = RequestMethod.GET)
    private ResponseEntity<StatusResponse>  getItem(@PathVariable String itemId) throws RecordNotFoundException {
        StatusResponse statusResponse = new StatusResponse();
        ProductDetail productDetail = productDetailRepository.findByItem(itemId);
        statusResponse.setData(productDetail);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @RequestMapping(value="/app/v1/get/category/{category}",method = RequestMethod.GET)
    private ResponseEntity<StatusResponse>  getCategory(@PathVariable String category) throws RecordNotFoundException {
        StatusResponse statusResponse = new StatusResponse();
        ProductDetail productDetail = productDetailRepository.findByCategory(category);
        statusResponse.setData(productDetail);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @RequestMapping(value="/app/v1/get/name/{name}",method = RequestMethod.GET)
    private ResponseEntity<StatusResponse>  getName(@PathVariable String name) throws RecordNotFoundException {
        StatusResponse statusResponse = new StatusResponse();
        List<ProductDetail> productDetail = productDetailRepository.findByname(name);
        statusResponse.setData(productDetail);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }
    }
