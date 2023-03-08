package com.unidapp.manager.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unidapp.manager.api.model.User;

public interface userRepository extends MongoRepository<User, String> {
    @Query("{email: {$eq: :#{#username} }}")
    User findByUsername(@Param("username") String username);
}