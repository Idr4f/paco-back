package com.unidapp.manager.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.unidapp.manager.api.model.Pqrs;

public interface pqrsRepository extends MongoRepository<Pqrs, String> {
    @Query("{id_asset: {$eq: :#{#id_asset}} }")
    List<Pqrs> findByAsset(@Param("id_asset") String id_asset);

    @Query("{id_neighbor: {$eq: :#{#id_neighbor}} }")
    List<Pqrs> findByNeighbor(@Param("id_neighbor") String id_neighbor);

    @Query("{id_category: {$eq: :#{#id_category}} }")
    List<Pqrs> findByCategory(@Param("id_category") String id_category);

}
