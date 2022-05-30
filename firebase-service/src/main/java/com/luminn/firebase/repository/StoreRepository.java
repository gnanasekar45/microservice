package com.luminn.firebase.repository;



import com.luminn.firebase.entity.Store;
import com.luminn.firebase.entity.TaxiDetail;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface StoreRepository extends MongoRepository<Store, String> {

    public List<Store> findByKey(String key);

}
