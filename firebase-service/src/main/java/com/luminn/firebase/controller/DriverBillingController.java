package com.luminn.firebase.controller;

import com.google.firebase.database.DatabaseException;
import com.luminn.firebase.dto.DriverBillingDTO;
import com.luminn.firebase.dto.DriverBillingDetailsDTO;
import com.luminn.firebase.dto.PriceDTO;
import com.luminn.firebase.entity.DriverBill;
import com.luminn.firebase.entity.TaxiDetail;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.model.DriverBillingModel;
import com.luminn.firebase.model.ModelStatus;
import com.luminn.firebase.model.UserStatus;
import com.luminn.firebase.repository.DriverBillRepository;
import com.luminn.firebase.repository.UserRepository;
import com.luminn.firebase.service.MessageByLocaleService;
import com.luminn.firebase.util.Path;
import com.luminn.firebase.view.TaxiDetailView;
import com.luminn.view.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/billing")
public class DriverBillingController {

   @Autowired
   DriverBillingModel driverBillingModel;

    @Autowired
    private MessageByLocaleService messageService;

    @Autowired
    DriverBillRepository driverBillRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/v1/add")
    private ResponseEntity<StatusResponse> addDelivery(@RequestBody DriverBillingDTO dto) {
        StatusResponse statusResponse = new StatusResponse();
        statusResponse.setStatus(false);
        driverBillingModel.createDummy(dto);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
   }

