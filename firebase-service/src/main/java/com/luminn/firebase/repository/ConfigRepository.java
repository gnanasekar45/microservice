package com.luminn.firebase.repository;



import com.luminn.firebase.entity.ConfigVersion;
import com.luminn.firebase.entity.Store;
import com.luminn.firebase.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ConfigRepository extends MongoRepository<ConfigVersion, String> {
    public ConfigVersion findByDomainContainingIgnoreCase(String domain);
}
