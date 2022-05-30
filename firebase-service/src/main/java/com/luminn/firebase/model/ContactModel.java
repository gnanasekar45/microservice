package com.luminn.firebase.model;


import com.luminn.firebase.dto.ContactDTO;
import com.luminn.firebase.entity.ContactMail;
import com.luminn.firebase.entity.TaxiDetail;
import com.luminn.firebase.helper.Converter;
import com.luminn.firebase.repository.ContactMailRepository;
import com.luminn.firebase.repository.TaxiDetailRepository;
import com.luminn.firebase.repository.TaxiRepository;
import com.luminn.firebase.service.ContactService;
import com.luminn.firebase.service.TaxiDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContactModel {

    @Autowired
    ContactService contactService;

    @Autowired
    TaxiDetailService  taxiDetailService;

    @Autowired
    ContactMailRepository contactMailRepository;
    private static final Logger log = LoggerFactory.getLogger(EmailModel.class);

    public String create(ContactDTO contactDTO) {


        TaxiDetail taxiDetail = taxiDetailService.findTaxiId(contactDTO.getTaxiId());

        if(taxiDetail != null && taxiDetail.getId() != null) {
            ContactMail contactMail = Converter.dtoToEntity(contactDTO);
            contactMail.setSupplierId(taxiDetail.getSupplierId());
            contactMailRepository.save(contactMail);
        }
        else {
            ContactMail contactMail = Converter.dtoToEntity(contactDTO);
            contactMailRepository.save(contactMail);
        }

        //contactService.createWithID(contactMail);
        //return CityStatus.CREATED;
        return "created";
    }

    public List<ContactMail> get() {
        //ContactMail contactMail = Converter.dtoToEntity(contactDTO);
      return  contactMailRepository.findAll();
    }
    public List<ContactMail> getMailBySupplier(String supplierId) {
        //ContactMail contactMail = Converter.dtoToEntity(contactDTO);
        return  contactMailRepository.findBySupplierId(supplierId);
    }

    public List<ContactMail> getMailByPhoneNumber(String phoneNumber) {
        //ContactMail contactMail = Converter.dtoToEntity(contactDTO);
        return  contactMailRepository.findByPhonenumber(phoneNumber);
    }


    public List<ContactMail> getMailByMail(String phoneNumber) {
        //ContactMail contactMail = Converter.dtoToEntity(contactDTO);
        return  contactMailRepository.findByEmail(phoneNumber);
    }
}
