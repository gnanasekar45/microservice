package com.luminn.firebase.repository;

import com.luminn.firebase.entity.ContactMail;
import com.luminn.firebase.entity.DriverBill;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ContactMailRepository extends MongoRepository<ContactMail, String> {
    List<ContactMail> findBySupplierId(String supplierId);
    List<ContactMail> findByPhonenumber(String phoneNumber);
    List<ContactMail> findByEmail(String email);
}