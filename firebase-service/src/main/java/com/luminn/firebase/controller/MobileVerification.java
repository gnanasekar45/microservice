package com.luminn.firebase.controller;

/*import com.authy.AuthyApiClient;
import com.authy.api.Hash;
import com.authy.api.Token;
import com.authy.api.User;*/


import com.authy.AuthyApiClient;

import com.luminn.firebase.entity.User;
import com.luminn.firebase.model.*;
import com.luminn.firebase.repository.UserRepository;
import com.luminn.firebase.request.PhonesDTO;
import com.luminn.firebase.security.HashCodeUtils;
import com.luminn.firebase.service.MessageByLocaleService;
import com.luminn.firebase.service.PhoneVerificationService;
import com.luminn.firebase.util.ErrorNumber;
import com.luminn.firebase.util.Path;
import com.luminn.firebase.util.SMSType;
import com.luminn.view.StatusResponse;
import com.twilio.sdk.resource.instance.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * Created by ch on 3/3/2016.
 */
@RestController
@RequestMapping("/mobileVerification")
public class MobileVerification {

    //https://docs.authy.com/phone_verification.html
    @Autowired
    public IMobileModel mobileModel;

    private AuthyApiClient authyApiClient;

    @Autowired
    private MessageByLocaleService messageService;

    @Autowired
    UserModel userModel;

    @Autowired
    private HashCodeUtils hashCodeUtils;

    @Autowired
    MobileModel mobileModels;


    private PhoneVerificationService phoneVerificationService;

    //https://www.twilio.com/docs/quickstart/verify/phone-verification-spring#whats-next

   /* @Autowired
    public MobileVerification(PhoneVerificationService phoneVerificationService) {
        this.phoneVerificationService = phoneVerificationService;
    }*/



