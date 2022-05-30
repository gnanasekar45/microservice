package com.luminn.firebase.repository;


import com.luminn.firebase.dto.Taxi;
import com.luminn.firebase.entity.TaxiDetail;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaxiRepository extends MongoRepository<Taxi, String> {

    public GeoResults<Taxi> findByLocationNear(Point location, Distance distance);
}
