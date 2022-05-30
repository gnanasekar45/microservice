package com.luminn.firebase.repository;

import com.luminn.firebase.entity.Coupon;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CouponRepository extends MongoRepository<Coupon, String> {
    public Coupon findByCoupan(String coupan);
    public List<Coupon> findBySupplierId(String coupan);
}