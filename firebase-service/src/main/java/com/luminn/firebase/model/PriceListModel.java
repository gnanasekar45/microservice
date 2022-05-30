package com.luminn.firebase.model;

import com.luminn.firebase.dto.PriceListDTO;
import com.luminn.firebase.entity.Price;
import com.luminn.firebase.entity.PriceList;
import com.luminn.firebase.repository.PriceListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class PriceListModel {

    @Autowired
    PriceListRepository priceListRepository;

    public PriceList getdriverId(String driverId){
        return priceListRepository.findByDriverId(driverId);
    }
    public void create(PriceListDTO priceListDTO){
        PriceList priceLists = new PriceList();
        priceLists.setDisplay(priceListDTO.getDisplay());
        priceLists.setDriverId(priceListDTO.getDriverId());

        HashMap<String,String> map = priceListDTO.getList();
        //HashMap<String,String> map2 = new  HashMap<String,String>();
        priceLists.setList(map);
        priceListRepository.save(priceLists);
    }

}
