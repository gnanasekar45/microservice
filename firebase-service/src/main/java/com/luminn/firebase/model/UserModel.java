package com.luminn.firebase.model;

import com.authy.AuthyApiClient;
import com.google.firebase.database.DatabaseException;
import com.luminn.firebase.dto.*;

import com.luminn.firebase.entity.*;
import com.luminn.firebase.helper.Converter;
import com.luminn.firebase.impl.UserFilterImpRepository;
import com.luminn.firebase.repository.SupplierRepository;
import com.luminn.firebase.repository.TaxiRepository;
import com.luminn.firebase.repository.UserRepository;
import com.luminn.firebase.request.LongDTO;
import com.luminn.firebase.request.PhonesDTO;
import com.luminn.firebase.response.ResponsePage;
import com.luminn.firebase.security.HashCodeUtils;
import com.luminn.firebase.service.*;
import com.luminn.firebase.util.LoginError;
import com.luminn.firebase.util.Path;
import com.luminn.firebase.util.Role;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Component
public class UserModel {

    private static final Logger log = LoggerFactory.getLogger(UserModel.class);

    @Autowired
    UserMongoService userService;

    @Autowired
    TaxiDetailService taxiDetailService;

    @Autowired
    TaxiService taxiService;

    @Autowired
    private HashCodeUtils hashCodeUtils;

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaxiRepository taxiRepository;

    @Autowired
    SupplierService supplierService;

    @Autowired
    TaxiModel taxiModel;

    @Autowired
    UserFilterImpRepository userFilterImpRepository;

    @Autowired
    DriverPhotoService photoService;


    private AuthyApiClient authyApiClient;

    public LoginDTO loginUserType(String emailId, String phoneNumber, String password, String oneSignal, String type, String domain) {
        log.info("User info -->" +  emailId + phoneNumber  + oneSignal + domain);
        return validation(emailId, phoneNumber, password, oneSignal, type, domain);
    }


    private UserStatus checkUserExisting(PhonesDTO requestBody){

        UserStatus userStatus = getByPhoneNumberandRole(requestBody.getPhoneNumber(),requestBody.getRole(),"");
        return userStatus;
    }

    public UserStatus getByPhoneNumberandRole(String phoneNumber,String role,String domain) {
        User user = checkGetByPhoneNumber(phoneNumber,role,domain);

        if (user != null && user.getId() != null && !"".equalsIgnoreCase(role) && user.getRole().equalsIgnoreCase(role))
            return UserStatus.EXISTS;
        else
            return UserStatus.CREATED;
    }

    public User updatePhoneVerification(String phoneNumber) {

        User user = userService.findByPhoneNumber(phoneNumber);

        try {
            if (user != null) {
                user.setPhoneVerified(Path.MESSAGE.VERIFIED);
                userRepository.save(user);
                return user;
            }
        } catch (DatabaseException e) {

        }

        if (user != null && user.getId() != null)
            return user;
        else
            return null;
    }
    public String getDomain(String emailorPhoneNumber,String phoneNumber){
        User user = null;
        if(emailorPhoneNumber != null && !"".equalsIgnoreCase(emailorPhoneNumber) )
            user = userRepository.findByEmail(emailorPhoneNumber);
        if(user != null && user.getDomain() != null)
            return user.getDomain();

        else {
            if(phoneNumber != null && !"".equalsIgnoreCase(phoneNumber) )
                user = userRepository.findByPhoneNumber(phoneNumber);
             if(user != null && user.getDomain() != null)
                return user.getDomain();
             else
                return Path.DOMAIN.MYDOMAIN;
        }

    }

