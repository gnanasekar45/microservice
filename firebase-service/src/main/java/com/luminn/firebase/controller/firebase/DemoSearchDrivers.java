package com.luminn.firebase.controller.firebase;

import com.luminn.firebase.entity.Driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



import java.util.ArrayList;

@RestController
@RequestMapping("/fcm1")
public class DemoSearchDrivers {

    @Autowired
    FireBaseConfig fireBaseConfig;

    @RequestMapping(value ="/add/driver", method = RequestMethod.POST)
    public Driver driver(@RequestBody Driver driver) {
       // fireBaseConfig.addDriver(driver.getName(),driver.getLatitude(),driver.getLongitude(),driver.getStatus());
        return driver;
    }



}
