package com.luminn.firebase.controller;

import com.luminn.firebase.entity.User;
import com.luminn.firebase.repository.UserRepository;
import com.luminn.firebase.request.TokenDTO;
import com.luminn.firebase.service.UserMongoService;
import com.luminn.view.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserMongoService userMongoService;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value="/v1/search",method = RequestMethod.GET)
    private List<User> getUsersByLocationAdd(@RequestParam float latitude, @RequestParam float longitude, @RequestParam double distance) {
        List<User> result = null;
        result = this.userMongoService.getUsersByLocation(latitude,longitude,distance);
        return result;
    }
    @RequestMapping(value="/v1/find",method = RequestMethod.GET)
    private void getByLocationAdd3(@RequestParam double longitude, @RequestParam double latitude, @RequestParam double distance) {
        List<User> result = null;
        this.userMongoService.findNearest(74.7774223, 20.833993,10);
        //return aggregationOperation.;
    }

    @RequestMapping(value="/v1/find/token",method = RequestMethod.GET)
    private ResponseEntity<StatusResponse> getByLocation4(@RequestParam String token) {

        StatusResponse sr = new StatusResponse();
        List<User> result = null;
        sr.setData(this.userMongoService.findToken(token));
        //return aggregationOperation.;
        return new ResponseEntity<>(sr, HttpStatus.OK);

    }

    @RequestMapping(value="/v1/find/id/token",method = RequestMethod.PUT)
    private ResponseEntity<StatusResponse> puzByLocation4(@RequestBody TokenDTO tokenDTO) {

        StatusResponse sr = new StatusResponse();
        List<User> result = null;
        sr.setData(this.userMongoService.updateTokenfromId(tokenDTO.getId(),tokenDTO.getToken(),tokenDTO.getNotificationToken()));
        //return aggregationOperation.;
        return new ResponseEntity<>(sr, HttpStatus.OK);

    }

    @RequestMapping(value="/v1/get/token/{token}",method = RequestMethod.GET)
    private User getByToken(@RequestParam String token) {
        List<User> result = null;
        return this.userMongoService.getToken(token);
        //return aggregationOperation.;
    }
    @RequestMapping(value="/v1/get/status/{status}/id/{id}",method = RequestMethod.GET)
    private User getByStatusToken(@PathVariable("status") String status,@PathVariable("id") String id) {
        List<User> result = null;
        return this.userMongoService.getToken(id);
        //return aggregationOperation.;
    }

    @RequestMapping(value="/v1/get/email/{email}",method = RequestMethod.GET)
    private User getByEmail(@RequestParam String email) {
        List<User> result = null;
        return this.userMongoService.findByEmail(email);
        //return aggregationOperation.;
    }

    @RequestMapping(value="/v1/get/phonenumber/{phonenumber}",method = RequestMethod.GET)
    private ResponseEntity getByPhoneNumber(@PathVariable String phonenumber) {
        StatusResponse statusResponse = new StatusResponse();

        List<User> result = null;
        User user = this.userMongoService.findByPhoneNumber(phonenumber);

        statusResponse.setStatus(true);
        statusResponse.setData(user);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        //return aggregationOperation.;
    }

    @RequestMapping(value="/v1/add",method = RequestMethod.POST)
    private void addDelivery(@RequestBody User delivery) {
        List<User> result = null;
        this.userMongoService.addUserOne(delivery);
       // return result;
    }

    @RequestMapping(value="/v1/registration/add",method = RequestMethod.POST)
    private void addRegistration(@RequestBody User delivery) {
        List<User> result = null;
        this.userMongoService.addUserOneRegistration(delivery);
        // return result;
    }




    @RequestMapping(value="/v1/update",method = RequestMethod.POST)
    private void updateDelivery(@RequestBody User delivery) {
        List<User> result = null;
        this.userMongoService.updateDriver(delivery);
    }

    @RequestMapping(value="/v1/phoneNumberupdate",method = RequestMethod.PUT)
    private void updatePhoneNumber(@RequestBody User delivery) {
        List<User> result = null;
        this.userMongoService.updateDriver(delivery);
    }



   /* @RequestMapping(value="/v1/update/email",method = RequestMethod.POST)
    private void updateName(@RequestBody User user) {
        List<User> result = null;
        User updateUser = null;
        //this.taxiService.updateName(taxi.getId(),taxi.getName());
        Optional<User> user2 = userRepository.findById(user.getId());
        if(user2.isPresent())
            updateUser = user2.get();
        updateUser.setEmail(user.getEmail());

        this.userRepository.save(updateUser);
    }*/
}
