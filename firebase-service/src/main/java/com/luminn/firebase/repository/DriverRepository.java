package com.luminn.firebase.repository;


import java.util.List;

import com.luminn.firebase.entity.Driver;
import com.luminn.firebase.entity.DriverPhoto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;



public interface DriverRepository extends MongoRepository<Driver, String> {
    //Driver findByDriverName(String name);

}
