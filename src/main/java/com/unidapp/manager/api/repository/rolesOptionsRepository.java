package com.unidapp.manager.api.repository;

import java.util.List;

import com.unidapp.manager.api.model.RolesOptions;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

public interface rolesOptionsRepository extends MongoRepository<RolesOptions, String> {
    @Query("{id_rol: {$eq: :#{#id_rol} }}")
    List<RolesOptions> findByRoleId(@Param("id_rol") String id_rol);

    @Query("{id_opcion: {$eq: :#{#id_opcion} }}")
    List<RolesOptions> findByOptionId(@Param("id_opcion") String id_opcion);
}
