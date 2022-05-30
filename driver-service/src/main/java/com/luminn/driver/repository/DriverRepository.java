package com.luminn.driver.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.luminn.driver.model.Driver;

public interface DriverRepository extends MongoRepository<Driver, String> {
	List<Driver> findByName(String name);
	@Query("{'longitude': {$gte : ?0, $lte : ?1 }, 'latitude': {$gte: ?2, $lte: ?3}}")
	List<Driver> findByLocation(double lg0, double lg1, double lt0, double lt1);


}
