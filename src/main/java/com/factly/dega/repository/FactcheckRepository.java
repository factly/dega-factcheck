package com.factly.dega.repository;

import com.factly.dega.domain.Factcheck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Factcheck entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactcheckRepository extends MongoRepository<Factcheck, String> {
    @Query("{}")
    Page<Factcheck> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Factcheck> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Factcheck> findOneWithEagerRelationships(String id);

    Optional<Factcheck> findByClientIdAndSlug(String clientId, String slug);

}
