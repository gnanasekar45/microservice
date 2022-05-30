package com.luminn.firebase.controller;


import com.google.firebase.database.DatabaseException;
import com.luminn.firebase.dto.*;
import com.luminn.firebase.entity.Ride;
import com.luminn.firebase.entity.Supplier;
import com.luminn.firebase.entity.TaxiDetail;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.helper.Converter;
import com.luminn.firebase.model.*;
import com.luminn.firebase.repository.RideRepository;
import com.luminn.firebase.repository.TaxiDetailRepository;
import com.luminn.firebase.repository.TripPoolRepository;
import com.luminn.firebase.repository.UserRepository;
import com.luminn.firebase.security.HashCodeUtils;
import com.luminn.firebase.service.MessageByLocaleService;
import com.luminn.firebase.service.SupplierService;
import com.luminn.firebase.service.UserService;
import com.luminn.firebase.util.Path;
import com.luminn.firebase.view.TaxiDetailView;
import com.luminn.firebase.view.TaxiView;
import com.luminn.view.StatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/admin")
public class adminController {

    @Autowired
    UserModel userModel;

    @Autowired
    TaxiModel taxiModel;

    @Autowired
    SupplierModel supplierModel;


    @Autowired
    UserService userService;

    @Autowired
    private MessageByLocaleService messageService;

    @Autowired
    private HashCodeUtils hashCodeUtils;

    @Autowired
    RideModel rideModel;

    @Autowired
    TripPoolModel tripPoolModel;
    @Autowired
    TaxiDetailRepository taxiDetailRepository;

    @Autowired
    RideRepository rideRepository;


    @Autowired
    UserRepository userRepository;



    @Autowired
    TripPoolRepository tripPoolRepository;
    private static final Logger log = LoggerFactory.getLogger(adminController.class);

    /*@ResponseBody
    @RequestMapping(value = "/app/admin/taxi/v1/delete" + "/{taxid}" , method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<StatusResponse> deleteUserTaxi(@PathVariable("taxid") String taxid) {

        Taxi taxi = taxiModel.getTaxiIds(taxid);
        StatusResponse sr = new StatusResponse();
        List<UserDTO> userDTO = null;

        //if (user != null)
        //   log.info("id ---" + user.getId() + "role ==" + taxid);
       /* else {
            ModelStatus status = userModel.deleteUser(userId, role);
            log.info("status ---" + status.name());

            if (status.equals(ModelStatus.DELETED)) {
                sr.setMessage(messageService.getMessage("user_deleted", userId));
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            sr.setMessage(messageService.getMessage("user_no_permission"));
        }*/

