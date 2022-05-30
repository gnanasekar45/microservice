package com.luminn.firebase.repository;




import com.luminn.firebase.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialRepository extends MongoRepository<User, String> {
    public User findByEmail(String email);
    public User findByPhoneNumber(String phoneNumber);
    public User findBySocialId(String socialId);
    public Page<User> findByDomain(String domain, Pageable pageable);
    public Page<User> findByDomainAndRole(String domain,String role, Pageable pageable);

    public Page<User> findByfirstNameContainingIgnoreCase(String firstName, Pageable pageable);
    public Page<User> findByRoleContainingIgnoreCase(String role, Pageable pageable);

    //List<Person> result = repository.findAll(person.address.zipCode.eq("C0123"));
    //https://bezkoder.com/spring-boot-mongodb-pagination/
    //Page<Tutorial> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
