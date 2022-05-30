package com.luminn.driver.controller;

import com.luminn.driver.model.Driver;
import com.luminn.driver.model.DriverSearch;
import com.luminn.driver.repository.DriverRepository;
import com.luminn.driver.service.DriverSearchService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
public class DriverLocalController {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    DriverSearchService driverSearchService;

    @PostMapping("/v1/driver")
    public ResponseEntity<Driver> insertDriverData(@RequestBody Driver driver) {
        try {

            System.out.println(" local insert  ");
            Driver userResult = this.driverRepository.insert(driver);

            System.out.println(" emd insert  ");
            return new ResponseEntity<>(userResult, HttpStatus.CREATED);
        } catch(Exception e) {
            log.info(e);
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/v1/driver/location")
    public List<DriverSearch> searchByLocation(@RequestParam double lt, @RequestParam double lg, @RequestParam double dis) {
        log.info(lt+" "+lg+" "+dis);
        return this.driverSearchService.searchByLocation(lt, lg, dis);
    }
}