        //once
      /*  if (taxi != null) {

            ModelStatus status = userModel.deleteTaxi(taxid);
            log.info("status ---" + status.name());

            if (status.equals(ModelStatus.DELETED)) {
                sr.setMessage(messageService.getMessage("user_deleted", taxid));
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            sr.setMessage(messageService.getMessage("user_no_permission"));
        } else {
            sr.setMessage(messageService.getMessage("user_deleteted_exception", taxid));
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }*/
    @ResponseBody
    @RequestMapping(value = "/app/admin/taxidetail/v1/delete" + "/{taxid}" , method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<StatusResponse> deleteUserTaxiDetail(@PathVariable("taxid") String taxid)  {


        StatusResponse sr = new StatusResponse();
        List<UserDTO> userDTO = null;

        if (taxid != null ) {

            ModelStatus status = userModel.deleteTaxiDetail(taxid);

            log.info("status ---" + status.name());

            if (status.equals(ModelStatus.DELETED)) {
                sr.setMessage(messageService.getMessage("user_deleted", taxid));
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            sr.setMessage(messageService.getMessage("user_no_permission"));
        } else {
            sr.setMessage(messageService.getMessage("user_deleteted_exception", taxid));
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }



    @ResponseBody
    @RequestMapping(value = "/app/admin/driver/v1/delete" +"/{supplierId}" +"/{userId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<StatusResponse> deleteUserSupplier(@PathVariable("supplierId") String supplierId, @PathVariable("userId") String userId)  {

        Supplier supplier = supplierModel.getSupplierId(supplierId);
        User user = userModel.validate(userId);
        StatusResponse sr = new StatusResponse();
        List<UserDTO> userDTO = null;

        if (user != null)
            log.info("id ---" + user.getId() + "role ==" );

        //once
        if (user != null && supplier != null) {
            ModelStatus status = userModel.deleteUser(user);
            if (status.equals(ModelStatus.DELETED)) {
                sr.setMessage(messageService.getMessage("user_deleted", userId));
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            sr.setStatus(false);
            sr.setMessage(messageService.getMessage("user_no_permission"));
        }
        else if (user != null) {
            ModelStatus status = userModel.deleteUser(userId,user.getRole(),false );
            if (status.equals(ModelStatus.DELETED)) {
                sr.setMessage(messageService.getMessage("user_deleted", userId));
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            sr.setStatus(false);
            sr.setMessage(messageService.getMessage("user_no_permission"));
        }
        else {
            sr.setStatus(false);
            sr.setMessage(messageService.getMessage("user_deleteted_exception", userId));
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }
    @ResponseBody
    @RequestMapping(value = "/app/admin/user/v1/delete" + "/{userId}" + "/{role}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<StatusResponse> deleteUser(@PathVariable("userId") String userId, @PathVariable("role") String role)  {

        User user = userModel.validate(userId);
        StatusResponse sr = new StatusResponse();
        List<UserDTO> userDTO = null;

        if (user != null)
            log.info("id ---" + user.getId() + "role ==" + role);

        //once
        if (user != null) {

            ModelStatus status = userModel.deleteUser(userId, role,false);
            log.info("status ---" + status.name());

            if (status.equals(ModelStatus.DELETED)) {
                sr.setMessage(messageService.getMessage("user_deleted", userId));
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            sr.setMessage(messageService.getMessage("user_no_permission"));
        } else {
            sr.setMessage(messageService.getMessage("user_deleteted_exception", userId));
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }
    //@Secured(value = { Role.ROLE_ADMIN })
    /*@ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.TAXI + Path.Url.VERSION_V1 + "/approve"  + "s"+"/{adminId}", method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity<StatusResponse> listTaxisForApproval(@PathVariable("adminId") String adminId) {
        UserDTO user = userModel.getDTO(adminId);
        log.info(" user --->>>>"+ "user.--->" + user.getId() + "--->" + user.getRole()) ;
        StatusResponse sr = new StatusResponse();
        if(user != null && user.getRole().equals(Path.ROLE.ADMIN) || user.getRole().equals(Path.ROLE.SUPPLIER)) {
            List<TaxiView> taxiDTO = taxiModel.listTaxiForApprovals(adminId);
            sr.setStatus(true);
            sr.setData(taxiDTO);
            sr.setMessage(messageService.getMessage("success"));
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
        else{
            List<TaxiDTO> taxiDTO = new ArrayList<>(0);
            // sr.setStatus(fasle);
            sr.setMessage(messageService.getMessage("taxi_id_not_valid"));
            sr.setData(taxiDTO);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
    }*/

   /* @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.TAXI + Path.Url.VERSION_V2 + "/approve"  + "s"+"/{adminId}", method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity<StatusResponse> listTaxisDetailForApproval(@PathVariable("adminId") String adminId) {
        UserDTO user = userModel.getDTO(adminId);
        StatusResponse sr = new StatusResponse();
        if(user.getRole().equals(Path.ROLE.ADMIN) || (user.getRole().equals(Path.ROLE.SUPPLIER))) {

            //CK
            //List<TaxiDetailView> taxiDTO = taxiModel.listTaxiForApprovalss(adminId);
            List<TaxiView> taxiDTO = taxiModel.listTaxiForApprovalss(adminId);
            sr.setStatus(true);
            sr.setData(taxiDTO);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
        else{
            List<TaxiDTO> taxiDTO = new ArrayList<>(0);
            // sr.setStatus(fasle);
            sr.setMessage(messageService.getMessage("admin_no_permission"));
            sr.setData(taxiDTO);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
    }*/



    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.TAXI + Path.Url.VERSION_V1 +  Path.OperationUrl.MYTAXIES + "/approve/{supplierId}"+"/{userId}", method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity<StatusResponse> getmyTaxiesSupplierApprove(@PathVariable("supplierId") String supplierId, @PathVariable("userId") String userId)  {

        log.info(" --- supplierId 2--- " + supplierId);

        List<TaxiDetailView> taxiView = taxiModel.listMyTaxiesDetailBySupplier(supplierId,userId);

        log.info(" --- supplierId 2 --- " + supplierId);
        log.info(" --- userId 2--- " + userId);

        StatusResponse sr = new StatusResponse();
        sr.setStatus(true);
        sr.setData(taxiView);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.TAXI + Path.Url.VERSION_V1 + "/approve"+"/{taxiId}"+"/{supplierId}", method = RequestMethod.GET, produces = {"application/json" })
    public ResponseEntity<StatusResponse> approveTaxiById(@PathVariable("taxiId") String taxiId, @PathVariable("supplierId") String supplierId ) {

        StatusResponse sr = new StatusResponse();
        UserDTO user = null;

        Supplier supplier = supplierModel.getSupplierId(supplierId);

        if(supplier.getUserId() != null) {
            user = userModel.getDTO(supplier.getUserId());

        }
        else {
            System.out.println(" user is not existing");
        }

        log.info("" + user);
        //if ((user != null && user.getId() != null) || user.getRole().equals(Path.ROLE.ADMIN) || user.getRole().equals(Path.ROLE.SUPPLIER)) {
        if (supplier != null) {
            if (supplierId != null) {

                TaxiView taxiView = taxiModel.setActivate(taxiId, true);
                sr.setMessage(messageService.getMessage("taxi_approved", taxiId));
                sr.setStatus(true);
                //sr.setData("Success taxiId->" + taxiId);
                sr.setData(taxiView);
                //sr.setData( ModelStatus.UPDATED);
                return new ResponseEntity<>(sr, HttpStatus.OK);
            } else {
                sr.setStatus(false);
                sr.setMessage(messageService.getMessage("admin_no_permission", supplierId));
            }

            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.RIDE + Path.Url.VERSION_V1 + "/supplier" + Path.OperationUrl.GET + "/{supplierId}", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<StatusResponse> getAllDriverBySupplier(@PathVariable("supplierId") String supplierId) throws DatabaseException {
        StatusResponse sr = new StatusResponse();
        List<RideDTO> rideDTO = null;
        if (supplierId != null && rideModel.getUserRole(supplierId) != null ) {
            rideDTO = rideModel.getAllDriversBySupplier(supplierId, null);
            sr.setStatus(true);
            sr.setMessage("SUCCESS");
        }
        else {
            sr.setStatus(false);
            sr.setMessage(messageService.getMessage("taxi_ids_not_valid",supplierId));
        }
        //sr.setData(rideModel.rideId);
        sr.setData("§§§");
        sr.setData(rideDTO);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.TAXI + Path.Url.VERSION_V1 + "/disapprove"+"/{taxiId}"+"/{supplierId}", method = RequestMethod.GET, produces = {"application/json" })
    public ResponseEntity<StatusResponse> disapproveTaxiById(@PathVariable("taxiId") String taxiId, @PathVariable("supplierId") String supplierId )  {

        StatusResponse sr = new StatusResponse();
        UserDTO user = null;

        Supplier supplier = supplierModel.getSupplierId(supplierId);

        if(supplier.getUserId() != null) {
            user = userModel.getDTO(supplier.getUserId());

        }
        else {
            System.out.println(" user is not existing");
        }

        log.info("" + user);
        //if((user != null && user.getId() > 0) || user.getRole().equals(Path.ROLE.ADMIN) || user.getRole().equals(Path.ROLE.SUPPLIER)) {
        if(supplierId != null ){

            TaxiView taxiView = taxiModel.setActivate(taxiId, false);
            sr.setMessage(messageService.getMessage("taxi_disapproved",taxiId));
            sr.setStatus(true);
            //sr.setData("Success taxiId->" + taxiId);
            sr.setData(taxiView);
            //sr.setData( ModelStatus.UPDATED);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
        else {
            sr.setStatus(false);
            sr.setMessage(messageService.getMessage("admin_no_permission",supplierId));
        }

        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.TAXI + Path.Url.VERSION_V1 +  Path.OperationUrl.MYTAXIES + "/validation/{supplierId}", method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity<StatusResponse> getmyTaxiesSupplierApproveValidation(@PathVariable("supplierId") String supplierId) {

        log.info(" --- supplierId 2--- " + supplierId);
        List<TaxiDTO> taxiView = taxiModel.validationApproves(supplierId);

        log.info(" --- supplierId 2 --- " + supplierId);

        StatusResponse sr = new StatusResponse();
        sr.setStatus(true);
        sr.setData(taxiView);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.TAXIDETAILS + "/V3" + Path.OperationUrl.DRIVER + Path.OperationUrl.MYTAXIES + "/{supplierId}"+"/{driverId}", method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity<StatusResponse> getmyTaxiesByDriver(@PathVariable("supplierId") String supplierId, @PathVariable("driverId") String driverId)  {

        List<TaxiDTO> taxiDTO = taxiModel.listMyTaxiesDriver(supplierId,driverId);

        StatusResponse sr = new StatusResponse();
        sr.setData(taxiDTO);
        return new ResponseEntity<>(sr, HttpStatus.OK);

    }

    //@CrossOrigin(origins="*")
    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.TAXI + Path.Url.VERSION_V1 +  Path.OperationUrl.MYTAXIES + Path.OperationUrl.USER + "/{supplierId}"+"/{userId}", method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity<StatusResponse> getmyTaxiesDetailSupplier(@PathVariable("supplierId") String supplierId, @PathVariable("userId") String userId)  {

        log.info(" --- supplierId 2--- " + supplierId);

        List<TaxiDetailView> taxiView = taxiModel.listMyTaxiesDetailBySupplier(supplierId,userId);


        StatusResponse sr = new StatusResponse();
        sr.setStatus(true);
        sr.setData(taxiView);
        sr.setInfoId(1000);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.TAXI + Path.Url.VERSION_V2 +  Path.OperationUrl.MYTAXIES + Path.OperationUrl.USER + "/{supplierId}"+"/{userId}"+
            "/{phoneNumber}"+"/{numberPlate}"+"/{name}" , method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity<StatusResponse> getmyTaxiesDetailSupplierPhoneNumber(@PathVariable("supplierId") String supplierId,
                                                                               @PathVariable("userId") String userId,
                                                                               @PathVariable("phoneNumber") String phoneNumber,
                                                                               @PathVariable("numberPlate") String numberPlate,
                                                                               @PathVariable("name") String name)  {

        log.info(" --- supplierId 2--- " + supplierId);

        List<TaxiDetailView> taxiView = taxiModel.listMyTaxiesDetailBySupplierPhoneNumber(supplierId,userId,phoneNumber,numberPlate,name);


        StatusResponse sr = new StatusResponse();
        sr.setStatus(true);
        sr.setData(taxiView);
        sr.setInfoId(1000);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }





    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.TAXI + Path.Url.VERSION_V1 +  Path.OperationUrl.MYTAXIES +"/pagenation" +Path.OperationUrl.USER, method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity<Map<String, Object>> getmyTaxiesDetailSupplierPageSecond(@RequestParam(required = true) String id,
                                                                             @RequestParam(defaultValue = "0") int page,

                                                                             @RequestParam(defaultValue = "3") int size)  {

        log.info(" --- supplierId 2--- " + id);

        try {

            Pageable paging = PageRequest.of(page, size);
            Page<TaxiDetail> pageTuts;
            List<TaxiDetail> taxiDetails = new ArrayList<TaxiDetail>();

            if (id == null)
                pageTuts = taxiDetailRepository.findAll(paging);
            else
                pageTuts = taxiDetailRepository.findBySupplierIds(id, paging);

            taxiDetails = pageTuts.getContent();

            List<TaxiDetailView> data = taxiModel.listMyTaxiesDetail(taxiDetails,false);
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
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V1 + "/ride" + Path.OperationUrl.GET, method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> getAllRideDriverBySupplier(@RequestParam(required = true) String id,
                                                                     @RequestParam(defaultValue = "0") int page,

                                                                     @RequestParam(defaultValue = "3") int size)  {
        StatusResponse sr = new StatusResponse();
        List<RideDTO> rideDTO = null;

        try {
            Pageable paging = PageRequest.of(page, size);
            Page<Ride> pageTuts;
            List<Ride> ride = new ArrayList<Ride>();

            if (id == null)
                pageTuts = rideRepository.findAll(paging);
            else
                pageTuts = rideRepository.findBySupplierId(id, paging);
            ride = pageTuts.getContent();

            if (id != null && rideModel.getUserRole(id) != null) {
                rideDTO = rideModel.getRidetoDTO(ride);
                sr.setStatus(true);
                sr.setMessage("SUCCESS");
            } else {
                sr.setStatus(false);
                sr.setMessage(messageService.getMessage("taxi_ids_not_valid", id));
            }
            Map<String, Object> response = new HashMap<>();
            response.put("taxiDetails", rideDTO);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V1 + "/trip" + Path.OperationUrl.GET, method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> getAllTripBySupplier(@RequestParam(required = true) String id,
                                                                          @RequestParam(defaultValue = "0") int page,

                                                                          @RequestParam(defaultValue = "3") int size)  {
        StatusResponse sr = new StatusResponse();
        List<TripPool> tripDTO = null;
        List<TripDTO> tripDTOs = null;
        try {
            Pageable paging = PageRequest.of(page, size);
            Page<TripPool> pageTuts;
            List<TripPool> tripPool = new ArrayList<TripPool>();


              pageTuts = tripPoolRepository.findBySupplierId(id, paging);
                tripPool = pageTuts.getContent();

            if (id != null && rideModel.getUserRole(id) != null) {
                tripDTOs = tripPoolModel.getTripDTO(tripPool);
                sr.setStatus(true);
                sr.setMessage("SUCCESS");
            } else {
                sr.setStatus(false);
                sr.setMessage(messageService.getMessage("taxi_ids_not_valid", id));
            }
            Map<String, Object> response = new HashMap<>();
            response.put("taxiDetails", tripDTOs);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Autowired
    SupplierService supplierService;

    @ResponseBody
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V1 + "/users" + Path.OperationUrl.GET, method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> getAllUsers(@RequestParam(required = true) String domain,
                                                           @RequestParam(required = true) String id,
                                                           @RequestParam(defaultValue = "0") int page,

                                                           @RequestParam(defaultValue = "3") int size)  {
        StatusResponse sr = new StatusResponse();
        List<User> user = null;
        List<UsersDTO> userDTO = null;
        try {
            //Sort.by(Sort.Direction.ASC, "seatNumber"))
            //Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"createdDate"));
            Pageable paging = PageRequest.of(page, size);
            Page<User> pageTuts;
            List<User> users = new ArrayList<User>();
            String domains = null;

            Supplier supplier = supplierService.findById(id);
            if(supplier != null)
                domains = supplier.getName();

            pageTuts = userRepository.findByDomain(domains, paging);
            //pageTuts = userRepository.findByDomainAndRole(domains,"ROLE_USER" ,paging);
            users = pageTuts.getContent();

            if (domains != null ) {
                userDTO = userModel.getDTOUsers(users);
                sr.setStatus(true);
                sr.setMessage("SUCCESS");
            } else {
                sr.setStatus(false);
                sr.setMessage(messageService.getMessage("taxi_ids_not_valid", id));
            }
            Map<String, Object> response = new HashMap<>();
            response.put("taxiDetails", userDTO);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V1 + "/users/search" + Path.OperationUrl.GET, method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> getAllUsersFirstname(@RequestParam String role,
                                                                    @RequestParam String firstName,
                                                                    @RequestParam String id,
                                                           @RequestParam(defaultValue = "0") int page,

                                                           @RequestParam(defaultValue = "3") int size)  {
        StatusResponse sr = new StatusResponse();
        List<User> user = null;
        List<UsersDTO> userDTO = null;
        try {
            Pageable paging = PageRequest.of(page, size);
            Page<User> pageTuts;
            List<User> users = new ArrayList<User>();

            //userModel.getUserData(role,firstName,id,page,size);
            String domains = null;

            //Supplier supplier = supplierService.findById(id);
            //if(supplier != null)
             //   domains = supplier.getName();

            pageTuts = userRepository.findByfirstNameContainingIgnoreCase(firstName, paging);
            users = pageTuts.getContent();

            if (users != null ) {
                userDTO = userModel.getDTOUsers(users);
                sr.setStatus(true);
                sr.setMessage("SUCCESS");
            } else {
                sr.setStatus(false);
                sr.setMessage(messageService.getMessage("taxi_ids_not_valid", id));
            }
            Map<String, Object> response = new HashMap<>();
            response.put("taxiDetails", userDTO);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V1 + "/users/role" + Path.OperationUrl.GET, method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> getRole(@RequestParam(required = true) String firstName,@RequestParam(required = true) String id,
                                                                    @RequestParam(defaultValue = "0") int page,

                                                                    @RequestParam(defaultValue = "3") int size)  {
        StatusResponse sr = new StatusResponse();
        List<User> user = null;
        List<UsersDTO> userDTO = null;
        try {
            Pageable paging = PageRequest.of(page, size);
            Page<User> pageTuts;
            List<User> users = new ArrayList<User>();
            String domains = null;

            //Supplier supplier = supplierService.findById(id);
            //if(supplier != null)
            //   domains = supplier.getName();

            pageTuts = userRepository.findByroleContainingIgnoreCase(firstName, paging);
            users = pageTuts.getContent();

            if (users != null ) {
                userDTO = userModel.getDTOUsers(users);
                sr.setStatus(true);
                sr.setMessage("SUCCESS");
            } else {
                sr.setStatus(false);
                sr.setMessage(messageService.getMessage("taxi_ids_not_valid", id));
            }
            Map<String, Object> response = new HashMap<>();
            response.put("taxiDetails", userDTO);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
