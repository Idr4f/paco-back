package com.unidapp.manager.api.repository;

import java.util.List;

import com.unidapp.manager.api.model.RestoreToken;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

public interface restoreTokenRepository extends MongoRepository<RestoreToken, String> {
    
    @Query("{token: {$eq: :#{#token} }}")
    RestoreToken findByToken(@Param("token") String token);

    @Query("{email: {$eq: :#{#email} }}")
    List<RestoreToken> findByEmail(@Param("email") String email);
}
