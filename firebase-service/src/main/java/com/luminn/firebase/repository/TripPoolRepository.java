package com.luminn.firebase.repository;


import com.luminn.firebase.dto.TripPool;
import com.luminn.firebase.entity.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TripPoolRepository extends MongoRepository<TripPool, String> {

    public TripPool findByTripId(String tripId);
    public Page<TripPool> findBySupplierId(String supplierIds, Pageable pageable);
}
