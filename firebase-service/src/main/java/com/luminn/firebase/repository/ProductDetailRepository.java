package com.luminn.firebase.repository;




import com.luminn.firebase.dto.ProductDetail;
import com.luminn.firebase.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends MongoRepository<ProductDetail, String> {
    public ProductDetail findByCategory(String category);
    public ProductDetail findByItem(String item);
    public List<ProductDetail> findByname(String item);

    //List<Person> result = repository.findAll(person.address.zipCode.eq("C0123"));
    //https://bezkoder.com/spring-boot-mongodb-pagination/
    //Page<Tutorial> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
