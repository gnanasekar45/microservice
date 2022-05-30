package com.luminn.firebase.repository;


import com.luminn.firebase.entity.DriverPhoto;
import com.luminn.firebase.entity.Ride;
import com.luminn.firebase.entity.TaxiDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface RideRepository extends MongoRepository<Ride, String> {

    //@Query(" { $limit: 2 }")
    public List<Ride> findByDriverIdOrderByStartDateDesc(String id);
    //List<TemperatureObject> temperatureObjects = temperatureRepo.findAll(new Sort(Sort.Direction.DESC, "timestamp"));

    //@Query("{ limit: 2 }")
    public List<Ride> findByUserIdOrderByStartDateDesc(String id);
    public List<Ride> findBySupplierId(String supplierId);

    public Page<Ride> findBySupplierId(String supplierIds, Pageable pageable);
    //Query("{ state : 'ACTIVE' }")
    //Page<Job> findOneActiveOldest(Pageable pageable);

    // Keep that in a constant if it stays the same
    //PageRequest request = new PageRequest(0, 1, new Sort(Sort.Direction.DESC, "created"));
    //Job job = repository.findOneActiveOldest(request).getContent().get(0);

}
