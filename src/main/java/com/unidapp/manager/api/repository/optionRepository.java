package com.unidapp.manager.api.repository;

import com.unidapp.manager.api.model.Option;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface optionRepository extends MongoRepository<Option, String> {
    
}
