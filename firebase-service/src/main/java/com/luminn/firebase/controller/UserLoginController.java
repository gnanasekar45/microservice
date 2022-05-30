package com.luminn.firebase.controller;

import com.google.firebase.database.DatabaseException;
import com.luminn.firebase.dto.*;
import com.luminn.firebase.entity.Supplier;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.model.MobileModel;
import com.luminn.firebase.model.ModelStatus;
import com.luminn.firebase.model.ResponseStatus;
import com.luminn.firebase.model.SupplierModel;
import com.luminn.firebase.model.UserModel;
import com.luminn.firebase.repository.UserRepository;
import com.luminn.firebase.security.JwtUtils;
import com.luminn.firebase.service.MessageByLocaleService;
import com.luminn.firebase.service.UserMongoService;
import com.luminn.firebase.util.ErrorNumber;
import com.luminn.firebase.util.Path;
import com.luminn.firebase.util.SMSType;
import com.luminn.firebase.util.Validation;
import com.luminn.view.StatusResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/userLogin")
public class UserLoginController {


    @Autowired
    private MessageByLocaleService messageservice;



    @Autowired
    UserModel userModel;

    @Autowired
    UserMongoService userMongoService;

    @Autowired
    SupplierModel supplierModel;

    @Autowired
    MobileModel mobileModels;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;
    private ResponseEntity getCall(LoginDTO loginDTO){

        StatusResponse sr = new StatusResponse();
        /*if (loginDTO != null && loginDTO.getId() == null ) {
            return getResponse(loginDTO, false, "password_wrong","",204);
        }*/
         if (loginDTO != null && (loginDTO.getId() != null) && loginDTO.getId().length() > 7) {
            return getResponse(loginDTO, true, "user_login",loginDTO.getEmail(),500);
        }
        if (loginDTO != null && (loginDTO.getId() != null) && loginDTO.getId().equalsIgnoreCase("-2")) {
            return getResponse(loginDTO, false, "email_not_existing",loginDTO.getEmail(),206);
        }
        else if (loginDTO != null && (loginDTO.getId() != null) && loginDTO.getId().equalsIgnoreCase("-4")) {
            return getResponse(loginDTO, false, "password_wrong",loginDTO.getEmail(),201);
        }
        else if (loginDTO != null && (loginDTO.getId() != null) && loginDTO.getId().equalsIgnoreCase("5")) {
            return getResponse(loginDTO, false, "password_wrong",loginDTO.getEmail(),201);
        }
        else if (loginDTO != null && (loginDTO.getId() != null) && loginDTO.getId().equalsIgnoreCase("7")) {
            return getResponse(loginDTO, false, "phoneNumber_no",loginDTO.getEmail(),201);
        }
        else if (loginDTO != null && (loginDTO.getId() != null) && loginDTO.getId().equalsIgnoreCase("8")) {
            return getResponse(loginDTO, false, "email_not_existing",loginDTO.getEmail(),203);
        }
        else if (loginDTO != null && (loginDTO.getId() != null) && loginDTO.getId().equalsIgnoreCase("10")) {
            return getResponse(loginDTO, false, "email_not_existing_default",loginDTO.getEmail(),203);
        }

        else if (loginDTO != null && (loginDTO.getTaxiId().equalsIgnoreCase("O"))) {
            return getResponse(loginDTO, false, "email_not_existing",loginDTO.getEmail(),205);
        }
        else if (loginDTO != null && (loginDTO.getId() != null) && (loginDTO.getId().equalsIgnoreCase("7"))  ) {
            return getResponse(loginDTO, false, "user_wrong",loginDTO.getEmail(),205);
        }

        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/app/user/v1/mobile/login", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<StatusResponse> userLoginMobile(@RequestBody PasswordDTO use) throws DatabaseException {

        StatusResponse sr = new StatusResponse();
        LoginDTO loginDTO = null;

        System.out.print(" use " + use.getRole() + " Email --> " + use.getEmail() + " getPhoneNumber()" + use.getPhoneNumber());

        if(use.getRole() == null || "".equalsIgnoreCase(use.getRole()) || use.getRole().equalsIgnoreCase("string")){
            return getResponse(loginDTO, false, "wrong_role","",202);
        }
        else if (!"".equals(use.getEmail()) || !"".equals(use.getPhoneNumber())) {
            loginDTO = userModel.loginUserType(use.getEmail(), use.getPhoneNumber(), use.getPassword(), use.getToken(), use.getRole(), use.getDomain());
            if(use.role.equalsIgnoreCase("ROLE_SUPPLIER")) {
                User user = userModel.getByEmailId(use.getEmail());
                if(user != null) {
                    loginDTO.setName(user.getDomain());
                }
                return getCall(loginDTO);
            }
            else {
                return getCall(loginDTO);
            }
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/app/user/v1/mobile/driver/login", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<StatusResponse> userLoginDriverMobile(@RequestBody SignInDTO use) throws DatabaseException {

        StatusResponse sr = new StatusResponse();
        LoginDTO loginDTO = null;

        System.out.print(" use " +   use.getEmail() + " getPhoneNumber()" + use.getPhoneNumber());


         if (!"".equals(use.getEmail()) || !"".equals(use.getPhoneNumber())) {
            loginDTO = userModel.loginUserType(use.getEmail(), use.getPhoneNumber(), use.getPassword(), use.getToken(), "ROLE_DRIVER", use.getDomain());
            return getCall(loginDTO);
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }



    @ResponseBody
    @RequestMapping(value = "/app/v1/mobile/delete/{emailId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<StatusResponse> deleteId(@PathVariable("emailId") String emailId)  {
        StatusResponse sr = new StatusResponse();
        LoginDTO loginDTO = null;
        sr.setData(userModel.delete(emailId));
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/app/user/v1/mobile/user/login", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<StatusResponse> userLoginUser(@RequestBody SignInDTO use) throws DatabaseException {

        StatusResponse sr = new StatusResponse();
        LoginDTO loginDTO = null;

        System.out.print(" use " +   use.getEmail() + " getPhoneNumber()" + use.getPhoneNumber());


        if (!"".equals(use.getEmail()) || !"".equals(use.getPhoneNumber())) {
            loginDTO = userModel.loginUserType(use.getEmail(), use.getPhoneNumber(), use.getPassword(), use.getToken(), "ROLE_USER", use.getDomain());
            return getCall(loginDTO);
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }




    private ResponseEntity<StatusResponse> registratio(UserDetailsDTO dto){
        StatusResponse statusResponse = new StatusResponse();
        LoginDTO loginDTO = null;
        ModelStatus status = null;
        ResponseStatus status_id = null;
        //PART 1
        System.out.println("--which object ---->" + dto.toString());

        if(dto.getRole().equals(Path.ROLE.SUPPLIER)) {

            List<Supplier> list = checkDuplicateDomainName(dto.getDomain());

            if (list != null)
                System.out.println("--Existing  Supplier id ---->" + list.size());
            else if (list == null && dto.role.equalsIgnoreCase(Path.ROLE.SUPPLIER) && dto.getDomain().length() > 0) {
                System.out.println("-- new Supplier ---->" + list);
            }

            if (list != null) {
                // Long supplierId = list.get(0).getId();
                statusResponse.setMessage(messageservice.getMessage("role_supplier_existing", list.get(0).getId() + list.get(0).getName()));
                statusResponse.setStatus(false);
                statusResponse.setInfoId(200);
                statusResponse.setData(dto.getDomain());
                return new ResponseEntity<>(statusResponse, HttpStatus.OK);
            }
        }

        boolean mobileNumber= false;
        //!mobileNumber &&
        //PART 2
        if (!dto.getEmail().equalsIgnoreCase("string")  && !isEmail(dto.getEmail()) || dto.getPhoneNumber() != null && dto.getPhoneNumber().length() > 5 ) {
            System.out.println("-- mobileNumber ---->"+ mobileNumber);

            try {
                //go to supplier part
                if((dto.getRole().equals(Path.ROLE.SUPPLIER))) {
                    status_id = supplierModel.createUserDriver(dto, true, true);
                    status = status_id.getStatus();
                    dto.setId(status_id.getId());
                } //TRUE- ONLY DRIVER
                else if((dto.getRole().equals(Path.ROLE.USER))) {
                    status_id = supplierModel.createUserDriver(dto, true, true);
                    status = status_id.getStatus();
                    dto.setId(status_id.getId());
                }
                else {
                    status_id = supplierModel.createUserDriver(dto, false, true);
                    if(status_id != null) {
                        status = status_id.getStatus();
                        dto.setId(status_id.getId());
                    }
                }

                //statusResponse.setData(supplierModel.getSuppplierId());
                statusResponse.setInfoId(500);
                statusResponse.setStatus(true);
            } catch (DatabaseException e) {
                return new ResponseEntity<>(statusResponse, HttpStatus.OK);
            }
            catch (Exception e) {
                statusResponse.setStatus(true);
                System.out.println(" Error " + e.getMessage());
                return new ResponseEntity<>(statusResponse, HttpStatus.OK);
            }
            return validateRegistrationInput(statusResponse, status, dto);
        }

        else if((mobileNumber || !mobileNumber) && ("".equals(dto.getEmail()) || dto.getEmail().equalsIgnoreCase("string"))) {



            if (dto.getPhoneNumber() != null && dto.getPhoneNumber().length() > 5) {
                System.out.println("-- getPhoneNumber ---->"+ dto.getPhoneNumber());

                String verification = mobileModels.verificationToken(dto.getPhoneNumber(), dto.getPassword(), SMSType.LOGIN);
                //verification - later we will check it
                if (verification != null && verification.equalsIgnoreCase("OK")) {
                    try {
                        /*if((dto.getRole().equals(Path.ROLE.SUPPLIER)))
                            status = supplierModel.createUserDriver(dto, false,true);
                        else
                            status = supplierModel.createUserDriver(dto, true,true);*/

                        if((dto.getRole().equals(Path.ROLE.SUPPLIER))) {
                            status_id = supplierModel.createUserDriver(dto, false, true);
                            status = status_id.getStatus();
                            dto.setId(status_id.getId());
                        }
                        else {
                            status_id = supplierModel.createUserDriver(dto, true, true);
                            status = status_id.getStatus();
                            dto.setId(status_id.getId());
                        }

                    } catch (DatabaseException e) {
                        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
                    }
                    catch (Exception e) {
                        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
                    }
                }
                if (!verification.equalsIgnoreCase("OK") || dto.getPhoneVerified().equalsIgnoreCase("9999")) {
                    try {
                        //status = supplierModel.createUserDriver(dto, true,true);

                        status_id = supplierModel.createUserDriver(dto, true, true);
                        status = status_id.getStatus();
                        dto.setId(status_id.getId());
                        statusResponse.setInfoId(500);
                    } catch (DatabaseException e) {
                        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
                    }
                    catch (Exception e) {
                        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
                    }
                }
                return validateRegistrationInput(statusResponse, status, dto);
            }
        }
        //mobile true but has email id
        else if((mobileNumber && (!"".equals(dto.getEmail()) &&!isEmail(dto.getEmail()))) ) {

            System.out.println("-- dto.getEmail() ---->"+ dto.getEmail());
            try {
                /*if((dto.getRole().equals(Path.ROLE.SUPPLIER)))
                    status = supplierModel.createUserDriver(dto, false,true);
                else if((dto.getRole().equals(Path.ROLE.DRIVER)))
                    status = supplierModel.createUserDriver(dto, true,true);*/

                if((dto.getRole().equals(Path.ROLE.SUPPLIER))) {
                    status_id = supplierModel.createUserDriver(dto, false, true);
                    status = status_id.getStatus();
                    dto.setId(status_id.getId());
                }
                else {
                    status_id = supplierModel.createUserDriver(dto, true, true);
                    status = status_id.getStatus();
                    dto.setId(status_id.getId());
                }

                statusResponse.setInfoId(500);


            } catch (DatabaseException e) {
                return new ResponseEntity<>(statusResponse, HttpStatus.OK);
            }
            catch (Exception e) {
                return new ResponseEntity<>(statusResponse, HttpStatus.OK);
            }
            return validateRegistrationInput(statusResponse, status, dto);
        }

        //userModel.addUserSupplier(use);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/app/user/v1/mobile/full/registration", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<StatusResponse> fullRegistration(@RequestBody MobileDTO dto) throws DatabaseException {

        UserDetailsDTO uster = new UserDetailsDTO(dto);
       // MobileDTO child = (MobileDTO)uster;
        return registratio(uster);
    }

    @ResponseBody
    @RequestMapping(value = "/app/user/v1/mobile/detail/registration", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<StatusResponse> fullDetailRegistration(@RequestBody UserDetailsDTO userDetailsDTO) throws DatabaseException {
        System.out.println(" userDetailsDTO -->" + userDetailsDTO.toString());

        return registratio(userDetailsDTO);
    }

    private List<Supplier> checkDuplicateDomainName(String domain){
        return supplierModel.getSupplierDomain(domain);

    }

    private boolean isEmail(String emailId){
        int intIndex = 0;

        if(emailId != null && emailId.isEmpty()) {
            return true;
        }

        if(emailId != null && !emailId.isEmpty()) {
            intIndex = emailId.indexOf("@");
        }
        if(intIndex == -1){
            return true;
        }
        return false;
    }

    public ResponseEntity<StatusResponse> checkSupplier(MobileDTO dto){

        StatusResponse statusResponse = new StatusResponse();

      if(dto.getRole().equals(Path.ROLE.SUPPLIER)) {
          List<Supplier> list = checkDuplicateDomainName(dto.getDomain());

          if (list != null)
              System.out.println("--Existing  Supplier id ---->" + list.size());
          else if (list == null && dto.role.equalsIgnoreCase(Path.ROLE.SUPPLIER) && dto.getDomain().length() > 0) {
              System.out.println("-- new Supplier ---->" + list);
          }

          if (list != null) {
              // Long supplierId = list.get(0).getId();
              //statusResponse.setMessage(messageservice.getMessage("role_supplier_existing", list.getId()));
              statusResponse.setStatus(false);
              statusResponse.setInfoId(200);
              statusResponse.setData(dto.getDomain());
              return new ResponseEntity<>(statusResponse, HttpStatus.OK);
          }
      }
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    public ResponseEntity getResponse(String message,String value,boolean status,String successorfailure,int errorCode) {

        StatusResponse sr = new StatusResponse();
        sr.setStatus(status);
        //sr.setMessage(successorfailure);
        if (value != null && !"".equalsIgnoreCase(value))
            sr.setMessage(messageservice.getMessage(message, value));
        else
            sr.setMessage(messageservice.getMessage(message));
        sr.setData(successorfailure);
        sr.setInfoId(errorCode);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }


        private ResponseEntity getResponse(LoginDTO loginDTO,boolean status,String message,String value,int errorCOde){
        StatusResponse sr = new StatusResponse();
        sr.setStatus(false);
        JSONObject js = new JSONObject(loginDTO);
        sr.setData(loginDTO);
        sr.setStatus(status);
        sr.setInfoId(errorCOde);
        if(!"".equalsIgnoreCase(value))
            sr.setMessage(messageservice.getMessage(message,value));
        else
            sr.setMessage(messageservice.getMessage(message));
        if (sr.isStatus()) {
            //lets do it later
            jwtUtils.getJwt(loginDTO.getId(), loginDTO.getRole(), loginDTO.getName(), sr);
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    public ResponseEntity<StatusResponse> validateRegistrationInput(StatusResponse statusResponse,ModelStatus status,MobileDTO dto){
        Long userId = 100l;
        Long supplierId = 1001l;
        Long driverId = 101l;
        //log.info("status -->" + status);
        //PHONENUMBER_IS_EXISTING

        if(status == null){
            //supplier id is empty
            return getResponse("supplier_deleted",dto.getRole(),false,Path.MESSAGE.SUCCESS,ErrorNumber.ROLE_NOT_EXISTING);
        }
        if(status.equals(ModelStatus.ROLE_NOT_EXISTING)){
            return getResponse("wrong_role",dto.getRole(),false,Path.MESSAGE.SUCCESS,ErrorNumber.ROLE_NOT_EXISTING);
        }
       /* if(status.equals(ModelStatus.CITY_WRONG_CODE)){
            statusResponse.setMessage(messageservice.getMessage("city_code_wrong",dto.getCode()));
            statusResponse.setStatus(false);
            statusResponse.setInfoId(ErrorNumber.CITY_WRONG_CODE);
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        }*/
        if(status !=null && status.equals(ModelStatus.EMAIL_IS_EXISTING)){
            statusResponse.setMessage(messageservice.getMessage("email_existing",dto.getEmail()));
            statusResponse.setStatus(false);
            statusResponse.setInfoId(ErrorNumber.EMAIL_IS_EXISTING);
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        }
        if(status !=null && status.equals(ModelStatus.DEVICE_ID_EXISTING)){
            statusResponse.setMessage(messageservice.getMessage("user_device_id_already_exiting",dto.getToken()));
            statusResponse.setStatus(false);
            statusResponse.setInfoId(ErrorNumber.DEVICE_ID_EXISTING);
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        }
        if(status !=null && status.equals(ModelStatus.PHONENUMBER_IS_EXISTING)){
            statusResponse.setMessage(messageservice.getMessage("phoneNumber_not_existing", dto.getPhoneNumber()));
            statusResponse.setData(-1l);
            statusResponse.setStatus(false);
            statusResponse.setInfoId(ErrorNumber.PHONENUMBER_IS_EXISTING);
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        }
        if(status !=null && (status.name().equalsIgnoreCase(ModelStatus.DRIVER_CREATED.name())
                                                                  || status.name().equalsIgnoreCase(ModelStatus.DRIVER_CREATED.name()))){
            return getResponse("driver_user",dto.getFirstName(),true,dto.getId(),ErrorNumber.DRIVER_CREATED);
        }
        if(status !=null && status.equals(ModelStatus.SUPPLIER_CREATED)) {
            //return getResponse("supplier_created",dto.getFirstName(),true,Path.MESSAGE.SUCCESS,500);

            //CK
            statusResponse.setMessage(messageservice.getMessage("supplier_created", 1000));

            //statusResponse.setMessage(messageservice.getMessage("supplier_created", supplierModel.getSuppplierId()));
            //statusResponse.setData(supplierModel.getUserId() + "_" + supplierModel.getSuppplierId());
            statusResponse.setStatus(true);


            statusResponse.setInfoId(ErrorNumber.SUPPLIER_CREATED);
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);


        }
        if(status !=null && status.equals(ModelStatus.NO_SUPPLIER_ID_WHEN_ADD_TAXI)) {
            statusResponse.setMessage(messageservice.getMessage("no_supplier_id_when_create_taxi", supplierId));
            statusResponse.setData(supplierId);
            statusResponse.setInfoId(700);
            statusResponse.setInfoId(ErrorNumber.NO_SUPPLIER_ID_WHEN_ADD_TAXI);
            statusResponse.setStatus(false);
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        }
        if(status !=null && (status.equals(ModelStatus.USER_CREATED) || status.equals(ModelStatus.CREATED))) {
            //statusResponse.setMessage(messageservice.getMessage("user_created", supplierModel.userId));
            statusResponse.setMessage(messageservice.getMessage("user_created", dto.getFirstName()));
            statusResponse.setData(userId);
            statusResponse.setStatus(true);
            statusResponse.setInfoId(ErrorNumber.USER_CREATED);
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        }
        if(status !=null && status.equals(ModelStatus.NO_TAG_CODE)) {
            statusResponse.setMessage(messageservice.getMessage("tag_code_is_not_existing",userId));
            //statusResponse.setData(supplierModel.userId);
            statusResponse.setInfoId(ErrorNumber.NO_TAG_CODE);
            statusResponse.setStatus(false);
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        }
        if(status !=null && status.equals(ModelStatus.CAR_TYPE_NOT_EXISTING)) {
            statusResponse.setMessage(messageservice.getMessage("car_type_is_not_existing", dto.getCarType()));
            //statusResponse.setData(supplierModel.userId);
            statusResponse.setStatus(false);
            statusResponse.setInfoId(ErrorNumber.CAR_TYPE_NOT_EXISTING);
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }
}
