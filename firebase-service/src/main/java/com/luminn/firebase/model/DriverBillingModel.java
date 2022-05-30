package com.luminn.firebase.model;


import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseException;
import com.luminn.firebase.dto.DriverBillingDTO;
import com.luminn.firebase.dto.DriverBillingDetailsDTO;
import com.luminn.firebase.dto.UserDTO;
import com.luminn.firebase.entity.AuditBill;
import com.luminn.firebase.entity.DriverBill;
import com.luminn.firebase.entity.TaxiDetail;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.helper.Converter;
import com.luminn.firebase.repository.AuditBillRepository;
import com.luminn.firebase.repository.DriverBillRepository;
import com.luminn.firebase.repository.TaxiDetailRepository;
import com.luminn.firebase.service.TaxiDetailService;
import com.luminn.firebase.service.UserMongoService;
import com.luminn.firebase.util.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class DriverBillingModel {

    @Autowired
    DriverBillRepository driverBillRepository;

    @Autowired
    UserModel userModel;

    @Autowired
    SupplierModel  supplierModel;

    @Autowired
    TaxiModel  taxiModel;

    @Autowired
    TaxiDetailService taxiDetailService;

    @Autowired
    AuditBillRepository auditBillRepository;

    private static final Logger log = LoggerFactory.getLogger(DriverBillingModel.class);

    @Autowired
    TaxiDetailRepository taxiDetailRepository;
    private boolean checkAccess(DriverBillingDTO userInfoDTO)  {

        //Supplier supplier = supplierModel.getById(userInfoDTO.getSupplierId());
        TaxiDetail td = taxiDetailService.findDriverId(userInfoDTO.getDriverId());
        String supplierId = td.getSupplierId();

        if(supplierId != null && supplierId != null ){
            //driverBillingService.createWithID(Converter.dtoToEntityInfo(userInfoDTO,null));
            //return UserStatus.CREATED;
            return true;
        }
        else if (supplierId == null || supplierId  == null ){
            User user = userModel.getById(userInfoDTO.getDriverId());
            //UserDTO user = userModel.getDTO(userInfoDTO.getDriverId());
            if(user != null) {
                return true;
            }
            return false;
        }
        return false;
    }

    private String checkSupplier(DriverBillingDTO userInfoDTO) throws DatabaseException{

        //Supplier supplier = supplierModel.getById(userInfoDTO.getSupplierId());
        //Long supplierId = userModel.findSupplierId(userInfoDTO.getDriverId());

        TaxiDetail td = taxiDetailService.findDriverId(userInfoDTO.getDriverId());
        String supplierId = td.getSupplierId();

        if(supplierId != null  ){
            //driverBillingService.createWithID(Converter.dtoToEntityInfo(userInfoDTO,null));
            //return UserStatus.CREATED;
            return supplierId;
        }
        return null;
    }
    public String getSupplierId(String driverId){
        TaxiDetail taxiDetail =   taxiDetailService.findDriverId(driverId);
        if(taxiDetail != null && taxiDetail.getSupplierId() != null)
            return (taxiDetail.getSupplierId());
        return null;
    }
    public UserStatus create(DriverBillingDTO userInfoDTO) throws DatabaseException {




        String supplierID = checkSupplier(userInfoDTO);
        if(supplierID != null){
            //userInfoDTO.setSupplierId(supplierID);
            Optional<DriverBill> OptionaluserInfo = driverBillRepository.findById(userInfoDTO.getDriverId());

            DriverBill bill = null;
            if(OptionaluserInfo.isPresent())
                bill = OptionaluserInfo.get();

            if(bill == null) {
                DriverBill driverBill = Converter.dtoToEntityInfo(userInfoDTO, null,getSupplierId(userInfoDTO.getDriverId()));
                driverBill.setComments("GUI");

                if(driverBill.getSupplierId() == null) {
                    driverBill.setSupplierId(getSupplierId(driverBill.getDriverId()));
                }

            driverBillRepository.save(driverBill);
                return UserStatus.CREATED;
            }
            else {
                UserStatus status = update(userInfoDTO);
                if(status.name().equalsIgnoreCase(UserStatus.UPDATED.name()))
                    return UserStatus.CREATED;
                else
                    return UserStatus.EXCEPTION;
            }
        }
        else
            return UserStatus.EXCEPTION;
    }

    public UserStatus createInsdieTaxiDetails(DriverBillingDTO userInfoDTO,String SupperId) throws DatabaseException {


            //userInfoDTO.setSupplierId(supplierID);
            Optional<DriverBill> OptionaluserInfo = driverBillRepository.findById(userInfoDTO.getDriverId());
            DriverBill bill = null;
            if(OptionaluserInfo.isPresent()) {
                bill = OptionaluserInfo.get();
            }
            if(bill == null) {
                DriverBill driverBill = Converter.dtoToEntityInfo(userInfoDTO, null,SupperId);
                driverBill.setComments("GUI");

                if(SupperId != null) {
                    driverBill.setSupplierId(SupperId);
                }

                driverBillRepository.save(driverBill);
                return UserStatus.CREATED;
            }
            else {
                UserStatus status = update(userInfoDTO);
                if(status.name().equalsIgnoreCase(UserStatus.UPDATED.name()))
                    return UserStatus.CREATED;
                else
                    return UserStatus.EXCEPTION;
            }

    }

    public UserStatus createDummy(DriverBillingDTO userInfoDTO) throws DatabaseException {
        String supplierID = checkSupplier(userInfoDTO);
        if(supplierID  != null){
            //userInfoDTO.setSupplierId(supplierID);
            //DriverBill userInfo = driverBillRepository.findByDriverId(userInfoDTO.getDriverId());
            //List<DriverBill> userInfo
            Optional<DriverBill> userInfo = driverBillRepository.findByDriverId(userInfoDTO.getDriverId());

            if(!userInfo.isPresent()) {
                //five Rs is reduced it
                userInfoDTO.setBalance(95);
                DriverBill driverBill = Converter.dtoToEntityInfo(userInfoDTO, null,getSupplierId(userInfoDTO.getDriverId()));
                driverBill.setComments("Automatic");
                driverBillRepository.save(driverBill);
                return UserStatus.CREATED;
            }
            else {
                UserStatus status = update(userInfoDTO);
                if(status.name().equalsIgnoreCase(UserStatus.UPDATED.name()))
                    return UserStatus.CREATED;
                else
                    return UserStatus.EXCEPTION;
            }
        }
        else
            return UserStatus.EXCEPTION;
    }

    public UserStatus update(DriverBillingDTO userInfoDTO) throws DatabaseException{

        if(checkAccess(userInfoDTO)){
            Optional<DriverBill> userInfos = driverBillRepository.findByDriverId(userInfoDTO.getDriverId());

            if(userInfos.isPresent()) {
                DriverBill userInfo = userInfos.get();
                userInfo.setCredit(userInfo.getBalance() + userInfoDTO.getCredit());
                if(userInfo.getSupplierId() == null){
                    userInfo.setSupplierId(getSupplierId(userInfo.getDriverId()));
                }
                driverBillRepository.save(userInfo);
            }

            return UserStatus.UPDATED;
        }
        else
            return UserStatus.EXCEPTION;
    }

    public UserStatus updateAmount(DriverBillingDTO userInfoDTO) throws DatabaseException{

        if(userInfoDTO.getId() != null || userInfoDTO.getDriverId() != null){
            Optional<DriverBill> opt = null;

            if(userInfoDTO.getId() != null && !userInfoDTO.getId().isEmpty()|| !"".equalsIgnoreCase(userInfoDTO.getId()) && !userInfoDTO.getId().trim().equalsIgnoreCase("")  )
                 opt = driverBillRepository.findById(userInfoDTO.getId());
            else
                 opt = driverBillRepository.findByDriverId(userInfoDTO.getDriverId());

            if(opt.isPresent()) {
                DriverBill userInfo =  opt.get();
                //Total -
                userInfo.setCredit(userInfoDTO.getCredit());
                userInfo.setBalance(userInfo.getBalance() + userInfoDTO.getCredit());

                if(userInfo.getSupplierId() == null){
                    userInfo.setSupplierId(getSupplierId(userInfo.getDriverId()));
                }

                //took credit already
                // store old balance amount
                userInfoDTO.setBalance(userInfo.getBalance());
                //store old Debit
                userInfoDTO.setDriverId(userInfo.getDriverId());
                userInfoDTO.setDebit(userInfo.getDebit());



                driverBillRepository.save(userInfo);


                AuditBill audit = Converter.dtoToEntityInfoClone(userInfoDTO);

                auditBillRepository.save(audit);

            }
            return UserStatus.UPDATED;
        }
        else
            return UserStatus.EXCEPTION;
    }

    public UserStatus updateDebit(String driverId,float debit) throws DatabaseException{

        if(driverId != null){
            Optional<DriverBill> opt = driverBillRepository.findByDriverId(driverId);
            if(opt.isPresent()) {
                DriverBill userInfo =  opt.get();
                userInfo.setDebit(debit);
                userInfo.setCurrentDate(new Date());
                userInfo.setBalance(userInfo.getBalance() - debit);

                log.info(" updateInfo : debitAmount -->" + debit);
                driverBillRepository.save(userInfo);
                return UserStatus.UPDATED;
            }
            return UserStatus.UPDATED;
        }
        else
            return UserStatus.EXCEPTION;
    }

    public DriverBillingDTO get(String driverId) {

        Optional<DriverBill> userInfo = driverBillRepository.findByDriverId(driverId);
        if(userInfo != null ) {
            DriverBill bill  = userInfo.get();
            if(bill != null){
                DriverBillingDTO dto = Converter.entityToDto(bill);
                dto.setDriverName("DUMMY");
                return dto;
            }

                return new DriverBillingDTO();
        }
        else
            return new DriverBillingDTO();
    }
    public List<DriverBillingDTO> getAllDrivers(String supplierId) {
        List<DriverBill> userInfo = driverBillRepository.findBySupplierId(supplierId);

        List<DriverBillingDTO> list = new ArrayList<>();
        if(userInfo != null && userInfo.size() > 0) {
            for (DriverBill bill : userInfo) {
                list.add(Converter.entityToDto(bill));
            }
            return list;
        }
        else
            return  list;
    }
    public DriverBill getDriver(String driverId) {
        Optional<DriverBill> userInfo = driverBillRepository.findByDriverId(driverId);
        if(userInfo.isPresent())
           return  userInfo.get();
        return new DriverBill();
    }

    public List<DriverBillingDTO> getDriverDTO( List<DriverBill> driverBill) {
        List<DriverBillingDTO> list = new ArrayList<>();

        for(DriverBill bill:driverBill){
            DriverBillingDTO dto = Converter.entityToDto(bill);
            User user = userModel.getById(bill.getDriverId());
            if(user != null){
                if(user.getFirstName() != null && !"".equalsIgnoreCase(user.getFirstName()))
                    dto.setDriverName(user.getFirstName());
                else
                    dto.setDriverName(user.getLastName());
            }
            list.add(dto);
        }
        return list;
    }

    public List<DriverBillingDetailsDTO> getDriverDetailDTO(List<DriverBill> driverBill) {
        List<DriverBillingDetailsDTO> list = new ArrayList<>();

        for(DriverBill bill:driverBill){
            DriverBillingDetailsDTO dto =  Converter.entityBillToDto(bill);
            User user = userModel.getById(bill.getDriverId());
            if(user != null){
                if(user.getFirstName() != null && !"".equalsIgnoreCase(user.getFirstName()))
                    dto.setDriverName(user.getFirstName());
                else
                    dto.setDriverName(user.getLastName());
                dto.setPhoneNumber(user.getPhoneNumber());
            }
            list.add(dto);
        }
        return list;
    }

    public DriverBillingDTO getDriver(String supplierId,String driverId) throws DatabaseException {
        DriverBillingDTO dto = new DriverBillingDTO();
        //dto.setSupplierId(supplierId);
        //dto.setSupplierId(supplierId);
        dto.setDriverId(driverId);
        if(checkAccess(dto)) {
            DriverBillingDTO userInfo = get(driverId);
            return userInfo;
        }
        return null;
    }

    /*public UserStatus updateFee(String userId,float fee) throws DatabaseException {
        Optional<DriverBill> userInfo = driverBillRepository.findByDriverId(userId);
        if(userInfo.isPresent()) {
            DriverBill driverBill = userInfo.get();
            driverBill.setDebit(fee);
            driverBillRepository.save(driverBill);
        }

        //driverBillingService.update(DriverBill.class,userInfo.getId(),userInfo);

        DriverBillingDTO userInfoDTO = new DriverBillingDTO();
        //driverBillingService.createWithID(Converter.dtoToEntityInfo(userInfoDTO,null));
        driverBillRepository.save(Converter.dtoToEntityInfo(userInfoDTO,null,getSupplierId(userInfoDTO.getDriverId())));
        return UserStatus.CREATED;
    }*/
    public UserStatus totalFee(String userId,float fee) throws DatabaseException {
        Optional<DriverBill> userInfo = driverBillRepository.findByDriverId(userId);

        if(userInfo.isPresent()) {
            DriverBill driverBill = userInfo.get();
            driverBill.setDebit(fee);
            driverBill.setBalance(driverBill.getBalance() - fee);
            //driverBillingService.update(DriverBill.class,userInfo.getId(),userInfo);
            driverBillRepository.save(driverBill);
            DriverBillingDTO userInfoDTO = new DriverBillingDTO();

        }

        DriverBillingDTO userInfoDTO = new DriverBillingDTO();


        //driverBillingService.createWithID(Converter.dtoToEntityInfo(userInfoDTO,null));
        driverBillRepository.save(Converter.dtoToEntityInfo(userInfoDTO,null,getSupplierId(userInfoDTO.getDriverId())));
        return UserStatus.CREATED;
    }


    //givecoupenoffer
    //firstgiveoofer touserandcredittouser
    public float calculatePercentage(float percentage,String driverId)  {
        //float totalPrice = ride.getTotalPrice();
        //float percentage =  (totalPrices/5);
        //ride.setDebit(percentage);


        if(percentage >= 1) {

            DriverBill driverBill = getDriver(driverId);
            if(driverBill != null && driverBill.getId() != null) {

                driverBill.setDebit(percentage);
                driverBill.setBalance(driverBill.getBalance() - percentage);

                driverBill.setSupplierId(getSupplierId(driverId));
                driverBillRepository.save(driverBill);

                auditBillRepository.save(Converter.dtoToEntityInfoClone(driverBill));

                log.info("--- percentage --->" + percentage  + driverBill.getDriverId());
                return percentage;
            }
            else if(driverBill != null && driverBill.getId() == null) {


                DriverBillingDTO dto = new DriverBillingDTO();


                dto.setDebit(percentage);
                dto.setBalance(Path.DOMAIN.amount - percentage);
                dto.setDriverId(driverId);
                //dto.setSupplierId(supplierId);

                driverBillRepository.save(Converter.dtoToEntityInfo(dto,null,getSupplierId(driverId)));
                log.info("Dummy account is created ..>" + dto.getDebit() );
            }
            else
                log.info("No driverId ..>" + driverId );
        }
        return percentage;
    }

   /* public String getSupplierId(String driverId){

        String supplierId = null;
        TaxiDetail td  = taxiDetailService.finBySupplierId(driverId);
        if(td != null && td.getSupplierId() != null) {
            supplierId = td.getSupplierId();
            log.info(" no supplier id" + supplierId );
        }
        return supplierId;
    }*/


}
