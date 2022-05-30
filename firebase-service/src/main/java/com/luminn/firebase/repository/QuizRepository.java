package com.luminn.firebase.repository;

import com.luminn.firebase.entity.Price;
import com.luminn.firebase.entity.Quiz;
import com.luminn.firebase.entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuizRepository extends MongoRepository<Quiz, String> {
    public List<Quiz> findByCategory(String category);
}
