package com.unidapp.manager.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.unidapp.manager.api.model.PqrsCategory;

public interface pqrsCategoryRepository extends MongoRepository<PqrsCategory, String> {
    @Query("{id_establishment: {$eq: :#{#id_establishment}} }")
    List<PqrsCategory> findByEstablishment(@Param("id_establishment") String id_establishment);

}
