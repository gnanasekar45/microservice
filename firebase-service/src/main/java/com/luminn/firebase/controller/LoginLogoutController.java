package com.luminn.firebase.controller;

import com.google.firebase.database.DatabaseException;
import com.luminn.firebase.dto.UserDTO;
import com.luminn.firebase.dto.UserInfoDTO;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.model.ModelStatus;
import com.luminn.firebase.model.SupplierModel;
import com.luminn.firebase.model.TaxiModel;
import com.luminn.firebase.model.UserModel;
import com.luminn.firebase.request.LongDTO;
import com.luminn.firebase.security.HashCodeUtils;
import com.luminn.firebase.security.JwtUtils;
import com.luminn.firebase.service.MessageByLocaleService;
import com.luminn.firebase.service.UserService;
import com.luminn.firebase.util.ErrorNumber;
import com.luminn.firebase.util.Path;
import com.luminn.firebase.util.STEPS;
import com.luminn.view.StatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/logout")
public class LoginLogoutController {


    @Autowired
    UserModel userModel;

    @Autowired
    TaxiModel taxiModel;

    @Autowired
    SupplierModel supplierModel;

    //@Autowired
    //public CityModel citymodel;

    //@Autowired
    //private CityService cityService;

    @Autowired
    UserService userService;

    @Autowired
    private MessageByLocaleService messageService;

    @Autowired
    private HashCodeUtils hashCodeUtils;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${siteUrl}")
    private String siteUrl;
    @Value("${sendingEmail}")
    private String sendingEmail;
    @Value("${emailTemplateFolder}")
    private String emailTemplateFolder;
    @Value("${email_verify}")
    private String emailTemplateResetPassword;

    private static final Logger log = LoggerFactory.getLogger(LoginLogoutController.class);



    @Autowired
    private MessageByLocaleService messageservice;

