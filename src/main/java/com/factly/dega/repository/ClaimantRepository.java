package com.factly.dega.repository;

import com.factly.dega.domain.Claimant;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Claimant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClaimantRepository extends MongoRepository<Claimant, String> {

}
