package com.luminn.firebase.model;

import com.luminn.firebase.entity.PriceTemplate;
import org.springframework.stereotype.Component;

@Component
public class PriceTemplateModel {

    public PriceTemplate createPriceSwiss(PriceTemplate priceTemplate){

        PriceTemplate priceTemplates = new PriceTemplate();

        priceTemplates.setPrice(priceTemplate.getPrice());
        priceTemplates.setCategory(priceTemplate.getCategory());
        priceTemplates.setKm(priceTemplate.getKm());
        priceTemplates.setDomain(priceTemplate.getDomain());
        priceTemplates.setRegion(priceTemplate.getRegion());
        priceTemplates.setSupplierId(priceTemplate.getSupplierId());
        priceTemplates.setDay(priceTemplate.getDay());
        priceTemplates.setHour(priceTemplate.getHour());
        priceTemplates.setZipCode(priceTemplate.getZipCode());
        priceTemplates.setTax(priceTemplate.getTax());
        return priceTemplate;
    }
}
