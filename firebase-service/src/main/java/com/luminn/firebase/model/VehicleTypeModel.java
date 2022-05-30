package com.luminn.firebase.model;

import com.luminn.firebase.dto.VehicleTypeDTO;
import com.luminn.firebase.entity.VehicleType;
import com.luminn.firebase.helper.Converter;
import com.luminn.firebase.repository.VehicleTypeRepository;
import com.luminn.firebase.service.MessageByLocaleService;
import com.luminn.firebase.service.VehicleTypeService;
import com.luminn.firebase.util.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by ch on 2/10/2016.
 */
@Component
public class VehicleTypeModel {

    @Autowired
    VehicleTypeService vehicleTypeService;

    @Autowired
    private MessageByLocaleService messageservice;

    @Autowired
    VehicleTypeRepository vehicleTypeRepository;


    public Long vehicleTypeId;
    public ModelStatus create(VehicleTypeDTO dto){

        if(!Validation.isEmptyLang(dto.getLang()))
            dto.setLang(Path.LANG.lang);

        VehicleType isExisting = vehicleTypeRepository.findByCode(dto.getCode());
        if(isExisting == null) {
            VehicleType vehicleType = Converter.toEntity(dto,null);
             vehicleTypeRepository.save(vehicleType);
            return ModelStatus.CREATED;
        }
        else
            return ModelStatus.EXISTS;

    }
    public List<VehicleType> listAllVehicle(){
        return vehicleTypeRepository.findAll();
    }

   /* public List<VehicleTypeDTO> listAllVehicleByLang(String lang) {
        List<VehicleType> lVehicleType =  vehicleTypeService.listAllVehicleByLang(lang);
        List<VehicleTypeDTO> newDTOList = new ArrayList<VehicleTypeDTO>();
        if(lVehicleType != null){
            for(VehicleType type  : lVehicleType){
                  VehicleType orginal = getDefaultCode(type.getCode());
                   //orginal.getId()
                    VehicleTypeDTO dto =  new VehicleTypeDTO(orginal.getId(),type.getCode(),type.getName());
                    dto.setDescription(type.getDescription());
                    dto.setLang(type.getLang());
                    newDTOList.add(dto);
            }
        }
        return newDTOList;
    }*/

    public VehicleType getDefaultCode(String code){
        VehicleType vehicleType = vehicleTypeRepository.findByCode(code);
        return vehicleType;
    }
    public VehicleType getDefaultCode(String code,String lang){
        VehicleType vehicleType = vehicleTypeRepository.findByCode(code);
        return vehicleType;
    }

   /* public String getVehicleBrand(long taxiId){
        TaxiDetail taxiDetail = taxiDetailService.getTaxiById(taxiId);
        return taxiDetail.getVehicleBrand();
    }*/

    //old
    public List<VehicleTypeDTO> listAllVehicle(String lang){
        List<VehicleType> vehicleTypelist =  vehicleTypeRepository.findAll();
        List<VehicleTypeDTO> list = new ArrayList<VehicleTypeDTO>(0);

        for(VehicleType  vehicleType : vehicleTypelist){
            if(lang.equalsIgnoreCase(vehicleType.getLang())) {
                list.add(Converter.toDTO(vehicleType,null));
            }
        }
        return list;
    }


    public VehicleType findCodeByLang(String code,String lang){
       return vehicleTypeRepository.findByCode(code);
    }
    public VehicleType  get(String id) {
       Optional<VehicleType> VehicleType =  vehicleTypeRepository.findById(id);
        VehicleType.isPresent();
        return VehicleType.get();

    }
}
