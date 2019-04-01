package com.factly.dega.repository;

import com.factly.dega.domain.Claim;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data MongoDB repository for the Claim entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClaimRepository extends DegaCustomRepository<Claim, String> {

    Optional<Claim> findByClientIdAndSlug(String clientId, String slug);

}
