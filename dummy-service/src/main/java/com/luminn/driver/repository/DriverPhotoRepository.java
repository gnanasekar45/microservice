package com.luminn.driver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.luminn.driver.model.DriverPhoto;

public interface DriverPhotoRepository extends MongoRepository<DriverPhoto, String> {
	DriverPhoto findByDriverName(String name);
}