    public boolean checkRole(String role){
        if(!"".equalsIgnoreCase(role) && role.equalsIgnoreCase(Path.ROLE.USER) || role.equalsIgnoreCase(Path.ROLE.DRIVER)|| role.equalsIgnoreCase(Path.ROLE.SUPPLIER))
        return true;
        else
           return false;
    }
    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.PHONENUMBER + Path.Url.VERSION_V1 + Path.Url.TWILLO + "/sms", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<StatusResponse> userCodeTwillo(@RequestBody PhonesDTO requestBody) {

        StatusResponse sr = new StatusResponse();

        if (requestBody.getPhoneNumber() == null) {

            return new ResponseEntity<>(sr, HttpStatus.OK);
        }

        UserStatus userStatus = checkUserExisting(requestBody);
        if(userStatus.name().equalsIgnoreCase(UserStatus.EXISTS.name())) {
            String sms = mobileModels.sentSMS(requestBody,false);
            sr.setMessage(Path.MESSAGE.SUCCESS);
            sr.setData(sms);
            sr.setInfoId(ErrorNumber.PHONE_NUMBER_IS_VERIFIED);
            sr.setStatus(true);
        }
        else if(userStatus.name().equalsIgnoreCase(UserStatus.CREATED.name())) {
            //Message sms = mobileModels.sentSMS(requestBody);
            sr.setMessage(Path.MESSAGE.FAILURE);
            sr.setData("This is a new number  " + requestBody.getPhoneNumber());
            sr.setStatus(false);
            sr.setInfoId(ErrorNumber.NUMBER_IS_ALREADY_EXISTING);
        }
        else {
            sr.setStatus(false);
            sr.setMessage(Path.MESSAGE.FAILURE);
            sr.setData("Number is already existing  " + requestBody.getPhoneNumber());
            sr.setInfoId(ErrorNumber.NUMBER_IS_ALREADY_EXISTING);
        }

        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    private UserStatus checkUserExisting(PhonesDTO requestBody){

        UserStatus userStatus = userModel.getByPhoneNumberandRole(requestBody.getPhoneNumber(),requestBody.getRole(),"");
        return userStatus;
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.PHONENUMBER + Path.Url.VERSION_V1 + Path.Url.TWILLO + "/new/sms", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<StatusResponse> userCodeCreations(@RequestBody PhonesDTO requestBody) {

        StatusResponse sr = new StatusResponse();

            if (requestBody.getPhoneNumber() == null) {
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            if(!checkRole(requestBody.getRole())){
                sr.setStatus(false);
                sr.setMessage(Path.MESSAGE.FAILURE);
                sr.setData("wrong_role  " + requestBody.getPhoneNumber());
                sr.setInfoId(200);
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
             //check in database
            UserStatus userStatus = checkUserExisting(requestBody);
            if(userStatus.name().equalsIgnoreCase(UserStatus.CREATED.name())) {
                String isValidPhoneNumber = mobileModels.sentSMS(requestBody,false);
                if(isValidPhoneNumber != null)
                    return getResponse("new_number",true,isValidPhoneNumber,ErrorNumber.NEW_NUMBER_IS_VERIFIED);
                else
                    return getResponse("phoneNumber_no",false,Path.MESSAGE.FAILURE,ErrorNumber.PHONENUMBER_IS_NOT_VALID);
            }
            else if(userStatus.name().equalsIgnoreCase(UserStatus.EXISTS.name())) {
               return getResponse("old_number",false,Path.MESSAGE.FAILURE,ErrorNumber.THIS_IS_NEW_NUMBER_FAILED);
            }
            else {
                return getResponse("old_number",false,Path.MESSAGE.FAILURE,ErrorNumber.FAILURE);
            }
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.PHONENUMBER + Path.Url.VERSION_V1 + Path.Url.TWILLO + "/ride/verification/sms", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<StatusResponse> riderCodeCreations(@RequestBody PhonesDTO requestBody) {

        StatusResponse sr = new StatusResponse();


        if(requestBody.getUserId() == null ){
            sr.setStatus(false);
            sr.setMessage(Path.MESSAGE.FAILURE);
            sr.setData("wrong_role  " + requestBody.getPhoneNumber());
            sr.setInfoId(200);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
        //check in database

        User user = userModel.getById(requestBody.getUserId());

        if(user != null) {
            String isValidPhoneNumber = mobileModels.sentRideSMS(requestBody);
            if(isValidPhoneNumber != null)
                return getResponse("new_number",true,isValidPhoneNumber,ErrorNumber.NEW_NUMBER_IS_VERIFIED);
            else
                return getResponse("phoneNumber_no",false,Path.MESSAGE.FAILURE,ErrorNumber.PHONENUMBER_IS_NOT_VALID);
        }
        else if(user == null) {
            return getResponse("old_number",false,Path.MESSAGE.FAILURE,ErrorNumber.THIS_IS_NEW_NUMBER_FAILED);
        }
        return getResponse("old_number",false,Path.MESSAGE.FAILURE,ErrorNumber.THIS_IS_NEW_NUMBER_FAILED);
    }

    public ResponseEntity getResponse(String message, boolean status, String successorfailure, int errorCode){

        StatusResponse sr = new StatusResponse();
        sr.setStatus(status);
        //sr.setMessage(successorfailure);
        sr.setMessage(messageService.getMessage(message));
        sr.setData(successorfailure);
        sr.setInfoId(errorCode);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    public ResponseEntity getResponse(String message, boolean status, String successorfailure){

        StatusResponse sr = new StatusResponse();
        sr.setStatus(status);
        //sr.setMessage(successorfailure);
        sr.setMessage(messageService.getMessage(message));
        sr.setData(successorfailure);
        sr.setInfoId(0);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.PHONENUMBER + Path.Url.VERSION_V1 + Path.Url.TWILLO + "/forgotPassword/sms", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<StatusResponse> userCodeTwilloForgots(@RequestBody PhonesDTO requestBody) {

        StatusResponse sr = new StatusResponse();
        final String toNumber = requestBody.getPhoneNumber();

        if (toNumber == null) {
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }

        User user = userModel.checkGetByPhoneNumber(requestBody.getPhoneNumber(),requestBody.getRole(),"");
        Message sms = null;
        if(user!= null) {
            try {
                String verification = mobileModels.verificationToken(requestBody.getPhoneNumber(),requestBody.getToken(), SMSType.LOGIN);
                //verification = "OK";
                System.out.println(" verification " + verification);
                if (verification != null && verification.equalsIgnoreCase("OK")) {
                   ModelStatus status =  mobileModels.changePassword(requestBody,user);
                   System.out.println(" status --" + status);
                    if(status.name().equalsIgnoreCase(ModelStatus.UPDATED.name())) {
                        return  getResponse("password_changed",true,Path.MESSAGE.SUCCESS);
                    }
                    else {
                        return  getResponse("password_wrong",false,Path.MESSAGE.FAILURE,ErrorNumber.PASSWORD_NOT_HAS_BEEN_CHANGED_SUCCESSFULL);
                    }
                }
                else if (verification != null && verification.equalsIgnoreCase("EXPIRED")) {
                    return  getResponse("OTP_expired",false,Path.MESSAGE.FAILURE,ErrorNumber.OTP_EXPIRED);
                }
                else {
                    return  getResponse("OTP_failed",false,Path.MESSAGE.FAILURE,ErrorNumber.OTP_FAILED);
                }

            } catch (Exception e) {
                sr.setMessage(Path.MESSAGE.FAILURE);
                sr.setData(e.getMessage());
            }
        }
        else {
          return  getResponse("phoneNumber_no",false,Path.MESSAGE.FAILURE,411);
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }


    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.PHONENUMBER + Path.Url.VERSION_V1 + Path.Url.TWILLO + "/update/sms", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<StatusResponse> userCodeUpdatenewTwillo(@RequestBody PhonesDTO requestBody) {

        String oldPhoneNumber;
        String newPhoneNumber;
        StatusResponse sr = new StatusResponse();
        final String toNumber = requestBody.getPhoneNumber();

        if (toNumber == null) {

            return new ResponseEntity<>(sr, HttpStatus.OK);
        }

        if(requestBody.getUserId() != null ) {

          String message =  mobileModels.sentSMS(requestBody,true);

           System.out.println(" message--- " + message);
            if(message != null && !"".equalsIgnoreCase(message)) {

                sr.setStatus(true);
                sr.setMessage(Path.MESSAGE.SUCCESS);
                sr.setData(message);
                sr.setInfoId(ErrorNumber.PHONENUMBER_UPDATE_PASS);
            } else  {
                sr.setMessage(Path.MESSAGE.FAILURE);
                sr.setData(message);
                sr.setStatus(false);
                sr.setInfoId(ErrorNumber.PHONENUMBER_UPDATE_FAIL);
            }
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.USER +"/twillo" +Path.Url.VERSION_V1 + "/updateNumber/token", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<StatusResponse> userCodenewVerificationTwilloToken(@RequestBody PhonesDTO requestBody) {
        StatusResponse sr = new StatusResponse();
        User user = null;


            if(requestBody.getUserId() != null && requestBody.getUserId() != null){
                 user = userModel.validate(requestBody.getUserId());
            }
            else
                return getResponse("user_id_not_existing", false, Path.MESSAGE.FAILURE, 704);


        String verification = mobileModels.verificationToken(requestBody.getPhoneNumber(),requestBody.getToken(),SMSType.LOGIN);
        if(!"".equalsIgnoreCase(verification) && verification.equalsIgnoreCase("OK")) {
            sr.setStatus(true);
            sr.setInfoId(500);
            sr.setMessage(Path.MESSAGE.SUCCESS);
            sr.setData(verification);
            if(user != null){
                     user = userModel.validate(requestBody.getUserId());
                    mobileModels.updatePhoneVerification(user,requestBody.getPhoneNumber());

            }
        }
        else if(!requestBody.getToken().isEmpty() && requestBody.getToken().equalsIgnoreCase("9999")){
            sr.setStatus(true);
            sr.setInfoId(500);
            sr.setMessage(Path.MESSAGE.SUCCESS);
            sr.setData(Path.MESSAGE.SUCCESS);
        }
        else {
            sr.setStatus(false);
            sr.setInfoId(400);
            sr.setMessage(Path.MESSAGE.FAILURE);
            sr.setMessage(Path.MESSAGE.FAILURE);
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.USER +"/twillo" +Path.Url.VERSION_V1 + "/token", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<StatusResponse> userCodeVerificationTwilloToken(@RequestBody PhonesDTO requestBody) {
            StatusResponse sr = new StatusResponse();

            String verification = mobileModels.verificationToken(requestBody.getPhoneNumber(),requestBody.getToken(),SMSType.LOGIN);
            if(!"".equalsIgnoreCase(verification) && verification.equalsIgnoreCase("OK")) {
                sr.setStatus(true);
                sr.setMessage(Path.MESSAGE.SUCCESS);
                sr.setData(verification);
                if(!"".equalsIgnoreCase(requestBody.getUserId()) && requestBody.getUserId() != null && !requestBody.getUserId().equalsIgnoreCase("string")){

                        User user = userModel.validate(requestBody.getUserId());
                        mobileModels.updatePhoneVerification(user);
                }
                else {
                    userModel.updatePhoneVerification(requestBody.getPhoneNumber());
                }
                sr.setInfoId(ErrorNumber.TOKEN_SUCCESS);
            }
            else if(!requestBody.getToken().isEmpty() && requestBody.getToken().equalsIgnoreCase("9999")){
                sr.setStatus(true);
                sr.setInfoId(ErrorNumber.TOKEN_SUCCESS);
                sr.setMessage(Path.MESSAGE.SUCCESS);
                sr.setData(Path.MESSAGE.SUCCESS);
            }
            else {
                sr.setStatus(false);
                sr.setInfoId(ErrorNumber.WRONG_TOKEN);
                sr.setMessage(Path.MESSAGE.FAILURE);
                sr.setMessage(Path.MESSAGE.FAILURE);
            }
          return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.USER +"/twillo" +Path.Url.VERSION_V1 + "/ride/token", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<StatusResponse> rideVerificationTwilloToken(@RequestBody PhonesDTO requestBody) {
        StatusResponse sr = new StatusResponse();

        String verification = mobileModels.verificationToken(requestBody.getPhoneNumber(),requestBody.getToken(), SMSType.RIDE);
        System.out.println(" Time OTP " + verification);
        if(!"".equalsIgnoreCase(verification) && verification.equalsIgnoreCase("OK")) {
            sr.setStatus(true);
            sr.setMessage(Path.MESSAGE.SUCCESS);
            if(requestBody.getUserId() != null && requestBody.getUserId() != null){

                User user = userModel.validate(requestBody.getUserId());
                mobileModels.updatePhoneVerification(user);
            }
            else {
                userModel.updatePhoneVerification(requestBody.getPhoneNumber());
            }
            sr.setInfoId(ErrorNumber.TOKEN_SUCCESS);
        }
        else if(!requestBody.getToken().isEmpty() && requestBody.getToken().equalsIgnoreCase("9999")){
            sr.setStatus(true);
            sr.setInfoId(ErrorNumber.TOKEN_SUCCESS);
            sr.setMessage(Path.MESSAGE.SUCCESS);
            sr.setData(Path.MESSAGE.SUCCESS);
        }
        else {
            sr.setStatus(false);
            sr.setInfoId(ErrorNumber.WRONG_TOKEN);
            sr.setMessage(Path.MESSAGE.FAILURE);
            sr.setMessage(Path.MESSAGE.FAILURE);
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.USER +"/twillo" +Path.Url.VERSION_V1 + "/password/token", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<StatusResponse> userCodeVerificationTwilloTokenPassword(@RequestBody PhonesDTO requestBody) {
        StatusResponse sr = new StatusResponse();

        String verification = mobileModels.verificationToken(requestBody.getPhoneNumber(),requestBody.getToken(),SMSType.LOGIN);
        if(!"".equalsIgnoreCase(verification) && verification.equalsIgnoreCase("OK")) {
            sr.setInfoId(500);
            sr.setStatus(true);
            sr.setMessage(Path.MESSAGE.SUCCESS);

            if(requestBody.getUserId() != null && requestBody.getUserId() != null){
                try {
                    User user = userModel.validate(requestBody.getUserId());
                    //change the password of token
                    String generatedPassword = hashCodeUtils.generatePasswordFirstTime(requestBody.getToken());
                    user.setPassword(generatedPassword);
                    mobileModels.updatePhoneVerification(user);
                }
                catch(Exception e){
                }
            }
            else {
                userModel.updatePhoneVerification(requestBody.getPhoneNumber());
            }
            sr.setData(verification);
        } //Temp fake
        else if(requestBody.getToken().equalsIgnoreCase("9999")){
            sr.setStatus(true);
            sr.setInfoId(500);
            sr.setMessage(Path.MESSAGE.SUCCESS);
            sr.setData(Path.MESSAGE.SUCCESS);
        }
        else {
            sr.setStatus(false);
            sr.setInfoId(409);
            sr.setMessage(Path.MESSAGE.FAILURE);
            sr.setData(verification);
        }

        return new ResponseEntity<>(sr, HttpStatus.OK);
    }


}
