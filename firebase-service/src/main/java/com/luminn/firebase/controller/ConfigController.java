package com.luminn.firebase.controller;

import com.luminn.firebase.dto.ConfigVersionDTO;
import com.luminn.firebase.entity.ConfigVersion;
import com.luminn.firebase.model.ConfigVersionModel;
import com.luminn.firebase.model.ModelStatus;
import com.luminn.firebase.repository.ConfigRepository;
import com.luminn.firebase.request.DomainConfig;
import com.luminn.firebase.service.MessageByLocaleService;
import com.luminn.firebase.util.Path;
import com.luminn.view.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/config")
public class ConfigController {


    @Autowired
    public ConfigVersionModel configVersionModel;

    @Autowired
    private MessageByLocaleService messageService;

    @Autowired
    ConfigRepository configRepository;


    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.CONFIG + Path.OperationUrl.CREATE, method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<StatusResponse> create(@RequestBody ConfigVersionDTO configVersionDTO) {

        StatusResponse sr = new StatusResponse();
        ModelStatus status =  configVersionModel.create(configVersionDTO);
        if(status.equals(ModelStatus.EXISTS)){
            sr.setMessage(messageService.getMessage("version_wrong",configVersionDTO.getAndroidUser()));
        }
        else {
            sr.setStatus(true);
            sr.setMessage(messageService.getMessage("version_created", configVersionDTO.getAndroidUser()));
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.CONFIG +Path.Url.VERSION_V1 + Path.OperationUrl.CREATE, method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<StatusResponse> createEntity(@RequestBody ConfigVersion configVersion) {

        StatusResponse sr = new StatusResponse();
        ModelStatus status =  configVersionModel.createEntity(configVersion);
        if(status.equals(ModelStatus.EXISTS)){
            sr.setMessage(messageService.getMessage("version_wrong",configVersion.getAndroidUser()));
        }
        else {
            sr.setStatus(true);
            sr.setMessage(messageService.getMessage("version_created", configVersion.getAndroidUser()));
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.CONFIG + Path.OperationUrl.UPDATE, method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<StatusResponse> update(@RequestBody ConfigVersionDTO configVersionDTO)  {

        StatusResponse sr = new StatusResponse();
        boolean status = configVersionModel.update(configVersionDTO);
        if(status){
            sr.setMessage(messageService.getMessage("version_updated",configVersionDTO.getAndroidUser()));
        }
        else {
            sr.setStatus(true);
            sr.setMessage(messageService.getMessage("version_wrong", configVersionDTO.getAndroidUser()));
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.CONFIG + Path.OperationUrl.GETALL, method = RequestMethod.GET, produces = {"application/json" })
    public ConfigVersion getVersionAll() {
        return configVersionModel.getId();
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.CONFIG + "/google/Map/Key/" + Path.OperationUrl.GETALL, method = RequestMethod.GET, produces = {"application/json" })
    public String getGoogleApi() {
        return "AIzaSyC6TkrTQ8bxU1iq4JusBbPKSexSi6aES6Q";
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.CONFIG + "/google/FCM/Key/" + Path.OperationUrl.GETALL, method = RequestMethod.GET, produces = {"application/json" })
    public String getGoogleFCMKey() {
        return "AIzaSyC6TkrTQ8bxU1iq4JusBbPKSexSi6aES6Q";
    }


    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.CONFIG + Path.Url.VERSION_V1 + "/domain/{domain}/{supplierId}", method = RequestMethod.GET, produces = {"application/json" })
    public ResponseEntity<StatusResponse> getDomainConfigDomain(@PathVariable String domain,@PathVariable String supplierId) {

        ConfigVersion configVersion =  configRepository.findByDomainContainingIgnoreCase(domain);
        List<DomainConfig> listDTO = new ArrayList<>(0);
        StatusResponse sr = new StatusResponse();
        DomainConfig dt = new DomainConfig();
        dt.setDomain("In");
        dt.setCurrency("Rs");
        dt.setIsPriceFlag(1);

        listDTO.add(dt);

        DomainConfig dt2 = new DomainConfig();
        dt2.setDomain("CH");
        dt2.setCurrency("CH");
        dt2.setIsPriceFlag(0);

        listDTO.add(dt2);


        sr.setData(configVersion);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.CONFIG + Path.Url.VERSION_V1 + "/domain/{domain}", method = RequestMethod.GET, produces = {"application/json" })
    public ResponseEntity<StatusResponse> getDomainConfigDomainSupplierId(@PathVariable String domain) {

        ConfigVersion configVersion =  configRepository.findByDomainContainingIgnoreCase(domain);
        List<DomainConfig> listDTO = new ArrayList<>(0);
        StatusResponse sr = new StatusResponse();
        DomainConfig dt = new DomainConfig();
        dt.setDomain("In");
        dt.setCurrency("Rs");
        dt.setIsPriceFlag(1);

        listDTO.add(dt);

        DomainConfig dt2 = new DomainConfig();
        dt2.setDomain("CH");
        dt2.setCurrency("CH");
        dt2.setIsPriceFlag(0);

        listDTO.add(dt2);


        sr.setData(configVersion);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }




}
