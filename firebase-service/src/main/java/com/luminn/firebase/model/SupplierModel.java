package com.luminn.firebase.model;


import com.google.firebase.database.DatabaseException;
import com.luminn.firebase.dto.MobileDTO;
import com.luminn.firebase.dto.UserDTO;
import com.luminn.firebase.dto.UserDetailsDTO;
import com.luminn.firebase.entity.Supplier;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.helper.Converter;
import com.luminn.firebase.repository.SupplierRepository;
import com.luminn.firebase.repository.UserRepository;
import com.luminn.firebase.service.MessageByLocaleService;
import com.luminn.firebase.service.SupplierService;
import com.luminn.firebase.service.UserMongoService;
import com.luminn.firebase.util.CARTYPE;
import com.luminn.firebase.util.Path;
import com.luminn.firebase.util.Role;
import com.luminn.view.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SupplierModel {

    @Value("${sendingEmail}")
    private String sendingEmail;
    @Value("${emailTemplateFolder}")
    private String emailTemplateFolder;
    @Value("${email_verify}")
    private String emailTemplate;
    @Value("${email_verify_driver}")
    private String emailTemplateDriver;

    @Value("${email_verify}")
    private String emailTemplateResetPassword;

    @Autowired
    SupplierService supplierService;

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    UserModel userModel;

    @Autowired
    UserMongoService  userMongoService;

    @Autowired
    TaxiModel taxiModel;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailModel emailModel;

    @Autowired
    private MessageByLocaleService messageservice;

    UserStatus emailStatus = null;
    UserStatus phoneStatus = null;

    public List<Supplier> getSupplierDomain(String domain) {

        List<Supplier> domains = supplierRepository.findByName(domain);
        if(domains != null && domains.size() >0)
        return domains;
        else
            return null;
    }

    public String createSupplier(MobileDTO userDTO) {

        Supplier suppliers = new Supplier();

        suppliers.setName(userDTO.getDomain());
        suppliers.setLicenseNumber(userDTO.getFirstName());

        Supplier m = supplierRepository.save(suppliers);
        System.out.println("createSupplier  " + m + " m.getIds() --->" + m.getId());
        return m.getId();


    }


    public Supplier getSupplierId(String id) {

        if (id != null) {
            Supplier supplier = supplierService.findById(id);
            if (supplier != null) {
                return supplier;
            }
        }
        return null;
    }

    public String getDomain(String name){

        List<Supplier> supplier = supplierRepository.findByName(name);
        for(Supplier sup :supplier){
            return sup.getId();
        }
        return null;
    }

    public String  createSupplierBefore(MobileDTO dto,String tempSupplierIds){

        List<Supplier> suppliers = supplierRepository.findByName(dto.getDomain());
        if (tempSupplierIds == null  || suppliers.size() == 0 )
            tempSupplierIds = createSupplier(dto);
        else if (tempSupplierIds != null && suppliers != null) {
            for(Supplier sup : suppliers) {
                tempSupplierIds = sup.getId();
            }
        }
        return tempSupplierIds;
    }

    private String updateSupplierWithUserId(String userKeys,String tempSupplierIds){

        supplierService.update(userKeys,tempSupplierIds);

        /*Optional<Supplier> supplier2 = supplierRepository.findById(tempSupplierIds);
        if (supplier2.isPresent()) {
            Supplier supp =  supplier2.get();
            //ObjectId objectId = new ObjectId(userKeys);
            supp.setUserIds(userKeys);
            supplierRepository.save(supp);
        }*/
        return  tempSupplierIds;
    }


    public ResponseStatus createUserDriver(UserDetailsDTO dto, boolean onlyDriver, boolean isEmail) throws Exception  {

        ModelStatus taxiStatus = null;
        String verifyUrl = null;
        String userTemplate = null;
        Long tempSupplierId = null;
        String tempSupplierIds = null;
        Long taxiDetailId = null;
        Long taxiId = null;
        Long driverId = null;
        String userKeys = null;

        Supplier supplier = null;
        StatusResponse response = new StatusResponse();
        ResponseStatus status = new ResponseStatus();
        //VERY IMPORTANT PART

        ModelStatus output = userValidation(dto,onlyDriver);
        if (!output.name().equalsIgnoreCase(ModelStatus.SUCCESS.name())) {


            //return output;
            status.setStatus(output);
            return status;
        } else {

            //Do validation either web or mobile - USe existing ID
            if (!onlyDriver) {
                if (!Validation.isEmpty(dto.getId())) {
                    if (dto.getDomain() == null || "".equalsIgnoreCase(dto.getDomain())) {
                        //return ModelStatus.NO_SUPPLIER_ID_WHEN_ADD_TAXI;
                        status.setStatus(ModelStatus.NO_SUPPLIER_ID_WHEN_ADD_TAXI);
                        return status;
                    }
                }

                ///////SUPLLIER PART
                System.out.println("Before Domain " + dto.getDomain());
                tempSupplierIds = getDomain(dto.getDomain());
                ///////////////////////////////////////////

                //PART - create a new suplier id
                //if ((tempSupplierIds == null || supplier.getIds() == null) &&
                if (((dto.role.equalsIgnoreCase(Path.ROLE.SUPPLIER)) && tempSupplierIds == null
                         && dto.getDomain().length() > 0)) {
                      tempSupplierIds = createSupplierBefore(dto,tempSupplierIds);
                    System.out.println("ROLE SUPPLIER tempSupplierId -->" + tempSupplierId + " DOMAIN " + dto.getDomain());
                }
                //take id
                else if (tempSupplierIds != null) {
                     tempSupplierIds = tempSupplierIds;
                }
                else {
                    //return ModelStatus.NO_SUPPLIER_ID_WHEN_ADD_TAXI;
                    status.setStatus(ModelStatus.NO_SUPPLIER_ID_WHEN_ADD_TAXI);
                    return status;
                }
            }


            //CREATE USER OR DRIVER OR SUPPLIER ////////////////////////////////
            userKeys = userMongoService.addUserOneRegistration(dto,false,null);

            //Check DRIVER Or SUPPLIER - CREATED DIRECTLY FROM MOBILE (NOT WEB)
            //NO need create a supplier here, both web and Mobile

            ///ADDING USER AND SUPPLIER PART
            if (userKeys != null && userKeys != null
                    && dto.getRole().equalsIgnoreCase(Path.ROLE.SUPPLIER)){
                if(tempSupplierIds != null){
                    updateSupplierWithUserId(userKeys,tempSupplierIds);
                }
                //CREATE A NEW SUPPLIER
                else {
                    tempSupplierIds = createSupplierBefore(dto,tempSupplierIds);
                    if(tempSupplierIds != null){
                        updateSupplierWithUserId(userKeys,tempSupplierIds);
                    }
                }
                onlyDriver = false;
            }


            if (((dto.getRole().equalsIgnoreCase(Role.ROLE_USER)))  &&
                    (Validation.isEmpty(userKeys)) ) {
                //log.info("ROLE_USER -->" + dto.getRole());
                isEmail = true;
            }

           else if ((dto.getRole().equalsIgnoreCase(Role.ROLE_DRIVER) ) &&
                    (Validation.isEmpty(userKeys) && Validation.isEmpty(tempSupplierIds)) && !onlyDriver || onlyDriver ) {

                System.out.println("outside tempSupplierId 1-->" + tempSupplierId);
                if(tempSupplierIds != null) {
                    taxiStatus = taxiModel.createTaxiWithDriver(dto, tempSupplierIds, dto.getFirstName(), userKeys);
                    //return taxiStatus;
                    isEmail = true;
                }
                System.out.println("driverId -->" + driverId + " taxiStatus " + taxiStatus);
            }
        }

        if(isEmail)
            return getEmailResponse(userKeys,dto,userTemplate,verifyUrl,taxiStatus);
        else
            return  checkRole(userKeys,dto.getRole());
        }

    //ResponseStatus

    private ResponseStatus getEmailResponse(String userKeys,MobileDTO dto,String userTemplate,String verifyUrl,ModelStatus taxiStatus) throws Exception {
        User user = null;

        if (userKeys != null) {
            user = userMongoService.findId(userKeys);


            if (dto.getRole().equalsIgnoreCase(Role.ROLE_USER) || dto.getRole().equalsIgnoreCase(Role.ROLE_SUPPLIER)
                    || dto.getRole().equalsIgnoreCase(Role.ROLE_USER_IT))
                userTemplate = emailTemplate;
            if (dto.getRole().equalsIgnoreCase(Role.ROLE_DRIVER) ||
                    dto.getRole().equalsIgnoreCase(Role.ROLE_DELIVERY) ||
                    dto.getRole().equalsIgnoreCase(Role.ROLE_ADMIN))
                userTemplate = emailTemplateDriver;
            if ("".equals(dto.getFirstName())) {
                String email = dto.getEmail();
                String state = email.substring(email.indexOf("@"));
                dto.setFirstName(state);
            }
            if (userTemplate != null) {
                try {
                    if(user != null && user.getPasswordResetKey() != null)
                        emailModel.selectAndSendEmail(dto.getFirstName(), dto.getEmail(), messageservice.getMessage("verify"), userTemplate, dto.getRole(), messageservice.getMessage("code"), user.getPasswordResetKey(), userKeys);
                    else
                        emailModel.selectAndSendEmail(dto.getFirstName(), dto.getEmail(), messageservice.getMessage("verify"), userTemplate, dto.getRole(), messageservice.getMessage("code"), "dummy", userKeys);
                }
                catch(Exception e){
                     System.out.println(" Exception in email sending -->" + e);
                }
                if(dto != null && dto.getRole() != null)
                    return checkRole(userKeys,dto.getRole());
                else
                    return checkRole(userKeys,Role.ROLE_DRIVER);
            }
            else {
                System.out.println("NO ROLE IS DEFINED");
                //systen.out.println("NO ROLE IS DEFINED");
                return checkRole(userKeys,dto.getRole());
            }
        }
        return checkRole(userKeys,dto.getRole());
    }

   /* private ModelStatus checkRole(String dto){
        if (dto.equalsIgnoreCase(Path.ROLE.DRIVER)) {
            return ModelStatus.DRIVER_CREATED;
        }  if (dto.equalsIgnoreCase(Path.ROLE.SUPPLIER)) {
            return ModelStatus.SUPPLIER_CREATED;
        }   if (dto.equalsIgnoreCase(Path.ROLE.USER)) {
            return ModelStatus.USER_CREATED;
        }
        if (dto.equalsIgnoreCase(Path.ROLE.USER_IT)) {
            return ModelStatus.USER_CREATED;
        }
        return ModelStatus.USER_CREATED;
    }*/

    private ResponseStatus checkRole(String id,String dto){
        ResponseStatus status = new ResponseStatus();
        status.setId(id);

        if (dto.equalsIgnoreCase(Path.ROLE.DRIVER)) {
             status.setStatus(ModelStatus.DRIVER_CREATED);
            status.setId(id);
            //return ModelStatus.DRIVER_CREATED;
            return status;
        }  if (dto.equalsIgnoreCase(Path.ROLE.SUPPLIER)) {
            status.setStatus(ModelStatus.SUPPLIER_CREATED);
            status.setId(id);
            return status;
        }   if (dto.equalsIgnoreCase(Path.ROLE.USER)) {
            status.setStatus(ModelStatus.USER_CREATED);
            status.setId(id);
            return status;
        }
      return status;

    }

    public ModelStatus userValidation(MobileDTO dto,boolean onlyDriver){

        ModelStatus status = null;

       /* if (!"".equalsIgnoreCase(dto.getCode()) && dto.getCode() != null)
            status = cityModel.create(dto.getCode());

        if("".equalsIgnoreCase(dto.getWebsite()) || (dto.getWebsite().equalsIgnoreCase("string")) || dto.getWebsite() == null){
            dto.setWebsite(Path.DOMAIN.MYDOMAIN);
        }*/

        if (!"".equals(dto.getEmail()) && !dto.getEmail().equalsIgnoreCase("string")) {
            emailStatus = userModel.getByEmail(dto.getEmail(), dto.getRole(), dto.getDomain());
            if (emailStatus != null && emailStatus.equals(UserStatus.EXISTS))
                return ModelStatus.EMAIL_IS_EXISTING;
        }
        if (!"".equals(dto.getPhoneNumber())) {
            User user = userModel.checkGetByPhoneNumber(dto.getPhoneNumber(), dto.getRole(), dto.getWebsite());
            if (user != null && user.getId() != null && user.getId() != null)
                return ModelStatus.PHONENUMBER_IS_EXISTING;
        }

        //HIDE CK
        /*if (status.name().equals(ModelStatus.EXCEPTION)) {
            return ModelStatus.CITY_WRONG_CODE;
            // else if(!User.getRole().equals(Path.ROLE.USER))
            //return ModelStatus.CITY_WRONG_CODE;
        } */

        else if (emailStatus != null && emailStatus.equals(UserStatus.EXISTS)) {
            return ModelStatus.EMAIL_IS_EXISTING;
        } else if (phoneStatus != null && phoneStatus.equals(UserStatus.EXISTS))
            return ModelStatus.PHONENUMBER_IS_EXISTING;
        else if (dto.getRole() != null && !dto.getRole().startsWith("ROLE"))
            return ModelStatus.ROLE_NOT_EXISTING;
        else if (dto.getRole() != null && dto.getRole().startsWith(Path.ROLE.DRIVER) && CARTYPE.forValue(dto.getCarType()) == null) {

            if (CARTYPE.forValue(dto.getCarType()) == null) {
                return ModelStatus.CAR_TYPE_NOT_EXISTING;
            }
            return ModelStatus.CAR_TYPE_NOT_EXISTING;
        }
        return ModelStatus.SUCCESS;
    }

    public ModelStatus updateUser(UserDTO userDTO) {
        try {
            //user = Converter.dtoToEntity(userDTO, user);
            //userRepository.save(user);
            userMongoService.updateSetting(userDTO);
        } catch (DatabaseException b) {
            //TODO fix this error to include userid. This goes for other places in the exception code too.
            //log.error(this.getClass().getName() + ".updateUser(): " + b.getMessage());
            return ModelStatus.USER_IS_NOT_EXISTING;
        }
        return ModelStatus.UPDATED;
    }

    String name;
    public String getUserName(){
        return name;
    }
    public void setUserName(String name){
        this.name = name;
    }

}