    public LoginDTO validation(String emailId, String phoneNumber, String password, String oneSignal, String type, String domain) {

        LoginDTO loginDTO = null;
        UserDTO user = null;
        User userType = null;
        //User userEntity;

        domain = domain.trim();
        log.info(" domain --> " + domain);
        if (domain != null && !"".equalsIgnoreCase(domain) && !domain.equalsIgnoreCase("string")) {
            log.info(" if domain --> " + domain);
        }
        else {
            //check for domain
            log.info(" else domain --> " + domain);
            domain = getDomain(emailId,phoneNumber);
        }

        if (!isEmail(emailId)) {
            //log.info(emailId + ".. phone type .." + type + "--" + phoneNumber );
            userType = checkGetByEmail(emailId, type, getDomainName(domain));
            if (userType == null ) {
                return wrongLogin(-3L, 7l);
            }
            else if (userType != null && userType.getRole() == null || userType.getRole().equalsIgnoreCase("")) {
                return wrongLogin(-3L, 0L);
            }
            else
                return getLoginValue(userType, password, oneSignal, loginDTO, Path.MESSAGE.PHONENUMBER,domain);
        }
        //else if (isEmail(emailId) && !"".equals(phoneNumber) && phoneNumber.length() >= 8) {
        else if (!"".equals(phoneNumber) && phoneNumber.length() >= 8) {
            //log.info(emailId + ".. phone type .." + type + "--" + phoneNumber );

            userType = userService.checkGetByPhoneNumber(phoneNumber, type, getDomainName(domain));


            if (userType == null || userType.getRole() == null || userType.getRole().equalsIgnoreCase("")) {
                return wrongLogin(-3L, 10l);
            } else
                return getLoginValue(userType, password, oneSignal, loginDTO, Path.MESSAGE.PHONENUMBER,domain);

           /* if (userType == null ) {
                return wrongLogin(-3L, 7l);
            }
            else if (userType != null || userType.getRole() == null || userType.getRole().equalsIgnoreCase("")) {
                return wrongLogin(-3L, 0l);
            }
            else
                return getLoginValue(userType, password, oneSignal, loginDTO, Path.MESSAGE.PHONENUMBER,domain);*/

        } else if (!"".equals(emailId) && !emailId.equalsIgnoreCase("string")) {

            userType = checkGetByEmail(emailId, type, getDomainName(domain));
            if (userType.getRole() == null || userType.getRole().equalsIgnoreCase("")) {
                return wrongLogin(-3L, 0l);
            } else
                return getLoginValue(userType, password, oneSignal, loginDTO, Path.MESSAGE.EMAIL,domain);
        }
        return wrongLogin(-3L, 0l);
    }

    public User checkGetByEmail(String email, String role, String domain) {

        List<User> users = userService.getEmailDuplicate(email);

        if (users != null && users.size() > 0) {
            for (User user : users) {
                if (user != null && user.getRole() != null && user.getRole().equalsIgnoreCase(role)) {
                    return user;
                } else if (user != null && (user.getWebsite() == null || "".equalsIgnoreCase(user.getWebsite())) && user.getRole().equalsIgnoreCase(role)) {
                    return user;
                } else
                    return users.get(0);
            }
        } else if (users == null)
            return new User();
        return new User();
    }

    public User getByPhoneNumber(String phoneNumber) {
        User user = userService.findByPhoneNumber(phoneNumber);
        return user;
    }

