package com.unidapp.manager.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.unidapp.manager.api.model.Neighbor;

public interface neighborRepository extends MongoRepository<Neighbor, String> {
    @Query("{id_usuario: {$eq: :#{#id_usuario} }}")
    Neighbor findByUserId(@Param("id_usuario") String id_usuario);

    @Query("{id_establishment: {$eq: :#{#id_establecimiento} }}")
    List<Neighbor> findByEstablishmentId(@Param("id_establecimiento") String id_establecimiento);
}
