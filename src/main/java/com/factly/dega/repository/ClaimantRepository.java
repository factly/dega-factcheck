package com.factly.dega.repository;

import com.factly.dega.domain.Claimant;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data MongoDB repository for the Claimant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClaimantRepository extends MongoRepository<Claimant, String> {

    Optional<Claimant> findByClientIdAndSlug(String clientId, String slug);
}
