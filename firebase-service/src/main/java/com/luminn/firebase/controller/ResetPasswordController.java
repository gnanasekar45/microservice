package com.luminn.firebase.controller;


import com.google.firebase.database.DatabaseException;
import com.luminn.firebase.dto.UserDTO;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.model.EmailModel;
import com.luminn.firebase.model.ModelStatus;
import com.luminn.firebase.model.UserModel;
import com.luminn.firebase.repository.UserRepository;
import com.luminn.firebase.service.MessageByLocaleService;
import com.luminn.firebase.service.UserMongoService;
import com.luminn.firebase.util.ErrorNumber;
import com.luminn.firebase.util.Path;
import com.luminn.view.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by ch on 2/6/2016.
 */
@RestController
@RequestMapping("/reset")
public class ResetPasswordController {

    @Value("${siteUrl}")
    private String siteUrl;
    @Value("${sendingEmail}")
    private String sendingEmail;
    @Value("${emailTemplateFolder}")
    private String emailTemplateFolder;
    @Value("${email_verify}")
    private String emailTemplate;

    @Value("${email_verify}")
    private String emailTemplateResetPassword;

    @Autowired
    private UserModel userModel;

    @Autowired
    private UserMongoService userService;

    @Autowired
    private MessageByLocaleService messageService;

    @Autowired
    EmailModel emailModel;


    @Autowired
    UserRepository userRepository;

    @ResponseBody
    @RequestMapping(value = "/app/user/v1/forgotpassword"+"/{email}", method = RequestMethod.GET,produces = "application/json" )
    public ResponseEntity<StatusResponse> forgotPassword(@PathVariable("email") String email) throws IOException {

        StatusResponse sr = new StatusResponse();
        if (email != null) {
            try {
                //User user = userModel.getById(email);
               User user =  userRepository.findByEmail(email);
                if (user != null && user.getPassword() != null) {

                    String resetKey = UUID.randomUUID().toString()+"-"+UUID.randomUUID().toString();
                    String verifyUrl = siteUrl+"/public/changePassword.html?email="+user.getEmail()+"&resetKey="+user.getPasswordResetKey();
                    //user.setPasswordResetKey(resetKey);
                    //userService.update(User.class, user.getId(), user);
                    //emailModel.selectAndSendEmail(user.getFirstName(),user.getEmail(), verifyUrl,messageService.getMessage("email_reset"), emailTemplateResetPassword);

                    emailModel.selectAndSendEmailwithForgot(user.getFirstName(),user.getEmail(),messageService.getMessage("email_reset"), emailTemplateResetPassword,user.getEmail(),user.getPasswordResetKey());
                    sr.setMessage(messageService.getMessage("password_rest"));
                    sr.setStatus(true);
                    return new ResponseEntity<>(sr, HttpStatus.OK);

                } else {
                    sr.setMessage(messageService.getMessage("email_not_existing",email));
                    return new ResponseEntity<>(sr, HttpStatus.ALREADY_REPORTED);
                }
            } catch (Exception dataAccessException) {
                sr.setMessage(messageService.getMessage("email_not_existing",email));

                return new ResponseEntity<>(sr, HttpStatus.ALREADY_REPORTED);
            }
        } else {
            sr.setMessage(messageService.getMessage("all_fields"));
            return new ResponseEntity<>(sr, HttpStatus.ALREADY_REPORTED);
        }
    }

