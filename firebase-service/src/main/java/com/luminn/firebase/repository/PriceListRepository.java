package com.luminn.firebase.repository;


import com.luminn.firebase.entity.PriceList;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface PriceListRepository extends MongoRepository<PriceList, String> {
    public PriceList findByDriverId(String driverID);
}
