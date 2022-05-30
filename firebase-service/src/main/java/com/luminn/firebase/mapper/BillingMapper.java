package com.luminn.firebase.mapper;

import com.luminn.firebase.dto.DriverBillingDetailsDTO;
import com.luminn.firebase.entity.DriverBill;
import com.luminn.firebase.entity.User;
import com.luminn.firebase.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BillingMapper {

    @Autowired
    UserModel userModel;

    public static List<DriverBillingDetailsDTO> encodedriverBillingDetailsDTO(List<DriverBill> driverBill){
        List<DriverBillingDetailsDTO> driverBillinDetails = new ArrayList<>();
        driverBill.forEach(detailList ->driverBillinDetails.add(decode(detailList)));
        return driverBillinDetails;
    }

    public static DriverBillingDetailsDTO decode(DriverBill bill){

        DriverBillingDetailsDTO dto = new DriverBillingDetailsDTO();

        //DriverBillingDetailsDTO dto = (DriverBillingDetailsDTO) Converter.entityToDto(bill);

        /*User user = userModel.getById(bill.getDriverId());
        if(user != null){
            if(user.getFirstName() != null && !"".equalsIgnoreCase(user.getFirstName()))
                dto.setDriverName(user.getFirstName());
            else
                dto.setDriverName(user.getLastName());
            dto.setPhoneNumber(user.getPhoneNumber());
        }*/

        return dto;
    }
    public void getUser(){

    }

    //public static
    /*  List<DriverBillingDetailsDTO> list = new ArrayList<>();
        for(DriverBill bill:driverBill){
            DriverBillingDetailsDTO dto = (DriverBillingDetailsDTO) Converter.entityToDto(bill);
            User user = userModel.getById(bill.getDriverId());
            if(user != null){
                if(user.getFirstName() != null && !"".equalsIgnoreCase(user.getFirstName()))
                    dto.setDriverName(user.getFirstName());
                else
                    dto.setDriverName(user.getLastName());
                dto.setPhoneNumber(user.getPhoneNumber());
            }
            list.add(dto);
        }*/

}
