package com.luminn.firebase.repository;




import com.luminn.firebase.entity.User;
import com.luminn.firebase.entity.VehicleType;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface VehicleTypeRepository extends MongoRepository<VehicleType, String> {
    public VehicleType findByCode(String email);
}