    public ResponseEntity getResponse(String message, String value, boolean status, String successorfailure, int errorCode){

        StatusResponse sr = new StatusResponse();
        sr.setStatus(status);
        //sr.setMessage(successorfailure);
        if(value != null && !"".equalsIgnoreCase(value))
            sr.setMessage(messageservice.getMessage(message,value));
        else
            sr.setMessage(messageservice.getMessage(message));
        sr.setData(successorfailure);


        sr.setInfoId(errorCode);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }


    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.USER_DETAIL + Path.Url.VERSION_V1 + "/{userId}", method = RequestMethod.GET, produces = "application/json")
    private ResponseEntity<StatusResponse> getUserById(@PathVariable("userId") String userId)  {
        UserDTO userDTO = null;
        StatusResponse sr = new StatusResponse();

        if(userId  != null)
            userDTO = userModel.getDTO(userId);

        if(userDTO != null && userDTO.getId() != null ) {
            sr.setStatus(true);
            sr.setInfoId(ErrorNumber.USER_DETAILS);
        }
        else {
            sr.setStatus(false);
        }
        sr.setData(userDTO);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.USER_DETAIL + Path.Url.VERSION_V2 + "/{userId}", method = RequestMethod.GET, produces = "application/json")
    private ResponseEntity<StatusResponse> getUserByIdImage(@PathVariable("userId") String userId)  {
        UserInfoDTO userDTO = null;
        StatusResponse sr = new StatusResponse();

        if(userId  != null)
            userDTO = userModel.getImageDTO(userId);

        if(userDTO != null && userDTO.getId() != null ) {
            sr.setStatus(true);
            sr.setInfoId(ErrorNumber.USER_DETAILS);
        }
        else {
            sr.setStatus(false);
        }
        sr.setData(userDTO);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.USERUPDATE + Path.Url.VERSION_V1 + Path.Url.NAME +  "/{userId}" + "/{firstName}" + "/{lastName}", method = RequestMethod.PUT, produces = "application/json")
    private ResponseEntity<StatusResponse> changeUserNames(@PathVariable("userId") String userId, @PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
        UserDTO userDTO = null;
        StatusResponse sr = new StatusResponse();

        try {
            ModelStatus status = userModel.changeName(userId, firstName, lastName);
            if (status.equals(ModelStatus.UPDATED)) {
                sr.setMessage(messageService.getMessage("user_name_updated", userId));
                sr.setStatus(true);
                return new ResponseEntity<StatusResponse>(sr, HttpStatus.OK);
            }
        } catch (DatabaseException ex) {
            sr.setMessage(messageService.getMessage("user_update_exception"));
            sr.setStatus(false);
            log.info("Error" + ex.getStackTrace());
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }


    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.USERUPDATE + Path.Url.PROFILE_PIC + Path.Url.VERSION_V1, method = RequestMethod.POST, produces = "application/json")
    private ResponseEntity<UserDTO> changeImages(@RequestBody UserDTO userDTO) {
        StatusResponse sr = new StatusResponse();

        try {
            //CK
            //ModelStatus status = userModel.changeImage(userDTO);
            //if (status.equals(ModelStatus.UPDATED)) {
              //  sr.setMessage(messageService.getMessage("user_name_updated", userDTO.getId()));
            //}
        } catch (DatabaseException ex) {
            sr.setMessage(messageService.getMessage("user_update_exception"));
            log.info("Error" + ex.getStackTrace());
        }
        return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
    }








    @ResponseBody
    @RequestMapping(value = "/app/user/v1/email" + "/{userId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<StatusResponse> getUserEMail(@PathVariable("userId") String userId) throws DatabaseException {

        User user = userModel.validate(userId);
        StatusResponse sr = new StatusResponse();

        if (user != null && user.getEmail() != null) {
            log.info("id ---" + user.getId() + "EMail role ==" + "");
            sr.setData(user.getEmail());
            sr.setMessage(messageService.getMessage("user_email"));
            sr.setStatus(true);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        } else {
            sr.setMessage(messageService.getMessage("user_no_email", userId));
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/app/admin/user/v1/fcm/update" + "/{userId}" + "/{fcm}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<StatusResponse> updateUserOneSignal(@PathVariable("userId") String userId, @PathVariable("fcm") String fcm) throws DatabaseException {
        User user = userModel.validate(userId);
        StatusResponse sr = new StatusResponse();
        List<UserDTO> userDTO = null;
        if (user != null) {

            //CK
            //ModelStatus status = userModel.updateOneSignal(userId, fcm);
            ModelStatus status = null;
            if (status.equals(ModelStatus.UPDATED)) {
                sr.setMessage(Path.MESSAGE.UPDATE);
                sr.setData(messageService.getMessage("user_device_id_updated", userId));
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            sr.setData(messageService.getMessage("user_no_permission"));
        }
        sr.setMessage(Path.MESSAGE.FAILURE);
        sr.setData(messageService.getMessage("user_device_id_not_exiting", userId));
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }


    @ResponseBody
    @RequestMapping(value = "/app/user/v1/fcm/update" + "/{userId}" + "/{oneSignalValue}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<StatusResponse> updateUserFCM(@PathVariable("userId") String userId, @PathVariable("oneSignalValue") String oneSignalValue) throws DatabaseException {

        User user = userModel.validate(userId);
        StatusResponse sr = new StatusResponse();
        List<UserDTO> userDTO = null;
        if (user != null) {

            //CK
            //ModelStatus status = userModel.updateOneSignal(userId, oneSignalValue);
            ModelStatus status = null;
            if (status != null && status.name().equals(ModelStatus.UPDATED.name())) {
                sr.setMessage(Path.MESSAGE.UPDATE);
                sr.setData(messageService.getMessage("user_device_id_updated", userId));
                sr.setStatus(true);
                sr.setInfoId(ErrorNumber.FCM_STATUS_UPDATE);
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            else {
                sr.setStatus(false);
                sr.setInfoId(ErrorNumber.FCM_STATUS_UPDATE_ERROR);
                sr.setData(messageService.getMessage("user_no_permission"));
            }
        }
        sr.setInfoId(ErrorNumber.FCM_STATUS_UPDATE_ERROR);
        sr.setMessage(Path.MESSAGE.FAILURE);
        sr.setData(messageService.getMessage("user_device_id_not_exiting", userId));
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/app/admin/user/v1/notification/update" + "/{userId}" + "/{notification}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<StatusResponse> updateNotification(@PathVariable("userId") String userId, @PathVariable("notification") String notification) throws DatabaseException {
        User user = userModel.validate(userId);
        StatusResponse sr = new StatusResponse();
        List<UserDTO> userDTO = null;
        if (user != null) {

            ModelStatus status = null;
            //ModelStatus status = userModel.updateNotification(userId, notification);
            if (status.equals(ModelStatus.UPDATED)) {
                sr.setMessage(messageService.getMessage("user_device_id_updated", notification));
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            sr.setMessage(messageService.getMessage("user_no_permission"));
        }
        sr.setMessage(messageService.getMessage("user_device_id_not_exiting", userId));
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    /*@ResponseBody
    @RequestMapping(value = "/app/admin/user/v1/fcm/update", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<StatusResponse> updatefcmNotification(@RequestBody UserDTO userDTO) throws DatabaseException {
        User user = userModel.validate(userDTO.getId());
        StatusResponse sr = new StatusResponse();
        List<UserDTO> luserDTO = null;
        if (user != null) {
            ModelStatus status = userModel.updateNotification(userDTO.getId(), userDTO.getNotification());
            if (status.equals(ModelStatus.UPDATED)) {
                log.info("It is updated -->" + userDTO.getId() + " .noti.." + userDTO.getNotification());
                sr.setMessage(messageService.getMessage("user_device_id_updated", userDTO.getNotification()));
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            sr.setMessage(messageService.getMessage("user_no_permission"));
        }
        sr.setMessage(messageService.getMessage("user_device_id_not_exiting", userDTO.getId()));
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }*/

    @ResponseBody
    @RequestMapping(value = "/app/admin/user/v1/loginStatus/update" + "/{userId}" + "/{loginStatus}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<StatusResponse> updateLoginStatus(@PathVariable("userId") String userId, @PathVariable("loginStatus") String loginStatus) throws DatabaseException {
        User user = userModel.validate(userId);
        StatusResponse sr = new StatusResponse();
        List<UserDTO> userDTO = null;
        if (user != null) {
            ModelStatus status = userModel.updateLoginStatus(userId, loginStatus);
            if (status.equals(ModelStatus.UPDATED)) {
                sr.setMessage(messageService.getMessage("user_device_id_updated", loginStatus));
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            sr.setMessage(messageService.getMessage("user_no_permission"));
        }
        sr.setMessage(messageService.getMessage("user_device_id_not_exiting", userId));
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/app/admin/user/v1/loginStatus" + "/{userId}" + "/{loginStatus}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<StatusResponse> LoginStatus(@PathVariable("userId") String userId, @PathVariable("loginStatus") String loginStatus) throws DatabaseException {

        User user = userModel.validate(userId);
        StatusResponse sr = new StatusResponse();
        List<UserDTO> userDTO = null;
        if (user != null) {
            ModelStatus status = userModel.updateLoginStatus(userId, loginStatus);
            if (status.equals(ModelStatus.UPDATED)) {
                sr.setStatus(true);
                sr.setInfoId(ErrorNumber.USER_LOGOUT_STATUS);
                sr.setMessage(messageService.getMessage("user_device_id_updated", loginStatus));
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            sr.setMessage(messageService.getMessage("user_no_permission"));
        }
        else {
            sr.setStatus(false);

            sr.setInfoId(ErrorNumber.USER_LOGOUT_ERROR);
            sr.setMessage(messageService.getMessage("user_device_id_not_exiting"));
        }

        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/app/user/v1/language/update" + "/{userId}" + "/{language}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<StatusResponse> updateLanguage(@PathVariable("userId") String userId, @PathVariable("language") String language) throws DatabaseException {
        User user = userModel.validate(userId);
        StatusResponse sr = new StatusResponse();
        List<UserDTO> userDTO = null;
        if (user != null) {
            ModelStatus status = userModel.updateLanguage(userId, language);
            if (status.equals(ModelStatus.UPDATED)) {
                sr.setInfoId(ErrorNumber.LANGUAGE_UPDATE);
                sr.setMessage(messageService.getMessage("user_device_id_updated", language));
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            sr.setMessage(messageService.getMessage("user_no_permission"));
        }
        sr.setInfoId(ErrorNumber.LANGUAGE_UPDATE_ERROR);
        sr.setMessage(messageService.getMessage("user_device_id_not_exiting", userId));
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/app/user/v1/step/update" + "/{userId}" + "/{status}", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<StatusResponse> updateStateStatus(@PathVariable("userId") String userId, @PathVariable("status") String language) throws DatabaseException {
        User user = userModel.validate(userId);
        StatusResponse sr = new StatusResponse();
        List<UserDTO> userDTO = null;
        if (user != null) {
            ModelStatus status = userModel.updateStepStatus(userId, language);
            if (status.equals(ModelStatus.UPDATED)) {
                sr.setMessage(messageService.getMessage("user_device_id_updated", language));
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            sr.setMessage(messageService.getMessage("user_no_permission"));
        }
        sr.setMessage(messageService.getMessage("user_device_id_not_exiting", userId));
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/app/user/v1/step/get" + "/{userId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<StatusResponse> getUserStatus(@PathVariable("userId") String userId) throws DatabaseException {


        StatusResponse sr = new StatusResponse();
        List<UserDTO> userDTO = null;
        User user = null;
        LongDTO loginDTO = null;
        if (userId != null)
            user = userModel.validate(userId);

        if (user != null && user.getId() != null) {
            try {
                loginDTO = userModel.getUser(userId);
                sr.setData(loginDTO.getValues());
                sr.setMessage(loginDTO.getValues());
            } catch (DatabaseException e) {

            }
            if (loginDTO.equals(ModelStatus.UPDATED)) {
                sr.setMessage(messageService.getMessage("user_device_id_updated", userId));
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            sr.setMessage(messageService.getMessage("user_no_permission"));
        } else {

            sr.setData(STEPS.LOGIN);
            sr.setMessage(STEPS.LOGIN);
        }
        sr.setMessage(messageService.getMessage("user_device_id_not_exiting", userId));
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

   /* @ResponseBody
    @RequestMapping(value = "/app/admin/user/v1/onesignal/delete" + "/{userId}" + "/{oneSignalValue}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<StatusResponse> deleteUserOneSignal(@PathVariable("userId") String userId, @PathVariable("oneSignalValue") String oneSignalValue) throws DatabaseException {
        User user = userModel.validate(userId);
        StatusResponse sr = new StatusResponse();
        List<UserDTO> userDTO = null;
        if (user != null) {
            ModelStatus status = userModel.deleteOneSignal(userId, oneSignalValue);
            if (status.equals(ModelStatus.DELETED)) {
                sr.setMessage(messageService.getMessage("user_device_id", oneSignalValue));
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            sr.setMessage(messageService.getMessage("user_no_permission"));
        }
        sr.setMessage(messageService.getMessage("user_device_id_not_exiting", userId));
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }*/

    @ResponseBody
    @RequestMapping(value = "/app/user/v1/type/login/isVerified", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<StatusResponse> userLoginTaxiVerified(@RequestBody UserDTO use) throws DatabaseException {
        StatusResponse sr = new StatusResponse();
        sr.setStatus(false);
        sr.setMessage(Path.MESSAGE.FAILURE);

        User user =  userModel.checkGetByPhoneNumber(use.getPhoneNumber(), use.getRole(),use.getWebsite());
        if(user == null || user.getPhoneVerified() != null && user.getPhoneVerified().equalsIgnoreCase(Path.MESSAGE.VERIFIED) ){
            sr.setMessage(Path.MESSAGE.FAILURE);
            sr.setData(user.getPhoneVerified());
        }
        else if(user == null || user.getPhoneVerified() != null && user.getPhoneVerified().equalsIgnoreCase(Path.MESSAGE.VERIFIED) ){
            sr.setMessage(Path.MESSAGE.SUCCESS);
            sr.setData(user.getPhoneVerified());
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }



    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.USER + Path.Url.VERSION_V2 + Path.OperationUrl.UPDATE, method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<StatusResponse> updateUser(@RequestBody UserDTO dto) throws DatabaseException {
        ModelStatus status = supplierModel.updateUser(dto);

        StatusResponse sr = new StatusResponse();

        if (status.equals(ModelStatus.UPDATED)) {
            sr.setStatus(true);
            sr.setData(messageService.getMessage("user_updated", dto.getId()));
            sr.setMessage(Path.MESSAGE.SUCCESS);
        } else
            sr.setMessage(Path.MESSAGE.FAILURE);
        sr.setData(messageService.getMessage("user_update_exception", dto.getId()));
        sr.setData(supplierModel.getUserName());
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.USER + Path.Url.VERSION_V1 + Path.OperationUrl.UPDATE_SETTING, method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<StatusResponse> updateUserSetting(@RequestBody UserDTO dto) throws DatabaseException {
        ModelStatus status = supplierModel.updateUser(dto);

        StatusResponse sr = new StatusResponse();

        if (status.equals(ModelStatus.UPDATED)) {
            sr.setStatus(true);
            sr.setData(messageService.getMessage("user_updated", dto.getId()));
            sr.setMessage(Path.MESSAGE.SUCCESS);
        } else
            sr.setMessage(Path.MESSAGE.FAILURE);
        sr.setData(messageService.getMessage("user_update_exception", dto.getId()));
        sr.setData(supplierModel.getUserName());
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.USER + "/S" + Path.Url.VERSION_V1 + Path.OperationUrl.UPDATE, method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<StatusResponse> updateUsers(@RequestBody UserDTO dto) throws DatabaseException {
        ModelStatus status = supplierModel.updateUser(dto);

        StatusResponse sr = new StatusResponse();

        if (status.equals(ModelStatus.UPDATED)) {
            sr.setStatus(true);
            sr.setData(messageService.getMessage("user_updated", dto.getId()));
            sr.setMessage(Path.MESSAGE.SUCCESS);
        } else
            sr.setMessage(Path.MESSAGE.FAILURE);
        sr.setData(messageService.getMessage("user_update_exception", dto.getId()));
        sr.setData(supplierModel.getUserName());
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

   /* @RequestMapping(value = Path.Url.APP + Path.Url.USER + Path.Url.DRIVER + Path.Url.VERSION_V1 + Path.OperationUrl.SEARCH + "/{param}", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<StatusResponse> taxiSearchByName(@PathVariable("param") String param) {
        StatusResponse sr = new StatusResponse();
        List<UserDTO> userDTOList = new ArrayList<>(0);
        if (param != null && !"".equals(param)) {
            List<User> userList = userService.getUserByName(param);
            for (User user : userList) {
                UserDTO userDTO = null;
                if (user != null) {
                    userDTO = Converter.entityToDTOHideId(user);
                }
                userDTOList.add(userDTO);
            }
        }
        sr.setData(userDTOList);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }*/
}