   /* @ResponseBody
    @RequestMapping(value = "/app/user/v1/forgotpassword"+"/{email}/{role}/{domain}", method = RequestMethod.GET,produces = "application/json" )
    public ResponseEntity<StatusResponse> forgotPasswordRole(@PathVariable("email") String email, @PathVariable("role") String role, @PathVariable("domain") String domain) throws IOException, DatabaseException {

        StatusResponse sr = new StatusResponse();
        String checkDomain = "";
        if (email != null) {
            try {
                if(domain == null || "".equalsIgnoreCase(domain))
                    checkDomain  = Path.DOMAIN.MYDOMAIN;
                else
                    checkDomain = domain;

                User user = userModel.checkGetByEmail(email,role,checkDomain);
                if (user != null && user.getPassword() != null) {

                    String resetKey = UUID.randomUUID().toString()+"-"+UUID.randomUUID().toString();
                    String verifyUrl = siteUrl+"/public/changePassword.html?email="+user.getEmail()+"&resetKey="+user.getPasswordResetKey();
                    //user.setPasswordResetKey(resetKey);
                    //userService.update(User.class, user.getId(), user);
                    //emailModel.selectAndSendEmail(user.getFirstName(),user.getEmail(), verifyUrl,messageService.getMessage("email_reset"), emailTemplateResetPassword);

                    emailModel.selectAndSendEmailwithForgot(user.getFirstName(),user.getEmail(),messageService.getMessage("email_reset"), emailTemplateResetPassword,user.getEmail(),user.getPasswordResetKey());
                    sr.setMessage(messageService.getMessage("password_rest"));
                    sr.setStatus(true);
                    return new ResponseEntity<>(sr, HttpStatus.OK);

                } else {
                    sr.setMessage(messageService.getMessage("email_not_existing",email));
                    return new ResponseEntity<>(sr, HttpStatus.ALREADY_REPORTED);
                }
            } catch (Exception dataAccessException) {
                sr.setMessage(messageService.getMessage("email_not_existing",email));

                return new ResponseEntity<>(sr, HttpStatus.ALREADY_REPORTED);
            }
        } else {
            sr.setMessage(messageService.getMessage("all_fields"));
            return new ResponseEntity<>(sr, HttpStatus.ALREADY_REPORTED);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/app/user/v1/forgotpassword/retrieve"+"/{email}", method = RequestMethod.GET,produces = "application/json" )
    public ResponseEntity<StatusResponse> forgotPasswordUser(@PathVariable("email") String email) throws IOException, DatabaseException {

        StatusResponse sr = new StatusResponse();
        if (email != null) {
            try {
                User user = userModel.getById(email);
                if (user != null && user.getPassword() != null) {

                    //String resetKey = UUID.randomUUID().toString()+"-"+UUID.randomUUID().toString();
                    String verifyUrl = siteUrl+"/public/changePassword.html?email="+user.getEmail()+"&resetKey="+user.getPasswordResetKey();
                    //user.setPasswordResetKey(resetKey);
                    //userService.update(User.class, user.getId(), user);
                    //emailModel.selectAndSendEmail(user.getFirstName(),user.getEmail(), verifyUrl,messageService.getMessage("email_reset"), emailTemplateResetPassword);

                    emailModel.selectAndSendEmailwithForgot(user.getFirstName(),user.getEmail(),messageService.getMessage("email_reset"), emailTemplateResetPassword,user.getEmail(),user.getPasswordResetKey());
                    sr.setMessage(messageService.getMessage("password_rest"));
                    sr.setStatus(true);
                    sr.setInfoId(ErrorNumber.FORGOTPASSWORD_CORRECT_RETRIVEL);
                    return new ResponseEntity<>(sr, HttpStatus.OK);

                } else {
                    sr.setMessage(messageService.getMessage("email_not_existing",email));
                    sr.setInfoId(ErrorNumber.FORGOTPASSWORD_RETRIVEL);
                    return new ResponseEntity<>(sr, HttpStatus.ALREADY_REPORTED);

                }
            } catch (Exception dataAccessException) {
                sr.setMessage(messageService.getMessage("email_not_existing",email));
                sr.setInfoId(ErrorNumber.FORGOTPASSWORD_RETRIVEL);
                return new ResponseEntity<>(sr, HttpStatus.ALREADY_REPORTED);
            }
        } else {
            sr.setMessage(messageService.getMessage("all_fields"));
            sr.setInfoId(ErrorNumber.FORGOTPASSWORD_RETRIVEL);
            return new ResponseEntity<>(sr, HttpStatus.ALREADY_REPORTED);
        }
    }*/


    @ResponseBody
    @RequestMapping(value = "/app/user/v1/domain/forgotpassword", method = RequestMethod.GET,produces = "application/json" )
    public ResponseEntity<StatusResponse> forgotPasswords(@RequestParam("email") String email, @RequestParam("role") String role, @RequestParam("domain") String domain) throws IOException, DatabaseException {

        StatusResponse sr = new StatusResponse();
        if (email != null) {
            try {
                User user = userModel.checkGetByEmail(email,role,domain);

                if (user != null && user.getPassword() != null) {

                    String resetKey = UUID.randomUUID().toString()+"-"+UUID.randomUUID().toString();

                    String verifyUrl = siteUrl+"/public/changePassword.html?email="+user.getEmail()+"&resetKey="+resetKey;

                    System.out.println(" URL " + verifyUrl);
                    userService.updateResetKeyId(user.getId(),resetKey);
                    //user.setPasswordResetKey(resetKey);
                    //userService.update(User.class, user.getId(), user);
                    String name = null;
                    if(user.getFirstName() != null)
                        name = user.getFirstName();
                        else
                        name = "default name";
                    emailModel.selectAndSendEmail(name,user.getEmail(), verifyUrl,messageService.getMessage("email_reset"), emailTemplateResetPassword);
                    //emailModel.selectAndSendEmailwithForgot(user.getFirstName(),user.getEmail(),messageService.getMessage("email_reset"), emailTemplateResetPassword,user.getEmail(),user.getPasswordResetKey());
                    sr.setMessage(messageService.getMessage("password_rest"));
                    sr.setStatus(true);
                    return new ResponseEntity<>(sr, HttpStatus.OK);

                } else {
                    sr.setMessage(messageService.getMessage("email_not_existing",email));
                    return new ResponseEntity<>(sr, HttpStatus.ALREADY_REPORTED);
                }
            } catch (Exception dataAccessException) {
                sr.setMessage(messageService.getMessage("email_not_existing",email));

                return new ResponseEntity<>(sr, HttpStatus.ALREADY_REPORTED);
            }
        } else {
            sr.setMessage(messageService.getMessage("all_fields"));
            return new ResponseEntity<>(sr, HttpStatus.ALREADY_REPORTED);
        }
    }


