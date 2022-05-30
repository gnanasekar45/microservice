package com.luminn.firebase.repository;




import com.luminn.firebase.entity.Order;
import com.luminn.firebase.entity.OrderBuy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderBuyRepository extends MongoRepository<OrderBuy, String> {

    public OrderBuy findByOrderId(String item);
    public List<OrderBuy> findByUserId(String userId);
    public List<OrderBuy> findByname(String item);

    //List<Person> result = repository.findAll(person.address.zipCode.eq("C0123"));
    //https://bezkoder.com/spring-boot-mongodb-pagination/
    //Page<Tutorial> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
