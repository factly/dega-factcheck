package com.factly.dega.repository;

import com.factly.dega.domain.Rating;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data MongoDB repository for the Rating entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RatingRepository extends MongoRepository<Rating, String> {

    Optional<Rating> findByClientIdAndSlug(String clientId, String slug);

}
