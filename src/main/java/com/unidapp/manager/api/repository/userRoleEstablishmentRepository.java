package com.unidapp.manager.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.unidapp.manager.api.model.UserRoleEstablishment;

public interface userRoleEstablishmentRepository extends MongoRepository<UserRoleEstablishment, String> {
    @Query("{id_usuario: {$eq: :#{#id_usuario} }}")
    List<UserRoleEstablishment> findByUserId(@Param("id_usuario") String id_usuario);

    @Query("{id_rol: {$eq: :#{#id_rol} }}")
    List<UserRoleEstablishment> findByRoleId(@Param("id_rol") String id_rol);

    @Query("{id_establecimiento: {$eq: :#{#id_establecimiento} }}")
    List<UserRoleEstablishment> findByEstablishmentId(@Param("id_establecimiento") String id_establecimiento);

    @Query("{num_inmueble: {$eq: :#{#num_inmueble} }, id_establecimiento: {$eq: :#{#id_establecimiento} }}")
    List<UserRoleEstablishment> findByNum_inmuebleEstablishmentId(@Param("num_inmueble") String num_inmueble, @Param("id_establecimiento") String id_establecimiento);

    @Query("{num_inmueble: {$eq: :#{#num_inmueble} }, id_establecimiento: {$eq: :#{#id_establecimiento} }, id_usuario: {$eq: :#{#id_usuario} }}")
    UserRoleEstablishment findByNum_inmuebleEstablishmentIdUserId(@Param("num_inmueble") String num_inmueble, @Param("id_establecimiento") String id_establecimiento, @Param("id_usuario") String id_usuario);
}
