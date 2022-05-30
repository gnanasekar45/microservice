package com.luminn.firebase.model;


import com.luminn.firebase.dto.PhoneBookingDTO;
import com.luminn.firebase.dto.PhoneBookingSMS;
import com.luminn.firebase.dto.PhoneBookingsStatus;
import com.luminn.firebase.entity.*;
import com.luminn.firebase.helper.Converter;
import com.luminn.firebase.repository.PhoneBookingRepository;
import com.luminn.firebase.repository.UserRepository;
import com.luminn.firebase.service.PhoneBookingService;
import com.luminn.firebase.util.SMSType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PhoneBookingModel {


    @Autowired
    UserModel userModel;

    @Autowired
    MobileModel mobileModel;

    @Autowired
    TaxiModel taxiModel;

    @Autowired
    PriceModel priceModel;

    @Autowired
    TaxiDetailModel taxiDetailModel;

    @Autowired
    PhoneBookingRepository phoneBookingRepository;

    @Autowired
    UserRepository userRepository;




    public float roughPrice = 0;
    public ModelStatus create(PhoneBookingDTO phoneBookingDTO) {

        String supplierId = null;

        supplierId = checkSupplier(phoneBookingDTO);
        if(supplierId  != null) {
            PhoneBooking entity = Converter.dtoToEntity(phoneBookingDTO);
            entity.setSupplierId(supplierId);
            phoneBookingRepository.save(entity);
        }
        else {

        }
        System.out.println("Before --");
        mobileModel.bookRidebySMS(phoneBookingDTO);
        System.out.println("After --");
        return ModelStatus.CREATED;
    }

    public ModelStatus createUserOffline(PhoneBookingDTO phoneBookingDTO) {

        String supplierId = null;

        supplierId = checkSupplier(phoneBookingDTO);
        if(supplierId  != null) {
            PhoneBooking entity = Converter.dtoToEntity(phoneBookingDTO);
            entity.setSupplierId(supplierId);
            phoneBookingRepository.save(entity);
        }
        else {

        }
        System.out.println("Before --");
        //mobileModel.toUserOfflineSMS(phoneBookingDTO);
        //mobileModel.rideFinsehdSMS(phoneBookingDTO);
        System.out.println("After --");
        return ModelStatus.CREATED;
    }

    public ModelStatus createUserOfflineSMS(PhoneBookingSMS phoneBookingDTO) {

        String supplierId = null;
        String driverId = null;
        supplierId = checkSupplier(phoneBookingDTO);

        String pin =  mobileModel.generatePIN(phoneBookingDTO.getDriverPhoneNumber(), SMSType.RIDE);

        System.out.println(" pin --> " + pin);
        if(supplierId != null) {
           // PhoneBooking entity = Converter.dtoToEntity(phoneBookingDTO);
           // entity.setSupplierId(supplierId);
           // phoneBookingService.create(entity);

        }
        if(pin != null && pin.length() > 2){
            phoneBookingDTO.setOTP(pin);
        }
        //This is for price calculation and give wrong calucaltion
        if(phoneBookingDTO.getDriverId() != null){

        }
        else {

        }
        //System.out.println("Before --");
       if(phoneBookingDTO.getDriverId()!= null) {
            driverId = findDriverId(phoneBookingDTO.getDriverId());
            //
       }

        mobileModel.toUserOfflineSMS(phoneBookingDTO);
        //mobileModel.rideFinsehdSMS(phoneBookingDTO);
        System.out.println("After --");
        return ModelStatus.CREATED;
    }

    public float calculatePercentage(float obtained, float total) {
                return obtained * 100 / total;
    }

    public ModelStatus rideFinsehdSMS(PhoneBookingSMS phoneBookingDTO) {

        mobileModel.rideFinsehdSMS(phoneBookingDTO);
        return ModelStatus.CREATED;
    }

    public ModelStatus findDriver(String driverId){
        User user = null;
        if(driverId != null)
            user = userModel.getById(driverId);

        if(user != null && user.getId() != null)
         return ModelStatus.CREATED;
        else
            return ModelStatus.DRIVER_IS_NOT_EXISTING;
    }

    public String findDriverId(String driverId){
        User user = null;
        if(driverId != null) {
            user = userModel.getById(driverId);
            return user.getId();
        }
        else
            return null;

    }





    private String checkSupplier(PhoneBookingDTO phoneBookingDTO)  {

        //Supplier supplier = supplierModel.getById(userInfoDTO.getSupplierId());
        TaxiDetail td =  taxiDetailModel.findByDriverId(phoneBookingDTO.getDriverId());

        String supplierId = td.getSupplierId();
        if(supplierId != null  ){
            //driverBillingService.createWithID(Converter.dtoToEntityInfo(userInfoDTO,null));
            //return UserStatus.CREATED;
            return supplierId;
        }

        return null;
    }

    private String checkSupplier(String driverId) {

        //Supplier supplier = supplierModel.getById(userInfoDTO.getSupplierId());
        TaxiDetail td = taxiDetailModel.findByDriverId(driverId);
        String supplierId = td.getSupplierId();

        if(supplierId != null ){
            //driverBillingService.createWithID(Converter.dtoToEntityInfo(userInfoDTO,null));
            //return UserStatus.CREATED;
            return supplierId;
        }

        return null;
    }
    private String checkSupplier(PhoneBookingSMS phoneBookingDTO) {

        //Supplier supplier = supplierModel.getById(userInfoDTO.getSupplierId());


        TaxiDetail td = taxiDetailModel.findByDriverId(phoneBookingDTO.getDriverId());
        String supplierId = td.getSupplierId();

        if(supplierId != null && supplierId != null ){
            //driverBillingService.createWithID(Converter.dtoToEntityInfo(userInfoDTO,null));
            //return UserStatus.CREATED;
            return supplierId;
        }

        return  null;
    }

    public List<PhoneBookingDTO> getAllPhoneBooking(String supplierId) {

        List<PhoneBooking> userInfo = phoneBookingRepository.findBySupplierId(supplierId);
        List<PhoneBookingDTO> list = new ArrayList<>();
        if(userInfo != null && userInfo.size() > 0) {
            for (PhoneBooking bill : userInfo) {
                list.add(Converter.entityToDto(bill));
            }
            return list;
        }
        else
            return  list;
    }

    public List<PhoneBookingDTO> getAlldrivers(String drivers) {

        List<PhoneBooking> userInfo = phoneBookingRepository.findByDriverId(drivers);

        System.out.println(" --?? " + userInfo.size());
        List<PhoneBookingDTO> list = new ArrayList<>();
        if(userInfo != null && userInfo.size() > 0) {
            for (PhoneBooking bill : userInfo) {
                if(bill.getIsMobile() != null && bill.getIsMobile().equalsIgnoreCase("WEB"))
                    list.add(Converter.entityToDto(bill));
            }
            return list;
        }
        else
            return  list;
    }

    public PhoneBookingDTO getBooking(String id)  {
        PhoneBooking userInfo = null;
        Optional<PhoneBooking> ouserInfo = phoneBookingRepository.findById(id);

        if(ouserInfo.isPresent())
            ouserInfo.get();


        return Converter.entityToDto(userInfo);

    }


    public void createPhoneBooking(String userId, UserOffline userOffline){

        PhoneBookingDTO phoneBookingDTO = new PhoneBookingDTO();
        phoneBookingDTO.setUserId(userId);
        PhoneBooking entity = Converter.dtoToEntity(phoneBookingDTO);
        String id = phoneBookingRepository.save(entity).getId();
        System.out.println(" phone booking id --" + id);
        //return id;
    }

    public String createPhoneBookingSMS(String userId,PhoneBookingSMS phoneBookingSMS) {

        String supplierID = checkSupplier(phoneBookingSMS.getDriverId());
        PhoneBooking entity = Converter.dtoToEntity(phoneBookingSMS,supplierID);
        return phoneBookingRepository.save(entity).getId();
    }

    public ModelStatus update(PhoneBookingsStatus phoneBookingsStatus) {

        String supplierId = null;


        Optional<PhoneBooking> ouserInfo = phoneBookingRepository.findById(phoneBookingsStatus.getBookingId());

        if(ouserInfo.isPresent()) {
            PhoneBooking phoneBookings = ouserInfo.get();
            if(phoneBookingsStatus.getStatus().equalsIgnoreCase("ACCEPT"))
            phoneBookings.setStatus(RIDESTATUS.ACCEPTED);
            else
                phoneBookings.setStatus(RIDESTATUS.REJECT);
            phoneBookings.setComments(phoneBookingsStatus.getComment());
            //phoneBookings.setDriverId();
            phoneBookingRepository.save(phoneBookings);
        }


        return ModelStatus.UPDATED;

    }

}
