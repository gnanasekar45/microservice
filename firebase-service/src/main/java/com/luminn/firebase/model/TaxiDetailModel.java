package com.luminn.firebase.model;

import com.luminn.firebase.entity.TaxiDetail;
import com.luminn.firebase.repository.TaxiDetailRepository;
import com.luminn.firebase.service.TaxiDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaxiDetailModel {

    @Autowired
    TaxiDetailService taxiDetailService;

    @Autowired
    TaxiDetailRepository taxiDetailRepository;

    public TaxiDetail getTaxiId(String taxiId){
        List<TaxiDetail> list = taxiDetailService.findSupplierId(taxiId);

        if(list != null && list.size() > 0){
            for(TaxiDetail td : list){
                return td;
            }
        }
        return null;
    }

    public TaxiDetail getId(String Id){
        return taxiDetailService.findById(Id,null);
    }

    public TaxiDetail findBySupplierId(String Id){
        return taxiDetailService.findBySupplierId(Id);
    }

    public TaxiDetail findByDriverId(String Id){
        return taxiDetailService.findDriverId(Id);
    }

    public TaxiDetail findSupplierIdId(String Id){
        return taxiDetailService.findBySupplierId(Id);
    }
}
