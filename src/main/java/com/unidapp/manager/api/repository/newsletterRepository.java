package com.unidapp.manager.api.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.unidapp.manager.api.model.Newsletter;

public interface newsletterRepository extends MongoRepository<Newsletter, String> {
    @Query("{id_establishment: {$eq: :#{#id_establishment} }, type: {$eq: :#{#type} }}")
    List<Newsletter> findByEstablishment(@Param("id_establishment") String id_establishment, @Param("type") String type, Sort sort);
}
