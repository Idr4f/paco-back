package com.unidapp.manager.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.unidapp.manager.api.model.NotificationCategory;

public interface notificationCategoryRepository extends MongoRepository<NotificationCategory, String> {
    @Query("{id_establishment: {$eq: :#{#id_establishment}} }")
    List<NotificationCategory> findByEstablishment(@Param("id_establishment") String id_establishment);

}
