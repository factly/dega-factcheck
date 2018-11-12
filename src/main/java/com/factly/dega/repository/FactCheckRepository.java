package com.factly.dega.repository;

import com.factly.dega.domain.FactCheck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the FactCheck entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactCheckRepository extends MongoRepository<FactCheck, String> {
    @Query("{}")
    Page<FactCheck> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<FactCheck> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<FactCheck> findOneWithEagerRelationships(String id);

}
