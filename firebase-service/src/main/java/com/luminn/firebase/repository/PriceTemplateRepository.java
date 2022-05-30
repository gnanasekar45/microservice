package com.luminn.firebase.repository;

import com.luminn.firebase.entity.PriceTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PriceTemplateRepository extends MongoRepository<PriceTemplate, String> {
    public List<PriceTemplate> findBySupplierId(String supplierId);
    public List<PriceTemplate> findByDomain(String domain);
    public List<PriceTemplate> findByRegion(String Region);
    //https://stackoverflow.com/questions/48460066/mongorepository-findbythisandthat-custom-query-with-multiple-parameters
    public List<PriceTemplate> findByDomainAndCategory(String domain,String category);
    public List<PriceTemplate> findByDomainAndCategoryAndRegion(String domain,String category,String region);
    public PriceTemplate findByRegionIgnoreCase(String Region);
}
