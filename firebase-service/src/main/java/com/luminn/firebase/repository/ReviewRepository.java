package com.luminn.firebase.repository;


import com.luminn.firebase.entity.Driver;
import com.luminn.firebase.entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface ReviewRepository extends MongoRepository<Review, String> {
    //taxiDetailId
    List<Review> findByTaxiDetailId(String taxiDetailId);
    List<Review> findByRideId(String rideId);
    List<Review> findByPostedBy(String rideId);
}
