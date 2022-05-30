package com.luminn.firebase.controller;

import com.luminn.firebase.entity.DriverPhoto;
import com.luminn.firebase.repository.DriverPhotoRepository;
import com.luminn.firebase.service.DriverPhotoService;
import com.luminn.view.StatusResponse;
import com.luminn.view.StatusResponseImage;
import lombok.extern.log4j.Log4j2;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@Log4j2
public class DriverController {

    @Autowired
    DriverPhotoService photoService;

    @Autowired
    DriverPhotoRepository photoRepository;

    @PostMapping("/driver-photo")
    public ResponseEntity<StatusResponse> addDriverPhoto(@RequestParam("driver_name") String name, @RequestParam MultipartFile image,
                                                         @RequestParam("userId") String userId, @RequestParam("type") String type) throws IOException {
        String photoName = this.photoService.saveDriverPhoto(name, image,userId,type);
        StatusResponse statusResponse = new StatusResponse();
        statusResponse.setStatus(true);
        statusResponse.setData(photoName);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @PostMapping("citu/driver-photo")
    public ResponseEntity<StatusResponse> addCITUDriverPhoto(@RequestParam("driver_name") String name, @RequestParam MultipartFile image,
                                                         @RequestParam("userId") String userId, @RequestParam("type") String type) throws IOException {
        String photoName = this.photoService.saveCITUDriverPhoto(name, image,userId,type);
        StatusResponse statusResponse = new StatusResponse();
        statusResponse.setStatus(true);
        statusResponse.setData(photoName);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/driver-photo/name/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getDriverPhoto(@PathVariable String name) {
        return this.photoService.getDriverPhoto(name).getData();
    }

    @GetMapping(value = "/driver-photo/v1/name/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<StatusResponse> getDriverPhotos(@PathVariable String name) {

        byte[] bytes = this.photoService.getDriverPhoto(name).getData();
        StatusResponse statusResponse = new StatusResponse();
        statusResponse.setStatus(true);
        statusResponse.setData(bytes);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/driver-photo/id/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getDriverUserId(@PathVariable String id) {
            DriverPhoto list = this.photoService.getDriverUserandId(id);
            //List<byte[]> listBytes = this.photoService.getDriverUserId(id);
        return list.getDriverImage().getData();

        /*StatusResponseImage statusResponse = new StatusResponseImage();
            statusResponse.setStatus(true);
            statusResponse.setDataByte(bytes);
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);*/
    }

    @GetMapping(value = "citu/driver-photo/id/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getCITUDriverUserId(@PathVariable String id) {
        DriverPhoto list = this.photoService.getCITUDriverUserandId(id);
        //List<byte[]> listBytes = this.photoService.getDriverUserId(id);
        return list.getDriverImage().getData();

        /*StatusResponseImage statusResponse = new StatusResponseImage();
            statusResponse.setStatus(true);
            statusResponse.setDataByte(bytes);
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);*/
    }

    @GetMapping(value = "/driver-photo/id/{type}/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getDriverUserIdType(@PathVariable String id,@PathVariable String type) {
        DriverPhoto list = this.photoService.getDriverUserandId(id,type);
        byte[] bytes = list.getDriverImage().getData();
        return bytes;
    }
    @GetMapping(value = "citu/driver-photo/id/{type}/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getDriverCituUserIdType(@PathVariable String id,@PathVariable String type) {
        DriverPhoto list = this.photoService.getDriverCituUserandId(id,type);
        byte[] bytes = list.getDriverImage().getData();
        return bytes;
    }
}
