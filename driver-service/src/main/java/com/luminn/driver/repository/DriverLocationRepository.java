package com.luminn.driver.repository;

import com.luminn.driver.model.Driver;
import com.luminn.driver.model.DriverPhoto;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DriverLocationRepository extends MongoRepository<Driver, String> {
	List<Driver> findByName(String name);
	@Query("{'longitude': {$gte : ?0, $lte : ?1 }, 'latitude': {$gte: ?2, $lte: ?3}}")
	List<Driver> findByLocation(double lg0, double lg1, double lt0, double lt1);



}
