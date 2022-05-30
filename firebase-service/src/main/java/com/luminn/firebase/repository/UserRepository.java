package com.luminn.firebase.repository;




import com.luminn.firebase.dto.TripPool;
import com.luminn.firebase.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    public User findByEmail(String email);
    public User findByPhoneNumber(String phoneNumber);
    //public Page<User> findByPhoneNumberContainingIgnoreCase(String phoneNumber);
    public User findBySocialId(String socialId);
    public Page<User> findByDomain(String domain, Pageable pageable);
    public Page<User> findByDomainAndRole(String domain,String role, Pageable pageable);

    public User findByfirstNameContainingIgnoreCase(String firstName);

    public Page<User> findByfirstNameContainingIgnoreCase(String firstName, Pageable pageable);

    public Page<User> findByphoneNumberContainingIgnoreCaseAndDomain(String firstName,String domain,Pageable pageable);
    public Page<User> findByroleContainingIgnoreCase(String role, Pageable pageable);
    public Page<User> findByRoleContainingIgnoreCaseAndDomain(String role,String domain,Pageable pageable);

    public Page<User> findByfirstNameContainingIgnoreCaseAndDomain(String firstName,String domain,Pageable pageable);
    public Page<User> findByfirstNameAndDomain(String firstName,String domain,Pageable pageable);
    //List<Person> result = repository.findAll(person.address.zipCode.eq("C0123"));
    //https://bezkoder.com/spring-boot-mongodb-pagination/
    //Page<Tutorial> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    //https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
}
