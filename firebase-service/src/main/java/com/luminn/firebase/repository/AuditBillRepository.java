package com.luminn.firebase.repository;

import com.luminn.firebase.entity.AuditBill;
import com.luminn.firebase.entity.DriverBill;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AuditBillRepository extends MongoRepository<AuditBill, String> {

    List<DriverBill> findByDriverId(String driverID);
    List<DriverBill> findBySupplierId(String supplierId);
}
