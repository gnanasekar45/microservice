package com.luminn.driver.repository;

import com.luminn.driver.model.UserOne;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.luminn.driver.model.User;

import javax.annotation.Resource;
import java.util.List;

@Resource
public interface UserRepository extends MongoRepository<UserOne, String> {

    //public void saveAll(Iterable<UserOne>);

    public List<UserOne> getUsersByLocation(double longitude, double latitude, double distance);
}
