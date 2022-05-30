package com.luminn.firebase.controller;

import com.google.firebase.database.DatabaseException;
import com.luminn.firebase.dto.TaxiUpdateDTO;
import com.luminn.firebase.dto.VehicleTypeDTO;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.entity.VehicleType;
import com.luminn.firebase.model.ModelStatus;
import com.luminn.firebase.model.TaxiModel;
import com.luminn.firebase.model.UserModel;
import com.luminn.firebase.model.VehicleTypeModel;
import com.luminn.firebase.repository.VehicleTypeRepository;
import com.luminn.firebase.service.MessageByLocaleService;
import com.luminn.firebase.service.VehicleTypeService;
import com.luminn.firebase.util.ErrorNumber;
import com.luminn.firebase.util.Path;
import com.luminn.view.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by ch on 2/10/2016.
 */
@Controller
@RequestMapping("/vehicleType")
public class VehicleTypeController {

    @Autowired
    VehicleTypeModel vehicleTypeModel;

    @Autowired
    VehicleTypeService vehicleTypeService;

    @Autowired
    TaxiModel taxiModel;

    @Autowired
    VehicleTypeRepository vehicleTypeRepository;


    @Autowired
    UserModel userModel;
    @Autowired
    private MessageByLocaleService messageservice;



    @RequestMapping(value = "/app/v1/create",    method = RequestMethod.POST,   produces = "application/json")
    @ResponseBody
    public ResponseEntity<StatusResponse> create(@RequestBody VehicleTypeDTO dto){
        ModelStatus status =  vehicleTypeModel.create(dto);
        StatusResponse sr = new StatusResponse();
        if(status.equals(ModelStatus.CREATED)) {
            sr.setStatus(true);
            sr.setMessage(messageservice.getMessage("vehicleType_created", vehicleTypeModel.vehicleTypeId));
            //sr.setData(vehicleTypeId);
        }
        else if(status.equals(ModelStatus.EXISTS)){
            sr.setStatus(false);
            sr.setMessage(messageservice.getMessage("vehicleType_is_existing",dto.getCode()));
        }

        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

   @RequestMapping(value = "/app/v1/get",    method = RequestMethod.GET,   produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<VehicleType>> get(){

        List<VehicleType> ls =  vehicleTypeModel.listAllVehicle();
        return new ResponseEntity<List<VehicleType>>(ls, HttpStatus.OK);
    }

    @RequestMapping(value = "/app/v1/getCode"+"{name}",    method = RequestMethod.GET,   produces = "application/json")
    @ResponseBody
    public ResponseEntity<StatusResponse> getCode(@PathVariable("name") String  name){
        StatusResponse sr = new StatusResponse();
        VehicleType type =  vehicleTypeModel.getDefaultCode(name);
        sr.setStatus(true);
        sr.setMessage(Path.MESSAGE.SUCCESS);
        sr.setData(type);
        return new ResponseEntity<>(sr, HttpStatus.OK);

        //return new ResponseEntity<List<VehicleType>>(ls, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = Path.Url.APP +  Path.OperationUrl.GETALL,    method = RequestMethod.GET,   produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<VehicleTypeDTO>> listByLanguage(){
        List<VehicleTypeDTO> ls =  vehicleTypeModel.listAllVehicle(messageservice.getMessage("code"));
        return new ResponseEntity<List<VehicleTypeDTO>>(ls, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP  + Path.Url.VERSION_V1 + Path.Url.DETAILVIEW+ "/type" +"/{taxiId}/{taxiName}", method = RequestMethod.GET,produces = { "application/json" })
    public ResponseEntity<StatusResponse> taxiViewDetailsPage(@PathVariable("taxiId") Long taxiId, @PathVariable("taxiName") String taxiName) throws DatabaseException {
        StatusResponse sr = new StatusResponse();
        //String taxiView = vehicleTypeModel.getVehicleBrand(taxiId);
        sr.setStatus(true);
        //sr.setData(taxiView);
        sr.setMessage(Path.MESSAGE.SUCCESS);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.VEHICLETYPE + Path.OperationUrl.DELETE+"/{vehicleTypeId}"+"/{adminId}", method = RequestMethod.POST, produces = { "application/json", "application/xml" })
    public ResponseEntity<StatusResponse> delete(@PathVariable("vehicleTypeId") String vehicleTypeId, @PathVariable("adminId") Long adminId) throws DatabaseException {

        //checking in DB
        StatusResponse sr = new StatusResponse();
        //UserDTO user = userModel.getDTO(adminId);
        if(vehicleTypeId != null){
            Optional<VehicleType> isExisting = vehicleTypeRepository.findById(vehicleTypeId);
            if (isExisting.isPresent()) {

                vehicleTypeRepository.delete(isExisting.get());
                sr.setData("Vehicletype deleted successfully " + ModelStatus.DELETED);
                return new ResponseEntity<>(sr, HttpStatus.OK);

            } else {
                sr.setData("Vehicletype doesnt exit " + ModelStatus.NOTEXISTS);
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }

        }
        else
            sr.setData("NO PERMISSION TO DELETE " + ModelStatus.NO_ADMIN_ACCESS);
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = Path.Url.APP + Path.Url.TAXI +  Path.Url.VERSION_V1 + "/type" +Path.OperationUrl.UPDATEINFO, method = RequestMethod.POST, produces = {"application/json" })
    public ResponseEntity<StatusResponse> updateTaxiInfoType(@RequestBody TaxiUpdateDTO dto)  {

        User user = null;
        ModelStatus status = null;
        StatusResponse sr = new StatusResponse();

        status = taxiModel.validationTaxiDetailbyIdSupplierID(dto);



        if(status.equals(ModelStatus.NOTEXISTS)){
            sr.setStatus(false);
            sr.setMessage(messageservice.getMessage("driver_id_not_exiting", dto.getTaxiId()));
            sr.setInfoId(ErrorNumber.DRIVER_ID_NOT_EXISTING);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
        else if(status.equals(ModelStatus.NO_SUPPLIER_EXISTING)) {
            sr.setStatus(false);
            sr.setMessage(messageservice.getMessage("supplier_invalid",dto.getSupplierId()));
            sr.setInfoId(ErrorNumber.NO_SUPPLIER_EXISTING);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
        else if(status.equals(ModelStatus.NO_TAXIDETAILS_ID)) {
            sr.setStatus(false);
            sr.setInfoId(ErrorNumber.NO_TAXIDETAILS_ID);
            sr.setMessage(messageservice.getMessage("taxi_id_not_valid",dto.getTaxiId()));
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
        else if(status.equals(ModelStatus.CAR_TYPE_NOT_EXISTING)) {
            sr.setStatus(false);
            sr.setInfoId(ErrorNumber.NO_TAXIDETAILS_ID);
            sr.setMessage(messageservice.getMessage("tag_code_is_not_existing",dto.getTaxiId()));
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }
        else if(status.equals(ModelStatus.NO_DRIVER_ROLE)) {
            sr.setStatus(false);
            sr.setMessage(messageservice.getMessage("wrong_role",dto.getTaxiId()));
            sr.setInfoId(ErrorNumber.NO_SUPPLIER_NOT_MATCHING);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        }

         if(dto.getId() != null && dto.getUserId() != null) {
            ModelStatus updateStatus = taxiModel.updateTaxiType(dto);
            if(updateStatus.equals(ModelStatus.UPDATED)){
                sr.setStatus(true);
                sr.setMessage(messageservice.getMessage("taxi_details_updated", dto.getTaxiId()));
                sr.setInfoId(ErrorNumber.UPDATE_TAXI_DETAIL_SUCCESS);
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            else if(updateStatus.equals(ModelStatus.NO_SUPPLIER_EXISTING)) {
                sr.setStatus(false);
                sr.setMessage(messageservice.getMessage("tag_code_is_not_existing",dto.getTaxiId()));
                sr.setInfoId(ErrorNumber.NO_SUPPLIER_EXISTING);
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            else if(updateStatus.equals(ModelStatus.NO_TAXIDETAILS_ID)) {
                sr.setStatus(false);
                sr.setMessage(messageservice.getMessage("tag_code_is_not_existing",dto.getTaxiId()));
                sr.setInfoId(ErrorNumber.NO_TAXIDETAILS_ID);
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            else if(updateStatus.equals(ModelStatus.NO_SUPPLIER_NOT_MATCHING)) {
                sr.setStatus(false);
                sr.setMessage(messageservice.getMessage("tag_code_is_not_existing",dto.getTaxiId()));
                sr.setInfoId(ErrorNumber.NO_SUPPLIER_NOT_MATCHING);
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }
            else if(updateStatus.equals(ModelStatus.NO_DRIVER_ROLE)) {
                sr.setStatus(false);
                sr.setMessage(messageservice.getMessage("wrong_role",dto.getTaxiId()));
                sr.setInfoId(ErrorNumber.NO_SUPPLIER_NOT_MATCHING);
                return new ResponseEntity<>(sr, HttpStatus.OK);
            }

        }
        else {
            sr.setInfoId(600);
            sr.setMessage(messageservice.getMessage("user_is_not_existing", dto.getUserId()));
            sr.setData(dto.getTaxiId());
        }
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

}
