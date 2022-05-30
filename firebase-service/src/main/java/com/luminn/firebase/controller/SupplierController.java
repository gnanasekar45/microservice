package com.luminn.firebase.controller;


import com.luminn.firebase.dto.SupplierDTO;
import com.luminn.firebase.dto.Taxi;
import com.luminn.firebase.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierController {

    @Autowired
    SupplierService supplierService;

    @RequestMapping(value="/v1/add/taxi",method = RequestMethod.POST)
    private void addSupplier(@RequestBody SupplierDTO delivery) {
        List<Taxi> result = null;
        this.supplierService.addUserOne(delivery);
    }

    @RequestMapping(value="/v1/update/taxi",method = RequestMethod.POST)
    private void addSupplierUpdate(@RequestBody SupplierDTO delivery) {
        List<Taxi> result = null;
        this.supplierService.update(delivery.getIds(),delivery.getUserIds());
    }

    @RequestMapping(value="/v1/get/taxi",method = RequestMethod.POST)
    private void addSupplierUGET(@RequestBody SupplierDTO delivery) {
        List<Taxi> result = null;
        this.supplierService.findUser(delivery.getUserIds());
    }

}
