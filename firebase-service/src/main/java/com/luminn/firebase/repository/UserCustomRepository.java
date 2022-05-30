package com.luminn.firebase.repository;

import com.luminn.firebase.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserCustomRepository {
    public List<User> findUserByProperties(String supplierId,String role,String firstName, Pageable page);
}
