package com.luminn.firebase.repository;



import com.luminn.firebase.entity.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface SupplierRepository extends MongoRepository<Supplier, String> {
    public List<Supplier> findByName(String name);
    public Supplier findByUserId(String userId);

}
