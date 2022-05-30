package com.luminn.driver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.luminn.driver.model.User;

public interface UserRepository extends MongoRepository<User, String> {

}
