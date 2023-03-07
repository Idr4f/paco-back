package com.unidapp.manager.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.unidapp.manager.api.model.Asset;

public interface assetRepository extends MongoRepository<Asset, String> {
    @Query("{num_inmueble: {$eq: :#{#num_inmueble} }, id_establecimiento: {$eq: :#{#id_establecimiento} }}")
    Asset findByNum_inmueble(@Param("num_inmueble") String num_inmueble, @Param("id_establecimiento") String id_establecimiento);

    @Query("{id_establecimiento: {$eq: :#{#id_establecimiento} }}")
    List<Asset> findByEstablishmentId(@Param("id_establecimiento") String id_establecimiento);
}
