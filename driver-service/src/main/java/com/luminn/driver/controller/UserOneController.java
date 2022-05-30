package com.luminn.driver.controller;

import com.luminn.driver.model.DriverOne;
import com.luminn.driver.model.UserOne;
import com.luminn.driver.service.DriverSearchService;
import com.luminn.driver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserOneController {

    @Autowired
    UserService userService;

    @Autowired
    DriverSearchService driverSearchService;


    @RequestMapping(value="/v1/search/users",method = RequestMethod.GET)
    private List<UserOne> getUsersByLocationAdd( @RequestParam double latitude,@RequestParam double longitude, @RequestParam double distance) {
        List<UserOne> result = null;
        result=userService.getUsersByLocation(latitude,longitude,distance);
        return result;
    }

    @RequestMapping(value="/v1/find/users",method = RequestMethod.GET)
    private void getByLocationAdd3(@RequestParam double longitude, @RequestParam double latitude, @RequestParam double distance) {
        List<UserOne> result = null;
         userService.findNearest(74.7774223, 20.833993,10);
        //return aggregationOperation.;
    }

    @RequestMapping(value="/v1/aggregation/users",method = RequestMethod.GET)
    private List<AggregationOperation> getByLocationAdd(@RequestParam double longitude, @RequestParam double latitude, @RequestParam double distance) {
        List<UserOne> result = null;
        List<AggregationOperation> aggregationOperation  = userService.getByLocation(longitude,latitude,distance);
        return aggregationOperation;
    }

    @RequestMapping(value="/v1/add/users1",method = RequestMethod.POST)
    private void addDelivery(@RequestBody DriverOne delivery) {
        List<UserOne> result = null;
       userService.addUserOne(delivery);
       // return result;
    }

    @RequestMapping(value="/v1/update/users1",method = RequestMethod.POST)
    private void updateDelivery(@RequestBody DriverOne delivery) {
        List<UserOne> result = null;
        userService.updateDriver(delivery);
        // return result;
    }
}
