package com.unidapp.manager.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unidapp.manager.api.model.Establishment;

public interface establishmentRepository extends MongoRepository<Establishment, String> {
    @Query("{cod_establec: {$eq: :#{#cod_establec} }}")
    Establishment findByCod_establec(@Param("cod_establec") String cod_establec);
}
