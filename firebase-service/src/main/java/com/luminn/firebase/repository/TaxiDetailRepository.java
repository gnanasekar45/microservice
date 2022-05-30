package com.luminn.firebase.repository;



import com.luminn.firebase.entity.TaxiDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaxiDetailRepository extends MongoRepository<TaxiDetail, String> {

    public Page<TaxiDetail> findBySupplierIds(String supplierIds,Pageable pageable);
    public TaxiDetail findByDriverId(String driverId);
    public List<TaxiDetail> findByTaxiId(String taxiId);
    //Page<TaxiDetail> findByPublished(boolean published, Pageable pageable);
}
