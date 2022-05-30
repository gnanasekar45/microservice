package com.luminn.firebase.repository;

import com.luminn.firebase.entity.DriverBill;
import com.luminn.firebase.entity.Ride;
import com.luminn.firebase.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DriverBillRepository extends MongoRepository<DriverBill, String> {

    Optional<DriverBill> findByDriverId(String driverID);
    List<DriverBill> findBySupplierId(String supplierId);
    public Page<DriverBill> findBySupplierId(String supplierIds, Pageable pageable);
    public Page<DriverBill> findByDriverId(String supplierIds, Pageable pageable);

}
