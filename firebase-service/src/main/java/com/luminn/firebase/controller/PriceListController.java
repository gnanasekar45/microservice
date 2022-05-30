package com.luminn.firebase.controller;

import com.luminn.firebase.dto.PriceListDTO;
import com.luminn.firebase.dto.ReviewDTO;
import com.luminn.firebase.dto.RidesRequest;
import com.luminn.firebase.entity.PriceList;
import com.luminn.firebase.model.PriceListModel;
import com.luminn.firebase.repository.PriceListRepository;
import com.luminn.firebase.util.Path;
import com.luminn.view.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/priceList")
public class PriceListController {

    @Autowired
    PriceListModel priceListModel;

    @Autowired
    PriceListRepository priceListRepository;

    @ResponseBody
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V1  + "/add", method = RequestMethod.POST,  produces = {"application/json" })
    public ResponseEntity<StatusResponse> add(@RequestBody PriceListDTO priceList) {
        StatusResponse sr = new StatusResponse();
        priceListModel.create(priceList);
        sr.setStatus(true);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V1  + "/{driverId}", method = RequestMethod.GET,  produces = {"application/json" })
    public ResponseEntity<StatusResponse> getByDriverId(@PathVariable("driverId") String driverId) {
        StatusResponse sr = new StatusResponse();
        //List<ReviewDTO> reviewDTO = reviewModel.getByTaxiDetailIds(taxiDetailId);
        //sr.setData(reviewDTO);
        sr.setData(priceListRepository.findByDriverId(driverId));
        sr.setStatus(true);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }
}
