package com.luminn.firebase.repository;

import com.luminn.firebase.entity.Coupon;
import com.luminn.firebase.entity.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WalletRepository extends MongoRepository<Wallet, String> {

    public Wallet findByUserId(String userId);
    public List<Wallet> findByToken(String token);
}