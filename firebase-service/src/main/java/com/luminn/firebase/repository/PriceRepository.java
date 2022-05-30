package com.luminn.firebase.repository;

import com.luminn.firebase.entity.Price;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PriceRepository extends MongoRepository<Price, String> {
    public List<Price> findBySupplierId(String supplierId);
    public List<Price> findByDomain(String domain);
    public List<Price> findByRegion(String Region);
    //https://stackoverflow.com/questions/48460066/mongorepository-findbythisandthat-custom-query-with-multiple-parameters
    public List<Price> findByDomainAndCategory(String domain,String category);
    public List<Price> findByDomainAndCategoryAndRegion(String domain,String category,String region);
    public Price findByRegionIgnoreCase(String Region);
}
