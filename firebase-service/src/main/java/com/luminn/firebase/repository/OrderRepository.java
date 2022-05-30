package com.luminn.firebase.repository;




import com.luminn.firebase.dto.ProductDetail;
import com.luminn.firebase.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    public Order findByItem(String item);
    public List<Order> findByUserId(String userId);
    public List<Order> findByname(String item);

    //List<Person> result = repository.findAll(person.address.zipCode.eq("C0123"));
    //https://bezkoder.com/spring-boot-mongodb-pagination/
    //Page<Tutorial> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
