package com.luminn.firebase.controller;

import com.luminn.firebase.controller.exception.RecordNotFoundException;
import com.luminn.firebase.dto.*;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.model.SocialModel;
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
@RequestMapping("/social")
public class SocialController {

    @Autowired
    UserMongoService userMongoService;

    @Autowired
    SupplierService supplierService;
     //return this.userMongoService.findByEmail(email);

    @Autowired
    SocialModel socialModel;
    //User user = this.userMongoService.findByPhoneNumber(phonenumber);

    //addUserOneRegistration
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value="/app/v1/add/user",method = RequestMethod.POST)
    private ResponseEntity<StatusResponse>  addSupplier(@RequestBody UserSocialDTO userSocial) throws RecordNotFoundException {

        StatusResponse statusResponse = new StatusResponse();
        //LoginDTO
        /*if(userSocial.getEmail() == null || "".equalsIgnoreCase(userSocial.getEmail())){
            throw new RecordNotFoundException("emai id : " );
        }*/

            User user = socialModel.getLogincheck(userSocial);
            if(user != null){
                LoginDTO loginDTO = socialModel.getLoginObject(user);
                statusResponse.setStatus(true);
                statusResponse.setInfoId(200);
                statusResponse.setData(loginDTO);
                statusResponse.setMessage("old");
                return new ResponseEntity<>(statusResponse, HttpStatus.OK);
            }
            else {
                LoginDTO loginDTO = socialModel.addUser(userSocial);
                //phonenumber or
                statusResponse.setStatus(true);
                statusResponse.setInfoId(200);
                statusResponse.setData(loginDTO);
                statusResponse.setMessage("new");
                return new ResponseEntity<>(statusResponse, HttpStatus.OK);
            }
        }

    @RequestMapping(value="/app/v1/get/{socailId}",method = RequestMethod.POST)
    private ResponseEntity<StatusResponse>  getSupplier(@PathVariable String socailId) throws RecordNotFoundException {
        StatusResponse statusResponse = new StatusResponse();
        User user = userMongoService.findBySocail(socailId);
        statusResponse.setData(user);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    }