    public User getBpthPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        return user;
    }

    public UserStatus getByEmail(String email, String role, String domain) {

        List<User> users = userService.getEmailDuplicate(email);

        if (users != null && users.size() > 0) {
            for (User user : users) {
                if (user != null && user.getRole() != null && user.getRole().equalsIgnoreCase(role) && user.getDomain() != null && user.getDomain().equalsIgnoreCase(domain)) {
                    return UserStatus.EXISTS;
                }

            }
            return UserStatus.EXISTS;
        }
        return UserStatus.CREATED;
    }

    public User checkGetByPhoneNumber(String phoneNumber, String role, String domain) {
        List<User> users = userService.getPhoneNumber(phoneNumber);


        if (users != null && users.size() > 0) {
            for (User user : users) {
                if (user != null && user.getRole().equalsIgnoreCase(role)) {
                    return user;
                }
            }
        } else if (users == null) {
            User emtpyuser = new User();
            return emtpyuser;
        }
        User emtpyuser = new User();
        return emtpyuser;

    }

    private boolean isEmail(String emailId) {
        int intIndex = 0;

        if (emailId != null && emailId.isEmpty()) {
            return true;
        }

        if (emailId != null && !emailId.isEmpty()) {
            intIndex = emailId.indexOf("@");
        }
        if (intIndex == -1) {
            return true;
        }
        return false;
    }

    public String getDomainName(String name) {
        //log.info("name --" + name);
        if (name == null || !"".equals(name) && (name.equalsIgnoreCase("string") || name.toUpperCase().equalsIgnoreCase(Path.DOMAIN.MYDOMAIN)) || "".equals(name)) {
            return Path.DOMAIN.MYDOMAIN;
        } else
            return name;
    }

    public LoginDTO wrongLogin(Long id, Long errorCode) {
        LoginDTO login = new LoginDTO();
        login.setId(null);

        if (errorCode == 7L) {
            login.setId("7");
            login.setTaxiId(String.valueOf(LoginError.NO_PHONENUMBER));
            return login;
        }
        if (errorCode >= 4L) {
            login.setId("5");
            login.setTaxiId(String.valueOf(LoginError.PASSWORD_WRONG));
            return login;
        }
        if (errorCode == 10l) {
            login.setId("10");
            login.setTaxiId(String.valueOf(LoginError.PASSWORD_WRONG));
            return login;
        }
        if (errorCode == 0L) {
            login.setId("8");
            login.setTaxiId(String.valueOf(LoginError.NO_USERID));
            return login;
        }
        return login;
    }

    private LoginDTO getLoginValue(User user, String password, String oneSignal, LoginDTO loginDTO, String phoneNumberOremail,String domain) {


        //List<TaxiDetail> ltaxiDetail = null;
        //chandra - the quick fix i did so that i could load spring boot 2.0.2.RELEASE
        if (checkPassword(user, password, phoneNumberOremail)) {

            //log.info("checkPassword -- SUCCESS");
            loginDTO = new LoginDTO();

            if (user.getLang() != null)
                loginDTO.setLan(user.getLang());
            if (user.getFirstName() != null && !user.getFirstName().equalsIgnoreCase("")
                    || user.getLastName() != null && !user.getLastName().equalsIgnoreCase(""))
                loginDTO.setName(user.getName());
            else if(user.getName() != null)
                loginDTO.setName(user.getName());

            if (!"".equalsIgnoreCase(user.getPhoneNumber()) && user.getPhoneNumber() != null)
                loginDTO.setPhoneNumber(loginDTO.getPhoneNumber());



            //HERE SUCCUESS SO SET
            loginDTO.setId("1000l");
            String role = user.getRole();

            loginDTO.setEmail(user.getEmail());
            loginDTO.setPhoneNumber(user.getPhoneNumber());
            loginDTO.setRole(role);
            //loginDTO.setUserId(user.getId());
            loginDTO.setId(user.getId());

            if (user.getToken() != null) {
                loginDTO.setToken(user.getToken());
            }
            if (user.getToken() != null && !"".equals(user.getToken())) {
                checkingDuplcateOneSignal(user.getToken(), user.getId());
            }

            if (user.getRole() != null && (role.equalsIgnoreCase(Role.ROLE_DRIVER)
                    || role.equalsIgnoreCase(Role.ROLE_SUPPLIER) || role.equalsIgnoreCase(Role.ROLE_DELIVERY))) {
                if (oneSignal != null && !"".equals(oneSignal)) {
                    //CK
                }
                if (role.equals(Role.ROLE_DRIVER) || role.equals(Role.ROLE_DELIVERY))
                    loginDTO =  checkDriverlogin(loginDTO,user);
                 else if (role.equals(Role.ROLE_SUPPLIER))
                    loginDTO = getSupplierId(loginDTO, user.getId(),domain);
            }
            return loginDTO;
        }

        else if(user != null && !hashCodeUtils.validPasswordCheck(user.getPassword(), password) ) { //user != null  && !passwordEncoder.encodePassword(password, null).equals(user.getPassword())) {
            return wrongLogin(22L, 22L);
        } else if (user == null) {
            return wrongLogin(0L, 0L);
        }
        return new LoginDTO();
    }

    public LoginDTO checkDriverlogin(LoginDTO loginDTO,User user) {
        log.info("inside DRIVER profile" + user.getId());

            //loginDTO.setHourly(user.getHourly());
            TaxiDetail taxiDetail = taxiDetailService.findDriverId(user.getId());

            if (taxiDetail != null) {
                String supplierId = taxiDetail.getSupplierId();
                loginDTO.setSupplierId(supplierId);
            }
            else
                log.info("NULL taxiDetail" + taxiDetail);

            if (taxiDetail != null && loginDTO.getSupplierId() != null) {

                //ltaxiDetail = taxidetailService.listBySupplier(loginDTO.getSupplierId());
                if (taxiDetail != null) {

                    // for (TaxiDetail taxiDetail : ltaxiDetail) {
                    if (taxiDetail != null) {

                        Taxi taxi = null;
                        //Price price = null;

                        if (taxiDetail.getDriverId() != null) {


                            if (taxiDetail.getDriverId().equalsIgnoreCase(user.getId())) {

                                loginDTO.setTaxiId(taxiDetail.getTaxiId());
                                String taxiId = taxiDetail.getTaxiId();

                                try {
                                     taxi = taxiService.findId(taxiId,null);



                                    if (taxi != null) {

                                        //price = taxiModel.getBestPricesFull(taxi.getCarType(), 0);

                                        loginDTO.setType(taxi.getCarType());
                                        if (taxi.isActive()) {
                                            loginDTO.setIsApproved("1");
                                        } else if (!taxi.isActive()) {
                                            loginDTO.setIsApproved("0");
                                        }
                                        log.info(" taxiId  " + taxi.getId() + " Approved or not ?  " + taxi.isActive());

                                        loginDTO.setPrice(taxiDetail.getPrice());
                                        loginDTO.setPeakPrice(taxiDetail.getPeakPrice());
                                        loginDTO.setBasePrice(taxiDetail.getBasePrice());
                                    }

                                } catch (DatabaseException e) {
                                    //worst case //Default price
                                    loginDTO.setPeakPrice(5.0);
                                    loginDTO.setPrice(2.99);
                                    loginDTO.setBasePrice(3.0);
                                }
                            }
                            log.info(" taxiDetail ---> 2" + taxiDetail);
                        }
                    }
                    return loginDTO;
                }
            }
            else {
                taxiDetail = taxiDetailService.findDriverId(user.getId());
                if (taxiDetail != null && taxiDetail.getTaxiId() != null)
                    loginDTO.setTaxiId(taxiDetail.getTaxiId());
                else
                    loginDTO.setTaxiId("-1l");
            }

            loginDTO.setPeakPrice(5.0);
            loginDTO.setPrice(3.0);
            loginDTO.setBasePrice(3.0);
        return loginDTO;
      }



    public LoginDTO getResponse(User user) {

        if (user.getRole() != null && (user.getRole().equalsIgnoreCase(Role.ROLE_DRIVER) || user.getRole().equalsIgnoreCase(Role.ROLE_SUPPLIER)
                || user.getRole().equalsIgnoreCase(Role.ROLE_DELIVERY))) {

            if (user.getRole().equals(Role.ROLE_DRIVER) || user.getRole().equals(Role.ROLE_DELIVERY)) {
            }
        }
        /*else if(user != null && !hashCodeUtils.validPasswordCheck(user.getPassword(), user.getPassword()) ) { //user != null  && !passwordEncoder.encodePassword(password, null).equals(user.getPassword())) {
            return wrongLogin(0L, 4L);
        } else if (user == null) {
            return wrongLogin(0L, 0L);
        }*/
        return new LoginDTO();
    }

    public User getById(String id){
        return userService.findId(id);
    }


    private void checkingDuplcateOneSignal(String token, String key) {


        List<User> duplicateChecking = userService.getDuplicateToken(token);

        if (duplicateChecking != null && duplicateChecking.size() >= 2) {

            for (User duplciateUser : duplicateChecking) {
                if (!duplciateUser.getId().equals(key)) {
                    duplciateUser.setToken(null);
                    try {
                        //Later to fix it
                        userRepository.save(duplciateUser);
                    } catch (DatabaseException e) {
                        log.info("--- error   ----");
                    }
                }
            }
        }

    }

    public void loginStatus(String status, User user) {
        user.setStatus(status);
        try {
            userRepository.save(user);
        } catch (DatabaseException e) {

        }
    }

    public ModelStatus changePassWordMobile(UserDTO userDTO, boolean onlypasswordChange,User user) {

        Date now = new Date();
        //User user = null;

        if("".equalsIgnoreCase(userDTO.getEmail())|| userDTO.getEmail() != null)
            user = userService.findByEmail(userDTO.getEmail());
        else if (user == null)
           user = checkGetByPhoneNumber(userDTO.getPhoneNumber(),userDTO.getRole(),"");

        if ((onlypasswordChange)) {


            Date previousDate = user.getUpdatedOn();
            // if(DateUtil.compare30mins(previousDate,now) || onlypasswordChange) {
            try {
                // user.setPassword(passwordEncoder.encodePassword(userDTO.getPassword(), null));
                String codeCheck = hashCodeUtils.generatePasswordFirstTime(userDTO.getPassword());
                user.setPassword(codeCheck);
                //user.setPassword(userDTO.getPassword());
                //userRepository.save(user);
                userService.updatePassword(user.getId(),codeCheck);
                return ModelStatus.UPDATED;
            } catch (DatabaseException b) {
                System.out.println("DataBaseException" + b.getStackTrace());
                return ModelStatus.NOTEXISTS;
            }

        }

        return ModelStatus.NOTEXISTS;
    }

    private boolean checkPassword(User user,String password, String phoneNumberOremail){

        if(phoneNumberOremail.equalsIgnoreCase(Path.MESSAGE.EMAIL)){
            if (user != null && hashCodeUtils.validPasswordCheck(user.getPassword(), password)){
                return true;
            }
        }
        //if((phoneNumberOremail.equalsIgnoreCase(Path.MESSAGE.PHONENUMBER) && user.getPhoneVerified().equalsIgnoreCase(Path.MESSAGE.VERIFIED) &&  hashCodeUtils       .validPasswordCheck(user.getPassword(), password) )
        if((phoneNumberOremail.equalsIgnoreCase(Path.MESSAGE.PHONENUMBER) &&  hashCodeUtils.validPasswordCheck(user.getPassword(), password) )
                || (phoneNumberOremail.equalsIgnoreCase(Path.MESSAGE.PHONENUMBER) && hashCodeUtils.validPasswordCheck(user.getPassword(), password) )){
            return true;
        }
        return false;
    }


    public User getToken(String token){

       Document document =  userService.findToken(token);
       User user = Converter.documentToUser(document);
       return user;
    }

    private LoginDTO getSupplierId(LoginDTO loginDTO,String userId,String domain){

        Supplier supplier = supplierRepository.findByUserId(userId);

        if(supplier == null) {

            List<Supplier> domains = supplierRepository.findByName(domain);
            if(domains != null && domains.size() >0) {
                supplier = domains.get(0);
                loginDTO.setSupplierId(supplier.getId());
            }

        }

        if (supplier != null && supplier.getId() != null) {
            loginDTO.setSupplierId(supplier.getId());
            //loginDTO.setSupplierName(supplier.getName());
            System.out.println("supplier Name 1 -->"+ supplier.getName() + " supplier" + supplier.getId());
            return loginDTO;
        }
        //This is case when supplier register the taxi Driver
        else if (supplier == null) {
            TaxiDetail taxiDetail = taxiDetailService.finByDriverId(userId);
            if (taxiDetail != null) {
                String tsupplier = taxiDetail.getSupplierId();
                if (tsupplier != null) {
                    System.out.println("supplier Name 2 -->"+ tsupplier);
                    loginDTO.setSupplierId(tsupplier);
                    // loginDTO.setSupplierName(tsupplier.getName());
                    return loginDTO;
                }
            }
            else {
                List<Supplier> domains = supplierRepository.findByName(domain);
                if(domains != null && domains.size() >0)
                    supplier = domains.get(0);
                 loginDTO.setSupplierId(supplier.getId());
                // loginDTO.setSupplierName(tsupplier.getName());
                return loginDTO;
            }
        }
        return loginDTO;

    }

    public User validate(String id) throws DatabaseException {
        User user = null;

        if (id != null) {
            user = userService.getById(id);
            if (user != null) {
                return user;
            } else
                return null;
        }
        return null;
    }

    public boolean validate(String id, String role) throws DatabaseException {
        User user = null;

        if (id != null) {
            user = userService.getById(id);
            if (user != null && user.getRole().equals(role))
                return true;
            else
                return false;
        }

        return false;
    }


    public String createUser(UserOffline userOffline) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(userOffline.getEmailId());
        userDTO.setFirstName(userOffline.getName());
        userDTO.setRole("ROLE_USER");
        userDTO.setPhoneNumber(userOffline.getPhoneNumber());
        String userId = createOrGet(userDTO);
        return userId;
    }

    public String createUser(PhoneBookingSMS userOffline) {
        UserDTO userDTO = new UserDTO();
        //userDTO.setEmail(userOffline.getEmailId());
        userDTO.setFirstName(userOffline.getDriverName());
        userDTO.setRole("ROLE_USER");
        userDTO.setPhoneNumber(userOffline.getUserPhoneNumber());
        String userId = createOrGet(userDTO);
        return userId;
    }

    public String createOrGet(UserDTO userDTO) {
        Long userId= null;
        userDTO.setRole("ROLE_USER");
        User user = getByPhoneNumber(userDTO.getPhoneNumber());
        if(user != null && user.getId() != null){
            return user.getId();
        }
        else {
            User user2 = Converter.dtoToEntity(userDTO,null);
            return userRepository.save(user2).getId();
        }
    }
    public ModelStatus deleteTaxi(String taxiId){
        return null;
    }
    public ModelStatus deleteTaxiDetail(String taxiDetailId){
        taxiModel.deleteTaxiDetails(taxiDetailId);
        return  ModelStatus.DELETED;
    }

    public ModelStatus deleteUser(String taxiDetailId){
        return null;
    }

    public ModelStatus deleteUser(String  userId, String role,boolean nosupplier) throws DatabaseException {
        User user = null;

        if(userId != null) {
            user = userService.findId(userId);

            if (user != null) {
                //user.setDomain(supplierService.findDomainName(id));
                userRepository.delete(user);
                return ModelStatus.DELETED;
            }
        }
        return ModelStatus.EXCEPTION;
    }

    public ModelStatus deleteUser(User user) throws DatabaseException {


        if(user != null) {


            if (user != null) {
                //user.setDomain(supplierService.findDomainName(id));
                userRepository.delete(user);
                return ModelStatus.DELETED;
            }
        }
        return ModelStatus.EXCEPTION;
    }

    public UserDTO getDTO(String id) throws DatabaseException {

        User user = null;
        UserDTO userDTO = null;

        if(id != null){
            //user = userService.findId(id);
            //Optional<User> ouser= userRepository.findById(id);
            user = userService.findId(id);


            if(user != null && user.getDomain() == null){
                user.setDomain(supplierService.findDomainName(id));
            }
            else if(user != null && user.getRole().equalsIgnoreCase("ROLE_DRIVER") ||user.getRole().equalsIgnoreCase("ROLE_SUPPLIER") ){
                //supplierService.findDomainName(id);
                user.setDomain(user.getDomain());
            }
        }
        if (user != null && user.getId() != null) {
            return Converter.entityToDto(user);
        }
        else
            return new UserDTO();
    }

    public UserInfoDTO getImageDTO(String id) throws DatabaseException {

        User user = null;
        UserInfoDTO userDTO = null;

        if(id != null){
            //user = userService.findId(id);
            //Optional<User> ouser= userRepository.findById(id);
            user = userService.findId(id);


            if(user != null && user.getDomain() == null){
                user.setDomain(supplierService.findDomainName(id));
            }
            else if(user != null && user.getRole().equalsIgnoreCase("ROLE_DRIVER") ||user.getRole().equalsIgnoreCase("ROLE_SUPPLIER") ){
                //supplierService.findDomainName(id);
                user.setDomain(user.getDomain());
            }
        }
        if (user != null && user.getId() != null) {
            UserInfoDTO userInfoDTO = Converter.entityToUserDto(user);

            if(user.getId() != null){
                if(photoService.getDriverUserandId(user.getId()) != null)
                    userInfoDTO.setIsImage("true");
            }
            else{
                userInfoDTO.setIsImage("false");
            }
            return userInfoDTO;
        }
        else
            return new UserInfoDTO();
    }

    public List<UserDTO> getDTOUser(List<User> user)  {

        List<UserDTO> userList = new ArrayList<>();
        for(User users : user) {
            if ( users != null) {
                UserDTO userDTO = Converter.entityToDto(users);
                userList.add(userDTO);
            }
        }
        return userList;
    }

   /* public ResponsePage getUserData(String role,
                            String firstName,
                            String id,int page,int size){

        Pageable paging = PageRequest.of(page, size);
        //PageRequest.of(page, 15)
        Page<User> pageTuts = null;
        List<User> users = new ArrayList<User>();
        String domains = null;

        List<User> user = null;
        List<UsersDTO> userDTO = null;
        ResponsePage response = new ResponsePage();
        Supplier supplier = null;
        //supplierId
        if(id != null)
             supplier = supplierService.findById(id);
        if(supplier != null && role == null && firstName == null) {
            domains = supplier.getName();

            pageTuts = userRepository.findByDomain(domains, paging);
            List<User>  userList =  userFilterImpRepository.findUserByProperties(domains,"ROLE_USER",null,paging);


            response.setPageTut(pageTuts);

            if (pageTuts != null) {
                userDTO = getDTOUsers(users);
                response.setUserDTO(userDTO);
            }
            return response;
        }

        //role
        else if(firstName != null && !"".equalsIgnoreCase(firstName)) {

            pageTuts = userRepository.findByroleContainingIgnoreCase(firstName, paging);
            response.setPageTut(pageTuts);

            if (pageTuts != null) {
                userDTO = getDTOUsers(users);
                response.setUserDTO(userDTO);
            }
            return response;
        }
        else if(role != null && "".equalsIgnoreCase(role)) {

            if(!role.equalsIgnoreCase("ALL")) {
                pageTuts = userRepository.findByRoleContainingIgnoreCase(role, paging);
                response.setPageTut(pageTuts);
            }
            else if (role.equalsIgnoreCase("ALL")){
                pageTuts = userRepository.findByRoleContainingIgnoreCase(role, paging);
                response.setPageTut(pageTuts);
            }

            if (pageTuts != null) {
                userDTO = getDTOUsers(users);
                response.setUserDTO(userDTO);
            }
            return response;
        }

        return response;
    } */

    public List<UsersDTO> getDTOUsers(List<User> user)  {

        List<UsersDTO> userList = new ArrayList<>();
        for(User users : user) {
            if ( users != null) {
                UsersDTO userDTO = Converter.entityToDtoUser(users);
                userList.add(userDTO);
            }
        }
        return userList;
    }
    public boolean checkDomain(User user,String domain){
        if(user.getDomain() != null){
            if(user.getDomain().equalsIgnoreCase(domain))
                return true;
            else
               return false;
        }
        return false;
    }


    public String getDTOUsersDomain(String supplierId)  {
        String domain = null;
        Optional<Supplier> supllie =  supplierRepository.findById(supplierId);
        if(supllie.isPresent()){
            Supplier suppllier =  supllie.get();
            domain = suppllier.getName();
            log.info( "Domain name " + domain);
        }
        return domain;
    }
    public List<UsersDTO> getDTOUsersDomain(List<User> user,String supplierId)  {
        String domain = null;
        List<UsersDTO> userList = new ArrayList<>();
       Optional<Supplier> supllie =  supplierRepository.findById(supplierId);

       if(supllie.isPresent()){
          Supplier suppllier =  supllie.get();
           domain = suppllier.getName();
           log.info( "Domain name " + domain);
       }

        for(User users : user) {
            if ( users != null && checkDomain(users,domain) || checkDomain(users,"taxideals") || checkDomain(users,"KERALACABS")) {
                UsersDTO userDTO = Converter.entityToDtoUser(users);
                userList.add(userDTO);
            }
        }
        return userList;
    }

    public String delete(String emailId){
        User user = userRepository.findByEmail(emailId);
        if(user != null)
            userRepository.delete(user);
        return ModelStatus.SUCCESS.name();
    }
    public ModelStatus changeName(String id, String firstName, String lastName) throws DatabaseException {

        User user = userService.getById(id);
        if (user != null) {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            try {
                userRepository.save(user);
            } catch (DatabaseException b) {

            }
            return ModelStatus.UPDATED;
        }
        return ModelStatus.EXCEPTION;
    }

    public ModelStatus changeNamePhoneNumber(String id, String firstName, String lastName,String phoneNumber) throws DatabaseException {
           userService.updateNameSetting(id,firstName,lastName,phoneNumber);
          return ModelStatus.UPDATED;
    }

    public ModelStatus updateLoginStatus(String id, String value) throws DatabaseException {

        User user = userService.getById(id);
        if (user != null) {
            user.setStatus(value);
            //userRepository.save(user);
            userService.updateValue(id,value);
            return ModelStatus.UPDATED;
        }
        return ModelStatus.EXCEPTION;
    }
    public ModelStatus updateLanguage(String id, String value) throws DatabaseException {

        User user = userService.getById(id);
        if (user != null) {
            if(checkLanguage(value)) {
                user.setLang(value);
                userRepository.save(user);
                return ModelStatus.UPDATED;
            }
            return ModelStatus.EXCEPTION;
        }
        return ModelStatus.EXCEPTION;
    }
    private boolean checkLanguage(String value){
        if(!"".equalsIgnoreCase(value) && value != null){
            if(value.equalsIgnoreCase("en"))
                return true;
            else if(value.equalsIgnoreCase("de"))
                return true;
            else if(value.equalsIgnoreCase("fr"))
                return true;
            else if(value.equalsIgnoreCase("it"))
                return true;
            else if(!"".equalsIgnoreCase(value)&&value.length() < 5)
                return true;
            else
                return false;
        }
        return false;
    }

    public ModelStatus updateStepStatus(String id, String value) throws DatabaseException {

        User user = userService.getById(id);
        if (user != null) {

            user.setStatus(value);
            userRepository.save(user);
            return ModelStatus.UPDATED;
        }
        return ModelStatus.EXCEPTION;
    }

    public LongDTO getUser(String userId) throws DatabaseException {
        User luser = userService.getById(userId);
        LongDTO userDTO = Converter.longToDto(luser);

        return userDTO;
    }

    public ModelStatus changePassWord(UserDTO userDTO, boolean onlypasswordChange) {

        Date now = new Date();
        User user = null;

        if("".equalsIgnoreCase(userDTO.getEmail())|| userDTO.getEmail() != null)
            user = userRepository.findByEmail(userDTO.getEmail());

        else if (user == null)
            user = userRepository.findByPhoneNumber(userDTO.getPhoneNumber());

        if ((userDTO.getRestKey() != null && userDTO.getRestKey().equals(user.getPasswordResetKey()) || onlypasswordChange)) {


            Date previousDate = user.getUpdatedOn();
            // if(DateUtil.compare30mins(previousDate,now) || onlypasswordChange) {
            try {
                // user.setPassword(passwordEncoder.encodePassword(userDTO.getPassword(), null));
                String codeCheck = hashCodeUtils.generatePasswordFirstTime(userDTO.getPassword());
                user.setPassword(codeCheck);
                log.info("codeCheck" + codeCheck);
                String resetKey = UUID.randomUUID().toString()+"-"+UUID.randomUUID().toString();
                user.setPasswordResetKey(resetKey);

                log.info("resetKey" + resetKey);
                userRepository.save(user);
                return ModelStatus.UPDATED;
            } catch (DatabaseException b) {
                System.out.println("DataBaseException" + b.getStackTrace());
                return ModelStatus.NOTEXISTS;
            }
            // }
            //return ModelStatus.EXPIRES;
        }
        return ModelStatus.NOTEXISTS;
    }

   public User getByEmailId(String email){
        return userService.findByEmail(email);
   }

    public User  getByPhoneNumberId(String phoneNumber){
        return userService.findByPhoneNumber(phoneNumber);
    }



}