  /*  @RequestMapping(value = "/app/v1/resetpassword", method = RequestMethod.GET)
    public String  resetPasswordPagereDirect(@RequestParam("email") String email, @RequestParam("resetKey") String resetKey){

        StatusResponse sr = new StatusResponse();
        ModelMap map = new ModelMap();
        if (email != null && resetKey != null) {
            try {
                User user = userService.getByEmail(email);
                if (user != null && user.getPassword() != null) {
                    if(resetKey.equals(user.getPasswordResetKey())){
                        sr.setMessage(messageService.getMessage("password_reset_link"));
                        map.addAttribute("email",email);
                        map.addAttribute("resetKey",resetKey);
                        return "redirect:/greetingtest2";
                    } else {
                        sr.setMessage("Error :");
                        sr.setStatus(false);
                        sr.setMessage(messageService.getMessage("wrong_resetcode"));
                        map.put("email",email);
                        map.put("resetKey",resetKey);

                        return "redirect:/greetingtest2";
                    }
                } else {
                    sr.setMessage(messageService.getMessage("email_not_existing",email));
                    return "redirect:/greetingtest2";
                }
            } catch (Exception dataAccessException) {
                sr.setMessage("Error :");
                sr.setMessage(messageService.getMessage("email_not_existing",email));
                return "redirect:/greetingtest2";
            }
        } else {
            sr.setMessage(messageService.getMessage("invalid_password"));
            return "redirect:/greetingtest2";
        }
    }*/

