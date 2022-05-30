package com.luminn.firebase.impl;

import com.luminn.firebase.entity.User;
import com.luminn.firebase.repository.UserCustomRepository;
import com.luminn.firebase.repository.UserFilterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserFilterImpRepository implements UserCustomRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<User> findUserByProperties(String domain, String role, String firstName, Pageable page) {
        //return null;

        final Query query = new Query().with(page);
        query.fields().include("supplierId");

        final List<Criteria> criteria = new ArrayList<>();
        if (role != null && !role.isEmpty())
            criteria.add(Criteria.where("role").is(role));
        if (domain != null && !domain.isEmpty())
            criteria.add(Criteria.where("domain").is(domain));


        if (!criteria.isEmpty())
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
        return mongoTemplate.find(query, User.class);
    }
}
