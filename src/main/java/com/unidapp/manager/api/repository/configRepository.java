package com.unidapp.manager.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unidapp.manager.api.model.Config;

public interface configRepository extends MongoRepository<Config, String> {
    @Query("{name: {$eq: :#{#name} }}")
    Config findByName(@Param("name") String name);
}
