package com.unidapp.manager.api.repository;

import com.unidapp.manager.api.model.Audit;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface auditRepository extends MongoRepository<Audit, String> {
    
}