    @PostMapping("/v1/get/{id}")
    private ResponseEntity<StatusResponse> get(@RequestBody DriverBillingDTO dto) {
        StatusResponse statusResponse = new StatusResponse();

        DriverBillingDTO dtos = driverBillingModel.get(dto.getDriverId());

        statusResponse.setStatus(true);
        statusResponse.setData(driverBillingModel.get(dto.getDriverId()));
        statusResponse.setMessage("SUCCESS");
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }


    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.DRIVER_BILLING + Path.Url.VERSION_V1 + Path.OperationUrl.CREATE, method = RequestMethod.POST, produces = {"application/json" })
    public ResponseEntity<StatusResponse> create(@RequestBody DriverBillingDTO userInfoDTO) throws DatabaseException {
        StatusResponse sr = new StatusResponse();
        UserStatus status = null;

        if(userInfoDTO.getId() != null && userInfoDTO.getId().length() > 5){
            status = driverBillingModel.updateAmount(userInfoDTO);
        }
        else
            status = driverBillingModel.create(userInfoDTO);

        if((status.name().equalsIgnoreCase(ModelStatus.CREATED.name()))) {
            sr.setStatus(true);
            sr.setData(userInfoDTO.getDriverId());
            sr.setMessage("SUCCESS");
        }
        else if((status.name().equalsIgnoreCase(ModelStatus.UPDATED.name()))) {
            sr.setStatus(true);
            sr.setData(userInfoDTO.getDriverId());
            sr.setMessage("SUCCESS");
        }
        else {
            sr.setStatus(false);
            sr.setData(userInfoDTO.getDriverId());
            sr.setMessage("FAILURE");
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.DRIVER_BILLING + Path.Url.VERSION_V1 + Path.OperationUrl.UPDATE, method = RequestMethod.PUT, produces = {"application/json" })
    public ResponseEntity<StatusResponse> update(@RequestBody DriverBillingDTO userInfoDTO) throws DatabaseException {
        StatusResponse sr = new StatusResponse();
        UserStatus status = driverBillingModel.updateAmount(userInfoDTO);
        if(status.equals(status.equals(ModelStatus.CREATED))) {
            sr.setStatus(true);
            sr.setData(userInfoDTO.getDriverId());
            sr.setMessage("Added Successfully ");
        }
        else {
            sr.setStatus(false);
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.DRIVER_BILLING + Path.Url.VERSION_V1 + "/allDrivers/{supplierId}/{driverId}", method = RequestMethod.GET, produces = {"application/json" })
    public ResponseEntity<StatusResponse> getAllDrivers(@PathVariable String supplierId) throws DatabaseException {
        StatusResponse sr = new StatusResponse();
        List<DriverBillingDTO> listDTO = driverBillingModel.getAllDrivers(supplierId);
        sr.setStatus(true);
        sr.setData(listDTO);
        sr.setMessage(messageService.getMessage("driver_billing", listDTO.size()));
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.DRIVER_BILLING + Path.Url.VERSION_V1 + "/{supplierId}"+"/{driverId}", method = RequestMethod.GET, produces = {"application/json" })
    public ResponseEntity<StatusResponse> getSupplierId(@PathVariable String driverId,@PathVariable String supplierId) throws DatabaseException {
        StatusResponse sr = new StatusResponse();
        DriverBillingDTO dto = null;
        if((driverId != null && driverId != null) &&  (supplierId != null && supplierId != null )){

            dto = driverBillingModel.getDriver(supplierId,driverId);
        }
        else {
            sr.setStatus(false);
            sr.setData(dto);
            sr.setInfoId(400);
            sr.setMessage(messageService.getMessage("driver_billing", dto.getCredit()));
        }

        if(dto != null){
            sr.setStatus(true);
            sr.setData(dto);
            sr.setInfoId(500);
            sr.setMessage(messageService.getMessage("driver_billing", dto.getCredit()));
        }
        else {
            sr.setStatus(true);
            DriverBillingDTO db = new DriverBillingDTO();
            db.setCredit(0);
            db.setBalance(0);
            sr.setData(new DriverBillingDTO());
            sr.setMessage(messageService.getMessage("driver_billing", 0));
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.DRIVER_BILLING + Path.Url.VERSION_V1  +"/pagenation" +Path.OperationUrl.USER, method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity<Map<String, Object>> getBillings(@RequestParam(required = true) String id,
                                                                                   @RequestParam(defaultValue = "0") int page,
                                                                                   @RequestParam(defaultValue = "3") int size)  {



        try {

            Pageable paging = PageRequest.of(page, size);
            Page<DriverBill> pageTuts = null;
            List<DriverBill> driverBills = new ArrayList<DriverBill>();

            if (id != null)
                pageTuts = driverBillRepository.findBySupplierId(id, paging);

            driverBills = pageTuts.getContent();
            //List<DriverBillingDTO> data = driverBillingModel.getDriverDTO(driverBills);
            List<DriverBillingDetailsDTO> data = driverBillingModel.getDriverDetailDTO(driverBills);

            Map<String, Object> response = new HashMap<>();
            response.put("taxiDetails", data);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String  getDriverId(String name){
        User user = userRepository.findByfirstNameContainingIgnoreCase(name);
        if(user != null && user.getRole() != null && user.getRole().equalsIgnoreCase("ROLE_DRIVER")){
            user.getId();
        }
        return null;

    }

    public String  getDriverId(String name,Pageable page){
        Page<User> users = userRepository.findByfirstNameContainingIgnoreCase(name,page);
        if(users != null && users.get() != null){

            Optional<User> user =  users.get().filter(a -> a.getRole().equalsIgnoreCase("ROLE_DRIVER")).findFirst();
            String driverIds = null;

            if(user.isPresent())
                driverIds =  user.get().getId().trim();

            List<User> drivers = users.getContent();
            for(User driver : drivers) {
                if(driver.getId() != null && driver.getRole().equalsIgnoreCase("ROLE_DRIVER"))
                    return driver.getId();

            }
        }
        return null;
    }

    @RequestMapping(value = Path.Url.APP + Path.Url.DRIVER_BILLING + Path.Url.VERSION_V1  +"/filter/pagenation" +Path.OperationUrl.USER, method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity<Map<String, Object>> getBillingsFilter(@RequestParam(required = true) String id,
                                                                 @RequestParam(required = true) String name,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "3") int size)  {


        try {

            Pageable paging = PageRequest.of(page, size);
            Page<DriverBill> pageTuts = null;
            List<DriverBill> driverBills = new ArrayList<DriverBill>();

            String driverID =  getDriverId(name);
            if (id != null)
                pageTuts = driverBillRepository.findByDriverId(driverID, paging);

            driverBills = pageTuts.getContent();

            List<DriverBillingDetailsDTO> data = driverBillingModel.getDriverDetailDTO(driverBills);
            Map<String, Object> response = new HashMap<>();
            response.put("taxiDetails", data);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.DRIVER_BILLING + Path.Url.VERSION_V1 + "/fee", method = RequestMethod.PUT, produces = {"application/json" })
    public ResponseEntity<StatusResponse> debit(@RequestBody DriverBillingDTO userInfoDTO)  {
        StatusResponse sr = new StatusResponse();
        UserStatus dto = driverBillingModel.totalFee(userInfoDTO.getDriverId(),userInfoDTO.getDebit());
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.DRIVER_BILLING + Path.Url.VERSION_V1 + "/fee" + "/{driverId}/{percentage}", method = RequestMethod.GET, produces = {"application/json" })
    public ResponseEntity<StatusResponse> percentage(@PathVariable("driverId") String driverId,@PathVariable("percentage") String percentage)  {
        StatusResponse sr = new StatusResponse();
        sr.setData(driverBillingModel.calculatePercentage(Float.valueOf(percentage),driverId));
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

}
