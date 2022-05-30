package com.luminn.firebase.controller;

import com.luminn.firebase.dto.PriceDTO;
import com.luminn.firebase.entity.Price;
import com.luminn.firebase.model.ModelStatus;
import com.luminn.firebase.model.PriceModel;
import com.luminn.firebase.repository.PriceRepository;
import com.luminn.firebase.util.Path;
import com.luminn.view.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/price")
public class priceController {

    @Autowired
    PriceRepository priceRepository;

    @Autowired
    PriceModel priceModel;

    @PostMapping("/v1/add")
    private ResponseEntity<StatusResponse> add(@RequestBody PriceDTO dto) {

        StatusResponse statusResponse = new StatusResponse();
        ModelStatus status = null;
        //taxiModel.createPrice(dto.type,dto.getKm(),dto.getWaitingTime(),dto.getRegion(),dto.zipCode,dto.getDomain());
        priceModel.createPrice(dto);
        priceModel.getBestPrices("Taxi",1.0);
        statusResponse.setStatus(false);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);

    }

  /*  @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.PRICE +  Path.Url.VERSION_V1 +"/{domain}", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<StatusResponse> getDomain(@PathVariable("domain") String domain) {
        StatusResponse statusResponse = new StatusResponse();
        ModelStatus status = null;
        statusResponse.setData(priceModel.getPriceDomain(domain));
        statusResponse.setStatus(false);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }*/

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.PRICE +  Path.Url.VERSION_V1 +"/{domain}/{category}", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<StatusResponse> getDomain(@PathVariable("domain") String domain,@PathVariable("category") String category) {
        StatusResponse statusResponse = new StatusResponse();
        ModelStatus status = null;
        statusResponse.setData(priceRepository.findByDomainAndCategory(domain,category));
        statusResponse.setStatus(false);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.PRICE +  Path.Url.VERSION_V1 +"/{domain}/{category}/{region}", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<StatusResponse> getDomain(@PathVariable("domain") String domain,@PathVariable("category") String category,@PathVariable("region") String region) {
        StatusResponse statusResponse = new StatusResponse();
        ModelStatus status = null;
        statusResponse.setData(priceRepository.findByDomainAndCategoryAndRegion(domain,category,region));
        statusResponse.setStatus(false);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }


    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.PRICE +  Path.Url.VERSION_V2 +"/{domain}", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<StatusResponse> getDomain2(@PathVariable("domain") String domain) {
        StatusResponse statusResponse = new StatusResponse();
        ModelStatus status = null;
        statusResponse.setData(priceRepository.findByDomain(domain));
        statusResponse.setStatus(false);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP +   Path.Url.VERSION_V1 + "/supplier"+"/{supplierId}", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<StatusResponse> getSupplierId(@PathVariable("supplierId") String supplierId)  {
        StatusResponse statusResponse = new StatusResponse();
        ModelStatus status = null;
        statusResponse.setData(priceModel.getSupplierId(supplierId));
        statusResponse.setStatus(false);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP +   Path.Url.VERSION_V1 + "/all"+"/{supplierId}", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<StatusResponse> getAll(@PathVariable("supplierId") String supplierId)  {
        StatusResponse statusResponse = new StatusResponse();
        ModelStatus status = null;
        List<Price> lists = priceRepository.findAll();

        statusResponse.setData(lists);
        statusResponse.setStatus(false);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V1 +Path.OperationUrl.UPDATE, method = RequestMethod.PUT,produces = "application/json")
    public ResponseEntity<StatusResponse> updatePriceRegistration(@RequestBody PriceDTO dto)  {
        StatusResponse statusResponse = new StatusResponse();
        ModelStatus status = null;
        //taxiModel.createPrice(dto.type,dto.getKm(),dto.getWaitingTime(),dto.getRegion(),dto.zipCode,dto.getDomain());
        priceModel.updatePrice(dto);
        //priceModel.getBestPrices("Taxi",1.0);
        statusResponse.setStatus(true);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }
}
