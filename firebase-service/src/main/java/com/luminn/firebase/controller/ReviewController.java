package com.luminn.firebase.controller;

import com.luminn.firebase.dto.ReviewDTO;
import com.luminn.firebase.dto.RideDTO;
import com.luminn.firebase.entity.Review;
import com.luminn.firebase.helper.Converter;
import com.luminn.firebase.model.ModelStatus;
import com.luminn.firebase.model.ReviewModel;
import com.luminn.firebase.repository.ReviewRepository;
import com.luminn.firebase.util.Path;
import com.luminn.view.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ReviewModel reviewModel;


    @PostMapping("/app/v1/add")
    private ResponseEntity<StatusResponse> addDelivery(@RequestBody ReviewDTO reviewDTO) {
        StatusResponse sr = new StatusResponse();
        ModelStatus ModelStatus = reviewModel.createReview(reviewDTO);
        sr.setData(ModelStatus.name());
        sr.setStatus(true);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V1  + "/ByTaxiDetailId"+"/{taxiDetailId}", method = RequestMethod.GET,  produces = {"application/json" })
    public ResponseEntity<StatusResponse> getByTaxiId(@PathVariable("taxiDetailId") String taxiDetailId) {
        StatusResponse sr = new StatusResponse();

        List<ReviewDTO> reviewDTO = reviewModel.getByTaxiDetailIds(taxiDetailId);
        //List<Review> reviewDTO = reviewRepository.findByTaxiDetailId(taxiDetailId);
        sr.setData(reviewDTO);
        sr.setStatus(true);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }




    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.REVIEW + Path.Url.VERSION_V1  + "/rideId"+"/{rideId}", method = RequestMethod.GET,  produces = {"application/json" })
    public ResponseEntity<StatusResponse> getByfindByRideId(@PathVariable("rideId") String rideId) {
        StatusResponse sr = new StatusResponse();
        List<ReviewDTO> dto = new ArrayList<>();
        List<Review> reviews = reviewRepository.findByRideId(rideId);

        for(Review review : reviews){
            dto.add(Converter.entityToDto(review));
        }
        sr.setData(dto);
        sr.setStatus(true);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }




}
