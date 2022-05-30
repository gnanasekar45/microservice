package com.luminn.firebase.controller;


import com.luminn.firebase.dto.PriceDTO;
import com.luminn.firebase.entity.PriceTemplate;
import com.luminn.firebase.model.ModelStatus;
import com.luminn.firebase.model.PriceModel;
import com.luminn.firebase.model.PriceTemplateModel;
import com.luminn.firebase.repository.PriceRepository;
import com.luminn.firebase.repository.PriceTemplateRepository;
import com.luminn.firebase.util.Path;
import com.luminn.view.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/priceTemplate")
public class priceTemplateController {

    @Autowired
    PriceTemplateRepository priceTemplateRepository;

    @Autowired
    PriceTemplateModel priceTemplateModel;

    @PostMapping("/v1/add")
    private ResponseEntity<StatusResponse> add(@RequestBody PriceTemplate dto) {

        StatusResponse statusResponse = new StatusResponse();
        ModelStatus status = null;
        //taxiModel.createPrice(dto.type,dto.getKm(),dto.getWaitingTime(),dto.getRegion(),dto.zipCode,dto.getDomain());
        priceTemplateModel.createPriceSwiss(dto);
        statusResponse.setStatus(false);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.PRICE +  Path.Url.VERSION_V1 +"/{domain}", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<StatusResponse> getDomain2(@PathVariable("domain") String domain) {
        StatusResponse statusResponse = new StatusResponse();
        ModelStatus status = null;
        statusResponse.setData(priceTemplateRepository.findByDomain(domain));
        statusResponse.setStatus(false);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

}