    @ResponseBody
    @RequestMapping(value = Path.Url.USER_ACTIVATION + Path.Url.SIGNUP + "/verify", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<StatusResponse> verifyUserKey(@RequestParam("id") String id, @RequestParam("resetKey") String resetKey) throws IOException  {
        User isUserExisting = null;
        String userId =  id;

         if(userId != null)
             isUserExisting = userService.findId(userId);
                //User isExistinSupplier = userService.getByKey(User.class, userKey);
        StatusResponse sr = new StatusResponse();
        //CK
        /*if(isUserExisting != null && !isUserExisting.getActive()){
            sr.setStatus(true);
            isUserExisting.setActive(true);
            userService.update(User.class, isUserExisting.getId(), isUserExisting);

            sr.setMessage(messageService.getMessage("thanks_registration",isUserExisting.getEmail()));
            return new ResponseEntity<>(sr, HttpStatus.OK);

        }*/
        {
            sr.setStatus(false);
            sr.setMessage(messageService.getMessage("user_is_existing",isUserExisting.getFirstName()));
            sr.setData( isUserExisting.getFirstName() + ""  + isUserExisting.getLastName());
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }

    }


    @RequestMapping(value="/rest/ct", method = RequestMethod.POST)
    public ResponseEntity<StatusResponse> homeCT(@RequestParam String action, @RequestParam String token, @RequestParam String email, @RequestParam String passWord, RedirectAttributes redir) {
        System.out.println("--333 home 333--" + action + "action --" + token + "email--" + email);

        StatusResponse sr = new StatusResponse();
        sr.setStatus(false);
        ModelStatus modelStatus = ModelStatus.NOTEXISTS;
        UserDTO userDTO = null;

        if (email != null && token != null) {
            try {
                //User user = userService.findByEmail(email);
                User user = userRepository.findByEmail(email);
                if (user != null && user.getPassword() != null) {

                    if(token.equals(user.getPasswordResetKey())){
                        userDTO = new UserDTO();
                        userDTO.setRestKey(token);
                        userDTO.setEmail(email);
                        userDTO.setPassword(passWord);
                        modelStatus =  userModel.changePassWord(userDTO,true);
                        if(modelStatus == ModelStatus.UPDATED) {

                            //next time
                            //userRepository.save(user);

                            sr.setStatus(true);
                            sr.setMessage(messageService.getMessage("password_updated", email));
                            return new ResponseEntity<>(sr, HttpStatus.OK);
                        }
                        else {
                            sr.setMessage(messageService.getMessage("wrong_token", email));
                            return new ResponseEntity<>(sr, HttpStatus.OK);
                        }
                        // model.setViewName("success");
                        // return model;
                    } else {
                        sr.setMessage(messageService.getMessage("wrong_token", email));
                        return new ResponseEntity<>(sr, HttpStatus.OK);
                    }
                } else {
                    sr.setMessage(messageService.getMessage("email_or_token",email));
                    return new ResponseEntity<>(sr, HttpStatus.OK);
                    //return model;Poin
                }
            } catch (Exception dataAccessException) {
                System.out.println("--ERROR--");
                sr.setMessage("Error :");
                sr.setMessage(messageService.getMessage("email_not_existing",email));
                return new ResponseEntity<>(sr, HttpStatus.OK);
                //return model;
            }
        } else {
            System.out.println("--ERROR 44--");
            sr.setMessage(messageService.getMessage("invalid_password"));
            return new ResponseEntity<>(sr, HttpStatus.OK);
            //return model;
        }
       /* if (StringUtils.hasText(action)) {
            return "Hellocc " + action;
        } else {
            return "Hello Worldeee";
        }*/
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.USER + Path.Url.CHANGED_PASSWORD,method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity<StatusResponse> changePassword(@RequestBody UserDTO dto) throws IOException{

        ModelStatus modelStatus = ModelStatus.NOTEXISTS;
        StatusResponse sr = new StatusResponse();

        if(dto.getEmail() != null && dto.getRestKey() != null)
            modelStatus =  userModel.changePassWord(dto,false);

        if(modelStatus.UPDATED.equals(ModelStatus.UPDATED)) {

            String verifyUrl = siteUrl;
            User user = userModel.getByEmailId(dto.getEmail());
            //String verifyUrl = siteUrl+"resetpassword?email="+user.getEmail()+"&resetKey="+resetKey;
            emailModel.selectAndSendEmail(user.getFirstName(),user.getEmail(), verifyUrl, "Verify login details with Taxi App", emailTemplate);
            sr.setData("success updated " + modelStatus.name());
        }
        else
            sr.setData("Already EXISTS " + modelStatus.name());

        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.USER + Path.Url.VERSION_V1 +  Path.Url.CHANGED_PASSWORD_BYUSER,method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity<StatusResponse> changePasswordByUser(@RequestBody UserDTO dto) throws IOException{

        ModelStatus modelStatus = ModelStatus.NOTEXISTS;
        StatusResponse sr = new StatusResponse();
        User user = null;
        UserDTO userDTO = null;

        //Part 1 validation
        try {
            if(dto.getId() != null)
                user = userModel.validate(dto.getId());
            else
                sr.setMessage(messageService.getMessage("user_wrong"));
        }
        catch(DatabaseException e){
             sr.setMessage(messageService.getMessage("user_wrong"));
        }

        //Part 2 validation
        if(!"".equals(dto.getEmail()) || !"".equals(dto.getPhoneNumber())){


            user = userModel.getByEmailId(dto.getEmail());
            String verifyUrl = siteUrl;

            if(user == null ){
                user = userModel.getByPhoneNumberId(dto.getPhoneNumber());
            }
            if(user != null) {
                modelStatus = userModel.changePassWord(dto, true);
                if(modelStatus.UPDATED.equals(ModelStatus.UPDATED)) {
                    sr.setMessage(messageService.getMessage("password_updated"));
                }
            }
            //String verifyUrl = siteUrl+"resetpassword?email="+user.getEmail()+"&resetKey="+resetKey;
            sr.setStatus(true);
            emailModel.selectAndSendEmail(user.getFirstName(),user.getEmail(), verifyUrl, "Verify login details with Taxi App", emailTemplate);
            sr.setData("success updated " + modelStatus.name());
        }
        else
            sr.setMessage(messageService.getMessage("user_wrong"));
            sr.setData("Already EXISTS " + modelStatus.name());

        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    public static boolean isEmpty(Long id){
        if(id == null)
            return false;
        if(id.longValue() == 0)
            return false;
        return true;

    }



}
