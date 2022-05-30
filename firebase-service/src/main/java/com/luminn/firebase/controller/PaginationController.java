package com.luminn.firebase.controller;


import com.luminn.firebase.dto.*;
import com.luminn.firebase.entity.*;
import com.luminn.firebase.model.*;
import com.luminn.firebase.repository.*;
import com.luminn.firebase.security.HashCodeUtils;
import com.luminn.firebase.service.MessageByLocaleService;
import com.luminn.firebase.service.SupplierService;
import com.luminn.firebase.service.UserService;
import com.luminn.firebase.util.Path;
import com.luminn.firebase.view.TaxiDetailView;
import com.luminn.view.StatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pagination")
public class PaginationController {

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
    PhoneBookingModel phoneBookingModel;

    @Autowired
    PhoneBookingRepository phoneBookingRepository;



    @Autowired
    TripPoolRepository tripPoolRepository;
    private static final Logger log = LoggerFactory.getLogger(PaginationController.class);



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

    @ResponseBody
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V2 + "/trip" + Path.OperationUrl.GET, method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> getAllTripDetailBySupplier(@RequestParam(required = true) String id,
                                                                    @RequestParam(defaultValue = "0") int page,

                                                                    @RequestParam(defaultValue = "3") int size)  {
        StatusResponse sr = new StatusResponse();
        List<TripPool> tripDTO = null;
        List<TripDetailDTO> tripDTOs = null;
        try {
            Pageable paging = PageRequest.of(page, size);
            Page<TripPool> pageTuts;
            List<TripPool> tripPool = new ArrayList<TripPool>();


            pageTuts = tripPoolRepository.findBySupplierId(id, paging);
            tripPool = pageTuts.getContent();

            if (id != null && rideModel.getUserRole(id) != null) {
                tripDTOs = tripPoolModel.getTripDetailDTO(tripPool);
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
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V2 + "/users/search" + Path.OperationUrl.GET, method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> getAllUsersFirstname(@RequestParam String role,
                                                                    @RequestParam String firstName,
                                                                    @RequestParam String phoneNumber,
                                                                    @RequestParam String supplierId,
                                                                    @RequestParam String numberPlate,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "3") int size)  {
        StatusResponse sr = new StatusResponse();
        List<User> user = null;
        List<UsersDTO> userDTO = null;
        int currentPage = 0;
        long totalItems = 0;
        int totalPages =0;
        try {
            Pageable paging = PageRequest.of(page, size);
            Page<User> pageTuts;
            List<User> users = new ArrayList<User>();

            //userModel.getUserData(role,firstName,id,page,size);
            String domains = null;
            domains = userModel.getDTOUsersDomain(supplierId);

             if(domains != null && phoneNumber != null && !"".equalsIgnoreCase(phoneNumber) && !phoneNumber.equalsIgnoreCase("string")){
                pageTuts = userRepository.findByphoneNumberContainingIgnoreCaseAndDomain(phoneNumber,domains, paging);
                users = pageTuts.getContent();
                currentPage = pageTuts.getNumber();
                totalItems = pageTuts.getTotalElements();
                totalPages = pageTuts.getTotalPages();
            }
            else if(domains != null && role != null && !"".equalsIgnoreCase(role) && !role.equalsIgnoreCase("string")){
                pageTuts = userRepository.findByRoleContainingIgnoreCaseAndDomain(role,domains, paging);
                users = pageTuts.getContent();
                currentPage = pageTuts.getNumber();
                totalItems = pageTuts.getTotalElements();
                totalPages = pageTuts.getTotalPages();
            }
            else if(domains != null && firstName != null && !"".equalsIgnoreCase(firstName) ){
                 pageTuts = userRepository.findByfirstNameContainingIgnoreCaseAndDomain(firstName,domains, paging);
                 users = pageTuts.getContent();
                currentPage = pageTuts.getNumber();
                totalItems = pageTuts.getTotalElements();
                totalPages = pageTuts.getTotalPages();
            }

            if (users != null ) {
                userDTO = userModel.getDTOUsersDomain(users,supplierId);
                sr.setStatus(true);
                sr.setMessage("SUCCESS");
            } else {
                sr.setStatus(false);
                sr.setMessage(messageService.getMessage("taxi_ids_not_valid", supplierId));
            }
            Map<String, Object> response = new HashMap<>();
            response.put("taxiDetails", userDTO);
            response.put("currentPage", currentPage);
            response.put("totalItems", totalItems);
            response.put("totalPages", totalPages);
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

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.PHONEBOOKING + Path.Url.VERSION_V1 + "/allBookings/{supplierId}", method = RequestMethod.GET, produces = {"application/json" })
    public ResponseEntity<Map<String, Object>> getAllDrivers(@RequestParam(required = true) String supplierId,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "3") int size)  {

        try {
            Pageable paging = PageRequest.of(page, size);
            Page<PhoneBooking> pageTuts = null;
            List<PhoneBooking> PhoneBookings = new ArrayList<PhoneBooking>();

            if (supplierId != null)
                pageTuts = phoneBookingRepository.findBySupplierId(supplierId,paging);
            PhoneBookings = pageTuts.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("phoneBooking", PhoneBookings);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);

        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        /*StatusResponse sr = new StatusResponse();
        List<PhoneBookingDTO> listDTO = phoneBookingModel.getAllPhoneBooking(supplierId);
        sr.setStatus(true);
        sr.setData(listDTO);
        sr.setMessage(messageService.getMessage("driver_billing", listDTO.size()));
        return new ResponseEntity<>(sr, HttpStatus.OK);*/
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

            List<TaxiDetailView> data = taxiModel.listMyTaxiesDetail(taxiDetails,true);
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
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V2 + "/approve/role" + Path.OperationUrl.GET, method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> getApprove(@RequestParam(defaultValue = "AAA") String firstName,
                                                          @RequestParam(required = true) String supplierId,
                                                          @RequestParam(required = true) String taxiId,
                                                          @RequestParam(defaultValue = "111") String phoneNumber,
                                                          @RequestParam(defaultValue = "NO") String flag,
                                                          @RequestParam(defaultValue = "0") int page,

                                                          @RequestParam(defaultValue = "3") int size)  {
        StatusResponse sr = new StatusResponse();
        List<User> user = null;
        List<UsersDTO> userDTO = null;
        try {

            //List<TaxiDetailView> taxiView = taxiModel.listMyTaxiesDetailBySupplier(supplierId,userId);
            List<TaxiDetailView> data = null;
            Pageable paging = PageRequest.of(page, size);
            Page<TaxiDetail> pageTuts;
            List<TaxiDetail> taxiDetails = new ArrayList<TaxiDetail>();

            if (supplierId == null)
                pageTuts = taxiDetailRepository.findAll(paging);
            else
                pageTuts = taxiDetailRepository.findBySupplierIds(supplierId, paging);

            if(phoneNumber != null && phoneNumber.length() > 7){
                if(flag != null && flag.equalsIgnoreCase("YES"))
                  data = taxiModel.listMyTaxiesDetail(phoneNumber,true);
                else
                    data = taxiModel.listMyTaxiesDetail(phoneNumber,false);
            }
            else {
                taxiDetails = pageTuts.getContent();
                if(flag != null && flag.equalsIgnoreCase("YES"))
                 data = taxiModel.listMyTaxiesDetail(taxiDetails,true);
                else
                    data = taxiModel.listMyTaxiesDetail(taxiDetails,false);
            }




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
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V1 + "/approve/role" + Path.OperationUrl.GET, method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> getApprove(@RequestParam(required = true) String firstName,
                                                       @RequestParam(required = true) String supplierId,
                                                       @RequestParam(required = true) String taxiId,
                                                       @RequestParam(defaultValue = "0") int page,

                                                       @RequestParam(defaultValue = "3") int size)  {
        StatusResponse sr = new StatusResponse();
        List<User> user = null;
        List<UsersDTO> userDTO = null;
        try {

            //List<TaxiDetailView> taxiView = taxiModel.listMyTaxiesDetailBySupplier(supplierId,userId);




                Pageable paging = PageRequest.of(page, size);
                Page<TaxiDetail> pageTuts;
                List<TaxiDetail> taxiDetails = new ArrayList<TaxiDetail>();

                if (supplierId == null)
                    pageTuts = taxiDetailRepository.findAll(paging);
                else
                    pageTuts = taxiDetailRepository.findBySupplierIds(supplierId, paging);

                taxiDetails = pageTuts.getContent();

                List<TaxiDetailView> data = taxiModel.listMyTaxiesDetail(taxiDetails,true);
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
    @RequestMapping(value = Path.Url.APP +  Path.Url.VERSION_V1 + "/disapprove/role" + Path.OperationUrl.GET, method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> getdisapprove(@RequestParam(required = true) String firstName,
                                                       @RequestParam(required = true) String supplierId,
                                                       @RequestParam(required = true) String taxiId,
                                                       @RequestParam(defaultValue = "0") int page,

                                                       @RequestParam(defaultValue = "3") int size)  {
        StatusResponse sr = new StatusResponse();
        List<User> user = null;
        List<UsersDTO> userDTO = null;
        try {

            //List<TaxiDetailView> taxiView = taxiModel.listMyTaxiesDetailBySupplier(supplierId,userId);




            Pageable paging = PageRequest.of(page, size);
            Page<TaxiDetail> pageTuts;
            List<TaxiDetail> taxiDetails = new ArrayList<TaxiDetail>();

            if (supplierId == null)
                pageTuts = taxiDetailRepository.findAll(paging);
            else
                pageTuts = taxiDetailRepository.findBySupplierIds(supplierId, paging);

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
}
