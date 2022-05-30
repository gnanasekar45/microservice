package com.luminn.firebase.repository;

import com.luminn.firebase.entity.Driver;
import com.luminn.firebase.entity.PhoneBooking;
import com.luminn.firebase.entity.TaxiDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PhoneBookingRepository extends MongoRepository<PhoneBooking, String> {
    public List<PhoneBooking> findBySupplierId(String supplierId);
    public Page<PhoneBooking> findBySupplierId(String supplierIds, Pageable pageable);
    public List<PhoneBooking> findByDriverId(String driverID);
}
