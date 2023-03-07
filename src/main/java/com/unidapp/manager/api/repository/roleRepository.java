package com.unidapp.manager.api.repository;

import com.unidapp.manager.api.model.Role;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

public interface roleRepository extends MongoRepository<Role, String> {
    
    @Query("{nom_rol: {$eq: :#{#nom_rol} }}")
    Role findByNom_rol(@Param("nom_rol") String nom_rol);
}
