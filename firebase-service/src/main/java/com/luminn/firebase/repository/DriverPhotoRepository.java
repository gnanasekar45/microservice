package com.luminn.firebase.repository;


import com.luminn.firebase.entity.DriverPhoto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DriverPhotoRepository extends MongoRepository<DriverPhoto, String> {
	DriverPhoto findByDriverName(String name);
	List<DriverPhoto> findByUserId(String userId);
	List<DriverPhoto> findByCituUserId(String userId);
}
