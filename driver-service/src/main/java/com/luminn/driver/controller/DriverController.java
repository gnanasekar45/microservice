package com.luminn.driver.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.luminn.driver.model.Driver;
import com.luminn.driver.model.DriverSearch;
import com.luminn.driver.repository.DriverRepository;
import com.luminn.driver.service.DriverPhotoService;
import com.luminn.driver.service.DriverSearchService;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class DriverController {
	
	@Autowired
	private DriverRepository driverRepository;
	
	@Autowired
	DriverSearchService driverSearchService;
	
	@Autowired
	DriverPhotoService photoService;
	
	@GetMapping("/driver/{name}")
	public List<Driver> getDriverByName(@PathVariable String name) {
		return this.driverRepository.findByName(name);
	}
	
	@PostMapping("/driver")
	public ResponseEntity<Driver> insertDriverData(@RequestBody Driver driver) {
		try {
			System.out.println(" insert before ");
			Driver userResult = this.driverRepository.insert(driver);
			System.out.println(" insert end ");
			return new ResponseEntity<>(userResult, HttpStatus.CREATED);
		} catch(Exception e) {
			log.info(e);
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PostMapping("/v2/driver")
	public ResponseEntity<Driver> insertDriverData2(@RequestBody Driver driver) {
		try {

			System.out.println(" insert before ");
			Driver userResult = this.driverRepository.insert(driver);
			System.out.println(" insert end ");
			return new ResponseEntity<>(userResult, HttpStatus.CREATED);
		} catch(Exception e) {
			log.info(e);
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}


	@PostMapping("/v3/driver")
	public ResponseEntity<Driver> insertDriverData3(@RequestBody Driver driver) {
		try {

			System.out.println(" insert before ");
			Driver userResult = this.driverRepository.insert(driver);
			System.out.println(" insert end ");
			return new ResponseEntity<>(userResult, HttpStatus.CREATED);
		} catch(Exception e) {
			log.info(e);
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/driver/location")
	public List<DriverSearch> searchByLocation(@RequestParam double lt, @RequestParam double lg, @RequestParam double dis) {
		log.info(lt+" "+lg+" "+dis);
		return this.driverSearchService.searchByLocation(lt, lg, dis);
	}
	
	@PostMapping("/driver-photo")
	public String addDriverPhoto(@RequestParam("driver_name") String name, @RequestParam MultipartFile image) throws IOException {	
		return this.photoService.saveDriverPhoto(name, image);
	}
	
	@GetMapping(value = "/driver-photo/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getDriverPhoto(@PathVariable String name) {
		return this.photoService.getDriverPhoto(name).getData();
	}
}
